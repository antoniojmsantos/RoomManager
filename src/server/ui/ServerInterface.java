package server.ui;

import server.logic.ServerLogic;
import shared_data.entities.*;
import shared_data.helper.KeepAlive;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

public class ServerInterface extends Thread {

    private ServerLogic serverLogic;
    private Scanner scan;

    public ServerInterface(ServerLogic serverLogic) {
        this.serverLogic = serverLogic;
        this.scan = new Scanner(System.in);
        scan.useDelimiter("\\n");
    }

    @Override
    public void run() {
        while (KeepAlive.getKeepAlive()) {
            showMenu();
            processChoice();
        }
    }

    private void showMenu() {
        System.out.println("------Menu-------");
        System.out.println("1 - Manage Rooms ");
        System.out.println("2 - Manage Roles ");
        System.out.println("3 - Manage Groups");
        System.out.println("-----------------");
    }

    private void processChoice() {
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                manageRooms();
                break;
            case 2:
                manageRoles();
                break;
            case 3:
                manageGroups();
                break;
            default:
                System.out.println("Choose a valid option");
                break;
        }
    }


    private void manageRooms() {
        System.out.println("------Room Management-------");
        System.out.println("1 - Add Rooms ");
        System.out.println("2 - Edit Rooms ");
        System.out.println("3 - View Rooms");
        System.out.println("4 - Back");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                addRoom();
                break;
            case 2:
                editRoom();
                break;
            case 3:
                showRooms();
                manageRooms();
                break;
            case 4:
                run();
                break;
            default:
                System.out.println("Choose a valid option");
                manageRooms();
                break;
        }

    }

    //FUNÇÃO QUE PROCESSA EDIÃO DAS ROLES DOS UTILIZADORES
    private void manageRoles() {

        String[] input = new String[2];
        boolean stop = false;
        do {
            System.out.println("Lista de utilizadores:");
            System.out.println("(username) | (tem permissões?)");
            System.out.println("---");

            for (User user : serverLogic.getAllUsers()) {
                System.out.println(user.getUsername() + " | " + user.getPermissions());
            }

            System.out.println("---");
            System.out.println("> ");

            if (scan.hasNext()) {
                input = scan.nextLine().split(" ");

                if (input.length != 2) {
                    System.out.println("Syntax: > <username> <has-permissions>");
                } else if (serverLogic.getUser(input[0]) == null) {
                    System.out.println("Warning: user \"" + input[0] + "\" does not exist!");
                } else if (input[0].equals("exit")) {
                    return;
                } else {
                    if (input[1].equals("true") || input[1].equals("false"))
                        stop = true;
                    else
                        System.out.println("Syntax Error: > <username> <true (or) false>");
                }
            }
        } while (!stop);

        serverLogic.setPermissions(input[0], Boolean.parseBoolean(input[1]));
    }


    //FUNÇÃO QUE PROCESSA EDIÃO DOS GRUPOS DOS UTILIZADORES
    private void manageGroups() {
        System.out.println("------Group Management-------");
        System.out.println("1 - Create Groups");
        System.out.println("2 - Edit Groups");
        System.out.println("3 - View Groups");
        System.out.println("4 - Back");
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                createGroup();
                break;
            case 2:
                modifyGroups();
                break;
            case 3:
                viewGroups();
                manageGroups();
                break;
            case 4:
                run();
                break;
            default:
                System.out.println("Choose a valid option");
                manageGroups();
                break;
        }
    }

    //FUNÇÃO QUE TRATA DE ADICIONAR UMA SALA À BASE DE DADOS. AQUI SÃO INTRODUZIDOS OS VALORES PELO UTILIZADOR
    private void addRoom() {
        System.out.print("Name = ");
        String addName = scan.nextLine();

        while (addName.isEmpty() || !checkRoomName(addName)) {
            System.out.print("<Invalid Name.Try Again>\nName = ");
            addName = scan.nextLine();
        }

        System.out.println("------Room Type-------");
        for(RoomType rT : RoomType.values())
            System.out.println(rT.getValue());
        System.out.println("----------------------");

        System.out.print("Room Type = ");
        String addType = scan.nextLine();

        while (!checkRoomType(addType)) {
            System.out.print("<Invalid Room Type.Try Again>\nRoom Type = ");
            addType = scan.nextLine();
        }

        System.out.print("Limit = ");
        String addLimit = scan.nextLine();

        while (Integer.parseInt(addLimit) <= 0) {
            System.out.print("<Invalid Capacity.Try Again>\nCapacity = ");
            addLimit = scan.nextLine();
        }

        System.out.println("-----Feature Options-----");
        for(RoomFeature rF : RoomFeature.values()){
            System.out.println(rF.getValue());
        }
        System.out.println("-------------------------");

        System.out.print("Features = ");
        String[] addFeatures = scan.nextLine().split(" , ");

        serverLogic.addRoom(addName, addType, addLimit, addFeatures);
        manageRooms();
    }

    //FUNÇÃO QUE VALIDA O NOME INTRODUZIDO PELO UTILIZADOR DA SALA. RETORNA FALSO SE O NOME INTRODUZIDO JÁ EXISTIR NA BASE DE DADOS
    public boolean checkRoomName(String addName) {
        List<Room> allRooms = serverLogic.getAllRooms();
        for (int i = 0; i < allRooms.size(); i++) {
            if (addName.equals(allRooms.get(i).getName()))
                return false;
        }
        return true;
    }

    //FUNÇÃO QUE VALIDA O TIPO DE SALA. RETORNA FALSEO SE ESCREVER TIPO DE SALA INVÁLIDO
    public boolean checkRoomType(String roomType) {
        try{
            RoomType.value(roomType);
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }
    }

    //FUNÇÃO QUE PERMITE EDITAR NOME, CAPACIDADE E FEATURES DUMA SALA. PERMITE TAMBÉM APAGAR UMA SALA POR COMPLETO
    private void editRoom() {

        System.out.println("--Choose the room you wish to edit--");
        showRooms();
        int room = scan.nextInt();
        int room_id = serverLogic.getAllRooms().get(room).getId();
        System.out.println("--Choose the field you wish to edit--");
        System.out.println("1->Room Name\n2->Room Max Capacity\n3->Room Features\n4->Delete Room");
        int choice = scan.nextInt();
        switch(choice){
            case 1: System.out.print("New Name= ");
                    String newName = scan.next();
                    if(!checkRoom(newName)){
                        System.out.print("Error: Invalid Name\nRepeat Name = ");
                        newName = scan.next();
                    }
                    serverLogic.updateName(room_id, newName);
                    break;
            case 2: System.out.print("New Capacity= ");
                    int cap = scan.nextInt();
                    if(cap <= 0){
                        System.out.print("Error: Invalid Capacity\nRepeat Capacity = ");
                        cap = scan.nextInt();
                    }
                    serverLogic.updateLimit(room_id, cap);
                    break;
            case 3: System.out.println("1->Add Feature\n2->Remove Feature");
                    int choice2 = scan.nextInt();
                    if(choice2 == 1){
                        System.out.println("-----Choose one of the Feature Options-----");
                        int k=0;
                        for(RoomFeature rF : RoomFeature.values()){
                            System.out.println(k +1 + "->" + rF.getValue());
                            k++;
                        }
                        System.out.println("-------------------------");
                        int feature = scan.nextInt();
                        switch(feature){
                            case 1:serverLogic.updateFeatures(room_id, RoomFeature.AR_CONDICIONADO);break;
                            case 2:serverLogic.updateFeatures(room_id, RoomFeature.COMPUTADORES_MAC);break;
                            case 3:serverLogic.updateFeatures(room_id, RoomFeature.COMPUTADORES_WINDOWS);break;
                            case 4:serverLogic.updateFeatures(room_id, RoomFeature.PROJETOR);break;
                            case 5:serverLogic.updateFeatures(room_id, RoomFeature.MESA_REUNIAO);break;
                            case 6:serverLogic.updateFeatures(room_id, RoomFeature.QUADRO_INTERATIVO);break;
                        }
                        break;
                    }
                    else if(choice2 == 2){
                        System.out.println("-----Choose one of the Feature Options-----");

                        List<RoomFeature> getFeatures = serverLogic.getFeatures(room_id);
                        for(int z=0; z<getFeatures.size(); z++){
                            System.out.println(z+"->"+getFeatures.get(z).getValue());
                        }

                        int feature = scan.nextInt();
                        serverLogic.removeFeatures(room_id, getFeatures.get(feature));
                    }
            case 4: serverLogic.removeRoom(room_id);break;

        }
        manageRooms();
    }

    //FUNÇÃO QUE VALIDA NOME DUMA SALA. RETORNA FALSE SE O NOME DA SALA JÁ EXISTIR NA BASE DE DADOS
    public boolean checkRoom(String name){
        List<Room> allRooms = serverLogic.getAllRooms();
        for (int i = 0; i < allRooms.size(); i++) {
            if (name.equals(allRooms.get(i).getName()))
                return false;
        }
        return true;
    }


    //FUNÇÃO QUE MOSTRA TODAS AS SALAS
    public void showRooms() {
        ArrayList<Room> allRooms = serverLogic.getAllRooms();
        if (allRooms.isEmpty())
            System.out.println("<There are no Rooms Available>");
        else {
            for (int i = 0; i < allRooms.size(); i++)
                System.out.println(i + "-> Id: " + allRooms.get(i).getId() + "\tName: " + allRooms.get(i).getName());
        }
    }

    //FUNÇÃO QUE TRATA DE PROCESSAR A CRAIÇÃO DE UM NOVO GRUPO
    public void createGroup() {
        System.out.print("Name = ");
        String addName = scan.next();
        while (!checkGroupName(addName)) {
            System.out.println("Error: <Group Name Already in Use>\nName = ");
            addName = scan.nextLine();
        }
        serverLogic.addGroup(addName);
        manageGroups();
    }

    //VERIFICA se já existe um grupo igual a name (Retorna falso se existir)
    public boolean checkGroupName(String name) {
        List<Group> allGroups = serverLogic.getGroups();
        for (int i = 0; i < allGroups.size(); i++) {
            if (name.equals(allGroups.get(i).getName()))
                return false;
        }
        return true;
    }

    //FUNÇÃO QUE TRATA DO PROCESSAMENTO DE EDIT DOS GRUPOS.
    public void modifyGroups() {

        System.out.println("--Choose the Group you want to Modify--");
        viewGroups();
        int i = scan.nextInt();
        String groupName = serverLogic.getGroups().get(i).getName();
        System.out.println(groupName);

        System.out.println("--Choose the Operation you want to preform--");

        System.out.println("1->Add User\n2->Remove User\n3->Delete Group");
        int operationI = scan.nextInt();

        if(operationI == 1){

            System.out.println("--Choose the User you want to Add--");
            showUserNotInGroup(groupName);
            int user = scan.nextInt();
            String userId = serverLogic.getNonMembers(groupName).get(user).getUsername();
            System.out.println(userId);
            serverLogic.addUserGroup(groupName, userId);
            manageGroups();
        }
        else if(operationI == 2){
            System.out.println("--Choose the User you wish to Remove--");
            viewGroup(groupName);
            int user = scan.nextInt();
            String userId = serverLogic.getUsersInGroup(groupName).get(user).getUsername();
            System.out.println(userId);
            serverLogic.removeUserGroup(groupName, userId);
            manageGroups();
        }
        else if(operationI == 3){
            serverLogic.removeGroup(groupName);
            manageGroups();
        }
    }


    //MOSTRA USERS EXISTENTES NA BASE DE DADOS
    public void showUsersDb(){
        List<User> allUsersDb = serverLogic.getAllUsers();
        for(int i = 0; i< allUsersDb.size(); i++)
            System.out.println(i + "-> " + allUsersDb.get(i).getName());
    }

    //VALIDA SE O USER EXISTE NA BASE DE DADOS, RETURN TRUE SE EXISTIR
    public boolean checkUserDb(String id) {
        List<User> allUsers = serverLogic.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (id.equals(allUsers.get(i).getName()))
                return true; //se user existe na bd
        }
        return false; //se user não existe na bd
    }

    //VALIDA SE O USER EXISTE NO GRUPO ESCOLHIDO (RETURN TRUE SE EXISTIR)
    public boolean checkUserGroup(String id, String groupName) {
        List<User> usersInGroup = serverLogic.getUsersInGroup(groupName);
        for (int j = 0; j < usersInGroup.size(); j++) {
            if (id.equals(usersInGroup.get(j).getName()))
                return true; //true se o user já existe nesse grupo
        }
        return false;//user não existe nesse grupo
    }


    //FUNÇÃO QUE MOSTRA OS GRUPOS E UTILIZADORES INSCRITOS EM CADA GRUPO
    public void viewGroups(){
        List<Group> allGroups = serverLogic.getGroups();
        if(allGroups.isEmpty())
        {
            System.out.println("<There are no Groups>");
            return;
        }
        for(int i = 0; i < allGroups.size(); i++) {
            List<User> usersInGroup = serverLogic.getUsersInGroup(allGroups.get(i).getName());
            System.out.println( i + "->" + allGroups.get(i).getName() + ":");

            if (usersInGroup.isEmpty())
                System.out.print("\t<There are no Users in this Group>\n");
            else {
                for (int j = 0; j < usersInGroup.size(); j++) {
                    System.out.print("\t-> " + usersInGroup.get(j).getName() + "\n");
                }
            }
        }
    }

    //MOSTRA USERS NUM GRUPO ESPECÍFICO
    public void viewGroup(String groupName){
        List<Group> allGroups = serverLogic.getGroups();
        for(int i = 0; i < allGroups.size(); i++) {
            if(groupName.equals(allGroups.get(i).getName())){
                List<User> usersInGroup = serverLogic.getUsersInGroup(allGroups.get(i).getName());
                for (int j = 0; j < usersInGroup.size(); j++)
                    System.out.print(j+"-> " + usersInGroup.get(j).getName() + "\n");

            }

        }
    }

    //MOSTRA TODOS OS USERS QUE NÃO ESTEJA NO DETERMINADO GRUPO
    public void showUserNotInGroup(String groupName) {
        List<User> usersNotInGroup = serverLogic.getNonMembers(groupName);
        for(int i = 0; i < usersNotInGroup.size(); i++){
            System.out.println(i + "-> " + usersNotInGroup.get(i).getName());
        }
    }
}
