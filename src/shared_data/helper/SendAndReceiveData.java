package shared_data.helper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class SendAndReceiveData {

    public static final  int BUFFER_SIZE = 5000;

    public static void sendData(Object sendingObjectTCP, Socket socket) throws IOException {

        byte[] objBytes = Serializer.objectToByteArray(sendingObjectTCP);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        out.writeUnshared(objBytes);
        out.flush();
    }

    public static Object receiveData(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        return Serializer.byteArrayToObject((byte[]) in.readObject());
    }

    public static Object receiveDataUDP(MulticastSocket socket) throws IOException, ClassNotFoundException {
        ArrayList<byte[]> infoObject = new ArrayList<>();
        String lastChecksum = "";
        socket.setSoTimeout(3000);



        while(true) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE + 1000],BUFFER_SIZE + 1000);

            try{

                socket.receive(datagramPacket);
                System.out.println("Received datagram with length: " + datagramPacket.getLength());
                if(datagramPacket.getLength() == 0){
                    break;
                }

                byte[] resultByte = ConstructPacket.verifyingChecksum(datagramPacket);

                DatagramPacket feedbackPacket ;

                if(resultByte != null) {
                    String checksum = ConstructPacket.calculateChecksum(resultByte,0,resultByte.length);
                    System.out.println(checksum);
                    System.out.println(lastChecksum);
                    if(!lastChecksum.equals(checksum)){
                        infoObject.add(resultByte);
                        lastChecksum = checksum;
                        byte[] feedBack = new byte[1];
                        feedBack[0] = (byte)1;

                        feedbackPacket = new DatagramPacket(feedBack,feedBack.length,datagramPacket.getAddress(),datagramPacket.getPort());
                        System.out.println("Recebi bem");

                        socket.send(feedbackPacket);
                    }


                }
                else {
                    byte[] feedBack = new byte[1];
                    feedBack[0] = (byte)0;

                    feedbackPacket = new DatagramPacket(feedBack,feedBack.length,datagramPacket.getAddress(),datagramPacket.getPort());
                    System.out.println("Recebi mal");

                    socket.send(feedbackPacket);
                }

            }catch (SocketTimeoutException e){
                break;
            }
        }

        int tam = 0;
        for (byte[] a: infoObject) {
            tam = tam + a.length;
        }
        byte[] finalObjectBytes = new byte[tam];

        int index = 0;
        for (byte[] a : infoObject) {
            for (byte b: a) {
                finalObjectBytes[index] = b;
                index++;
            }
        }
        if(tam == 0){
            return null;
        }else{
            return Serializer.byteArrayToObject(finalObjectBytes);
        }
    }

    public static Object receiveDataUDP(DatagramSocket socket) throws IOException, ClassNotFoundException {
        ArrayList<byte[]> infoObject = new ArrayList<>();
        String lastChecksum = "";
        socket.setSoTimeout(3000);
        while(true) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE + 1000],BUFFER_SIZE + 1000);

            try{
                socket.receive(datagramPacket);
                System.out.print("Recebi : " + datagramPacket.getLength());
                if(datagramPacket.getLength() == 0)
                    break;
                byte[] resultByte = ConstructPacket.verifyingChecksum(datagramPacket);

                DatagramPacket feedbackPacket;

                if(resultByte != null) {
                    String checksum = ConstructPacket.calculateChecksum(resultByte,0,resultByte.length);
                    if(!lastChecksum.equals(checksum)){
                        infoObject.add(resultByte);
                        lastChecksum = checksum;

                        byte[] feedBack = new byte[1];
                        feedBack[0] = (byte)1;

                        feedbackPacket = new DatagramPacket(feedBack,feedBack.length,datagramPacket.getAddress(),datagramPacket.getPort());

                        socket.send(feedbackPacket);
                    }


                }
                else {
                    byte[] feedBack = new byte[1];
                    feedBack[0] = (byte)0;

                    feedbackPacket = new DatagramPacket(feedBack,feedBack.length,datagramPacket.getAddress(),datagramPacket.getPort());

                    socket.send(feedbackPacket);
                }

            }catch (SocketTimeoutException e){
                break;
            }
        }

        int tam = 0;
        for (byte[] a: infoObject) {
            tam = tam + a.length;
        }
        byte[] finalObjectBytes = new byte[tam];

        int index = 0;
        for (byte[] a : infoObject) {
            for (byte b: a) {
                finalObjectBytes[index] = b;
                index++;
            }
        }
        if(tam == 0){
            return null;
        }else{
            return Serializer.byteArrayToObject(finalObjectBytes);
        }
    }
    /**
     *
     * @param sendingObjectUdp objeto a enviar
     * @param socket socket udp aberto
     * @param ip  ip UDP do server
     * @param port port UDP do server
     */
    public static void sendDataUDP(Object sendingObjectUdp, DatagramSocket socket, InetAddress ip, int port) throws IOException{

        byte[] objectBytes = Serializer.objectToByteArray(sendingObjectUdp); //serializar o objeto
//        System.out.print(socket.getInetAddress().getLocalHost().getHostAddress() + ":" +socket.getLocalPort());

        int sentBytes = 0;
        int countTimeouts = 0;
        //verificar o bytes enviados com os bytes total do objeto
        //Construir o datagramPacket para enviar
        //esperar pela resposta para cada packet enviado para ver se é necessário voltar a reenviar
        while(sentBytes < objectBytes.length){
            System.out.print("Entrei AQUI");

            int bufferSize = 0;

            if(objectBytes.length - sentBytes < BUFFER_SIZE){ //enviar os restos menos de 5000 bytes
                bufferSize = objectBytes.length - sentBytes;
            }
            else{
                bufferSize = BUFFER_SIZE; //envio de 5000 bytes
            }
            DatagramPacket packet = ConstructPacket.constructDatagramPacket(objectBytes, sentBytes, bufferSize, ip, port);

            socket.send(packet);
            System.out.println("Enviei um pacote com length: " +packet.getLength());
            DatagramPacket responsePacket = new DatagramPacket(new byte[1],1);


            try{
                socket.receive(responsePacket);  //timeout
                System.out.println("Resposta recebida :" +responsePacket.getLength());
            }catch (SocketTimeoutException exception){
                countTimeouts++;
                if(countTimeouts == 5){
                    throw exception;
                }
                continue;
            }
            if (responsePacket.getData()[0] == (byte)1){//em caso da resposta ser diferente de 1 o sentBytes nao atualizam
                sentBytes = sentBytes + bufferSize;

                System.out.println("O server recebeu bem: " + sentBytes + " de " + objectBytes.length);
            }
        }
        socket.send(new DatagramPacket(new byte[0], 0, ip ,port));
        System.out.println("Pacote enviado");

    }
}

