package client.communication;

import shared_data.communication.request.RequestAuthentication;
import shared_data.communication.request.RequestFirstContact;
import shared_data.communication.response.ResponseAuthentication;
import shared_data.communication.response.ResponseFirstContact;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

public class ClientCommunication {

    private final int MULTICAST_PORT = 54321;
    private final String MULTICAST_GROUP = "239.3.2.1";

    private Socket socketTCP;

    private User loggedUser;

    public User getLoggedUser(){ return loggedUser; }

    public void run() {
        try {
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
//        try {
//            SendAndReceiveData.sendData(new RequestAuthentication(InetAddress.getLocalHost().getHostAddress(), socketTCP.getLocalPort(),username,password), socketTCP);
//
//            ResponseAuthentication response = (ResponseAuthentication) SendAndReceiveData.receiveData(socketTCP);
//
//            System.out.println("Authentication: "+response.isAuthenticated());
//
//            return response.isAuthenticated();
//        } catch (IOException | ClassNotFoundException /*| ClassNotFoundException*/ e) {
//            e.printStackTrace();
//        }
//        //se chegar aqui ocorreu um erro no try
//        return false;
//                    //TODO: TEM DE RECEBER UM OBJETO USER E ARMAZENALO NO MEMBRO PRIVADO DESTA CLASSE.
//
//                    //return response.isAuthenticated();
//                } catch (IOException /*| ClassNotFoundException*/ e) {
//                    e.printStackTrace();
//                }
//            }
        return true;
    }

    public boolean Register(Boolean permissionLvl, String name, String username, String password){

        // permissionLvl(0) = aluno, etc. permissionLvl(1) = docente, etc.

        //TODO: CRIAR UM OBJETO USER COM OS DADOS QUE RECEBEU DO CONTROLADOR E MANDA LO AO SERVIDOR PARA INSERIR NA DB.

        //TODO: RETORNA 0 SE NAO FOI CRIADO COM SUCESSO OU 1 SE FOI CRIADO COM SUCESSO
        return true;
    }

    public boolean CreateEvent(Boolean permissionLvl, String name, String username, String password){

        //TODO: CRIAR UM OBJETO EVENT COM OS DADOS Q RECEBEU DO CONTROLADOR E MANDA LO AO SERVIDOR PARA INSERIR NA DB.

        //TODO: ATENCAO!!! PELO QUE ESTIVE A VER, ASSIM QUE FOR CRIADO UM EVENTO VAI TER DE SER ADICIONADO À
        // TABELA DE SUBSCRICOES UM REGISTO PARA TODOS OS UTILIZADORES E VAI TER DE TER UM CAMPO QUE DIGA SE ACEITOU OU REJEITOU.

        //TODO: RETORNA 0 SE NAO FOI CRIADO COM SUCESSO OU 1 SE FOI CRIADO COM SUCESSO
        return true;
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

    public ArrayList<Room> getRoomsWithFilter(String name){
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
