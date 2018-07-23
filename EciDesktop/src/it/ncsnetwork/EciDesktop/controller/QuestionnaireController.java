package it.ncsnetwork.EciDesktop.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.animations.FadeInLeftTransition;
import it.ncsnetwork.EciDesktop.animations.FadeInLeftTransition1;
import it.ncsnetwork.EciDesktop.animations.FadeInRightTransition;
import it.ncsnetwork.EciDesktop.model.Domanda;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Opzione;
import it.ncsnetwork.EciDesktop.model.QuestionarioDAO;
import it.ncsnetwork.EciDesktop.model.Questionnaire;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuestionnaireController {

	private Intervention selectedInterv;
	private int selectedState;
	private User selectedUser;
	private int indice = 0;
	private int iDomandaVuota = 1;
	private ObservableList<Domanda> questionario = FXCollections.observableArrayList();
	
	@FXML private VBox reportBox;
	
	@FXML private Button indietro, avanti;
	@FXML private ComboBox comboBox;
	
	@FXML private String user;
	@FXML private MenuBar menuBar;
	@FXML private Menu usernameMenu;
	@FXML private Label usernameMenuLbl;
	
	// salva le info dell'intervento per rimetterle sulla pagina verbali
	public void setData(Intervention interv, int state, User user) {
		selectedInterv = interv;
		selectedState = state;
		selectedUser = user;
		usernameMenu.setText(selectedUser.getUsername());
		usernameMenuLbl.setText(selectedUser.getUsername());
		usernameMenuLbl.setStyle("-fx-text-fill: #444444;");
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
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);

		// ricarica le info dell'intervento sulla pagina verbali
		ReportController controller = loader.getController();
		controller.initData(selectedInterv, selectedState, selectedUser);
		
		// aggiorno lo stato dell'intervento sul db
		InterventionDAO.setState();

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
		
		return vb;
	}
	// crea la domanda a risposta aperta
	public void loadOpenQuestion(Domanda d) {
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(800);
		t.setWrapText(true);
		if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
		else t.setText(indice + 1 + ". " + d.getTesto());
		createTemplateQuestion().getChildren().add(t);
		
		TextArea ta = new TextArea(r.getTestoRisposta());
		
		// salva sul db la risposta
		ta.focusedProperty().addListener((obs, oldVal, newVal) -> {
			//System.out.println(newVal ? "Focused" : "Unfocused")
		    if (!newVal) {
		    	try {
		    		r.setTestoRisposta(ta.getText());
					QuestionarioDAO.saveResText(r);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		
		ta.setPrefHeight(40);
		createTemplateQuestion().getChildren().add(ta);
	}
	// crea la domanda formula
	public void loadFormula(Domanda d) {
		
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(800);
		t.setWrapText(true);
		if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
		else t.setText(indice + 1 + ". " + d.getTesto());
		createTemplateQuestion().getChildren().add(t);
		HBox hb = new HBox();
		hb.setSpacing(20);
		//Label opErr = new Label();
		createTemplateQuestion().getChildren().add(hb);
		//createTemplateQuestion().getChildren().add(opErr);
		TextField output = new TextField(r.getRisultato());
		TextField input1 = new TextField(r.getInput1());
		// salva sul db la risposta
		input1.focusedProperty().addListener((obs, oldVal, newVal) -> {
		    if (!newVal) {
		    	try {
		    		r.setInput1(input1.getText());
		    		r.setRisultato(output.getText());
					QuestionarioDAO.saveResFormula(r);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		TextField input2 = new TextField(r.getInput2());
		// salva sul db la risposta
		input2.focusedProperty().addListener((obs, oldVal, newVal) -> {
		    if (!newVal) {
		    	try {
		    		r.setInput2(input2.getText());
		    		r.setRisultato(output.getText());
					QuestionarioDAO.saveResFormula(r);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		
		
		// evento per fare la somma
		EventHandler<KeyEvent> somma = new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent event) {
		    	if (input1.getText().isEmpty() || input2.getText().isEmpty()) {
	    			output.setText("");
		    	} else {
	    			try {
	    				double out = Double.parseDouble(input1.getText()) + Double.parseDouble(input2.getText());
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
		// evento per fare la moltiplicazione
		EventHandler<KeyEvent> moltiplicazione = new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent event) {
		    	if (input1.getText().isEmpty() || input2.getText().isEmpty()) {
	    			output.setText("");
		    	} else {
	    			try {
	    				double out = Double.parseDouble(input1.getText()) + Double.parseDouble(input2.getText());
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
		if (r.getOperatore().equals(config.SOMMA)) {
			gridpane.add(new Label(" + "), 1, 1);
			input1.setOnKeyReleased(somma);
			input2.setOnKeyReleased(somma);
		} else if (r.getOperatore().equals(config.MOLTIPLICAZIONE)) {
			gridpane.add(new Label(" X "), 1, 1);
			input1.setOnKeyReleased(moltiplicazione);
			input2.setOnKeyReleased(moltiplicazione);
		}
		
		hb.getChildren().add(gridpane);
	}
	// crea la domanda a scelta singola
	public void loadRadioButton(Domanda d) throws IOException {
		
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(800);
		t.setWrapText(true);
		if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
		else t.setText(indice + 1 + ". " + d.getTesto());
		//t.setId("question");
		createTemplateQuestion().getChildren().add(t);
		
		HBox hb = new HBox();
		hb.setSpacing(20);
		createTemplateQuestion().getChildren().add(hb);
		
		ToggleGroup group = new ToggleGroup();
		for (Opzione o: r.getOpzioni()) {
			RadioButton rb = new RadioButton(o.getTesto());
			rb.setToggleGroup(group);
			rb.setSelected(o.isChecked());
			//System.out.println(o.isChecked());
			// salva sul db la risposta
			rb.focusedProperty().addListener((obs, oldVal, newVal) -> {
			    if (!newVal) {
			    	try {
			    		o.setChecked(rb.isSelected());
			    		QuestionarioDAO.resetChoice(o, r.getId());
						QuestionarioDAO.saveResChoice(o);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
			    }
			});
			String id = Double.toString(o.getId());
			rb.setId(id);
			hb.getChildren().add(rb);
		}
	}
	//cra la domanda a scelta multipla
	public void loadCheckBox(Domanda d) throws IOException {
		
		Risposta r = d.getRisposta();
		Label t = new Label();
		t.setMaxWidth(800);
		t.setWrapText(true);
		if (d.isObbligatoria()) t.setText(indice + 1 + ". " + d.getTesto() + " (Domanda obbligatoria)");
		else t.setText(indice + 1 + ". " + d.getTesto());
		createTemplateQuestion().getChildren().add(t);
		
		HBox hb = new HBox();
		hb.setSpacing(20);
		createTemplateQuestion().getChildren().add(hb);
	
		for (Opzione o: r.getOpzioni()) {
			CheckBox cb = new CheckBox(o.getTesto());
			cb.setSelected(o.isChecked());
			// salva sul db la risposta
			cb.focusedProperty().addListener((obs, oldVal, newVal) -> {
			    if (!newVal) {
			    	try {
			    		o.setChecked(cb.isSelected());
						QuestionarioDAO.saveResChoice(o);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
			    }
			});
			String id = Double.toString(o.getId());
			cb.setId(id);
			hb.getChildren().add(cb);
		}
	}
	//carica il questionario tutto insieme
	public void createQuest() throws IOException {
		for (Domanda d: questionario) {
			Risposta risposta = d.getRisposta();
			if (risposta.getTipo().equals(config.RES_TEXT)) {
				loadOpenQuestion(d);
			} else if (risposta.getTipo().equals(config.RES_FORMULA)) {
				loadFormula(d);
			} else if (risposta.getTipo().equals(config.RES_CHOICE)) {
				if (risposta.isMultipla()) {
					loadCheckBox(d);
				} else {
					loadRadioButton(d);
				}
			}
		}			
	}
	
	// carica la domanda secondo l'indice
	public void loadQuestion() throws IOException {
		try {
			searchDomande();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		Domanda d = questionario.get(indice);
		Risposta risposta = d.getRisposta();
		if (risposta.getTipo().equals(config.RES_TEXT)) {
			loadOpenQuestion(d);
		} else if (risposta.getTipo().equals(config.RES_FORMULA)) {
			loadFormula(d);
		} else if (risposta.getTipo().equals(config.RES_CHOICE)) {
			if (risposta.isMultipla()) {
				loadCheckBox(d);
			} else {
				loadRadioButton(d);
			}
		}
	}
	
	private boolean isCompleto() throws IOException {
		iDomandaVuota = 1;
		for (Domanda d: questionario) {
			if (d.isObbligatoria()) {
				Risposta risposta = d.getRisposta();
				if (risposta.getTipo().equals(config.RES_TEXT)) {
					String risp = risposta.getTestoRisposta();
					if(risp.isEmpty()) return false;

				} else if (risposta.getTipo().equals(config.RES_FORMULA)) {
					String ris = risposta.getRisultato();
					if(ris.isEmpty() || ris.equals("err")) return false;
					
				} else if (risposta.getTipo().equals(config.RES_CHOICE)) {
					boolean compl = false;
					for (Opzione o : risposta.getOpzioni()) {
						if (o.isChecked()) compl = true;
					}
					if (!compl) return false;
				}
			}
			iDomandaVuota++;
		}
		return true;
	}
		
	
	
	public void initialize() throws IOException, ClassNotFoundException, SQLException {	
		//searchDomande();
		loadQuestion();
		/*for (Domanda d : questionario) {
			completo.add(!d.isObbligatoria());
		}*/
		indietro.setVisible(false);
        comboBox.getItems().addAll(comboItems(questionario.size()));
        comboBox.setPromptText("1");
        
	}
	
	private ObservableList<Integer> comboItems(int n){
		ObservableList<Integer> list = FXCollections.observableArrayList();
		for (int i=1; i <= n; i++) {
			list.add(i);
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

		ReportDAO.changeState(1);
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
	}

	public void completeReport(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		if(isCompleto()) {
			ReportDAO.changeState(2);
			goBack(event);
		} else {
			//alert
			config.dialog(AlertType.WARNING, "Impossibile completare il verbale.\n"
					+ "La domanda numero " + iDomandaVuota + " è obbligatoria.");
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
		config c = new config();
		c.logout(menuBar);
	}

}
