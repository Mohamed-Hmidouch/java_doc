package app.repositories;

import app.models.Account;
import java.util.List;
import java.util.UUID;

public interface AccountInterface {
    void save(Account account);
    Account findById(UUID id);
    List<Account> findAll();
    List<Account> findByUserId(UUID userId);
    void delete(Account account);
    boolean existsById(UUID id);
}