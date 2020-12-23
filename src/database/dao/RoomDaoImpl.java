package database.dao;

import database.DBManager;
import shared_data.entities.Room;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoImpl implements RoomDao {

    private final Connection conn;

    public RoomDaoImpl(Connection conn) {this.conn = conn;}


    @Override
    public Optional<Room> get(int id) {
        PreparedStatement st = null;
        ResultSet rs= null;

        try{
            st = conn.prepareStatement(
                    "select * from tb_room where i_id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(build(rs));
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
    public List<Room> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Room> rooms = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_room"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                rooms.add(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return rooms;
    }

    @Override
    public void insert(Room room) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_user(i_id, vc_name, vc_details, i_capacity) " +
                            "values (?,?,?,?)"
            );
            st.setInt(1,room.getId());
            st.setString(2,room.getName());
            st.setString(3,room.getDetails());
            st.setInt(4,room.getCapacity());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void delete(Room room) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_room where i_id= ?"
            );
            st.setInt(1,room.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    // own
    private Room build(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("details"),
                rs.getInt("capacity"));
    }
}
