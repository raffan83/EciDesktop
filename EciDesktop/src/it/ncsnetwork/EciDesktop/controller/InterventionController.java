package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Domanda;
import it.ncsnetwork.EciDesktop.model.QuestionarioDAO;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Opzione;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import it.ncsnetwork.EciDesktop.model.Risposta;
import it.ncsnetwork.EciDesktop.model.RisposteIntervento;
import it.ncsnetwork.EciDesktop.model.RisposteVerbale;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

	private int selectedState = 4;
	private User selectedUser;
	private static ObservableList<Long> listaInterventi;
	private static ObservableList<Long> listaVerbali = FXCollections.observableArrayList();
	
	@FXML private TableView interventionTable;
	@FXML private TableColumn<Intervention, Long> idCol;
	@FXML private TableColumn<Intervention, String> sedeCol;
	@FXML private TableColumn<Intervention, String> dataCol;
	@FXML private TableColumn<Intervention, String> statoCol;
	@FXML private TableColumn<Intervention, String> codCategoriaCol;
	@FXML private TableColumn<Intervention, String> codVerificaCol;
	@FXML private TableColumn<Intervention, String> detailCol;
	@FXML private TableColumn<Intervention, String> inviaCol;
	@FXML private ComboBox comboBox;
	@FXML private ImageView imgDownload;
	@FXML private ImageView imgSend;
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
			if (stateText == Intervention.STATO_2)
				((Intervention) item).getStatoLbl().getStyleClass().add("completo");
			else if (stateText == Intervention.STATO_1) 
				((Intervention) item).getStatoLbl().getStyleClass().add("inLavorazione");
			else if (stateText == Intervention.STATO_3) 
				((Intervention) item).getStatoLbl().getStyleClass().add("inviato");
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
			
			if (((Intervention) item).getInviaInterv() instanceof Button) {
				((Intervention) item).getInviaInterv().getStyleClass().add("invia");	
				((Intervention) item).getInviaInterv().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						long intervId = ((Intervention) item).getId();
						Intervention.setIntervId(intervId);
	
						try {
							getJson2();
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
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
		inviaCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("inviaInterv"));

		idCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		sedeCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.32));
		dataCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		statoCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.11));
		codCategoriaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.11));
		codVerificaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.12));
		detailCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.06));
		inviaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.06));
		
		// popola la tabella
		searchInterventions();
		
		// imposta il button dettagli cliccabile e il colore allo stato e l'altezza righe
		setDetailAndState();
		setCellHeight();
	
        //configura la select per filtrare lo stato
        comboBox.getItems().addAll("Tutti",Intervention.STATO_0,Intervention.STATO_1,Intervention.STATO_2,Intervention.STATO_3);
        comboBox.setPromptText("Tutti");

	}

	//popola tabella
	private void populateInterventions(ObservableList<Intervention> intervData) throws ClassNotFoundException {
		interventionTable.setItems(intervData);
	}
	
	// imposta il valore del filtro slezionato
	public void selectState(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		selectedState = 4;
		if(comboBox.getValue().toString() == Intervention.STATO_0) selectedState = 0;
		else if(comboBox.getValue().toString() == Intervention.STATO_1) selectedState = 1;
		else if(comboBox.getValue().toString() == Intervention.STATO_2) selectedState = 2;
		else if(comboBox.getValue().toString() == Intervention.STATO_3) selectedState = 3;
		
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
	        if (selectedState == 0) str = Intervention.STATO_0;
	        else if (selectedState == 1) str = Intervention.STATO_1;
	        else if (selectedState == 2) str = Intervention.STATO_2;
	        else if (selectedState == 3) str = Intervention.STATO_3;
	        comboBox.setPromptText(str);
		} catch (SQLException e) {
			System.out.println("Error occurred while getting interventions information from DB.\n" + e);
			throw e;
		}
	}
	
	@FXML
	private void downloadInterventions(ActionEvent event) throws ClassNotFoundException {
		// imposta la gif di caricamento
		new Thread(() -> {
		    Platform.runLater(()-> imgDownload.setImage(new Image("/it/ncsnetwork/EciDesktop/img/load.gif")));	
		

		//verifica connessione
		if (config.isConnected()) {
			
			//chiamata get
	        Client client = ClientBuilder.newClient();
	        WebTarget target = client.target(config.URL_API.concat("intervento?action=download"));
	        
	        Response response = target.request().header("X-ECI-Auth", selectedUser.getAccessToken()).get();
	        System.out.println("Response code: " + response.getStatus());
	        
	        if (response.getStatus() == 200) {
	        
		        String s = response.readEntity(String.class);
		        System.out.println(s);
		        
		        ParseJsonInterventi(s);
		        
				// ripopola la tabella
				try {
					searchInterventions();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				// imposta il button dettagli cliccabile e l'altezza righe
				setDetailAndState();
				setCellHeight(); 
		             
	        } else {
	        	Platform.runLater(()-> config.dialogLogout(AlertType.ERROR, "Impossibile scaricare i nuovi interventi.", menuBar));
	        }
	        
		} else {
			Platform.runLater(()-> config.dialog(AlertType.WARNING, "Nessuna connessione"));
		}
        // ripristina l'immagine di default
        Platform.runLater(()-> imgDownload.setImage(new Image("/it/ncsnetwork/EciDesktop/img/download2.png")));
		}).start();

        
	}
	
	// parsa il json degli interventi e salva sul db
	private void ParseJsonInterventi(String s) {
		
		JSONParser parser = new JSONParser();
        
        try {
        	Object obj = parser.parse(s);
        	//Object obj = parser.parse(new FileReader("interventi.txt")); // per fare test
        	
        	JSONArray interventi = (JSONArray) obj;
        	for (Object intervento : interventi) {

            	JSONObject interv = (JSONObject) intervento;
            	
            	// id
            	long id = (long) interv.get("id");           	
            	// data creazione
            	long data = (long) interv.get("dataCreazione");
    			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    			String dataCreazione = df.format(data);
    			// sede
    			String sede = (String) interv.get("nome_sede");   			
    			// id commessa
    			//String idCommessa = (String) interv.get("idCommessa");
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
					Report r = new Report();
					long idVerb = (long) verb.get("id");
					r.setId(idVerb);
					r.setCodVerifica((String) verb.get("codiceVerifica"));
					r.setCodCategoria((String) verb.get("codiceCategoria"));
					r.setDescrVerifica((String) verb.get("descrizioneVerifica"));
					ReportDAO.saveJSON(r, id);
					
					//domande
					JSONArray domande = (JSONArray) verb.get("domande");
					for (Object dom : domande) {	
	                	JSONObject domanda = (JSONObject) dom;
	                	Domanda d = new Domanda();
	                	long idDomanda = (long) domanda.get("id");
						d.setId(idDomanda);
						d.setTesto((String) domanda.get("testo"));
						d.setObbligatoria((boolean) domanda.get("obbligatoria"));
						d.setPosizione((long) domanda.get("posizione"));
						QuestionarioDAO.saveJSONDomande(d, idVerb);
	                	
						// risposte
						Risposta risp = new Risposta();	                	
	                	JSONObject risposta = (JSONObject) domanda.get("risposta");
	                	long idRisposta = (long) risposta.get("id");
	                	risp.setId(idRisposta);
	                	String tipoRisposta = (String) risposta.get("tipo");
	                	risp.setTipo(tipoRisposta);
	                	//RES_TEXT
	                	if (tipoRisposta.equals(config.RES_TEXT)){
	                		QuestionarioDAO.saveJSONResText(risp, idDomanda);
	                	}
	                	// RES_FORMULA
	                	else if (tipoRisposta.equals(config.RES_FORMULA)) {
	                		risp.setLabel1((String) risposta.get("label1"));
	                		risp.setLabel2((String) risposta.get("label2"));
	                		risp.setOperatore((String) risposta.get("operatore"));
	                		risp.setLabelRisultato((String) risposta.get("label_risultato"));
	                		QuestionarioDAO.saveJSONResFormula(risp, idDomanda);		
	                	}
                		// RES_CHOICE
	                	else if (tipoRisposta.equals(config.RES_CHOICE)) {
	                		risp.setMultipla((boolean) risposta.get("multipla"));
	                		QuestionarioDAO.saveJSONResChoice(risp, idDomanda);
	                		
	                		JSONArray opzioni = (JSONArray) risposta.get("opzioni");
	    					for (Object op : opzioni) {
	    						JSONObject opzione = (JSONObject) op;
	    						Opzione o = new Opzione();
	    						o.setId((long) opzione.get("id"));
	    						o.setTesto((String) opzione.get("testo"));
	    						o.setPosizione((long) opzione.get("posizione"));
	    						QuestionarioDAO.saveJSONOpzioni(o, idRisposta);
	    					}
	                	}	
					}
					
				}
			
        	}	
        	
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void logout() throws ClassNotFoundException {
			config c = new config();
			c.logout(menuBar);
	}
	
	public void getJson() throws ClassNotFoundException, SQLException {
		String json = risposteInterventiJson();
		sendJson(json);
	}
	
	public void getJson2() throws ClassNotFoundException, SQLException {
		String json = risposteInterventiJson2();
		sendJson(json);
	}
	
	public void sendJson(String json) throws ClassNotFoundException, SQLException {
		new Thread(() -> {
		    Platform.runLater(()-> imgSend.setImage(new Image("/it/ncsnetwork/EciDesktop/img/load.gif")));	
		if (config.isConnected()) {
			
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(config.URL_API.concat("intervento"));
	
			Response response = target.request().header("X-ECI-Auth", selectedUser.getAccessToken()).post(Entity.entity(json, MediaType.APPLICATION_JSON));

			System.out.println("Response code: " + response.getStatus());
			
			//cambia stato
			if (response.getStatus() == 200) {
				
				try {
					InterventionDAO.updateStato(listaInterventi);
					ReportDAO.updateStato(listaVerbali);
					searchInterventions();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				setDetailAndState();
				setCellHeight();
				
			} else {
				Platform.runLater(()-> config.dialogLogout(AlertType.ERROR, "Impossibile inviare gli interventi.", menuBar));
			}
			
		} else {
			Platform.runLater(()-> config.dialog(AlertType.WARNING, "Nessuna connessione"));
		}
				
		 // ripristina l'immagine di default
	    Platform.runLater(()-> imgSend.setImage(new Image("/it/ncsnetwork/EciDesktop/img/Send.png")));
		}).start();
			
	}
	// invio tutti gli interventi completi
	public String risposteInterventiJson() throws ClassNotFoundException, SQLException {
		
		ArrayList<RisposteIntervento> risposteInterventoList = new ArrayList<RisposteIntervento>();
		
		listaInterventi = InterventionDAO.serchIntervCompleti();
		for (long idInterv: listaInterventi) {
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
				listaVerbali.add(idVerb);
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
	
// invio singolo intervento
	public static String risposteInterventiJson2() throws ClassNotFoundException, SQLException {
			
		ArrayList<RisposteIntervento> risposteInterventoList = new ArrayList<RisposteIntervento>();
		
		ArrayList<RisposteVerbale> risposteVerbale = new ArrayList<RisposteVerbale>();
		
		ObservableList<Long> lista = FXCollections.observableArrayList();
		lista.add(Intervention.getIntervId());
		listaInterventi = lista;
			
		listaVerbali = InterventionDAO.serchIntervCompleto(Intervention.getIntervId());
		for (long idVerb: listaVerbali) {
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
