package shared_data.communication.request;

import shared_data.communication.Request;

public class RequestAuthentication extends Request {

    private String username;
    private String password;

    public RequestAuthentication(String ip, int port, String username, String password) {
        super(ip, port);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
