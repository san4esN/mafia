/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.Service.Listener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Eduard
 */
public class AudioReciver implements Listener<byte[]> {

    AudioFormat.Encoding encoding =  new AudioFormat.Encoding("PCM_SIGNED");
    AudioFormat format =  new AudioFormat(encoding,((float) 91000.0), 16, 1, 2, ((float) 400000.0) ,false);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    Queue<byte[]> bufQueue = new LinkedList<byte[]>();
    SourceDataLine auline1;
    SourceDataLine auline2;
    SourceDataLine auline3;
    int userId;
    int bufCount=0;
        public AudioReciver(int userId) throws LineUnavailableException
        {
            this.userId = userId;
            auline1 = (SourceDataLine) AudioSystem.getLine(info);
            auline2 = (SourceDataLine) AudioSystem.getLine(info);
            auline3 = (SourceDataLine) AudioSystem.getLine(info);
            auline1.open(format);
            auline2.open(format);
            auline3.open(format);
            auline1.start();
            auline2.start();
            auline3.start();
        }
    @Override
    public  void getObject(byte[] object, int id) {
        if(id != userId)
            {
                Platform.runLater(new Runnable()
                {
                    
                    @Override
                    public void run() {
                        try
                        {
                            if(id==0)
                            {
                                final AtomicReference<byte[]> ref0 = new AtomicReference<>();
                                ref0.set(object);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            auline1.write(ref0.get(), 0, ref0.get().length);
                                    }
                                });
                            }
                            if(id==1)
                            {
                                final AtomicReference<byte[]> ref1 = new AtomicReference<>();
                                ref1.set(object);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            auline2.write(ref1.get(), 0, ref1.get().length);
                                    }
                                });
                        }
                            if(id==2)
                            {
                                final AtomicReference<byte[]> ref2 = new AtomicReference<>();
                                ref2.set(object);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                            auline3.write(ref2.get(), 0, ref2.get().length);
                                    }
                                });
                            }
                        }
                        catch(Exception e)
                        {
                            //System.out.println("");
                        }
                            
                    }
                    
                });    
            }
    }
    
}
