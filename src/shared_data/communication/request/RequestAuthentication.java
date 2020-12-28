package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

public class RequestAuthentication extends Request implements Serializable {

    private String username;
    private String password;
    private int socketTCPclient;

    public int getSocketTCPclient() {
        return socketTCPclient;
    }

    public RequestAuthentication(String ip, int port, String username, String password, int socketTCPclient) {
        super(ip, port);
        this.username = username;
        this.password = password;
        this.socketTCPclient = socketTCPclient;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
