package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.*;
import shared_data.helper.ClientInfo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    public int registerUsers(RequestRegister register){
        return DBManager.getUserDao().insert(register.getUsername(), register.getUser(), register.getPassword());
    }

    public void addToClientInfo(Socket socketCallBack, User user) {
        synchronized (clientInfo){
            clientInfo.add(new ClientInfo(socketCallBack,user));
        }
    }

    public int createEvent(RequestCreateEvent requestCreateEvent) {
        return DBManager.getEventDao().insert(
                requestCreateEvent.getName(),
                requestCreateEvent.getRoomId(),
                requestCreateEvent.getGroupName(),
                requestCreateEvent.getCreatorUsername(),
                requestCreateEvent.getStartDate(),
                requestCreateEvent.getDuration());
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

    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) DBManager.getUserDao().getAll();
    }

    public List<User> getNonMembers(String groupName){
        return DBManager.getGroupDao().getNonMembers(groupName);
    }

    public User getUser(String username) {
        return DBManager.getUserDao().get(username);
    }

    public boolean getUserPermissions(String userid) {
            return DBManager.getUserDao().get(userid).getPermissions();
    }

    public void setPermissions(String userid,boolean i) {
        DBManager.getUserDao().updatePermissions(userid, i);
    }
    public void addRoom(String addName, String roomTypeString,String addLimit, String[] addFeatures) {

        RoomType roomType = RoomType.value(roomTypeString.trim());

        ArrayList<RoomFeature> features = new ArrayList<>();
        for (String addFeature : addFeatures) {
            features.add(RoomFeature.value(addFeature.trim()));
        }

        DBManager.getRoomDao().insert(addName,Integer.parseInt(addLimit), roomType,features);
    }

    public void removeRoom(int roomId){
        DBManager.getRoomDao().delete(roomId);
    }

    public void updateFeatures(int roomId, RoomFeature rF){
        DBManager.getRoomDao().insertFeature(roomId, rF);
    }

    public void removeFeatures(int roomId, RoomFeature rF){
        DBManager.getRoomDao().deleteFeature(roomId, rF);
    }

    public void updateName(int parseInt, String editName) {
        DBManager.getRoomDao().updateName(parseInt, editName);
    }

    public void updateLimit(int parseInt, int editLimit) {
        DBManager.getRoomDao().updateCapacity(parseInt, editLimit);
    }


    public void addGroup(String addName) {
        DBManager.getGroupDao().insert(addName);
    }

    public List<Group> getGroups(){
        return DBManager.getGroupDao().getAll();
    }


    public void removeGroup(String groupName) {
        DBManager.getGroupDao().delete(groupName);
    }


    public void addUserGroup(String groupName, String userId) {
        DBManager.getGroupDao().addMember(groupName, userId);
    }


    public void removeUserGroup(String groupName, String userId) {
        DBManager.getGroupDao().removeMember(groupName, userId);
    }

    public List<User> getUsersInGroup(String groupName){
       return DBManager.getGroupDao().getMembers(groupName);
    }

    public boolean deleteEvent(int idEvent, User user) {
        return DBManager.getEventDao().delete(idEvent);
    }

    public Event getEvent(int idEvent) {
        return DBManager.getEventDao().get(idEvent);
    }

    public boolean cancelSubscription(int idEvent, User user) {
        return DBManager.getEventDao().cancelEvent(idEvent, user.getUsername());
    }

    public boolean acceptEvent(int idEvent, User user) {
        return DBManager.getEventDao().acceptEvent(idEvent,user.getUsername());
    }

    public boolean declineEvent(int idEvent, User user) {
        return DBManager.getEventDao().refuseEvent(idEvent,user.getUsername());
    }
}
