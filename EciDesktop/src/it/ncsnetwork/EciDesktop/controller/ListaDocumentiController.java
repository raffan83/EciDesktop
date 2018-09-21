package it.ncsnetwork.EciDesktop.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import it.ncsnetwork.EciDesktop.Utility.config;
import it.ncsnetwork.EciDesktop.model.Documento;
import it.ncsnetwork.EciDesktop.model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
		
		documentiCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.6));
		showCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.2));
		eliminaCol.prefWidthProperty().bind(listaDocumentiTable.widthProperty().multiply(0.2));
		
		searchDocumenti();
		
		setShowAndRemove();
	}

	private void searchDocumenti() throws ClassNotFoundException {
		ObservableList<Documento> listaDocumenti = FXCollections.observableArrayList();
		//List<String> results = new ArrayList<String>();
		File[] files = new File(config.PATH_DOCUMENTI).listFiles();
		
		for (File file : files) {
		    if (file.isFile() && file.getName().startsWith("verbale"+Report.getReportId())) {
		    	Documento doc = new Documento();
		    	doc.setDocumento(file);
		    	doc.setNomeDocumento(file.getName());
		    	listaDocumenti.add(doc);
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
			((Documento) item).getVisualizzaDocumento().setOnAction(new EventHandler<ActionEvent>() {
				@Override
                public void handle(final ActionEvent e) {	

					File file = ((Documento) item).getDocumento();
					if(!Desktop.isDesktopSupported()){
			            System.out.println("Desktop is not supported");
			            return;
			        }
			        
			        Desktop desktop = Desktop.getDesktop();
			        if(file.exists())
						try {
							desktop.open(file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
                }
			});
			
			((Documento) item).getEliminaDocumento().setOnAction(new EventHandler<ActionEvent>() {
				@Override
                public void handle(final ActionEvent e) {	

					File file = ((Documento) item).getDocumento();
					file.delete();
					try {
						searchDocumenti();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
			});
		}
	}
		
}