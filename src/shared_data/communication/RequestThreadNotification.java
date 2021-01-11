package shared_data.communication;

import shared_data.entities.Event;

import java.io.Serializable;

/**
 * Classe que representa a notificação enviado ao cliente quando
 * existe uma nova atualização nos eventos
 */
public class RequestThreadNotification extends Request implements Serializable {

    //Evento a qual sofreu alteração
    private Event event;
    //Tipo da notificação (criado ou apagado)
    private String type;

    public RequestThreadNotification(Event event, String type) {
        this.event = event;
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public String getType() {
        return type;
    }
}
