/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Clients;

import ClientLib.Senders.ImageSender;
import ClientLib.Receivers.ImageReceiver;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Константин
 */
public class ImageClient extends Client {
    
    public ImageClient(String ip, int port,int id) {
        init(ip, port,id);
    }

    @Override
    public void connect() throws UnknownHostException, IOException {
        isConnected = true;
        socket= new Socket(ipAdress,TCPport);
        if(socket.isConnected())
        {
            isConnected = true;
            receiver = new ImageReceiver(socket,this);
            receiver.setName("ВидеоРесивер");
            receiver.start();
            sender = new ImageSender(socket,this);
        }
    }
    
}
