package database.dao;

import database.DBManager;
import shared_data.entities.Group;
import shared_data.entities.User;
import shared_data.helper.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberDao implements IGroupMemberDao {

    private final Connection conn;

    public GroupMemberDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Pair<Group, User>> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Pair<Group, User>> pairs = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group_member"
            );
            rs = st.executeQuery();
            while (rs.next()) {
                pairs.add(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(st);
        }
        return pairs;
    }

    @Override
    public List<User> getMembersOfGroup(Group group) {
        return null;
    }

    @Override
    public List<Group> getGroupsOfMember(User user) {
        return null;
    }

    @Override
    public void insert(Pair<Group, User> pair) {

    }

    @Override
    public void delete(Pair<Group, User> pair) {

    }

    // own
    private Pair<Group, User> build(ResultSet rs) throws SQLException {
        Group group = DBManager.getGroupDao().get(rs.getString("vc_group_name"));
        User user   = DBManager.getUserDao().get(rs.getString("vc_user_username"));

        return group == null || user == null ? null : Pair.make(group, user);
    }
}
