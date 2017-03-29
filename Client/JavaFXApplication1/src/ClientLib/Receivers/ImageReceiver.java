/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Receivers;

import ClientLib.Clients.Client;
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
public class ImageReceiver extends TCPReceiver {
    private ImageConverter converter;
    public ImageReceiver(Socket socket, Client client) throws IOException {
        super(socket, client);
        updater = new ListenersUpadater<BufferedImage>();
        converter = new ImageConverter();
    }

    @Override
    protected void ConvertData(byte[] data) throws IOException, InterruptedException {
        int id=converter.getIdFromData(data);
        if(data[0]==DataCode.IMAGE.ordinal())
        {
            BufferedImage img=null;
            img = converter.convertToImage(data);
            if(img!=null)
            updater.update(img,id);
        }
    }
    
}
