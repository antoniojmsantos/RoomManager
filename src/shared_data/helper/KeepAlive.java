package shared_data.helper;

/**
 * Classe que representa se certo processo ainda está vivo
 * Necessário por causa do syncronized que só aceita objetos
 * Objeto estático
 */
public class KeepAlive {

    // Uma classe que contem um boolean já que boolean não é considerado um objeto
    private static MutableBoolean keepAlive = new MutableBoolean();

    //vai buscar o valor do boolean
    public static boolean getKeepAlive() {
        boolean temp;
        synchronized(KeepAlive.keepAlive){
            temp = keepAlive.getKeepALive();
        }
        return temp;
    }

    public static void setKeepAlive(boolean keepAlive) {
        synchronized(KeepAlive.keepAlive){
            KeepAlive.keepAlive.setKeepAlive(keepAlive); //necessita do 1º KeepAlive porque se nao confunde com a variavel
        }
    }

    /**
     * FUnção usada para saídas de emergencia, quando não é possivel
     * continuar com o processo
     * @param e exceção que foi apanhada e que não permite avançar à frente
     * @param message descrição do probblema
     */
    public static void emergencyExit(Exception e, String message){
        System.out.println(message);
        System.out.println();
        e.printStackTrace();
        setKeepAlive(false);
    }
}
