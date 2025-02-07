package client.gui;

import client.gui.auxiliar.Constants;
import client.gui.custom_controls.Calendar;
import client.gui.custom_controls.ListCellPendingEvent;
import client.logic.ClientObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import shared_data.entities.Event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * O MainLowLevelPane é o painel principal que é apresentado a um Aluno/Funcionário.
 */
public class MainLowLevelPane extends HBox implements Constants, PropertyChangeListener {
    private ClientObservable observable;


    Label lbUser;
    ListView<Event> lvPendingEvents;


    ArrayList<Event> listEvents;
    ArrayList<Event> listPendingEvents;

    Calendar calendar;


    public MainLowLevelPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        lbUser = new Label();

        this.setPadding(new Insets(30));
        setSpacing(30);

        setupPendingEventList();
        setupEventCalendar();


        propertyChange(null);
    }

    public void setupPendingEventList(){
        VBox boxPendingEvents = new VBox(20);

        boxPendingEvents.setPrefHeight(DIM_Y_FRAME);

        lbUser.setFont(Font.font("verdana", FontWeight.BOLD, 10));

        VBox box_list = new VBox();
        box_list.setStyle("-fx-border-color: black");

        AnchorPane header_list = new AnchorPane();
        header_list.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");
        header_list.setPadding(new Insets(0,0,5,5));
        Label lbl = new Label("Eventos pendentes");
        lbl.setFont(Font.font("verdana", FontWeight.BOLD, 11));

        AnchorPane.setTopAnchor(lbl, 5.0);
        AnchorPane.setLeftAnchor(lbl, 2.0);

        header_list.getChildren().addAll(lbl);


        lvPendingEvents = new ListView<>();
        lvPendingEvents.setCellFactory(listView -> {
            // CADA CELULA DA LISTVIEW É UM OBJETO QUE TEM UM LISTENER DE MOUSE ENTER
            ListCellPendingEvent ev = new ListCellPendingEvent(observable);
            ev.setOnMouseEntered(e-> lvPendingEvents.getSelectionModel().select(ev.getIndex()));
            ev.setOnMouseExited(e-> lvPendingEvents.getSelectionModel().clearSelection());
            return ev;
        });
        lvPendingEvents.setStyle("-fx-border-color: black");
        lvPendingEvents.setPrefHeight(DIM_Y_FRAME);
        box_list.getChildren().addAll(header_list, lvPendingEvents);

        boxPendingEvents.getChildren().addAll(lbUser, box_list);

        this.getChildren().addAll(boxPendingEvents);
    }

    public void setupEventCalendar(){
        calendar = new Calendar(observable);
        this.getChildren().addAll(calendar);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /**
         * Apresenta o painel se o estado atual for corresponder a "Main"(Principal)
         * e for um utilizador com premissões baixas.
         * Sempre que acontecer uma alteração na aplicação (propertyChange) vai pedir uma atualização dos eventos à logica.
         */
        setVisible(observable.isStateMain() && !observable.isHighPermission());

        if(observable.isAuthenticated()){

            lbUser.setText("Autenticado como: " + observable.getName());
            listEvents = observable.getUserEvents();


            try{
                calendar.refresh(listEvents);
            } catch (Exception ignored){

            }

            listPendingEvents = observable.getPendingEvents();

            try{
                ObservableList<Event> items = FXCollections.observableArrayList(listPendingEvents);
                lvPendingEvents.setItems(items);
            } catch (Exception e){
                lvPendingEvents.setItems(null);
            }

        }
    }

}
