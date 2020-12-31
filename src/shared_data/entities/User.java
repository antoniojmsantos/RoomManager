package shared_data.entities;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {

    private String username;
    private String name;
    private String password;
    private boolean permissions;

    // constructor
    public User(
            String username,
            String name,
            String password,
            boolean permissions
    ) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.permissions = permissions;
    }

    // getter & setter

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getPermissions() {
        return permissions;
    }

    public void setPermissions(boolean permissions) {
        this.permissions = permissions;
    }

    // make
    public static User make(
            String username,
            String name,
            String password,
            boolean permissions
    ) {
        return new User(username, name, password, permissions);
    }

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.getUsername());
    }

    @Override
    public String toString() {
        return username;
    }
}
