/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Clients;

import ClientLib.Service.Command;
import ClientLib.Senders.CommandSender;
import ClientLib.Receivers.CommandReceiver;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Константин
 */
public class DataClient extends Client{
    
    public DataClient(String ip, int port) {
        init(ip, port);
    }
    private void getConnectionId() throws IOException
    {
        socket= new Socket(ipAdress,TCPport);
        if(socket.isConnected())
        {
            isConnected = true;
            receiver = new CommandReceiver(socket,this);
            id = ((CommandReceiver)receiver).getConnectionId();
        }
        else
            throw new SocketException("Не удалось получить id клиента");
    }
    public void SendCommand(String command, int id) throws IOException
    {
        Command com = new Command(id, command);
        sender.sendData(com);
    }
    
    @Override
    public void connect() throws IOException
    {
       getConnectionId();
       receiver.setName("Ресивер данных");
       receiver.start();
       sender = new CommandSender(socket,this);
    }
}
