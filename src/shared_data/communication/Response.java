package shared_data.communication;

import java.io.Serializable;

/**
 * Classe que representa uma resposta
 */
public class Response implements Serializable {

    private String ip;
    private int port;

    public Response(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public Response(){

    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
