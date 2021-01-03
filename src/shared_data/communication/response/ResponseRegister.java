package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

public class ResponseRegister extends Response implements Serializable {

    private int result;

    public ResponseRegister(String ip, int port,int result) {
        super(ip, port);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
