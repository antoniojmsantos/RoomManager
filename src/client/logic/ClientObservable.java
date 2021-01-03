package client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import client.communication.threads.ThreadUpdateEventsView;
import shared_data.entities.*;
import shared_data.helper.MyMutex;

public class ClientObservable {

    private ClientController controller;
    private PropertyChangeSupport support;

    public ClientObservable(MyMutex mutex) {
        controller = new ClientController(mutex);
        support = new PropertyChangeSupport(this);
    }

    public void init() {
        controller.init();
    }

    public ClientController getController(){
        return controller;
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

    public boolean isPasswordAccepted(String password) {return controller.isPasswordAccepted(password);}

    public ArrayList<Room> getRooms(){
        return controller.getRooms();
    }

    public boolean isRoomAvailable(int roomId, LocalDateTime startDate, int durationInMinutes){
        return controller.isRoomAvailable(roomId, startDate, durationInMinutes);
    }

    public ArrayList<Event> getEventsCreated() {
        return controller.getEventsCreated();
    }

    public ArrayList<Event> getUserEvents() {
        return controller.getUserEvents();
    }

    public ArrayList<Event> getPendingEvents() {
        return controller.getPendingEvents();
    }

    public boolean deleteEvent(int id){

        if(controller.deleteEvent(id)){
            support.firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }



    public void refreshEvents() {
        //chamaar a função que vai ao server buscar os pending events
        //fazer o fire property change
    }
}
