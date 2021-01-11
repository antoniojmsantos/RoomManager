package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

/**
 * Request que representa o registo no sistema de um user
 */
public class RequestRegister extends Request implements Serializable {

    private String username;
    private String user;
    private String password;

    public RequestRegister(String ip, int port,String username, String user, String password) {
        super(ip, port);
        this.username = username;
        this.user = user;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
