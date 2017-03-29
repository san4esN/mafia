/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Converters;

import ClientLib.Service.DataCode;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Константин
 */
public class ImageConverter extends Converter {
    public ImageConverter() {
        code = DataCode.IMAGE;
    }
    private BufferedImage createResizedCopy(java.awt.Image originalImage, 
                    int scaledWidth, int scaledHeight, 
                    boolean preserveAlpha)
        {
            int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
            BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
            Graphics2D g = scaledBI.createGraphics();
            if (preserveAlpha) {
                    g.setComposite(AlphaComposite.Src);
            }
            g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
            g.dispose();
            return scaledBI;
        }

    public byte[] convertToMarkedByteArray(BufferedImage image,int id, int width,int height) throws IOException
    {
        image = createResizedCopy(image, width, height, true); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        return getMarkedData(imageInByte,this.code,id);
    }
    
    private synchronized BufferedImage bytesToImage(byte[] data) throws InterruptedException, IOException
        {
            BufferedImage img=null;
            ByteArrayInputStream a = new ByteArrayInputStream(data);
            img = ImageIO.read(a);
            if (img  ==  null) Thread.sleep((long)25);
            a.close();
            return img;
        }
    
    public BufferedImage convertToImage(byte[] data) throws InterruptedException, IOException
    {
        byte[] imgData = getUnmarkedData(data);
        BufferedImage img = bytesToImage(imgData);
        return img;
    }
}
