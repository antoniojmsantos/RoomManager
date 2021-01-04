package client.gui;

import client.gui.auxiliar.Constants;
import client.gui.auxiliar.Images;
import client.logic.ClientObservable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LogInPane extends VBox implements Constants, PropertyChangeListener {

    private ClientObservable observable;

    TextField txt_username;
    PasswordField txt_password;
    Button bt_entrar;

    public LogInPane(ClientObservable observable){
        this.observable = observable;
        this.observable.addPropertyChangeListener(this);



        setupLogo();
        setupAvatar();
        setupLoginInfo();
        setupEntrar();

        propertyChange(null);

    }

    public void resetControls(){
        txt_username.clear();
        txt_password.clear();
    }

    public void setupLogo() {
        ImageView img_logo = new ImageView(Images.getImage(Constants.LOGO));
        img_logo.setFitWidth(400);
        img_logo.setPreserveRatio(true);

        HBox boxLogo = new HBox();
//        boxLogo.setStyle("-fx-background-color: red");
        boxLogo.setAlignment(Pos.CENTER);
        boxLogo.setPadding(new Insets(75,0,75,0));
        boxLogo.getChildren().add(img_logo);

        this.getChildren().add(boxLogo);
    }

    public void setupAvatar() {
        ImageView img_avatar = new ImageView(Images.getImage(Constants.AVATAR));
        img_avatar.setFitHeight(200);
        img_avatar.setFitWidth(200);
        img_avatar.setPreserveRatio(true);

        HBox boxAvatar = new HBox();


        //TODO: APAGAR ISTOOOO!!!!!!!!!
        boxAvatar.setOnMouseClicked(e->{
            txt_username.setText("antonio@isec.docentes.pt");
            txt_password.setText("Pilanocu123");
            if(e.getButton() == MouseButton.SECONDARY){
                txt_username.setText("a21270359@isec.alunos.pt");
                txt_password.setText("Pilanocu123");
            }
        } );
        /////////////////////////////


        boxAvatar.setAlignment(Pos.CENTER);
        boxAvatar.setPadding(new Insets(20));
        boxAvatar.getChildren().add(img_avatar);

        this.getChildren().add(boxAvatar);
    }

    public void setupLoginInfo() {
        VBox boxLogin = new VBox(10);
        boxLogin.setAlignment(Pos.CENTER);
        boxLogin.setPadding(new Insets(10));

        txt_username = new TextField();
        txt_username.setOnKeyPressed(new EnterListener());
        txt_username.setPromptText("Email da Organização");
        txt_username.setMaxWidth(450);
        txt_username.setFocusTraversable(true);

        txt_password = new PasswordField();
        txt_password.setOnKeyPressed(new EnterListener());
        txt_password.setPromptText("Palavra-Passe");
        txt_password.setMaxWidth(450);
        txt_password.setFocusTraversable(true);

        boxLogin.getChildren().addAll(txt_username, txt_password);


        this.getChildren().add(boxLogin);
    }

    public void setupEntrar() {
        HBox boxEntrar = new HBox();
        boxEntrar.setSpacing(125);
        boxEntrar.setAlignment(Pos.CENTER);
        Hyperlink hl_registo = new Hyperlink("Não tem conta? Registe-se aqui");
        hl_registo.setOnAction(e-> observable.setStateRegister());
        hl_registo.setFocusTraversable(false);
        bt_entrar = new Button("Entrar");
        bt_entrar.setPrefWidth(150);

        bt_entrar.setOnAction(e->doLogin());
        boxEntrar.getChildren().addAll(hl_registo, bt_entrar);



        this.getChildren().add(boxEntrar);

    }

    class EnterListener implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent k) {
            if (k.getCode().equals(KeyCode.ENTER))
            {
                doLogin();
            }
        }
    }

    public void doLogin(){
        Alert alert = new Alert( Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText( "Erro ao autenticar!" );

        if(txt_username.getText().trim().equals("") || txt_password.getText().trim().equals("")){
            alert.setContentText( "É obrigatório preencher todos os campos." );
            alert.showAndWait();
        }
        else if(!observable.Authentication(txt_username.getText(), txt_password.getText())){

            alert.setContentText( "Verifique os seus dados e tente novamente" );
            alert.showAndWait();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //ESTE MÉTODO É EXECUTADO SEMPRE QUE É FEITO ALGUM FIRE (HOUVER ALTERACAO)

        resetControls();
//        setVisible(observable.isStateAuthentication());

        if(observable.isStateAuthentication()) {
            setVisible(true);
            bt_entrar.requestFocus();
        }
        else{
            resetControls();
            setVisible(false);
        }

    }
}
