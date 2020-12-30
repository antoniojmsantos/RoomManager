package client;

import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.gui.auxiliar.PaneOrganizer;
import client.logic.ClientObservable;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;

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
            //CÓDIGO PARA POR ICON NO MACOS
            Taskbar taskbar=Taskbar.getTaskbar();
            Image img = Images.getImage(Constants.ICON);
            BufferedImage image = SwingFXUtils.fromFXImage(img, null);
            taskbar.setIconImage(image);
        }
        catch (Exception e){
            //(THROWS EXCEPTION NO WINDOWS)
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
