package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponsePendingEvents extends Response implements Serializable {

    private ArrayList<Event> userEvents;

    public ResponsePendingEvents(ArrayList<Event> userEvents) {

        this.userEvents = userEvents;
    }

    public ArrayList<Event> getUserEvents() {
        return userEvents;
    }
}
