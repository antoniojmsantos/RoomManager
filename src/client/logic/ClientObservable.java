package client.logic;

import java.beans.PropertyChangeSupport;

public class ClientObservable extends PropertyChangeSupport {

    private ClientController controller;

    public ClientObservable(ClientController controller) {
        super(controller);
        this.controller = controller;
    }

}
