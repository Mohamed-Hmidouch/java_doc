package app.models;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class User {
    private UUID id;
    private String fullName;
    private String email;
    private String adress;
    private String password;
    private boolean loggedIn;
    private List<Account> accounts;

    public User(UUID id, String fullName, String email, String adress, String password, List<Account> accounts) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.adress = adress;
        this.password = password;
        this.accounts = accounts != null ? new ArrayList<>(accounts) : new ArrayList<>();
        this.loggedIn = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public Account getAccountById(UUID accountId) {
        return accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .orElse(null);
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public boolean hasAccounts() {
        return !accounts.isEmpty();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
