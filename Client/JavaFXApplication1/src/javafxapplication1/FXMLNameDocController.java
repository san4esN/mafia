/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Eduard
 */
public class FXMLNameDocController implements Initializable {
    
    FXMLMainDocController mainController;
    
    
    @FXML
    private AnchorPane anchPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ipField;
    private Stage enterStage;
    //FXMLMainDocController mainController;
    userProperty user = new userProperty();
    public void setStage(Stage stage)
    {
        this.enterStage = stage;
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(nameField.getText()!= "" && ipField.getText()!="")
    	{
			userProperty.userName=nameField.getText();
                        //user.setUserName(nameField.getText());
                        userProperty.ipAddr = ipField.getText();
                        //user.setIpAddr(ipField.getText());
			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("FXMLMainDoc.fxml"));
                        //Parent mainRoot;
			try {
                            AnchorPane mainPane = (AnchorPane)mainLoader.load();
                            //mainRoot = FXMLLoader.load(getClass().getResource("FXMLMainDoc.fxml"));
                           
                            mainController = mainLoader.getController();
                            
                            mainController.setStage(enterStage);
                            
//                          mainController.setUser(user);
                            Scene scene = new Scene(mainPane);
                            enterStage.setScene(scene);
                            enterStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
    	}
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        anchPane.setMaxSize(200, 200);
        anchPane.setMinSize(200, 200);
        anchPane.setPrefSize(200, 200);
    }    
    
}
