package server.logic;

import database.DBManager;
import shared_data.communication.request.RequestCreateEvent;
import shared_data.communication.request.RequestRegister;
import shared_data.entities.*;
import shared_data.helper.ClientInfo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class ServerLogic {

    private ArrayList<ClientInfo> clientInfo; // contem a informação dos clientes logados

    public ServerLogic(){
        clientInfo = new ArrayList<>();
        DBManager.init(
                "jdbc:mysql://localhost:3306/db_room_manager?serverTimezone=Europe/Lisbon",
                "admin",
                "pass1234"
        );
    }

    /**
     * Vai verificar a BD os dados do User
     * @param username username(mail) do cliente
     * @param password password que ele inseriu
     * @return de um User ou null caso os dados estejam incorretos
     */
    public User getAuthenticate(String username, String password) {
        if (DBManager.getUserDao().authenticate(username, password)) {
            return DBManager.getUserDao().get(username);
            // success
        } else {
            return null;
            // failure
        }
    }

    /**
     * Regista na DB um registo novo de um cleinte
     * @param register Request que contem os dados do registo
     * @return um numero que representa o erro ou sucesso
     */
    public int registerUsers(RequestRegister register){
        return DBManager.getUserDao().insert(register.getUsername(), register.getUser(), register.getPassword());
    }

    /**
     * Adiciona O CLiente logado na info
     * @param socketCallBack recebe o socket para as notificações
     * @param user recebe o User logado
     */
    public void addToClientInfo(Socket socketCallBack, User user) {
        synchronized (clientInfo){
            clientInfo.add(new ClientInfo(socketCallBack,user));
        }
    }

    /**
     * Cria evento na base de dados
     * @param requestCreateEvent request que contem os dados do evento
     * @return um int que representa sucesso ou insucesso
     */
    public int createEvent(RequestCreateEvent requestCreateEvent) {
        return DBManager.getEventDao().insert(
                requestCreateEvent.getName(),
                requestCreateEvent.getRoomId(),
                requestCreateEvent.getGroupName(),
                requestCreateEvent.getCreatorUsername(),
                requestCreateEvent.getStartDate(),
                requestCreateEvent.getDuration());
    }

    /**
     * Verifica quais os clientes logados pertencem a um evento
     * @param event evento a qual se vai verificar os clientes pertencentes
     * @return retorna uma lista de clientes que estão no evento e logados
     */
    public ArrayList<ClientInfo> getClientInfo(Event event) {
        ArrayList<ClientInfo> clientInEvent = new ArrayList<>();
        synchronized (this.clientInfo){
            ArrayList<User> users = (ArrayList<User>) DBManager.getGroupDao().getMembers(event.getGroup().getName());

            for(int i = 0; i < clientInfo.size(); i++){
                for(int j = 0; j < users.size(); j++){
                    if(clientInfo.get(i).getUser().getUsername().equals(users.get(j).getUsername())){
                        clientInEvent.add(clientInfo.get(i));
                    }
                }
            }
        }
        return clientInEvent;
    }

    /**
     * Vai à BD buscar as Salas todas
     * @return uma lista de salas
     */
    public ArrayList<Room> getAllRooms() {
        return (ArrayList<Room>) DBManager.getRoomDao().getAll();
    }

    /**
     * Vai à BD buscar uma lista de eventos inscritos
     * @param user utilizador que pediu
     * @return uma lista de eventos isncritos deste user
     */
    public ArrayList<Event> getUserEvents(User user) {
        return (ArrayList<Event>) DBManager.getUserDao().getEventsAccepted(user.getUsername());
    }

    /**
     * Vai à BD buscar uma lista de eventos por aceitar ou recusar
     * @param user utilizador que pediu
     * @return uma lista de eventos por aceitar ou recusar deste user
     */
    public ArrayList<Event> getUserPendingEvents(User user) {
        return (ArrayList<Event>) DBManager.getUserDao().getEventsPending(user.getUsername());
    }

    /**
     * Vai à BD buscar os grupos
     * @return uma lista de grupos
     */
    public ArrayList<Group> getAllGroups() {
        return (ArrayList<Group>) DBManager.getGroupDao().getAll();
    }

    /**
     * Vai à BD buscar os eventos creados pelo User
     * @param user user que pediu
     * @return ua lista de eventos criados pelo user
     */
    public ArrayList<Event> getCreatedEvents(User user) {
        return (ArrayList<Event>) DBManager.getEventDao().getEventsByCreator(user.getUsername());
    }

    /**
     * Vai buscar os Users todos à BD
     * @return lista de Users
     */
    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) DBManager.getUserDao().getAll();
    }

    /**
     * Vai buscar os users que não estão no grupo
     * @param groupName nome do grupo
     * @return lista de users
     */
    public List<User> getNonMembers(String groupName){
        return DBManager.getGroupDao().getNonMembers(groupName);
    }

    /**
     * Vai buscar um User À BD
     * @param username mail do user
     * @return o utlizador
     */
    public User getUser(String username) {
        return DBManager.getUserDao().get(username);
    }

    public boolean getUserPermissions(String userid) {
            return DBManager.getUserDao().get(userid).getPermissions();
    }

    /**
     * Modificar a permissão de um User
     * @param userid id do user
     * @param i tipo de permissão
     */
    public void setPermissions(String userid,boolean i) {
        DBManager.getUserDao().updatePermissions(userid, i);
    }

    /**
     * adicionar uma sala
     * @param addName nome da sala
     * @param roomTypeString tipo  de sala
     * @param addLimit limite da sala
     * @param addFeatures lista de caracteriscas da sala
     */
    public void addRoom(String addName, String roomTypeString,String addLimit, String[] addFeatures) {

        RoomType roomType = RoomType.value(roomTypeString.trim());

        ArrayList<RoomFeature> features = new ArrayList<>();
        for (String addFeature : addFeatures) {
            features.add(RoomFeature.value(addFeature.trim()));
        }

        DBManager.getRoomDao().insert(addName,Integer.parseInt(addLimit), roomType,features);
    }

    /**
     * Remove uma sala
     * @param roomId id da sala
     */
    public void removeRoom(int roomId){
        DBManager.getRoomDao().delete(roomId);
    }

    /**
     * Vai buscar uma lista de caracteristiscas de uma sala
     * @param roomId id da sala
     * @return uma lista de caracteristicas
     */
    public List<RoomFeature> getFeatures(int roomId){return DBManager.getRoomDao().getFeatures(roomId);}

    /**
     * Modifica as caracteristicas
     * @param roomId id sala
     * @param rF caracterisitca
     */
    public void updateFeatures(int roomId, RoomFeature rF){
        DBManager.getRoomDao().insertFeature(roomId, rF);
    }

    /**
     * Remove uma caracteristica
     * @param roomId id da sala
     * @param rF caracteristica
     */
    public void removeFeatures(int roomId, RoomFeature rF){
        DBManager.getRoomDao().deleteFeature(roomId, rF);
    }

    /**
     * Modifica o nome de uma sala
     * @param parseInt id da sala
     * @param editName nome novo da sala
     */
    public void updateName(int parseInt, String editName) {
        DBManager.getRoomDao().updateName(parseInt, editName);
    }

    /**
     * Modifica o limite da sala
     * @param parseInt id da sala
     * @param editLimit novo limite
     */
    public void updateLimit(int parseInt, int editLimit) {
        DBManager.getRoomDao().updateCapacity(parseInt, editLimit);
    }

    /**
     * Adiciona um grupo
     * @param addName nome do grupo
     */
    public void addGroup(String addName) {
        DBManager.getGroupDao().insert(addName);
    }

    /**
     * Vai buscar os grupos
     * @return lista de grupos
     */
    public List<Group> getGroups(){
        return DBManager.getGroupDao().getAll();
    }

    /**
     * remove um grupo
     * @param groupName nome do grupo
     */
    public void removeGroup(String groupName) {
        DBManager.getGroupDao().delete(groupName);
    }

    /**
     * adicionar um user ao grupo
     * @param groupName nome do grupo
     * @param userId id do user
     */
    public void addUserGroup(String groupName, String userId) {
        DBManager.getGroupDao().addMember(groupName, userId);
    }

    /**
     * remove um user do grupo
     * @param groupName nome do grupo
     * @param userId id do user
     */
    public void removeUserGroup(String groupName, String userId) {
        DBManager.getGroupDao().removeMember(groupName, userId);
    }

    /**
     * Vai buscar uma lsita de users de um grupo
     * @param groupName nome do grupo
     * @return uma lista de grupos
     */
    public List<User> getUsersInGroup(String groupName){
       return DBManager.getGroupDao().getMembers(groupName);
    }

    /**
     * Apaga Evento
     * @param idEvent id do evento
     * @param user o user que pediu
     * @return true caso sucesso false caso dê erro
     */
    public boolean deleteEvent(int idEvent, User user) {
        return DBManager.getEventDao().delete(idEvent);
    }

    /**
     * vai bsucar um evento
     * @param idEvent id do evento
     * @return do evento
     */
    public Event getEvent(int idEvent) {
        return DBManager.getEventDao().get(idEvent);
    }

    /**
     * cancelar uma subscrição
     * @param idEvent id do eventp
     * @param user user que pretende cancenlar
     * @return true caso sucesso ou false caso erro
     */
    public boolean cancelSubscription(int idEvent, User user) {
        return DBManager.getEventDao().cancelEvent(idEvent, user.getUsername());
    }

    /**
     * Aceita evento
     * @param idEvent id do evento
     * @param user user que prentende se isncrever
     * @return true caso sucesso ou false caso erro
     */
    public boolean acceptEvent(int idEvent, User user) {
        return DBManager.getEventDao().acceptEvent(idEvent,user.getUsername());
    }

    /**
     * Rejeita um evento
     * @param idEvent id do evento
     * @param user user que quer recusar o evento
     * @return true caso sucesso false caso erro
     */
    public boolean declineEvent(int idEvent, User user) {
        return DBManager.getEventDao().refuseEvent(idEvent,user.getUsername());
    }

    /**
     * Remover um user quando ele se desliga
     * @param user utilziador que se desligou
     */
    public void removeClientInfo(User user){
        synchronized (this.clientInfo){
            for(int i = 0; i< clientInfo.size(); i++){
                if(user.getUsername().equals(clientInfo.get(i).getUser().getUsername())){
                    clientInfo.remove(i);
                    return;
                }
            }
        }
    }
}
