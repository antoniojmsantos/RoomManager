package client.gui.custom_controls;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.Room;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CalendarItem extends VBox {

    // Date associated with this pane
    private LocalDate date;

    ArrayList<Event> events = new ArrayList<>();

    public CalendarItem() {
        this.setSpacing(2);
        this.setPrefSize(200,200);
        this.setPadding(new Insets(5));
        // Add action handler for mouse clicked
//        this.setOnMouseClicked(e -> System.out.println("This pane's date is: " + date));

        events.add(new Event(0, "SO_P3_L1.1", new Room(0, "L1.1", 10),
                new Group("P1"), LocalDateTime.now(), 120));
        events.add(new Event(1, "IRC_P1_L2.1", new Room(1, "L2.1", 15),
                new Group("P3"), LocalDateTime.now(), 130));

    }

    void populateItem(LocalDate calendarDate, LocalDate currentDate){
        if (this.getChildren().size() != 0) { // PARA NAO REPETIR CONTROLOS
            this.getChildren().remove(0);
        }
        Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
        if(currentDate.isEqual(calendarDate)) // Changes the color of the text on the current day
            txt.setFill(Color.web("#0093ff"));
        this.setDate(calendarDate);
        this.getChildren().addAll(txt);

        populateEvents(calendarDate);
    }

    public void populateEvents(LocalDate calendarDate){
        //TODO: NAO ESQUECER DE FAZER A TOOLTIP COM A INFORMACAO DO EVENTO

        for(Event e : events){ //FOR EACH OF THE USER EVENTS POPULATE IT ON THE CALENDAR
            LocalDate eventDate = e.getStart().toLocalDate();

            if(calendarDate.isEqual(eventDate)){
                Label lb = new Label(e.getName());
                HBox boxEvent = new HBox(lb);
                boxEvent.setAlignment(Pos.CENTER);
                boxEvent.setBorder(new Border(new BorderStroke(Color.web("#0093ff"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                Tooltip tooltip = new Tooltip("Informações do Evento:\n\nSala: " + e.getRoom().getName() +
                        "\nGrupo: " +e.getGroup().getName() + "\nHora Inicio: " +
                        e.getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\nDuração: " +
                        e.getDuration() + " minutos");
                Tooltip.install(boxEvent, tooltip);

                this.getChildren().add(boxEvent);
            }
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}