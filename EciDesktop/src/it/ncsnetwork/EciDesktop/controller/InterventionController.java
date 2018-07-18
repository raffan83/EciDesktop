package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class InterventionController {

	private int selectedState = 3;
	private User selectedUser;
	//private static User user;
	
	@FXML private TableView interventionTable;
	@FXML private TableColumn<Intervention, Long> idCol;
	@FXML private TableColumn<Intervention, String> sedeCol;
	@FXML private TableColumn<Intervention, String> dataCol;
	@FXML private TableColumn<Intervention, String> statoCol;
	@FXML private TableColumn<Intervention, String> codCategoriaCol;
	@FXML private TableColumn<Intervention, String> codVerificaCol;
	@FXML private TableColumn<Intervention, String> detailCol;
	@FXML private ComboBox comboBox;
	@FXML private ImageView imgDownload;
	@FXML private MenuBar menuBar;
	@FXML private Menu usernameMenu;
	@FXML private Label usernameMenuLbl;
	
	
	public void initData(User user) {
		selectedUser = user;
		usernameMenu.setText(selectedUser.getUsername());
		usernameMenuLbl.setText(selectedUser.getUsername());
		usernameMenuLbl.setStyle("-fx-text-fill: #444444;");
	}
	
	@FXML
	private void searchInterventions() throws SQLException, ClassNotFoundException {
		try {
			ObservableList<Intervention> intervData = InterventionDAO.searchInterventions();
			populateInterventions(intervData);
		} catch (SQLException e) {
			System.out.println("Error occurred while getting interventions information from DB.\n" + e);
			throw e;
		}
	}

	// imposta il testo a capo nelle righe della tabella
	public void setCellHeight () {
		List<TableColumn<Intervention, String>> list = new ArrayList<>();
		list.add(sedeCol);list.add(codVerificaCol);list.add(codCategoriaCol);
		for(TableColumn<Intervention, String> col : list) {
		   col.setCellFactory(new Callback<TableColumn<Intervention, String>, TableCell<Intervention, String>>() {
		        @Override
		        public TableCell<Intervention, String> call(
		                TableColumn<Intervention, String> param) {
		            TableCell<Intervention, String> cell = new TableCell<>();
		            Text text = new Text();
		            cell.setGraphic(text);
		            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		            text.wrappingWidthProperty().bind(cell.widthProperty());
		            text.textProperty().bind(cell.itemProperty());
		            return cell ;
		        }
		    });
		}
	}
	
	// imposta l'on click sul button dettagli
	public void setDetailAndState() {
		for (Object item : interventionTable.getItems()) {
			String stateText = ((Intervention) item).getStatoLbl().getText();
			((Intervention) item).getStatoLbl().setText(stateText.toUpperCase());
			if (stateText == "Completo")
				((Intervention) item).getStatoLbl().getStyleClass().add("completo");
			else if (stateText == "In lavorazione") 
				((Intervention) item).getStatoLbl().getStyleClass().add("inLavorazione");
			else ((Intervention) item).getStatoLbl().getStyleClass().add("daCompilare");
			
			((Intervention) item).getDetailBtn().getStyleClass().add("dettagli");	
			((Intervention) item).getDetailBtn().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					long intervId = ((Intervention) item).getId();
					Intervention.setIntervId(intervId);

					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
						Parent tableViewParent = loader.load();

						Scene tableViewScene = new Scene(tableViewParent);

						// invio i dettagli dell'intervento alla pagina verbali
						ReportController controller = loader.getController();
						controller.initData((Intervention) item, selectedState, selectedUser);

						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setWidth(Control.USE_COMPUTED_SIZE);
						window.setHeight(Control.USE_COMPUTED_SIZE);
						window.setScene(tableViewScene);
						window.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}
	
	// Initializing the controller class.
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		
		idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		sedeCol.setCellValueFactory(cellData -> cellData.getValue().sedeProperty());
		dataCol.setCellValueFactory(cellData -> cellData.getValue().dataCreazioneProperty());
		statoCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("statoLbl"));
		codCategoriaCol.setCellValueFactory(cellData -> cellData.getValue().codCategoriaProperty());
		codVerificaCol.setCellValueFactory(cellData -> cellData.getValue().codVerificaProperty());
		detailCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("detailBtn"));

		idCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		sedeCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.35));
		dataCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		statoCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.12));
		codCategoriaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.11));
		codVerificaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.12));
		detailCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.08));
		
		// popola la tabella
		searchInterventions();
		
		// imposta il button dettagli cliccabile e il colore allo stato e l'altezza righe
		setDetailAndState();
		setCellHeight();
	
        //configura la select per filtrare lo stato
        comboBox.getItems().addAll("Tutti","Da compilare","In lavorazione","Completo");
        comboBox.setPromptText("Tutti");

	}

	//popola tabella
	private void populateInterventions(ObservableList<Intervention> intervData) throws ClassNotFoundException {
		interventionTable.setItems(intervData);
	}
	
	// imposta il valore del filtro slezionato
	public void selectState(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		selectedState = 3;
		if(comboBox.getValue().toString() == "Da compilare") selectedState = 0;
		else if(comboBox.getValue().toString() == "In lavorazione") selectedState = 1;
		else if(comboBox.getValue().toString() == "Completo") selectedState = 2;
		
		searchSelectedState(selectedState);
	}
	
	// cerca interventi per lo stato selezionato
	public void searchSelectedState(int stato) throws ClassNotFoundException, SQLException {
		selectedState = stato;
		try {
			ObservableList<Intervention> intervData = InterventionDAO.searchIntervention(selectedState);
			populateInterventions(intervData);
			setDetailAndState();
			setCellHeight();
	        String str = "Tutti";
	        if (selectedState == 0) str = "Da compilare";
	        else if (selectedState == 1) str = "In lavorazione";
	        else if (selectedState == 2) str = "Completo";
	        comboBox.setPromptText(str);
		} catch (SQLException e) {
			System.out.println("Error occurred while getting interventions information from DB.\n" + e);
			throw e;
		}
	}
	
	@FXML
	private void downloadInterventions(ActionEvent event) {
		// imposta la gif di caricamento
		new Thread(() -> {
		    Platform.runLater(()-> imgDownload.setImage(new Image("/it/ncsnetwork/EciDesktop/img/load.gif")));	
		
		//verifica connessione
		if (config.isConnected()) {
			String username = selectedUser.getUsername();
			String password = selectedUser.getPassword();
			
			//chiamata get
	        Client client = ClientBuilder.newClient();
	        WebTarget target = client.target(config.URL_API.concat("intervento?username="+username+"&password="+password+"&action=download"));
	         
	        Response response = target.request().get();
	        System.out.println("Response code: " + response.getStatus());
	        
	        if (response.getStatus() == 200) {
	        
		        String s = response.readEntity(String.class);
		        System.out.println(s);
		        
		        JSONParser parser = new JSONParser();
		        
		        try {
		        	Object obj = parser.parse(s);
		        	
		        	JSONArray interventi = (JSONArray) obj;
		        	for (Object intervento : interventi) {
		
		            	JSONObject interv = (JSONObject) intervento;
		            	
		            	// id
		            	Long id = (Long) interv.get("id");
		            	
		            	// data creazione
		            	Long data = (Long) interv.get("dataCreazione");
		    			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		    			String dataCreazione = df.format(data);
		    			
		    			// sede
		    			String sede = (String) interv.get("nome_sede");
		    			
		    			// id commessa
		    			String idCommessa = (String) interv.get("idCommessa");
		
		    			// tipo verifica
		    			JSONArray tipoVerifica = (JSONArray) interv.get("tipo_verifica");
		    			String descrizioneTipo="", codiceTipo="", descrizioneCat="", codiceCat="";
		    			int count=0;
		    			for (Object tv : tipoVerifica) {
		                	JSONObject tipoVer = (JSONObject) tv;
			    			String descrTipo = (String) tipoVer.get("descrizione");	    			
			    			String codTipo = (String) tipoVer.get("codice");
			    			//categoria
			    			JSONObject catVer = (JSONObject) tipoVer.get("categoria");
			    			String descrCat = (String) catVer.get("descrizione");
			    			String codCat = (String) catVer.get("codice");
			    			
			    			if (count == 0) {
				    			descrizioneTipo = descrTipo;
				    			codiceTipo = codTipo;
				    			descrizioneCat = descrCat;
				    			codiceCat = codCat;
			    			}else {
				    			descrizioneTipo += ", " + descrTipo;
				    			codiceTipo += ", " + codTipo;
				    			descrizioneCat += ", " + descrCat;
				    			codiceCat += ", " + codCat;
			    			} 
			    			count++;
		        	 }
		    			
					//salva sul db i nuovi interventi
					Intervention i = new Intervention();
					i.setId(id);
					i.setDataCreazione(dataCreazione);
					i.setSede(sede);
					i.setDescrVerifica(descrizioneTipo);
					i.setCodVerifica(codiceTipo);
					i.setDescrCategoria(descrizioneCat);
					i.setCodCategoria(codiceCat);
					InterventionDAO.saveJSON(i);
					
					
					// salvo i verbali
					JSONArray verbali = (JSONArray) interv.get("verbali");
					for (Object v : verbali) {
						JSONObject verb = (JSONObject) v;
						Long idVerb = (Long) verb.get("id");
						String codVerVerb = (String) verb.get("codiceVerifica");
						String codCatVerb = (String) verb.get("codiceCategoria");
						String descrVerVerb = (String) verb.get("descrizioneVerifica");
							
						//salva sul db i verbali
						Report r = new Report();
						r.setId(idVerb);
						r.setCodVerifica(codVerVerb);
						r.setCodCategoria(codCatVerb);
						r.setDescrVerifica(descrVerVerb);
						ReportDAO.saveJSON(r, id);
					}
					
		        }
				// ripopola la tabella
				searchInterventions();
				
				// imposta il button dettagli cliccabile e l'altezza righe
				setDetailAndState();
				setCellHeight(); 	
		        	
		        } catch (Exception e) {
					e.printStackTrace();
				}
	        
	        } else {
	        	Platform.runLater(()-> config.dialog(AlertType.ERROR, "Impossibile scaricare i nuovi interventi."));
	        }
	        
		} else {
			Platform.runLater(()-> config.dialog(AlertType.WARNING, "Nessuna connessione"));
		}
        // ripristina l'immagine di default
        Platform.runLater(()-> imgDownload.setImage(new Image("/it/ncsnetwork/EciDesktop/img/download2.png")));
		}).start();
        
	}
	
	public void logout(ActionEvent event) throws ClassNotFoundException, SQLException {
			config c = new config();
			c.logout(menuBar);
	}
	

}
