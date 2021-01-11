package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

/**
 * Request que representa o primeiro contacto quando entra no sistema
 */
public class RequestFirstContact extends Request implements Serializable {

    public RequestFirstContact(String ip, int port) {
        super(ip, port);
    }
}
