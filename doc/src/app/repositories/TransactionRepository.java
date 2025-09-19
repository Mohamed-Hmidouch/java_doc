package app.repositories;

import java.util.*;
import app.models.Transaction;

public class TransactionRepository implements TransactionInterface{

    Map<UUID,Transaction> transactions = new HashMap<>();

    @Override
    public  void save(Transaction transaction){
        if(transaction != null && transaction.getId() != null)
            transactions.put(transaction.getId(),transaction);
    }

    public  Transaction findById(UUID id){
        return  transactions.get(id);
    }

    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions.values()) {
            if (transaction != null && accountId != null && accountId.equals(transaction.getAccountId()))
                result.add(transaction);
        }
        return result;
    }

    public  boolean ifExistsById(UUID id){
        return  transactions.containsKey(id);
    }
}