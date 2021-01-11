package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Resposta do pedido de obtenção dos eventos criados
 */
public class ResponseCreatedEvents extends Response implements Serializable {

    private ArrayList<Event> createdEvents;

    public ResponseCreatedEvents(ArrayList<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public ArrayList<Event> getCreatedEvents() {
        return createdEvents;
    }
}
