package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Resposta onde devolve uma lista de eventos a que user está inscrito
 */
public class ResponseUserEvents extends Response implements Serializable {

    private ArrayList<Event> userEvents;

    public ResponseUserEvents(ArrayList<Event> userEvents) {
        this.userEvents = userEvents;
    }

    public ArrayList<Event> getUserEvents() {
        return userEvents;
    }
}
