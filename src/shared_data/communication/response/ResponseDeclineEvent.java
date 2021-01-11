package shared_data.communication.response;

import shared_data.communication.Response;

import java.io.Serializable;

/**
 * Resposta ao pedido de recusar um convite de um evento
 */
public class ResponseDeclineEvent extends Response implements Serializable {

    private boolean result;

    public ResponseDeclineEvent(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
