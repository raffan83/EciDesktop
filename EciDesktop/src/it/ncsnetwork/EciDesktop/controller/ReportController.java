package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ReportController {
	
	private Intervention selectedInterv;
	
	@FXML private TableView reportTable;
	@FXML private TableColumn<Report, Integer> idCol;
	@FXML private TableColumn<Report, String> nameCol;
	@FXML private TableColumn<Report, ImageView> stateCol;
	@FXML private TableColumn<Report, String> dateCol;
	@FXML private TableColumn<Report, String> completeCol;
	@FXML private Label sedeLabel, dataLabel, codVerLabel, descrVerLabel, codCatLabel, descrCatLabel;
	
    public void initData(Intervention interv){
        selectedInterv = interv;
        sedeLabel.setText(selectedInterv.getSede());
        dataLabel.setText(selectedInterv.getDataCreazione());
        codVerLabel.setText(selectedInterv.getCodVerifica());
        descrVerLabel.setText(selectedInterv.getDescrVerifica());
        codCatLabel.setText(selectedInterv.getCodCategoria());
        descrCatLabel.setText(selectedInterv.getDescrCategoria());
    }
    
	@FXML
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/intervention.fxml"));
        Parent tableViewParent = loader.load();
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Interventi");
        window.setScene(tableViewScene);
        window.show();
    }
	
	@FXML
    private void searchReports() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Report> reportData = ReportDAO.searchReports();
            populateReports(reportData);
        } catch (SQLException e){
            System.out.println("Error occurred while getting reports information from DB.\n" + e);
            throw e;
        }
    }
    
    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
    	//String id = String.valueOf(Intervention.getIntervId());
    	//intervID.setText(id);
    	//intervName.setText(Intervention.getIntervName());
    	
		idCol.setCellValueFactory(cellData -> cellData.getValue().idRepProperty().asObject());
		nameCol.setCellValueFactory(cellData -> cellData.getValue().nameRepProperty());
		stateCol.setCellValueFactory(new PropertyValueFactory<Report, ImageView>("state"));
		stateCol.setStyle("-fx-alignment: CENTER;");
		//dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		//detailCol.setCellValueFactory(cellData -> cellData.getValue().detailRepProperty());
		completeCol.setCellValueFactory(new PropertyValueFactory<Report, String>("completeRep"));
		
		searchReports();
		
		for (Object item: reportTable.getItems()) {
			if(((Report) item).getCompleteRep() instanceof Button) {
				((Report) item).getCompleteRep().setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						
	                    int repId = ((Report) item).getIdRep();
	                    Report.setReportId(repId);
	                    
						Parent tableViewParent;
							try {
								tableViewParent = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/questionnaire.fxml"));
								Scene tableViewScene = new Scene(tableViewParent);
						        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
						        window.setTitle("Questionario");
						        window.setScene(tableViewScene);
						        window.show();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					}
				});

			}
		}
		
		//reportTable.getItems().forEach(item -> System.out.println(((Report) item).getCompleteRep()));
    	
		// campo completa cliccabile
		/*	completeCol.setCellFactory(tc -> {
		            TableCell<Report, String> cell = new TableCell<Report, String>() {
		                @Override
		                protected void updateItem(String item, boolean empty) {
		           		super.updateItem(item, empty) ;
		                    setText(empty ? null : item);
		                }x
		            };
		            cell.setOnMouseClicked(e -> {
		               if (! cell.isEmpty()) {
		                    Object userId = cell.getTableRow().getItem();
		                    if (((Report) userId).getCompleteRep() != null) {
			                    int reportId = ((Report) userId).getId();
			                    Report.setReportId(reportId);
			                    config c = new config();
			                    c.newStageReport("/it/ncsnetwork/EciDesktop/view/questionnaire.fxml", "Verbale");
		                    }
		                }
		            });
		            return cell ;
		        });*/
    }

	@FXML
    private void populateReports (ObservableList<Report> reportData) throws ClassNotFoundException {
    	reportTable.setItems(reportData);
    }
	
}

