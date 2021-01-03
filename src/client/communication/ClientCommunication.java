package client.communication;

import client.communication.threads.ThreadListenPendentEvents;
import shared_data.communication.request.*;
import shared_data.communication.response.*;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.*;
import java.rmi.UnexpectedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ClientCommunication {

    private final int MULTICAST_PORT = 54321;
    private final String MULTICAST_GROUP = "239.3.2.1";

    private ServerSocket clientSocket;

    private Socket socketTCP;
    private Socket socketCallBack;

    private User loggedUser;

    private ArrayList<Event> pendingEvents;
    private ArrayList<Event> registeredEvents;

    public User getLoggedUser(){ return loggedUser; }

    public void run() {
        try {
            clientSocket = new ServerSocket(0);
            firstContact();
        } catch (SocketException | UnexpectedException e ) {
            KeepAlive.emergencyExit(e, "Falha a criar comunicação");
        } catch (IOException e) {
            KeepAlive.emergencyExit(e, "Falha ao enviar o primeiro contato");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void firstContact() throws IOException, ClassNotFoundException {
        DatagramSocket socketFirstContact = new DatagramSocket();

        // send first contact
        SendAndReceiveData.sendDataUDP(
                new RequestFirstContact(InetAddress.getLocalHost().getHostAddress(), socketFirstContact.getLocalPort()),
                socketFirstContact,
                InetAddress.getByName(MULTICAST_GROUP),
                MULTICAST_PORT
        );

        // get response
        ResponseFirstContact responseFirstContact = (ResponseFirstContact)SendAndReceiveData.receiveDataUDP(socketFirstContact);
        System.out.println(responseFirstContact);

        // create server socket
        socketTCP = new Socket(responseFirstContact.getIpServerTCP(),responseFirstContact.getPortTCP());
    }

    public boolean Authenticate(String username, String password) {
        try {
           SendAndReceiveData.sendData(new RequestAuthentication(InetAddress.getLocalHost().getHostAddress(), socketTCP.getLocalPort(),username,password,clientSocket.getLocalPort()), socketTCP);
           ResponseAuthentication response = (ResponseAuthentication) SendAndReceiveData.receiveData(socketTCP);
           if(response.isAuthenticated()){
               socketCallBack = clientSocket.accept();
               this.loggedUser = response.getUser();
               this.pendingEvents = response.getPendingEvents();
               this.registeredEvents = response.getRegisteredEvents();
               ThreadListenPendentEvents threadListenPendentEvents = new ThreadListenPendentEvents(socketCallBack);
               threadListenPendentEvents.start();
           }
           return response.isAuthenticated();
        } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }
        System.out.println("se chegar aqui ocorreu um erro no try");
        return false;
    }

    public boolean Register(String name, String username, String password){
        String[] data = username.split("@");
        String typeOfUser = data[0];
        Boolean permission = true;

        try{
            if(typeOfUser.length() == 9){
                if(typeOfUser.charAt(0) == 'a'){
                    for(int i = 1; i < 9;i++){
                        try{
                            Integer.parseInt(String.valueOf(typeOfUser.charAt(i)));
                            permission = false;
                        } catch (NumberFormatException e) {
                            permission = true;
                            break;
                        }
                    }
                    System.out.println(username + " " + name +" " + password+" " + permission);
                    RequestRegister requestRegister = new RequestRegister(InetAddress.getLocalHost().getHostAddress(), socketTCP.getPort(),username,name,password,permission);
                    SendAndReceiveData.sendData(requestRegister,socketTCP);
                }
            }else {
                RequestRegister requestRegister = new RequestRegister(InetAddress.getLocalHost().getHostAddress(), socketTCP.getPort(),username,name,password,true);
                SendAndReceiveData.sendData(requestRegister,socketTCP);
            }

            ResponseRegister responseRegister = (ResponseRegister) SendAndReceiveData.receiveData(socketTCP);

            return responseRegister.getResult();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CreateEvent(int idRoom, String nameGroup, String usernameCreator, String name, LocalDateTime initialDate, int duration){
        try{
            RequestCreateEvent requestCreateEvent = new RequestCreateEvent(idRoom,nameGroup, usernameCreator, duration,name,initialDate);
            SendAndReceiveData.sendData(requestCreateEvent,socketTCP);
            ResponseCreateEvent responseCreateEvent = (ResponseCreateEvent) SendAndReceiveData.receiveData(socketTCP);
            if(responseCreateEvent.getEvent() != null){
                return true;
            }
            else{
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Event> getPendingEvents(){
        try{
            RequestPendingEvents requestPendingEvents = new RequestPendingEvents(this.loggedUser);
            SendAndReceiveData.sendData(requestPendingEvents,socketTCP);
            ResponsePendingEvents responsePendingEvents = (ResponsePendingEvents)SendAndReceiveData.receiveData(socketTCP);
            return responsePendingEvents.getUserEvents();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Event> getUserEvents(){
        try{
            RequestUserEvents requestUserEvents = new RequestUserEvents(this.loggedUser);
            SendAndReceiveData.sendData(requestUserEvents,socketTCP);
            ResponseUserEvents responseUserEvents = (ResponseUserEvents)SendAndReceiveData.receiveData(socketTCP);
            return responseUserEvents.getUserEvents();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Event> getEventsCreated(){
        try{
            RequestCreatedEvents requestCreatedEvents = new RequestCreatedEvents(this.loggedUser);
            SendAndReceiveData.sendData(requestCreatedEvents,socketTCP);

            ResponseCreatedEvents responseCreatedEvents = (ResponseCreatedEvents)SendAndReceiveData.receiveData(socketTCP);
            return responseCreatedEvents.getCreatedEvents();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Group> getAllGroups(){
        try{
            RequestGetGroups requestGetGroups = new RequestGetGroups();
            SendAndReceiveData.sendData(requestGetGroups,socketTCP);
            ResponseGetGroup  responseGetGroup = (ResponseGetGroup)SendAndReceiveData.receiveData(socketTCP);
            return responseGetGroup.getGroups();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Room> getRooms(){
        try{
            RequestGetRooms requestGetRooms = new RequestGetRooms();
            SendAndReceiveData.sendData(requestGetRooms,socketTCP);
            ResponseGetRooms responseGetRooms = (ResponseGetRooms)SendAndReceiveData.receiveData(socketTCP);
            return responseGetRooms.getRooms();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Event getEvent(int idEvent){

        //TODO: PEDE AO SERVIDOR UM OBJETO EVENTO A PARTIR DO SEU ID E RETORNA-O.
        return null;
    }

    public boolean deleteEvent(int idEvent){
        try{
            RequestDeleteEvent requestDeleteEvent = new RequestDeleteEvent(idEvent,this.loggedUser);
            SendAndReceiveData.sendData(requestDeleteEvent,socketTCP);
            ResponseDeleteEvent responseDeleteEvent = (ResponseDeleteEvent)SendAndReceiveData.receiveData(socketTCP);
            return responseDeleteEvent.isResult();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelEventSubscription(int idEvent){
        try{
            RequestCancelSubscription requestCancelSubscription = new RequestCancelSubscription(idEvent,this.loggedUser);
            SendAndReceiveData.sendData(requestCancelSubscription,socketTCP);
            ResponseCancelSubscription responseCancelSubscription = (ResponseCancelSubscription)SendAndReceiveData.receiveData(socketTCP);
            return responseCancelSubscription.isResult();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean acceptEventSubscription(int idEvent){
        try{
            RequestAcceptEvent requestAcceptEvent = new RequestAcceptEvent(idEvent,this.loggedUser);
            SendAndReceiveData.sendData(requestAcceptEvent,socketTCP);
            ResponseAcceptEvent responseAcceptEvent = (ResponseAcceptEvent)SendAndReceiveData.receiveData(socketTCP);
            return responseAcceptEvent.isResult();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean declineEventSubscription(int idEvent){
        try{
            RequestDeclineEvent requestDeclineEvent = new RequestDeclineEvent(idEvent,this.loggedUser);
            SendAndReceiveData.sendData(requestDeclineEvent,socketTCP);
            ResponseDeclineEvent responseDeclineEvent = (ResponseDeclineEvent)SendAndReceiveData.receiveData(socketTCP);
            return responseDeclineEvent.isResult();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
