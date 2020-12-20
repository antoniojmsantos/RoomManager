package server.server_communication;

import server.server_communication.threads.AcceptClients;
import server.server_communication.threads.FirstContact;
import server.server_logic.ServerLogic;
import shared_data.helper.KeepAlive;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerCommunication {
    private final ServerLogic serverLogic;
    private ServerSocket socketAttendanceClient;

    public ServerCommunication(ServerLogic serverLogic) throws IOException {
        this.serverLogic = serverLogic;
        this.socketAttendanceClient = new ServerSocket(0);
    }

    public void run(){
        try {
            FirstContact firstContact = new FirstContact(this.serverLogic,socketAttendanceClient.getLocalPort());
            firstContact.start();

            AcceptClients acceptClients = new AcceptClients(socketAttendanceClient,this.serverLogic);
            acceptClients.start();

        } catch (IOException e) {
            KeepAlive.emergencyExit(e,"Falha na inicialização no server multicast do primeiro contacto");
        }

    }
}
