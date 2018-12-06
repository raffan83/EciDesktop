package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

public class Colonna {

	private long id;
	private ArrayList<Risposta> risposte;
	
	public Colonna() {
		
	}
	
	
	public ArrayList<Risposta> getRisposte() {
		return risposte;
	}
	public void setRisposte(ArrayList<Risposta> risposte) {
		this.risposte = risposte;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
