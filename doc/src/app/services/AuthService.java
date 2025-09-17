package app.services;

import app.models.User;
import app.repositories.AuthInterface;

public class AuthService {
    private AuthInterface authRepository;

    public AuthService(AuthInterface authRepository) {
        this.authRepository = authRepository;
    }

    public boolean login(String email, String password) {
        User user = findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            user.setLoggedIn(true);
            authRepository.save(user);
            return true;
        }
        return false;
    }

    public void logout(String userId) {
        User user = authRepository.findById(userId);
        if (user != null) {
            user.setLoggedIn(false);
            authRepository.save(user);
        }
    }

    public boolean isUserLoggedIn(String userId) {
        User user = authRepository.findById(userId);
        return user != null && user.isLoggedIn();
    }

    private User findUserByEmail(String email) {
        for (User user : authRepository.findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    // Méthode pour créer un nouvel utilisateur (inscription)
    public boolean register(String fullName, String email, String address, String password) {
        // Vérifier si l'email existe déjà
        if (findUserByEmail(email) != null) {
            return false; // Email déjà utilisé
        }

        // Créer nouvel utilisateur
        User newUser = new User(
            java.util.UUID.randomUUID(),
            fullName,
            email,
            address,
            password
        );

        // Sauvegarder dans le repository
        authRepository.save(newUser);
        return true;
    }

    // Méthode pour mettre à jour un utilisateur
    public void updateUser(User user) {
        authRepository.save(user);
    }

    // Méthode pour obtenir l'utilisateur connecté par email
    public User getLoggedInUserByEmail(String email) {
        User user = findUserByEmail(email);
        if (user != null && user.isLoggedIn()) {
            return user;
        }
        return null;
    }
}