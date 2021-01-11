package client.gui.custom_controls;


import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.logic.ClientObservable;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import shared_data.entities.Event;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

/**
 * Classe responsável pelo Calendário do painel inicial de cada utilizador.
 * É um controlo que extende da VBox porque vai ser apresentado verticalmente
 * Armazena um vetor com todos os dias do calendario
 * Armazena um vetor de Eventos
 * Para cada evento vai apresenta-lo no calendario do seu dia correspondente
 *
 */
public class Calendar extends VBox {
    private final ArrayList<CalendarItem> allCalendarDays = new ArrayList<>();
    private final Text calendarTitle;
    private YearMonth currentYearMonth;

    final ClientObservable observable;

    ArrayList<Event> events;

    public Calendar(ClientObservable observable){
        this.observable = observable;

        currentYearMonth = YearMonth.now();

        GridPane calendar = new GridPane();
        calendar.setStyle("-fx-background-color: white;" + "-fx-border-color: black");
        calendar.setPrefSize(950, 650);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarItem ap = new CalendarItem(observable);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{ new Text("Domingo"), new Text("Segunda-feira"), new Text("Terça-feira"),
                new Text("Quarta-feira"), new Text("Quinta-feira"), new Text("Sexta-feira"),
                new Text("Sábado") };
        GridPane dayLabels = new GridPane();

        dayLabels.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");
        dayLabels.setPrefWidth(800);
        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            AnchorPane.setBottomAnchor(txt, 3.0);
            AnchorPane.setLeftAnchor(txt, 40.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        calendarTitle.setFont(Font.font("verdana", FontWeight.BOLD, 16));

        ImageView imgButtonPrevious = new ImageView(Images.getImage(Constants.PREVIOUS));
        imgButtonPrevious.setFitWidth(18);
        imgButtonPrevious.setFitHeight(18);
        imgButtonPrevious.setOnMouseEntered(e->setCursor(Cursor.HAND));
        imgButtonPrevious.setOnMouseExited(e->setCursor(Cursor.DEFAULT));
        imgButtonPrevious.setOnMouseClicked(e->previousMonth());

        ImageView imgButtonNext = new ImageView(Images.getImage(Constants.NEXT));
        imgButtonNext.setFitWidth(18);
        imgButtonNext.setFitHeight(18);
        imgButtonNext.setOnMouseEntered(e->setCursor(Cursor.HAND));
        imgButtonNext.setOnMouseExited(e->setCursor(Cursor.DEFAULT));
        imgButtonNext.setOnMouseClicked(e->nextMonth());


        AnchorPane titleBar = new AnchorPane(imgButtonPrevious, calendarTitle, imgButtonNext);
        titleBar.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");

        AnchorPane.setLeftAnchor(calendarTitle, 400.0);
        AnchorPane.setTopAnchor(calendarTitle, 3.0);

        AnchorPane.setLeftAnchor(imgButtonPrevious, 5.0);
        AnchorPane.setBottomAnchor(imgButtonPrevious, 3.0);
        AnchorPane.setBottomAnchor(imgButtonNext, 3.0);
        AnchorPane.setRightAnchor(imgButtonNext, 5.0);

        // Populate calendar with the appropriate day numbers
        populateCalendar();
        // Create the calendar view

        this.getChildren().addAll(titleBar, dayLabels, calendar);
    }

    /**
     * Método responsável por preencher os dias do calendario
     * e por os eventos do utilizador em cada dia
     */
    public void populateCalendar() {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), 1);

        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (CalendarItem ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                for(int i = 0; i < ap.getChildren().size(); i++)
                    ap.getChildren().remove(i);
            }
            ap.refreshEvents(events);
            ap.populateItem(calendarDate);
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(currentYearMonth.getMonth().toString() + " " + String.valueOf(currentYearMonth.getYear()));
    }

    /**
     * Método responsável por atualizar os eventos no calendario
     */
    public void refresh(ArrayList<Event> events){
        this.events = events;
        populateCalendar();
    }

    /**
     * Método responsável por andar para tras 1 mes no calendario
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar();
    }

    /**
     * Método responsável por adiantar 1 mes no calendario
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar();
    }
}
