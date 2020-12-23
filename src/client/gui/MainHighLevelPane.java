package client.gui;

import client.logic.ClientObservable;
import client.logic.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import java.time.*;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class MainHighLevelPane extends HBox implements Constants, PropertyChangeListener {
    private ClientObservable observable;

    private ArrayList<GridItemEvent> allCalendarDays = new ArrayList();
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    private VBox box_events;
    private VBox box_calendar;

    public MainHighLevelPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        this.setPadding(new Insets(30));
        setSpacing(30);

        setupEventList();
        setupEventCalendar();

        this.getChildren().addAll(box_events,box_calendar);

        propertyChange(null);
    }

    public void setupEventList(){
        box_events = new VBox(20);

        box_events.setPrefHeight(DIM_Y_FRAME);
        Label lb_user = new Label("Autenticado como: xpto@organizacao.pt");
        lb_user.setFont(Font.font("verdana", FontWeight.BOLD, 10));

        VBox box_list = new VBox();
        box_list.setStyle("-fx-border-color: black");

        AnchorPane header_list = new AnchorPane();
        header_list.setStyle("-fx-background-color: lightgrey;" + "-fx-border-color: black");
//        header_list.setPadding(new Insets(5));
        Label lbl = new Label("Eventos criados");
        lbl.setFont(Font.font("verdana", FontWeight.BOLD, 11));

        Button btn_new = new Button("+");
        AnchorPane.setTopAnchor(lbl, 5.0);
        AnchorPane.setLeftAnchor(lbl, 2.0);
        AnchorPane.setTopAnchor(btn_new, 0.0);
        AnchorPane.setRightAnchor(btn_new, 0.0);

        header_list.getChildren().addAll(lbl, btn_new);

        ListView<String> list_events = new ListView<>();
        list_events.setStyle("-fx-border-color: black");
        list_events.setPrefHeight(DIM_Y_FRAME);
        ObservableList<String> items = FXCollections.observableArrayList (
                "Evento 1", "Evento 2", "Evento 3");
        list_events.setItems(items);

        box_list.getChildren().addAll(header_list, list_events);

        box_events.getChildren().addAll(lb_user, box_list);
    }

    public void setupEventCalendar(){
        box_calendar = new VBox();


        currentYearMonth = YearMonth.now();

        GridPane calendar = new GridPane();
        calendar.setStyle("-fx-background-color: white;" + "-fx-border-color: black");
        calendar.setPrefSize(950, 650);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                GridItemEvent ap = new GridItemEvent();
                ap.setPrefSize(200,200);
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
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 50);
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

        box_calendar.getChildren().addAll(titleBar, dayLabels, calendar);
    }

    public void populateCalendar(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (GridItemEvent ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
//            ap.setTopAnchor(txt, 5.0);
//            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
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


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateMain());
        //TODO: FALTA VERIFICAR TAMBÉM SE O USER TEM PERMISSAO ALTA
    }

}
