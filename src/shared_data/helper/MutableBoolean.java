package shared_data.helper;

/**
 * Classe usada no syncronized já que boolean não é visto como objeto
 */
public class MutableBoolean {

    private boolean keepAlive; // classe para encapsular o boolean como mutavel para manter a referencia igual

    public MutableBoolean(){ //true = alive/false = dead
        keepAlive = true;
    }

    public boolean getKeepALive(){
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive){
        this.keepAlive = keepAlive;
    }
}
