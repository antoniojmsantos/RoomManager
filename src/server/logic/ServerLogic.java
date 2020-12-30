package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;
import shared_data.helper.ClientInfo;

import java.net.Socket;
import java.util.ArrayList;

import static database.DBManager.getGroupDao;

public final class ServerLogic {

    private ArrayList<ClientInfo> clientInfo;

    public ServerLogic(){
        clientInfo = new ArrayList<>();
        DBManager.init(
                "jdbc:mysql://localhost:3306/db_room_manager?serverTimezone=Europe/Lisbon",
                "admin",
                "pass1234"
        );
    }

    public User getAuthenticate(String username, String password) {
        if (DBManager.getUserDao().authenticate(username, password)) {
            return DBManager.getUserDao().get(username);
            // success
        } else {
            return null;
            // failure
        }
    }
    public boolean registerUsers(RequestRegister register){
        return DBManager.getUserDao().insert(register.getUsername(), register.getUser(), register.getPassword(), register.getPermissionLevel());
    }

    public void addToClientInfo(Socket socketCallBack, User user) {
        synchronized (clientInfo){
            clientInfo.add(new ClientInfo(socketCallBack,user));
        }
    }

    public Event createEvent(RequestCreateEvent requestCreateEvent) {
        int idEvent = DBManager.getEventDao().insert(
                requestCreateEvent.getName(),
                requestCreateEvent.getIdRoom(),
                requestCreateEvent.getNameGroup(),
                requestCreateEvent.getUsernameCreator(),
                requestCreateEvent.getInitialDate(),
                requestCreateEvent.getDuration());

        return DBManager.getEventDao().get(idEvent);
    }

    public ArrayList<ClientInfo> getClientInfo(Event event) {
        ArrayList<ClientInfo> clientInEvent = new ArrayList<>();
        synchronized (this.clientInfo){
            ArrayList<User> users = (ArrayList<User>) DBManager.getGroupDao().getMembers(event.getGroup().getName());

            for(int i = 0; i < clientInfo.size(); i++){
                for(int j = 0; j < users.size(); j++){
                    if(clientInfo.get(i).getUser().getUsername().equals(users.get(j).getUsername())){
                        clientInEvent.add(clientInfo.get(i));
                    }
                }
            }
        }
        return clientInEvent;
    }

    public ArrayList<Room> getAllRooms() {
        return (ArrayList<Room>) DBManager.getRoomDao().getAll();
    }

    public ArrayList<Event> getUserEvents(User user) {
        return (ArrayList<Event>) DBManager.getUserDao().getEventsAccepted(user.getUsername());
    }

    public ArrayList<Event> getUserPendingEvents(User user) {
        return (ArrayList<Event>) DBManager.getUserDao().getEventsPending(user.getUsername());
    }

    public ArrayList<Group> getAllGroups() {
        return (ArrayList<Group>) DBManager.getGroupDao().getAll();
    }

    public ArrayList<Event> getCreatedEvents(User user) {
        return (ArrayList<Event>) DBManager.getEventDao().getEventsByCreator(user.getUsername());
    }

    public ArrayList<User> getAllUsersInterface() {
        return (ArrayList<User>) DBManager.getUserDao().getAll();
    }


    public boolean getUserPermissions(String userid) {
            return DBManager.getUserDao().get(userid).isPermissions();
    }

    public void setPermissions(String userid,boolean i){
        DBManager.getUserDao().updatePermissions(userid,i);
    }
}
