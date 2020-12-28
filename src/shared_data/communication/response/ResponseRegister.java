package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

public class ResponseRegister extends Response implements Serializable {

    private boolean result;

    public ResponseRegister(String ip, int port,boolean result) {
        super(ip, port);
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }
}
