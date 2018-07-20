package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Risposta {
	private long id;
	private String tipo;
	//RES_TEXT
	private SimpleStringProperty testoRisposta;
	//RES_FORMULA
	private SimpleStringProperty label1;
	private SimpleDoubleProperty input1;
	private SimpleStringProperty label2;
	private SimpleDoubleProperty input2;
	private String operatore;
	private SimpleStringProperty labelRisultato;
	private SimpleDoubleProperty risultato;
	//RES_CHOICE
	private boolean multipla;
	private ArrayList<Opzione> opzioni = new ArrayList<Opzione>();
	
	public Risposta() {
		this.testoRisposta = new SimpleStringProperty();
		this.label1 = new SimpleStringProperty();
		this.input1 = new SimpleDoubleProperty();
		this.label2 = new SimpleStringProperty();
		this.input2 = new SimpleDoubleProperty();
		this.labelRisultato = new SimpleStringProperty();
		this.risultato = new SimpleDoubleProperty();
		
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
	
	public Double getInput1() {
		return input1.get();
	}

	public void setInput1(Double input1) {
		this.input1.set(input1);
	}

	public DoubleProperty input1Property() {
		return input1;
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
	
	public Double getInput2() {
		return input2.get();
	}

	public void setInput2(Double input2) {
		this.input2.set(input2);
	}

	public DoubleProperty input2Property() {
		return input2;
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
	
	public Double getRisultato() {
		return risultato.get();
	}

	public void setRisultato(Double risultato) {
		this.risultato.set(risultato);
	}

	public DoubleProperty risultatoProperty() {
		return risultato;
	}

	public boolean isMultipla() {
		return multipla;
	}

	public void setMultipla(boolean multipla) {
		this.multipla = multipla;
	}

	public ArrayList<Opzione> getOpzioni() {
		return opzioni;
	}

	public void setOpzioni(ArrayList<Opzione> opzioni) {
		this.opzioni = opzioni;
	}

}
