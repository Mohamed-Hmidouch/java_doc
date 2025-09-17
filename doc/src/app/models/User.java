package app.models;

import java.util.UUID;

public class User {
    private UUID id;
    private String fullName;
    private String email;
    private String adress;
    private String password;
    private boolean loggedIn;

    public User(UUID id, String fullName, String email, String adress, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.adress = adress;
        this.password = password;
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
