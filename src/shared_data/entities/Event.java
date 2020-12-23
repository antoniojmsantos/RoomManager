package shared_data.entities;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event {

    int id;
    String name;
    LocalDateTime start;
    Duration duration;
    Room room;
    //Group group;

    public Event(int id, String name, LocalDateTime start, Duration duration, Room room){
        this.id=id;
        this.name=name;
        this.start=start;
        this.duration=duration;
        this.room=room;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public LocalDateTime getStart(){return start;}
    public void setStart(LocalDateTime start){this.start=start;}

    public Duration getDuration(){return duration;}
    public void setDuration(Duration duration){this.duration=duration;}

    public Room getRoom(){return room;}
    public void setRoom(Room room){this.room=room;}

}
