package server.ui;

import database.dao.RoomDao;
import server.logic.ServerLogic;
import shared_data.entities.*;
import shared_data.helper.ClientInfo;
import shared_data.helper.KeepAlive;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

public class ServerInterface extends Thread {

    private ServerLogic serverLogic;
    private Scanner scan;

    public ServerInterface(ServerLogic serverLogic) {
        this.serverLogic = serverLogic;
        this.scan = new Scanner(System.in);
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
        String choice = scan.nextLine();
        switch (Integer.parseInt(choice)) {
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
        String choice = scan.nextLine();
        switch (Integer.parseInt(choice)) {
            case 1:
                addRoom();
                break;
            case 2:
                editRoom();
                break;
            case 3:
                showRooms();
                break;
            default:
                System.out.println("Choose a valid option");
                break;
        }

    }

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


    private void manageGroups() {
        System.out.println("------Group Management-------");
        System.out.println("1- Create Groups");
        System.out.println("2- Edit Groups");
        System.out.println("3- View Groups");

        String choice = scan.nextLine();
        switch (Integer.parseInt(choice)) {
            case 1:
                createGroup();
                break;
            case 2:
                modifyGroups();
                break;
            case 3:
                viewGroups();
                break;
            default:
                System.out.println("Choose a valid option");
                break;
        }
    }

    private void addRoom() {
        System.out.print("Name = ");
        String addName = scan.nextLine();

        while (addName.isEmpty() || checkRoomName(addName) == false) {
            System.out.print("<Invalid Name.Try Again>\nName = ");
            addName = scan.nextLine();
        }

        System.out.print("Room Type = ");
        String addType = scan.nextLine();

        while (checkRoomType(addType) == false) {
            System.out.print("<Invalid Room Type.Try Again>\nName = ");
            addType = scan.nextLine();
        }

        System.out.print("Limit = ");
        String addLimit = scan.nextLine();

        while (Integer.parseInt(addLimit) <= 0) {
            System.out.print("<Invalid Capacity.Try Again>\nCapacity = ");
            addLimit = scan.nextLine();
        }

        System.out.print("Features = ");
        String addFeatures = scan.nextLine();

        serverLogic.addRoom(addName, addType, addLimit, addFeatures);

    }

    public boolean checkRoomName(String addName) {
        List<Room> allRooms = serverLogic.getAllRooms();
        for (int i = 0; i < allRooms.size(); i++) {
            if (addName.equals(allRooms.get(i).getName()))
                return false;
        }
        return true;
    }

    public boolean checkRoomType(String roomType) {
        if (roomType.equals("auditorio") || roomType.equals("laboratorio"))
            return true;
        return false;
    }


    private void editRoom() {
        showRooms();

        String adminResponse = scan.nextLine();
        Scanner eachResponse = new Scanner(adminResponse);
        String param1 = eachResponse.next();


        if (param1.equals("edit")) {
            String roomId = eachResponse.next();
            String changeField = eachResponse.next();
            String newValue = eachResponse.next();

            if (changeField.equals("name")) {
                if (newValue.isEmpty()) {
                    System.out.println("Invalid Room Name. <EMPTY STRING>");
                }
                serverLogic.updateName(Integer.parseInt(roomId), newValue);
            } else {
                if (changeField.equals("capacity") || changeField.equals("limit"))
                    serverLogic.updateLimit(Integer.parseInt(roomId), Integer.parseInt(newValue));
                else {
                    if (changeField.equals("description") || changeField.equals("text"))
                        serverLogic.updateDescription(Integer.parseInt(roomId), newValue);
                    else {
                        if (changeField.equals("roomfeature") || changeField.equals("feature") || changeField.equals("features")) {
                            System.out.println("add (or) delete (feature?)");
                            if (newValue.equals("add")) {
                                System.out.println("Feature name: ");
                                String newFeature = scan.next();
                                addFeature(newFeature, Integer.parseInt(roomId));
                            } else {
                                if (newValue.equals("delete")) {
                                    System.out.println("Feature name: ");
                                    String featureRemove = scan.next();
                                    removeFeature(featureRemove, Integer.parseInt(roomId));
                                }
                            }

                        }
                    }

                }
            }
        } else {
            if (param1.equals("delete")) {
                String roomId = eachResponse.next();
                serverLogic.removeRoom(Integer.parseInt(roomId));
            } else
                System.out.println("Invalid Comand");
        }

    }

    public void addFeature(String addFeatures, int roomId) {

        if (addFeatures.equals("ar_condicionado"))
            serverLogic.updateFeatures(roomId, RoomFeature.AR_CONDICIONADO);
        else {
            if (addFeatures.equals("computadores_mac"))
                serverLogic.updateFeatures(roomId, RoomFeature.COMPUTADORES_MAC);
            else {
                if (addFeatures.equals("computadores_windows"))
                    serverLogic.updateFeatures(roomId, RoomFeature.COMPUTADORES_WINDOWS);
                else {
                    if (addFeatures.equals("projetor"))
                        serverLogic.updateFeatures(roomId, RoomFeature.PROJETOR);
                    else {
                        if (addFeatures.equals("quadro_interativo"))
                            serverLogic.updateFeatures(roomId, RoomFeature.QUADRO_INTERATIVO);
                        else
                            System.out.println("Invalid Feature.");
                    }
                }

            }
        }
    }

    public void removeFeature(String addFeatures, int roomId) {
        if (addFeatures.equals("ar_condicionado"))
            serverLogic.removeFeatures(roomId, RoomFeature.AR_CONDICIONADO);
        else {
            if (addFeatures.equals("computadores_mac"))
                serverLogic.removeFeatures(roomId, RoomFeature.COMPUTADORES_MAC);
            else {
                if (addFeatures.equals("computadores_windows"))
                    serverLogic.removeFeatures(roomId, RoomFeature.COMPUTADORES_WINDOWS);
                else {
                    if (addFeatures.equals("projetor"))
                        serverLogic.removeFeatures(roomId, RoomFeature.PROJETOR);
                    else {
                        if (addFeatures.equals("quadro_interativo"))
                            serverLogic.removeFeatures(roomId, RoomFeature.QUADRO_INTERATIVO);
                        else
                            System.out.println("Invalid Feature.");
                    }
                }

            }
        }
    }

    public void showRooms() {
        ArrayList<Room> allRooms = serverLogic.getAllRooms();
        if (allRooms.isEmpty())
            System.out.println("<There are no Rooms Available>");
        else {
            for (int i = 0; i < allRooms.size(); i++)
                System.out.println("Id: " + allRooms.get(i).getId() + "\nName: " + allRooms.get(i).getName());
        }
    }

    public void createGroup() {
        System.out.print("Name = ");
        String addName = scan.nextLine();
        while (!checkGroupName(addName)) {
            System.out.println("Error: <Group Name Already in Use>\nName = ");
            addName = scan.nextLine();
        }
        serverLogic.addGroup(addName);
    }

    //Vê se já existe um grupo igual a name (Retorna falso se existir)
    public boolean checkGroupName(String name) {
        List<Group> allGroups = serverLogic.getGroups();
        for (int i = 0; i < allGroups.size(); i++) {
            if (name.equals(allGroups.get(i).getName()))
                return false;
        }
        return true;
    }

    public void modifyGroups() {
        viewGroups();

        String adminResponse = scan.nextLine();
        Scanner eachResponse = new Scanner(adminResponse);
        String param1 = eachResponse.next();
        String groupName = eachResponse.next();

        if (param1.equals("edit")) {
            String operation = eachResponse.next();
            String userId = eachResponse.next();

            while (!checkUserDb(userId)) {
                System.out.print("Error: <Inexisting User>\nUser Name = ");
                userId = scan.nextLine();
            }

            if (operation.equals("add")) {
                while(checkGroupName(groupName)){
                    System.out.print("Error: <Inexisting Group>\nGroup Name= ");
                    groupName = scan.nextLine();
                }
                while(checkUserGroup(userId, groupName)){
                    System.out.print("Error: <User Already Exists in this Group>\nUser Name= ");
                    userId = scan.nextLine();
                    while (!checkUserDb(userId)) {
                        System.out.print("Error: <Inexisting User>\nUser Name = ");
                        userId = scan.nextLine();
                    }
                }
                serverLogic.addUserGroup(groupName, userId);
            } else {
                if (operation.equals("remove")) {
                    while (checkGroupName(groupName)) {
                        System.out.print("Error: <Inexisting Group>\nGroup Name = ");
                        groupName = scan.nextLine();
                    }
                    serverLogic.removeUserGroup(groupName, userId);
                } else
                    System.out.println("Invalid Operation. Valid Operations are: add ; remove \n");
            }
        } else {
            if (param1.equals("delete")) {
                while (checkGroupName(groupName)) {
                    System.out.print("Error: <Inexisting Group>\nGroup Name = ");
                    groupName = scan.nextLine();
                }
                serverLogic.removeGroup(groupName);
            } else
                System.out.println("Invalid Comand.");
        }

    }

    public boolean checkUserDb(String id) {
        //VALIDA SE O USER EXISTE NA BASE DE DADOS, RETURN TRUE SE EXISTIR
        List<User> allUsers = serverLogic.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (id.equals(allUsers.get(i).getName()))
                return true; //se user existe na bd
        }
        return false; //se user não existe na bd
    }

    public boolean checkUserGroup(String id, String groupName) {
        //VALIDA SE O USER EXISTE NO GRUPO ESCOLHIDO (RETURN TRUE SE EXISTIR)
        List<User> usersInGroup = serverLogic.getUsersInGroup(groupName);
        for (int j = 0; j < usersInGroup.size(); j++) {
            if (id.equals(usersInGroup.get(j).getName()))
                return true; //true se o user já existe nesse grupo
        }
        return false;//user não existe nesse grupo
    }


    public void viewGroups(){
        List<Group> allGroups = serverLogic.getGroups();
        for(int i = 0; i < allGroups.size(); i++) {
            List<User> usersInGroup = serverLogic.getUsersInGroup(allGroups.get(i).getName());
            System.out.println(allGroups.get(i).getName() + ":");

            if (usersInGroup.isEmpty())
                System.out.println("<There are no Users in this Group>\n");
            else {
                for (int j = 0; j < usersInGroup.size(); j++) {
                    System.out.println("->" + usersInGroup.get(j).getName() + "\n");
                }
            }
        }
    }

}
