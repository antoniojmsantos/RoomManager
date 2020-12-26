package client.gui;

import client.logic.ClientObservable;
import com.sun.javafx.scene.control.IntegerField;
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

    TextField txt_nome, txt_grupo, txt_sala, txt_horainicio;
    TextField txt_lotacao, txt_duracao;

    ListView lv_rooms;

    Button bt_criaevento;

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

        lv_rooms = new ListView();

//        n_lot = new IntegerField();

        setupEventoInfo();
        setupRoomType();
        showRooms();
        setupButton();

       propertyChange(null);

    }


    public void setupEventoInfo(){
        VBox boxEvent = new VBox(10);
        boxEvent.setAlignment(Pos.TOP_CENTER);
        //boxEvent.setPadding(new Insets(0));

        HBox boxSub0Event = new HBox(10);
        boxSub0Event.setAlignment(Pos.TOP_CENTER);
        //boxSub0Event.setPadding(new Insets(0));

        HBox boxSub1Event = new HBox(10);
        boxSub1Event.setAlignment(Pos.TOP_CENTER);
        //boxSub1Event.setPadding(new Insets(0));


        txt_nome.setPromptText("Nome Evento");
        txt_nome.setMaxWidth(450);
        txt_nome.setFocusTraversable(true);

        txt_grupo.setPromptText("Grupo Pertencente");
        txt_grupo.setMaxWidth(450);
        txt_grupo.setFocusTraversable(true);

        txt_sala.setPromptText("Nome da Sala");
        txt_sala.setMaxWidth(700);
        txt_sala.setFocusTraversable(true);

        txt_duracao.setPromptText("Lotação");

        txt_horainicio.setPromptText("Hora Início");

        txt_lotacao.setPromptText("Duração");

        boxSub0Event.getChildren().addAll(txt_nome, txt_grupo, txt_horainicio );

        boxSub1Event.getChildren().addAll(txt_sala, txt_lotacao, txt_duracao);

        boxEvent.getChildren().addAll(boxSub0Event, boxSub1Event);
        this.getChildren().addAll(boxEvent);


    }

    public void setupRoomType(){

        HBox boxFiltro1 = new HBox(10);
        boxFiltro1.setAlignment(Pos.TOP_CENTER);
        //boxFiltro1.setPadding(new Insets(10,0,0,0));

        HBox boxFiltro2 = new HBox(10);

        VBox boxSub0Filtro = new VBox(10);
        boxSub0Filtro.setAlignment(Pos.TOP_LEFT);
        boxSub0Filtro.setPadding(new Insets(10,10,0,0));

        VBox boxSub1Filtro = new VBox(10);
        //boxSub1Filtro.setAlignment(Pos.TOP_RIGHT);
        boxSub1Filtro.setPadding(new Insets(10, 0, 0, 50));

        VBox boxSub2Filtro = new VBox(10);
        //boxSub2Filtro.setAlignment(Pos.TOP_RIGHT);
        //boxSub2Filtro.setPadding(new Insets(BAIXO, ESQUERDA, CIMA, DIREITA));
        boxSub2Filtro.setPadding(new Insets(10, 0, 0, 0));

        cb_anfi = new CheckBox("Anfiteatro");
        cb_lab = new CheckBox("Laboratório");
        cb_salareuniao = new CheckBox("Sala Reunião");

        cb2_projetor = new CheckBox("Projetor");
        cb2_mesareuniao = new CheckBox("Mesa Reunião");
        cb2_windows = new CheckBox("Computadores Windows");
        cb2_macos = new CheckBox("Computadores MacOS");
        cb2_quadroiterativo = new CheckBox("Quadro Iterativo");
        cb2_arcondicionado = new CheckBox("Ar Comdicionado");

        boxSub0Filtro.getChildren().addAll(cb_anfi, cb_lab, cb_salareuniao);

        boxSub1Filtro.getChildren().addAll(cb2_projetor, cb2_mesareuniao, cb2_windows );

        boxSub2Filtro.getChildren().addAll(cb2_macos, cb2_quadroiterativo, cb2_arcondicionado );

        boxFiltro2.getChildren().addAll(boxSub1Filtro, boxSub2Filtro);

        boxFiltro1.getChildren().addAll(boxSub0Filtro,boxFiltro2);

        this.getChildren().add(boxFiltro1);
    }

    public void showRooms(){
        //AQUI É SUPOSTO SEREM MOSTRADAS AS SALAS QUE CORRESPONDAM AO QUE FOI FILTRADO PELO UTILIZADOR
        HBox boxRooms = new HBox(10);
        boxRooms.setAlignment(Pos.BASELINE_CENTER);

        this.getChildren().add(boxRooms);

    }

    public void setupButton(){
        HBox boxCreate = new HBox();
        boxCreate.setAlignment(Pos.BASELINE_CENTER);
        boxCreate.setPadding(new Insets(20, 0, 0, 385));
        bt_criaevento = new Button("Criar Evento");

        boxCreate.getChildren().add(bt_criaevento);
        this.getChildren().add(boxCreate);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateCreate());


    }


}


