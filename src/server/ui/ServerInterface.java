package server.ui;

import server.logic.ServerLogic;
import shared_data.entities.User;
import shared_data.helper.KeepAlive;

import java.util.ArrayList;
import shared_data.entities.Group;
import shared_data.helper.KeepAlive;

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

        //1º-> É apresentada a lista de utilizadores registados na base de dados
        ArrayList<User> users=serverLogic.getAllUsersInterface();
        for(int i=0;i<users.size();i++){
            System.out.println(users.get(i).getUsername()+"\n");
        }

        //2º-> É possível escolher um utilizador específico, para alterar as suas permissões, inserindo o username do utilizador
        System.out.println("\nUsername:\t");
        String userid = scan.nextLine();

        //3º-> É apresentada uma lista de permisões, sendo possível alterá-las
        Boolean permissions= serverLogic.getUserPermissions(userid);

                //docente if(permissions)

        if(permissions)
            System.out.println("O user é docente.\n");
        else if(!permissions) System.out.println("O user é aluno.\n");
        else System.out.println("Username não encontrado.\n");

        //  É feito através da inserção do nome da permissão e do valor a registar para a permissão
        System.out.println("\nPermissão:\t");
        String perm = scan.nextLine();

        if(perm.equals("aluno")) {
            if(!permissions)
                System.out.println("\nUser já com role de aluno.");
            //4º-> O sistema atualiza a base de dados (Tabela correspondente às permissões do utilizador)
            else serverLogic.setPermissions(userid, false);
        }
        else if(perm.equals("docente")) {
            if(!permissions)
                System.out.println("\nUser já com role de docente.");
            //4º-> O sistema atualiza a base de dados (Tabela correspondente às permissões do utilizador)
            else serverLogic.setPermissions(userid, false);
        }
        else System.out.println("Nome de permissao invalido.");

        //5º-> O administrador é remetido para o ecrã de administrasção de utilizadores
        run();
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
