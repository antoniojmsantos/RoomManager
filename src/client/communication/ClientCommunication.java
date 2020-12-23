package client.communication;

import shared_data.communication.request.RequestAuthentication;
import shared_data.communication.request.RequestFirstContact;
import shared_data.communication.response.ResponseAuthentication;
import shared_data.communication.response.ResponseFirstContact;
import shared_data.helper.KeepAlive;
import shared_data.helper.SendAndReceiveData;

import java.io.IOException;
import java.net.*;
import java.rmi.UnexpectedException;

public class ClientCommunication {

    private final int MULTICAST_PORT = 54321;
    private final String MULTICAST_GROUP = "239.3.2.1";

    private Socket socketTCP;

    public void run() {
        try {
            firstContact();
        } catch (SocketException | UnexpectedException e ) {
            KeepAlive.emergencyExit(e, "Falha a criar comunicação");
        } catch (IOException e) {
            KeepAlive.emergencyExit(e, "Falha ao enviar o primeiro contato");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void firstContact() throws IOException, ClassNotFoundException {
        DatagramSocket socketFirstContact = new DatagramSocket();

        // send first contact
        SendAndReceiveData.sendDataUDP(
                new RequestFirstContact(InetAddress.getLocalHost().getHostAddress(), socketFirstContact.getLocalPort()),
                socketFirstContact,
                InetAddress.getByName(MULTICAST_GROUP),
                MULTICAST_PORT
        );

        // get response
        ResponseFirstContact responseFirstContact = (ResponseFirstContact)SendAndReceiveData.receiveDataUDP(socketFirstContact);
        System.out.println(responseFirstContact);

        // create server socket
        socketTCP = new Socket(responseFirstContact.getIpServerTCP(),responseFirstContact.getPortTCP());
    }

    public boolean Authenticate(String username, String password) {
            if (socketTCP != null) {
                try {
                    SendAndReceiveData.sendData(
                            new RequestAuthentication(InetAddress.getLocalHost().getHostAddress(), socketTCP.getLocalPort()),
                            socketTCP
                    );

                    //ResponseAuthentication response = (ResponseAuthentication) SendAndReceiveData.receiveData(socketTCP);

                    //System.out.println("Authentication: "+response.isAuthenticated());

                    //return response.isAuthenticated();
                } catch (IOException /*| ClassNotFoundException*/ e) {
                    e.printStackTrace();
                }
            }
        return false;
    }
}
