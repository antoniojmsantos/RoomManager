package client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import shared_data.entities.*;

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

    public boolean isHighPermission(){
        return controller.isHighPermission();
    }

    public boolean isAuthenticated(){
        return controller.isAuthenticated();
    }

    public String getUsername(){
        return controller.getUsername();
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


    public void Logout(){
        controller.Logout();
        support.firePropertyChange(null, null, null);
    }



    public boolean Authentication(String username, String password){
        if(controller.Authentication(username, password)){
            support.firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }

    public boolean Register(String name, String username, String password){
        if(controller.Register(name, username, password)){
            support.firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }

    public boolean CreateEvent(int idRoom, String nameGroup, String name, LocalDateTime initialDate, int durationMin){

        return controller.CreateEvent(idRoom, nameGroup, name, initialDate, durationMin);
    }

    public ArrayList<Room> getRooms(String name){

        return controller.getRooms();
    }

    public boolean isRoomAvailable(int id){
        return controller.isRoomAvailable(id);
    }
}
