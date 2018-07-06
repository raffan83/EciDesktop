package it.ncsnetwork.EciDesktop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TemplateController {

	@FXML TextField input1, input2, output;
	@FXML Label opErr;
	@FXML Text quest;
	@FXML TextField answer;
	@FXML HBox choiceHBox;

	List<String> list = new ArrayList<>();
	
	public TemplateController() {}
	
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
	public void getRadioButton(List<String> list) throws IOException {
		ToggleGroup group = new ToggleGroup();
		for (int i = 0; i < list.size(); i++) {
			RadioButton rb = new RadioButton(list.get(i));
			rb.setToggleGroup(group);
			rb.setId("radio" + i);
			choiceHBox.getChildren().add(rb);
		}
	}
	
	public void getCheckBox(List<String> list) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			CheckBox cb = new CheckBox(list.get(i));
			cb.setId("check" + i);
			choiceHBox.getChildren().add(cb);
		}
	}

	public void getCheckBox(int j) throws IOException {
		for (int i = 0; i < j; i++) {
			CheckBox cb = new CheckBox("CheckBox" + i);
			cb.setId("check" + i);
			choiceHBox.getChildren().add(cb);
		}
	}

}
