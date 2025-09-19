# GitHub Copilot Instructions

## 🎯 **Contexte & Vision**

### **Contexte du Projet**
Développement d'une application console Java de banque digitale permettant la création, la gestion et la consultation de comptes bancaires, ainsi que l'exécution d'opérations courantes.

### **Cible Pédagogique**
- **Apprenants Java** : Consolidation de la POO et des bonnes pratiques
- **Manipulation financière** : Validation et gestion précise des montants
- **Architecture propre** : Code structuré, testable et maintenable

### **Vision Technique**
Produire un socle propre, testable, et facile à comprendre avec architecture en couches (Repository/Service Pattern) pour l'apprentissage des bonnes pratiques de développement.

## 📚 **Objectifs Pédagogiques**

1. **POO Avancée** : Entités, services, repositories, séparation des responsabilités
2. **Précision Financière** : BigDecimal obligatoire avec scale=2 (2 décimales max)
3. **Authentification** : Session basique (email + mot de passe, minimum 6 caractères)
4. **Règles Métier Bancaires** : Soldes, virements, clôture, validation des fonds
5. **Code Quality** : Structure lisible, testable, avec validation robuste

## 🏦 **Périmètre Fonctionnel**

### **Authentification & Profil**
- Inscription : nom, email unique, adresse, mot de passe (≥6 caractères)
- Connexion : email + mot de passe avec gestion de session
- Mise à jour profil : modification email/adresse
- Changement mot de passe avec validation

### **Comptes Bancaires**
- Création : identifiant unique (ex. BK-XXXX-1234), solde initial 0, statut actif
- Consultation : liste et solde par utilisateur propriétaire uniquement
- Clôture : statut inactif si solde = 0 (pas de suppression physique)

### **Transactions** (à implémenter)
- Dépôt : augmentation solde + transaction DEPOSIT
- Retrait : diminution si fonds suffisants + transaction WITHDRAW  
- Virement interne : double écriture TRANSFEROUT/TRANSFERIN
- Historique : tri chronologique avec détails complets

## ✅ **À ENCOURAGER - Préférences de Code**

### **Architecture & Patterns**
- **Repository Pattern** : Toujours séparer les interfaces des implémentations
- **Service Layer Pattern** : Logique métier centralisée dans les services
- **Anemic Model** : Modèles purs avec getters/setters, logique dans les services
- **Séparation des responsabilités** : UI → Service → Repository → Model

### **Types de Données Financières**
- **BigDecimal obligatoire** pour tous les montants (scale=2, max 2 décimales)
- **UUID** pour les identifiants entités (User.id, Transaction.id)
- **String** pour identifiants comptes lisibles (ex. BK-XXXX-1234)
- **Instant** pour horodatage des transactions
- **Enum** pour types de transactions : DEPOSIT, WITHDRAW, TRANSFERIN, TRANSFEROUT

### **Règles Métier Obligatoires**
- **Montants > 0** : Validation systématique avant opérations
- **Solde suffisant** : Vérification avant retrait/virement
- **Propriété** : Un utilisateur agit uniquement sur ses comptes
- **Comptes actifs** : Opérations refusées sur comptes inactifs
- **Email unique** : Validation à l'inscription
- **Traçabilité** : Toute opération génère une transaction immuable

### **Conventions de Nommage**
- **Français** pour les noms de variables métier : `solde`, `adresse`, `getAdress()`
- **Anglais** pour les concepts techniques : `repository`, `service`, `interface`
- **Méthodes descriptives** : `getActiveAccountCount()`, `hasActiveAccounts()`

### **Gestion des Données**
- **HashMap** pour le stockage en mémoire (pas de base de données)
- **Immutable collections** en retour : `new ArrayList<>(originalList)`
- **Validation null-safe** systématique
- **Construction defensive** dans les constructeurs

### **Modèle de Données Bancaires**
```java
// User - Utilisateur du système
class User {
    UUID id;
    String fullName;
    String email;        // Unique
    String address;
    // + Liste des comptes
}

// Account - Compte bancaire
class Account {
    String accountId;    // Format: BK-XXXX-1234
    UUID ownerUserId;
    BigDecimal balance;  // Scale=2, ≥0
    Instant createdAt;
    boolean active;      // true pour autoriser opérations
}

// Transaction - Opération bancaire (à implémenter)
class Transaction {
    UUID id;
    Instant timestamp;
    String accountId;
    TransactionType type; // DEPOSIT, WITHDRAW, TRANSFERIN, TRANSFEROUT
    BigDecimal amount;   // Scale=2, >0
    String counterpartyAccountId; // Pour virements
    String description;
}
```

### **État Actuel du Projet**
```
src/app/
├── models/          # User.java, Account.java (implémentés)
├── repositories/    # AuthInterface/Repository, AccountInterface/Repository (implémentés)
├── services/        # AuthService, AccountService (implémentés)
├── ui/             # Console - AuthMenu, LoginView, RegisterView, UserDashboard (implémentés)
└── Main.java       # Point d'entrée avec gestion des sessions

État : Authentication + Gestion comptes bancaires fonctionnels
À implémenter : Transaction system (DEPOSIT, WITHDRAW, TRANSFER)
```

## ❌ **À ÉVITER - Anti-Patterns**
- **PAS d'emojis** dans les messages console
- **PAS de logique métier** dans les modèles (User/Account)
- **PAS de manipulation directe** des modèles depuis l'UI
- **PAS d'interface graphique** (Swing/JavaFX) - Console uniquement

### **Types de Données**
- **PAS de double/float** pour les montants → Toujours BigDecimal
- **PAS de String ID** pour les entités → Toujours UUID
- **PAS de types primitifs** pour les objets métier

### **Code Style**
- **PAS de sur-ingénierie** : éviter trop de méthodes pour les mêmes concepts
- **PAS de getters spécialisés** : utiliser Stream.filter() à la place
- **PAS de validation UI** complexe : rester simple avec Scanner

### **Dépendances**
- **PAS de frameworks externes** (Spring, Hibernate, etc.)
- **PAS de librairies tierces** sauf Java Standard Library
- **PAS de base de données** : stockage HashMap uniquement

## 🛠️ **Patterns Spécifiques à Implémenter**

### **Repository Pattern**
```java
// Interface clean
public interface AccountInterface {
    void save(Account account);
    Account findById(UUID id);
    List<Account> findAll();
}

// Implémentation HashMap
public class AccountRepository implements AccountInterface {
    private Map<UUID, Account> accounts = new HashMap<>();
    // ...
}
```

### **Service Pattern**
```java
public class AccountService {
    // Injection de dépendances par constructeur
    public AccountService(AccountInterface repo, AuthInterface auth) {}
    
    // Validation métier + exceptions explicites
    public boolean createAccount(UUID userId, String type) {
        if (!isValidAccountType(type)) {
            throw new IllegalArgumentException("Type invalide: " + type);
        }
        // ...
    }
}
```

### **Stream + Filter Pattern**
```java
// ✅ PRÉFÉRÉ : Filtrage à la demande
var activeAccounts = user.getAccounts().stream()
    .filter(account -> account.getStatus().equals("active"))
    .collect(toList());

// ❌ ÉVITER : Méthodes spécialisées
// public List<Account> getActiveAccounts() { ... }
```

## 📋 **Validation & Exceptions**

### **Patterns de Validation**
- **IllegalArgumentException** pour paramètres invalides
- **IllegalStateException** pour états métier incorrects
- **Messages d'erreur en français** pour l'utilisateur
- **Validation early return** dans les services

### **Gestion des Erreurs UI**
```java
try {
    boolean success = accountService.createAccount(userId, type);
    if (success) {
        System.out.println("Compte créé avec succès!");
    }
} catch (IllegalStateException | IllegalArgumentException e) {
    System.out.println("Erreur: " + e.getMessage());
}
```

## 🎨 **Style d'Interface Console**

### **Affichage Préféré**
```java
System.out.println("=".repeat(40));
System.out.println("    GESTION COMPTES BANCAIRES");
System.out.println("=".repeat(40));
System.out.println("1. Voir mes comptes");
System.out.println("2. Créer nouveau compte");
// PAS d'emojis, style clean et professionnel
```

### **Input Validation**
```java
try {
    int choice = Integer.parseInt(scanner.nextLine().trim());
} catch (NumberFormatException e) {
    System.out.println("Veuillez entrer un nombre valide!");
}
```

---

**Philosophie** : Code simple, maintenable, et éducatif pour l'apprentissage des patterns Java enterprise sans la complexité des frameworks.