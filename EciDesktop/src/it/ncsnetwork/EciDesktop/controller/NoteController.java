package it.ncsnetwork.EciDesktop.controller;

import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.InterventionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class NoteController {
	Intervention selectedInterv;
	
	@FXML TextArea modNote;
	
    public void initData(Intervention interv){
        selectedInterv = interv;
        modNote.setText(selectedInterv.getNote());
    }
    
    public void saveNote(ActionEvent event) throws ClassNotFoundException, SQLException {
    	InterventionDAO.saveNote(selectedInterv.getId(), modNote.getText());
    	//selectedInterv.setNote(modNote.getText());     
    	cancel(event);
    }
    
    public void cancel(ActionEvent event) {
	 	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	    window.close();
    }
	
	@FXML
    private void initialize () {
	
	}
	
}
