package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

public class RequestRegister extends Request implements Serializable {

    private String username;
    private String user;
    private String password;
    private Boolean permissionLevel;

    public RequestRegister(String ip, int port,String username, String user, String password, Boolean permissionLevel) {
        super(ip, port);
        this.username = username;
        this.user = user;
        this.password = password;
        this.permissionLevel = permissionLevel;
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

    public Boolean getPermissionLevel() {
        return permissionLevel;
    }
}
