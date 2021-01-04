package client.logic;

import client.communication.ClientCommunication;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.helper.MyMutex;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public int Register(String name, String username, String password) {
        int resultCode = communication.Register(name, username, password);
        switch (resultCode) {
            case -1:
                return -1;
            case 0:
                return 0;
            default:
                setStateAuthentication();
                return 1;
        }
    }

    public int CreateEvent(String name, int idRoom, String nameGroup, LocalDateTime initialDate, int durationMin){
        int errorCode = communication.CreateEvent(name, idRoom, nameGroup, getUsername(), initialDate, durationMin);
        if (errorCode > 0) {
            setStateMain();
        }
        return errorCode;
    }

    public boolean isEmailAccepted(String email) {
        return email.contains("@") && email.indexOf("@") > 0 && email.indexOf("@") < email.length()-1;
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

    public void acceptEvent(int id) {
        communication.acceptEventSubscription(id);
    }

    public void declineEvent(int id) {
        communication.declineEventSubscription(id);
    }

    public void cancelEvent(int id) {
        communication.cancelEventSubscription(id);
    }

    public List<Group> getGroups() {
        return communication.getAllGroups();
    }
}
