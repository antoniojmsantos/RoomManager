package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.*;
import shared_data.helper.ClientInfo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    public void addRoom(String addName, String roomTypeString,String addLimit, String addFeatures) {
        ArrayList<RoomFeature> features = new ArrayList<>();
        String rest;String[] data;
        data = addFeatures.split(" ");
        rest = data[1];
        while(!rest.isEmpty()){
            if(data[0].equals("ar_condicionado")){
                features.add(RoomFeature.AR_CONDICIONADO);
            }else if(data[0].equals("computadores_mac")){
                features.add(RoomFeature.COMPUTADORES_MAC);
            }else if(data[0].equals("computadores_windows")){
                features.add(RoomFeature.COMPUTADORES_WINDOWS);
            }else if(data[0].equals("projetor")){
                features.add(RoomFeature.PROJETOR);
            }else if(data[0].equals("quadro_interativo")){
                features.add(RoomFeature.QUADRO_INTERATIVO);
            }
            data = addFeatures.split(" ");
            rest = data[1];
        }

        RoomType roomType = null;
        if(roomTypeString.equals("laboratorio")){
            roomType = RoomType.LABORATORIO;
        }else if(roomTypeString.equals("auditorio")){
            roomType = RoomType.AUDITORIO;
        }

        DBManager.getRoomDao().insert(addName,Integer.parseInt(addLimit), roomType,features);
    }

    public void updateName(int parseInt, String editName) {
        DBManager.getRoomDao().updateName(parseInt, editName);
    }

    public void updateLimit(int parseInt, int editLimit) {
        DBManager.getRoomDao().updateCapacity(parseInt, editLimit);
    }

    public void updateDescription(int parseInt, String editDescription) {
        //FALTA FAZER NA BASE DE DADOS FUNÇÃO PARA ALTERAR DESCRIÇÃO DE SALAS
        //DBManager.getRoomDao().updateDescription(parseInt, editDescription);

    }

    public void addGroup(String addName) {
        DBManager.getGroupDao().insert(addName);
    }

    public List<Group> getGroups(){
        return DBManager.getGroupDao().getAll();
    }
}
