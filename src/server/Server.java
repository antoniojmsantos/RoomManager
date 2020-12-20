package server;

import server.server_communication.ServerCommunication;
import server.server_interface.ServerInterface;
import server.server_logic.ServerLogic;
import shared_data.helper.KeepAlive;

import java.io.IOException;

public class Server {

    public static void main(String[] args){
        ServerLogic serverLogic = new ServerLogic();
        try{
            ServerCommunication serverCommunication = new ServerCommunication(serverLogic);
            serverCommunication.run();

            ServerInterface serverInterface = new ServerInterface(serverLogic);
            serverInterface.start();
        } catch (IOException e) {
            KeepAlive.emergencyExit(e,"Falha na criação do server socket");
        }

    }
}
