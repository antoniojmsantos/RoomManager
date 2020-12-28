package client.communication.threads;

import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.Socket;

public class ThreadListenPendentEvents extends Thread {

    private Socket socketCallBack;

    public ThreadListenPendentEvents(Socket socketCallBack) {
        this.socketCallBack = socketCallBack;
    }

    @Override
    public void run() {

        System.out.println("À escuta");
        while(KeepAlive.getKeepAlive()){
            try {
                SendAndReceiveData.receiveData(socketCallBack);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
