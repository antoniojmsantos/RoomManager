package shared_data.entities;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {

    private final int id;
    private final String name;
    private final RoomType type;
    private final List<RoomFeature> features;
    private final int capacity;

    public Room(int id,String name, int capacity, RoomType type, List<RoomFeature> features) {
        this.id=id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.features = features;
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

    @Override
    public String toString() {
        return name;
    }
}
