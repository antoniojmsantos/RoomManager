package client.gui.custom_controls;

import client.logic.ClientObservable;
import javafx.scene.control.ListCell;
import shared_data.entities.Event;

/**
 * Classe que é uma celula da lista de eventos pendentes
 * por cada evento pendente cria um PendingEvent e apresenta-o nesta celula
 */
public class ListCellPendingEvent extends ListCell<Event> {

    ClientObservable observable;

    public ListCellPendingEvent(ClientObservable observable){
        super();
        this.observable=observable;
    }


    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);
        if (event != null && !empty) { // <== test for null item and empty parameter
            PendingEvent boxPE = new PendingEvent(observable, event);
            setGraphic(boxPE);
        } else {
            setGraphic(null);
        }
    }
}
