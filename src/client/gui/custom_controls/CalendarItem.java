package client.gui.custom_controls;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class CalendarItem extends VBox {

    // Date associated with this pane
    private LocalDate date;


    public CalendarItem() {
        this.setPadding(new Insets(5));
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> System.out.println("This pane's date is: " + date));

//        Label lb = new Label("TESTE");
//this.setStyle("-fx-background-color: #FFFFFF");
//        this.getChildren().add(lb);

//        propertyChange(null);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void addEvents(){
        //TODO: FAZER FUNCAO QUE VAI BUSCAR OS EVENTOS NESTA DATA E OS POE DENTRO DE CAIXAS NESTE LAYOUT
        //TODO: NAO ESQUECER DE FAZER A TOOLTIP COM A INFORMACAO DO EVENTO
    }

}