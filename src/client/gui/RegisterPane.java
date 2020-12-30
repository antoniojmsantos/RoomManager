package client.gui;

import client.logic.ClientObservable;
import client.logic.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Optional;
import java.util.Stack;

public class RegisterPane extends VBox implements Constants, PropertyChangeListener{

    private ClientObservable observable;

    TextField txt_name;
    TextField txt_email;
    PasswordField txt_password;
    CheckBox cbAccept;
    Button btRegister;

    public RegisterPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        txt_name = new TextField();
        txt_email = new TextField();
        txt_password = new PasswordField();

        setupLogo();
        setupRegisterInfo();
        setupRegister();

        propertyChange(null);

    }

    public void resetControls(){
        txt_name.clear();
        txt_email.clear();
        txt_password.clear();
        cbAccept.setSelected(false);
    }

    public void setupLogo() {
        ImageView img_logo = new ImageView(Images.getImage(Constants.LOGO));
        img_logo.setFitWidth(400);
        img_logo.setPreserveRatio(true);

        HBox boxLogo = new HBox();
//        boxLogo.setStyle("-fx-background-color: red");
        boxLogo.setAlignment(Pos.TOP_CENTER);
        boxLogo.setPadding(new Insets(75,0,75,0));
        boxLogo.getChildren().add(img_logo);

        this.getChildren().add(boxLogo);
    }



    public void setupRegisterInfo() {

        VBox boxRegisterInfo = new VBox(20);
        boxRegisterInfo.setPadding(new Insets(0, 0, 0, 150));
        boxRegisterInfo.setMinWidth(DIM_X_FRAME-500);
        boxRegisterInfo.setAlignment(Pos.CENTER_LEFT);



//        HBox boxType = new HBox(10);
//        boxType.setPadding(new Insets(0,0,60,0));
//        boxType.setAlignment(Pos.CENTER_LEFT);
//
//        Label lblType = new Label("Registar como:");
//        cb_rank.getItems().add("Aluno/Funcionário");
//        cb_rank.getItems().add("Docente/Encarregado/Chefe de Equipa");
//        cb_rank.setValue("Aluno/Funcionário");
//
//        ImageView img_question = new ImageView(Images.getImage(Constants.QUESTION));
//        img_question.setFitHeight(20);
//        img_question.setFitWidth(20);
//        img_question.setPreserveRatio(true);
//
//        Tooltip tooltip = new Tooltip("Este estatuto terá de ser aprovado pelo administrador.");
//        tooltip.setShowDelay(Duration.seconds(0.1));
//        Tooltip.install(img_question, tooltip);
//
//        boxType.getChildren().addAll(lblType, cb_rank, img_question);



        txt_name.setPromptText("Nome");
        txt_email.setPromptText("Email da Organização");
        txt_password.setPromptText("Palavra-Passe");


        boxRegisterInfo.getChildren().addAll(txt_name, txt_email, txt_password );





        VBox boxAvatar = new VBox();

        boxAvatar.setMinWidth(500);
        boxAvatar.setAlignment(Pos.CENTER);
//        boxAvatar.setPadding(new Insets(0,100,0,0));


        ImageView imgAvatar = new ImageView(Images.getImage(Constants.AVATAR));

        imgAvatar.setFitHeight(150);
        imgAvatar.setFitWidth(150);
        imgAvatar.setPreserveRatio(true);

        AnchorPane boxAvatarControls = new AnchorPane();
        boxAvatarControls.setMaxWidth(150);
        boxAvatarControls.setStyle("-fx-background-color: lightgrey");
        ImageView imgUpload = new ImageView(Images.getImage(Constants.UPLOAD));
        imgUpload.setCursor(Cursor.HAND);
        imgUpload.setFitHeight(20);
        imgUpload.setFitWidth(20);
        imgUpload.setPreserveRatio(false);

        ImageView imgDelete = new ImageView(Images.getImage(Constants.DELETE));
        imgDelete.setCursor(Cursor.HAND);
        imgDelete.setFitHeight(20);
        imgDelete.setFitWidth(20);
        imgDelete.setPreserveRatio(true);

        AnchorPane.setRightAnchor(imgDelete, 0.0);

        boxAvatarControls.getChildren().addAll(imgUpload, imgDelete);


        boxAvatar.getChildren().addAll(boxAvatarControls, imgAvatar);



        HBox boxMergeInfoAvatar = new HBox();
        boxMergeInfoAvatar.getChildren().addAll(boxRegisterInfo, boxAvatar);


        this.getChildren().add(boxMergeInfoAvatar);
    }

    public void setupRegister() {
        AnchorPane boxMerge = new AnchorPane();
//        boxMerge.setPadding(new Insets(100,0,0,100));
        VBox boxEntrar = new VBox(15);


        boxEntrar.setPrefWidth(DIM_X_FRAME-600);
//

//        boxEntrar.setAlignment(Pos.CENTER);

       cbAccept = new CheckBox("Concordo que os meus dados irão ser submetidos de modo a serem analisados," +
                " para que o administrador proceda à aprovação do meu estatuto.");
        cbAccept.setWrapText(true);
//        check.setAlignment(Pos.CENTER_RIGHT);

        btRegister = new Button("Efetuar registo");
        btRegister.setMaxWidth(Double.MAX_VALUE);


        Pane boxLogin = new Pane();

        Hyperlink hlLogin = new Hyperlink("Já tem uma conta? Efetuar Autenticação");
        hlLogin.setOnAction(e-> observable.Logout());
        hlLogin.setFocusTraversable(false);
//        setMargin(hl_registo, new Insets(500,0,0,0));
        boxLogin.getChildren().add(hlLogin);

        btRegister.setOnAction(new RegisterPane.RegisterListener());

        boxEntrar.getChildren().addAll(cbAccept, btRegister);

//        boxMerge.setStyle("-fx-background-color: black");

        boxMerge.getChildren().addAll(boxEntrar, boxLogin);

        AnchorPane.setTopAnchor(boxEntrar, 100.0);
        AnchorPane.setLeftAnchor(boxEntrar, 150.0);
        AnchorPane.setBottomAnchor(boxLogin, 0.0);
        AnchorPane.setLeftAnchor(boxLogin, 875.0);


        this.getChildren().addAll(boxMerge);

    }

    class RegisterListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");

            String email = txt_email.getText().trim(); //TEM DE APANHAR AQUI PQ SENAO NAO MOSTRA NO ALERT PQ JA MUDOU DE PANE E DEU RESET.

            if(txt_name.getText().trim().equals("") || txt_name.getText().trim().equals("") || txt_password.getText().trim().equals("")){
                alert.setHeaderText( "Erro ao criar novo registo!" );
                alert.setContentText( "É obrigatório preencher todos os campos." );
                alert.showAndWait();
            }
            else if(!cbAccept.isSelected())
            {
                alert.setHeaderText( "Erro ao criar novo registo!" );
                alert.setContentText( "É obrigatório aceitar as condições de registo." );
                alert.showAndWait();
            }
            else if(!observable.Register(txt_name.getText(), txt_email.getText(), txt_password.getText())){
                alert.setHeaderText( "Erro ao criar novo registo!" );
                alert.setContentText( "Já existe uma conta com este email." );
                alert.showAndWait();
            }
            else{
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText( "Registado com sucesso!" );
                alert.setContentText( "A sua conta '" + email + "' foi criada com sucesso.\n" +
                        "Pode agora efetuar Autenticação." );
                alert.showAndWait();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(observable.isStateRegister())
            setVisible(true);
        else{
            resetControls();
            setVisible(false);
        }
    }
}
