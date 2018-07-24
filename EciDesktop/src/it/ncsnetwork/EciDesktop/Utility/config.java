/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.Utility;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import it.ncsnetwork.EciDesktop.controller.InterventionController;
import it.ncsnetwork.EciDesktop.model.Domanda;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.QuestionarioDAO;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.Risposta;
import it.ncsnetwork.EciDesktop.model.RisposteIntervento;
import it.ncsnetwork.EciDesktop.model.RisposteVerbale;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
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
	
	public static String risposteVerbaleJson() throws ClassNotFoundException, SQLException {
		
		ObservableList<Domanda> domande = FXCollections.observableArrayList();
		try {
			domande = QuestionarioDAO.searchRisposte(Report.getReportId());
		} catch (SQLException e) {
			System.out.println("Error occurred while getting questions information from DB.\n" + e);
			throw e;
		}
		RisposteVerbale risposte = new RisposteVerbale();
		ArrayList<Risposta> rispList = new ArrayList<Risposta>();

		for (Domanda d: domande) {
			
			Risposta r = d.getRisposta();
			rispList.add(r);

		}
			risposte.setVerbale_id(Report.getReportId());
			risposte.setRisposte(rispList);
			
			Gson gson = new Gson();
			String json = gson.toJson(risposte);
			System.out.println(json);
			
			return json;
	}
	
	public static String risposteInterventiJson() throws ClassNotFoundException, SQLException {
		
		ArrayList<RisposteIntervento> risposteInterventoList = new ArrayList<RisposteIntervento>();
		
		for (long idInterv: InterventionDAO.serchIntervCompleti()) {
			ArrayList<RisposteVerbale> risposteVerbale = new ArrayList<RisposteVerbale>();
			
			for (long idVerb: InterventionDAO.serchIntervCompleto(idInterv)) {
				ObservableList<Domanda> domande = FXCollections.observableArrayList();
				try {
					domande = QuestionarioDAO.searchRisposte(idVerb);
				} catch (SQLException e) {
					System.out.println("Error occurred while getting questions information from DB.\n" + e);
					throw e;
				}
				
				RisposteVerbale risposte = new RisposteVerbale();
				ArrayList<Risposta> rispList = new ArrayList<Risposta>();
				for (Domanda d: domande) {
					Risposta r = d.getRisposta();
					rispList.add(r);
				}
				risposte.setVerbale_id(idVerb);
				risposte.setRisposte(rispList);
				
				risposteVerbale.add(risposte);
			}
			
			RisposteIntervento risposteIntervento = new RisposteIntervento();
			risposteIntervento.setIntervento_id(idInterv);
			risposteIntervento.setRisposteVerbale(risposteVerbale);
			
			risposteInterventoList.add(risposteIntervento);
			
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(risposteInterventoList);
		System.out.println(json);
		
		return json;
	}
	
	public static String risposteInterventiJson2() throws ClassNotFoundException, SQLException {
			
		ArrayList<RisposteIntervento> risposteInterventoList = new ArrayList<RisposteIntervento>();
		
		ArrayList<RisposteVerbale> risposteVerbale = new ArrayList<RisposteVerbale>();
			
		for (long idVerb: InterventionDAO.serchIntervCompleto(Intervention.getIntervId())) {
			ObservableList<Domanda> domande = FXCollections.observableArrayList();
			try {
				domande = QuestionarioDAO.searchRisposte(idVerb);
			} catch (SQLException e) {
				System.out.println("Error occurred while getting questions information from DB.\n" + e);
				throw e;
			}
			
			RisposteVerbale risposte = new RisposteVerbale();
			ArrayList<Risposta> rispList = new ArrayList<Risposta>();
			for (Domanda d: domande) {
				Risposta r = d.getRisposta();
				rispList.add(r);
			}
			risposte.setVerbale_id(idVerb);
			risposte.setRisposte(rispList);
			
			risposteVerbale.add(risposte);
		}
		
		RisposteIntervento risposteIntervento = new RisposteIntervento();
		risposteIntervento.setIntervento_id(Intervention.getIntervId());
		risposteIntervento.setRisposteVerbale(risposteVerbale);
		
		risposteInterventoList.add(risposteIntervento);
		
		Gson gson = new Gson();
		String json = gson.toJson(risposteInterventoList);
		System.out.println(json);
		
		return json;
	}
	
}
