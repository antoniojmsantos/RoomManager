package database.dao;

import shared_data.entities.Room;
import shared_data.entities.RoomFeature;
import shared_data.entities.RoomType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRoomDao {
    Room get(int roomId);
    List<Room> getAll();
    void insert(String name, int capacity, RoomType type, List<RoomFeature> features);
    void delete(int roomId);

    void updateName(int roomId, String name);
    void updateCapacity(int roomId, int capacity);

    List<RoomFeature> getFeatures(int roomId);
    void insertFeature(int roomId, RoomFeature feature);
    void deleteFeature(int roomId, RoomFeature feature);

    // own
    Room build(ResultSet rs) throws SQLException;
}
