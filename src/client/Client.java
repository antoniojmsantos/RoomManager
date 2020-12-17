package client;

import client.gui.PaneOrganizer;
import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ClientController controller = new ClientController();
        ClientObservable observable = new ClientObservable(controller);

        PaneOrganizer organizer = new PaneOrganizer(observable);
        Scene scene = new Scene(organizer.getRoot());

//        primaryStage.getIcons().add(Imagens.getImagem(Constants.SPACESHIP));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Planet Bound - The Game");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
