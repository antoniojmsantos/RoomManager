package database.dao;

import shared_data.entities.Group;

import java.util.List;

public interface GroupDao {
    Group get(String name);
    List<Group> getAll();
    void insert(Group group);
    void delete(Group group);
}
