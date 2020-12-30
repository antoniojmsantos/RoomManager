package database.dao;

import database.DBManager;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDao implements IEventDao {

    private final Connection conn;

    public EventDao(Connection conn) {this.conn = conn;}

    @Override
    public Event get(int id) {
        PreparedStatement st = null;
        ResultSet rs= null;

        try{
            st = conn.prepareStatement(
                    "select * from tb_event where i_id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return build(rs);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return null;
    }

    @Override
    public List<Event> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Event> events = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_event"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                events.add(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return events;
    }

    @Override
    public List<Event> getEventsInRoom(int id_room){
        PreparedStatement st = null;
        ResultSet rs= null;

        List<Event> events = new ArrayList<>();
        try{
            st = conn.prepareStatement(
                    "select * from tb_event where i_room_id = ?"
            );
            st.setInt(1, id_room);
            rs = st.executeQuery();
            while (rs.next()) {
                events.add(build(rs));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return events;
    }

    @Override
    public List<Event> getEventsByCreator(String username) {
        PreparedStatement st = null;
        ResultSet rs= null;

        List<Event> events = new ArrayList<>();
        try{
            st = conn.prepareStatement(
                    "select * from tb_event where vc_creator_username = ?"
            );
            st.setString(1, username);
            rs = st.executeQuery();
            while (rs.next()) {
                events.add(build(rs));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return events;
    }

    @Override
    public int insert(String name, int roomId, String groupName, String creatorUsername, LocalDateTime startDate, int duration) {
        PreparedStatement st = null;
        ResultSet rs = null;

        int id = 0;
        try {
            st = conn.prepareStatement(
                    "insert into tb_event(vc_name, i_room_id, vc_group_name, vc_creator_username, d_date_start, t_duration) " +
                            "values (?,?,?,?,?,?)"
            );
            st.setString(1,name);
            st.setInt(2,roomId);
            st.setString(3,groupName);
            st.setString(4,creatorUsername);
            st.setObject(5,startDate);
            st.setObject(6,duration);
            st.executeUpdate();

            st = conn.prepareStatement("select LAST_INSERT_ID()");
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return id;
    }

    @Override
    public void delete(int id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_event where i_id= ?"
            );
            st.setInt(1,id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public final Event build(ResultSet rs) throws SQLException {
        Room room   = DBManager.getRoomDao().get(rs.getInt("i_room_id"));
        Group group = DBManager.getGroupDao().get(rs.getString("vc_group_name"));
        User user   = DBManager.getUserDao().get(rs.getString("vc_name"));

        return (room == null || group == null || user == null) ? null : Event.make(
                rs.getInt("id_int"),
                rs.getString("vc_name"),
                room,
                group,
                user,
                ((LocalDateTime)rs.getObject("d_date_start")),
                ((int)rs.getObject("t_duration"))
            );
    }
}
