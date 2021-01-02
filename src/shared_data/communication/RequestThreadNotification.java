package shared_data.communication;

import shared_data.entities.Event;

import java.io.Serializable;

public class RequestThreadNotification extends Request implements Serializable {

    private Event event;
    private String type;

    public RequestThreadNotification(Event event, String type) {
        this.event = event;
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public String getType() {
        return type;
    }
}
