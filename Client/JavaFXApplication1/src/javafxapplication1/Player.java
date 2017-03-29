/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Eduard
 */
public class Player {
    private ImageView imgView;
    private Label stateLabel;
    private Button chooseBtn;
    private boolean playerState;
    private boolean isCurrentPlayer = false;
    private boolean isDisconnected = false;
    private ObjectProperty<Image> imagePropertyRecived;
    private AtomicReference<WritableImage> ref;
    Player(ImageView imgView, Label stateLabel, Button chooseBtn, ObjectProperty<Image> imagePropertyRecived, boolean playerState)
    {
        this.imgView = imgView;
        this.stateLabel = stateLabel;
        this.chooseBtn = chooseBtn;
        this.imagePropertyRecived = imagePropertyRecived;
        this.playerState = playerState;
    }

    public Label getStateLable()
    {
        return this.stateLabel;
    }
    public ImageView getImageView()
    {
        return this.imgView;
    }
    public Button getChooseButton()
    {
        return this.chooseBtn;
    }
    public ObjectProperty<Image> getImageProperty()
    {
        return imagePropertyRecived;
    }
    public boolean getState()
    {
        return playerState;
    }
    public void setState(boolean state)
    {
        if(state)
            this.stateLabel.setText("Жив");
        else
            this.stateLabel.setText("Мертв");
    }
    public void setState(String stateString)
    {
        if(stateString.equals("Жив"))
            playerState = true;
        else
            playerState = false;
        this.stateLabel.setText(stateString);
    }
    public void setDisconnectedState(boolean isDiscon)
    {
        if(isDiscon)
            this.stateLabel.setText("Отключился");
        else if(this.playerState)
            this.stateLabel.setText("Жив");
        else
            this.stateLabel.setText("Мертв");
        this.isDisconnected = isDiscon;
    }
    public boolean getDisconnectedState()
    {
        return this.isDisconnected;
    }
    public void setIsCurrentPlayer(boolean currPlr)
    {
        this.isCurrentPlayer = currPlr;
    }
    public boolean IsCurrentPlayer()
    {
        return isCurrentPlayer;
    }
    public void setAtomicRef(AtomicReference<WritableImage> ref)
    {
        this.ref = ref;
    }
    public AtomicReference<WritableImage> getAtomicRef()
    {
        return this.ref;
    }
}
