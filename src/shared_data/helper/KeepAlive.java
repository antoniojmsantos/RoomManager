package shared_data.helper;

public class KeepAlive {

    private static MutableBoolean keepAlive = new MutableBoolean();

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

    public static void emergencyExit(Exception e, String message){
        System.out.println(message);
        System.out.println();
        e.printStackTrace();
        setKeepAlive(false);
    }
}
