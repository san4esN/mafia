/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Receivers;

import ClientLib.Clients.Client;
import ClientLib.Converters.Converter;
import ClientLib.Service.DataCode;
import ClientLib.Converters.ImageConverter;
import ClientLib.Service.ListenersUpadater;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Константин
 */
public class AudioReceiver extends TCPReceiver
{
    private Converter converter; 
    public AudioReceiver(Socket socket, Client client) throws IOException {
        super(socket, client);
        updater = new ListenersUpadater<byte[]>();
        converter = new Converter();
    }

    @Override
    protected synchronized void ConvertData(byte[] data) throws IOException 
    {
            int id = converter.getIdFromData(data);
            if(data[0] == DataCode.AUDIO.ordinal())
            {
                byte[] audio = converter.getUnmarkedData(data);
                if(audio==null)
                    return;
                updater.update(audio,id);
            }
    }
    
}
