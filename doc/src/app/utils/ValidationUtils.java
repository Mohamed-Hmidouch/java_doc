package app.utils;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Classe utilitaire pour centraliser toutes les validations
 * Suit les bonnes pratiques définies dans les instructions Copilot
 */
public class ValidationUtils {
    
    // Patterns de validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    /**
     * Valide la longueur minimale d'un mot de passe
     * @param password Le mot de passe à valider
     * @return true si valide, false sinon
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= MIN_PASSWORD_LENGTH;
    }
    
    /**
     * Valide le format d'un email
     * @param email L'email à valider
     * @return true si valide, false sinon
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Valide qu'un montant est positif (règle métier bancaire)
     * @param montant Le montant BigDecimal à valider
     * @return true si > 0, false sinon
     */
    public static boolean isValidMontant(BigDecimal montant) {
        return montant != null && montant.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Valide qu'un solde est non-négatif
     * @param solde Le solde BigDecimal à valider
     * @return true si >= 0, false sinon
     */
    public static boolean isValidSolde(BigDecimal solde) {
        return solde != null && solde.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    /**
     * Valide le format UUID
     * @param uuidString La chaîne UUID à valider
     * @return true si format valide, false sinon
     */
    public static boolean isValidUUID(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            return false;
        }
        
        try {
            UUID.fromString(uuidString.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Valide qu'une chaîne n'est pas vide
     * @param value La valeur à valider
     * @return true si non-null et non-vide, false sinon
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Valide qu'un entier est dans une plage donnée
     * @param value La valeur à valider
     * @param min Valeur minimale (inclusive)
     * @param max Valeur maximale (inclusive)
     * @return true si dans la plage, false sinon
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Valide qu'une chaîne représente un entier valide
     * @param numberString La chaîne à valider
     * @return true si entier valide, false sinon
     */
    public static boolean isValidInteger(String numberString) {
        if (numberString == null || numberString.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(numberString.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Parse un entier de façon sécurisée
     * @param numberString La chaîne à parser
     * @param defaultValue Valeur par défaut si parsing échoue
     * @return L'entier parsé ou la valeur par défaut
     */
    public static int parseIntSafely(String numberString, int defaultValue) {
        if (!isValidInteger(numberString)) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(numberString.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Valide qu'une réponse de confirmation est positive
     * @param response La réponse utilisateur
     * @return true si confirmation positive, false sinon
     */
    public static boolean isPositiveConfirmation(String response) {
        if (response == null) {
            return false;
        }
        
        String normalized = response.trim().toLowerCase();
        return normalized.equals("o") || normalized.equals("oui") || 
               normalized.equals("y") || normalized.equals("yes");
    }
    
    /**
     * Messages d'erreur standardisés pour les validations
     */
    public static class ErrorMessages {
        public static final String INVALID_PASSWORD = "Le mot de passe doit contenir au moins " + MIN_PASSWORD_LENGTH + " caractères!";
        public static final String INVALID_EMAIL = "Format d'email invalide!";
        public static final String INVALID_MONTANT = "Le montant doit être positif!";
        public static final String INVALID_UUID = "Format d'ID invalide! Utilisez un UUID valide.";
        public static final String EMPTY_FIELD = "Ce champ ne peut pas être vide!";
        public static final String INVALID_NUMBER = "Veuillez entrer un nombre valide!";
        public static final String PASSWORDS_NOT_MATCH = "Les mots de passe ne correspondent pas!";
        public static final String CURRENT_PASSWORD_INCORRECT = "Mot de passe actuel incorrect!";
    }
}