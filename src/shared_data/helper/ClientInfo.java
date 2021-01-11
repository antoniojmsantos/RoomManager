package shared_data.helper;

import shared_data.entities.User;

import java.net.Socket;

/**
 * Representa a informção de um User em runtime
 */
public class ClientInfo {

    //Socket para enviar notificações
    private final Socket callBackSocket;
    //User logado
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
