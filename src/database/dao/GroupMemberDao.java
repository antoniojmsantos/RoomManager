package database.dao;

import shared_data.entities.Group;
import shared_data.entities.User;
import shared_data.helper.Pair;

import java.util.List;
import java.util.Optional;

public interface GroupMemberDao {
    List<Pair<Group, User>> getAll();
    List<User> getMembersOfGroup(Group group);
    List<Group> getGroupsOfMember(User user);
    void insert(Pair<Group, User> pair);
    void delete(Pair<Group, User> pair);
}
