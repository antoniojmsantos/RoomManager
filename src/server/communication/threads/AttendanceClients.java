package server.communication.threads;

import server.logic.ServerLogic;
import shared_data.communication.Request;
import shared_data.communication.request.*;
import shared_data.communication.response.*;
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
                responseAuthentication = new ResponseAuthentication(
                        InetAddress.getLocalHost().getHostAddress()
                        , socketClient.getPort(),
                        true,
                        user,
                        serverLogic.getUserPendingEvents(user),
                        serverLogic.getUserEvents(user));

                SendAndReceiveData.sendData(responseAuthentication,socketClient);
                socketCallBack = new Socket(authentication.getIp(),authentication.getSocketTCPclient());
                serverLogic.addToClientInfo(socketCallBack,user);
            }else{
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), false, null,null,null);
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
        else if(request instanceof RequestGetRooms){
            ResponseGetRooms responseGetRooms = new ResponseGetRooms(serverLogic.getAllRooms());
            SendAndReceiveData.sendData(responseGetRooms,socketClient);
        }
        else if(request instanceof RequestGetGroups){
            ResponseGetGroup responseGetGroup = new ResponseGetGroup(serverLogic.getAllGroups());
            SendAndReceiveData.sendData(responseGetGroup,socketClient);
        }
        else if(request instanceof  RequestUserEvents){
            RequestUserEvents requestUserEvents = (RequestUserEvents)request;
            ResponseUserEvents responseUserEvents = new ResponseUserEvents(serverLogic.getUserEvents(requestUserEvents.getUser()));
            SendAndReceiveData.sendData(responseUserEvents,socketClient);
        }
        else if(request instanceof RequestPendingEvents){
            RequestPendingEvents requestPendingEvents = (RequestPendingEvents)request;
            ResponsePendingEvents responsePendingEvents = new ResponsePendingEvents(serverLogic.getUserPendingEvents(requestPendingEvents.getUser()));
            SendAndReceiveData.sendData(responsePendingEvents,socketClient);
        }
    }
}
