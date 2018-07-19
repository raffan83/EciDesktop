package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Domanda {
	private long id;
	private SimpleStringProperty testo;
	private boolean obbligatoria;
	private long posizione;
	private Risposta risposta;
	
	public Domanda() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTesto() {
		return testo.get();
	}

	public void setTesto(String testo) {
		this.testo.set(testo);
	}

	public StringProperty testoProperty() {
		return testo;
	}

	public boolean isObbligatoria() {
		return obbligatoria;
	}

	public void setObbligatoria(boolean obbligatoria) {
		this.obbligatoria = obbligatoria;
	}

	public long getPosizione() {
		return posizione;
	}

	public void setPosizione(long posizione) {
		this.posizione = posizione;
	}

	public Risposta getRisposta() {
		return risposta;
	}

	public void setRisposta(Risposta risposta) {
		this.risposta = risposta;
	}
}
