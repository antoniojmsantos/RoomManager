package client.gui;

import client.logic.ClientObservable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMPane extends BorderPane implements PropertyChangeListener {
    private final ClientObservable observable;


    public RMPane(ClientObservable observable) {
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        propertyChange(null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
