package it.ncsnetwork.EciDesktop.model;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Documento {
	private File documento;
	private SimpleStringProperty nomeDocumento;
	private Button visualizzaDocumento;
	private Button eliminaDocumento;
	
	public Documento() {
		this.nomeDocumento = new SimpleStringProperty();
		this.eliminaDocumento = new Button("");
		this.visualizzaDocumento = new Button("");
	}
	
	public String getNomeDocumento() {
		return nomeDocumento.get();
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento.set(nomeDocumento);
	}

	public StringProperty nomeDocumentoProperty() {
		return nomeDocumento;
	}
	
	public Button getEliminaDocumento() {
		return eliminaDocumento;
	}

	public void setEliminaDocumento(Button eliminaDocumento) {
		this.eliminaDocumento = eliminaDocumento;
	}

	public File getDocumento() {
		return documento;
	}

	public void setDocumento(File documento) {
		this.documento = documento;
	}

	public Button getVisualizzaDocumento() {
		return visualizzaDocumento;
	}

	public void setVisualizzaDocumento(Button visualizzaDocumento) {
		this.visualizzaDocumento = visualizzaDocumento;
	}
}
