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

//        leftBox = new VBox(10);   // 10 é o spacing between objects
//        leftBox.setPrefSize(LEFT_VBOX_X, LEFT_VBOX_Y);
//        leftBox.setMinSize(LEFT_VBOX_X, LEFT_VBOX_Y);
//        leftBox.setPadding(new Insets(10, 10, 10, 10));
//        leftBox.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID,
//                null, new BorderWidths(1))));
//
//        leftBox.getChildren().addAll(navePane);
//
//        rightBox = new VBox(10);
//        rightBox.setPrefSize(LEFT_VBOX_X, LEFT_VBOX_Y);
//        rightBox.setMinSize(LEFT_VBOX_X, LEFT_VBOX_Y);
//        rightBox.setPadding(new Insets(10, 10, 10, 10));
//        rightBox.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID,
//                null, new BorderWidths(1))));
//        rightBox.getChildren().addAll(planetaPane, noPlanetaPane);
//

//        HBox top = new HBox(10);
//        top.setPrefSize(DIM_X_SOUTH_PANEL , DIM_Y_SOUTH_PANEL+13);
//        top.setMinSize(DIM_X_SOUTH_PANEL , DIM_Y_SOUTH_PANEL );
//        top.setPadding(new Insets(1, 1, 1, 1));
//        top.getChildren().addAll(logPane);
//
//
//        HBox center = new HBox();
//        center.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID,
//                null, new BorderWidths(2))));
//        center.setAlignment(Pos.CENTER);
//        center.getChildren().addAll(leftBox, rightBox);
//
//        StackPane bottom = new StackPane(aguardaInicioPane, aguardaMovimentoPane, aplicaEventoPane,
//                emOrbitaStackPane, converterRecursosPane, noPlanetaStackPane, gameOverPane);
//
//        bottom.setPrefSize(DIM_X_SOUTH_PANEL , DIM_Y_SOUTH_PANEL );
//        bottom.setMinSize(DIM_X_SOUTH_PANEL , DIM_Y_SOUTH_PANEL );
//
//        setTop(top);
//        setCenter(center);
//        setBottom(bottom);

        getChildren().add(loginPane);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
