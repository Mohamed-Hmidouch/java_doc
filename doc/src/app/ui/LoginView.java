package app.ui;

import app.services.AuthService;
import app.models.User;
import java.util.Scanner;

public class LoginView {
    private Scanner scanner = new Scanner(System.in);
    private AuthService authService;
    private User loggedInUser = null;

    public LoginView(AuthService authService) {
        this.authService = authService;
    }

    public boolean showLoginDialog() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("        CONNEXION");
        System.out.println("=".repeat(30));
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("Email obligatoire!");
            return false;
        }
        
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();
        
        if (password.isEmpty()) {
            System.out.println("Mot de passe obligatoire!");
            return false;
        }
        
        // Tentative de connexion
        if (authService.login(email, password)) {
            loggedInUser = authService.getLoggedInUserByEmail(email);
            showSuccessMessage();
            return true;
        } else {
            showErrorMessage("Email ou mot de passe incorrect!");
            return false;
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void showSuccessMessage() {
        System.out.println("Connexion réussie!");
    }

    public void showErrorMessage(String message) {
        System.out.println("Erreur: " + message);
    }
}