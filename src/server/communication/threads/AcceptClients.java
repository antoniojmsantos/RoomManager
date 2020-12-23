package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.helper.KeepAlive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClients extends Thread {

    private ServerSocket serverSocket;
    private ServerLogic serverLogic;
    public AcceptClients(ServerSocket socketAttendanceClient, ServerLogic serverLogic){
        this.serverSocket = socketAttendanceClient;
        this.serverLogic = serverLogic;
    }

    @Override
    public void run() {
        while (KeepAlive.getKeepAlive()){
            try {
                Socket socketNewClient = serverSocket.accept();
                AttendanceClients attendanceClients = new AttendanceClients(socketNewClient,this.serverLogic);
                attendanceClients.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
