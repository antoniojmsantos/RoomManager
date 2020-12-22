package client.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class GridItemEvent extends VBox {

    // Date associated with this pane
    private LocalDate date;


    public GridItemEvent() {
        this.setPadding(new Insets(5));
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> System.out.println("This pane's date is: " + date));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}