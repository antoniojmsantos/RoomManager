package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.communication.Request;
import shared_data.communication.request.RequestAuthentication;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.communication.response.ResponseAuthentication;
import shared_data.communication.response.ResponseCreateEvent;
import shared_data.communication.response.ResponseRegister;
import shared_data.entities.Event;
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
    private Socket socketCallBack;

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

    /**
     *
     * @param request
     * @throws IOException
     */
    public void verifyRequest(Request request) throws IOException {

        if(request instanceof RequestAuthentication) {
            RequestAuthentication authentication = (RequestAuthentication) request;
            User user = serverLogic.getAuthenticate(authentication.getUsername(), authentication.getPassword());
            ResponseAuthentication responseAuthentication;
            if(user != null){
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), true, user);
                SendAndReceiveData.sendData(responseAuthentication,socketClient);
                socketCallBack = new Socket(authentication.getIp(),authentication.getSocketTCPclient());
                serverLogic.addToClientInfo(socketCallBack,user);
            }else{
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), false, null);
                SendAndReceiveData.sendData(responseAuthentication,socketClient);
            }
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
        else if(request instanceof RequestCreateEvent) {
            RequestCreateEvent requestCreateEvent = (RequestCreateEvent) request;
            Event event = serverLogic.createEvent(requestCreateEvent);
            ResponseCreateEvent responseCreateEvent;
            if(event != null){
                //positivo
                responseCreateEvent = new ResponseCreateEvent(event);
                SendAndReceiveData.sendData(responseCreateEvent,socketClient);
                NotifyPendentEvents notifyPendentEvents = new NotifyPendentEvents(event,serverLogic);
                notifyPendentEvents.start();
            }else{
                responseCreateEvent = new ResponseCreateEvent(null);
                SendAndReceiveData.sendData(responseCreateEvent,socketClient);
                //negativo
            }
        }
    }
}
