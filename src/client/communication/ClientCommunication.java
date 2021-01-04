package client.communication;

import client.communication.threads.ThreadListenPendentEvents;
import shared_data.communication.request.*;
import shared_data.communication.response.*;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;
import shared_data.helper.MyMutex;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.*;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/* Client Communication
* ---
* classe responsável pela comunicação com o servidor
* */
public class ClientCommunication {

    private final int MULTICAST_PORT = 54321;
    private final String MULTICAST_GROUP = "239.3.2.1";

    private ServerSocket clientSocket;

    private Socket socketTCP;
    private Socket socketCallBack;

    private User loggedUser;

    private ArrayList<Event> pendingEvents;
    private ArrayList<Event> registeredEvents;

    private final MyMutex mutex;

    public ClientCommunication(MyMutex mutex) {
        this.mutex = mutex;
    }

    public User getLoggedUser(){ return loggedUser; }

    // entry point
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

    // primeiro contacto com o servidor
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

        // create server socket
        socketTCP = new Socket(responseFirstContact.getIpServerTCP(),responseFirstContact.getPortTCP());
    }

    /**
     * Requisita a autenticação do utilizador ao servidor
     *
     * @param username  username do utilizador
     * @param password  password do utilizador
     * @return true se correu bem, false se correu mal
     */
    public boolean Authenticate(String username, String password) {
        try {
           SendAndReceiveData.sendData(new RequestAuthentication(InetAddress.getLocalHost().getHostAddress(), socketTCP.getLocalPort(),username,password,clientSocket.getLocalPort()), socketTCP);
           ResponseAuthentication response = (ResponseAuthentication) SendAndReceiveData.receiveData(socketTCP);
           if(response.isAuthenticated()){
               socketCallBack = clientSocket.accept();
               this.loggedUser = response.getUser();

               ThreadListenPendentEvents threadListenPendentEvents = new ThreadListenPendentEvents(socketCallBack,this.mutex);
               threadListenPendentEvents.start();
           }
           return response.isAuthenticated();
        } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }
        return false;
    }

    /**
     * Requisita o registo de um novo utilizador ao servidor
     *
     * @param name      nome do utilizador
     * @param username  username do utilizador
     * @param password  password do utilizador
     * @return
     *      -1  ->
     *      0   ->
     *      > 0 ->
     */
    public int Register(String name, String username, String password) {
        try {
            RequestRegister requestRegister = new RequestRegister(InetAddress.getLocalHost().getHostAddress(), socketTCP.getPort(),username,name,password);
            SendAndReceiveData.sendData(requestRegister,socketTCP);

            ResponseRegister responseRegister = (ResponseRegister) SendAndReceiveData.receiveData(socketTCP);

            return responseRegister.getResult();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Criar um evento
     *
     * @param name              nome do evento
     * @param idRoom            id da sala do evento
     * @param nameGroup         name do grupo inscrito no evento
     * @param usernameCreator   username do utilizador que criou o evento
     * @param startDate         data do início do evento
     * @param duration          duração do evento
     * @return  see bellow
     * @see database.dao.EventDao
     */
    public int createEvent(String name, int idRoom, String nameGroup, String usernameCreator, LocalDateTime startDate, int duration){
        int errorCode = 0;
        try{
            RequestCreateEvent requestCreateEvent = new RequestCreateEvent(name, idRoom, nameGroup, usernameCreator, startDate, duration);
            SendAndReceiveData.sendData(requestCreateEvent,socketTCP);

            ResponseCreateEvent responseCreateEvent = (ResponseCreateEvent) SendAndReceiveData.receiveData(socketTCP);
            errorCode =  responseCreateEvent.getErrorCode();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return errorCode;
    }

    /**
     * Pede ao servidor para apagar um evento
     *
     * @param idEvent   id do evento a apagar
     * @return true se a operação sucedeu, false caso contrário
     */
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

    /**
     * Pede ao servidor a lista dos seus eventos pendentes
     *
     * @return array list dos eventos pendentes do utilizador
     */
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

    /**
     * Pede ao servidor a lista dos seus eventos aceites
     *
     * @return array list dos eventos aceites do utilizador
     */
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

    /**
     * Pede ao servidor a lista dos seus eventos criados
     *
     * @return array list dos eventos criados pelo utilizador
     */
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

    /**
     * Pede ao servidor a lista dos grupos existentes
     *
     * @return array list dos grupos existentes
     */
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

    /**
     * Pede ao servidor a lista das salas existentes
     *
     * @return array list das salas existentes
     */
    public ArrayList<Room> getAllRooms(){
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
}
