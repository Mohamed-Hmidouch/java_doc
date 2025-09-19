package app.services;

import java.util.*;
import java.math.BigDecimal;

import app.models.Account;
import app.models.Transaction;
import app.models.User;
import app.repositories.AccountRepository;
import app.repositories.TransactionRepository;

public class TransactionService {

    public TransactionRepository transactionRepository;
    public User currentUser;
    public Account compte;
    public Transaction transaction;
    public AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, User currentUser, Account compte,
            Transaction transaction, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.currentUser = currentUser;
        this.compte = compte;
        this.transaction = transaction;
        this.accountRepository = accountRepository;
    }

    public void deposer(UUID userId, UUID accountId, BigDecimal montant) {
        if (currentUser.getAccountById(accountId) == null) {
            System.out.println("Erreur : tu doit crée un compte aux moinx pour fair déposer");
            return;
        }
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Erreur: Le montant doit être supérieur à 0.");
            return;
        }
        if (!"active".equals(compte.getStatus())) {
            System.out.println("Erreur: Le compte n'est pas actif.");
            return;
        }
        compte.setSolde(compte.getSolde().add(montant));
        Transaction depositTransaction = new Transaction(UUID.randomUUID(), accountId, java.time.LocalDateTime.now(),
                montant);
        transactionRepository.save(depositTransaction);
        System.out.println("Dépôt effectué avec succès.");
    }

    public void consultezTransaction(UUID userid, UUID accountId) {

        if (currentUser.getAccountById(accountId) == null) {
            System.out.println("Erreur : tu doit crée un compte aux moinx pour voir les transactions");
            return;
        }
        var listOfTransactions = transactionRepository.findByAccountId(accountId);

        listOfTransactions.sort(Comparator.comparing(Transaction::getDateTransaction));

        for (Transaction listOfTransaction : listOfTransactions) {
            System.out.println("=".repeat(40));
            System.out.println("ID of transaction        : " + listOfTransaction.getDateTransaction());
            System.out.println("Account Id  : " + listOfTransaction.getAccountId());
            System.out.println("Montant     : " + listOfTransaction.getMontant().setScale(2));
            System.out.println("Date: " + listOfTransaction.getDateTransaction());
            System.out.println("=".repeat(40));
        }
    }

    public void withdraw(UUID userId, UUID accountId, BigDecimal montant) {
        if (currentUser.getAccountById(accountId) == null) {
            System.out.println("Erreur : tu doit crée un compte aux moinx pour fair déposer");
            return;
        }
        if (!"active".equals(compte.getStatus())) {
            System.out.println("Erreur: Le compte n'est pas actif.");
            return;
        }
        if(montant.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("Erreur: Le montant doit être supérieur à 0.");
            return;
        }
        
        if (compte.getSolde().compareTo(montant) < 0) {
            System.out.println("Erreur: Solde insuffisant pour effectuer ce retrait.");
            System.out.println("Solde disponible: " + compte.getSolde().setScale(2) + "€");
            return;
        }
        
        compte.setSolde(compte.getSolde().subtract(montant));
        Transaction withdrawTransaction = new Transaction(UUID.randomUUID(), accountId, java.time.LocalDateTime.now(), montant);
        transactionRepository.save(withdrawTransaction);
        System.out.println("Withdraw effectué avec succès.");
    }

    public  void transfer(UUID userId, UUID accountId, UUID accountIdToTransfer, BigDecimal montant){
        if(currentUser.getAccountById(accountId) == null){
            System.out.println("Erreur : tu doit crée un compte aux moinx pour fair déposer");
            return;
        }
        if (!"active".equals(compte.getStatus())) {
            System.out.println("Erreur: Le compte n'est pas actif.");
            return;
        }
        
        if (accountId.equals(accountIdToTransfer)) {
            System.out.println("Erreur: Impossible de virer vers le même compte.");
            return;
        }
        
        Account compteDestinataire = accountRepository.findById(accountIdToTransfer);
        if (compteDestinataire == null) {
            System.out.println("Erreur : Le compte destinataire n'existe pas.");
            return;
        }
        if (!"active".equals(compteDestinataire.getStatus())) {
            System.out.println("Erreur: Le compte destinataire n'est pas actif.");
            return;
        }
        if(montant.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("Erreur: Le montant doit être supérieur à 0.");
            return;
        }
        
        if (compte.getSolde().compareTo(montant) < 0) {
            System.out.println("Erreur: Solde insuffisant pour effectuer ce virement.");
            System.out.println("Solde disponible: " + compte.getSolde().setScale(2) + "€");
            return;
        }
        
        compte.setSolde(compte.getSolde().subtract(montant));
        compteDestinataire.setSolde(compteDestinataire.getSolde().add(montant));
        
        Transaction transferOutTransaction = new Transaction(UUID.randomUUID(), accountId, java.time.LocalDateTime.now(), montant);
        Transaction transferInTransaction = new Transaction(UUID.randomUUID(), accountIdToTransfer, java.time.LocalDateTime.now(), montant);
        
        transactionRepository.save(transferOutTransaction);
        transactionRepository.save(transferInTransaction);
        
        System.out.println("Virement effectué avec succès.");
        System.out.println("Nouveau solde compte source: " + compte.getSolde().setScale(2) + "€");
        System.out.println("Nouveau solde compte destinataire: " + compteDestinataire.getSolde().setScale(2) + "€");
    }

}