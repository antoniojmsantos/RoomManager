package client.gui;

import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import shared_data.helper.KeepAlive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMRoot extends VBox implements PropertyChangeListener {

    private final ClientObservable observable;
    private final RMPane RMPane;

    private MenuItem logoutMenuItem;


    public RMRoot(ClientObservable observable) {

        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        RMPane = new RMPane(observable);

        MenuBar menuBar = getMenuBar();

        getChildren().addAll(menuBar, RMPane);

        propertyChange(null);

    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        // game menu
        Menu actionsMenu = new Menu("Ações");

        logoutMenuItem = new MenuItem("Terminar Sessão");
        logoutMenuItem.setOnAction(new LogoutObjMenuBarListener());

        MenuItem exitMenuItem = new MenuItem("Sair");
        exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        exitMenuItem.setOnAction(e-> {
            KeepAlive.setKeepAlive(false);
            Platform.exit();
        });

        actionsMenu.getItems().addAll(logoutMenuItem, new SeparatorMenuItem(), exitMenuItem);

        Menu configMenu = new Menu("Configurações");

        Menu helpMenu = new Menu("Ajuda");

        MenuItem helpContentMenuItem = new MenuItem("Help Contents");
        helpContentMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        helpMenu.getItems().addAll(helpContentMenuItem, aboutMenuItem);

        menuBar.getMenus().addAll(actionsMenu,configMenu, helpMenu);

//        helpContentMenuItem.setOnAction(new HelpContentListener());
//        aboutMenuItem.setOnAction(new AboutListener());

        return menuBar;
    }

    class LogoutObjMenuBarListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent t) {
            observable.Logout();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logoutMenuItem.setDisable((observable.isStateAuthentication() || observable.isStateRegister()));
    }
}
