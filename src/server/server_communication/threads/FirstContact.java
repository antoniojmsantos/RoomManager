package server.server_communication.threads;

import server.server_logic.ServerLogic;
import shared_data.communication.requests.RequestFirstContact;
import shared_data.communication.response.ResponseFirstContact;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class FirstContact extends Thread{

    private MulticastSocket listenNewFirstContact;
    private int PORT_MULTICAST;
    private String multicastGroup;
    private DatagramSocket sendResponseSocket;
    private ServerLogic serverLogic;
    private int portTCP;

    public FirstContact(ServerLogic logic,int portTCP) throws IOException {
        this.PORT_MULTICAST = 54321;
        this.multicastGroup = "239.3.2.1";
        this.listenNewFirstContact = new MulticastSocket(PORT_MULTICAST);
        this.listenNewFirstContact.setSoTimeout(2000);
        this.listenNewFirstContact.joinGroup(InetAddress.getByName(multicastGroup));
        this.sendResponseSocket = new DatagramSocket();
        this.serverLogic = logic;
        this.portTCP = portTCP;
    }

    @Override
    public void run() {

        while (KeepAlive.getKeepAlive()){
            try {
                RequestFirstContact requestFirstContact = (RequestFirstContact) SendAndReceiveData.receiveDataUDP(this.listenNewFirstContact);
                ResponseFirstContact responseFirstContact = new ResponseFirstContact(InetAddress.getLocalHost().getHostAddress(), PORT_MULTICAST, InetAddress.getLocalHost().getHostAddress(), portTCP);
                SendAndReceiveData.sendDataUDP(responseFirstContact, sendResponseSocket, InetAddress.getByName(requestFirstContact.getIp()), requestFirstContact.getPort());
                System.out.println(responseFirstContact.getIpServerTCP() + " " + responseFirstContact.getPortTCP());
            } catch (SocketTimeoutException e){
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
