package app.repositories;

import java.util.UUID;
import java.util.*;
import app.models.Transaction;

public interface TransactionInterface {
    void save(Transaction transaction);
    Transaction findById(UUID id);
    List<Transaction> findByAccountId(UUID accountId);
    boolean ifExistsById(UUID id);
}
