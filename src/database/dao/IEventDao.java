package database.dao;

import shared_data.entities.Event;

import java.util.List;
import java.util.Optional;

public interface IEventDao {
    Event get(int id);
    List<Event> getAll();
    void insert (Event event);
    void delete(Event event);
}
