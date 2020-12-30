package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.entities.Event;
import shared_data.helper.ClientInfo;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.util.ArrayList;

public class NotifyPendentEvents extends Thread {

    private ServerLogic serverLogic;
    private Event event;

    public NotifyPendentEvents(Event event, ServerLogic serverLogic) {
        this.serverLogic = serverLogic;
        this.event = event;
    }

    @Override
    public void run() {
        ArrayList<ClientInfo> clientToSendNotification = serverLogic.getClientInfo(event);
        for(int i = 0; i < clientToSendNotification.size();i++){
            try {
                SendAndReceiveData.sendData(event,clientToSendNotification.get(i).getCallBackSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
