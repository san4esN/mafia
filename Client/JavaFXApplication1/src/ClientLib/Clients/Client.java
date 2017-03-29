/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Clients;

import ClientLib.Senders.TCPSender;
import ClientLib.Receivers.TCPReceiver;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Константин
 */
public abstract class Client
{
    protected Socket socket;
    protected int TCPport;
    protected String ipAdress;
    protected TCPSender sender;
    protected TCPReceiver receiver;
    protected boolean isConnected;
    protected int id;
    protected void init(String ip, int port)
    {
        this.TCPport = port;
        this.ipAdress = ip;
    }
    protected void init(String ip, int port,int id)
    {
       init(ip,port);
       this.id = id;
    }
    
    public abstract void connect() throws UnknownHostException,IOException;
    
    public int getId()
    {
        return id;
    }
    public void disconnect()
    {
        try {
            socket.close();
            isConnected = false;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean getConnectionStatus()
    {
        return isConnected;
    }
    public TCPSender getSender()
    {   
        if(isConnected == false)
        {
            return null;
        }
        else
        {
            return sender;
        }
    }
    public TCPReceiver getReceiver()
    {   
        if(isConnected == false)
        {
            return null;
        }
        else
        {
            return receiver;
        }
    }
}
