/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Utility;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import it.ncsnetwork.EciDesktop.controller.InterventionController;
import it.ncsnetwork.EciDesktop.controller.ReportController;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.User;
import it.ncsnetwork.EciDesktop.model.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Herudi
 */
public class config {
	
	public static final String URL_API = "http://localhost:8080/PortalECI/rest/";
	public static final String RES_TEXT = "RES_TEXT";
	public static final String RES_FORMULA = "RES_FORMULA";
	public static final String RES_CHOICE = "RES_CHOICE";
	public static final String SOMMA = "Somma";
	public static final String MOLTIPLICAZIONE = "Moltiplicazione";
	
	public config() {
	}
	
	public static void dialogLogout(Alert.AlertType alertType, String s, MenuBar mb) {
		Alert alert = new Alert(alertType, s);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Errore");
		alert.setOnCloseRequest(event -> {
			config c = new config();
			try {
				c.logout(mb);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		alert.showAndWait();
	}

	public static void dialog(Alert.AlertType alertType, String s) {
		Alert alert = new Alert(alertType, s);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Info");
		alert.showAndWait();
	}

	public void newStage(Stage stage, Label lb, String load, String judul, boolean resize, StageStyle style,
			boolean maximized, User user) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(load));
			Parent root = loader.load();
			
			Stage st = new Stage();
			stage = (Stage) lb.getScene().getWindow();
			//Parent root = FXMLLoader.load(getClass().getResource(load));

			Scene scene = new Scene(root);
			st.initStyle(style);
			st.setResizable(resize);
			st.setMaximized(maximized);
			st.getIcons().add(new Image("/it/ncsnetwork/EciDesktop/img/logo-eci.jpg"));
			st.setTitle(judul);
			st.setScene(scene);
			
			InterventionController controller = loader.getController();
			controller.initData(user);
			
			st.show();
			stage.close();
		} catch (Exception e) {
		}
	}
	
	public void logout(MenuBar mb) throws ClassNotFoundException {
		try {
			Stage st = new Stage();
			Stage stage = (Stage) mb.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/login.fxml"));
			Scene scene = new Scene(root);
			st.initStyle(StageStyle.UNDECORATED);
			st.getIcons().add(new Image("/it/ncsnetwork/EciDesktop/img/logo-eci.jpg"));
			st.setScene(scene);
			st.show();
			stage.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static boolean isConnected() {

		try {
           URL siteURL = new URL(URL_API);
           HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
           connection.setRequestMethod("HEAD");
           connection.connect();
           System.out.println("connesso");
           return true;
      
		} catch (Exception e) {
			System.out.println("non connesso");
			return false;
		}
	}
/*
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
*/
/*
	public void loadAnchorPane(AnchorPane ap, String a) {
		try {
			AnchorPane p = FXMLLoader.load(getClass().getResource("/herudi/view/" + a));
			ap.getChildren().setAll(p);
		} catch (IOException e) {
		}
	}
*/
/*
	public static void setModelColumn(TableColumn tb, String a) {
		tb.setCellValueFactory(new PropertyValueFactory(a));
	}
*/
	
}
