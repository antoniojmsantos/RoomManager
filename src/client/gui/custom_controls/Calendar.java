package client.gui.custom_controls;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class Calendar extends VBox {
    private ArrayList<CalendarItem> allCalendarDays = new ArrayList();
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    public Calendar(){
        currentYearMonth = YearMonth.now();

        GridPane calendar = new GridPane();
        calendar.setStyle("-fx-background-color: white;" + "-fx-border-color: black");
        calendar.setPrefSize(950, 650);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarItem ap = new CalendarItem();
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
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        calendarTitle.setFont(Font.font("verdana", FontWeight.BOLD, 16));
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        AnchorPane titleBar = new AnchorPane(previousMonth, calendarTitle, nextMonth);
        titleBar.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");

        AnchorPane.setLeftAnchor(calendarTitle, 400.0);
        AnchorPane.setTopAnchor(calendarTitle, 5.0);

        AnchorPane.setBottomAnchor(previousMonth, 0.0);
        AnchorPane.setBottomAnchor(nextMonth, 0.0);
        AnchorPane.setRightAnchor(nextMonth, 0.0);

        // Populate calendar with the appropriate day numbers
        populateCalendar(currentYearMonth);
        // Create the calendar view

        this.getChildren().addAll(titleBar, dayLabels, calendar);
    }

    public void populateCalendar(YearMonth yearMonth) {
        LocalDate currentDate = LocalDate.now();

        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);

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
            ap.populateItem(calendarDate, currentDate);
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }


    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }


    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }
}
