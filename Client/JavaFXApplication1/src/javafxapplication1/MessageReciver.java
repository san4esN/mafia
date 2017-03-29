/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.Service.Listener;
import java.awt.Font;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 *
 * @author Eduard
 */
public class MessageReciver implements Listener<String> {
    
    private ScrollPane scrlPane;
    private VBox box;
    private double heightNeeded=0;
    private int nextYLayout=0;
    private int messageCount;
    public MessageReciver(ScrollPane scrollPane, VBox Vbox)
    {
        this.scrlPane = scrollPane;
        this.box = Vbox;
    }
    
    @Override
    public void getObject(String object, int id) 
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {   
                scrlPane.vvalueProperty().setValue(1.0);
                TextArea tArea = new TextArea();
                tArea.setWrapText(true);
                tArea.setFont(javafx.scene.text.Font.font(Font.MONOSPACED));
                double width =(int) box.getWidth()/8;
                String str = object;
                int length = str.length();
                if(width>length)
                    heightNeeded = 35;
                else
                    heightNeeded = 36+(length/width)*16.1428571;
                tArea.setMinHeight(heightNeeded);
                tArea.setPrefHeight(heightNeeded);
                tArea.setMaxHeight(heightNeeded);
                tArea.appendText(str);
                tArea.setLayoutY(nextYLayout);
                nextYLayout += (int) (tArea.getHeight())+20;
                DoubleProperty wProperty = new SimpleDoubleProperty();
                wProperty.bind(box.heightProperty());
                wProperty.addListener(new ChangeListener() 
                {
                    @Override
                    public void changed(ObservableValue ov, Object t, Object t1) 
                    {
                        scrlPane.setVvalue(scrlPane.getVmax()); 
                    }
                });
                box.getChildren().add(messageCount, tArea); 
                messageCount++;
            }
        });
    }
}
    

