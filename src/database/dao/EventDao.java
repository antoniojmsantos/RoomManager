package database.dao;

import database.DBManager;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;

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
    public void insert(Event event) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_event(i_id, vc_name, i_room_id, vc_group_name, d_date_start, t_duration) " +
                            "values (?,?,?,?,?,?)"
            );
            st.setInt(1,event.getId());
            st.setString(2,event.getName());
            st.setInt(3,event.getRoom().getId());
            st.setString(4,event.getGroup().getName());
            st.setObject(5,event.getStart());
            st.setObject(6,event.getDuration());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void delete(Event event) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_event where i_id= ?"
            );
            st.setInt(1,event.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    private Event build(ResultSet rs) throws SQLException {
        Room room = DBManager.getRoomDao().get(rs.getInt("i_room_id"));
        Group group = DBManager.getGroupDao().get(rs.getString("vc_group_name"));

        return (room == null || group == null) ? null : Event.make(
                rs.getInt("id_int"),
                rs.getString("vc_name"),
                room,
                group,
                ((LocalDateTime)rs.getObject("d_date_start")),
                ((Duration)rs.getObject("t_duration"))
            );
    }
}
