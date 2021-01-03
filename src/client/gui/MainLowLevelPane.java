package client.gui;

import client.gui.auxiliar.Constants;
import client.gui.custom_controls.Calendar;
import client.gui.custom_controls.ListViewPendingEvents;
import client.gui.custom_controls.PendingEvent;
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
import javafx.util.Callback;
import shared_data.entities.Event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

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
        lvPendingEvents.setCellFactory(listView -> new ListViewPendingEvents());
        lvPendingEvents.setStyle("-fx-border-color: black");
        lvPendingEvents.setPrefHeight(DIM_Y_FRAME);


        box_list.getChildren().addAll(header_list, lvPendingEvents);

        boxPendingEvents.getChildren().addAll(lbUser, box_list);

        this.getChildren().addAll(boxPendingEvents);
    }

    public void setupEventCalendar(){
        calendar = new Calendar();
        this.getChildren().addAll(calendar);
    }

    public void populateListView(){
        for(Event e : listPendingEvents){
            PendingEvent boxPE = new PendingEvent(e);
//            lvPendingEvents
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateMain() && !observable.isHighPermission());

        if(observable.isAuthenticated()){
            lbUser.setText("Autenticado como: " + observable.getUsername());
            listEvents = observable.getUserEvents();
            if(!listEvents.isEmpty()){
                calendar.refresh(listEvents);
            }


            listPendingEvents = observable.getPendingEvents();
            if(!listPendingEvents.isEmpty()){
                ObservableList<Event> items = FXCollections.observableArrayList(listPendingEvents);
                lvPendingEvents.setItems(items);
            }

        }
    }

}
