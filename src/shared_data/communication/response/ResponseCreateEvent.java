package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;

import java.io.Serializable;

public class ResponseCreateEvent extends Response implements Serializable {

    private static final long serialVersionUID = 42L;
    private Event event;
    private int errorCode;

    public Event getEvent() {
        return event;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ResponseCreateEvent(Event event, int errorCode) {
        this.event = event;
        this.errorCode = errorCode;
    }
}
