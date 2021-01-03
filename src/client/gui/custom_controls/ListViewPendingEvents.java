package client.gui.custom_controls;

import javafx.scene.control.ListCell;
import shared_data.entities.Event;

import java.util.ArrayList;

public class ListViewPendingEvents extends ListCell<Event> {

    ArrayList<Event> pendingEvents;

    public ListViewPendingEvents(){
        super();
        this.setMinHeight(100);
    }


//    public void refreshList(ArrayList<Event> pendingEvents){
//        this.pendingEvents = pendingEvents;
//    }

    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);
        if (event != null && !empty) { // <== test for null item and empty parameter
            PendingEvent boxPE = new PendingEvent(event);
            setGraphic(boxPE);
        } else {
            setGraphic(null);
        }
    }
}
