package app.ui;

import app.services.AuthService;
import java.util.Scanner;

public class RegisterView {
    private Scanner scanner = new Scanner(System.in);
    private AuthService authService;

    public RegisterView(AuthService authService) {
        this.authService = authService;
    }

    public boolean showRegisterDialog() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("       INSCRIPTION");
        System.out.println("=".repeat(30));
        
        System.out.print("Nom complet: ");
        String fullName = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Adresse: ");
        String address = scanner.nextLine().trim();
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Confirmer mot de passe: ");
        String confirmPassword = scanner.nextLine().trim();
        
        // Validations
        if (fullName.isEmpty() || email.isEmpty() || address.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Tous les champs sont obligatoires!");
            return false;
        }
        
        if (!email.contains("@")) {
            showErrorMessage("Format d'email invalide!");
            return false;
        }
        
        if (password.length() < 6) {
            showErrorMessage("Le mot de passe doit contenir au moins 6 caractères!");
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showErrorMessage("Les mots de passe ne correspondent pas!");
            return false;
        }
        
        // Tentative d'inscription
        if (authService.register(fullName, email, address, password)) {
            showSuccessMessage();
            return true;
        } else {
            showErrorMessage("Cet email est déjà utilisé!");
            return false;
        }
    }

    public void showSuccessMessage() {
        System.out.println("Inscription réussie! Vous pouvez maintenant vous connecter.");
    }

    public void showErrorMessage(String message) {
        System.out.println("Erreur: " + message);
    }
}