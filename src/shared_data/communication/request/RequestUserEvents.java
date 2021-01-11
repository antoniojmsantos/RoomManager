package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

/**
 * Request que representa um pedido de listas de eventos inscritos
 */
public class RequestUserEvents extends Request implements Serializable {

    private User user;
    public RequestUserEvents(User loggedUser) {
        this.user = loggedUser;
    }

    public User getUser() {
        return user;
    }
}
