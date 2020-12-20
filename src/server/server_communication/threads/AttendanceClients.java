package server.server_communication.threads;

import server.server_logic.ServerLogic;
import shared_data.communication.Request;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.Socket;

public class AttendanceClients extends Thread{

    private Socket socketClient;
    private ServerLogic serverLogic;
    public AttendanceClients(Socket socketNewClient, ServerLogic serverLogic){
        this.socketClient = socketNewClient;
        this.serverLogic = serverLogic;
    }

    @Override
    public void run() {
        while (KeepAlive.getKeepAlive()){
            try {
                Request request = (Request)SendAndReceiveData.receiveData(socketClient);
                verifyRequest(request);
            } catch (IOException e) {
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client went off");
    }

    public void verifyRequest(Request request){

    }
}
