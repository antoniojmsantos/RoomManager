package client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientObservable {

    private ClientController controller;
    private PropertyChangeSupport support;

    public ClientObservable() {
        controller = new ClientController();
        support = new PropertyChangeSupport(this);
    }

    public void init() {
        controller.init();
    }

    public void logout(){
        controller.logout();
        support.firePropertyChange(null, null, null);
    }

    /* property change support */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // state

    public boolean isStateAuthentication(){
        return controller.isStateAuthentication();
    }

    public boolean isStateRegister(){
        return controller.isStateRegister();
    }

    public boolean isStateMain(){
        return controller.isStateMain();
    }

    public boolean isStateCreate(){return controller.isStateCreate();}

    public boolean Authentication(String username, String password){
        if(controller.Authentication(username, password)){
            support.firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }

    public void setStateRegister(){
        controller.setStateRegister();
        support.firePropertyChange(null, null, null);
    }

    public void setStateCreate(){
        controller.setStateCreate();
        support.firePropertyChange(null,null,null);
    }

    public void setStateMain(){
        controller.setStateMain();
        support.firePropertyChange(null, null, null);
    }
}
