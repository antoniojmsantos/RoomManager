package database.dao;

import shared_data.entities.Room;

import java.util.List;
import java.util.Optional;

public interface IRoomDao {
    Room get(int id);
    List<Room> getAll();
    void insert(Room room);
    void delete(int id);

    List<String> getFeatures(int roomId);
    void insertFeature(int roomId, Room.Feature feature);
    void deleteFeature(int roomId, Room.Feature feature);
}
