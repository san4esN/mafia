/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Clients;

import ClientLib.Senders.AudioSender;
import ClientLib.Receivers.AudioReceiver;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Константин
 */
public class AudioClient extends Client {
    
    public AudioClient(String ip, int port, int id) {
        init(ip, port,id);
    }
    @Override
    public void connect() throws UnknownHostException,IOException
    {
        isConnected = true;
        socket= new Socket(ipAdress,TCPport);
        if(socket.isConnected())
        {
            isConnected = true;
            receiver = new AudioReceiver(socket,this);
            receiver.setName("АудиоРесивер");
            receiver.start();
            sender = new AudioSender(socket,this);
        }
    }
}
