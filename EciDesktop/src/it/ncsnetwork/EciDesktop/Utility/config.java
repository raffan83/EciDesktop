/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import it.ncsnetwork.EciDesktop.controller.InterventionController;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Herudi
 */
public class config {
	
//	public static final String URL_API = "http://192.168.3.99:8080/PortalECI/rest/";

	public static final String URL_API = "http://localhost:8080/PortalECI/rest/";

	//public static final String URL_API = "http://192.168.1.64:8080/PortalECI/rest/";
	//public static final String URL_API = "http://31.14.128.57:8096/PortalECI/rest/";

	// combinazione per cambiare domanda al questionario
	public static final KeyCombination AVANTI = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN);
	public static final KeyCombination INDIETRO = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN);
	
	// path documenti
	public static String PATH_DOCUMENTI = "/documentiAllegati/";
	
	static {
		Properties properties = System.getProperties();
		PATH_DOCUMENTI = properties.getProperty("PATH_DOCUMENTI", PATH_DOCUMENTI);
		File documentiAllegati = new File(PATH_DOCUMENTI);
		if(!documentiAllegati.exists()) documentiAllegati.mkdirs();
	}
	
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
	
	public static void uploadFile(File source, long verbaleID) throws IOException {
		String path = PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+verbaleID;
		new File(path).mkdirs();
		File dest = new File(path+"\\"+source.getName());
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
        	inputChannel = new FileInputStream(source).getChannel();
        	outputChannel = new FileOutputStream(dest).getChannel();
        	outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
        	inputChannel.close();
        	outputChannel.close();
        }
	}
	
	public static String encodeFileToBase64Binary(File file) throws IOException {
	    byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
	    return new String(encoded, StandardCharsets.US_ASCII);
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
<<<<<<< HEAD
	
	public void testGet() {  //192.168.1.11:8080/PortalECI/rest/intervento
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/PortalECI/rest/intervento");
         
        Response response = target.request().get();
        System.out.println("Response code: " + response.getStatus());
        
        String s = response.readEntity(String.class);
        System.out.println(s);

	}
=======
*/

}
