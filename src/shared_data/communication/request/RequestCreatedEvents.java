package shared_data.communication.request;

import shared_data.communication.Request;
import shared_data.entities.User;

import java.io.Serializable;

public class RequestCreatedEvents extends Request implements Serializable {
    private User user;

    public RequestCreatedEvents(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
