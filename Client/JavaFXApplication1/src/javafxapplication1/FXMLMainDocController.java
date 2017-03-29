/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.GameClient;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * FXML Controller class
 *
 * @author Eduard
 */
public class FXMLMainDocController implements Initializable{
    
    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Pane enteringNamePane;
    @FXML
    private TextArea senderArea;
    @FXML
    private TextArea reciverArea;
    @FXML
    private ScrollPane scrlPane;
    @FXML
    private Button btnCamreaStart;
    @FXML
    private Button btnCamreaStop;
    @FXML
    private ChoiceBox cBox;
    @FXML
    private VBox box;
    @FXML
    ImageView recivedCamMe;
    @FXML
    ImageView recivedCamFirst;
    @FXML
    ImageView recivedCamSecond;
    @FXML
    ImageView recivedCamThird;
    @FXML 
    ImageView recivedCamFourth;
    @FXML
    ImageView recivedCamFifth;
    @FXML
    ImageView recivedCamSixth;
    @FXML
    Button btnMicroStart;
    @FXML
    Button btnMicroStop;
    @FXML
    TextField waitField;
    @FXML
    private TextField waitRecField;
    @FXML
    Label conLabel;
    @FXML
    Label infoLabel;
    @FXML
    Label timeLabel;
    @FXML
     Label killLabel;
    @FXML
    Label disconnectLabel;
    @FXML
     Label voteLabel;
    @FXML
     Label nameLabel;
    @FXML
     Label roleLabel;
    @FXML
     Label infoConLabel;
    @FXML
     Label stateLabel1;
    @FXML
     Label stateLabel2;
    @FXML
     Label stateLabel3;
    @FXML
    Label stateLabel4;
    @FXML
    Label stateLabel5;
    @FXML
    Label stateLabel6;
    @FXML
    Button chooseBtn1;
    @FXML
    Button chooseBtn2;
    @FXML
    Button chooseBtn3;
    @FXML
    Button chooseBtn4;
    @FXML
    Button chooseBtn5;
    @FXML
    Button chooseBtn6;
    @FXML
    Button startButton;
    @FXML
    Button continueButton;
    private double heightNeeded;
    
    private Stage mainStage;
    //private String userName = userProperty.userName;
    userProperty currUser;

    BufferedImage avatar = null;
    WebCamSend webCamera;
    private boolean microStop = true;
    SourceDataLine auline;
    private int userId;
    GameClient client = new GameClient();
    AudioSend audioSend;
    DataReciver dataReciver;
    String role;
    AnimationTimer timer;
    int mafiaId;
    boolean timerReset;
    ImageReciver imgRec;
    ArrayList<Player> playerList = new ArrayList<>();
    ArrayList<ObjectProperty<Image>> objPropList = new ArrayList<>();
    
    //public FXMLMainDocController() throws LineUnavailableException {
        //this.audioSend = new AudioSend(client.getAudioClient(),null);
        //webCamera = new WebCamSend(client.getVideoClient());
    //}
    
    @FXML
    public void handleButtonActionStart(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("start",userProperty.userId);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        imgRec.startButton.setDisable(true);
    }
    @FXML
    public void handleButtonActionContinue(ActionEvent event)
    {
        imgRec.timer.stop();
        imgRec.continueButton.visibleProperty().set(false);
        try {
            imgRec.client.sendCommand("stopTalk",userProperty.userId);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        imgRec.timeLabel.setVisible(false);
        imgRec.infoLabel.setText("Подождите пока остальные игроки выскажут свое мнение.");
        imgRec.infoLabel.setAlignment(Pos.CENTER);
        imgRec.infoLabel.setTextAlignment(TextAlignment.CENTER);
    }
    @FXML
    public void handleButtonActionVote1(ActionEvent event) throws IOException
    {
        imgRec.client.sendCommand("vote", 0);
        chooseButtonHandler();
    }
    @FXML
    public void handleButtonActionVote2(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("vote", 1);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chooseButtonHandler();
    }
    @FXML
    public void handleButtonActionVote3(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("vote", 2);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chooseButtonHandler();
    }
    @FXML
    public void handleButtonActionVote4(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("vote", 3);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chooseButtonHandler();;
    }
    @FXML
    public void handleButtonActionVote5(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("vote", 4);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chooseButtonHandler();
    }
    @FXML
    public void handleButtonActionVote6(ActionEvent event)
    {
        try {
            imgRec.client.sendCommand("vote", 5);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        chooseButtonHandler();
    }
    @FXML
    public void handleButtonActionStartWebCamStream(ActionEvent event)
    {
        btnCamreaStart.setVisible(false);
        btnCamreaStop.setVisible(true);
        //audioSend.sendAudio();
        webCamera.initializeWebCam();
    }
    @FXML
    public void handleButtonActionStopWebCamStream(ActionEvent event) throws IOException
    {
        btnCamreaStart.setVisible(true);
        btnCamreaStop.setVisible(false);
        //audioSend.setMicroStop(true);
        webCamera.getWebCam().close();
        webCamera.setStopCamera(true);
    }
    @FXML
    public void handleButtonActionStartMicroStream(ActionEvent event)
    {
        audioSend.sendAudio();
        btnMicroStart.setVisible(false);
        btnMicroStop.setVisible(true);
    }
    @FXML
    public void handleButtonActionStopMicroStream(ActionEvent event)
    {
        audioSend.setMicroStop(true);
        btnMicroStart.setVisible(true);
        btnMicroStop.setVisible(false);
    }
    
    @FXML
    public void handleButtonActionSendMessage(ActionEvent event) {
        if(senderArea.getText()!=null || senderArea.getText()!="")
        {
            //String str = LocalTime.now().getHour()+":"+LocalTime.now().getMinute()+":"+LocalTime.now().getSecond()+" "+userProperty.userName+": "+senderArea.getText();
            //client.getDataClient().getSender().sendData(str);
        }
    }
    @FXML
    public void handleButtonActionSetWait(ActionEvent event) {
        audioSend.setWait(Integer.valueOf(waitField.getText()));
        
    }
    @FXML
    public void handleButtonActionSendImage(ActionEvent event){
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("All Images", "*.*");
//        try{
//            fileChooser.selectedExtensionFilterProperty().set(exFilter);
//            File file = fileChooser.showOpenDialog(mainStage);
//            if(file!=null)
//            {
//                BufferedImage buf = ImageIO.read(file);
//                String str = LocalTime.now().getHour()+":"+LocalTime.now().getMinute()+":"+LocalTime.now().getSecond()+" "+userProperty.userName+": ";
//                client.getSender().sendData(str);
//                client.getSender().sendData(buf);
//            }
//        }
//        catch(Exception e)
//        {}
    }
    
    
    public void setStage(Stage stage)
    {
        this.mainStage = stage;
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public synchronized void handle(WindowEvent we) {
                if(webCamera.getWebCam()!=null)
                    webCamera.getWebCam().close();
                try {
                    //client.getDataClient().getSender().sendData("exit");
                    client.disconnect();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    wait(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(1);
            }    
        });
    }
    
    

    private void chooseButtonHandler()
    {
        for(int i=0;i<6;i++)
        {
            playerList.get(i).getChooseButton().setVisible(false);
        }
        imgRec.voteLabel.setVisible(true);
        imgRec.voteLabel.setText("Ваш голос учтен.");
        imgRec.timer.stop();
        imgRec.timeLabel.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.setImplicitExit(false);
        try {
            for(int i=0;i<6;i++)
            {
                objPropList.add(new SimpleObjectProperty<Image>());
            }
            userProperty.userId = client.connect(userProperty.ipAddr);
            client.sendCommand("name;"+userProperty.userName,userProperty.userId);
            this.audioSend = new AudioSend(client,null);
            webCamera = new WebCamSend(client);
            playerList.add(new Player(recivedCamFirst,stateLabel1,chooseBtn1,objPropList.get(0),true));
            playerList.add(new Player(recivedCamSecond,stateLabel2,chooseBtn2,objPropList.get(1),true));
            playerList.add(new Player(recivedCamThird,stateLabel3,chooseBtn3,objPropList.get(2),true));
            playerList.add(new Player(recivedCamFourth,stateLabel4,chooseBtn4,objPropList.get(3),true));
            playerList.add(new Player(recivedCamFifth,stateLabel5,chooseBtn5,objPropList.get(4),true));
            playerList.add(new Player(recivedCamSixth,stateLabel6,chooseBtn6,objPropList.get(5),true));
            timerReset = false;
            imgRec = new ImageReciver(client,this,playerList);

            /*client,this.dataReciver,this.recivedCamFirst, this.recivedCamSecond,
                                        this.recivedCamThird, this.recivedCamFourth,
                                        this.recivedCamFifth, this.recivedCamSixth,
                                        this.imagePropertyRecivedFirst, this.imagePropertyRecivedSecond,
                                        this.imagePropertyRecivedThird, this.imagePropertyRecivedFourth,
                                        this.imagePropertyRecivedFifth, this.imagePropertyRecivedSixth,
                                        this.stateLabel1,this.stateLabel2,this.stateLabel3,this.stateLabel4,this.stateLabel5,this.stateLabel6,
                                        this.conLabel, this.infoLabel, this.infoConLabel, this.killLabel,this.voteLabel,this.timeLabel,
                                        this.roleLabel,this.nameLabel,this.disconnectLabel,
                                        this.startButton,this.continueButton,
                                        this.chooseBtn1,this.chooseBtn2,this.chooseBtn3,this.chooseBtn4,this.chooseBtn5,this.chooseBtn6*/
        } catch (Exception ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        client.addCommandListener(imgRec.getDataReciver());

        client.addImageListener(imgRec);

        //client.getDataClient().getReceiver().getMessageUpdater().add(new MessageReciver(this.scrlPane,this.box));
        try {
            client.addAudioListener(new AudioReciver(userProperty.userId));
        } catch (LineUnavailableException ex) {
            Logger.getLogger(FXMLMainDocController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
}
        

         
