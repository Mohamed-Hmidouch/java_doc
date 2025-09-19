package app.ui;

import app.models.User;
import app.models.Account;
import app.services.TransactionService;
import app.services.AccountService;
import app.repositories.TransactionRepository;
import app.repositories.AccountRepository;
import app.utils.ValidationUtils;
import java.util.Scanner;
import java.util.UUID;
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
    private AccountRepository accountRepository;

    public TransactionView(User user, AccountService accountService, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.currentUser = user;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }
    
    /**
     * Crée une instance de TransactionService avec les paramètres appropriés
     * @param selectedAccount Le compte sélectionné pour l'opération
     * @return Instance de TransactionService
     */
    private TransactionService createTransactionService(Account selectedAccount) {
        return new TransactionService(
            transactionRepository,
            currentUser,
            selectedAccount,
            null,
            accountRepository
        );
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
        
        Account selectedAccount = selectAccount("dépôt");
        if (selectedAccount == null) {
            return;
        }
        
        BigDecimal montant = saisirMontant("dépôt");
        if (montant == null) {
            return;
        }
        
        TransactionService transactionService = createTransactionService(selectedAccount);
        transactionService.deposer(currentUser.getId(), selectedAccount.getId(), montant);
    }
    
    private void effectuerRetrait() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("          EFFECTUER UN RETRAIT");
        System.out.println("=".repeat(40));
        
        Account selectedAccount = selectAccount("retrait");
        if (selectedAccount == null) {
            return;
        }
        
        System.out.println("Solde actuel: " + selectedAccount.getSolde().setScale(2) + "€");
        
        BigDecimal montant = saisirMontant("retrait");
        if (montant == null) {
            return;
        }
        
        TransactionService transactionService = createTransactionService(selectedAccount);
        transactionService.withdraw(currentUser.getId(), selectedAccount.getId(), montant);
    }
    

    private void effectuerVirement() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         EFFECTUER UN VIREMENT");
        System.out.println("=".repeat(40));

        Account selectedAccount = selectAccount("virement (compte source)");
        if (selectedAccount == null) {
            return;
        }

        System.out.println("Solde actuel: " + selectedAccount.getSolde().setScale(2) + "€");

        System.out.print("\nSaisissez l'ID du compte destinataire: ");
        String destinataireIdStr = scanner.nextLine().trim();
        
        if (destinataireIdStr.isEmpty()) {
            System.out.println("Opération annulée.");
            return;
        }
        
        UUID destinataireId;
        try {
            destinataireId = UUID.fromString(destinataireIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Format d'ID invalide!");
            return;
        }

        BigDecimal montant = saisirMontant("virement");
        if (montant == null) {
            return;
        }

        TransactionService transactionService = createTransactionService(selectedAccount);
        transactionService.transfer(currentUser.getId(), selectedAccount.getId(), destinataireId, montant);
    }
    
    /**
     * Consulte l'historique des transactions
     */
    private void consulterHistorique() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        HISTORIQUE DES TRANSACTIONS");
        System.out.println("=".repeat(50));
        
        Account selectedAccount = selectAccount("consultation");
        if (selectedAccount == null) {
            return;
        }
        
        TransactionService transactionService = createTransactionService(selectedAccount);
        transactionService.consultezTransaction(currentUser.getId(), selectedAccount.getId());
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
     * Saisie sécurisée d'un montant
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
            return new BigDecimal(montantStr).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            System.out.println("Format de montant invalide! Utilisez le format: 50.00");
            return null;
        }
    }
}