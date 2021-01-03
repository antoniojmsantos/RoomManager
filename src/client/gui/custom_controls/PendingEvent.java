package client.gui.custom_controls;

import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import shared_data.entities.Event;


public class PendingEvent extends AnchorPane {

    public PendingEvent(Event event){
        Label lbName = new Label(event.getName());

        Button btAccept = new Button();
        ImageView imgAccept = new ImageView(Images.getImage(Constants.ACCEPT));
        btAccept.setGraphic(imgAccept);
        ImageView imgDecline = new ImageView(Images.getImage(Constants.DECLINE));
        Button btDecline = new Button();
        btDecline.setGraphic(imgDecline);

        HBox boxButtons = new HBox(btAccept, btDecline);

        AnchorPane.setLeftAnchor(lbName, 5.0);
        AnchorPane.setRightAnchor(boxButtons, 5.0);
    }
}
