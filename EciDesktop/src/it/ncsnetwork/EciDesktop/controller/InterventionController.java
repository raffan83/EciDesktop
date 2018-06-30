package it.ncsnetwork.EciDesktop.controller;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InterventionController {
	
	@FXML private TableView interventionTable;
	//@FXML private TableColumn<Intervention, Integer> idCol;
	@FXML private TableColumn<Intervention, String> sedeCol;
	@FXML private TableColumn<Intervention, String> dataCol;
	@FXML private TableColumn<Intervention, String> statoCol;
	@FXML private TableColumn<Intervention, String> codCategoriaCol;
	@FXML private TableColumn<Intervention, String> codVerificaCol;
	@FXML private TableColumn<Intervention, String> detailCol;
	
	Stage stage;

    @FXML
    private void searchInterventions() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Intervention> intervData = InterventionDAO.searchInterventions();
            populateInterventions(intervData);
        } catch (SQLException e){
            System.out.println("Error occurred while getting interventions information from DB.\n" + e);
            throw e;
        }
    }
	
    //Initializing the controller class.
	@FXML
	public void initialize () throws ClassNotFoundException, SQLException {
		InterventionDAO.setState();
		
		//idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		sedeCol.setCellValueFactory(cellData -> cellData.getValue().sedeProperty());
		dataCol.setCellValueFactory(cellData -> cellData.getValue().dataCreazioneProperty());
		statoCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("stato"));
		codCategoriaCol.setCellValueFactory(cellData -> cellData.getValue().codCategoriaProperty());
		codVerificaCol.setCellValueFactory(cellData -> cellData.getValue().codVerificaProperty());
		detailCol.setCellValueFactory(new PropertyValueFactory<Intervention, String>("detailBtn"));
		
		searchInterventions();
		
		//idCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.05));
		sedeCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.5));
		dataCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		//statoCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.12));
		codCategoriaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.088));
		codVerificaCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));
		//detailCol.prefWidthProperty().bind(interventionTable.widthProperty().multiply(0.1));

		
		for (Object item: interventionTable.getItems()) {
			//if (((Intervention) item).getStato() == "In lavorazione") setStyle("-fx-text-fill: YELLOW");
			((Intervention) item).getDetailBtn().setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
                    int intervId = ((Intervention) item).getId();
                    Intervention.setIntervId(intervId);
                    
						try {
					        FXMLLoader loader = new FXMLLoader();
					        loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
					        Parent tableViewParent = loader.load();
					        
					        Scene tableViewScene = new Scene(tableViewParent);
					        
					        //access the controller and call a method
					        ReportController controller = loader.getController();
					        controller.initData((Intervention) item);
							
					        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
					        //window.setFullScreen(true);
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

		// campo dettagli cliccabile
	/*	detailCol.setCellFactory(tc -> {
            TableCell<Intervention, String> cell = new TableCell<Intervention, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item);
                }
                
            };
            cell.setOnMouseClicked(e -> {
                if (! cell.isEmpty()) {
                    Object userId = cell.getTableRow().getItem();
                    int intervId = ((Intervention) userId).getId();
                    String intervName = ((Intervention) userId).getName();
                    Intervention.setIntervId(intervId);
                    Intervention.setIntervName(intervName);
                    config c = new config();
                    c.newStageReport("/it/ncsnetwork/EciDesktop/view/report.fxml", "Verbali");
                }
            });
            return cell ;
        });*/
	}
	
    private void populateInterventions (ObservableList<Intervention> intervData) throws ClassNotFoundException {
        interventionTable.setItems(intervData);
    }
    
    @FXML
    private void downloadInterventions() {

    	JSONParser parser = new JSONParser();
    	 
        try {
 
            Object obj = parser.parse(new FileReader("interventi.txt"));
            JSONObject jsonObject = (JSONObject) obj;
 
            JSONObject interventi = (JSONObject) jsonObject.get("listaInterventi");
            //System.out.println(interventi);
           
            //System.out.println(interventi.size());
            
            JSONObject interv = (JSONObject) interventi.get("16");
       
            Long data = (Long) interv.get("dataCreazione");
            Date d = new Date(data);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dataString = df.format(d);
            
            System.out.println(data);
            System.out.println(d.toString());
            System.out.println(dataString);



            
            
            //JSONObject interv = (JSONObject) interventi.get("16");
            

            //String sede = (String) interv.get("nome_sede");
           // String idCommessa = (String) interv.get("idCommessa");
            //JSONArray companyList = (JSONArray) jsonObject.get("Company List");
 
          //  System.out.println("Sede: " + sede);
          //  System.out.println("idCommessa: " + idCommessa);
           /* System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }

}
