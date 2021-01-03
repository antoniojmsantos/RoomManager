package database.dao;

import database.DBManager;
import shared_data.entities.Room;
import shared_data.entities.RoomFeature;
import shared_data.entities.RoomType;
import shared_data.helper.TimePeriod;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDao implements IRoomDao {

    private final Connection conn;

    public RoomDao(Connection conn) {this.conn = conn;}

    @Override
    public Room get(int roomId) {
        PreparedStatement st = null;
        ResultSet rs= null;

        try{
            st = conn.prepareStatement(
                    "select * from tb_room where i_id = ?"
            );
            st.setInt(1, roomId);
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
    public void insert(String name, int capacity, RoomType type, List<RoomFeature> features) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_room(vc_name, i_capacity, vc_type) " +
                            "values (?,?,?)"
            );
            st.setString(1,name);
            st.setInt(2,capacity);
            st.setString(3, type.toString());
            st.executeUpdate();

            st = conn.prepareStatement(
                    "select last_insert_id() as res"
            );
            rs = st.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("res");
                for (RoomFeature feature : features) {
                    insertFeature(roomId,feature);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
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
    public void updateName(int roomId, String name) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "update tb_room set vc_name = ? where i_id = ?"
            );
            st.setString(1, name);
            st.setInt(2,roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void updateCapacity(int roomId, int capacity) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "update tb_room set i_capacity = ? where i_id = ?"
            );
            st.setInt(1,capacity);
            st.setInt(2,roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public List<RoomFeature> getFeatures(int roomId) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<RoomFeature> features = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_room_feature where i_room_id = ?"
            );
            st.setInt(1,  roomId);
            rs = st.executeQuery();
            while(rs.next()) {
                features.add(RoomFeature.value(rs.getString("vc_name")));
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
    public void insertFeature(int roomId, RoomFeature feature) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_room_feature(i_room_id, vc_name) values (?,?)"
            );
            st.setInt(1, roomId);
            st.setString(2,feature.toString());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    @Override
    public void deleteFeature(int roomId, RoomFeature feature) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_room_feature where i_room_id = ? and vc_name = ?"
            );
            st.setInt(1,roomId);
            st.setString(2,feature.toString());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    private List<TimePeriod> getSchedule(int roomId) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<TimePeriod> schedule = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_event where i_room_id = ?"
            );
            st.setInt(1,  roomId);
            rs = st.executeQuery();
            while(rs.next()) {
                schedule.add(TimePeriod.make(
                        ((Timestamp) rs.getObject("d_date_start")).toLocalDateTime(),
                        rs.getInt("i_duration")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return schedule;
    }

    // own
    @Override
    public final Room build(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("i_id"),
                rs.getString("vc_name"),
                rs.getInt("i_capacity"),
                RoomType.value(rs.getString("vc_type")),
                getFeatures(rs.getInt("i_id")),
                getSchedule(rs.getInt("i_id")));
    }
}
