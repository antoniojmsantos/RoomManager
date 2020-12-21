package database.dao;

import shared_data.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> get(String username);
    List<User> getAll();
    void insert (User user);
    void updateUsername(User user, String username);
    void updateName(User user, String name);
    void updatePassword(User user, String password);
    void updatePermissions(User user, boolean permissions);
    void delete(User user);
}
