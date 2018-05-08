/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Execute;

import java.net.URL;

import it.ncsnetwork.EciDesktop.view.PivotView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Herudi
 */
public class SplashScreenWin8 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
    //	String imgLocation = "/image/walpaper_top.jpg";
	//	URL imageURL = GeneralGUI.class.getResource(imgLocation);
		
   //     Parent root = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/splash.fxml"));
    //    Scene scene = new Scene(root);
    //    stage.setScene(scene);
   //     stage.initStyle(StageStyle.UNDECORATED);
     //   stage.show();
        
      //  Thread.sleep(2000);
        Parent login = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
        Scene sceneLogin = new Scene(login);
        stage.setScene(sceneLogin);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
