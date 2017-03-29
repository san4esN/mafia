/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Senders;

import ClientLib.Clients.Client;
import ClientLib.Converters.CommandConverter;
import ClientLib.Converters.Converter;
import ClientLib.Service.DataCode;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Константин
 */
public class AudioSender extends TCPSender {
    private Converter converter;
    public AudioSender(Socket socket, Client client) throws IOException {
        super(socket, client);
        converter = new CommandConverter();
    }

    @Override
    public void sendData(Object object) throws IOException
    {
        byte[] audio = (byte[])object;
        byte data[] = converter.getMarkedData(audio, DataCode.AUDIO,client.getId());
        super.sendData(data);
    }
    
}
