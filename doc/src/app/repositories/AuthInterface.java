package app.repositories;

import app.models.User;
import java.util.List;


public interface AuthInterface {
    

    User findById(String id);
    
    void save(User user);
        
    void delete(String id);    

    List<User> findAll();

    User findByFullName(String fullName);
}