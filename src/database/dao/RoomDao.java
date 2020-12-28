package database.dao;

import database.DBManager;
import shared_data.entities.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDao implements IRoomDao {

    private final Connection conn;

    public RoomDao(Connection conn) {this.conn = conn;}

    @Override
    public Room get(int id) {
        PreparedStatement st = null;
        ResultSet rs= null;

        try{
            st = conn.prepareStatement(
                    "select * from tb_room where i_id = ?"
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
                    "insert into tb_room(i_id, vc_name, i_capacity) " +
                            "values (?,?,?)"
            );
            st.setInt(1,room.getId());
            st.setString(2,room.getName());
            st.setInt(3,room.getCapacity());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void delete(int roomId) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_room where i_id= ?"
            );
            st.setInt(1,roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public List<String> getFeatures(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<String> features = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_room_feature where i_room_id = ?"
            );
            st.setInt(1,  id);
            rs = st.executeQuery();
            while(rs.next()) {
                features.add(
                        rs.getString("vc_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }

        return features;
    }

    @Override
    public void insertFeature(int id, Room.Feature feature) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_room_feature(i_room_id, vc_name) values (?,?)"
            );
            st.setInt(1, id);
            st.setString(2,feature.toString());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void deleteFeature(int id, Room.Feature feature) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_room_feature where i_room_id = ? and vc_name = ?"
            );
            st.setInt(1,id);
            st.setString(2,feature.toString());
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
                rs.getInt("i_id"),
                rs.getString("vc_name"),
                rs.getInt("i_capacity"));
    }
}
