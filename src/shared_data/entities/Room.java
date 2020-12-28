package shared_data.entities;

public class Room {

    public enum Feature {
        AR_CONDICIONADO,
        COMPUTADORES_MAC,
        COMPUTADORES_WINDOWS,
        PROJETOR,
        QUADRO_INTERATIVO
    }

    private int id;
    private String name;
    private int capacity;

    public Room(int id,String name, int capacity) {
        this.id=id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
