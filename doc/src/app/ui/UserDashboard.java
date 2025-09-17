package app.ui;

import app.models.User;
import app.services.AuthService;
import java.util.Scanner;

public class UserDashboard {
    private Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private AuthService authService;

    public UserDashboard(User user, AuthService authService) {
        this.currentUser = user;
        this.authService = authService;
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
                    showHelp();
                    break;
                case 5:
                    if (confirmLogout()) {
                        authService.logout(currentUser.getId().toString());
                        System.out.println("✅ Déconnexion réussie!");
                        return true; // Logout requested
                    }
                    break;
                default:
                    System.out.println("❌ Choix invalide! Veuillez réessayer.");
            }
        }
        return false;
    }

    private void displayUserInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    TABLEAU DE BORD - " + currentUser.getFullName().toUpperCase());
        System.out.println("=".repeat(50));
        System.out.println("📧 Email: " + currentUser.getEmail());
        System.out.println("📍 Adresse: " + currentUser.getAdress());
        System.out.println("✅ Statut: " + (currentUser.isLoggedIn() ? "Connecté" : "Déconnecté"));
        System.out.println("=".repeat(50));
    }

    private int displayMenu() {
        System.out.println("\n📋 ACTIONS DISPONIBLES:");
        System.out.println("1. Modifier profil");
        System.out.println("2. Changer mot de passe");
        System.out.println("3. Détails du compte");
        System.out.println("4. Aide");
        System.out.println("5. Se déconnecter");
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
        System.out.println("✅ Profil mis à jour avec succès!");
    }

    private void changePassword() {
        System.out.println("\n--- Changer Mot de Passe ---");
        
        System.out.print("Mot de passe actuel: ");
        String currentPassword = scanner.nextLine().trim();
        
        if (!currentPassword.equals(currentUser.getPassword())) {
            System.out.println("❌ Mot de passe actuel incorrect!");
            return;
        }
        
        System.out.print("Nouveau mot de passe (min 6 caractères): ");
        String newPassword = scanner.nextLine().trim();
        
        if (newPassword.length() < 6) {
            System.out.println("❌ Le mot de passe doit contenir au moins 6 caractères!");
            return;
        }
        
        System.out.print("Confirmer le nouveau mot de passe: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("❌ Les mots de passe ne correspondent pas!");
            return;
        }
        
        currentUser.setPassword(newPassword);
        authService.updateUser(currentUser);
        System.out.println("✅ Mot de passe changé avec succès!");
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
        System.out.println("1️⃣  Modifier Profil:");
        System.out.println("    Changez votre nom et adresse");
        System.out.println();
        System.out.println("2️⃣  Changer Mot de Passe:");
        System.out.println("    Modifiez votre mot de passe de connexion");
        System.out.println();
        System.out.println("3️⃣  Détails du Compte:");
        System.out.println("    Consultez toutes vos informations");
        System.out.println();
        System.out.println("5️⃣  Se Déconnecter:");
        System.out.println("    Fermez votre session en toute sécurité");
        System.out.println();
        System.out.println("💡 Pour toute assistance, contactez l'administrateur");
        System.out.println("=".repeat(40));
    }

    private boolean confirmLogout() {
        System.out.print("\n Êtes-vous sûr de vouloir vous déconnecter? (o/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("o") || response.equals("oui") || response.equals("y") || response.equals("yes");
    }
}
