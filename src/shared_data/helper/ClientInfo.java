package shared_data.helper;

import shared_data.entities.User;

import java.net.Socket;

public class ClientInfo {

    private final Socket callBackSocket;
    private final User user;

    public ClientInfo(Socket callBackSocket, User user) {
        this.callBackSocket = callBackSocket;
        this.user = user;
    }

    public Socket getCallBackSocket() {
        return callBackSocket;
    }

    public User getUser() {
        return user;
    }
}
