/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Senders;

import ClientLib.Clients.Client;
import ClientLib.Service.Command;
import ClientLib.Converters.CommandConverter;
import java.io.IOException;
import java.net.Socket;
import ClientLib.Converters.CommandConverter;
/**
 *
 * @author Константин
 */
public class CommandSender extends TCPSender
{
    private CommandConverter converter;
    public CommandSender(Socket socket, Client client) throws IOException {
        super(socket, client);
        converter = new CommandConverter();
    }
    @Override
    public void sendData(Object object) throws IOException {
        Command command = (Command)object;        
        byte[] commandData = converter.convertToMarkedByteArray(command.getId(), command.getCommand());
        sendData(commandData);
    }
    
}
