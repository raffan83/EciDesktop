/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.ncsnetwork.EciDesktop.Execute.MainApp;
import it.ncsnetwork.EciDesktop.animations.FadeInLeftTransition;
import it.ncsnetwork.EciDesktop.animations.FadeInRightTransition;
import it.ncsnetwork.EciDesktop.animations.FadeInTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * FXML Controller class
 *
 * @author Herudi
 */
public class SplashController implements Initializable  {
    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblRudy;
    @FXML
    private VBox vboxBottom;
    @FXML
    private Label lblClose;
    Stage stage;
    @FXML
    private ImageView imgLoading;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
   
    public SplashController() 
    {
    }
   /* public void initialize(URL url, ResourceBundle rb) {
   //     longStart();
    	 new FadeInLeftTransition(lblWelcome).play();
         new FadeInRightTransition(lblRudy).play();
         new FadeInTransition(vboxBottom).play();
        lblClose.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit();
            System.exit(0);
        });
        // TODO
    }   
    
    private void longStart() {
        Service<ApplicationContext> service = new Service<ApplicationContext>() {
            @Override
            protected Task<ApplicationContext> createTask() {
                return new Task<ApplicationContext>() {           
                    @Override
                    protected ApplicationContext call() throws Exception {
                        ApplicationContext appContex = config.getInstance().getApplicationContext();
                        int max = appContex.getBeanDefinitionCount();
                        updateProgress(0, max);
                        for (int k = 0; k < max; k++) {
                            Thread.sleep(50);
                            updateProgress(k+1, max);
                        }
                        return appContex;
                    }
                };
            }
        };
        service.start();
        service.setOnRunning((WorkerStateEvent event) -> {
            new FadeInLeftTransition(lblWelcome).play();
            new FadeInRightTransition(lblRudy).play();
            new FadeInTransition(vboxBottom).play();
        });
   //     service.setOnSucceeded((WorkerStateEvent event) -> {
            
   //     });
    } */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 new FadeInLeftTransition(lblWelcome).play();
         new FadeInRightTransition(lblRudy).play();
         new FadeInTransition(vboxBottom).play();
        lblClose.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit();
            System.exit(0);
        });
		
        try {
			Thread.sleep(2000);
		//	 Parent login = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
		//        Scene sceneLogin = new Scene(login);
		 //       SplashScreenWin8.primaryStage.setScene(sceneLogin);
		 //       SplashScreenWin8.primaryStage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

	}
}
