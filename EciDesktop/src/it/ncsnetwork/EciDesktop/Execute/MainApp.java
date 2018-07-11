/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Execute;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Herudi
 */
public class MainApp extends Application {

	public static Stage primaryStage;

	@Override

	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/splash.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

		Parent login = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
		Scene sceneLogin = new Scene(login);
		PauseTransition delay = new PauseTransition(Duration.seconds(5));
		delay.setOnFinished(event -> stage.setScene(sceneLogin));
		delay.play();

	}

	/*
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
