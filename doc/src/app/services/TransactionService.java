package app.services;

import java.util.*;
import java.math.BigDecimal;

import app.models.Account;
import app.models.Transaction;
import app.models.User;
import app.repositories.TransactionRepository;

public class TransactionService {

    public TransactionRepository transactionRepository;
    public User currentUser;
    public Account compte;
    public Transaction transaction;

    public TransactionService(TransactionRepository transactionRepository, User currentUser, Account compte,
            Transaction transaction) {
        this.transactionRepository = transactionRepository;
        this.currentUser = currentUser;
        this.compte = compte;
        this.transaction = transaction;
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
        Transaction newTransaction = new Transaction(UUID.randomUUID(), accountId, java.time.LocalDateTime.now(),
                montant);
        transactionRepository.save(newTransaction);
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
}
