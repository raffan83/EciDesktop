package it.ncsnetwork.EciDesktop.controller;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class InterventionController {

	private int selectedState = 3;
	
	@FXML private TableView interventionTable;
	@FXML private TableColumn<Intervention, String> sedeCol;
	@FXML private TableColumn<Intervention, String> dataCol;
	@FXML private TableColumn<Intervention, String> statoCol;
	@FXML private TableColumn<Intervention, String> codCategoriaCol;
	@FXML private TableColumn<Intervention, String> codVerificaCol;
	@FXML private TableColumn<Intervention, String> detailCol;
	@FXML private ComboBox comboBox;

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
	
	//imposta il colore allo stato
	public void setStateColor() {
		statoCol.setCellFactory(new Callback<TableColumn<Intervention, String>, TableCell<Intervention, String>>(){
           @Override
            public TableCell<Intervention, String> call(TableColumn<Intervention, String> param) {
                return new TableCell<Intervention, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {
                       //this.setTextFill(Color.RED);
                        // Get fancy and change color based on data
                        if(item.contains("In lavorazione")) 
                            this.setTextFill(Color.ORANGE);
                        else if(item.contains("Completo")) 
                            this.setTextFill(Color.LIMEGREEN);
                        setText(item);
                    }
                }
                };        
	           };
		});
	}
	
	// imposta l'on click sul button dettagli
	public void setDetail() {
		for (Object item : interventionTable.getItems()) {
			((Intervention) item).getDetailBtn().getStyleClass().add("dettagli");
			((Intervention) item).getDetailBtn().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					int intervId = ((Intervention) item).getId();
					Intervention.setIntervId(intervId);

					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
						Parent tableViewParent = loader.load();

						Scene tableViewScene = new Scene(tableViewParent);

						// invio i dettagli dell'intervento alla pagina verbali
						ReportController controller = loader.getController();
						controller.initData((Intervention) item, selectedState);

						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setWidth(Control.USE_COMPUTED_SIZE);
						window.setHeight(Control.USE_COMPUTED_SIZE);
						window.setTitle("Verbali");
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

		sedeCol.setCellValueFactory(cellData -> cellData.getValue().sedeProperty());
		dataCol.setCellValueFactory(cellData -> cellData.getValue().dataCreazioneProperty());
		statoCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("stato"));
		codCategoriaCol.setCellValueFactory(cellData -> cellData.getValue().codCategoriaProperty());
		codVerificaCol.setCellValueFactory(cellData -> cellData.getValue().codVerificaProperty());
		detailCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("detailBtn"));

		sedeCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.45));
		dataCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		codCategoriaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.11));
		codVerificaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.11));
		
		// popola la tabella
		searchInterventions();
	
		// imposta il button dettagli cliccabile e il colore allo stato
		setDetail();
		setStateColor();
	
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
			setDetail();
			setStateColor();
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
	private void downloadInterventions() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://192.168.1.11:8080/PortalECI/rest/intervento?username=xxx&password=macrosolution");
         
        Response response = target.request().get();
        System.out.println("Response code: " + response.getStatus());
        
        String s = response.readEntity(String.class);
        System.out.println(s);
        
        JSONParser parser = new JSONParser();
        
        try {
        	Object obj = parser.parse(s);
        	JSONObject jsonObject = (JSONObject) obj;
        	
        	JSONArray interventi = (JSONArray) jsonObject.get("listaInterventi");
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
			
			System.out.println(id + " " + dataCreazione + " " + sede);
			
        	 }
        	
        } catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	
	

	@FXML
	private void downloadInterventions2() {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("interventi.txt"));
			JSONObject jsonObject = (JSONObject) obj;
			
            JSONArray interventi = (JSONArray) jsonObject.get("listaInterventi");

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
    			// tecnico verificatore
    			JSONObject tecnico = (JSONObject) interv.get("tecnico_verificatore");
    			Long idTecnico = (Long) tecnico.get("id");
    			// tipo verifica
    			JSONObject tipoVerifica = (JSONObject) interv.get("tipo_verifica");
    			String descrizioneTipo = (String) tipoVerifica.get("descrizione");
    			String codiceTipo = (String) tipoVerifica.get("codice");
    			// categoria verifica
    			JSONObject catVerifica = (JSONObject) interv.get("tipo_verifica");
    			String descrizioneCat = (String) catVerifica.get("descrizione");
    			String codiceCat = (String) catVerifica.get("codice");
    			
    			Intervention i = new Intervention();
    			i.setIdIntervPortale(id);
    			i.setDataCreazione(dataCreazione);
    			i.setSede(sede);
    			i.setDescrVerifica(descrizioneTipo);
    			i.setCodVerifica(codiceTipo);
    			i.setDescrCategoria(descrizioneCat);
    			i.setCodCategoria(codiceCat);
    			InterventionDAO.saveJSON(i);
            }
            
    		// ripopola la tabella
    		searchInterventions();
    		
    		// imposta button dettgli cliccabile e il colore allo stato
    		setDetail();
    		setStateColor();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
