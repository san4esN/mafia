/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Senders;

import ClientLib.Clients.Client;
import ClientLib.Converters.ImageConverter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Константин
 */
public class ImageSender extends TCPSender{
    private ImageConverter converter;
    public ImageSender(Socket socket, Client client) throws IOException {
        super(socket, client);
        converter = new ImageConverter();
    }

    @Override
    public void sendData(Object object) throws IOException {
        byte[] data = converter.convertToMarkedByteArray((BufferedImage)object, client.getId(), 100, 100);
        sendData(data);
    }
    
}
