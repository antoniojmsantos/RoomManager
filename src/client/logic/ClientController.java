package client.logic;

import client.communication.ClientCommunication;

public class ClientController {

    private final ClientCommunication communication = new ClientCommunication();
    private final State state =  new State(State.TYPE.AUTHENTICATE);

    public void init() {
        communication.run();
    }

    public State getState(){
        return state;
    }

    public void logout(){
        state.setType(State.TYPE.AUTHENTICATE);
    }

    public boolean Authentication(String username, String password){
        if(communication.Authenticate(username, password)){
            setStateMain();
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

    public boolean isStateCreate(){return state.isCreate();}

    public void setStateMain() {
        state.setType(State.TYPE.MAIN);
    }

    public void setStateCreate(){
        state.setType(State.TYPE.CREATE_EVENT);
    }

    public void setStateRegister(){
        state.setType(State.TYPE.REGISTER);
    }
}
