/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Converters;

import ClientLib.Service.DataCode;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Константин
 */
public class Converter {
    protected DataCode code;

    public byte[] getMarkedData(byte[] data,DataCode code,int id)
    {
        byte[] markedData = new byte[data.length +6];
        markedData[0] = (byte) code.ordinal();
        markedData[1] = (byte) id;
        int length = data.length;
        byte[] bytes = ByteBuffer.allocate(4).putInt(length).array();
        System.arraycopy(bytes, 0, markedData, 2, bytes.length);
        System.arraycopy(data, 0, markedData, bytes.length+2, data.length);
        return markedData;
    }
    public int getIdFromData(byte[] data)
    {
        int id = data[1];
        return id;
    }
    public byte[] getUnmarkedData(byte[] data) throws IOException
    {
        int result=0;
            byte[] length =new byte[4];
            System.arraycopy(data, 2, length, 0, 4);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(length));
            result = dis.readInt();
            if(result>10000)
                throw new IllegalArgumentException("Пакет данных поврежден");
            byte[] unmarkedData = new byte[result];
            System.arraycopy(data, 6, unmarkedData, 0, result);
            return unmarkedData;
    }
    public DataCode getDataCode()
    {
        return code;
    }
}
