package client.communication;

import client.communication.threads.ThreadListenPendentEvents;
import shared_data.communication.request.RequestAuthentication;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestFirstContact;
import shared_data.communication.request.RequestRegister;
import shared_data.communication.response.ResponseAuthentication;
import shared_data.communication.response.ResponseFirstContact;
import shared_data.communication.response.ResponseRegister;
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

    public boolean CreateEvent(int idRoom, String nameGroup, String name, LocalDateTime initialDate, int durationMin){
//        try{
//            RequestCreateEvent requestCreateEvent = new RequestCreateEvent(idRoom,nameGroup,durationMin,name,initialDate);
//            SendAndReceiveData.sendData(requestCreateEvent,socketTCP);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //TODO: CRIAR UM OBJETO EVENT COM OS DADOS Q RECEBEU DO CONTROLADOR E MANDA LO AO SERVIDOR PARA INSERIR NA DB.

        //TODO: ATENCAO!!! PELO QUE ESTIVE A VER, ASSIM QUE FOR CRIADO UM EVENTO VAI TER DE SER ADICIONADO À
        // TABELA DE SUBSCRICOES UM REGISTO PARA TODOS OS UTILIZADORES E VAI TER DE TER UM CAMPO QUE DIGA SE ACEITOU OU REJEITOU.

        //TODO: RETORNA 0 SE NAO FOI CRIADO COM SUCESSO OU 1 SE FOI CRIADO COM SUCESSO
        return false;
    }

    public ArrayList<Group> getAllGroups(){
        //TODO: RECEBE TODOS OS GRUPOS QUE EXISTEM NA BD, DE MODO A PODER APRESENTA-LOS COMO DICIONARIO NO CRIA EVENTO

        return null;
    }

    public ArrayList<Event> getPendingUserEvents(){
        //TODO: PEDE AO SERVIDOR OS EVENTOS PENDENTES DO "currentUser"
        return null;
    }

    public ArrayList<Event> getUserEvents(){

        //TODO: PEDE AO SERVIDOR OS EVENTOS A QUE O "currentUser" SE ENCONTRA INSCRITO
        return null;
    }

    public ArrayList<Room> getRooms(){ //JA N EXISTE FUNCAO DE getRoomsWithFilter() PQ VAI SER A PROPRIA GUI A FILTRAR A LISTA. VAI TER DE TER UM METODO QUE VERIFICA A DESPONIBILIDADE DE CADA SALA ANTES DE A APRESENTAR NA LISTA FILTRADA
        //TODO: FALTA VER DE QUE MANEIRA VAMOS LHE ENVIAR AS CARACTERISTICAS. ArrayList? String com espaços?
        // Isto é importante porque de que forma vai ser gerida essa string na interface?
        // visto que o user pode fazer check e uncheck às caracteristicas.

        //TODO: NA QUERY SQL É PRECISO TER ATENÇÃO PARA SÓ DEVOLVER AS SALAS QUE NÃO ESTEJAM JA OCUPADAS NO DETERMINADO HORARIO


        //TODO: PEDE AO SERVIDOR AS SALAS COM AS DETERMINADAS CARACTERISTICAS QUE RECEBE DO CONTROLADOR.
        return null;
    }

    public Event getEvent(int idEvent){

        //TODO: PEDE AO SERVIDOR UM OBJETO EVENTO A PARTIR DO SEU ID E RETORNA-O.
        return null;
    }

    public boolean deleteEvent(int idEvent){

        //TODO: PEDE AO SERVIDOR UM OBJETO EVENTO A PARTIR DO SEU ID E RETORNA-O.
        return true;
    }

    public boolean deleteEventSubscription(int idEvent){

        //TODO: MANDA AO SERVIDOR O ID DO EVENTO E O ID DO USER E CANCELA A SUA SUBSCRICAO NAQUELE EVENTO
        return true;
    }

    public boolean acceptUserSubscription(int idEvent){
        //TODO: PEDE AO SERVIDOR PARA ACEITAR A SUBSCRICAO DO USER currentUser NO EVENTO idEvent

        return true;
    }

    public boolean declineUserSubscription(int idEvent){
        //TODO: PEDE AO SERVIDOR PARA REJEITAR A SUBSCRICAO DO USER currentUser NO EVENTO idEvent

        return true;
    }


}
