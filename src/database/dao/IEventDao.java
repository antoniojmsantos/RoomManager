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
    int insert(String name, int roomId, String groupName, LocalDateTime startDate, Duration duration);
    void delete(int id);

    // own
    Event build(ResultSet rs) throws SQLException;
}
