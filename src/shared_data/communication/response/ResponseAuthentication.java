package shared_data.communication.response;

import shared_data.communication.Response;

public class ResponseAuthentication extends Response {

    private final boolean authenticated;

    public ResponseAuthentication(String ip, int port, boolean authenticated) {
        super(ip, port);
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
