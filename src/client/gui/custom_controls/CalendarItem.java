package client.gui.custom_controls;

import client.logic.ClientObservable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import shared_data.entities.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarItem extends VBox {

    private final ClientObservable observable;

    // Date associated with this pane
    private LocalDate date;
    ArrayList<Event> events;

    final LocalDate currentDate;

    public CalendarItem(ClientObservable observable) {
        this.observable = observable;

        this.setSpacing(2);
        this.setPrefSize(200,200);
        this.setPadding(new Insets(5));
        currentDate = LocalDate.now();
        // Add action handler for mouse clicked
//        this.setOnMouseClicked(e -> System.out.println("This pane's date is: " + date));

    }

    public void refreshEvents(ArrayList<Event> events){
        this.events = events;
    }

    void populateItem(LocalDate calendarDate){

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

        //TODO PEDE OS EVENTOS AO SERVIDOR

        if(events != null){
            for(Event e : events){ //FOR EACH OF THE USER EVENTS POPULATE IT ON THE CALENDAR
                LocalDate eventDate = e.getStartDate().toLocalDate();

                if(calendarDate.isEqual(eventDate)){
                    Label lb = new Label(e.toString());
                    lb.setTextFill(Color.WHITE);
                    HBox boxEvent = new HBox(lb);
                    boxEvent.setAlignment(Pos.CENTER);
                    boxEvent.setBackground(new Background(new BackgroundFill(Color.web("#0093ff"), new CornerRadii(5), Insets.EMPTY)));

                    if(eventDate.isBefore(currentDate)) // SE A DATA DO EVENTO JÁ PASSOU O DIA DE HOJE A BORDA FICA A CINZA
                        boxEvent.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), Insets.EMPTY)));

                    Tooltip tooltip = new Tooltip("Informações do Evento:\n\n" +
                            "Criado por: " + e.getCreator().getUsername() +
                            "\nSala: " + e.getRoom().getType().getValue() + " " + e.getRoom().getName() +
                            "\nGrupo: " +e.getGroup().getName() + "\nData Início: " +
                            e.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\nData Fim: " +
                            e.getStartDate().plusMinutes(e.getDuration()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                            + "\nDuração: " +e.getDuration()  +" minutos");
                    tooltip.setStyle("-fx-font-size: 14");
                    tooltip.setShowDelay(Duration.millis(200));
                    tooltip.setShowDuration(Duration.INDEFINITE);
                    Tooltip.install(boxEvent, tooltip);

                    if(!observable.isHighPermission()){ //SE FOR ALUNO ADICIONA A OPCAO DE CANCELAR INSCRICAO
                        MenuItem menuItemCancelSubscription = new MenuItem("Cancelar Inscrição");
                        ContextMenu contextMenu = new ContextMenu(menuItemCancelSubscription);

                        boxEvent.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.isSecondaryButtonDown()) {
                                    contextMenu.show(boxEvent, event.getScreenX(), event.getScreenY());
                                }
                            }
                        });
                        menuItemCancelSubscription.setOnAction((event) -> {
                            observable.cancelEvent(e.getId());
                        });
                    }


                    this.getChildren().add(boxEvent);
                }
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