package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ReportController {

	private Intervention selectedInterv;
	private int selectedState;
	private User selectedUser;

	@FXML private TableView reportTable;
	@FXML private TableColumn<Report, Long> idCol;
	@FXML private TableColumn<Report, String> descrVerificaCol;
	@FXML private TableColumn<Report, String> codVerificaCol;
	@FXML private TableColumn<Report, String> codCategoriaCol;
	@FXML private TableColumn<Report, String> statoCol;
	@FXML private TableColumn<Report, String> completeCol;
	@FXML private Label sedeLabel, dataLabel, codVerLabel, descrVerLabel, codCatLabel, descrCatLabel;
	@FXML private Text note;
	@FXML private Button modNoteBtn;
	@FXML private MenuBar menuBar;
	@FXML private Menu usernameMenu;
	@FXML private Label usernameMenuLbl;
	@FXML private HBox noteHBox;

		
	public void initData(Intervention interv, int state, User user) {
		selectedInterv = interv;
		sedeLabel.setText(selectedInterv.getSede());
		dataLabel.setText(selectedInterv.getDataCreazione());
		codVerLabel.setText(selectedInterv.getCodVerifica());
		descrVerLabel.setText(selectedInterv.getDescrVerifica());
		codCatLabel.setText(selectedInterv.getCodCategoria());
		descrCatLabel.setText(selectedInterv.getDescrCategoria());
		note.setText(selectedInterv.getNote());
		selectedState = state;
		selectedUser = user;
		usernameMenu.setText(selectedUser.getUsername());
		usernameMenuLbl.setText(selectedUser.getUsername());
		usernameMenuLbl.setStyle("-fx-text-fill: #444444;");
	}

	// modifica le note
	public void modNote(ActionEvent event) throws IOException {
		
		modNoteBtn.setVisible(false);
		
		TextArea itNote = new TextArea(note.getText());
		Button annNote = new Button ("Annulla");
		Button salvaNote = new Button ("Salva");
		
		String defaultNote = note.getText();
		note.setText("");
			
		annNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				note.setText(defaultNote);
				note.setVisible(true);
				modNoteBtn.setVisible(true);
				noteHBox.getChildren().remove(itNote);
				noteHBox.getChildren().remove(annNote);
				noteHBox.getChildren().remove(salvaNote);
		
			}
		});
		
		salvaNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {	
				try {
					InterventionDAO.saveNote(selectedInterv.getId(), itNote.getText());
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				note.setText(itNote.getText());
				note.setVisible(true);
				modNoteBtn.setVisible(true);
				noteHBox.getChildren().remove(itNote);
				noteHBox.getChildren().remove(annNote);
				noteHBox.getChildren().remove(salvaNote);
		
			}
		});
		
		noteHBox.getChildren().add(itNote);
		noteHBox.getChildren().add(annNote);
		noteHBox.getChildren().add(salvaNote);
	}

	@FXML
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/intervention.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);
		InterventionController controller = loader.getController();
		controller.searchSelectedState(selectedState);
		controller.initData(selectedUser);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
	
		idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		descrVerificaCol.setCellValueFactory(cellData -> cellData.getValue().descrVerificaProperty());
		codVerificaCol.setCellValueFactory(cellData -> cellData.getValue().codVerificaProperty());
		codCategoriaCol.setCellValueFactory(cellData -> cellData.getValue().codCategoriaProperty());
		statoCol.setCellValueFactory(new PropertyValueFactory<Report, String>("statoLbl"));
		completeCol.setCellValueFactory(new PropertyValueFactory<Report, String>("completeRep"));
		
		idCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.14));
		descrVerificaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.32));
		codVerificaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.15));
		codCategoriaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.14));
		statoCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.13));
		completeCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.1));

		searchReports();

		setCompleteAndState();
		setCellHeight();
			
	}

	@FXML
	private void populateReports(ObservableList<Report> reportData) throws ClassNotFoundException {
		reportTable.setItems(reportData);
	}
	
	@FXML
	private void setCompleteAndState() {
		for (Object item : reportTable.getItems()) {
			if (((Report) item).getStatoLbl() instanceof Label) {
				String stateText = ((Report) item).getStatoLbl().getText();
				((Report) item).getStatoLbl().setText(stateText.toUpperCase());
				if (stateText == "Completo")
					((Report) item).getStatoLbl().getStyleClass().add("completo");
				else if (stateText == "In lavorazione") 
					((Report) item).getStatoLbl().getStyleClass().add("inLavorazione");
				else ((Report) item).getStatoLbl().getStyleClass().add("daCompilare");
			}
			if (((Report) item).getCompleteRep() instanceof Button) {
				((Report) item).getCompleteRep().getStyleClass().add("completaVerbale");
				((Report) item).getCompleteRep().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {

						long repId = ((Report) item).getId();
						Report.setReportId(repId);

						try {
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/questionnaire.fxml"));
							Parent tableViewParent = loader.load();
							Scene tableViewScene = new Scene(tableViewParent);

							QuestionnaireController controller = loader.getController();
							controller.setData(selectedInterv, selectedState, selectedUser);
							controller.createQuest();

							Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
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
	
	// imposta il testo a capo nelle righe della tabella
	public void setCellHeight () {
		List<TableColumn<Report, String>> list = new ArrayList<>();
		list.add(descrVerificaCol);list.add(codVerificaCol);list.add(codCategoriaCol);
		for(TableColumn<Report, String> col : list) {
		   col.setCellFactory(new Callback<TableColumn<Report, String>, TableCell<Report, String>>() {
		        @Override
		        public TableCell<Report, String> call(TableColumn<Report, String> param) {
		            TableCell<Report, String> cell = new TableCell<>();
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
	
	public void logout(ActionEvent event) throws ClassNotFoundException, SQLException {
		config c = new config();
		c.logout(menuBar);
}
	
}
