package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

/**
 * Request que representa o cancelamento de um evento pelo criador
 */
public class RequestDeleteEvent extends Request implements Serializable {

    private int idEvent;
    private User user;

    public RequestDeleteEvent(int idEvent, User user) {
        this.user = user;
        this.idEvent = idEvent;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public User getUser() {
        return user;
    }
}
