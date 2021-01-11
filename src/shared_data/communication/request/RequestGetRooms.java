package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;

/**
 * Request que representa um pedido de uma lista de salas
 */
public class RequestGetRooms extends Request implements Serializable {

    public RequestGetRooms() {
    }
}
