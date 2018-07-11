package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

public class Questionnaire {
	public static String openQuestion = "/it/ncsnetwork/EciDesktop/view/template/openQuestion.fxml";
	public static String multiplication = "/it/ncsnetwork/EciDesktop/view/template/multiplication.fxml";
	public static String oneChoice = "/it/ncsnetwork/EciDesktop/view/template/oneChoice.fxml";
	public static String multiChoice = "/it/ncsnetwork/EciDesktop/view/template/multiChoice.fxml";

	private String question;
	private String openAnswer;
	private ArrayList<Boolean> checkBox = new ArrayList<Boolean>();
	private String input1, input2, output;

	public Questionnaire(String quest, String openA) {
		this.question = quest;
		this.openAnswer = openA;
	}

	public Questionnaire(String quest, ArrayList<Boolean> checkB) {
		this.question = quest;
		this.checkBox = checkB;
	}

	public Questionnaire(String quest, String i1, String i2, String out) {
		this.question = quest;
		this.input1 = i1;
		this.input2 = i2;
		this.output = out;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String quest) {
		this.question = quest;
	}

	public String getOpenAnswer() {
		return openAnswer;
	}

	public void setOpenAnswer(String answ) {
		this.openAnswer = answ;
	}

	public ArrayList<Boolean> getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(ArrayList<Boolean> list) {
		this.checkBox = list;
	}

	public String getInput1() {
		return input1;
	}

	public void setInput1(String i) {
		this.input1 = i;
	}

	public String getInput2() {
		return input2;
	}

	public void setInput2(String i) {
		this.input2 = i;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String o) {
		this.output = o;
	}

}