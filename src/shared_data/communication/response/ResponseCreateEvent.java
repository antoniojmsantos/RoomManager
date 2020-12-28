package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;

import java.io.Serializable;

public class ResponseCreateEvent extends Response implements Serializable {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public ResponseCreateEvent(Event event) {
        this.event = event;
    }
}
