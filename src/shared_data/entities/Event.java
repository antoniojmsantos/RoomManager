package shared_data.entities;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.lang.Object;

public class Event {
    private int id;
    private String name;
    private Room room;
    private Group group;
    private LocalDateTime start;
    private Duration duration;

    public Event(int id, String name, Room room, Group group, LocalDateTime start, Duration duration){
        this.id=id;
        this.name=name;
        this.room=room;
        this.group=group;
        this.start=start;
        this.duration=duration;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public Room getRoom(){return room;}
    public void setRoom(Room room){this.room=room;}

    public Group getGroup(){return group;}
    public void setGroup(Group group){this.group=group;}

    public LocalDateTime getStart(){return start;}
    public void setStart(LocalDateTime start){this.start=start;}

    public Duration getDuration(){return duration;}
    public void setDuration(Duration duration){this.duration=duration;}

    public static Event make(int id, String name, Room room , Group group, LocalDateTime start, Duration duration){
        return new Event(id, name, room,group, start, duration);
    }

}
