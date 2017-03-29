/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.GameClient;
import ClientLib.Service.Listener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.TextAlignment;
import static javafxapplication1.DataReciver.con;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduard
 */
public class ImageReciver extends ImageView implements Listener<BufferedImage> {
    Label conLabel;
    Label infoConLabel;
    Label infoLabel;
    Label killLabel;
    Label voteLabel;
    Label timeLabel;
    Label roleLabel;
    Label nameLabel;
    Label disconnectLabel;
    Button startButton;
    Button continueButton;
    BlockingDeque<AtomicReference<WritableImage>> buffer0 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    BlockingDeque<AtomicReference<WritableImage>> buffer1 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    BlockingDeque<AtomicReference<WritableImage>> buffer2 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    BlockingDeque<AtomicReference<WritableImage>> buffer3 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    BlockingDeque<AtomicReference<WritableImage>> buffer4 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    BlockingDeque<AtomicReference<WritableImage>> buffer5 = new LinkedBlockingDeque<AtomicReference<WritableImage>>();
    ArrayList<BlockingDeque<AtomicReference<WritableImage>>> bufferList = new ArrayList<BlockingDeque<AtomicReference<WritableImage>>>();
    
    boolean timerReset = false;
    GameClient client;
    String role;
    int mafiaId;
    DataReciver dataReciverr;
    FXMLMainDocController doc;
    ArrayList<Player> playerList = new ArrayList<>();
    public ImageReciver(GameClient client,FXMLMainDocController doc,ArrayList<Player> playerList)
    {
        this.doc = doc;
        this.client = client;
        this.dataReciverr = doc.dataReciver;
        this.playerList = playerList;
        this.conLabel = doc.conLabel;
        this.infoConLabel = doc.infoConLabel;
        this.killLabel = doc.killLabel;
        this.voteLabel = doc.voteLabel;
        this.infoLabel = doc.infoLabel;
        this.timeLabel = doc.timeLabel;
        this.roleLabel = doc.roleLabel;
        this.nameLabel = doc.nameLabel;
        this.disconnectLabel = doc.disconnectLabel;
        this.startButton = doc.startButton;
        this.continueButton = doc.continueButton;
        this.bufferList.add(buffer0);
        this.bufferList.add(buffer1);
        this.bufferList.add(buffer2);
        this.bufferList.add(buffer3);
        this.bufferList.add(buffer4);
        this.bufferList.add(buffer5);
    }
    private void changeStateLabelToKill(int id)
    {
        playerList.get(id).setState(false);
    }
    private void changeStateToDiscon(int id)
    {
        playerList.get(id).setDisconnectedState(true);
    }
    private void showButtons(int id)
    {
        playerList.get(id).setIsCurrentPlayer(true);
        for(int i=0;i<6;i++)
        {
            if(!playerList.get(i).IsCurrentPlayer()&&!playerList.get(i).getDisconnectedState())
            {
                if(playerList.get(i).getState())
                    playerList.get(i).getChooseButton().setVisible(true);
            }
            else
                playerList.get(i).getChooseButton().setVisible(false);
        }
        
    }
     private void hideImages()
    {
        Image img0 = new Image("Безымянный1234.png");
        for(int i=0;i<6;i++)
        {
            if(!playerList.get(i).IsCurrentPlayer()&&i!=mafiaId)
            {
                playerList.get(i).getImageProperty().set(img0);
                if(playerList.get(i).getImageProperty()!=null)
                    playerList.get(i).getImageView().imageProperty().bind(playerList.get(i).getImageProperty());
            }
        }
    }
    AnimationTimer timer = new AnimationTimer() 
                                {
                                    int minutes = 2;
                                    int seconds = 0;
                                    int miliseconds = 0;
                                    @Override
                                    public void handle(long now) {
                                        if(timerReset)
                                        {
                                            minutes = 2;
                                            seconds = 0;
                                            miliseconds = 0;
                                            timerReset = false;
                                        }
                                        timeLabel.setText("Осталось времени: "+minutes+":"+seconds);
                                        if(seconds ==0)
                                        {
                                            if(minutes==0)
                                            {
                                                if(miliseconds==0)
                                                {
                                                    minutes = 2;
                                                    seconds = 0;
                                                    miliseconds =0;
                                                    this.stop();
                                                    if(con.equals("day"))
                                                    {
                                                        try {
                                                            infoLabel.setText("Время вышло. Подождите пока остальные игроки выскажут свое мнение.");
                                                            continueButton.setVisible(false);
                                                            timeLabel.setVisible(false);
                                                            client.sendCommand("stopTalk",userProperty.userId);
                                                        } catch (IOException ex) {
                                                            Logger.getLogger(ImageReciver.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                    else if(con.equals("night"))
                                                    {
                                                        timeLabel.setVisible(false);
                                                        if(role.equals("mafia"))
                                                        {
                                                            voteLabel.setText("Время вышло. Вы проголосовали за себя.");
                                                            try {
                                                                client.sendCommand("vote", userProperty.userId);
                                                            } catch (IOException ex) {
                                                                Logger.getLogger(ImageReciver.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                            timeLabel.setVisible(false);
                                                        }
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                minutes--;
                                                seconds = 60;
                                            }
                                        }
                                        if(miliseconds ==0)
                                        {
                                            seconds--;
                                            miliseconds = 60;
                                        }
                                        miliseconds--;
                                        
                                    }
                                };
    private void showImages(BufferedImage object,int id)
    {
        playerList.get(id).setAtomicRef(new AtomicReference<>());
        try
        {
            playerList.get(id).getAtomicRef().set(SwingFXUtils.toFXImage(object, playerList.get(id).getAtomicRef().get()));
            bufferList.get(id).add(playerList.get(id).getAtomicRef());
            if(bufferList.get(id).size()>=45)
            {
                Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playerList.get(id).getImageProperty().set(bufferList.get(id).pollLast().get());
                            }   
                        });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0;i<6;i++)
        {
            if(playerList.get(i).getImageProperty()!=null)
                playerList.get(i).getImageView().imageProperty().bind(playerList.get(i).getImageProperty());
        }
    }
    @Override
    public void getObject(BufferedImage object, int id) 
    {
        Platform.runLater(new Runnable()
            {
                @Override
                public void run() 
                {
                    if(DataReciver.con==null||DataReciver.con.equals("day"))
                    {
                        showImages(object,id);
                    }
                    else
                    { 
                        if(role.equals("mafia"))
                        {
                            if(id==userProperty.userId || id == mafiaId)
                            {
                                showImages(object, id);
                            }
                        }
                    }
                }
            });
    }
    public dataReciver getDataReciver()
    {
        dataReciver dataRec = new dataReciver();
        return dataRec;
    }
    class dataReciver implements Listener<String[]>
    {
        @Override
        public void getObject(String[] object, int id) 
        {
            Platform.runLater(new Runnable() 
            {
                @Override
                public void run() 
                {

                    String obj = object[0];
                    boolean first = obj.equals("mafia");
                    boolean second = obj.equals("citizen");
                    boolean firstIf = first || second;
                    if (obj.equals("start_game"))
                    {
                        for(int i=0;i<6;i++)
                        {
                            playerList.get(i).setState(true);
                            playerList.get(i).getStateLable().setVisible(true);
                        }

                        startButton.setVisible(false);
                        killLabel.setText("");
                        killLabel.setVisible(true);
                        conLabel.setVisible(true);
                        infoConLabel.setVisible(true);
                        infoLabel.setText("Игра началась. Игроки высказывают свое мнение. Ожидайте своей очереди.");
                    }
                    else if(obj.equals("night")||obj.equals("day"))
                    {

                        con = obj;
                        if(con.equals("day"))
                        {
                            timer.stop();
                            conLabel.setText("День");
                            infoConLabel.setText("Наступил день. Мирные жители проснулись.");
                            infoLabel.setText("Игроки высказывают свое мнение. Ожидайте своей очереди.");
                            voteLabel.setVisible(false);
                        }
                        else
                        {
                            voteLabel.setVisible(false);
                            conLabel.textProperty().set("Ночь");
                            infoConLabel.setText("Наступила ночь. Мирные жители засыпают. Просыпается мафия.");
                            timerReset = true;
                            timer.start();
                            timeLabel.setVisible(true);
                            if(role.equals("citizen"))
                            {
                                infoLabel.setText("Подождите пока мафия определится с выбором.");
                                Image img0 = new Image("Безымянный1234.png");
                                for(int i=0;i<6;i++)
                                {
                                    playerList.get(i).getImageProperty().set(img0);
                                    if(playerList.get(i).getImageProperty()!=null)
                                        playerList.get(i).getImageView().imageProperty().bind(playerList.get(i).getImageProperty());
                                } 
                            }
                            else if(role.equals("mafia"))
                            {
                                infoLabel.setText("У вас есть 2 минуты что бы определиться или вы проголосуете за себя.");
                                hideImages();
                                showButtons(userProperty.userId);
                            }
                        }
                    }
                    else if(firstIf)
                    {
                        if(role==null&&id == userProperty.userId)
                        {
                            role = obj;
                            if(role.equals("citizen"))
                                roleLabel.setText("Ваша роль: мирный житель.");
                            else
                                roleLabel.setText("Ваша роль: мафия.");
                            nameLabel.setText("Вы: "+userProperty.userName);
                        }
                        if(obj.equals("mafia")&&id!=userProperty.userId)
                            mafiaId = id;
                    }
                    else if(obj.equals("startTalk"))
                    {
                            timer.stop();
                            voteLabel.setVisible(false);
                            infoLabel.setText("У вас есть 2 минуты что бы выразить вашу позицию.");
                            infoLabel.setAlignment(Pos.CENTER);
                            infoLabel.setTextAlignment(TextAlignment.CENTER);
                            timerReset = true;
                            timer.start();
                            timeLabel.visibleProperty().set(true);
                            continueButton.visibleProperty().set(true);

                    }
                    else if(obj.equals("startVote"))
                    {
                        timer.stop();
                        showButtons(userProperty.userId);    
                    }
                    else if(obj.equals("voteFailed"))
                    {
                        timer.stop();
                        if(con.equals("day"))
                            infoConLabel.setText("Голосование мирных жителей не удалось. Наступила ночь.");
                        if(con.equals("night"))
                            infoConLabel.setText("Голосование мафии не удалось. Наступил день.");
                    }
                    else if(obj.equals("kill"))
                    {
                        timer.stop();
                        timeLabel.setVisible(false);
                        if(con.equals("day"))
                            killLabel.setText("Мирные жители убили игрока: "+object[1]+".");
                        if(con.equals("night"))
                            killLabel.setText("Мафия убила игрока: "+object[1]+".");
                        changeStateLabelToKill(id);
                    }
                    else if(obj.equals("disconnected"))
                    {
                        disconnectLabel.setText("Игрок: "+object[1]+" отключился.");
                        changeStateToDiscon(id);        
                    }
                    else if(obj.equals("mafia_win"))
                    {
                        timer.stop();
                        infoLabel.setText("Мафия победила.");
                        continueButton.setVisible(false);
                        conLabel.setVisible(false);
                        killLabel.setVisible(false);
                        infoConLabel.setVisible(false);
                        voteLabel.setVisible(false);
                        startButton.setDisable(false);
                        startButton.setVisible(true);
                    }
                    else if(obj.equals("citizen_win"))
                    {
                        timer.stop();
                        infoLabel.setText("Мирные жители победили.");
                        continueButton.setVisible(false);
                        conLabel.setVisible(false);
                        killLabel.setVisible(false);
                        infoConLabel.setVisible(false);
                        voteLabel.setVisible(false);
                        startButton.setDisable(false);
                        startButton.setVisible(true);
                    }
                }
            });
        }
    }   
}
