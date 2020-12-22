package client.gui;

import client.logic.ClientObservable;
import client.logic.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Optional;

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
        setupAvatar();
        setupRegisterInfo();

        setupEntrar();

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

    public void setupAvatar() {
        ImageView img_avatar = new ImageView(Images.getImage(Constants.AVATAR));
        img_avatar.setFitHeight(200);
        img_avatar.setFitWidth(200);
        img_avatar.setPreserveRatio(true);

        HBox boxAvatar = new HBox();
        boxAvatar.setAlignment(Pos.CENTER_RIGHT);
       // boxAvatar.setPadding(new Insets(-10));
       // boxAvatar.getSpacing();
        boxAvatar.getChildren().add(img_avatar);

        this.getChildren().add(boxAvatar);
    }

    public void setupRegisterInfo() {

        VBox boxRegister = new VBox(10);
        boxRegister.setAlignment(Pos.TOP_CENTER);
        boxRegister.setPadding(new Insets(0));



        cb_rank.getItems().add("Aluno/Funcionário");
        cb_rank.getItems().add("Docente/Encarregado/Chefe de Equipa");
        cb_rank.setValue("Aluno/Funcionário");


        txt_username.setPromptText("Nome");
        txt_username.setMaxWidth(450);
        txt_username.setFocusTraversable(true);

        txt_email.setPromptText("Email da Organização");
        txt_email.setMaxWidth(450);
        txt_email.setFocusTraversable(true);

        txt_password.setPromptText("Palavra-Passe");
        txt_password.setMaxWidth(450);
        txt_password.setFocusTraversable(true);






        //Aqui também passa a adicionar o email? txt_email
        boxRegister.getChildren().addAll(cb_rank, txt_username, txt_email, txt_password );


        this.getChildren().add(boxRegister);
    }

    public void setupEntrar() {
        HBox boxEntrar = new HBox();
        boxEntrar.setSpacing(-100);
        boxEntrar.setAlignment(Pos.CENTER);

        CheckBox check = new CheckBox("Concordo que os meus dados irão ser submetidos de modo a serem analisados, e o\n administrador proceda à aprovação do meu perfil");
        check.setAlignment(Pos.CENTER_RIGHT);
        boxEntrar.setSpacing(100);

        bt_entrar = new Button("Enviar formulário para aprovação");
        bt_entrar.setPrefWidth(450);


        Hyperlink hl_registo = new Hyperlink("Já tem uma conta? Efetuar Autenticação");
        hl_registo.setOnAction(e-> observable.logout());
        hl_registo.setFocusTraversable(false);

        bt_entrar.setOnAction(new RegisterPane.RegisterListener());

        boxEntrar.getChildren().addAll(hl_registo, bt_entrar);



        this.getChildren().addAll( check, bt_entrar, boxEntrar);

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
