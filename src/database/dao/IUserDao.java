package database.dao;

import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    User get(String username);
    List<User> getAll();
    boolean insert(String username, String name, String password, Boolean permissions);
    void updatePermissions(String username, boolean permissions);
    void delete(String username);

    boolean authenticate(String username, String password);

    // group member
    List<Group> getGroups(String username);

    // own
    User build(ResultSet rs) throws SQLException;
}
