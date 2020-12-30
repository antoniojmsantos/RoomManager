package client.gui;

import client.logic.ClientObservable;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private final Pane root;

    public PaneOrganizer(ClientObservable observable) {

        root = new RMRoot(observable);
    }

    public Pane getRoot() {
        return root;
    }
}
