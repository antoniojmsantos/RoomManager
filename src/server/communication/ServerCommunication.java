package server.communication;

import server.communication.threads.AcceptClients;
import server.communication.threads.FirstContact;
import server.logic.ServerLogic;
import shared_data.helper.KeepAlive;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerCommunication {

    private final ServerLogic serverLogic;
    private ServerSocket socketAttendanceClient;

    // constructor
    public ServerCommunication(ServerLogic serverLogic) throws IOException {
        this.serverLogic = serverLogic;
        this.socketAttendanceClient = new ServerSocket(0);
    }

    /**
     * função responsável por lançar as threads necessárias à comunicação
     * thread firstContact serve para os clientes obterem o ip e porto do tcp
     * thread AcceptClients é a thread que vai aceitar clientes e posteriormente antendelos
     */
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
