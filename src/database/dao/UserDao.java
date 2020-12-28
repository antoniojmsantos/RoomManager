package database.dao;

import database.DBManager;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {

    private final Connection conn;

    // constructor
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User get(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select * from tb_user where vc_username = ?"
            );
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                return build(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return null;
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return users;
    }

    @Override
    public boolean insert(User user) {
        PreparedStatement st = null;
        int rows = 0;

        try {
            st = conn.prepareStatement(
                    "insert into tb_user(vc_username, vc_name, vc_password, b_permissions) " +
                            "values (?,?,?,?)"
            );
            st.setString(1,user.getUsername());
            st.setString(2,user.getName());
            st.setString(3,user.getPassword());
            st.setBoolean(4,user.isPermissions());
            rows = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }

        return rows > 0;
    }

    @Override
    public void updatePermissions(User user, boolean permissions) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "update tb_user set b_permissions = ? where vc_username = ?"
            );
            st.setBoolean(1, permissions);
            st.setString(2, user.getUsername());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void delete(User user) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_user where vc_username = ?"
            );
            st.setString(1, user.getUsername());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select exists(select * from tb_user where  vc_username = ? and vc_password = ?) as res;"
            );
            st.setString(1, username);
            st.setString(2, password);
            rs = st.executeQuery();

            if (rs.next()) {
                // todo: could be getInt instead
                return rs.getBoolean("res");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(st);
        }
        return false;
    }

    // own
    private User build(ResultSet rs) throws SQLException {
        return User.make(
                rs.getString("vc_username"),
                rs.getString("vc_name"),
                rs.getString("vc_password"),
                rs.getBoolean("b_permissions")
        );
    }
}
