package client.communication.threads;

import shared_data.communication.RequestThreadNotification;
import shared_data.helper.KeepAlive;
import shared_data.helper.MyMutex;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ThreadListenPendentEvents extends Thread {

    private MyMutex mutex;
    private Socket socketCallBack;
    private RequestThreadNotification lastNotification = null;

    public ThreadListenPendentEvents(Socket socketCallBack,MyMutex mutex) throws SocketException {
        this.socketCallBack = socketCallBack;
        this.mutex = mutex;
        this.socketCallBack.setSoTimeout(50);
    }

    @Override
    public void run() {

        while(KeepAlive.getKeepAlive()){
            synchronized (mutex){
                try {
                    lastNotification = (RequestThreadNotification)SendAndReceiveData.receiveData(socketCallBack);
                    mutex.notify();
                }catch (SocketTimeoutException e){
                    continue;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
