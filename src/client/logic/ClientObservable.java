package client.logic;

import java.beans.PropertyChangeSupport;

public class ClientObservable extends PropertyChangeSupport {

    private ClientController controller;

    public ClientObservable(ClientController controller) {
        super(controller);
        this.controller = controller;
    }

    public void logout(){
        controller.logout();

        firePropertyChange(null, null, null);
    }


    public boolean isStateAuthentication(){
        return controller.isStateAuthentication();
    }

    public boolean isStateRegister(){
        return controller.isStateRegister();
    }

    public boolean isStateMain(){
        return controller.isStateMain();
    }

    public boolean Authentication(String usr, String password){
        if(controller.Authentication(usr, password)){
            firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }

    public void setStateRegister(){
        controller.setStateRegister();
    }
}
