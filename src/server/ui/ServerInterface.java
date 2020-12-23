package server.ui;

import server.logic.ServerLogic;
import shared_data.helper.KeepAlive;

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
                break;
            case 2:
                break;
            case 3:
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
}
