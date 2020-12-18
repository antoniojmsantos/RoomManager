package database.dao;

import database.DBManager;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<User> get(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "select * from tb_user where vc_username = ?"
            );
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_user"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                users.add(build(rs));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return users;
    }

    @Override
    public void insert(User user) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_user(vc_username, vc_name, vc_password, b_permissions) " +
                            "VALUES (?,?,?,?)"
            );
            st.setString(1,user.getUsername());
            st.setString(2,user.getName());
            st.setString(3,user.getPassword());
            st.setBoolean(4,user.isPermissions());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
    }

    @Override
    public void updateUsername(User user, String username) {

    }

    @Override
    public void updateName(User user, String name) {

    }

    @Override
    public void updatePassword(User user, String password) {

    }

    @Override
    public void updatePermissions(User user, boolean permissions) {

    }

    @Override
    public void delete(User user) {

    }

    // own
    private User build(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getBoolean("permissions"));
    }
}
