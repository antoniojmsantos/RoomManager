package shared_data.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Event implements Serializable {
    private final int id;
    private String name;
    private Room room;
    private Group group;
    private final User creator;
    private LocalDateTime startDate;
    private int duration;

    public Event(int id, String name, Room room, Group group, User creator, LocalDateTime startDate, int duration){
        this.id=id;
        this.name=name;
        this.room=room;
        this.group=group;
        this.creator = creator;
        this.startDate = startDate;
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

    public LocalDateTime getStartDate(){return startDate;}
    public void setStartDate(LocalDateTime startDate){this.startDate = startDate;}

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

    @Override
    public String toString() {
        return name;
    }
}
