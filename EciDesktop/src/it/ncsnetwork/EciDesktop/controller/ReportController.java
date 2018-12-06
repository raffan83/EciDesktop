package it.ncsnetwork.EciDesktop.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Documento;
import it.ncsnetwork.EciDesktop.model.Domanda;
import it.ncsnetwork.EciDesktop.model.EncodedFile;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.QuestionarioDAO;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import it.ncsnetwork.EciDesktop.model.Risposta;
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
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	@FXML private TableColumn<Report, String> inviaCol;
	@FXML private TableColumn<Report, String> docCol;
	@FXML private TableColumn<Report, String> uploadDocCol;
	@FXML private TableColumn<Report, String> showDocCol;
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

		idCol.setCellValueFactory(cellData -> cellData.getValue().verbaleIdProperty().asObject());
		descrVerificaCol.setCellValueFactory(cellData -> cellData.getValue().descrVerificaProperty());
		codVerificaCol.setCellValueFactory(cellData -> cellData.getValue().codVerificaProperty());
		codCategoriaCol.setCellValueFactory(cellData -> cellData.getValue().codCategoriaProperty());
		statoCol.setCellValueFactory(new PropertyValueFactory<Report, String>("statoLbl"));
		uploadDocCol.setCellValueFactory(new PropertyValueFactory<Report, String>("uploadDoc"));
		showDocCol.setCellValueFactory(new PropertyValueFactory<Report, String>("showDoc"));
		completeCol.setCellValueFactory(new PropertyValueFactory<Report, String>("completeRep"));
		inviaCol.setCellValueFactory(new PropertyValueFactory<Report, String>("inviaRep"));
		
		idCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.12));
		descrVerificaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.3));
		codVerificaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.11));
		codCategoriaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.11));
		statoCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.13));
		docCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.12));
		uploadDocCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.06));
		showDocCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.06));
		completeCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.05));
		inviaCol.prefWidthProperty().bind(reportTable.widthProperty().multiply(0.05));

		// aggiorna stato

		
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
			
			long repId = ((Report) item).getId();

		//	if (((Report) item).getCompleteRep() instanceof Button) {
			
			int stato = ((Report) item).getStato();
			String stateText = ((Report) item).getStatoLbl().getText();
			((Report) item).getStatoLbl().setText(stateText.toUpperCase());
			
			if (stato == 3) {
				((Report) item).getStatoLbl().getStyleClass().add("inviato");
				((Report) item).getCompleteRep().getStyleClass().add("showVerbale");
			} else {
				((Report) item).getCompleteRep().getStyleClass().add("completaVerbale");
			}
	
			if (stato == 2)
				((Report) item).getStatoLbl().getStyleClass().add("completo");
			else if (stato == 1) 
				((Report) item).getStatoLbl().getStyleClass().add("inLavorazione");
			else ((Report) item).getStatoLbl().getStyleClass().add("daCompilare");
				
				
			((Report) item).getCompleteRep().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {

					Report.setReportId(repId);

					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/questionnaire.fxml"));
						Parent tableViewParent = loader.load();
						Scene tableViewScene = new Scene(tableViewParent);

						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						
						QuestionnaireController controller = loader.getController();
						controller.setData(selectedInterv, selectedState, selectedUser, stato, window);
						
						// Imposta cambio pagina con SHIFT + FRECCIA
						tableViewScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
							  @Override
							  public void handle(KeyEvent event){
							    if (config.AVANTI.match(event)) {
									controller.avanti();
							    } else if (config.INDIETRO.match(event)) {
									controller.indietro();
							    }
							  }
							});
						
						window.setScene(tableViewScene);
						window.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		//	}
			// button invia verbale
			if (((Report) item).getInviaRep() instanceof Button) {
				((Report) item).getInviaRep().getStyleClass().add("invia");
				((Report) item).getInviaRep().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {

						Report.setReportId(repId);				
					/*	try {
							// controlla se ha la scheda tecnica
							Report schedaTecnica = ReportDAO.getSchedaTecnica(repId);
							long schedaTecnicaId = schedaTecnica.getId();
							int stato = schedaTecnica.getStato();
							if (schedaTecnicaId != 0) {
								if (stato == 2) {
									//invia scheda tecnica
									sendJson(schedaTecnicaId);
									// invia il verbale
									sendJson(repId);
								}
								else config.dialog(AlertType.WARNING, "Prima di inviare il verbale devi completare la scheda tecnica.");
							} else {
								// invia il verbale
								sendJson(repId);
							}
							
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}*/
						new Thread(() -> {
						    Platform.runLater(()-> {
						    ((Report) item).getInviaRep().getStyleClass().remove("invia");
						    ((Report) item).getInviaRep().getStyleClass().add("load");
						    });
						try {	
							sendJson(repId);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					    Platform.runLater(()-> {
					    	((Report) item).getInviaRep().getStyleClass().remove("load");
					    	((Report) item).getInviaRep().getStyleClass().add("invia");	
					    });}).start();
					}
				});
			}
			
			// action carica documento
			if (((Report) item).getUploadDoc() instanceof Button) {
				((Report) item).getUploadDoc().getStyleClass().add("uploadDoc");
				((Report) item).getUploadDoc().setOnAction(new EventHandler<ActionEvent>() {
					@Override
	                public void handle(final ActionEvent e) {
						Report.setReportId(repId);	
						
						FileChooser fileChooser = new FileChooser();
						File selectedFile = fileChooser.showOpenDialog(null);
	                    if (selectedFile != null) {
	                    	try {                  		
	                		    config.uploadFile(selectedFile, repId);
							} catch (IOException e1) {
								e1.printStackTrace();
							} finally {
								try {
									searchReports();
								} catch (ClassNotFoundException | SQLException e1) {
									e1.printStackTrace();
								}
								setCompleteAndState();
								setCellHeight();
							}
	                    }
	                }
				});
			}
			
			// action visulizza documenti
			if (((Report) item).getShowDoc() instanceof Button) {
				((Report) item).getShowDoc().getStyleClass().add("showDoc");
				((Report) item).getShowDoc().setOnAction(new EventHandler<ActionEvent>() {
					@Override
	                public void handle(final ActionEvent e) {		
						Report.setReportId(repId);
						// new stage
						try {
							Parent root = FXMLLoader.load(getClass().getResource("/it/ncsnetwork/EciDesktop/view/listaDocumenti.fxml"));
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.setScene(scene);
							stage.getIcons().add(new Image("/it/ncsnetwork/EciDesktop/img/logo-eci.jpg"));
							stage.setTitle("Eci spa");
							stage.show();
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
	
	public void sendJson(long idVerbale) throws ClassNotFoundException, SQLException {
		
		//if (config.isConnected()) {
			
			String json = risposteVerbaleJson(idVerbale);
			
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(config.URL_API.concat("verbale"));
	
			Response response = target.request().header("X-ECI-Auth", selectedUser.getAccessToken()).post(Entity.entity(json, MediaType.APPLICATION_JSON));

			System.out.println("Response code: " + response.getStatus());
			
			//cambia stato
			if (response.getStatus() == 200) {
				ReportDAO.setStateInviato(idVerbale);
				searchReports();
				setCompleteAndState();
				setCellHeight();	
			} else if (response.getStatus() == 401){
				Platform.runLater(()-> config.dialogLogout(AlertType.ERROR, "Operazione non autorizzata, effettuare la login.", menuBar));
			} else {
				Platform.runLater(()-> config.dialog(AlertType.ERROR, "Impossibile inviare il verbale."));
			}
			
		//} else {
		///	Platform.runLater(()-> config.dialog(AlertType.WARNING, "Nessuna connessione"));
		//}
	}
	
	public String risposteVerbaleJson(long idVerbale) throws ClassNotFoundException, SQLException {
		
		ObservableList<Domanda> domande = FXCollections.observableArrayList();
		try {
			domande = QuestionarioDAO.searchRisposte(idVerbale);
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
			risposte.setVerbale_id(idVerbale);
			risposte.setRisposte(rispList);
			//documenti
			ArrayList<EncodedFile> docList = searchDocumenti();
			//if (!docList.isEmpty())
				risposte.setDocumenti(docList);
				
			Gson gson = new Gson();
			String json = gson.toJson(risposte);
			System.out.println(json);

			return json;
	}
	
	private ArrayList<EncodedFile> searchDocumenti() {
		ArrayList<EncodedFile> docList = new ArrayList<EncodedFile>();
		String filePath = config.PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+Report.getReportId();
		if(new File(filePath).exists()) {
			File[] files = new File(filePath).listFiles();
			for (File file : files) {
			    if (file.isFile()) {
			    	try {
						String encoded = config.encodeFileToBase64Binary(file);
						EncodedFile doc = new EncodedFile();
						doc.setFileName(file.getName());
						doc.setEncodedFile(encoded);
						docList.add(doc);
					} catch (IOException e) {
						e.printStackTrace();
					}	    	
			    }
			}
		}
		return docList;
	}
	
}
