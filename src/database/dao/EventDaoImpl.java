package database.dao;

import database.DBManager;
import shared_data.entities.Event;
import shared_data.entities.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDaoImpl implements EventDao{

    private final Connection conn;

    public EventDaoImpl(Connection conn) {this.conn = conn;}

    @Override
    public Optional<Event> get(int id) {
        PreparedStatement st = null;
        ResultSet rs= null;

        try{
            st = conn.prepareStatement(
                    "select * from tb_event where i_id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                //return Optional.of(build(rs));
                return Optional.empty();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return Optional.empty();

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
              //  events.add(build(rs));
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
    public void insert(Event event) {           //ver melhor, pq ha mais cenas do q id e nome
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_user(i_id, vc_name, vc_details, i_capacity) " +
                            "values (?,?,?,?)"
            );
            st.setInt(1,event.getId());
            st.setString(2,event.getName());
            //...
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
/*
    private Event build(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getLocalDateTime(),
           //     rs.getD
                );
    }
    */

}
