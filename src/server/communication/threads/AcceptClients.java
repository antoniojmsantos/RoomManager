package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.helper.KeepAlive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClients extends Thread {

    private final ServerSocket serverSocket;
    private final ServerLogic serverLogic;
    public AcceptClients(ServerSocket socketAttendanceClient, ServerLogic serverLogic){
        this.serverSocket = socketAttendanceClient;
        this.serverLogic = serverLogic;
    }

    /**
     * Thread responsável por atribuir uma thread que irá atender cada cliente
     */
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
