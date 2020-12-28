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

    TextField txt_username;
    TextField txt_email;
    PasswordField txt_password;
    ChoiceBox<String> cb_rank;
    Button bt_entrar;

    public RegisterPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);

        txt_username = new TextField();
        txt_email = new TextField();
        txt_password = new PasswordField();
        cb_rank = new ChoiceBox<>();


        setupLogo();
        setupRegisterInfo();
        setupRegister();

        propertyChange(null);

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



        HBox boxType = new HBox(10);
        boxType.setPadding(new Insets(0,0,60,0));
        boxType.setAlignment(Pos.CENTER_LEFT);

        Label lblType = new Label("Registar como:");
        cb_rank.getItems().add("Aluno/Funcionário");
        cb_rank.getItems().add("Docente/Encarregado/Chefe de Equipa");
        cb_rank.setValue("Aluno/Funcionário");

        ImageView img_question = new ImageView(Images.getImage(Constants.QUESTION));
        img_question.setFitHeight(20);
        img_question.setFitWidth(20);
        img_question.setPreserveRatio(true);

        Tooltip tooltip = new Tooltip("Este estatuto terá de ser aprovado pelo administrador.");
        tooltip.setShowDelay(Duration.seconds(0.1));
        Tooltip.install(img_question, tooltip);

        boxType.getChildren().addAll(lblType, cb_rank, img_question);



        txt_username.setPromptText("Nome");
        txt_email.setPromptText("Email da Organização");
        txt_password.setPromptText("Palavra-Passe");


        boxRegisterInfo.getChildren().addAll(boxType, txt_username, txt_email, txt_password );





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

        CheckBox check = new CheckBox("Concordo que os meus dados irão ser submetidos de modo a serem analisados," +
                " para que o administrador proceda à aprovação do meu estatuto.");
        check.setWrapText(true);
//        check.setAlignment(Pos.CENTER_RIGHT);

        bt_entrar = new Button("Efetuar registo");
        bt_entrar.setMaxWidth(Double.MAX_VALUE);


        Pane boxLogin = new Pane();

        Hyperlink hlLogin = new Hyperlink("Já tem uma conta? Efetuar Autenticação");
        hlLogin.setOnAction(e-> observable.Logout());
        hlLogin.setFocusTraversable(false);
//        setMargin(hl_registo, new Insets(500,0,0,0));
        boxLogin.getChildren().add(hlLogin);

        bt_entrar.setOnAction(new RegisterPane.RegisterListener());

        boxEntrar.getChildren().addAll(check, bt_entrar);

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
            //AQUI VAI DIZER AO OBSERVAVEL QUE QUER FAZER register E MANDAR LHE AS
            //CREDENCIAIS QUE OBTEVE, para validar que não existem outra iguais e senão existirem, guardar na base de dados


           /* if(!observable.Authentication(txt_username.getText(), txt_password.getText())){
                Alert alert = new Alert( Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText( "Erro ao autenticar!" );
                alert.setContentText( "Verifique os seus dados e tente novamente" );

                Timeline idlestage = new Timeline( new KeyFrame( Duration.seconds(3), new EventHandler<ActionEvent>()
                {

                    @Override
                    public void handle( ActionEvent event )
                    {
                        alert.setResult(ButtonType.CANCEL);
                        alert.hide();
                    }
                } ) );
                idlestage.setCycleCount( 1 );
                idlestage.play();

                alert.showAndWait();
            }*/

        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setVisible(observable.isStateRegister());
    }
}
