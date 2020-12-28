package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.User;

import java.io.Serializable;

public class ResponseAuthentication extends Response implements Serializable {

    private final boolean authenticated;
    private User user;

    public ResponseAuthentication(String ip, int port, boolean authenticated,User user) {
        super(ip, port);
        this.authenticated = authenticated;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
