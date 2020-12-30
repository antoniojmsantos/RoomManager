package shared_data.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.lang.Object;

public class Event implements Serializable {
    private final int id;
    private String name;
    private Room room;
    private Group group;
    private final User creator;
    private LocalDateTime start;
    private int duration;

    public Event(int id, String name, Room room, Group group, User creator, LocalDateTime start, int duration){
        this.id=id;
        this.name=name;
        this.room=room;
        this.group=group;
        this.creator = creator;
        this.start=start;
        this.duration=duration;
    }

    public int getId(){return id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public Room getRoom(){return room;}
    public void setRoom(Room room){this.room=room;}

    public Group getGroup(){return group;}
    public void setGroup(Group group){this.group=group;}

    public User getCreator() {
        return creator;
    }

    public LocalDateTime getStart(){return start;}
    public void setStart(LocalDateTime start){this.start=start;}

    public int getDuration(){return duration;}
    public void setDuration(int duration){this.duration=duration;}

    public static Event make(
            int id,
            String name,
            Room room ,
            Group group,
            User creator,
            LocalDateTime start,
            int duration)
    {
        return new Event(id, name, room,group, creator, start, duration);
    }

}
