package shared_data.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ConstructPacket {
    //Tabela com valores primos random para calcular a checksum
    private final static int[] hashTable = { 41, 17, 13, 31, 5, 23, 11};
    //Tamanho da tabela de hash
    private final static int HASH_TABLE_SIZE = 7;

    /**
     * Construir o packet com a informação e a checksum
     * calcuar a checksum do packet
     * transformar em array de bytes e ver quantos bytes é constituido
     * fazer uma string com 1 digito para escrever o numero bytes é constituida a checksum
     * o 1 digito é decidido atraves de um calculo
     * transformar esta string que tem o numero de digitos da checksum em byteArray
     * por fim coloar no byteArrayFinal toda a informção a ser enviada pelo packet
     *
     *  checksumSize  |  checksumBytes    |  object
     *       0 0 9    | 1 0 0 0 0 0 0 0 0 | resto do object
     *
     *
     * @param byteArray é a informação
     * @param offset ponto de partida onde vai ser obtida a informção
     * @param length tamanho do buffer size
     * @param ip  ip do server UDP
     * @param port porto do server UDP
     * @return Devolve o packet construido com a info necessária
     * @throws IOException dá throw em caso de erro
     */
    public static DatagramPacket constructDatagramPacket(byte[] byteArray, int offset, int length, InetAddress ip, int port) throws IOException {

        String checksum = calculateChecksum(byteArray,offset,length);
        byte[] checksumBytes = checksum.getBytes(); //tranformar a checksum em array de bytes
        int checksumSize = checksumBytes.length; //buscar o numero de digitos da checksum

        StringBuilder temp = new StringBuilder(3);
        int lengthTamCheckSum = Integer.toString(checksumSize).length();

        for(int i = 0; i < 3 - lengthTamCheckSum;i++){
            temp.append("0");
        }
        temp.append(Integer.toString(checksumSize));

        byte[] checksumSizeBytes = temp.toString().getBytes();

        int totalSize = checksumSizeBytes.length + checksum.getBytes().length + length;

        byte[] finalArray = new byte[totalSize];
        int totalSizeIterator = 0;
        for(int i = 0; i <checksumSizeBytes.length; i++)
            finalArray[totalSizeIterator++] = checksumSizeBytes[i];
        for(int i = 0; i < checksum.getBytes().length; i++)
            finalArray[totalSizeIterator++] = checksumBytes[i];
        for(int i = offset; i <offset + length; i++)
            finalArray[totalSizeIterator++] = byteArray[i];

        return new DatagramPacket(finalArray,totalSize,ip,port);
    }

    /**
     *
     * esta função serve só para calcular um valor "random" para servir de chave para cada packet
     * o calculo pode ser qualquer um, não é preciso necessariamente ser este
     * este é só um exemplo
     * os valores da hashTable são valores random
     * o retorno como string serve para ocupar menos espaço no packet
     *
     *
     * @param byteArray é a informação (objeto a enviar)
     * @param offset  ponto de partida
     * @param length  tamanho do do buffer
     * @return string
     */
    public static String calculateChecksum(byte[] byteArray, int offset, int length){

        long numberOne = 0;
        long numberTwo = 0;

        for(int i = offset, s = 0; i< offset + length; i++, s++){
            if(s == HASH_TABLE_SIZE){ //numeros que estão na tabela
                s = 0;
            }
            numberOne = numberOne + (byteArray[i] * hashTable[s]); //tabela com numero primos random para facilitar os calculos
        }
        for (int i = offset, s = 0; i < offset + length; i++, s++){
            if(s == HASH_TABLE_SIZE){
                s = 0;
            }
            numberTwo = numberTwo + (byteArray[i] * hashTable[s]);
        }

        return numberOne * 3 + Long.toString(numberTwo * 7) + (numberOne + numberTwo);
    }

    /**
     * Inicialmente copiamos toda a informação do packet para um array de byte
     * Logo de seguida vamos buscar os bytes que representam o numero de bytes a ler a seguir para obter checksum
     * Depois vamos buscar os bytes que representam o nosso objeto
     * E por fim calcular sobre esse array a checksum e verificar se dá igual
     * Caso dê igual singifica que chegou igual a enviado
     * Caso dê diferente enviamos null porque chegou com informação defeituosa
     *
     * @param packet que contem a informação enviada
     * @return o array de bytes que representa parte do nosso objeto para o podermos construir de volta
     */
    public static byte[] verifyingChecksum(DatagramPacket packet){

        int index = 0;

        byte[] info = new byte[packet.getLength()];
        for(int i = 0; i < packet.getLength(); i++) //copiar toda a informação do packet
        {
            info[i] = packet.getData()[i];
        }


        byte[] checksumSizeBytes = new byte[3];
        for(; index < 3; index++){
            checksumSizeBytes[index] = info[index];
        }
        String checksumStringSize = new String(checksumSizeBytes,0,checksumSizeBytes.length);
        int checksumSize = Integer.parseInt(checksumStringSize);


        byte[] checksum = new byte[checksumSize];
        for(int i = 0; i < checksumSize;i++,index++){
            checksum[i] = info[index];
        }
        String checksumString = new String(checksum,0,checksum.length);

        int numberOfBytesObject = packet.getLength() - index;
        byte[] bytesOfObject = new byte[numberOfBytesObject];

        for(int i = 0;index < packet.getLength();index++,i++){
            bytesOfObject[i] = info[index];
        }

        String resultChecksum = calculateChecksum(bytesOfObject,0,bytesOfObject.length);
        if(resultChecksum.equals(checksumString)){
            return bytesOfObject;
        }
        else{
            return null;
        }
    }
}
