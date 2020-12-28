package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

public class RequestAuthentication extends Request implements Serializable {

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
