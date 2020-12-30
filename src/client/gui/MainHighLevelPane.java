package client.gui;

import client.gui.aux.Constants;
import client.gui.custom_controls.Calendar;
import client.logic.ClientObservable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class MainHighLevelPane extends HBox implements Constants, PropertyChangeListener {
    private ClientObservable observable;


    Label lbUser;

    public MainHighLevelPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        lbUser = new Label();

        this.setPadding(new Insets(30));
        setSpacing(30);

        setupEventList();
        setupEventCalendar();


        propertyChange(null);
    }

    public void setupEventList(){
        VBox box_events = new VBox(20);

        box_events.setPrefHeight(DIM_Y_FRAME);

        lbUser.setFont(Font.font("verdana", FontWeight.BOLD, 10));

        VBox box_list = new VBox();
        box_list.setStyle("-fx-border-color: black");

        AnchorPane header_list = new AnchorPane();
        header_list.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");
//        header_list.setPadding(new Insets(5));
        Label lbl = new Label("Eventos criados");
        lbl.setFont(Font.font("verdana", FontWeight.BOLD, 11));

        Button btn_new = new Button("+");
        btn_new.setOnAction(e-> observable.setStateCreate());
        AnchorPane.setTopAnchor(lbl, 5.0);
        AnchorPane.setLeftAnchor(lbl, 2.0);
        AnchorPane.setTopAnchor(btn_new, 0.0);
        AnchorPane.setRightAnchor(btn_new, 0.0);

        header_list.getChildren().addAll(lbl, btn_new);

        ListView<String> list_events = new ListView<>();
        list_events.setStyle("-fx-border-color: black");
        list_events.setPrefHeight(DIM_Y_FRAME);
        ObservableList<String> items = FXCollections.observableArrayList (
                "Evento 1", "Evento 2", "Evento 3");
        list_events.setItems(items);

        list_events.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to edit item...
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> list_events.getItems().remove(cell.getItem()));
            contextMenu.getItems().addAll(editItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });

        box_list.getChildren().addAll(header_list, list_events);

        box_events.getChildren().addAll(lbUser, box_list);

        this.getChildren().addAll(box_events);
    }

    public void setupEventCalendar(){
        Calendar calendar = new Calendar();

        this.getChildren().addAll(calendar);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateMain());

        if(observable.isAuthenticated())
            lbUser.setText("Autenticado como: " + observable.getUsername());

//        setVisible(observable.isStateMain() && observable.isHighPermission());
        //TODO: FALTA VERIFICAR TAMBÉM SE O USER TEM PERMISSAO ALTA
    }

}
