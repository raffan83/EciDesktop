package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReportController {

	private Intervention selectedInterv;
	private int selectedState;

	@FXML private TableView reportTable;
	@FXML private TableColumn<Report, Integer> idCol;
	@FXML private TableColumn<Report, String> nameCol;
	@FXML private TableColumn<Report, ImageView> stateCol;
	@FXML private TableColumn<Report, String> dateCol;
	@FXML private TableColumn<Report, String> completeCol;
	@FXML private Label sedeLabel, dataLabel, codVerLabel, descrVerLabel, codCatLabel, descrCatLabel;
	@FXML private Text note;
	
	
	public ReportController() {
		
	}
	
	public void initData(Intervention interv, int state) {
		selectedInterv = interv;
		sedeLabel.setText(selectedInterv.getSede());
		dataLabel.setText(selectedInterv.getDataCreazione());
		codVerLabel.setText(selectedInterv.getCodVerifica());
		descrVerLabel.setText(selectedInterv.getDescrVerifica());
		codCatLabel.setText(selectedInterv.getCodCategoria());
		descrCatLabel.setText(selectedInterv.getDescrCategoria());
		note.setText(selectedInterv.getNote());
		selectedState = state;
	}

	// apre dialog note
	public void modNote(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/template/note.fxml"));
		Stage st = new Stage();
		Parent root = loader.load();
		Scene scene = new Scene(root);
		NoteController controller = loader.getController();
		controller.initData(selectedInterv);
		st.setTitle("Note");
		st.setScene(scene);
		st.show();
	}

	@FXML
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/intervention.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);
		InterventionController controller = loader.getController();
		controller.searchSelectedState(selectedState);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setTitle("Interventi");
		window.setScene(tableViewScene);
		window.show();
	}

	@FXML
	private void searchReports() throws SQLException, ClassNotFoundException {
		try {
			ObservableList<Report> reportData = ReportDAO.searchReports();
			populateReports(reportData);
		} catch (SQLException e) {
			System.out.println("Error occurred while getting reports information from DB.\n" + e);
			throw e;
		}
	}

	@FXML
	private void initialize() throws ClassNotFoundException, SQLException {

		idCol.setCellValueFactory(cellData -> cellData.getValue().idRepProperty().asObject());
		nameCol.setCellValueFactory(cellData -> cellData.getValue().nameRepProperty());
		stateCol.setCellValueFactory(new PropertyValueFactory<Report, ImageView>("state"));
		stateCol.setStyle("-fx-alignment: CENTER;");
		// dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		completeCol.setCellValueFactory(new PropertyValueFactory<Report, String>("completeRep"));

		searchReports();

		for (Object item : reportTable.getItems()) {
			if (((Report) item).getCompleteRep() instanceof Button) {
				((Report) item).getCompleteRep().getStyleClass().add("completaVerbale");
				((Report) item).getCompleteRep().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {

						int repId = ((Report) item).getIdRep();
						Report.setReportId(repId);

						try {
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/questionnaire.fxml"));
							Parent tableViewParent = loader.load();
							Scene tableViewScene = new Scene(tableViewParent);

							QuestionnaireController controller = loader.getController();
							controller.setData(selectedInterv, selectedState);
							controller.createQuest();

							Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
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
		
	}

	@FXML
	private void populateReports(ObservableList<Report> reportData) throws ClassNotFoundException {
		reportTable.setItems(reportData);
	}
	
}
