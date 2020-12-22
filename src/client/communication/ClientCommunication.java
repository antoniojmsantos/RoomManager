package client.communication;

import shared_data.communication.requests.RequestFirstContact;
import shared_data.communication.response.ResponseFirstContact;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientCommunication {

    private DatagramSocket socketFirstContact;
    private int PORT_MULTICAST;
    private String multicastGroup;
    private Socket socketTCP;


    public ClientCommunication() throws SocketException {
        this.PORT_MULTICAST = 54321;
        this.multicastGroup = "239.3.2.1";
        this.socketFirstContact = new DatagramSocket();
    }

    public void run() throws IOException, ClassNotFoundException {
        ResponseFirstContact responseFirstContact = firstContact();
        connectToServer(responseFirstContact);
    }

    private void connectToServer(ResponseFirstContact responseFirstContact) throws IOException {
        this.socketTCP = new Socket(responseFirstContact.getIpServerTCP(),responseFirstContact.getPortTCP());
    }

    public ResponseFirstContact firstContact() throws IOException, ClassNotFoundException {
        RequestFirstContact requestFirstContact = new RequestFirstContact(InetAddress.getLocalHost().getHostAddress(),socketFirstContact.getLocalPort());
        SendAndReceiveData.sendDataUDP(requestFirstContact,socketFirstContact,InetAddress.getByName(multicastGroup),PORT_MULTICAST);
        ResponseFirstContact responseFirstContact = (ResponseFirstContact)SendAndReceiveData.receiveDataUDP(socketFirstContact);
        System.out.println(responseFirstContact.getIpServerTCP() + " " + responseFirstContact.getPortTCP());
        return responseFirstContact;
    }

    public boolean Authenticate(String usr, String password){
//        User user(usr, password);
//        SendAndReceiveData.sendData();

        return true;
    }
}
