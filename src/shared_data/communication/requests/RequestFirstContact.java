package shared_data.communication.requests;

import shared_data.communication.Request;

import java.io.Serializable;

public class RequestFirstContact extends Request implements Serializable {

    public RequestFirstContact(String ip, int port) {
        super(ip, port);
    }
}
