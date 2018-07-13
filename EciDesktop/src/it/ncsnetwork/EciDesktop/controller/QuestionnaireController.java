package it.ncsnetwork.EciDesktop.controller;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Questionnaire;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuestionnaireController {

	private Intervention selectedInterv;
	private int selectedState;

	List<String> list = new ArrayList<>();
	
	@FXML private Label reportId;
	@FXML VBox reportBox;
	@FXML TextField input1, input2, output;
	@FXML Label opErr;
	@FXML Text quest;
	@FXML TextField answer;
	@FXML HBox choiceHBox;
	
	@FXML private String user;
	@FXML private MenuBar menuBar;
	@FXML private Menu usernameMenu;
	@FXML private Label usernameMenuLbl;

	@FXML
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);

		// ricarica le info dell'intervento sulla pagina verbali
		ReportController controller = loader.getController();
		controller.initData(selectedInterv, selectedState, user);
		
		// aggiorno lo stato dell'intervento sul db
		InterventionDAO.setState();

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
	}

	// salva le info dell'intervento per rimetterle sulla pagina verbali
	public void setData(Intervention interv, int state, String username) {
		selectedInterv = interv;
		selectedState = state;
		user = username;
		usernameMenu.setText(user);
		usernameMenuLbl.setText(user);
		usernameMenuLbl.setStyle("-fx-text-fill: #444444;");
	}

	//metodo da chiamare per caricare la domanda
	public void loadFxml(String template) throws IOException {
		Pane newLoadedPane = FXMLLoader.load(getClass().getResource(template));
		reportBox.getChildren().add(newLoadedPane);
	}
	
	public VBox createTemplateQuestion() {
		reportBox.setPadding(new Insets (20, 50, 20, 50));
		Pane pane = new Pane();
		pane.setPadding(new Insets (10, 15, 10, 15));
		reportBox.getChildren().add(pane);
		VBox vb = new VBox();
		vb.setSpacing(10);
		pane.getChildren().add(vb);
		
		return vb;
	}
	public void loadOpenQuestion(String question) {
		Text t = new Text();
		t.setText(question);
		t.setId("question");
		createTemplateQuestion().getChildren().add(t);
		TextArea ta = new TextArea();
		ta.setPrefHeight(40);
		createTemplateQuestion().getChildren().add(ta);
	}
	
	public void loadOperation(String question) {
		Text t = new Text();
		t.setText(question);
		t.setId("question");
		createTemplateQuestion().getChildren().add(t);
		HBox hb = new HBox();
		hb.setSpacing(20);
		Label opErr = new Label();
		createTemplateQuestion().getChildren().add(hb);
		createTemplateQuestion().getChildren().add(opErr);
		TextField input1 = new TextField();
		TextField input2 = new TextField();
		TextField output = new TextField();
		
		hb.getChildren().add(input1);
		hb.getChildren().add(input2);
		hb.getChildren().add(output);

		if (input1.getText().isEmpty() || input2.getText().isEmpty()) {
			output.setText("");
		} else {
			try {
				double out = Double.parseDouble(input1.getText()) * Double.parseDouble(input2.getText());
				String outs = Double.toString(out);
				opErr.setText("");
				output.setText(outs);
			} catch (Exception e) {
				output.setText("err");
				opErr.setText("Inserisci valori numerici!");
			}
		}
	}
	
	public void loadRadioButton(String question, List<String> list) throws IOException {
		Text t = new Text();
		t.setText(question);
		t.setId("question");
		createTemplateQuestion().getChildren().add(t);
		
		HBox hb = new HBox();
		hb.setSpacing(20);
		createTemplateQuestion().getChildren().add(hb);
		
		ToggleGroup group = new ToggleGroup();	
		for (int i = 0; i < list.size(); i++) {
			RadioButton rb = new RadioButton(list.get(i));
			rb.setToggleGroup(group);
			hb.getChildren().add(rb);
		}
	}
	
	public void loadCheckBox(String question, List<String> list) throws IOException {
		Text t = new Text();
		t.setText(question);
		t.setId("question");
		createTemplateQuestion().getChildren().add(t);
		HBox hb = new HBox();
		hb.setSpacing(20);
		createTemplateQuestion().getChildren().add(hb);
	
		for (int i = 0; i < list.size(); i++) {
			CheckBox cb = new CheckBox(list.get(i));
			hb.getChildren().add(cb);
		}
	}

	public void createQuest() throws IOException {
		loadFxml(Questionnaire.openQuestion);
		loadFxml(Questionnaire.oneChoice);
		loadFxml(Questionnaire.multiChoice);
		loadFxml("/it/ncsnetwork/EciDesktop/view/template/quest.fxml");
		loadFxml(Questionnaire.multiplication);
		loadRadioButton("Domanda", list);
		loadRadioButton("Domanda", list);
		loadCheckBox("Domanda", list);
		loadOpenQuestion("Domanda");
		
	}
	
	public void initialize() throws IOException {
		//String id = String.valueOf(Report.getReportId());
		//reportId.setText(id);
		
		list.add("ciao");
		list.add("ciao2");
		list.add("ciao3");
		list.add("ciao4");

	}

	public void saveReport() throws IOException, ClassNotFoundException, SQLException {
		System.out.print(getQuestionnaire());
		ReportDAO.changeState(1);
	}

	public void completeReport(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		System.out.print(getQuestionnaire());
		ReportDAO.changeState(2);
		goBack(event);
	}

	public Map<String, Questionnaire> getQuestionnaire() {

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

		/*
		 * System.out.println(questMap); System.out.println(questMap.values());
		 * 
		 * System.out.println("Domanda: "+ questMap.get("quest1").getQuestion());
		 * System.out.println("Risposta: " + questMap.get("quest1").getOpenAnswer());
		 * 
		 * System.out.println("Domanda: "+ questMap.get("quest2").getQuestion());
		 * System.out.println("Risposta: " + questMap.get("quest2").getCheckBox());
		 * 
		 * System.out.println("Domanda: "+ questMap.get("quest3").getQuestion());
		 * System.out.println("Risposta: " + questMap.get("quest3").getCheckBox());
		 * 
		 * System.out.println("Domanda: "+ questMap.get("quest4").getQuestion());
		 * System.out.println("Risposta: " + questMap.get("quest4").getOutput());
		 */

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
	
	public void doMultiplication() {
		if (input1.getText().isEmpty() || input2.getText().isEmpty()) {
			output.setText("");
		} else {
			try {
				double out = Double.parseDouble(input1.getText()) * Double.parseDouble(input2.getText());
				String outs = Double.toString(out);
				opErr.setText("");
				output.setText(outs);
			} catch (Exception e) {
				output.setText("err");
				opErr.setText("Inserisci valori numerici!");
			}
		}
	}
	
	public void getCheckBox(List<String> list) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			CheckBox cb = new CheckBox(list.get(i));
			cb.setId("check" + i);
			choiceHBox.getChildren().add(cb);
		}
	}
	
	public void logout(ActionEvent event) throws ClassNotFoundException {
		config c = new config();
		c.logout(menuBar);
	}

}
