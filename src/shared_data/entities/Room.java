package shared_data.entities;

import shared_data.helper.TimePeriod;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Room implements Serializable {

    private final int id;
    private final String name;
    private final RoomType type;
    private final List<RoomFeature> features;
    private final int capacity;

    private final List<TimePeriod> schedule;

    public Room(
            int id,
            String name,
            int capacity,
            RoomType type,
            List<RoomFeature> features,
            List<TimePeriod> schedule
    ) {
        this.id=id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.features = features;
        this.schedule = schedule;
    }

    public int getId(){return id;}

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomType getType() {
        return type;
    }

    public List<RoomFeature> getFeatures() {
        return features;
    }

    public List<TimePeriod> getSchedule() {
        return schedule;
    }

    public boolean isAvailable(LocalDateTime from, int durationInMinutes) {
        return isAvailable(from, from.plusMinutes(durationInMinutes));
    }
    public boolean isAvailable(LocalDateTime from, LocalDateTime to) {
        System.out.println("DESIRED: "+from+" - "+to);
        for (TimePeriod period : schedule) {
            System.out.println(period);
            if (period.intersects(from) || period.intersects(to)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
