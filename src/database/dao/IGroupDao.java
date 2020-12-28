package database.dao;

import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IGroupDao {
    Group get(String name);
    List<Group> getAll();
    void insert(Group group);
    void delete(String name);

    // group member
    List<User> getMembers(String name);
    void addMember(String name, String username);
    void removeMember(String name, String username);

    // own
    Group build(ResultSet rs) throws SQLException;
}
