package client.gui;

import client.logic.ClientObservable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RMPane extends StackPane implements Constants, PropertyChangeListener {
    private final ClientObservable observable;

    private LogInPane loginPane;


    public RMPane(ClientObservable observable) {
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        setupComponents();
        setupLayout();

        propertyChange(null);
    }

    private void setupComponents(){

        setPrefSize(DIM_X_FRAME, DIM_Y_FRAME);
        setMinSize(DIM_X_FRAME, DIM_Y_FRAME);

        loginPane = new LogInPane(observable);

    }

    private void setupLayout(){
        //CRIA SE O PAINEL E ADICIONA SE SEMPRE AOS FILHOS DO PAINEL "PAI(RMPane)"
        getChildren().add(loginPane);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //AQUI VAI TER DE SAR VISIBLE TRUE AO PAINEL QUE CORRESPONDER AO ESTADO DO PROG QUE ESTIVER ATUALMENTE
    }
}
