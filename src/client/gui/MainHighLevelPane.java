package client.gui;

import client.logic.ClientObservable;
import client.logic.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainHighLevelPane extends HBox implements Constants, PropertyChangeListener {
    private ClientObservable observable;

    public MainHighLevelPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        VBox box_events = new VBox(20);
        box_events.setPadding(new Insets(30));

        Label lb_user = new Label("Autenticado como: xpto@organizacao.pt");

        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (
                "Evento 1", "Evento 2", "Evento 3");
        list.setItems(items);

        box_events.getChildren().addAll(lb_user, list);

        this.getChildren().addAll(box_events);

        propertyChange(null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateMain());
    }

}
