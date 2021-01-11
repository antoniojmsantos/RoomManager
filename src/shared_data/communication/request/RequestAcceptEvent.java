package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

/**
 * Request que representa a inscrição de um evento
 */
public class RequestAcceptEvent extends Request implements Serializable {
    private int idEvent;
    private User user;

    public RequestAcceptEvent(int idEvent, User user) {
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
