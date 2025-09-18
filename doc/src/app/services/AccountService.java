package app.services;

import app.models.Account;
import app.models.User;
import app.repositories.AccountInterface;
import app.repositories.AuthInterface;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountService {
    private AccountInterface accountRepository;
    private AuthInterface authRepository;
    
    public AccountService(AccountInterface accountRepository, AuthInterface authRepository) {
        this.accountRepository = accountRepository;
        this.authRepository = authRepository;
    }
    
    // Créer un nouveau compte bancaire
    public boolean createAccount(UUID userId, String accountType) {
        User user = authRepository.findById(userId.toString());
        if (user == null) {
            return false;
        }
        
        if (!isValidAccountType(accountType)) {
            throw new IllegalArgumentException("Type de compte invalide: " + accountType);
        }
        
        // Créer le nouveau compte
        UUID accountId = UUID.randomUUID();
        Account newAccount = new Account(accountId, accountType, BigDecimal.ZERO);
        
        accountRepository.save(newAccount);
        
        user.addAccount(newAccount);
        
        authRepository.save(user);
        
        return true;
    }

    public int getActiveAccountCount(UUID userId) {
        java.util.List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts == null)
            return 0;
        return accounts.size();
    }

    public boolean hasActiveAccounts(UUID userId){
        if(getActiveAccountCount(userId) > 0)
            return true;
        else
            return false;
    }
    // Supprimer un compte
    public boolean deleteAccount(UUID userId, UUID accountId) {
        User user = authRepository.findById(userId.toString());
        if (user == null) {
            return false;
        }
        
        Account accountToDelete = user.getAccountById(accountId);
        if (accountToDelete == null) {
            return false; // Compte introuvable
        }
        
        // Validation métier : ne pas supprimer un compte avec solde positif
        if (accountToDelete.getSolde().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Impossible de supprimer un compte avec un solde positif!");
        }
        
        // Supprimer du repository
        accountRepository.delete(accountToDelete);
        
        // Supprimer de l'utilisateur
        user.removeAccount(accountToDelete);
        
        // Mettre à jour l'utilisateur
        authRepository.save(user);
        
        return true;
    }
    
    // Effectuer un dépôt
    public boolean deposer(UUID userId, UUID accountId, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif!");
        }
        
        User user = authRepository.findById(userId.toString());
        if (user == null) {
            return false;
        }
        
        Account account = user.getAccountById(accountId);
        if (account == null) {
            return false;
        }
        
        // Effectuer le dépôt directement sur le solde
        BigDecimal nouveauSolde = account.getSolde().add(montant);
        account.setSolde(nouveauSolde);
        
        // Sauvegarder les modifications
        accountRepository.save(account);
        authRepository.save(user);
        
        return true;
    }
    
    // Effectuer un retrait
    public boolean retirer(UUID userId, UUID accountId, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif!");
        }
        
        User user = authRepository.findById(userId.toString());
        if (user == null) {
            return false;
        }
        
        Account account = user.getAccountById(accountId);
        if (account == null) {
            return false;
        }
        
        // Vérifier le solde suffisant
        if (account.getSolde().compareTo(montant) < 0) {
            throw new IllegalStateException("Solde insuffisant!");
        }
        
        // Effectuer le retrait directement sur le solde
        BigDecimal nouveauSolde = account.getSolde().subtract(montant);
        account.setSolde(nouveauSolde);
        
        // Sauvegarder les modifications
        accountRepository.save(account);
        authRepository.save(user);
        
        return true;
    }
    
    // Validation des types de comptes
    private boolean isValidAccountType(String accountType) {
        return accountType.equals("Compte Courant") ||
               accountType.equals("Compte Épargne") ||
               accountType.equals("Compte Bloqué") ||
               accountType.equals("Compte Professionnel");
    }
}