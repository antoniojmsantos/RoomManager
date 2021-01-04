package client;

import client.communication.threads.ThreadUpdateEventsView;
import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.gui.auxiliar.PaneOrganizer;
import client.logic.ClientObservable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shared_data.helper.MyMutex;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Platform.setImplicitExit(false);
        MyMutex mutex = new MyMutex();

        ClientObservable observable = new ClientObservable(mutex);
        observable.init();

        ThreadUpdateEventsView threadUpdateEventsView = new ThreadUpdateEventsView(mutex,observable);
        threadUpdateEventsView.start();

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
