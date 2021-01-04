package client.gui.custom_controls;

import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.logic.ClientObservable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import shared_data.entities.Event;


public class PendingEvent extends AnchorPane {

    public PendingEvent(ClientObservable observable, Event event){
        Label lbName = new Label(event.getName());

        ImageView imgAccept = new ImageView(Images.getImage(Constants.ACCEPT));
        imgAccept.setFitWidth(17);
        imgAccept.setFitHeight(17);
        imgAccept.setOnMouseEntered(e->setCursor(Cursor.HAND));
        imgAccept.setOnMouseExited(e->setCursor(Cursor.DEFAULT));
        imgAccept.setOnMouseClicked(e->observable.acceptEvent(event.getId()));

        ImageView imgDecline = new ImageView(Images.getImage(Constants.DECLINE));
        imgDecline.setFitWidth(17);
        imgDecline.setFitHeight(17);
        imgDecline.setOnMouseEntered(e->setCursor(Cursor.HAND));
        imgDecline.setOnMouseExited(e->setCursor(Cursor.DEFAULT));
        imgDecline.setOnMouseClicked(e->observable.declineEvent(event.getId()));

        HBox boxButtons = new HBox(5);
        boxButtons.getChildren().addAll(imgAccept, imgDecline);


        AnchorPane.setTopAnchor(lbName, 0.0);
        AnchorPane.setLeftAnchor(lbName, 5.0);
        AnchorPane.setTopAnchor(boxButtons, 0.0);
        AnchorPane.setRightAnchor(boxButtons, 5.0);

        this.getChildren().addAll(lbName, boxButtons);
    }
}
