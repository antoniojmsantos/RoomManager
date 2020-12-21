package client.gui;

import client.logic.ClientController;
import client.logic.ClientObservable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMRoot extends VBox implements PropertyChangeListener {

    private final ClientObservable observable;
    private final RMPane RMPane;

    private MenuItem newMenuItem;

    public RMRoot(ClientObservable observable) {

        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        RMPane = new RMPane(observable);

        MenuBar menuBar = getMenuBar();

        getChildren().addAll(menuBar, RMPane);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        // game menu
        Menu gameMenu = new Menu("_File");

        newMenuItem = new MenuItem("Novo");
        newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        MenuItem readMenuItem = new MenuItem("_Carregar");
        readMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));

        MenuItem saveMenuItem = new MenuItem("_Guardar");
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        MenuItem exitMenuItem = new MenuItem("E_xit");
        exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        gameMenu.getItems().addAll(newMenuItem, readMenuItem, saveMenuItem,new SeparatorMenuItem(), exitMenuItem);
//
//        newMenuItem.setOnAction(new NewObjMenuBarListener());
//        readMenuItem.setOnAction(new LoadObjMenuBarListener());
//        saveMenuItem.setOnAction(new SaveObjMenuBarListener());
//        exitMenuItem.setOnAction(new ExitListener());

        // help menu
        Menu helpMenu = new Menu("_Help");

        MenuItem helpContentMenuItem = new MenuItem("Help Contents");
        helpContentMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        helpMenu.getItems().addAll(helpContentMenuItem, aboutMenuItem);

        menuBar.getMenus().addAll(gameMenu,helpMenu);

//        helpContentMenuItem.setOnAction(new HelpContentListener());
//        aboutMenuItem.setOnAction(new AboutListener());

        return menuBar;
    }
}
