/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Converters;

import ClientLib.Service.DataCode;
import java.io.IOException;

/**
 *
 * @author Константин
 */
public class CommandConverter extends Converter{

    public CommandConverter() {
        code = DataCode.DATASTRING;
    }
    public byte[] convertToMarkedByteArray(int id, String message)
    {
        String mess = id + ";" + message;
        return getMarkedData(mess.getBytes(),this.code,id);
    }
    
    public Object[] convertToCommand(byte[] data) throws IOException
    {
        byte[] messageData = getUnmarkedData(data);
        String str = new String(messageData);
        String [] splitStr =  str.split(";");
        Object[] obj = new Object[3];
        obj[0] = Integer.parseInt(splitStr[0]);
        obj[1] = splitStr[1];
        if(splitStr.length ==3)
            obj[2] = splitStr[2];
        return obj;
    }
}
