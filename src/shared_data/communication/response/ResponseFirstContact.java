package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

public class ResponseFirstContact extends Response implements Serializable {

    private String ipServerTCP;
    private int portTCP;

    private static final long serialVersionUID = 42L;


    public ResponseFirstContact(String ip, int port,String ipServerTCP, int portTCP) {
        super(ip, port);
        this.ipServerTCP = ipServerTCP;
        this.portTCP = portTCP;
    }

    public String getIpServerTCP() {
        return ipServerTCP;
    }

    public int getPortTCP() {
        return portTCP;
    }

    @Override
    public String toString() {
        return "First Contact @ "+ipServerTCP +":"+ portTCP;
    }
}
