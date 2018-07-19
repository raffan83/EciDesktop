package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Risposta {
	private long id;
	private String tipo;
	private SimpleStringProperty testoRisposta;
	private SimpleStringProperty label1;
	private SimpleStringProperty label2;
	private String operatore;
	private SimpleStringProperty labelRisultato;
	
	public Risposta() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTestoRisposta() {
		return testoRisposta.get();
	}

	public void setSede(String testoRisposta) {
		this.testoRisposta.set(testoRisposta);
	}

	public StringProperty testoRispostaProperty() {
		return testoRisposta;
	}
	
	public String getLabel1() {
		return label1.get();
	}

	public void setLabel1(String label1) {
		this.label1.set(label1);
	}

	public StringProperty label1Property() {
		return label1;
	}
	
	public String getLabel2() {
		return label2.get();
	}

	public void setLabel2(String label2) {
		this.label2.set(label2);
	}

	public StringProperty label2Property() {
		return label2;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	
	public String getLabelRisultato() {
		return labelRisultato.get();
	}

	public void setLabelRisultato(String labelRisultato) {
		this.labelRisultato.set(labelRisultato);
	}

	public StringProperty labelRisultatoProperty() {
		return labelRisultato;
	}

}
