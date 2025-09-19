package app.ui;

import app.models.User;
import app.models.Account;
import app.services.TransactionService;
import app.services.AccountService;
import app.repositories.TransactionRepository;
import app.utils.ValidationUtils;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Interface utilisateur pour la gestion des transactions bancaires
 * Adaptée au TransactionService existant
 */
public class TransactionView {
    
    private Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private AccountService accountService;
    private TransactionRepository transactionRepository;

    public TransactionView(User user, AccountService accountService, TransactionRepository transactionRepository) {
        this.currentUser = user;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }
    
    /**
     * Affiche le menu principal des transactions
     * @return true si retour au dashboard demandé, false pour continuer
     */
    public boolean showTransactionMenu() {
        boolean managing = true;
        
        while (managing) {
            displayTransactionHeader();
            int choice = displayTransactionOptions();
            
            switch (choice) {
                case 1:
                    effectuerDepot();
                    break;
                case 2:
                    effectuerRetrait();
                    break;
                case 3:
                    effectuerVirement();
                    break;
                case 4:
                    consulterHistorique();
                    break;
                case 5:
                    managing = false; // Retour au dashboard
                    break;
                default:
                    System.out.println("Choix invalide! Choisissez entre 1-5");
            }
        }
        return true;
    }
    
    private void displayTransactionHeader() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           GESTION DES TRANSACTIONS");
        System.out.println("=".repeat(50));
        System.out.println("Utilisateur: " + currentUser.getFullName());
        System.out.println("Comptes actifs: " + accountService.getActiveAccountCount(currentUser.getId()));
        System.out.println("=".repeat(50));
    }
    
    private int displayTransactionOptions() {
        System.out.println("\nOPÉRATIONS DISPONIBLES:");
        System.out.println("1. Effectuer un dépôt");
        System.out.println("2. Effectuer un retrait");
        System.out.println("3. Effectuer un virement");
        System.out.println("4. Consulter historique des transactions");
        System.out.println("5. Retour au menu principal");
        System.out.println("-".repeat(40));
        System.out.print("Votre choix: ");
        
        String input = scanner.nextLine().trim();
        return ValidationUtils.parseIntSafely(input, -1);
    }
    
    /**
     * Gère l'opération de dépôt
     */
    private void effectuerDepot() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           EFFECTUER UN DÉPÔT");
        System.out.println("=".repeat(40));
        
        if (!accountService.hasActiveAccounts(currentUser.getId())) {
            System.out.println("Aucun compte actif disponible!");
            System.out.println("Créez d'abord un compte dans la gestion des comptes.");
            return;
        }
        
        // Sélectionner le compte
        Account selectedAccount = selectAccount("dépôt");
        if (selectedAccount == null) {
            return;
        }
        
        // Saisir le montant
        BigDecimal montant = saisirMontant("dépôt");
        if (montant == null) {
            return;
        }
        
        // Effectuer le dépôt
        try {
            // Créer TransactionService selon le constructeur original
            TransactionService transactionService = new TransactionService(
                transactionRepository, 
                currentUser, 
                selectedAccount, 
                null  // Transaction sera créée dans le service
            );
            
            transactionService.deposer(currentUser.getId(), selectedAccount.getId(), montant);
            System.out.println("\nDépôt traité.");
            System.out.println("Nouveau solde: " + selectedAccount.getSolde().setScale(2) + "€");
        } catch (Exception e) {
            System.out.println("Erreur lors du dépôt: " + e.getMessage());
        }
    }
    
    /**
     * Gère l'opération de retrait (à implémenter dans TransactionService)
     */
    private void effectuerRetrait() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("          EFFECTUER UN RETRAIT");
        System.out.println("=".repeat(40));
        
        // Vérifier si l'utilisateur a des comptes
        if (!accountService.hasActiveAccounts(currentUser.getId())) {
            System.out.println("Aucun compte actif disponible!");
            return;
        }
        
        // Sélectionner le compte
        Account selectedAccount = selectAccount("retrait");
        if (selectedAccount == null) {
            return;
        }
        
        // Afficher le solde actuel
        System.out.println("Solde actuel: " + selectedAccount.getSolde().setScale(2) + "€");
        
        // Saisir le montant
        BigDecimal montant = saisirMontant("retrait");
        if (montant == null) {
            return;
        }
        
        // Vérifier la disponibilité des fonds
        if (selectedAccount.getSolde().compareTo(montant) < 0) {
            System.out.println("Erreur: Fonds insuffisants!");
            System.out.println("Solde disponible: " + selectedAccount.getSolde().setScale(2) + "€");
            return;
        }
        
        // TODO: Implémenter retrait dans TransactionService
        System.out.println("Fonction de retrait à implémenter dans TransactionService");
    }
    
    /**
     * Gère l'opération de virement (à implémenter dans TransactionService)
     */
    private void effectuerVirement() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         EFFECTUER UN VIREMENT");
        System.out.println("=".repeat(40));
        
        // TODO: Implémenter virement dans TransactionService
        System.out.println("Fonction de virement à implémenter dans TransactionService");
    }
    
    /**
     * Consulte l'historique des transactions
     */
    private void consulterHistorique() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        HISTORIQUE DES TRANSACTIONS");
        System.out.println("=".repeat(50));
        
        // Vérifier si l'utilisateur a des comptes
        if (!accountService.hasActiveAccounts(currentUser.getId())) {
            System.out.println("Aucun compte disponible!");
            return;
        }
        
        // Sélectionner le compte
        Account selectedAccount = selectAccount("consultation");
        if (selectedAccount == null) {
            return;
        }
        
        try {
            // Créer TransactionService selon le constructeur original
            TransactionService transactionService = new TransactionService(
                transactionRepository, 
                currentUser, 
                selectedAccount, 
                null  // Transaction sera créée dans le service
            );
            
            transactionService.consultezTransaction(currentUser.getId(), selectedAccount.getId());
        } catch (Exception e) {
            System.out.println("Erreur lors de la consultation: " + e.getMessage());
        }
    }
    
    /**
     * Permet à l'utilisateur de sélectionner un compte parmi ses comptes actifs
     * @param operation Le type d'opération (pour affichage)
     * @return Le compte sélectionné ou null si annulation
     */
    private Account selectAccount(String operation) {
        var accounts = currentUser.getAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("Aucun compte disponible pour " + operation);
            return null;
        }
        
        System.out.println("\nSélectionnez un compte pour " + operation + ":");
        System.out.println("-".repeat(40));
        
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getType() + 
                             " (ID: " + account.getId() + ")");
            System.out.println("   Solde: " + account.getSolde().setScale(2) + "€");
        }
        
        System.out.println((accounts.size() + 1) + ". Annuler");
        System.out.print("Votre choix: ");
        
        String input = scanner.nextLine().trim();
        int choix = ValidationUtils.parseIntSafely(input, -1);
        
        if (choix == accounts.size() + 1) {
            System.out.println("Opération annulée.");
            return null;
        }
        
        if (!ValidationUtils.isInRange(choix, 1, accounts.size())) {
            System.out.println("Choix invalide!");
            return null;
        }
        
        return accounts.get(choix - 1);
    }
    
    /**
     * Saisie sécurisée d'un montant avec validation
     * @param operation Le type d'opération (pour affichage)
     * @return Le montant saisi ou null si erreur/annulation
     */
    private BigDecimal saisirMontant(String operation) {
        System.out.print("\nMontant pour " + operation + " (ex: 50.00): ");
        String montantStr = scanner.nextLine().trim();
        
        if (montantStr.isEmpty()) {
            System.out.println("Opération annulée.");
            return null;
        }
        
        try {
            BigDecimal montant = new BigDecimal(montantStr).setScale(2, RoundingMode.HALF_UP);
            
            if (!ValidationUtils.isValidMontant(montant)) {
                System.out.println(ValidationUtils.ErrorMessages.INVALID_MONTANT);
                return null;
            }
            
            return montant;
            
        } catch (NumberFormatException e) {
            System.out.println("Format de montant invalide! Utilisez le format: 50.00");
            return null;
        }
    }
}