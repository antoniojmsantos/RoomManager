package client.gui.auxiliar;

import client.gui.RMRoot;
import client.logic.ClientObservable;
import javafx.scene.layout.Pane;

/**
 * Classe responsável pela criação e armazenanamento do painel mais abaixo da GUI (root)
 */
public class PaneOrganizer {
    private final Pane root;

    public PaneOrganizer(ClientObservable observable) {

        root = new RMRoot(observable);
    }

    public Pane getRoot() {
        return root;
    }
}
