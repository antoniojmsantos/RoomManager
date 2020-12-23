package database.dao;

import shared_data.entities.User;

import java.util.List;

public interface IUserDao {
    User get(String username);
    List<User> getAll();
    void insert (User user);
    void updatePermissions(User user, boolean permissions);
    void delete(User user);
}
