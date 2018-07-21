package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Opzione {
	private long id;
	private SimpleStringProperty testo;
	private long posizione;
	private boolean checked;
	
	public Opzione() {
		this.testo = new SimpleStringProperty();		
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

	public long getPosizione() {
		return posizione;
	}

	public void setPosizione(long posizione) {
		this.posizione = posizione;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
