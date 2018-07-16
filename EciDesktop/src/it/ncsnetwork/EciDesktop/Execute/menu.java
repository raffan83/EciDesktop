/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Execute;

import it.ncsnetwork.EciDesktop.view.PivotView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Herudi
 */
public class menu extends Application {

	public static String parameters;

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(PivotView.class.getResource("intervention.fxml"));
		Scene scene = new Scene(root);
		stage.getIcons().add(new Image("/it/ncsnetwork/EciDesktop/img/logo-eci.jpg"));
		stage.setScene(scene);
		stage.setTitle("ECI spa");
		stage.initStyle(StageStyle.DECORATED);
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
