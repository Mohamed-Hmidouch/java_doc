package app.ui;

import app.models.User;
import app.models.Account;
import app.services.AuthService;
import app.services.AccountService;
import java.util.Scanner;
import java.util.UUID;
import java.math.BigDecimal;

public class UserDashboard {

    private Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private AuthService authService;
    private AccountService accountService;

    public UserDashboard(User user, AuthService authService, AccountService accountService) {
        this.currentUser = user;
        this.authService = authService;
        this.accountService = accountService;
    }
    
    public boolean showDashboard() {
        boolean running = true;
        
        while (running) {
            displayUserInfo();
            int choice = displayMenu();
            
            switch (choice) {
                case 1:
                    editProfile();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    showAccountDetails();
                    break;
                case 4:
                    manageAccounts();
                    break;
                case 5:
                    showHelp();
                    break;
                case 6:
                    if (confirmLogout()) {
                        authService.logout(currentUser.getId().toString());
                        System.out.println("Déconnexion réussie!");
                        return true; // Logout requested
                    }
                    break;
                default:
                    System.out.println("Choix invalide! Veuillez réessayer.");
            }
        }
        return false;
    }

    private void displayUserInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    TABLEAU DE BORD - " + currentUser.getFullName().toUpperCase());
        System.out.println("=".repeat(50));
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Adresse: " + currentUser.getAdress());
        System.out.println("Statut: " + (currentUser.isLoggedIn() ? "Connecté" : "Déconnecté"));
        System.out.println("=".repeat(50));
    }

    private int displayMenu() {
        System.out.println("\nACTIONS DISPONIBLES:");
        System.out.println("1. Modifier profil");
        System.out.println("2. Changer mot de passe");
        System.out.println("3. Détails du compte");
        System.out.println("4. Gérer comptes bancaires");
        System.out.println("5. Aide");
        System.out.println("6. Se déconnecter");
        System.out.println("-".repeat(30));
        System.out.print("Votre choix: ");
        
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void editProfile() {
        System.out.println("\n--- Modifier Profil ---");
        
        System.out.print("Nouveau nom complet (actuel: " + currentUser.getFullName() + "): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            currentUser.setFullName(newName);
        }
        
        System.out.print("Nouvelle adresse (actuelle: " + currentUser.getAdress() + "): ");
        String newAddress = scanner.nextLine().trim();
        if (!newAddress.isEmpty()) {
            currentUser.setAdress(newAddress);
        }
        
        authService.updateUser(currentUser);
        System.out.println("Profil mis à jour avec succès!");
    }

    private void changePassword() {
        System.out.println("\n--- Changer Mot de Passe ---");
        
        System.out.print("Mot de passe actuel: ");
        String currentPassword = scanner.nextLine().trim();
        
        if (!currentPassword.equals(currentUser.getPassword())) {
            System.out.println("Mot de passe actuel incorrect!");
            return;
        }
        
        System.out.print("Nouveau mot de passe (min 6 caractères): ");
        String newPassword = scanner.nextLine().trim();
        
        if (newPassword.length() < 6) {
            System.out.println("Le mot de passe doit contenir au moins 6 caractères!");
            return;
        }
        
        System.out.print("Confirmer le nouveau mot de passe: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Les mots de passe ne correspondent pas!");
            return;
        }
        
        currentUser.setPassword(newPassword);
        authService.updateUser(currentUser);
        System.out.println("Mot de passe changé avec succès!");
    }

    private void showAccountDetails() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        DÉTAILS DU COMPTE");
        System.out.println("=".repeat(40));
        System.out.println("ID: " + currentUser.getId());
        System.out.println("Nom: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Adresse: " + currentUser.getAdress());
        System.out.println("Statut: " + (currentUser.isLoggedIn() ? "Connecté" : "Déconnecté"));
        System.out.println("Session: Active");
        System.out.println("=".repeat(40));
    }

    private void showHelp() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           GUIDE D'AIDE");
        System.out.println("=".repeat(40));
        System.out.println("1. Modifier Profil:");
        System.out.println("    Changez votre nom et adresse");
        System.out.println();
        System.out.println("2. Changer Mot de Passe:");
        System.out.println("    Modifiez votre mot de passe de connexion");
        System.out.println();
        System.out.println("3. Détails du Compte:");
        System.out.println("    Consultez toutes vos informations");
        System.out.println();
        System.out.println("6. Se Déconnecter:");
        System.out.println("    Fermez votre session en toute sécurité");
        System.out.println();
        System.out.println("=".repeat(40));
    }

    private boolean confirmLogout() {
        System.out.print("\n Êtes-vous sûr de vouloir vous déconnecter? (o/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("o") || response.equals("oui") || response.equals("y") || response.equals("yes");
    }

    private void manageAccounts() {
        boolean managingAccounts = true;
        
        while (managingAccounts) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("    GESTION COMPTES BANCAIRES");
            System.out.println("=".repeat(40));
            System.out.println("1. Voir mes comptes (" + accountService.getActiveAccountCount(currentUser.getId()) + " comptes)");
            System.out.println("2. Créer nouveau compte");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Retour au menu principal");
            System.out.println("-".repeat(40));
            System.out.print("Votre choix: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        viewAccounts();
                        break;
                    case 2:
                        createAccount();
                        break;
                    case 3:
                        deleteAccount();
                        break;
                    case 4:
                        managingAccounts = false;
                        break;
                    default:
                        System.out.println("Choix invalide! Choisissez entre 1-4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide!");
            }
        }
    }
    
    private void createAccount() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       CRÉER NOUVEAU COMPTE");
        System.out.println("=".repeat(40));
        
        if (currentUser.getAccountCount() >= 5) {
            System.out.println("Limite de comptes atteinte! Maximum 5 comptes par utilisateur.");
            return;
        }
        
        System.out.println("Types de comptes disponibles:");
        System.out.println("1. " + AccountType.COURANT.getDisplayName());
        System.out.println("2. " + AccountType.EPARGNE.getDisplayName());
        System.out.println("3. " + AccountType.BLOQUE.getDisplayName());
        System.out.println("4. " + AccountType.PROFESSIONNEL.getDisplayName());
        System.out.println("-".repeat(40));
        System.out.print("Choisissez le type de compte (1-4): ");
        
        int choix;
        try {
            choix = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ntrée invalide! Veuillez entrer un nombre.");
            return;
        }
        
        if (choix < 1 || choix > 4) {
            System.out.println("Type de compte invalide! Choisissez entre 1-4");
            return;
        }
        
        AccountType selectedType;
        switch (choix) {
            case 1: selectedType = AccountType.COURANT; break;
            case 2: selectedType = AccountType.EPARGNE; break;
            case 3: selectedType = AccountType.BLOQUE; break;
            case 4: selectedType = AccountType.PROFESSIONNEL; break;
            default: 
                System.out.println("Erreur inattendue!");
                return;
        }
        
        try {
            // Utiliser AccountService au lieu de création directe
            boolean success = accountService.createAccount(currentUser.getId(), selectedType.getDisplayName());
            
            if (success) {
                // Recharger l'utilisateur pour avoir les données à jour
                currentUser = authService.getLoggedInUserByEmail(currentUser.getEmail());
                
                System.out.println("Compte créé avec succès!");
                System.out.println("Type: " + selectedType.getDisplayName());
                System.out.println("Solde initial: 0.00€");
            } else {
                System.out.println("Erreur lors de la création du compte!");
            }
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    private void viewAccounts() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           MES COMPTES BANCAIRES");
        System.out.println("=".repeat(50));
        
        if (!accountService.hasActiveAccounts(currentUser.getId())) {
            System.out.println("Aucun compte bancaire trouvé.");
            System.out.println("   Créez votre premier compte dans l'option 2!");
            return;
        }
        
        var accounts = currentUser.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getType());
            System.out.println("ID: " + account.getId());
            System.out.println("Solde: " + account.getSolde().doubleValue() + "€");
            System.out.println("   " + "-".repeat(30));
        }
        
        BigDecimal totalSolde = accounts.stream()
                .map(Account::getSolde)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("SOLDE TOTAL: " + totalSolde.doubleValue() + "€");
        System.out.println("=".repeat(50));
    }
    
    private void deleteAccount() {
        if (!currentUser.hasAccounts()) {
            System.out.println("Aucun compte à supprimer!");
            return;
        }
        
        System.out.println("\n=== SUPPRIMER UN COMPTE ===");
        viewAccounts();
        
        System.out.print("Entrez l'ID du compte à supprimer: ");
        String accountIdStr = scanner.nextLine().trim();
        
        try {
            UUID accountId = UUID.fromString(accountIdStr);
            
            // Utiliser AccountService au lieu de manipulation directe
            boolean success = accountService.deleteAccount(currentUser.getId(), accountId);
            
            if (success) {
                // Recharger l'utilisateur pour avoir les données à jour
                currentUser = authService.getLoggedInUserByEmail(currentUser.getEmail());
                System.out.println("Compte supprimé avec succès!");
            } else {
                System.out.println("Compte non trouvé!");
            }
            
        } catch (IllegalStateException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Format d'ID invalide! Utilisez un UUID valide.");
        }
    }
    
    enum AccountType {
        COURANT("Compte Courant"),
        EPARGNE("Compte Épargne"), 
        BLOQUE("Compte Bloqué"),
        PROFESSIONNEL("Compte Professionnel");
        
        private String displayName;
        
        AccountType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
