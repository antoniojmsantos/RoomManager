package client.gui;

import client.logic.ClientObservable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LogInPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    public LogInPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);


        ImageView img_logo = new ImageView(Images.getImage(Constants.LOGO));
        ImageView img_avatar = new ImageView(Images.getImage(Constants.AVATAR));
        img_logo.setFitWidth(400);
        img_logo.setPreserveRatio(true);
        img_avatar.setFitHeight(200);
        img_avatar.setFitWidth(200);
        img_avatar.setPreserveRatio(true);

        HBox boxLogo = new HBox();
//        boxLogo.setStyle("-fx-background-color: red");
        boxLogo.setAlignment(Pos.CENTER);
        boxLogo.setPadding(new Insets(75,0,75,0));
        boxLogo.getChildren().add(img_logo);

        HBox boxAvatar = new HBox();
        boxAvatar.setAlignment(Pos.CENTER);
        boxAvatar.setPadding(new Insets(20));
        boxAvatar.getChildren().add(img_avatar);


        VBox boxLogin = new VBox(10);
        boxLogin.setAlignment(Pos.CENTER);
        boxLogin.setPadding(new Insets(10));

        TextField txt_username = new TextField();
        txt_username.setPromptText("Email da Organização");
        txt_username.setMaxWidth(450);
        txt_username.setFocusTraversable(false);

        TextField txt_password = new TextField();
        txt_password.setPromptText("Palavra-Passe");
        txt_password.setMaxWidth(450);
        txt_password.setFocusTraversable(false);

        boxLogin.getChildren().addAll(txt_username, txt_password);

        HBox boxEntrar = new HBox();
        boxEntrar.setSpacing(125);
        boxEntrar.setAlignment(Pos.CENTER);
        Hyperlink hl_registo = new Hyperlink("Não tem conta? Registe-se aqui");
        hl_registo.setFocusTraversable(false);
        Button bt_entrar = new Button("Entrar");
        bt_entrar.setPrefWidth(150);

        boxEntrar.getChildren().addAll(hl_registo, bt_entrar);

        getChildren().addAll(boxLogo, boxAvatar, boxLogin, boxEntrar);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
