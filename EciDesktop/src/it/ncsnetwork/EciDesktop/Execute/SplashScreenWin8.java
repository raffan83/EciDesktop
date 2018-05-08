/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Execute;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
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
    
	public static Stage primaryStage;

	@Override
    public void start(Stage stage) throws Exception {
    //	String imgLocation = "/image/walpaper_top.jpg";
	//	URL imageURL = GeneralGUI.class.getResource(imgLocation);
       primaryStage = stage;
       Parent root = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/splash.fxml"));
       Scene scene = new Scene(root);
       primaryStage.setScene(scene);
       primaryStage.initStyle(StageStyle.UNDECORATED);
       primaryStage.show();
        
  
       Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    	    	Parent login;
    	    	try {
    	    		
					login = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
					 Scene sceneLogin = new Scene(login);
				      primaryStage.setScene(sceneLogin);
				      primaryStage.show();
    	    	} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    	    }
    	});
    }

	class ValidateThread implements Runnable {
		public void run() 
		{
			
			 Parent login;
			try {
				Thread.sleep(2000);
				login = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
				 Scene sceneLogin = new Scene(login);
			      primaryStage.setScene(sceneLogin);
			      primaryStage.show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       
		}
		}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
