package client.logic;

import client.communication.ClientCommunication;

import java.io.IOException;

public class ClientController {

    private final ClientCommunication communication = new ClientCommunication();
    private final State state =  new State(State.TYPE.AUTHENTICATE);

    public void init() {
        communication.run();
    }

    public State getState(){
        return state;
    }



    public boolean isStateRegister(){ return state.isRegister();}

    public boolean isStateAuthentication() {
        return state.isAuthentication();
    }

    public boolean isStateMain() {
        return state.isMain();
    }

    public boolean isStateCreate(){return state.isCreate();}

    public void setStateAuthentication() {
        state.setType(State.TYPE.AUTHENTICATE);
    }

    public void setStateMain() {
        state.setType(State.TYPE.MAIN);
    }

    public void setStateCreate(){
        state.setType(State.TYPE.CREATE_EVENT);
    }

    public void setStateRegister(){
        state.setType(State.TYPE.REGISTER);
    }


    public void Logout(){
        setStateAuthentication();
    }

    public boolean Authentication(String username, String password){
        if(communication.Authenticate(username, password)){
            setStateMain();
            return true;
        }
        else
            return false;
    }

    public boolean Register(String name, String username, String password) {
        if(communication.Register(name, username, password)){
            setStateAuthentication();
            return true;
        }
        else
            return false;
    }


    public String getUsername(){
        return communication.getLoggedUser().getUsername();
    }
}
