package database.dao;

import shared_data.entities.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IEventDao {
    Event get(int id);
    List<Event> getAll();
    int insert(String name, int roomId, String groupName, String creatorUsername, LocalDateTime startDate, int duration);
    boolean delete(int id);

    boolean acceptEvent(int eventId, String userUsername);
    boolean refuseEvent(int eventId, String userUsername);
    public boolean cancelEvent(int eventId, String userUsername);

    List<Event> getEventsInRoom(int id_room);
    List<Event> getEventsByCreator(String username);

    // own
    Event build(ResultSet rs) throws SQLException;
}
