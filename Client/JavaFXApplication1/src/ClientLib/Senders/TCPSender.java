/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Senders;

import ClientLib.Clients.Client;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Константин
 */
public abstract class TCPSender 
{
    private OutputStream out;
    protected Client client;
    private final Object lock = new Object();
    public TCPSender(Socket socket,Client client) throws IOException
    {
        this.client = client;
        out = socket.getOutputStream();
    }
    
    public abstract void sendData(Object object) throws IOException;

    protected void sendData(byte[] data) throws IOException
    {
        synchronized(lock)
        {
                out.write(data,0,data.length);
        } 
    }
    public void close() throws IOException
    {
            out.close();
    }
    
}
