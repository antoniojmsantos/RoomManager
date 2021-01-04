package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.communication.RequestThreadNotification;
import shared_data.entities.Event;
import shared_data.helper.ClientInfo;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.util.ArrayList;

public class NotifyPendentEvents extends Thread {

    private ServerLogic serverLogic;
    private Event event;
    private String type;

    public NotifyPendentEvents(Event event, ServerLogic serverLogic, String type) {
        this.serverLogic = serverLogic;
        this.event = event;
        this.type = type;
    }

    /**
     * Envia uma notificação ao cliente através do socketCallback
     *
     * Primeiro vai buscar a lista dos users logados no momento e verifica se estão no grupo do evento
     * Depois envia uma notificação através do socketCallBack
     */
    @Override
    public void run() {
        ArrayList<ClientInfo> clientToSendNotification = serverLogic.getClientInfo(event);
        for(int i = 0; i < clientToSendNotification.size();i++){
            try {
                RequestThreadNotification requestThreadNotification = new RequestThreadNotification(event,type);
                SendAndReceiveData.sendData(requestThreadNotification,clientToSendNotification.get(i).getCallBackSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
