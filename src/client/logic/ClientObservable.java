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

    public int Register(String name, String username, String password){
        int resultCode = controller.Register(name, username, password);
        switch(resultCode){
            case -1:
            case 0:
                return resultCode;
            default:
                support.firePropertyChange(null,null,null);
                return 1;
        }
    }

    public int CreateEvent(String name, int idRoom, String nameGroup, LocalDateTime initialDate, int durationMin){
        int errorCode = controller.CreateEvent(name, idRoom, nameGroup, initialDate, durationMin);
        if(errorCode > 0) {
            support.firePropertyChange(null, null, null);
        }
        return errorCode;
    }

    public boolean isEmailAccepted(String email) {
        return controller.isEmailAccepted(email);
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

    public void acceptEvent(int id){
        controller.acceptEvent(id);
        support.firePropertyChange(null, null, null);
    }

    public void declineEvent(int id){
        controller.declineEvent(id);
        support.firePropertyChange(null, null, null);
    }

    public void refreshEvents() {
        //chamaar a função que vai ao server buscar os pending events
        //fazer o fire property change
    }
}
