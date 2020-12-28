package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.Event;
import shared_data.entities.User;
import shared_data.helper.ClientInfo;

import java.net.Socket;
import java.util.ArrayList;

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
                requestCreateEvent.getInitialDate(),
                requestCreateEvent.getDuration());

        return DBManager.getEventDao().get(idEvent);
    }
}
