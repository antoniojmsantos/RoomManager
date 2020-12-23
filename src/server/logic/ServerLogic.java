package server.logic;

import database.DBManager;

public final class ServerLogic {

    public ServerLogic(){
        DBManager.init(
                "jdbc:mysql://localhost:3306/db_room_manager?serverTimezone=Europe/Lisbon",
                "admin",
                "pass1234"
        );
    }

    public void getAuthenticate(String username, String password) {
        if (DBManager.getUserDao().authenticate(username, password)) {
            // success
        } else {
            // failure
        }
    }
}
