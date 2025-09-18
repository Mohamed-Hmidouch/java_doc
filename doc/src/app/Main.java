package app;

import app.repositories.AuthRepository;
import app.repositories.AccountRepository;
import app.services.AuthService;
import app.services.AccountService;
import app.ui.AuthMenu;
import app.ui.LoginView;
import app.ui.RegisterView;
import app.ui.UserDashboard;
import app.models.User;

public class Main {
    public static void main(String[] args) {

        // Repositories
        AuthRepository authRepository = new AuthRepository();
        AccountRepository accountRepository = new AccountRepository();

        // Services
        AuthService authService = new AuthService(authRepository);
        AccountService accountService = new AccountService(accountRepository, authRepository);

        AuthMenu authMenu = new AuthMenu();
        
        boolean running = true;
        while(running){
            int choice = authMenu.displayMenu();
            switch(choice){
                case 1:
                    LoginView   loginView = new LoginView(authService);
                    if(loginView.showLoginDialog()){
                        User user = loginView.getLoggedInUser();
                        authMenu.setUserLoggedIn(true);
                        UserDashboard dashboard = new UserDashboard(user, authService, accountService);
                        dashboard.showDashboard();
                        authMenu.setUserLoggedIn(false);
                    }
                    break;
                case 2:
                    RegisterView registerView = new RegisterView(authService);
                    registerView.showRegisterDialog();
                    break;
                case 3:
                    authMenu.setUserLoggedIn(false);
                    break;
                case 4:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide !"); 
            }
        }
         System.exit(0);
    }
}
