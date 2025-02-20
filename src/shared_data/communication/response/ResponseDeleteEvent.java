package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

/**
 * Resposta ao pedido de apagar um evento
 */
public class ResponseDeleteEvent extends Response implements Serializable {

    private boolean result;

    public ResponseDeleteEvent(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
