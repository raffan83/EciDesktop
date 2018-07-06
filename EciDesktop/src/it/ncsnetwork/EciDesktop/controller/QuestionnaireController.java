package it.ncsnetwork.EciDesktop.controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import it.ncsnetwork.EciDesktop.model.Questionnaire;
import it.ncsnetwork.EciDesktop.model.Report;
import it.ncsnetwork.EciDesktop.model.ReportDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

	@FXML
	public void goBack(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/ncsnetwork/EciDesktop/view/report.fxml"));
		Parent tableViewParent = loader.load();
		Scene tableViewScene = new Scene(tableViewParent);

		// ricarica le info dell'intervento sulla pagina verbali
		ReportController controller = loader.getController();
		controller.initData(selectedInterv, selectedState);
		
		// aggiorno lo stato dell'intervento sul db
		InterventionDAO.setState();

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setTitle("Verbali");
		window.setScene(tableViewScene);
		window.show();
	}

	// salva le info dell'intervento per rimetterle sulla pagina verbali
	public void setData(Intervention interv, int state) {
		selectedInterv = interv;
		selectedState = state;
	}

	//metodo da chiamare per caricare la domanda
	public void loadFxml(String template) throws IOException {
		Pane newLoadedPane = FXMLLoader.load(getClass().getResource(template));
		reportBox.getChildren().add(newLoadedPane);
	}

	public void initialize() throws IOException {
		String id = String.valueOf(Report.getReportId());
		reportId.setText(id);
		
		list.add("ciao");
		list.add("ciao2");
		list.add("ciao3");
		list.add("ciao4");
		
		TemplateController tc = new TemplateController();
		tc.getRadioButton(list);

		loadFxml(Questionnaire.openQuestion);
		loadFxml(Questionnaire.oneChoice);
		loadFxml(Questionnaire.multiChoice);
		loadFxml(Questionnaire.multiplication);

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

}
