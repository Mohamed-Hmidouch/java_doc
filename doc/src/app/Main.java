package app;

import app.repositories.AuthRepository;
import app.repositories.AccountRepository;
import app.repositories.TransactionRepository;
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
        final AuthRepository authRepository = new AuthRepository();
        final AccountRepository accountRepository = new AccountRepository();
        final TransactionRepository transactionRepository = new TransactionRepository();

        // Services
        final AuthService authService = new AuthService(authRepository);
        final AccountService accountService = new AccountService(accountRepository, authRepository);

        final AuthMenu authMenu = new AuthMenu();
        
        boolean running = true;
        while(running){
            int choice = authMenu.displayMenu();
            switch(choice){
                case 1:
                    LoginView   loginView = new LoginView(authService);
                    if(loginView.showLoginDialog()){
                        User user = loginView.getLoggedInUser();
                        authMenu.setUserLoggedIn(true);
                        UserDashboard dashboard = new UserDashboard(user, authService, accountService, transactionRepository, accountRepository);
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
