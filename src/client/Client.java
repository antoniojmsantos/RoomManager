package client;

import client.communication.ClientCommunication;
import client.gui.Constants;
import client.gui.Images;
import client.gui.PaneOrganizer;
import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shared_data.helper.KeepAlive;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Application {

    ClientCommunication clientCommunication;

    @Override
    public void start(Stage primaryStage) throws Exception{
        clientCommunication = new ClientCommunication();
//        try{
//            clientCommunication = new ClientCommunication();
//            clientCommunication.run();
//        } catch (SocketException | UnknownHostException e) {
//            KeepAlive.emergencyExit(e,"Falha a criar a comunicação");
//        } catch (IOException e) {
//            KeepAlive.emergencyExit(e,"Falha ao enviar o priemiro contacto");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        ClientController controller = new ClientController(clientCommunication);
        ClientObservable observable = new ClientObservable(controller);

        PaneOrganizer organizer = new PaneOrganizer(observable);
        Scene scene = new Scene(organizer.getRoot());

        primaryStage.getIcons().add(Images.getImage(Constants.ICON));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RoomManager");
        primaryStage.show();

        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
