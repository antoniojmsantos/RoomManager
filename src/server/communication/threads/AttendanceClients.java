package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.communication.Request;
import shared_data.communication.request.RequestAuthentication;
import shared_data.communication.request.RequestRegister;
import shared_data.communication.response.ResponseAuthentication;
import shared_data.communication.response.ResponseRegister;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client went off");
    }

    public void verifyRequest(Request request) throws IOException {
        if(request instanceof RequestAuthentication) {
            RequestAuthentication authentication = (RequestAuthentication) request;
            User user = serverLogic.getAuthenticate(authentication.getUsername(), authentication.getPassword());
            ResponseAuthentication responseAuthentication;
            if(user != null){
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), true, user);
            }else{
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), false, null);
            }
            SendAndReceiveData.sendData(responseAuthentication,socketClient);
        }
        else if(request instanceof RequestRegister) {
            RequestRegister register = (RequestRegister) request;
            System.out.println(register.getUsername() + " "+ register.getPermissionLevel());
            if (serverLogic.registerUsers(register)){
                ResponseRegister responseRegister = new ResponseRegister(InetAddress.getLocalHost().getHostAddress(),socketClient.getPort(),true);
                SendAndReceiveData.sendData(responseRegister,socketClient);
            }else {
                ResponseRegister responseRegister = new ResponseRegister(InetAddress.getLocalHost().getHostAddress(),socketClient.getPort(),false);
                SendAndReceiveData.sendData(responseRegister,socketClient);
            }
        }
    }
}
