package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Room;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Representa a resposta do pedido de obtenção das salas
 */
public class ResponseGetRooms extends Response implements Serializable {

    private ArrayList<Room> rooms;

    public ResponseGetRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
