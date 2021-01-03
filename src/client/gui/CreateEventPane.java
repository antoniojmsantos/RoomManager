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
import shared_data.entities.RoomFeature;
import shared_data.entities.RoomType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class CreateEventPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    VBox boxEventFilters;

    TextField txtEventName, txtGroup, txtRoomName;
    DateTimePicker dtInitialDate;
    Spinner<Integer> spDuration, spCapacity;

    // Room Types
    CheckBox cbRoomType_Anfiteatro,
            cbRoomType_Lab,
            cbRoomType_SalaReunioes,
            cbRoomType_Auditorio;

    // Room Features
    CheckBox cbRoomFeature_Projetor,
            cbRoomFeature_MesaReuniao,
            cbRoomFeature_ComputadoresWindows,
            cbRoomFeature_ComputadoresMacOS,
            cbRoomFeature_QuadroInterativo,
            cbRoomFeature_ArCondicionado;

    ListView<Room> lvRooms;
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


        ArrayList<Room> salasTeste = observable.getRooms();
        roomsList = FXCollections.observableArrayList(salasTeste);

        setupEventFilters();
        setupRooms();
        setupButton();

        this.getChildren().add(boxEventFilters);

       propertyChange(null);
    }


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
        HBox boxFilterType = new HBox(150);

        // check boxes room types
        cbRoomType_Anfiteatro = new CheckBox(RoomType.ANFITEATRO.getValue());
        cbRoomType_Auditorio = new CheckBox(RoomType.AUDITORIO.getValue());
        cbRoomType_Lab = new CheckBox(RoomType.LABORATORIO.getValue());
        cbRoomType_SalaReunioes = new CheckBox(RoomType.SALA_REUNIOES.getValue());

        VBox boxFilterTypeAux = new VBox(
                cbRoomType_Anfiteatro,
                cbRoomType_Auditorio,
                cbRoomType_Lab);
        boxFilterTypeAux.setSpacing(10);

        VBox boxFilterTypeAux1 = new VBox(
                cbRoomType_SalaReunioes);
        boxFilterTypeAux1.setSpacing(10);

        boxFilterType.getChildren().addAll(
                boxFilterTypeAux,
                boxFilterTypeAux1
        );
        paneFilterType.setContent(boxFilterType);

//        VBox boxFilterDetails = new VBox(10);
        TitledPane paneFilterDetails = new TitledPane();
        paneFilterDetails.setText("Características");
        paneFilterDetails.setCollapsible(false);
        HBox boxAux1 = new HBox(150);
        VBox boxVAux = new VBox(10);
        VBox boxVAux1 = new VBox(10);

        // checkboxes room features v1
        cbRoomFeature_Projetor = new CheckBox(RoomFeature.PROJETOR.getValue());
        cbRoomFeature_MesaReuniao = new CheckBox(RoomFeature.MESA_REUNIAO.getValue());
        cbRoomFeature_ComputadoresWindows = new CheckBox(RoomFeature.COMPUTADORES_WINDOWS.getValue());
        boxVAux.getChildren().addAll(cbRoomFeature_Projetor, cbRoomFeature_MesaReuniao, cbRoomFeature_ComputadoresWindows);

        // checkboxes room features v2
        cbRoomFeature_ComputadoresMacOS = new CheckBox(RoomFeature.COMPUTADORES_MAC.getValue());
        cbRoomFeature_QuadroInterativo = new CheckBox(RoomFeature.QUADRO_INTERATIVO.getValue());
        cbRoomFeature_ArCondicionado = new CheckBox(RoomFeature.AR_CONDICIONADO.getValue());
        boxVAux1.getChildren().addAll(cbRoomFeature_ComputadoresMacOS, cbRoomFeature_QuadroInterativo, cbRoomFeature_ArCondicionado);

        boxAux1.getChildren().addAll(boxVAux, boxVAux1);

        paneFilterDetails.setContent(boxAux1);

        boxAux.getChildren().addAll(paneFilterType, paneFilterDetails);

        //MAKE THIS TWO PANELS THE SAME SIZE AS THE PARENT (boxAUX)
        paneFilterDetails.prefWidthProperty().bind(boxAux.widthProperty());
        paneFilterType.prefWidthProperty().bind(boxAux.widthProperty());

        boxEventFilters.getChildren().add(boxAux);
    }

    public void setupRooms(){
        TitledPane paneRooms = new TitledPane();
        paneRooms.setText("Salas com estes filtros");
        paneRooms.setCollapsible(false);

        lvRooms = new ListView<>();
        filteredRoomsList = new FilteredList<>(roomsList);
        lvRooms.setItems(filteredRoomsList);

        // setup listeners
        registerListeners();

        paneRooms.setContent(lvRooms);
        boxEventFilters.getChildren().add(paneRooms);
    }

    private void registerListeners() {
        txtRoomName.textProperty().addListener(obs->{
            calculatePredicate();
        });
        spCapacity.valueProperty().addListener(obs->{
            calculatePredicate();
        });

        // room type
        cbRoomType_Auditorio.selectedProperty().addListener(obs -> {
            calculatePredicate();
        });
        cbRoomType_Anfiteatro.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomType_Lab.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomType_SalaReunioes.selectedProperty().addListener(obs->{
            calculatePredicate();
        });

        // room features
        cbRoomFeature_Projetor.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomFeature_MesaReuniao.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomFeature_ComputadoresWindows.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomFeature_ComputadoresMacOS.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomFeature_QuadroInterativo.selectedProperty().addListener(obs->{
            calculatePredicate();
        });
        cbRoomFeature_ArCondicionado.selectedProperty().addListener(obs->{
            calculatePredicate();
        });

        // date
        dtInitialDate.valueProperty().addListener(obs->{
            calculatePredicate();
        });
        spDuration.valueProperty().addListener( obs -> {
            calculatePredicate();
        });
    }

    private void calculatePredicate() {
        String filterRoomName = txtRoomName.getText();
        int filterRoomCapacity = spCapacity.getValue();

        Map<RoomType, Boolean> filterRoomType = new HashMap<>(Map.of(
                RoomType.ANFITEATRO, cbRoomType_Anfiteatro.isSelected(),
                RoomType.AUDITORIO, cbRoomType_Auditorio.isSelected(),
                RoomType.LABORATORIO, cbRoomType_Lab.isSelected(),
                RoomType.SALA_REUNIOES, cbRoomType_SalaReunioes.isSelected()
        ));

        Map<RoomFeature, Boolean> filterRoomFeatures = new HashMap<>(Map.of(
                RoomFeature.AR_CONDICIONADO, cbRoomFeature_ArCondicionado.isSelected(),
                RoomFeature.COMPUTADORES_MAC, cbRoomFeature_ComputadoresMacOS.isSelected(),
                RoomFeature.COMPUTADORES_WINDOWS, cbRoomFeature_ComputadoresWindows.isSelected(),
                RoomFeature.PROJETOR, cbRoomFeature_Projetor.isSelected(),
                RoomFeature.QUADRO_INTERATIVO, cbRoomFeature_QuadroInterativo.isSelected(),
                RoomFeature.MESA_REUNIAO, cbRoomFeature_MesaReuniao.isSelected()
        ));

        LocalDateTime startDate = dtInitialDate.getDateTimeValue();
        int duration = spDuration.getValue();

        filteredRoomsList.setPredicate(room -> {
            boolean matchesRoomName = filterRoomName.isEmpty() || room.getName().contains(filterRoomName);
            boolean matchesRoomCapacity = room.getCapacity() >= filterRoomCapacity;

            boolean matchesRoomType = !filterRoomType.containsValue(true);
            for (RoomType rt : filterRoomType.keySet()) {
                if (filterRoomType.get(rt)) {
                    matchesRoomType = matchesRoomType || room.getType().equals(rt);
                }
            }

            boolean matchesRoomFeatures = !filterRoomFeatures.containsValue(true);
            for (RoomFeature rf : filterRoomFeatures.keySet()) {
                if (filterRoomFeatures.get(rf)) {
                    matchesRoomFeatures = matchesRoomFeatures || room.getFeatures().contains(rf);
                }
            }

            return  room.isAvailable(startDate, duration) &&
                    matchesRoomName &&
                    matchesRoomCapacity &&
                    matchesRoomType &&
                    matchesRoomFeatures;
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


            int idRoom = lvRooms.getSelectionModel().getSelectedItem().getId();
            System.out.println(idRoom);

            Alert alert = new Alert(Alert.AlertType.ERROR);

            if(lvRooms.getSelectionModel().isEmpty()){
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


