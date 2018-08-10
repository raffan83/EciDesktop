/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.animations.FadeInLeftTransition;
import it.ncsnetwork.EciDesktop.animations.FadeInLeftTransition1;
import it.ncsnetwork.EciDesktop.animations.FadeInRightTransition;
import it.ncsnetwork.EciDesktop.model.User;
import it.ncsnetwork.EciDesktop.model.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Herudi
 */
public class LoginController implements Initializable {

	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Text lblEci;
	@FXML
	private Text lblSpa;
	@FXML
	private Text lblUserLogin;
	@FXML
	private Text lblUsername;
	@FXML
	private Text lblPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Text lblRudyCom;
	@FXML
	private Label lblClose;
	@FXML
	private Label errLogin;

	Stage stage;

	/**
	 * Initializes the controller class.
	 * 
	 * @param url
	 * @param rb
	 */

	public LoginController() {
	}

	public void initialize(URL url, ResourceBundle rb) {
		try {
			UserDAO.deleteAccessToken();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		Platform.runLater(() -> {
			new FadeInRightTransition(lblUserLogin).play();
			new FadeInLeftTransition(lblEci).play();
			new FadeInLeftTransition(lblSpa).play();
			new FadeInLeftTransition1(lblPassword).play();
			new FadeInLeftTransition1(lblUsername).play();
			new FadeInLeftTransition1(txtUsername).play();
			new FadeInLeftTransition1(txtPassword).play();
			new FadeInRightTransition(btnLogin).play();
			lblClose.setOnMouseClicked((MouseEvent event) -> {
				Platform.exit();
				System.exit(0);
			});
		});
		// TODO
	}

	public void Login(ActionEvent event) throws ClassNotFoundException, UnknownHostException, IOException {
		
		//verifica connessione

		if (config.isConnected()) { // login chiamata rest
			
			 Client client = ClientBuilder.newClient();

			 WebTarget target = client.target(config.URL_API.concat("login?username="+txtUsername.getText()+"&password="+txtPassword.getText()+"&action=download"));

		     User user = new User();
		     user.setUsername(txtUsername.getText());
		     user.setPassword(txtPassword.getText());
		     
		     try {
			     Response response = target.request().post(Entity.entity(user, MediaType.APPLICATION_JSON));
			     System.out.println("Response code: " + response.getStatus());
			     
			     if(response.getStatus() == 200) {
			    	 try { // aggiorna sul db i dati dell'utente
						String s = response.readEntity(String.class);
						JSONParser parser = new JSONParser(); 
						Object obj = parser.parse(s);
						JSONObject jsonObj = (JSONObject) obj;
						String accessToken = (String) jsonObj.get("access_token");
					    user.setAccessToken(accessToken);
			    		UserDAO.updateUser(txtUsername.getText(), txtPassword.getText(), accessToken);
			    		UserDAO.setUserId(txtUsername.getText(), txtPassword.getText());
					} catch (SQLException e) {
							e.printStackTrace();
					} finally {
			    	 config c = new config();
					 c.newStage(stage, lblClose, "/it/ncsnetwork/EciDesktop/view/intervention.fxml","Eci spa", true, StageStyle.DECORATED, false, user); 	
					}	     
			     } else {
			    	 errLogin.setText("Username o password errati!");
			     }
		     }catch (Exception e) {
		    	 e.printStackTrace();
		     } 

		} else { //effettua login  offline

			try {
				if (UserDAO.isLogin(txtUsername.getText(), txtPassword.getText())) {
				    User user = new User();
				    user.setUsername(txtUsername.getText());
				    user.setPassword(txtPassword.getText());
					config c = new config();
					c.newStage(stage, lblClose, "/it/ncsnetwork/EciDesktop/view/intervention.fxml","Eci spa", true,
							StageStyle.DECORATED, false, user);
				} else {
					errLogin.setText("Username o password errati!");
				}
			} catch (SQLException e) {
				errLogin.setText("Username o password errati!");
				e.printStackTrace();
			}
		}
	}

	/*
	 * private void aksiLogin(ActionEvent event) { if
	 * (txtUsername.getText().equals("herudi") &&
	 * txtPassword.getText().equals("herudi")) { config c = new config();
	 * c.newStage(stage, lblClose, "/it/ncsnetwork/EciDesktop/view/formMenu.fxml",
	 * "Test App", true, StageStyle.UNDECORATED, false); }else{ //
	 * config2.dialog(Alert.AlertType.ERROR,
	 * "Error Login, Please Chek Username And Password"); } }
	 */

}
