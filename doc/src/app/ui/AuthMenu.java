package app.ui;

import java.util.Scanner;

public class AuthMenu {
    private Scanner scanner = new Scanner(System.in);
    private boolean isUserLoggedIn = false;

    public int displayMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("    MENU D'AUTHENTIFICATION");
        System.out.println("=".repeat(40));
        
        if (!isUserLoggedIn) {
            System.out.println("1. Se connecter");
            System.out.println("2. S'inscrire");
            System.out.println("4. Quitter");
        } else {
            System.out.println("3. Se déconnecter");
            System.out.println("4. Quitter");
        }
        
        System.out.println("=".repeat(40));
        System.out.print("Votre choix: ");
        
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void setUserLoggedIn(boolean loggedIn) {
        this.isUserLoggedIn = loggedIn;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }
}