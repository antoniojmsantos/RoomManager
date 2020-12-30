package client.gui;

import client.gui.auxiliar.Constants;
import client.gui.custom_controls.DateTimePicker;
import client.logic.ClientObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import shared_data.entities.Room;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class CreateEventPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    VBox boxEventFilters;

    TextField txtEventName, txtGroup, txtRoomName;
    DateTimePicker dtInitialDate;
    Spinner<Integer> spDuration, spCapacity;

    CheckBox cb_anfi, cb_lab, cb_salareuniao;
    CheckBox cb2_projetor, cb2_mesareuniao, cb2_windows, cb2_macos, cb2_quadroiterativo, cb2_arcondicionado;

    ListView<Room> listRooms;
    FilteredList<Room> filteredRoomsList;
    ObservableList<Room> roomsList;


    public CreateEventPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        txtRoomName = new TextField();
        txtEventName= new TextField();
        txtGroup= new TextField();
        dtInitialDate = new DateTimePicker();
        spDuration = new Spinner<>();


        boxEventFilters = new VBox(10);
        boxEventFilters.setPadding(new Insets(30));



        ArrayList<Room> salasTeste = new ArrayList<>();
        salasTeste.add(new Room(1, "L1.1", 10));
        salasTeste.add(new Room(2, "L1.2", 25));
        salasTeste.add(new Room(3, "L2.1", 25));
        salasTeste.add(new Room(3, "L2.3", 25));
        salasTeste.add(new Room(3, "L3.1", 25));
        salasTeste.add(new Room(3, "A4.1", 25));
        roomsList = FXCollections.observableArrayList(salasTeste);

        setupEventFilters();
        setupRooms();
        setupButton();

        this.getChildren().add(boxEventFilters);

       propertyChange(null);

    }


//    public void applyFilterRooms(){
//        //TODO: FUNCAO QUE VAI SER EVOCADA NO INICIO DO PANE (MOSTRA TODAS) E SEMPRE QUE FOR APLICADO UM FILTRO (MOSTRA COM FILTRO)
//
//    }


    public void setupEventFilters(){

        VBox boxAux = new VBox(10); //Auxiliar box because it will have 2 vertical lines of info
//        boxAux.setMinWidth(DIM_X_FRAME);

        HBox boxFilters1 = new HBox(10);

        txtEventName.setPromptText("Nome Evento");
        txtGroup.setPromptText("Grupo Pertencente");
        Button btListGroups = new Button("?");
//        btListGroups.setOnAction(e-> );

        Label lbInitialDate = new Label("Hora Inicio:");
        dtInitialDate.setDateTimeValue(LocalDateTime.now());
        dtInitialDate.editorProperty().get().setAlignment(Pos.CENTER);

        HBox boxAuxDate = new HBox(lbInitialDate, dtInitialDate);
        boxAuxDate.setSpacing(10);
        boxAuxDate.setAlignment(Pos.CENTER);

        boxFilters1.getChildren().addAll(txtEventName, txtGroup, btListGroups, boxAuxDate);

        lbInitialDate.setMinWidth(Region.USE_PREF_SIZE);
        dtInitialDate.prefWidthProperty().bind(boxAuxDate.widthProperty());

        txtEventName.prefWidthProperty().bind(boxFilters1.widthProperty());
        txtGroup.prefWidthProperty().bind(boxFilters1.widthProperty());
        btListGroups.setMinWidth(Region.USE_PREF_SIZE);
        boxAuxDate.prefWidthProperty().bind(boxFilters1.widthProperty());


        HBox boxFilters2 = new HBox(10);


        txtRoomName.setPromptText("Nome da Sala");

        spCapacity = new Spinner<>();

        Label lbCapacity = new Label("Capacidade:");
        spCapacity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,500, 0));
        spCapacity.editorProperty().get().setAlignment(Pos.CENTER);
        HBox boxAuxCapacity = new HBox(lbCapacity, spCapacity);
        boxAuxCapacity.setSpacing(10);
        boxAuxCapacity.setAlignment(Pos.CENTER);

        lbCapacity.setMinWidth(Region.USE_PREF_SIZE);
        spCapacity.prefWidthProperty().bind(boxAuxCapacity.widthProperty());

        Label lbDuration = new Label("Duração (Min):");
        // get a localized format for parsing
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, filter);

        spDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 1000, 1));
        spDuration.setEditable(true);
        spDuration.getEditor().setTextFormatter(priceFormatter);
        spDuration.editorProperty().get().setAlignment(Pos.CENTER);
        HBox boxAuxDuration = new HBox(lbDuration, spDuration);
        boxAuxDuration.setSpacing(10);
        boxAuxDuration.setAlignment(Pos.CENTER);

        lbDuration.setMinWidth(Region.USE_PREF_SIZE);
        spDuration.prefWidthProperty().bind(boxAuxDuration.widthProperty());

        boxFilters2.getChildren().addAll(txtRoomName, boxAuxCapacity, boxAuxDuration);

        txtRoomName.prefWidthProperty().bind(boxFilters2.widthProperty());
        boxAuxCapacity.prefWidthProperty().bind(boxFilters2.widthProperty());
        boxAuxDuration.prefWidthProperty().bind(boxFilters2.widthProperty());

        boxAux.getChildren().addAll(boxFilters1, boxFilters2);

        //MAKE THIS TWO BOX's THE SAME SIZE AS THE PARENT (boxAUX)
//        boxFilters1.prefWidthProperty().bind(boxAux.widthProperty());
        boxFilters2.prefWidthProperty().bind(boxAux.widthProperty());

        boxEventFilters.getChildren().add(boxAux);

        setupRoomType();
    }

    public void setupRoomType(){

        HBox boxAux = new HBox(10);



        TitledPane paneFilterType = new TitledPane();
        paneFilterType.setText("Tipo de Sala");
        paneFilterType.setCollapsible(false);
        VBox boxFilterType = new VBox(10);
        cb_anfi = new CheckBox("Anfiteatro");
        cb_lab = new CheckBox("Laboratório");
        cb_salareuniao = new CheckBox("Sala Reunião");

        boxFilterType.getChildren().addAll(cb_anfi, cb_lab, cb_salareuniao);
        paneFilterType.setContent(boxFilterType);


//        VBox boxFilterDetails = new VBox(10);
        TitledPane paneFilterDetails = new TitledPane();
        paneFilterDetails.setText("Características");
        paneFilterDetails.setCollapsible(false);
        HBox boxAux1 = new HBox(150);
        VBox boxVAux = new VBox(10);
        VBox boxVAux1 = new VBox(10);
        cb2_projetor = new CheckBox("Projetor");
        cb2_mesareuniao = new CheckBox("Mesa Reunião");
        cb2_windows = new CheckBox("Computadores Windows");
        boxVAux.getChildren().addAll(cb2_projetor, cb2_mesareuniao, cb2_windows);
        cb2_macos = new CheckBox("Computadores MacOS");
        cb2_quadroiterativo = new CheckBox("Quadro Iterativo");
        cb2_arcondicionado = new CheckBox("Ar Condicionado");
        boxVAux1.getChildren().addAll(cb2_macos, cb2_quadroiterativo, cb2_arcondicionado);

        boxAux1.getChildren().addAll(boxVAux, boxVAux1);

        paneFilterDetails.setContent(boxAux1);


        boxAux.getChildren().addAll(paneFilterType, paneFilterDetails);

        //MAKE THIS TWO PANELS THE SAME SIZE AS THE PARENT (boxAUX)
        paneFilterDetails.prefWidthProperty().bind(boxAux.widthProperty());
        paneFilterType.prefWidthProperty().bind(boxAux.widthProperty());

        boxEventFilters.getChildren().add(boxAux);

    }

    public void setupRooms(){
//        //AQUI É SUPOSTO SEREM MOSTRADAS AS SALAS QUE CORRESPONDAM AO QUE FOI FILTRADO PELO UTILIZADOR

        TitledPane paneRooms = new TitledPane();
        paneRooms.setText("Salas com estes filtros");
        paneRooms.setCollapsible(false);

        listRooms = new ListView<>();
        filteredRoomsList = new FilteredList<>(roomsList);
        listRooms.setItems(filteredRoomsList);

//        LISTENERS PARA CADA CONTROLO QUE PODE APLICAR FILTRO NA LISTVIEW
        txtRoomName.textProperty().addListener(obs->{
            calculatePredicate();
        });
        spCapacity.valueProperty().addListener(obs->{
            calculatePredicate();
        });
        cb_anfi.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb_lab.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb_salareuniao.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_projetor.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_mesareuniao.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_windows.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_macos.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_quadroiterativo.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cb2_arcondicionado.selectedProperty().addListener(obs->{
            calculatePredicate();
        });


        paneRooms.setContent(listRooms);

        boxEventFilters.getChildren().add(paneRooms);

    }

    private void calculatePredicate() {
        String filterTxt = txtRoomName.getText();
        int filterCap = spCapacity.getValue();
        boolean cbAnfiCap = cb_anfi.isSelected();
        boolean cbLabCap = cb_lab.isSelected();
        boolean cbSalaReuniaoCap = cb_salareuniao.isSelected();
        boolean cbProjetorCap = cb2_projetor.isSelected();
        boolean cbMesaReuniaoCap = cb2_mesareuniao.isSelected();
        boolean cbWindowsCap = cb2_windows.isSelected();
        boolean cbMacOSCap = cb2_macos.isSelected();
        boolean cbQuadroInterativoCap = cb2_quadroiterativo.isSelected();
        boolean cbArCondicionadoCap = cb2_arcondicionado.isSelected();

        filteredRoomsList.setPredicate(s -> {
            boolean availableMatch = observable.isRoomAvailable(s.getId()); //CHECKS AND ONLY SHOWS IF ROOM SCHEDULE IS AVAILABLE
            boolean txtMatch = filterTxt.isEmpty() || s.getName().contains(filterTxt);
            boolean capMatch = filterCap == 0 || s.getCapacity() == filterCap;
//            boolean cbAnfiMatch = cbAnfiCap;
//            boolean cbLabMatch =;
//            boolean cbSalaReuniaoMatch =;
//            boolean cbProjetorMatch =;
//            boolean cbMesaReuniaoMatch =;
//            boolean cbWindowsMatch =;
//            boolean cbMacOSMatch =;
//            boolean cbQuadroInterativoMatch =;
//            boolean cbArCondicionadoMatch =;
            return txtMatch && capMatch;
        });
    }

    public void setupButton(){
        HBox boxCreate = new HBox(10);
        boxCreate.setAlignment(Pos.CENTER_RIGHT);
        Button btCreate = new Button("Criar Evento");
        btCreate.setOnAction(new CreateEventListener());
        Button btCancel = new Button("Cancelar");
        btCancel.setOnAction(e-> observable.setStateMain());

        boxCreate.getChildren().addAll(btCreate, btCancel);

        boxEventFilters.getChildren().add(boxCreate);
    }

    class CreateEventListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

            int idRoom = 0;
//            DateFormat sdf = new SimpleDateFormat("hh:mm");
//            Date date = null;
//            try {
//                date = sdf.parse("01:35");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            assert date != null;
//            Time time = new Time(date.getTime());
//
//            Duration duration;
//            duration = Duration.ofMinutes(spDuration.getValue());

            Alert alert = new Alert(Alert.AlertType.ERROR);

            if(listRooms.getSelectionModel().isEmpty()){
                alert.setTitle("");
                alert.setHeaderText("Ocorreu um erro!");
                alert.setContentText("É necessário selecionar uma sala da lista!");
                alert.showAndWait();
            }
            else if(observable.CreateEvent(idRoom, txtGroup.getText(), txtEventName.getText(),
                    dtInitialDate.getDateTimeValue(), spDuration.getValue())){
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Sucesso!");
                alert.setContentText("Evento '" + txtEventName.getText() + "' criado com sucesso!");
                alert.showAndWait();
            }
            else{

                alert.setTitle("");
                alert.setHeaderText("Ocorreu um erro!");
                alert.setContentText("Não foi possível criar o evento.\nVerifique os dados introduzidos e tente novamente!");
                alert.showAndWait();
            }




        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateCreate());


    }


}


