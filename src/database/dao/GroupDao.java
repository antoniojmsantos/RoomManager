package database.dao;

import database.DBManager;
import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao implements IGroupDao {

    private final Connection conn;

    public GroupDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Group get(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select * from tb_group where vc_name = ?"
            );
            st.setString(1, name);
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
    public List<Group> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Group> groups = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                groups.add(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return groups;
    }

    @Override
    public void insert(Group group) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_group(vc_name) " +
                            "values (?)"
            );
            st.setString(1,group.getName());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void delete(String name) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_group where vc_name = ?"
            );
            st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public List<User> getMembers(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group_member where vc_group_name = ?"
            );
            st.setString(1, name);
            rs = st.executeQuery();
            while (rs.next()) {
                users.add(DBManager.getUserDao().build(rs));
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
    public void addMember(String name, String username) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_group_member(vc_group_name, vc_user_username) values (?,?)"
            );
            st.setString(1, name);
            st.setString(2, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void removeMember(String name, String username) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_group_member where vc_group_name = ? and vc_user_username = ?"
            );
            st.setString(1, name);
            st.setString(2, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    // own
    @Override
    public final Group build(ResultSet rs) throws SQLException {
        return Group.make(rs.getString("vc_name"));
    }
}
