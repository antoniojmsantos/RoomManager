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

public class AttendanceClients extends Thread{


    private final Socket socketClient; //socket responsável por enviar requests e receber responses
    private final ServerLogic serverLogic;
    private Socket socketCallBack; //socket call back para quando necessário uma notificação do servidor
    private User user;
    public AttendanceClients(Socket socketNewClient, ServerLogic serverLogic){
        this.socketClient = socketNewClient;
        this.serverLogic = serverLogic;
    }

    /**
     * Função encarregue de receber requests
     * Chama a função verifyRequest para verificar tipo
     */
    @Override
    public void run() {
        while (KeepAlive.getKeepAlive()){
            try {
                Request request = (Request)SendAndReceiveData.receiveData(socketClient);
                verifyRequest(request);
            } catch (IOException e) {
                //e.printStackTrace();
                serverLogic.removeClientInfo(this.user);
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client went off");
    }

    /**
     * Verifica o tipo de request e trata-o consoante o tipo
     * @param request request enviado pelo cliente
     *                Todos os requests derivam de Request para facilitar depois a disntinção com o instanceOf
     *                e na altura do receive receber um objeto mais genérico
     * RequestAuthentication: corresponde ao pedido do cliente de Login na aplicação
     *                        Vai á logica verificar a Base de Dados e retorna um user
     *                        Após isso é enviado uam resposta (ResponseAuthentication) e é feito uma conexão de
     *                        um novo socket (socketCallBack) usado nas notificações
     * RequestRegister: corresponde ao pedido do cliente de Registo na aplicação
     *                  Vai á logica inserir o User na BD e envia uma resposta(ResponseRegister)
     * RequestCreateEvent: corresponde ao pedido do cliente de criar um novo envento na aplicação,
     *                     após enviar uma resposta (ResponseCreateEvent) ao utilizador,
     *                     notifica através da Thread NotifyPendentEvents
     *                     os clientes que estão no grupo do evento criado
     * RequestGetRooms: corresponde ao pedido do cliente de obter as salas do sistema
     *                  Vai á logica buscar uma lista de salas e envia ao cliente numa resposta(ResponseGetRooms)
     * RequestGetGroups: corresponde ao pedido do cliente de obter os groupos do sistema
     *                   Vai á logica buscar uma lista de grupos e envia ao cliente numa resposta(ResponseGetGroups)
     * RequestUserEvents: corresponde ao pedido do cliente de obter os eventos a que está inscrito
     *                    Vai á logica buscar uma lista de eventos inscritos e envia ao cliente numa resposta(ResponseUserEvents)
     * RequestPendingEvents: corresponde ao pedido do cliente de obter os eventos por aceitar/recusar
     *                      Vai á logica buscar uma lista de eventos por responder
     *                      e envia ao cliente numa resposta(ResponsePendingEvents)
     * RequestCreatedEvents: corresponde ao pedido do cliente de obter os eventos por ele criado
     *                      Vai á logica buscar uma lista de eventos criados
     *                      e envia ao cliente numa resposta(ResponseCreatedEvents)
     * RequestDeleteEvent: corresponde ao pedido do cliente de apagar um evento por ele criado,
     *                     após enviar uma resposta(ResponseDeleteEvent) ao utilizador, notifica através da Thread NotifyPendentEvents
     *                     os clientes que estão no grupo do evento apagado
     * RequestCancelSubscription: corresponde ao pedido do cliente de cancelar uma inscrição num evento
     *                          Vai á logica cancelar a inscrição do user num evento
     *                          e envia ao cliente uma resposta(ResponseCancelSubscription)
     * RequestAcceptEvent: corresponde ao pedido do cliente de se increver num evento
     *                Vai á logica aceitar a inscrição do user num evento
     *                e envia ao cliente uma resposta(ResponseAcceptEvent)
     * RequestDeclineEvent: corresponde ao pedido do cliente de recusar um evento
     *                Vai á logica rejeitar a inscrição do user num evento
     *                e envia ao cliente uma resposta(ResponseDeclineEvent)
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
                this.user = user;
                serverLogic.addToClientInfo(socketCallBack,user);
            }else{
                responseAuthentication = new ResponseAuthentication(InetAddress.getLocalHost().getHostAddress(), socketClient.getPort(), false, null,null,null);
                SendAndReceiveData.sendData(responseAuthentication,socketClient);
            }
        }
        else if(request instanceof RequestRegister) {
            RequestRegister register = (RequestRegister) request;
            int resultCode = serverLogic.registerUsers(register);

            ResponseRegister responseRegister = new ResponseRegister(InetAddress.getLocalHost().getHostAddress(),socketClient.getPort(),resultCode);
            SendAndReceiveData.sendData(responseRegister,socketClient);
        }
        else if(request instanceof RequestCreateEvent) {
            RequestCreateEvent requestCreateEvent = (RequestCreateEvent) request;
            ResponseCreateEvent responseCreateEvent;

            int eventId = serverLogic.createEvent(requestCreateEvent);
            switch (eventId) {
                case -1:    // dates incompatible
                case 0: // database error
                    responseCreateEvent = new ResponseCreateEvent(null, eventId);
                    SendAndReceiveData.sendData(responseCreateEvent,socketClient);
                    break;
                default:
                    Event event = serverLogic.getEvent(eventId);
                    responseCreateEvent = new ResponseCreateEvent(event,eventId);
                    SendAndReceiveData.sendData(responseCreateEvent,socketClient);

                    NotifyPendentEvents notifyPendentEvents = new NotifyPendentEvents(event,serverLogic,"create");
                    notifyPendentEvents.start();
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
        else if(request instanceof RequestCreatedEvents){
            RequestCreatedEvents requestCreatedEvents = (RequestCreatedEvents)request;
            ResponseCreatedEvents responseCreatedEvents  = new ResponseCreatedEvents(serverLogic.getCreatedEvents(requestCreatedEvents.getUser()));
            SendAndReceiveData.sendData(responseCreatedEvents,socketClient);
        }
        else if(request instanceof RequestDeleteEvent){
            RequestDeleteEvent requestDeleteEvent = (RequestDeleteEvent)request;
            Event event = serverLogic.getEvent(requestDeleteEvent.getIdEvent());

            ResponseDeleteEvent responseDeleteEvent = new ResponseDeleteEvent(serverLogic.deleteEvent(requestDeleteEvent.getIdEvent(),requestDeleteEvent.getUser()));
            SendAndReceiveData.sendData(responseDeleteEvent,socketClient);

            NotifyPendentEvents notifyPendentEvents = new NotifyPendentEvents(event,serverLogic,"delete");
            notifyPendentEvents.start();
        }
        else if (request instanceof RequestCancelSubscription){
            RequestCancelSubscription requestCancelSubscription = (RequestCancelSubscription)request;
            boolean result = serverLogic.cancelSubscription(requestCancelSubscription.getIdEvent(),requestCancelSubscription.getUser());
            ResponseCancelSubscription responseCancelSubscription = new ResponseCancelSubscription(result);
            SendAndReceiveData.sendData(responseCancelSubscription,socketClient);
        }
        else if(request instanceof RequestAcceptEvent){
            RequestAcceptEvent requestAcceptEvent = (RequestAcceptEvent)request;
            boolean result = serverLogic.acceptEvent(requestAcceptEvent.getIdEvent(),requestAcceptEvent.getUser());
            ResponseAcceptEvent responseAcceptEvent = new ResponseAcceptEvent(result);
            SendAndReceiveData.sendData(responseAcceptEvent,socketClient);
        }
        else if (request instanceof RequestDeclineEvent){
            RequestDeclineEvent requestDeclineEvent = (RequestDeclineEvent)request;
            boolean result = serverLogic.declineEvent(requestDeclineEvent.getIdEvent(),requestDeclineEvent.getUser());
            ResponseDeclineEvent responseDeclineEvent = new ResponseDeclineEvent(result);
            SendAndReceiveData.sendData(responseDeclineEvent,socketClient);
        }
    }
}
