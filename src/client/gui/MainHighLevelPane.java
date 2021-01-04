package client.gui;

import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.gui.custom_controls.Calendar;
import client.logic.ClientObservable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import shared_data.entities.Event;
import shared_data.entities.Room;



public class MainHighLevelPane extends HBox implements Constants, PropertyChangeListener {
    private ClientObservable observable;

    Label lbUser;
    ListView<Event> lvCreatedEvents;

    ArrayList<Event> listEvents;

    Calendar calendar;

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
        header_list.setPadding(new Insets(0,0,5,5));
        Label lbl = new Label("Eventos criados");
        lbl.setFont(Font.font("verdana", FontWeight.BOLD, 11));

        ImageView imgButtonCreate = new ImageView(Images.getImage(Constants.ACCEPT));
        imgButtonCreate.setFitWidth(18);
        imgButtonCreate.setFitHeight(18);
        imgButtonCreate.setOnMouseEntered(e->setCursor(Cursor.HAND));
        imgButtonCreate.setOnMouseExited(e->setCursor(Cursor.DEFAULT));
        imgButtonCreate.setOnMouseClicked(e->observable.setStateCreate());

        AnchorPane.setTopAnchor(lbl, 5.0);
        AnchorPane.setLeftAnchor(lbl, 2.0);
        AnchorPane.setTopAnchor(imgButtonCreate, 5.0);
        AnchorPane.setRightAnchor(imgButtonCreate, 5.0);

        header_list.getChildren().addAll(lbl, imgButtonCreate);



        lvCreatedEvents = new ListView<>();
        lvCreatedEvents.setStyle("-fx-border-color: black");
        lvCreatedEvents.setPrefHeight(DIM_Y_FRAME);

        lvCreatedEvents.setCellFactory(lv -> {
            ListCell<Event> cell = new ListCell<Event>() {
                @Override
                protected void updateItem(Event item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {

                        setText(item.getName());
                    }

                }
            };

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Tem certeza que deseja apagar o evento '"
                        + cell.itemProperty().get().toString() + "' ?\n\nEsta operação não é reversível.", ButtonType.YES, ButtonType.NO);
                alert.setTitle("");
                alert.setHeaderText("Atenção!");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    observable.deleteEvent(lvCreatedEvents.getSelectionModel().getSelectedItem().getId());
                }

            });
            contextMenu.getItems().addAll(deleteItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });


            cell.setOnMouseEntered(e-> lvCreatedEvents.getSelectionModel().select(cell.getIndex()));
            cell.setOnMouseExited(e-> lvCreatedEvents.getSelectionModel().clearSelection());

            return cell ;
        });


        box_list.getChildren().addAll(header_list, lvCreatedEvents);

        box_events.getChildren().addAll(lbUser, box_list);

        this.getChildren().addAll(box_events);
    }

    public void setupEventCalendar(){
        calendar = new Calendar(observable);
        this.getChildren().addAll(calendar);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateMain() && observable.isHighPermission());

        if(observable.isAuthenticated()){
            lbUser.setText("Autenticado como: " + observable.getUsername());
            listEvents = observable.getEventsCreated();
            if(!listEvents.isEmpty()){
//                for(Event e : listEvents)
//                    System.out.println(e);
                calendar.refresh(listEvents);
                ObservableList<Event> items = FXCollections.observableArrayList(listEvents);
//                lvCreatedEvents.getItems().clear();
                lvCreatedEvents.setItems(items);
            }
            else
                lvCreatedEvents.setItems(null);
        }


    }

}
