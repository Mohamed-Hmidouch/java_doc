package app.repositories;

import app.models.User;
import java.util.*;


public class AuthRepository implements AuthInterface {

    private Map<String, User> database = new HashMap<>();

    @Override
    public User findById(String id) {
        return database.get(id);
    }

    @Override
    public void save(User user) {
        database.put(user.getId().toString(), user);
    }

    @Override
    public void delete(String id) {
        database.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public User findByFullName(String fullName) {
        for (User user : database.values()) {
            if (user.getFullName().equals(fullName)) {
                return user;
            }
        }
        return null;
    }
}
