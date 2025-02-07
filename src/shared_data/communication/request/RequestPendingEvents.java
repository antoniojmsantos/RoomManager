package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

/**
 * Request que representa o pedido de uma lista de eventos pendentes
 */
public class RequestPendingEvents extends Request implements Serializable {

    private User user;

    public RequestPendingEvents(User loggedUser) {
        this.user = loggedUser;
    }

    public User getUser() {
        return user;
    }
}
