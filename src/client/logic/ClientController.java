package client.logic;

import client.communication.ClientCommunication;

public class ClientController {

    ClientCommunication communication;
    State state;

    public ClientController(ClientCommunication com){
        this.communication = com;
        state = new State(State.TYPE.AUTHENTICATE);

    }

    public State getState(){

        return state;
    }

    public void logout(){
        state.setType(State.TYPE.AUTHENTICATE);
    }

    public boolean Authentication(String usr, String password){
        if(communication.Authenticate(usr, password)){
            state.setType(State.TYPE.MAIN);
            return true;
        }
        else
            return false;
    }

    public boolean isStateRegister(){ return state.isRegister();}

    public boolean isStateAuthentication() {
        return state.isAuthentication();
    }

    public boolean isStateMain() {
        return state.isMain();
    }

    public void setStateRegister(){
        state.setType(State.TYPE.REGISTER);
    }
}
