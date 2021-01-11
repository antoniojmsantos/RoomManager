package client.logic;

import java.io.Serializable;

/**
 * Classe responsável por armazenar todos os estados possíveis da aplicação.
 * O controlador manipula os estados a partir do que vai decorrendo na interação do utilizador.
 */
public class State implements Serializable {

    private static final long serialVersionUID = 42L;

    public enum TYPE {
        AUTHENTICATE,
        REGISTER,
        MAIN,
        CREATE_EVENT
    }

    private TYPE type;

    public State(TYPE type) {
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type){ this.type = type; }

    public boolean isAuthentication(){
        return type == TYPE.AUTHENTICATE;
    }

    public boolean isRegister(){
        return type == TYPE.REGISTER;
    }

    public boolean isCreate(){
        return type == TYPE.CREATE_EVENT;
    }


    public boolean isMain(){
        return type == TYPE.MAIN;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
