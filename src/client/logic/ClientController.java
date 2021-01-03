package client.logic;

import client.communication.ClientCommunication;
import shared_data.entities.Event;
import shared_data.entities.Room;
import shared_data.helper.MyMutex;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ClientController {

    private final ClientCommunication communication;
    private final State state =  new State(State.TYPE.AUTHENTICATE);

    public ClientController(MyMutex mutex) {
        communication = new ClientCommunication(mutex);
    }
    public ClientCommunication getCommunication(){
        return communication;
    }

    public void init() {
        communication.run();
    }

    public State getState(){
        return state;
    }


    public boolean isStateRegister(){ return state.isRegister();}

    public boolean isStateAuthentication() {
        return state.isAuthentication();
    }

    public boolean isStateMain() {
        return state.isMain();
    }

    public boolean isStateCreate(){return state.isCreate();}

    public boolean isHighPermission(){
        return communication.getLoggedUser().getPermissions();
    }

    public boolean isAuthenticated(){
        return communication.getLoggedUser() != null;
    }

    public String getUsername(){
        return communication.getLoggedUser().getUsername();
    }


    public void setStateAuthentication() {
        state.setType(State.TYPE.AUTHENTICATE);
    }

    public void setStateMain() {
        state.setType(State.TYPE.MAIN);
    }

    public void setStateCreate(){
        state.setType(State.TYPE.CREATE_EVENT);
    }

    public void setStateRegister(){
        state.setType(State.TYPE.REGISTER);
    }


    public void Logout(){
        setStateAuthentication();
    }

    public boolean Authentication(String username, String password){
        if(communication.Authenticate(username, password)){
            setStateMain();
            return true;
        }
        else
            return false;
    }

    public boolean Register(String name, String username, String password) {
        if(communication.Register(name, username, password)){
            setStateAuthentication();
            return true;
        }
        else
            return false;
    }

    public boolean CreateEvent(String name, int idRoom, String nameGroup, LocalDateTime initialDate, int durationMin){
        return communication.CreateEvent(name, idRoom, nameGroup, getUsername(), initialDate, durationMin);
    }

    public boolean isPasswordAccepted(String password){
        return password.length() >= 8 && !password.equals(password.toLowerCase()) && password.matches(".*\\d.*");
    }

    public ArrayList<Room> getRooms(){
        return communication.getRooms();
    }

    public boolean isRoomAvailable(int roomId, LocalDateTime startDate, int durationInMinutes){
        return true;
    }

    public ArrayList<Event> getEventsCreated() {
        return communication.getEventsCreated();
    }

    public boolean deleteEvent(int id) {
        return communication.deleteEvent(id);
    }

    public ArrayList<Event> getUserEvents() {
        return communication.getUserEvents();
    }

    public ArrayList<Event> getPendingEvents() {
        return communication.getPendingEvents();
    }
}
