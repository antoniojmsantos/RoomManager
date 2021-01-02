package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

public class ResponseAcceptEvent extends Response implements Serializable {

    private boolean result;

    public ResponseAcceptEvent(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
