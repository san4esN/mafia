/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Receivers;

import ClientLib.Clients.Client;
import ClientLib.Service.Listener;
import ClientLib.Service.Updater;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxapplication1.AudioReciver;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author Константин
 */
public abstract class TCPReceiver extends Thread
{
    protected Updater updater;
    protected InputStream in;
    Client client;
  
    public TCPReceiver(Socket socket, Client client) throws IOException {
        this.client = client;
            in = socket.getInputStream();
    }
    
    protected abstract void ConvertData(byte[] data) throws IOException,InterruptedException;
    
    @Override
    public synchronized void run()
    {
        try
        { 
            byte[] data = new byte[10000];
            while(true)
            {
                    in.read(data, 0, data.length);
                    if(data[0]!=0)
                    {
                        ConvertData(data); 
                    }
            }
        }
        catch(IOException ex)
        {
            client.disconnect();
        } catch (InterruptedException ex) {
            Logger.getLogger(TCPReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public void addListener(Listener listener)
   {
       updater.add(listener);
   }
   public void deleteListener(Listener listener)
   {
       updater.delete(listener);
   }
}
