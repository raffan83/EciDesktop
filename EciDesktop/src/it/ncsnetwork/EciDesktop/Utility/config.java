/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Utility;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Herudi
 */
public class config {

	public config() {
	}

	public static void dialog(Alert.AlertType alertType, String s) {
		Alert alert = new Alert(alertType, s);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Info");
		alert.showAndWait();
	}

	public void newStage(Stage stage, Label lb, String load, String judul, boolean resize, StageStyle style,
			boolean maximized) {
		try {
			Stage st = new Stage();
			stage = (Stage) lb.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource(load));

			Scene scene = new Scene(root);
			st.initStyle(style);
			st.setResizable(resize);
			st.setMaximized(maximized);
			st.setTitle(judul);
			st.setScene(scene);
			st.show();
			stage.close();
		} catch (Exception e) {
		}
	}

	public void newStage2(Stage stage, Button lb, String load, String judul, boolean resize, StageStyle style,
			boolean maximized) {
		try {
			Stage st = new Stage();
			stage = (Stage) lb.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource(load));
			Scene scene = new Scene(root);
			st.initStyle(style);
			st.setResizable(resize);
			st.setMaximized(maximized);
			st.setTitle(judul);
			st.setScene(scene);
			st.show();
			stage.close();
		} catch (Exception e) {
		}
	}

	public void newStageReport(String load, String title) {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource(load));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initStyle(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeScreenButtonPushed(ActionEvent event, String url) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource(url));
		Scene tableViewScene = new Scene(tableViewParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}

	public void loadAnchorPane(AnchorPane ap, String a) {
		try {
			AnchorPane p = FXMLLoader.load(getClass().getResource("/herudi/view/" + a));
			ap.getChildren().setAll(p);
		} catch (IOException e) {
		}
	}

	public static void setModelColumn(TableColumn tb, String a) {
		tb.setCellValueFactory(new PropertyValueFactory(a));
	}
}
