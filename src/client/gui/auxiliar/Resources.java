package client.gui.auxiliar;

import java.io.InputStream;

/**
 * Classe responsável por obter ficheiros a partir de uma string
 */
public class Resources {

    public static InputStream getResourceFile(String name){
        // Getting named resource from Resources.class location...
        return Resources.class.getResourceAsStream(name);
    }
}
