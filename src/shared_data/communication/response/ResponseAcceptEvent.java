package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

/**
 * Representa a resposta para o request de inscrever no evento
 */
public class ResponseAcceptEvent extends Response implements Serializable {

    private boolean result;

    public ResponseAcceptEvent(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
