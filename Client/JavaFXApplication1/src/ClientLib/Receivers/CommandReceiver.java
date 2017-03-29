/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Receivers;

import ClientLib.Clients.Client;
import ClientLib.Converters.CommandConverter;
import ClientLib.Service.DataCode;
import ClientLib.Service.ListenersUpadater;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Константин
 */
public class CommandReceiver extends TCPReceiver {
    private CommandConverter converter;
    public CommandReceiver(Socket socket, Client client) throws IOException {
        super(socket, client);
        updater = new ListenersUpadater<String[]>();
        converter = new CommandConverter();
    }

    public int getConnectionId() throws IOException
    {
        byte[] id = new byte[1];
            in.read(id);
            int result = id[0];
            return result;
    }
    
    @Override
    protected void ConvertData(byte[] data) throws IOException {
        if(data[0] == DataCode.DATASTRING.ordinal())
        {
            Object[] dataString = converter.convertToCommand(data);
            int dataId = (int)dataString[0];
            String[] command = new String[2];
            command[0] = (String)dataString[1];
            if(dataString.length==3)
            command[1] = (String)dataString[2];
            updater.update(command, dataId);
       }
    }
}