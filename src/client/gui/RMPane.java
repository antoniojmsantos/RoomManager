package client.gui;

import client.gui.auxiliar.Constants;
import client.logic.ClientObservable;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMPane extends StackPane implements Constants, PropertyChangeListener {
    private final ClientObservable observable;

    private LogInPane loginPane;
    private RegisterPane registerPane;
    private CreateEventPane createEventPane;
    private MainHighLevelPane mainHighLevelPane;
    private MainLowLevelPane mainLowLevelPane;


    public RMPane(ClientObservable observable) {
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        setup();

        propertyChange(null);
    }

    private void setup(){

        setPrefSize(DIM_X_FRAME, DIM_Y_FRAME);
        setMinSize(DIM_X_FRAME, DIM_Y_FRAME);

        loginPane = new LogInPane(observable);
        registerPane = new RegisterPane(observable);
        createEventPane = new CreateEventPane(observable);
        mainHighLevelPane = new MainHighLevelPane(observable);
        mainLowLevelPane = new MainLowLevelPane(observable);

        //CRIA SE O PAINEL E ADICIONA SE SEMPRE AOS FILHOS DO PAINEL "PAI(RMPane)"
        getChildren().addAll(loginPane, registerPane, mainHighLevelPane, mainLowLevelPane, createEventPane);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
