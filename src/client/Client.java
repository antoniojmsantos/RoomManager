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

    @Override
    public void start(Stage primaryStage) throws Exception{
        ClientObservable observable = new ClientObservable();
        observable.init();

        PaneOrganizer organizer = new PaneOrganizer(observable);
        Scene scene = new Scene(organizer.getRoot());

        primaryStage.getIcons().add(Images.getImage(Constants.ICON));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RoomManager");
        primaryStage.show();

        try{
            //CÓDIGO PARA POR ICON NO MACOS (THROWS EXCEPTION NO WINDOWS)
            Taskbar taskbar=Taskbar.getTaskbar();
            Image img = Images.getImage(Constants.ICON);
            BufferedImage image = SwingFXUtils.fromFXImage(img, null);
            taskbar.setIconImage(image);
        }
        catch (Exception e){

        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
