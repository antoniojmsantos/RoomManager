package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Event;
import shared_data.entities.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseAuthentication extends Response implements Serializable {

    private final boolean authenticated;
    private User user;
    private ArrayList<Event> pendingEvents;
    private ArrayList<Event> registeredEvents;

    public ResponseAuthentication(String ip, int port, boolean authenticated,User user,ArrayList<Event> pendingEvents,ArrayList<Event> registeredEvents) {
        super(ip, port);
        this.authenticated = authenticated;
        this.user = user;
        this.pendingEvents = pendingEvents;
        this.registeredEvents = registeredEvents;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public ArrayList<Event> getPendingEvents() {
        return pendingEvents;
    }

    public ArrayList<Event> getRegisteredEvents() {
        return registeredEvents;
    }
}
