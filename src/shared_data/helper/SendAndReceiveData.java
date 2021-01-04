package shared_data.helper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class SendAndReceiveData {

    /*
    Tamanho do Buffer
     */
    public static final  int BUFFER_SIZE = 5000;

    /**
     * Envio de informação por TCP
     * @param sendingObjectTCP objeto a enviar
     * @param socket socket que vai enviar
     * @throws IOException
     */
    public static void sendData(Object sendingObjectTCP, Socket socket) throws IOException {

        byte[] objBytes = Serializer.objectToByteArray(sendingObjectTCP);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        out.writeUnshared(objBytes);
        out.flush();
    }

    /**
     * Recebe informação por TCP
     * @param socket socket que recebe a informação
     * @return o objeto que recebeu
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object receiveData(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        return Serializer.byteArrayToObject((byte[]) in.readObject());
    }

    /**
     * Recebe Um objeto por Multicast
     * Sempre que recebe um packet verifica a checksum para verificar se o apcket evem em condições
     * @param socket scoket multicast que vai receber
     * @return o Objeto recebido
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object receiveDataUDP(MulticastSocket socket) throws IOException, ClassNotFoundException {
        ArrayList<byte[]> infoObject = new ArrayList<>();
        String lastChecksum = "";
        socket.setSoTimeout(3000);



        while(true) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE + 1000],BUFFER_SIZE + 1000);

            try{

                socket.receive(datagramPacket);
                if(datagramPacket.getLength() == 0){
                    break;
                }

                byte[] resultByte = ConstructPacket.verifyingChecksum(datagramPacket);

                DatagramPacket feedbackPacket ;

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
     * Recebe um objeto por UDP
     * Sempre que recebe um packet verifica a checksum para verificar se o apcket evem em condições
     * @param socket socket que vai recever
     * @return o objeto
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object receiveDataUDP(DatagramSocket socket) throws IOException, ClassNotFoundException {
        ArrayList<byte[]> infoObject = new ArrayList<>();
        String lastChecksum = "";
        socket.setSoTimeout(3000);
        while(true) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE + 1000],BUFFER_SIZE + 1000);

            try{
                socket.receive(datagramPacket);
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
     * ENvia informação por UDP de maneira que assegura que chega ao outro lado com sucesso
     * Sempre que envia uma packet recebe um resposta se recebeu bem ou mal através da checksum
     * @param sendingObjectUdp objeto a enviar
     * @param socket socket udp aberto
     * @param ip  ip UDP do server
     * @param port port UDP do server
     */
    public static void sendDataUDP(Object sendingObjectUdp, DatagramSocket socket, InetAddress ip, int port) throws IOException{

        byte[] objectBytes = Serializer.objectToByteArray(sendingObjectUdp); //serializar o objeto

        int sentBytes = 0;
        int countTimeouts = 0;
        //verificar o bytes enviados com os bytes total do objeto
        //Construir o datagramPacket para enviar
        //esperar pela resposta para cada packet enviado para ver se é necessário voltar a reenviar
        while(sentBytes < objectBytes.length){

            int bufferSize = 0;

            if(objectBytes.length - sentBytes < BUFFER_SIZE){ //enviar os restos menos de 5000 bytes
                bufferSize = objectBytes.length - sentBytes;
            }
            else{
                bufferSize = BUFFER_SIZE; //envio de 5000 bytes
            }
            DatagramPacket packet = ConstructPacket.constructDatagramPacket(objectBytes, sentBytes, bufferSize, ip, port);
            socket.send(packet);

            DatagramPacket responsePacket = new DatagramPacket(new byte[1],1);
            try{
                socket.receive(responsePacket);  //timeout
            }catch (SocketTimeoutException exception){
                countTimeouts++;
                if(countTimeouts == 5){
                    throw exception;
                }
                continue;
            }
            if (responsePacket.getData()[0] == (byte)1){//em caso da resposta ser diferente de 1 o sentBytes nao atualizam
                sentBytes = sentBytes + bufferSize;
            }
        }
        socket.send(new DatagramPacket(new byte[0], 0, ip ,port));
    }
}

