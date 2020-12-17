package client.gui;

import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMRoot extends VBox implements PropertyChangeListener {

    private final ClientObservable observable;
    private final RMPane RMPane;

    public RMRoot(ClientObservable observable) {

        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        RMPane = new RMPane(observable);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
