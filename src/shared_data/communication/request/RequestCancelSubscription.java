package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

public class RequestCancelSubscription extends Request implements Serializable {

    private int idEvent;
    private User user;

    public RequestCancelSubscription(int idEvent,User user) {
        this.idEvent = idEvent;
        this.user = user;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public User getUser() {
        return user;
    }
}