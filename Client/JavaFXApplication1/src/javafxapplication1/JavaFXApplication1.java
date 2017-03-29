/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Eduard
 */
public class JavaFXApplication1 extends Application {
    FXMLNameDocController doc;
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNameDoc.fxml"));
        try{
        //nameRoot = FXMLLoader.load(getClass().getResource("FXMLNameDoc.fxml"));
            AnchorPane pane = (AnchorPane)loader.load();
            doc = loader.getController();
            doc.setStage(stage);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
            
            
        //FXMLNameDocController.enterStage = stage;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
