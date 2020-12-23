package client.gui;

import client.logic.ClientObservable;
import com.sun.javafx.scene.control.IntegerField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.CharacterCodingException;

public class CreateEventPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    TextField txt_nome, txt_grupo, txt_sala;

    Button bt_criaevento;

    CheckBox cb_anfi, cb_lab, cb_salareuniao;
    CheckBox cb2_projetor, cb2_mesareuniao, cb2_windows, cb2_macos, cb2_quadroiterativo, cb2_arcondicionado;

    public CreateEventPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        txt_nome= new TextField();
        txt_grupo= new TextField();
        txt_sala = new TextField();

//        n_lot = new IntegerField();
        setupLogo();
        setupEventoInfo();
        setupFiltragemSalas();
        //setupEntrar();

       propertyChange(null);

    }

    public void setupLogo() {
        ImageView img_logo = new ImageView(Images.getImage(Constants.LOGO));
        img_logo.setFitWidth(400);
        img_logo.setPreserveRatio(true);

        HBox boxLogo = new HBox();
//        boxLogo.setStyle("-fx-background-color: red");
        boxLogo.setAlignment(Pos.CENTER);
        boxLogo.setPadding(new Insets(75,0,75,0));
        boxLogo.getChildren().add(img_logo);

        this.getChildren().add(boxLogo);
    }

    public void setupEventoInfo(){

        VBox boxRegister = new VBox(10);
        boxRegister.setAlignment(Pos.TOP_CENTER);
        boxRegister.setPadding(new Insets(0));


        txt_nome.setPromptText("Nome Evento");
        txt_nome.setMaxWidth(450);
        txt_nome.setFocusTraversable(true);

        txt_grupo.setPromptText("Grupo Pertencente");
        txt_grupo.setMaxWidth(450);
        txt_grupo.setFocusTraversable(true);

        txt_sala.setPromptText("Nome da Sala");
        txt_sala.setMaxWidth(450);
        txt_sala.setFocusTraversable(true);

        boxRegister.getChildren().addAll(txt_nome, txt_grupo, txt_sala );


        this.getChildren().add(boxRegister);


    }

    public void setupFiltragemSalas(){

        VBox boxFiltro = new VBox(10);
        boxFiltro.setAlignment(Pos.TOP_CENTER);
        boxFiltro.setPadding(new Insets(0));


        cb_anfi = new CheckBox("Anfiteatro");
        cb_lab = new CheckBox("Laboratório");
        cb_salareuniao = new CheckBox("Sala Reunião");

        cb2_projetor = new CheckBox("Projetor");
        cb2_mesareuniao = new CheckBox("Mesa Reunião");
        cb2_windows = new CheckBox("Computadores Windows");
        cb2_macos = new CheckBox("Computadores MacOS");
        cb2_quadroiterativo = new CheckBox("Quadro Iterativo");
        cb2_arcondicionado = new CheckBox("Ar Comdicionado");

        boxFiltro.getChildren().addAll(cb_anfi, cb_lab, cb_salareuniao, cb2_projetor, cb2_mesareuniao, cb2_windows, cb2_macos, cb2_quadroiterativo, cb2_arcondicionado );

        this.getChildren().add(boxFiltro);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateCreate());


    }


}


