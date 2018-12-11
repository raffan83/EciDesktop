package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Opzione {
	private long id;
	private String testo;
	private Long posizione;
	private boolean choice;
	private ArrayList<Risposta> risposte;
	
    @Override
    public String toString() {
        return testo;
    }
	
	public Opzione() {
		//this.testo = new SimpleStringProperty();		
	}
	
	public Opzione(long id) {
		this.id = id;;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo=testo;
	}

	/*public StringProperty testoProperty() {
		return testo;
	}*/

	public long getPosizione() {
		return posizione;
	}

	public void setPosizione(long posizione) {
		this.posizione = posizione;
	}

	public boolean isChecked() {
		return choice;
	}

	public void setChecked(boolean checked) {
		this.choice = checked;
	}

	public ArrayList<Risposta> getRisposte() {
		return risposte;
	}

	public void setRisposte(ArrayList<Risposta> risposte) {
		this.risposte = risposte;
	}
	
}
