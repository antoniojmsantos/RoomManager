package database.dao;

import database.DBManager;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;
import shared_data.helper.TimePeriod;

import java.sql.*;
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

        System.out.println(username);

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
        for(Event e : events)
            System.out.println(e);
        return events;
    }

    private boolean canCreateEvent(int eventRoomId, LocalDateTime eventStartDate, LocalDateTime eventEndDate) {
        PreparedStatement st = null;
        ResultSet rs = null;

        boolean canCreate = true;
        try {
            st = conn.prepareStatement(
                    "select d_date_start, i_duration from tb_event where i_room_id = ?"
            );
            st.setInt(1, eventRoomId);
            rs = st.executeQuery();

            while (rs.next()) {
                if (TimePeriod.overlaps(
                        ((Timestamp)rs.getObject("d_date_start")).toLocalDateTime(),
                        ((Timestamp)rs.getObject("d_date_start")).toLocalDateTime().plusMinutes(rs.getInt("i_duration")),
                        eventStartDate,
                        eventEndDate
                )){
                    canCreate = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return canCreate;
    }

    @Override
    public int insert(String name, int roomId, String groupName, String creatorUsername, LocalDateTime startDate, int duration) {
        PreparedStatement st = null;
        ResultSet rs = null;

        int id = 0;
        try {
            // check if event can be created
            if (!canCreateEvent(roomId, startDate, startDate.plusMinutes(duration))) throw new SQLException();

            st = conn.prepareStatement(
                    "insert into tb_event(vc_name, i_room_id, vc_group_name, vc_creator_username, d_date_start, i_duration) " +
                            "values (?,?,?,?,?,?)"
            );
            st.setString(1,name);
            st.setInt(2,roomId);
            st.setString(3,groupName);
            st.setString(4,creatorUsername);
            st.setObject(5,startDate);
            st.setObject(6,duration);
            st.executeUpdate();

            // get event id
            st = conn.prepareStatement("select LAST_INSERT_ID()");
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            // insert into event subscription
            for (User user: DBManager.getGroupDao().getMembers(groupName)) {
                st = conn.prepareStatement(
                        "insert into tb_event_subscription(i_event_id, vc_user_username, vc_state) values(?,?,?)"
                );
                st.setInt(1, id);
                st.setString(2, user.getUsername());
                st.setString(3, "pending");
                st.executeUpdate();
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
    public boolean delete(int id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_event where i_id= ?"
            );
            st.setInt(1,id);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
        return false;
    }

    private boolean updateEventSubscriptionState(int eventId, String userUsername, String state) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "update tb_event_subscription set vc_state = ? where i_event_id = ? and vc_user_username = ?"
            );
            st.setString(1,state);
            st.setInt(2,eventId);
            st.setString(3,userUsername);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
        return false;
    }

    @Override
    public boolean acceptEvent(int eventId, String userUsername) {
        return updateEventSubscriptionState(eventId, userUsername, "accepted");
    }

    @Override
    public boolean refuseEvent(int eventId, String userUsername) {
        return updateEventSubscriptionState(eventId, userUsername, "refused");
    }

    @Override
    public boolean cancelEvent(int eventId, String userUsername) {
        return updateEventSubscriptionState(eventId, userUsername, "cancelled");
    }

    // own
    @Override
    public final Event build(ResultSet rs) throws SQLException {
        Room room   = DBManager.getRoomDao().get(rs.getInt("i_room_id"));
        Group group = DBManager.getGroupDao().get(rs.getString("vc_group_name"));
        User user   = DBManager.getUserDao().get(rs.getString("vc_creator_username"));

        return (room == null || group == null || user == null) ? null : Event.make(
                rs.getInt("i_id"),
                rs.getString("vc_name"),
                room,
                group,
                user,
                ((Timestamp)rs.getObject("d_date_start")).toLocalDateTime(),
                rs.getInt("i_duration")
            );
    }
}
