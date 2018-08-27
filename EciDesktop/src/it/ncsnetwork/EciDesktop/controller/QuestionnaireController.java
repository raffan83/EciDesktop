package it.ncsnetwork.EciDesktop.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.animations.FadeInRightTransition;
import it.ncsnetwork.EciDesktop.model.Domanda;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.Opzione;
import it.ncsnetwork.EciDesktop.model.QuestionarioDAO;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import it.ncsnetwork.EciDesktop.model.Risposta;
import it.ncsnetwork.EciDesktop.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

import javafx.scene.control.*;

import javafx.util.Callback;

public class QuestionnaireController {

	private Intervention selectedInterv;
	private int selectedState;
	private User selectedUser;
	private int indice = 0;
	private int iDomandaVuota = 1;
	private ObservableList<Domanda> questionario = FXCollections.observableArrayList();
	private ObservableList<Domanda> domandeAnnidate = FXCollections.observableArrayList();
	private boolean solaLettura;
	private int stato;
	
	@FXML private VBox reportBox;
	@FXML private Pane paneAnn = new Pane();
	@FXML private Label stepX, stepN;
	@FXML private Button indietro, avanti, completa;
	@FXML private ComboBox<String> comboBox;
	
	@FXML private String user;
	@FXML private MenuBar menuBar;
	@FXML private Menu usernameMenu;
	@FXML private Label usernameMenuLbl;
	
	// salva le info dell'intervento per rimetterle sulla pagina verbali
	public void setData(Intervention interv, int selState, User user, int s, Stage stage) {
		selectedInterv = interv;
		selectedState = selState;
		selectedUser = user;
		usernameMenu.setText(selectedUser.getUsername());
		usernameMenuLbl.setText(selectedUser.getUsername());
		usernameMenuLbl.setStyle("-fx-text-fill: #444444;");
		stato = s;
		// se il verbale è completo e si cancella una risposta obbligatoria il verbale torna in lavorazione
		stage.setOnCloseRequest(event ->
		{
			try {
				if(stato == 2 && !isCompleto()) {
					ReportDAO.changeState(1);
				}
			} catch (ClassNotFoundException | IOException | SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	@FXML
	private void searchDomande() throws SQLException, ClassNotFoundException {
		try {
			ObservableList<Domanda> quest = QuestionarioDAO.searchDomande();
			questionario = quest;
		} catch (SQLException e) {
			System.out.println("Error occurred while getting questions information from DB.\n" + e);
			throw e;
		}
	}
	
	@FXML
	private void searchDomandeAnnidate(long idOpzione) throws SQLException, ClassNotFoundException {
		try {
			ObservableList<Domanda> quest = QuestionarioDAO.searchDomandeAnnidate(idOpzione);
			domandeAnnidate = quest;
		} catch (SQLException e) {
			System.out.println("Error occurred while getting questions information from DB.\n" + e);
			throw e;
		}
	}

	@FXML
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		// se il verbale è completo e si cancella una risposta obbligatoria il verbale torna in lavorazione
		if(!isCompleto()) {
			ReportDAO.changeState(1);
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);

		// ricarica le info dell'intervento sulla pagina verbali
		ReportController controller = loader.getController();
		controller.initData(selectedInterv, selectedState, selectedUser);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
	}

	//metodo da chiamare per caricare la domanda
	/*public void loadFxml(String template) throws IOException {
		Pane newLoadedPane = FXMLLoader.load(getClass().getResource(template));
		reportBox.getChildren().add(newLoadedPane);
	}*/
	
	// crea template
	public VBox createTemplateQuestion() {
		Platform.runLater(() -> {
			new FadeInRightTransition(reportBox).play();
		});
		reportBox.setPadding(new Insets (20, 50, 20, 50));
		Pane pane = new Pane();
		pane.setPadding(new Insets (10, 15, 10, 15));
		reportBox.getChildren().add(pane);
		VBox vb = new VBox();
		vb.setSpacing(10);
		pane.getChildren().add(vb);
		//pane.getChildren().add(domandaAnnidataBox);
		
		return vb;
	}
	
	/*public VBox createTemplateQuestionAnn() {
		Platform.runLater(() -> {
			new FadeInRightTransition(reportBox).play();
		});
		reportBox.setPadding(new Insets (20, 50, 20, 50));
		Pane pane = new Pane();
		pane.setPadding(new Insets (10, 15, 10, 15));
		reportBox.getChildren().add(pane);
		VBox vb = new VBox();
		vb.setSpacing(10);
		pane.getChildren().add(vb);
		
		return vb;
	}*/
	
	// crea la domanda a risposta aperta
	public VBox loadOpenQuestion(Domanda d, boolean annidata) {
		
		VBox vbox = createTemplateQuestion();
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(750);
		t.setWrapText(true);
		
		if (!annidata) {
			if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(indice + 1 + ". " + d.getTesto());
		} else {
			if (d.isObbligatoria()) t.setText(d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(d.getTesto());
		}
		vbox.getChildren().add(t);
		
		TextArea ta = new TextArea(r.getTestoRisposta());
		
		// salva sul db la risposta
		ta.focusedProperty().addListener((obs, oldVal, newVal) -> {
			//System.out.println(newVal ? "Focused" : "Unfocused")
		    if (!newVal) {
		    	try {
		    		String testoRisposta = ta.getText();
		    		System.out.println("TestoRisposta: "+ testoRisposta);
		    		if(testoRisposta != null && !testoRisposta.isEmpty()) {
			    		testoRisposta = testoRisposta.replaceAll("'", "''");
			    		r.setTestoRisposta(testoRisposta);
		    		} else {
		    			r.setTestoRisposta("");
		    		}	    		
					QuestionarioDAO.saveResText(r);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		
		ta.setPrefHeight(120);
		if (solaLettura) ta.setEditable(false);
		vbox.getChildren().add(ta);
		
		return vbox;
		
	}
	// crea la domanda formula
	public VBox loadFormula(Domanda d, boolean annidata) {
		
		VBox vbox = createTemplateQuestion();
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(750);
		t.setWrapText(true);
		
		if (!annidata) {
			if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(indice + 1 + ". " + d.getTesto());
		} else {
			if (d.isObbligatoria()) t.setText(d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(d.getTesto());
		}
		vbox.getChildren().add(t);
		HBox hb = new HBox();
		hb.setSpacing(20);
		//Label opErr = new Label();
		vbox.getChildren().add(hb);
		//createTemplateQuestion().getChildren().add(opErr);
		TextField input1 = new TextField(r.getInput1());
		TextField input2 = new TextField(r.getInput2());
		if (solaLettura) {
			input1.setEditable(false);
			input2.setEditable(false);
		}	
		TextField output = new TextField(r.getRisultato());
		output.setEditable(false);
		
		input1.textProperty().addListener(new ChangeListener<String>() { 
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { 
				if(!newValue.matches("^\\d*\\.?\\d*$")){ 
					input1.setText(oldValue); 
				} 
			} 
		});
		// salva sul db la risposta
		input1.focusedProperty().addListener((obs, oldVal, newVal) -> {
		    if (!newVal) {
		    	try {
		    		if (input1.getText() == null ||
		    				input1.getText().isEmpty() || 
		    				input2.getText() == null || 
		    				input2.getText().isEmpty() ||
		    				output.getText().equals("err")) {
		    			QuestionarioDAO.resetResFormula(r);
		    		} else {
		    			r.setInput1(input1.getText());
		    			r.setInput2(input2.getText());
			    		r.setRisultato(output.getText());
		    			QuestionarioDAO.saveResFormula(r);
		    		}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		if(r.getRisultato() == null || r.getRisultato().isEmpty() || r.getRisultato().equals("err"))
		
		input2.textProperty().addListener(new ChangeListener<String>() { 
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { 
				if(!newValue.matches("^\\d*\\.?\\d*$")){ 
					input2.setText(oldValue); 
				} 
			} 
		});
		// salva sul db la risposta
		input2.focusedProperty().addListener((obs, oldVal, newVal) -> {
		    if (!newVal) {
		    	try {
		    		if (input2.getText() == null ||
		    				input2.getText().isEmpty() ||
		    				input1.getText() == null ||
		    				input1.getText().isEmpty() ||	    				
		    				output.getText().equals("err")) {
		    			QuestionarioDAO.resetResFormula(r);
		    		} else {
		    			r.setInput1(input1.getText());
		    			r.setInput2(input2.getText());
			    		r.setRisultato(output.getText());
		    			QuestionarioDAO.saveResFormula(r);
		    		}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});

		EventHandler<KeyEvent> operazione = new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent event) {
		    	//System.out.println("R" + input2.getText());
		    	if (input1.getText() == null || input2.getText() == null || 
		    			input1.getText().isEmpty() || input2.getText().isEmpty()) {
	    			output.setText("");
		    	} else {
	    			try {
	    				double out = 0;
	    				if (r.getOperatore().equals(Risposta.SOMMA))
	    					out = Double.parseDouble(input1.getText()) + Double.parseDouble(input2.getText());
	    				else if (r.getOperatore().equals(Risposta.MOLTIPLICAZIONE))
	    					out = Double.parseDouble(input1.getText()) * Double.parseDouble(input2.getText());
	    				else if (r.getOperatore().equals(Risposta.SOTTRAZIONE))
	    					out = Double.parseDouble(input1.getText()) - Double.parseDouble(input2.getText());
	    				else if (r.getOperatore().equals(Risposta.DIVISIONE))
	    					out = Double.parseDouble(input1.getText()) / Double.parseDouble(input2.getText());
	    				else if (r.getOperatore().equals(Risposta.POTENZA))
	    					out = Math.pow(Double.parseDouble(input1.getText()), Double.parseDouble(input2.getText()));
	    				String outs = Double.toString(out);
	    				//opErr.setText("");
	    				output.setText(outs);
	    			} catch (Exception e) {
	    				output.setText("err");
	    				//opErr.setText("Inserisci valori numerici!");
	    			}
		    	}
		    }
		};
		
		GridPane gridpane = new GridPane();
		gridpane.add(new Label(r.getLabel1()), 0, 0); // colonna riga
		gridpane.add(new Label(r.getLabel2()), 2, 0);
		gridpane.add(new Label(r.getLabelRisultato()), 4, 0);
		gridpane.add(input1, 0, 1);
		gridpane.add(input2, 2, 1);
		gridpane.add(new Label(" = "), 3, 1);
		gridpane.add(output, 4, 1);			
		if (r.getOperatore().equals(Risposta.SOMMA)) {
			gridpane.add(new Label(" + "), 1, 1);
		} else if (r.getOperatore().equals(Risposta.MOLTIPLICAZIONE)) {
			gridpane.add(new Label(" X "), 1, 1);
		} else if (r.getOperatore().equals(Risposta.SOTTRAZIONE)) {
			gridpane.add(new Label(" - "), 1, 1);
		} else if (r.getOperatore().equals(Risposta.DIVISIONE)) {
			gridpane.add(new Label(" / "), 1, 1);
		} else if (r.getOperatore().equals(Risposta.POTENZA)) {
			gridpane.add(new Label(" ^ "), 1, 1);
		}		
			
		input1.setOnKeyReleased(operazione);
		input2.setOnKeyReleased(operazione);
		hb.getChildren().add(gridpane);
		
		return vbox;
	}
	// crea la domanda a scelta singola
	public VBox loadRadioButton(Domanda d, boolean annidata) throws IOException {
		
		VBox vbox = createTemplateQuestion();
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(750);
		t.setWrapText(true);
		
		if (!annidata) {
			if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(indice + 1 + ". " + d.getTesto());		
		} else {
			if (d.isObbligatoria()) t.setText(d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(d.getTesto());
		}
		vbox.getChildren().add(t);
		
		VBox hb = new VBox();
		hb.setSpacing(20);
		vbox.getChildren().add(hb);
		Pane paneAnn =new Pane();
		vbox.getChildren().add(paneAnn);
		
		ToggleGroup group = new ToggleGroup();
		
		for (Opzione o: r.getOpzioni()) {
			RadioButton rb = new RadioButton(o.getTesto());
			rb.setToggleGroup(group);
			rb.setUserData(o.getId());
			rb.setSelected(o.isChecked());
			if (o.isChecked()) {
				paneAnn.getChildren().add(loadQuestionAnn(o.getId()));
			}
			// salva sul db la risposta
			rb.focusedProperty().addListener((obs, oldVal, newVal) -> {
			    if (!newVal) {
			    	try {
			    		o.setChecked(rb.isSelected());
			    		QuestionarioDAO.resetChoice(r.getId());
						QuestionarioDAO.saveResChoice(o);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
			    }
			});
			String id = Double.toString(o.getId());
			rb.setId(id);
			if (solaLettura) {
				rb.setDisable(true);
				rb.setStyle("-fx-opacity: 1");
			}
			hb.getChildren().add(rb);
		}

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {	
				
		         if (group.getSelectedToggle() != null) {

		             long idOpzione = Long.parseLong(group.getSelectedToggle().getUserData().toString());
		             try {
		            	 searchDomandeAnnidate(idOpzione);
		            	 if (!domandeAnnidate.isEmpty()) {
		            		 paneAnn.getChildren().clear();
		            		 paneAnn.getChildren().add(loadQuestionAnn(idOpzione));
		            	 } else {
		            		 paneAnn.getChildren().clear();
		            	 }
						
					} catch (ClassNotFoundException | SQLException | IOException e) {
						e.printStackTrace();
					}

		         }

		     } 
		});
		
		Button reset = new Button("Reset");
		reset.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
				try {
					QuestionarioDAO.resetChoice(r.getId());
					reportBox.getChildren().clear();
					loadQuestion();
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					e1.printStackTrace();
				}
		    }
		});
		if (!solaLettura)
			hb.getChildren().add(reset);
		
		return vbox;
	}
	//cra la domanda a scelta multipla
	public VBox loadCheckBox(Domanda d, boolean annidata) throws IOException {
		
		VBox vbox = createTemplateQuestion();
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(750);
		t.setWrapText(true);
		
		if (!annidata) {
			if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(indice + 1 + ". " + d.getTesto());			
		} else {
			if (d.isObbligatoria()) t.setText(d.getTesto() + " (Domanda obbligatoria)");
			else t.setText(d.getTesto());
		}
		vbox.getChildren().add(t);
		VBox hb = new VBox();
		hb.setSpacing(20);
		vbox.getChildren().add(hb);
		
		Pane paneAnn = new Pane();
		VBox vboxAnn = new VBox();
		paneAnn.getChildren().add(vboxAnn);
		vbox.getChildren().add(paneAnn);
	
		for (Opzione o: r.getOpzioni()) {
			CheckBox cb = new CheckBox(o.getTesto());
			cb.setSelected(o.isChecked());
			if (o.isChecked()) {
				VBox vb = loadQuestionAnn(o.getId());
				vb.setId("vb"+o.getId());
				vboxAnn.getChildren().add(vb);
			}
			
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			    @Override
			    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			    	//salva sul db
			    	try {
			    		o.setChecked(cb.isSelected());
						QuestionarioDAO.saveResChoice(o);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
			    	//visualizza domande annidate
			    	if (cb.isSelected()) {
			        	try {
			        		VBox vb = loadQuestionAnn(o.getId());
							vb.setId("vb"+o.getId());
							vboxAnn.getChildren().add(vb);
						} catch (IOException e) {
							e.printStackTrace();
						}
			        } else {
			        	Scene scene = reportBox.getScene();
			        	VBox vb = (VBox) scene.lookup("#vb"+o.getId());
			        	vboxAnn.getChildren().remove(vb);
			        }
			    }
			});
			
			// salva sul db la risposta
		/*	cb.focusedProperty().addListener((obs, oldVal, newVal) -> {
			    if (!newVal) {
			    	try {
			    		o.setChecked(cb.isSelected());
						QuestionarioDAO.saveResChoice(o);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
			    }
			});*/
			String id = Double.toString(o.getId());
			cb.setId(id);
			if (solaLettura) {
				cb.setDisable(true);
				cb.setStyle("-fx-opacity: 1");
			}		
			hb.getChildren().add(cb);
		}
		
		return vbox;
	}
	//carica il questionario tutto insieme
/*	public void showQuest() throws IOException {
		reportBox.getChildren().clear();
		for (Domanda d: questionario) {
			Risposta risposta = d.getRisposta();
			if (risposta.getTipo().equals(Risposta.RES_TEXT)) {
				loadOpenQuestion(d, false);
			} else if (risposta.getTipo().equals(Risposta.RES_FORMULA)) {
				loadFormula(d, false);
			} else if (risposta.getTipo().equals(Risposta.RES_CHOICE)) {
				if (risposta.isMultipla()) {
					loadCheckBox(d, false);
				} else {
					loadRadioButton(d, false);
				}
			}
		}			
	}*/
	
	// carica la domanda secondo l'indice
	public void loadQuestion() throws IOException {
		try {
			searchDomande();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		Domanda d = questionario.get(indice);
		Risposta risposta = d.getRisposta();
		if (risposta.getTipo().equals(Risposta.RES_TEXT)) {
			loadOpenQuestion(d, false);
		} else if (risposta.getTipo().equals(Risposta.RES_FORMULA)) {
			loadFormula(d, false);
		} else if (risposta.getTipo().equals(Risposta.RES_CHOICE)) {
			if (risposta.isMultipla()) {
				loadCheckBox(d, false);
			} else {
				loadRadioButton(d, false);
			}
		}
	}
	
	// carica la domanda secondo l'indice
	public VBox loadQuestionAnn(long idOpzione) throws IOException {
		VBox vbox = new VBox();
		try {
			searchDomandeAnnidate(idOpzione);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
		for (Domanda d: domandeAnnidate) {
			Risposta risposta = d.getRisposta();
			if (risposta.getTipo().equals(Risposta.RES_TEXT)) {
				vbox.getChildren().add(loadOpenQuestion(d, true));
			} else if (risposta.getTipo().equals(Risposta.RES_FORMULA)) {
				vbox.getChildren().add(loadFormula(d,true));
			} else if (risposta.getTipo().equals(Risposta.RES_CHOICE)) {
				if (risposta.isMultipla()) {
					vbox.getChildren().add(loadCheckBox(d, true));
				} else {
					vbox.getChildren().add(loadRadioButton(d, true));
				}
			}
		}
		return vbox;
	}
	
	private boolean isCompleto() throws IOException, ClassNotFoundException, SQLException {
		iDomandaVuota = 1;
		for (Domanda d: questionario) {
			Risposta r = d.getRisposta();
			if (d.isObbligatoria()) {				
				if (r.getTipo().equals(Risposta.RES_TEXT)) {
					if(r.getTestoRisposta() == null || r.getTestoRisposta().isEmpty()) return false;

				} else if (r.getTipo().equals(Risposta.RES_FORMULA)) {
					if(r.getRisultato() == null || r.getRisultato().isEmpty() || r.getRisultato().equals("err")) return false;
					
				} else if (r.getTipo().equals(Risposta.RES_CHOICE)) {
					boolean compl = false;
					for (Opzione o : r.getOpzioni()) {
						if (o.isChecked()) {
							searchDomandeAnnidate(o.getId());
							if (isCompletoAnn()) {
								compl = true;
							} else {
								compl = false;
							}
						}
					}
					if (!compl) return false;
				}
			}
			else if (r.getTipo().equals(Risposta.RES_CHOICE)) {
				boolean compl = true;
				for (Opzione o : r.getOpzioni()) {
					if (o.isChecked()) {
						searchDomandeAnnidate(o.getId());
						if (isCompletoAnn()) {
							compl = true;
						} else {
							compl = false;
						}
					}
				}
				if (!compl) return false;				
			}
			iDomandaVuota++;
		}
		return true;
	}
	
	private boolean isCompletoAnn() throws ClassNotFoundException, SQLException {
		if (domandeAnnidate.isEmpty()) return true;
		else {
			for (Domanda dAnn: domandeAnnidate) {
				Risposta r = dAnn.getRisposta();
				if (dAnn.isObbligatoria()) {
					if (r.getTipo().equals(Risposta.RES_TEXT)) {
						if(r.getTestoRisposta() == null || r.getTestoRisposta().isEmpty()) return false;
	
					} else if (r.getTipo().equals(Risposta.RES_FORMULA)) {
						if(r.getRisultato() == null || r.getRisultato().isEmpty() || r.getRisultato().equals("err")) return false;
						
					} else if (r.getTipo().equals(Risposta.RES_CHOICE)) {
						boolean compl = false;
						for (Opzione o : r.getOpzioni()) {
							if (o.isChecked()) {
								searchDomandeAnnidate(o.getId());
								if (isCompletoAnn()) {
									compl = true;
								} else {
									compl = false;
								}
							}
						}
						if (!compl) return false;
					}
				}
				else if (r.getTipo().equals(Risposta.RES_CHOICE)) {
					boolean compl = true;
					for (Opzione o : r.getOpzioni()) {
						if (o.isChecked()) {
							searchDomandeAnnidate(o.getId());
							if (isCompletoAnn()) {
								compl = true;
							} else {
								compl = false;
							}
						}
					}
					if (!compl) return false;				
				}
			}
		}
		return true;
	}
		
	
	
	public void initialize() throws IOException, ClassNotFoundException, SQLException {	

		if (ReportDAO.getStato() == 3) {
			solaLettura = true;
			completa.setVisible(false);
		}
		
		loadQuestion();
		if (questionario.size() <= 1) avanti.setVisible(false);
		indietro.setVisible(false);
        comboBox.getItems().addAll(comboItems(questionario.size()));
        comboBox.setPromptText("1");
        stepX.setText("1");
        String total = Integer.toString(questionario.size());
        stepN.setText(total);
        setColor();
    
	}
	
	private ObservableList<String> comboItems(int n){
		ObservableList<String> list = FXCollections.observableArrayList();
		for (int i=1; i <= n; i++) {
			String s = Integer.toString(i);
			list.add(s);
		}
		return list;
	}
	
	@FXML
	public void avanti(ActionEvent e) throws IOException, ClassNotFoundException, SQLException {
		//cambia domanda
		reportBox.getChildren().clear();
		if (indice == questionario.size()-2) {
        	avanti.setVisible(false);
        	indice++;
        } else {
        	indietro.setVisible(true);
        	indice++;
        }
		String ind = Integer.toString(indice+1);
		comboBox.setValue(ind);	
	}
	
	@FXML
	public void indietro(ActionEvent e) throws IOException {
		//cambia domanda	
		reportBox.getChildren().clear();
        if (indice == 1) {
        	indietro.setVisible(false);
        	indice--;
        } else {
        	avanti.setVisible(true);
        	indice--;
        }
		String ind = Integer.toString(indice+1);
		comboBox.setValue(ind);	
	}
	
	//combo box
	public void selectDomanda(ActionEvent actionEvent) throws IOException {
		String s = comboBox.getValue().toString();
		indice = Integer.parseInt(s) -1;
		reportBox.getChildren().clear();
		loadQuestion();
		if (indice == 0) {
			indietro.setVisible(false);
			avanti.setVisible(true);
		}
		else if (indice == questionario.size()-1) {
			avanti.setVisible(false);
			indietro.setVisible(true);
		}
		else {
			indietro.setVisible(true);
			avanti.setVisible(true);
		}
		stepX.setText(s);
		//imposta il colore
		setColor();
		//cambia stato
		if (stato == 0) {
			try {
				ReportDAO.changeState(1);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} finally {
				stato = 1;
			}
		}
		//if (!solaLettura) ReportDAO.changeState(1);
	}

	public void completeReport(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		if(isCompleto()) {
			ReportDAO.changeState(2);
			goBack(event);
		} else {
			//alert
			config.dialog(AlertType.WARNING, "Impossibile completare il verbale.\n"
					+ "La domanda numero " + iDomandaVuota + " \u00E8 obbligatoria.");
		}

	}

/*	public Map<String, Questionnaire> getQuestionnaire() {

		int questionId = 0;
		String questionText = "";
		Map<String, Questionnaire> questMap = new TreeMap<String, Questionnaire>();
		ArrayList<Boolean> cb = new ArrayList<Boolean>();
		ArrayList<String> op = new ArrayList<String>();

		for (Node node : getAllNodes(reportBox)) {

			// per ogni domanda -->
			if (node instanceof Text && ((Text) node).getId() != null) {
				// aggiungo alla mappa checkbox o radioButton
				if (!cb.isEmpty()) {
					ArrayList<Boolean> list = new ArrayList<Boolean>(cb);
					questMap.put("quest" + questionId, new Questionnaire(questionText, list));
				}
				// svuoto la lista delle checkbox
				cb.clear();

				// aggiungo alla mappa l'operazione
				if (!op.isEmpty()) {
					ArrayList<String> opList = new ArrayList<String>(op);
					questMap.put("quest" + questionId,
							new Questionnaire(questionText, opList.get(0), opList.get(1), opList.get(2)));
				}
				// svuoto la lista delle checkbox
				op.clear();

				questionId++;
				questionText = ((Text) node).getText();
				((Text) node).setId("quest" + questionId);
				// System.out.println(((Text) node).getText() + " id=" + ((Text) node).getId());

			}

			else if (node instanceof TextArea) {
				questMap.put("quest" + questionId, new Questionnaire(questionText, ((TextArea) node).getText()));
			}

			else if (node instanceof TextField) {
				op.add(((TextField) node).getText());
			}

			else if (node instanceof CheckBox) {
				cb.add(((CheckBox) node).isSelected());
			}

			else if (node instanceof RadioButton) {
				cb.add(((RadioButton) node).isSelected());
			}

		} // fine ciclo for

		// aggiungo l'ultima domanda alla mappa
		if (!cb.isEmpty()) {
			ArrayList<Boolean> list = new ArrayList<Boolean>(cb);
			questMap.put("quest" + questionId, new Questionnaire(questionText, list));
		}
		cb.clear();
		if (!op.isEmpty()) {
			ArrayList<String> opList = new ArrayList<String>(op);
			questMap.put("quest" + questionId,
					new Questionnaire(questionText, opList.get(0), opList.get(1), opList.get(2)));
		}
		op.clear();

		
		 //* System.out.println(questMap); System.out.println(questMap.values());
		 //* 
		 //* System.out.println("Domanda: "+ questMap.get("quest1").getQuestion());
		 //* System.out.println("Risposta: " + questMap.get("quest1").getOpenAnswer());
		 //* 
		 //* System.out.println("Domanda: "+ questMap.get("quest2").getQuestion());
		 //* System.out.println("Risposta: " + questMap.get("quest2").getCheckBox());
		 //* 
		 //* System.out.println("Domanda: "+ questMap.get("quest3").getQuestion());
		 //* System.out.println("Risposta: " + questMap.get("quest3").getCheckBox());
		 //* 
		 //* System.out.println("Domanda: "+ questMap.get("quest4").getQuestion());
		 //* System.out.println("Risposta: " + questMap.get("quest4").getOutput());
		 
		return questMap;
	}

	public static ArrayList<Node> getAllNodes(Parent root) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addAllDescendents(root, nodes);
		return nodes;
	}

	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			nodes.add(node);
			if (node instanceof Parent)
				addAllDescendents((Parent) node, nodes);
		}
	}
*/
	public void logout(ActionEvent event) throws ClassNotFoundException, SQLException {
		// se il verbale è completo e si cancella una risposta obbligatoria il verbale torna in lavorazione
		try {
			if(stato == 2 && !isCompleto()) {
				ReportDAO.changeState(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		config c = new config();
		c.logout(menuBar);
	}
	public int getComboBoxColor (String s) {
		int i = Integer.parseInt(s) -1;
		Domanda d = questionario.get(i);
		
			Risposta r = d.getRisposta();
			if (r.getTipo().equals(Risposta.RES_TEXT)) {
				if(r.getTestoRisposta() == null || r.getTestoRisposta().isEmpty()) 
					if (d.isObbligatoria()) return 0;
					else return 1;

			} else if (r.getTipo().equals(Risposta.RES_FORMULA)) {
				if(r.getRisultato() == null || r.getRisultato().isEmpty() || r.getRisultato().equals("err"))
					if (d.isObbligatoria()) return 0;
					else return 1;

			} else if (r.getTipo().equals(Risposta.RES_CHOICE)) {
				boolean compl = false;
				for (Opzione o : r.getOpzioni()) {
					if (o.isChecked()) compl = true;
				}
				if (!compl) 
					if (d.isObbligatoria()) return 0;
					else return 1;
			}
		return 2;
	}
	
	private void setColor() {
		if(!solaLettura) {
			comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
				@Override public ListCell<String> call(ListView<String> param) {
		            final ListCell<String> cell = new ListCell<String>() {
		                //{ super.setPrefWidth(100);}    
		                @Override public void updateItem(String item, boolean empty) {
		                	super.updateItem(item, empty);
	                        if (item != null) {
	                            setText(item);    
	                            if (getComboBoxColor(item)==0) {
	                                setTextFill(Color.RED);
	                            }
	                            else if (getComboBoxColor(item)==2){
	                                setTextFill(Color.GREEN);
	                            }
	                            else {
	                                setTextFill(Color.BLACK);
	                            }
	                        }
	                        else {
	                            setText(null);
	                        }
	                    }
		            };
		            return cell;
				}
			});
		}
	}
	

}
