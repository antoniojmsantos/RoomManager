package shared_data.helper;

import java.io.*;

/**
 * Classe que possui os métodos de Serializar e deserialiazar objeto
 * Métodos estáticos para não instanciar um objeto deste tipo
 */
public class Serializer {


    public static byte[] objectToByteArray(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        return object;
    }
}
