package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.User;

public final class ServerLogic {

    public ServerLogic(){
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
        User user = new User(register.getUsername(), register.getUser(),register.getPassword(),register.getPermissionLevel());
        System.out.println(DBManager.getUserDao());
        if(DBManager.getUserDao().insert(user)){
           return true;
        }else {
            return false;
        }
    }
}
