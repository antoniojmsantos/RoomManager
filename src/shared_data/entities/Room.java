package shared_data.entities;

public class Room {

    private int id;
    private String name;
    private String details;
    private int capacity;

    public Room(int id,String name, String details, int capacity) {
        this.id=id;
        this.name = name;
        this.details = details;
        this.details = details;
    }


    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
