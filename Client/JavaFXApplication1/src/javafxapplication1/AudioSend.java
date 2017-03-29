/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.Clients.Client;
import javafx.concurrent.Task;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import ClientLib.Clients.Client;
import ClientLib.GameClient;
import ClientLib.Service.Listener;
import com.github.sarxos.webcam.Webcam;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import sun.plugin2.message.Message;

/**
 *
 * @author Eduard
 */
public class AudioSend {
    byte[] abData;
    int CHUNK_SIZE = 8192;
    int waitInt;
    AudioFormat format =  new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"),((float) 91000.0), 16, 1, 2, ((float) 400000.0) ,false);
    TargetDataLine microphone;
    
    
    GameClient client;
    boolean microStop;
    public AudioSend(GameClient client, Mixer mixer) throws LineUnavailableException
    {
        try{
        this.client = client;
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        if(mixer ==null)
            microphone = AudioSystem.getTargetDataLine(format);
        else
            microphone = (TargetDataLine) mixer.getLine(dataLineInfo);
        microphone.open(format);
        microphone.start();
        SourceDataLine auline = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        auline.open(format);
        auline.start();
        abData = new byte[CHUNK_SIZE];
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
    }
    public void setMicroStop(boolean microStop)
    {
        this.microStop = microStop;
    }
    //public void set
    public void setWait(int waitInt)
    {
        this.waitInt = waitInt;
    }
    public void sendAudio()
    {
        microStop = false;
        Task<Void> task = new Task<Void>()
        {
            
            @Override
            protected synchronized Void call() throws Exception {
                
                //!microStop
                while(!microStop)
                {
                    
                        microphone.read(abData, 0, CHUNK_SIZE);
                        //System.out.println(microphone.getLineInfo());
                        client.sendAudio(abData);  
                        wait(5);  
                }
                
                return null;
            }
            
            
        };
        Thread audioThread = new Thread(task);
        audioThread.setDaemon(true);
        audioThread.start();
        
                
    }
}
