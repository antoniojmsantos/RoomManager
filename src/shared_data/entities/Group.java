package shared_data.entities;

import shared_data.helper.Pair;

import java.util.*;

public class Group {

    private String name;
    private Map<String, User> users;

    // constructor
    public Group(String name, Map<String,User> users) {
        this.name = name;
        this.users = users;
    }

    // constructor
    public Group(String name) {
        this(name, new HashMap<>());
    }

    // getter & setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public boolean addMember(User user) {
        if (users.containsKey(user.getUsername()))

        if (!containsMember(user)) {
            users.put(user.getUsername(), user);
            return true;
        }
        return false;
    }

    public boolean removeMember(User user) {
        return users.remove(user.getUsername(), user);
    }

    public boolean containsMember(User user) {
        return users.containsKey(user.getUsername()) && users.get(user.getUsername()).compareTo(user) == 0;
    }

    public Optional<User> getMember(String username) {
        if (users.containsKey(username)) {
            return Optional.of(users.get(username));
        } else {
            return Optional.empty();
        }
    }

    // make
    public static Group make(
            String name, Map<String,
            User> users
    ) {
        return new Group(name, users);
    }
    public static Group make(String name) {return new Group(name);}
}