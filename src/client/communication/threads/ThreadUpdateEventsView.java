package client.communication.threads;

import client.logic.ClientObservable;
import javafx.application.Platform;
import shared_data.helper.KeepAlive;
import shared_data.helper.MyMutex;

public class ThreadUpdateEventsView extends Thread{

    private MyMutex mutex;
    private ClientObservable observable;

    public ThreadUpdateEventsView(MyMutex mutex, ClientObservable observable){
        this.mutex = mutex;
        this.observable = observable;
    }



    @Override
    public void run() {
        while(KeepAlive.getKeepAlive()) {
            synchronized (mutex) {
                try {
                    mutex.wait();
                    Platform.runLater(() -> observable.refresh());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
