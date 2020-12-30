package client.gui;

import client.logic.ClientObservable;
import com.sun.javafx.scene.control.IntegerField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.CharacterCodingException;

public class CreateEventPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    VBox boxEventFilters;

    TextField txt_nome, txt_grupo, txt_sala, txt_horainicio;
    TextField txt_lotacao, txt_duracao;

    ListView<String> listRooms;

    CheckBox cb_anfi, cb_lab, cb_salareuniao;
    CheckBox cb2_projetor, cb2_mesareuniao, cb2_windows, cb2_macos, cb2_quadroiterativo, cb2_arcondicionado;

    public CreateEventPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        txt_nome= new TextField();
        txt_grupo= new TextField();
        txt_sala = new TextField();
        txt_horainicio = new TextField();
        txt_lotacao = new TextField();
        txt_duracao = new TextField();


        boxEventFilters = new VBox(10);
        boxEventFilters.setPadding(new Insets(30));

        setupEventFilters();
        showRooms();
        setupButton();

        this.getChildren().add(boxEventFilters);

       propertyChange(null);

    }


    public void setupEventFilters(){

        VBox boxAux = new VBox(10); //Auxiliar box because it will have 2 vertical lines of info

        HBox boxFilters1 = new HBox(10);

        txt_nome.setPromptText("Nome Evento");
        txt_grupo.setPromptText("Grupo Pertencente");
        txt_horainicio.setPromptText("Hora Início");

        boxFilters1.getChildren().addAll(txt_nome, txt_grupo, txt_horainicio);


        HBox boxFilters2 = new HBox(10);

        txt_sala.setPromptText("Nome da Sala");
        txt_duracao.setPromptText("Lotação");
        txt_lotacao.setPromptText("Duração");

        boxFilters2.getChildren().addAll(txt_sala, txt_duracao, txt_lotacao);

        boxAux.getChildren().addAll(boxFilters1, boxFilters2);

        boxEventFilters.getChildren().add(boxAux);

        setupRoomType();
    }

    public void setupRoomType(){

        HBox boxAux = new HBox(10);


        TitledPane paneFilterType = new TitledPane();
        paneFilterType.setText("Tipo de Sala");
        paneFilterType.setCollapsible(false);
        VBox boxFilterType = new VBox(10);
        cb_anfi = new CheckBox("Anfiteatro");
        cb_lab = new CheckBox("Laboratório");
        cb_salareuniao = new CheckBox("Sala Reunião");

        boxFilterType.getChildren().addAll(cb_anfi, cb_lab, cb_salareuniao);
        paneFilterType.setContent(boxFilterType);


//        VBox boxFilterDetails = new VBox(10);
        TitledPane paneFilterDetails = new TitledPane();
        paneFilterDetails.setText("Características");
        paneFilterDetails.setCollapsible(false);
        HBox boxAux1 = new HBox();
        VBox boxVAux = new VBox(10);
        VBox boxVAux1 = new VBox(10);
        cb2_projetor = new CheckBox("Projetor");
        cb2_mesareuniao = new CheckBox("Mesa Reunião");
        cb2_windows = new CheckBox("Computadores Windows");
        boxVAux.getChildren().addAll(cb2_projetor, cb2_mesareuniao, cb2_windows);
        cb2_macos = new CheckBox("Computadores MacOS");
        cb2_quadroiterativo = new CheckBox("Quadro Iterativo");
        cb2_arcondicionado = new CheckBox("Ar Condicionado");
        boxVAux1.getChildren().addAll(cb2_macos, cb2_quadroiterativo, cb2_arcondicionado);

        boxAux1.getChildren().addAll(boxVAux, boxVAux1);

        paneFilterDetails.setContent(boxAux1);


        boxAux.getChildren().addAll(paneFilterType, paneFilterDetails);

        boxEventFilters.getChildren().add(boxAux);

    }

    public void showRooms(){
//        //AQUI É SUPOSTO SEREM MOSTRADAS AS SALAS QUE CORRESPONDAM AO QUE FOI FILTRADO PELO UTILIZADOR

        TitledPane paneRooms = new TitledPane();
        paneRooms.setText("Salas com estes filtros");
        paneRooms.setCollapsible(false);

        listRooms = new ListView<>();
//        listRooms.setStyle("-fx-border-color: black");
//        listRooms.setPrefHeight(DIM_Y_FRAME);
        ObservableList<String> items = FXCollections.observableArrayList (
                "Sala 1", "Sala 2", "Sala 3");
        listRooms.setItems(items);

        paneRooms.setContent(listRooms);

        boxEventFilters.getChildren().add(paneRooms);

    }

    public void setupButton(){
        HBox boxCreate = new HBox(10);
        boxCreate.setAlignment(Pos.CENTER_RIGHT);
        Button btCreate = new Button("Criar Evento");
        Button btCancel = new Button("Cancelar");
        btCancel.setOnAction(e-> observable.setStateMain());

        boxCreate.getChildren().addAll(btCreate, btCancel);

        boxEventFilters.getChildren().add(boxCreate);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateCreate());


    }


}


