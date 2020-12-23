package database.dao;

import shared_data.entities.User;

import java.util.List;

public interface IUserDao {
    User get(String username);
    List<User> getAll();
    void insert (User user);
    void updateName(User user, String name);
    void updatePassword(User user, String password);
    void updatePermissions(User user, boolean permissions);
    void delete(User user);
}
