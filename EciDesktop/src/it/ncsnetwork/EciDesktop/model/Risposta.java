package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

public class Risposta {
	
	public static final String RES_TEXT = "RES_TEXT";
	public static final String RES_FORMULA = "RES_FORMULA";
	public static final String RES_CHOICE = "RES_CHOICE";
	public static final String SOMMA = "Somma";
	public static final String MOLTIPLICAZIONE = "Moltiplicazione";
	public static final String DIVISIONE = "Divisione";
	public static final String SOTTRAZIONE = "Sottrazione";
	public static final String POTENZA = "Potenza";
	
	private long id;
	private String type;
	//RES_TEXT
	private String valore;
	//RES_FORMULA
	private String label1;
	private String valore_1;
	private String label2;
	private String valore_2;
	private String operatore;
	private String labelRisultato;
	private String risultato;
	//RES_CHOICE
	private Boolean multipla;
	private ArrayList<Opzione> scelte;
	
	public Risposta() {
		
		/*this.testoRisposta = new SimpleStringProperty();
		this.label1 = new SimpleStringProperty();
		this.input1 = new SimpleStringProperty();
		this.label2 = new SimpleStringProperty();
		this.input2 = new SimpleStringProperty();
		this.labelRisultato = new SimpleStringProperty();
		this.risultato = new SimpleStringProperty();*/	
	}
	
	public Risposta(long id, ArrayList<Opzione> opzioni) {
		this.id = id;
		this.scelte = opzioni;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getTipo() {
		return type;
	}

	public void setTipo(String tipo) {
		this.type = tipo;
	}
	
	public String getTestoRisposta() {
		return valore;
	}

	public void setTestoRisposta(String testoRisposta) {
		this.valore=testoRisposta;
	}

	/*public StringProperty testoRispostaProperty() {
		return testoRisposta;
	}*/
	
	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label1) {
		this.label1 = label1;
	}

	/*public StringProperty label1Property() {
		return label1;
	}*/
	
	public String getInput1() {
		return valore_1;
	}

	public void setInput1(String input1) {
		this.valore_1=input1;
	}

	/*public StringProperty input1Property() {
		return input1;
	}*/
	
	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2=label2;
	}

	/*public StringProperty label2Property() {
		return label2;
	}*/
	
	public String getInput2() {
		return valore_2;
	}

	public void setInput2(String input2) {
		this.valore_2=input2;
	}

	/*public StringProperty input2Property() {
		return input2;
	}*/

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	
	public String getLabelRisultato() {
		return labelRisultato;
	}

	public void setLabelRisultato(String labelRisultato) {
		this.labelRisultato=labelRisultato;
	}

	/*public StringProperty labelRisultatoProperty() {
		return labelRisultato;
	}*/
	
	public String getRisultato() {
		return risultato;
	}

	public void setRisultato(String risultato) {
		this.risultato=risultato;
	}

	/*public StringProperty risultatoProperty() {
		return risultato;
	}*/

	public boolean isMultipla() {
		return multipla;
	}

	public void setMultipla(boolean multipla) {
		this.multipla = multipla;
	}

	public ArrayList<Opzione> getOpzioni() {
		return scelte;
	}

	public void setOpzioni(ArrayList<Opzione> opzioni) {
		this.scelte = opzioni;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}
