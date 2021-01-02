package client.logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<Event> getEventsCreated() {

//        ArrayList<Event> events = new ArrayList<>();
//        events.add(new Event(0, "SO_P3_L1.1", new Room(0, "L1.1", 10, RoomType.AUDITORIO, List.of(
//                RoomFeature.AR_CONDICIONADO,
//                RoomFeature.COMPUTADORES_WINDOWS,
//                RoomFeature.PROJETOR
//        )),
//                new Group("P1"),new User("pedrito@gmail.com","Pedro","1234", true), LocalDateTime.now(), 120));
//        events.add(new Event(1, "IRC_P1_L2.1", new Room(1, "L2.1", 15,RoomType.LABORATORIO,List.of(
//                RoomFeature.AR_CONDICIONADO,
//                RoomFeature.QUADRO_INTERATIVO
//        )),
//                new Group("P3"),new User("rodigo@gmail.com","Rodrigo","1234", false), LocalDateTime.now(), 130));
//return events;
        return controller.getEventsCreated();
    }

    public boolean deleteEvent(int id){

        if(controller.deleteEvent(id)){
            support.firePropertyChange(null, null, null);
            return true;
        }
        else
            return false;
    }
}
