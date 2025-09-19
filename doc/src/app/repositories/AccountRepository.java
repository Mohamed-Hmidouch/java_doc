package app.repositories;

import app.models.Account;
import java.util.*;

public class AccountRepository implements AccountInterface {
    private Map<UUID, Account> accounts = new HashMap<>();
    
    @Override
    public void save(Account account) {
        if (account != null && account.getId() != null) {
            accounts.put(account.getId(), account);
        }
    }
    
    @Override
    public Account findById(UUID id) {
        return accounts.get(id);
    }
    
    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
    
    @Override
    public List<Account> findByUserId(UUID userId) {
        return accounts.values().stream().filter(account -> account.getUserId() != null && account.getUserId().equals(userId)).collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public void delete(Account account) {
        if (account != null && account.getId() != null) {
            accounts.remove(account.getId());
        }
    }
    
    @Override
    public boolean existsById(UUID id) {
        return accounts.containsKey(id);
    }
}