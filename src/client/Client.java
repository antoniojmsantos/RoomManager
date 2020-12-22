package client;

import client.communication.ClientCommunication;
import client.gui.PaneOrganizer;
import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared_data.helper.KeepAlive;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            ClientCommunication clientCommunication = new ClientCommunication();
            clientCommunication.run();
        } catch (SocketException | UnknownHostException e) {
            KeepAlive.emergencyExit(e,"Falha a criar a comunicação");
        } catch (IOException e) {
            KeepAlive.emergencyExit(e,"Falha ao enviar o priemiro contacto");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ClientController controller = new ClientController();
        ClientObservable observable = new ClientObservable(controller);

        PaneOrganizer organizer = new PaneOrganizer(observable);
        Scene scene = new Scene(organizer.getRoot());

//        primaryStage.getIcons().add(Imagens.getImagem(Constants.SPACESHIP));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RoomManager - Autenticação");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
