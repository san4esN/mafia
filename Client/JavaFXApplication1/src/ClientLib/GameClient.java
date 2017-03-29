/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib;

import ClientLib.Service.Command;
import ClientLib.Service.Listener;
import ClientLib.Clients.AudioClient;
import ClientLib.Clients.Client;
import ClientLib.Clients.DataClient;
import ClientLib.Clients.ImageClient;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;

/**
 *
 * @author Константин
 */
public class GameClient
{
    private int id;
    private String ip;
    private boolean idReceived;
    private Client dataClient;
    private Client videoClient;
    private Client audioClient;
    public GameClient()
    {
        idReceived = false;
    }
    
    public int connect(String ip) throws IOException, InterruptedException,SocketException
    {
        this.ip = ip;
        if(!idReceived)
            getId();
        audioClient = new AudioClient(ip, 16385,id);
        videoClient = new ImageClient(ip, 16386,id);
        audioClient.connect();
        videoClient.connect();
        return id;
    }
    
     private int getId() throws IOException {
         if(idReceived)
            return id;
         else
         {
             dataClient = new DataClient(ip, 16387);
             dataClient.connect();
             id = dataClient.getId();
             idReceived=true;
             return id;
         }
    }
    
    public void disconnect() throws IOException
    {
       videoClient.disconnect();
       audioClient.disconnect();
       dataClient.disconnect();
    }
    
    public void sendAudio(byte[] audio) throws IOException
    {
        audioClient.getSender().sendData(audio);
    }
     
    public void sendCommand(String command, int id) throws IOException
    {
        Command com = new Command(id, command);
        dataClient.getSender().sendData(com);
    }
    
    public void sendCommand(String command) throws IOException
    {
        Command com = new Command(this.id, command);
        dataClient.getSender().sendData(com);
    }
    
    public void sendImage(BufferedImage image) throws IOException
    {
        videoClient.getSender().sendData(image);
    }
    
    public void addAudioListener(Listener<byte[]> listener)
    {
        audioClient.getReceiver().addListener(listener);
    }
    
    public void addImageListener(Listener<BufferedImage> listener)
    {
        videoClient.getReceiver().addListener(listener);
    }
    
    public void addCommandListener(Listener<String[]> listener)
    {
        dataClient.getReceiver().addListener(listener);
    }
    
   
}
