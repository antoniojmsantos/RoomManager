package server.ui;

import server.logic.ServerLogic;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;

import java.util.ArrayList;
import shared_data.entities.Group;

import java.util.List;
import java.util.Scanner;

public class ServerInterface extends Thread {

    private ServerLogic serverLogic;
    private Scanner scan;

    public ServerInterface(ServerLogic serverLogic){
        this.serverLogic = serverLogic;
        this.scan = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (KeepAlive.getKeepAlive()){
            showMenu();
            processChoice();
        }
    }

    private void processChoice() {
        String choice = scan.nextLine();
        switch (Integer.parseInt(choice)){
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

    private void showMenu() {
        System.out.println("------Menu-------");
        System.out.println("1 - Manage Rooms ");
        System.out.println("2 - Manage Roles ");
        System.out.println("3 - Manage Groups");
        System.out.println("-----------------");
    }

    private void manageRooms(){
        System.out.println("------Room Management-------");
        System.out.println("1 - Add Room ");
        System.out.println("2 - Edit Room ");
        String choice = scan.nextLine();
        switch(Integer.parseInt(choice)){
            case 1:
                addRoom();
                break;
            case 2:
                editRoom();
                break;
            default:
                System.out.println("Choose a valid option");
                break;
        }

    }

    private void manageRoles(){

        String[] input = new String[2];
        boolean stop = false;
        do {
            System.out.println("Lista de utilizadores:");
            System.out.println("(username) | (tem permissões?)");
            System.out.println("---");

            for (User user: serverLogic.getAllUsers()) {
                System.out.println(user.getUsername()+" | "+user.getPermissions());
            }

            System.out.println("---");
            System.out.println("> ");

            if (scan.hasNext()) {
                input = scan.nextLine().split(" ");

                if (input.length != 2) {
                    System.out.println("Syntax: > <username> <has-permissions>");
                } else if (serverLogic.getUser(input[0]) == null) {
                    System.out.println("Warning: user \""+input[0]+"\" does not exist!");
                } else {
                    stop = true;
                }
            }
        } while (!stop);

        serverLogic.setPermissions(input[0], Boolean.parseBoolean(input[1]));
    }



    private void manageGroups(){
        System.out.println("------Group Management-------");

        System.out.println("1- Create Groups");
        System.out.println("2- Manage Groups");

        String choice = scan.nextLine();
        switch(Integer.parseInt(choice)){
            case 1:
                createGroup();
                break;
            case 2:
                modifyGroups();
                break;
            default:
                System.out.println("Choose a valid option");
                break;
        }
    }

    private void addRoom(){
        System.out.print("Name = ");
        String addName = scan.nextLine();

        System.out.printf("\nRoom Type = ");
        String addType = scan.nextLine();

        System.out.print("\nLimit = ");
        String addLimit = scan.nextLine();

        System.out.print("\nFeatures = ");
        String addFeatures = scan.nextLine();

        serverLogic.addRoom(addName, addType, addLimit, addFeatures);

    }

    private void editRoom(){
        System.out.println("------Room Administration-------");
        System.out.println("Room ID: ");
        String editRoom = scan.nextLine();

        System.out.println("1- Edit Name");
        System.out.println("2- Edit Max Limit");
        System.out.println("3- Edit Description");

        String choice = scan.nextLine();
        switch(Integer.parseInt(choice)){
            case 1:
                String editName = scan.nextLine();
                serverLogic.updateName(Integer.parseInt(editRoom), editName);
                break;

            case 2:
                String editLimit = scan.nextLine();
                serverLogic.updateLimit(Integer.parseInt(editRoom), Integer.parseInt(editLimit));
                break;

            case 3:
                String editDescription = scan.nextLine();
                serverLogic.updateDescription(Integer.parseInt(editRoom), editDescription );
                break;

            default:
                System.out.println("Choose a valid option");
                break;
        }

    }

    public void createGroup(){
        System.out.println("Name = ");
        String addName = scan.next();
        serverLogic.addGroup(addName);
    }

    public void modifyGroups(){
        List<Group> groups = serverLogic.getGroups();
        for(int i=0; i < groups.size(); i++){
            System.out.println(groups.get(i).getName());
        }
    }
}
