package it.ncsnetwork.EciDesktop.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Documento;
import it.ncsnetwork.EciDesktop.model.Intervention;
import it.ncsnetwork.EciDesktop.model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ListaDocumentiController {

	@FXML private TableView listaDocumentiTable;
	@FXML private TableColumn<Documento, String> documentiCol;
	@FXML private TableColumn<Documento, String> showCol;
	@FXML private TableColumn<Documento, String> eliminaCol;
	@FXML private Label idVerbale;
	
	@FXML
	private void initialize() throws ClassNotFoundException {
		String id = Long.toString(Report.getReportId());
		idVerbale.setText(id);
		
		documentiCol.setCellValueFactory(cellData -> cellData.getValue().nomeDocumentoProperty());
		showCol.setCellValueFactory(new PropertyValueFactory<Documento, String>("visualizzaDocumento"));
		eliminaCol.setCellValueFactory(new PropertyValueFactory<Documento, String>("eliminaDocumento"));
		
		documentiCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.7));
		showCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.14));
		eliminaCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.14));
		
		searchDocumenti();
		setShowAndRemove();
	}

	private void searchDocumenti() throws ClassNotFoundException {
		ObservableList<Documento> listaDocumenti = FXCollections.observableArrayList();
		String filePath = config.PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+Report.getReportId();
		if(new File(filePath).exists()) {
			File[] files = new File(filePath).listFiles();
			for (File file : files) {
				 if (file.isFile()) {
			    	Documento doc = new Documento();
			    	doc.setDocumento(file);
			    	doc.setNomeDocumento(file.getName());
			    	listaDocumenti.add(doc);
			    }
			}
		}
		populateListaDocumenti(listaDocumenti);
	}
	
	@FXML
	private void populateListaDocumenti(ObservableList<Documento> listaDocumenti) throws ClassNotFoundException {
		listaDocumentiTable.setItems(listaDocumenti);
	}
	
	@FXML
	private void setShowAndRemove() {
		for (Object item : listaDocumentiTable.getItems()) {
			
			// visualizza il documento
			((Documento) item).getVisualizzaDocumento().setOnAction(new EventHandler<ActionEvent>() {
				@Override
                public void handle(final ActionEvent e) {	

					File file = ((Documento) item).getDocumento();
					if(!Desktop.isDesktopSupported()){
			            System.out.println("Desktop is not supported");
			            return;
			        }
			        
			        Desktop desktop = Desktop.getDesktop();
			        if(file.exists()) {
						try {
							desktop.open(file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			        }
                }
			});
			
			// elimina il docuemnto
			((Documento) item).getEliminaDocumento().setOnAction(new EventHandler<ActionEvent>() {
				@Override
                public void handle(final ActionEvent e) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Sicuro di voler rimuovere il documento dal verbale?");
					alert.initStyle(StageStyle.UTILITY);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						File file = ((Documento) item).getDocumento();
						file.delete();
						deleteEmptyFolder();
						try {
							searchDocumenti();
							setShowAndRemove();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					} else {
					    // ... user chose CANCEL or closed the dialog
					}
                }
			});
			
		}
	}
	
	private void deleteEmptyFolder() {
		File[] files = new File(config.PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+Report.getReportId()).listFiles();
		if (files.length == 0) {
			File verbaleFolder = new File(config.PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+Report.getReportId());
			verbaleFolder.delete();
			
			File[] filesInterv = new File(config.PATH_DOCUMENTI+Intervention.getIntervId()).listFiles();
			if (filesInterv.length == 0) {
				File interventoFolder = new File(config.PATH_DOCUMENTI+Intervention.getIntervId());
				interventoFolder.delete();
			}
		}
	}
		
}