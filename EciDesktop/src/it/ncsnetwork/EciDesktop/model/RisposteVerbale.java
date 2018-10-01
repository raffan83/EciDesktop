package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

public class RisposteVerbale {
	private long verbale_id;
	private ArrayList<Risposta> risposte;
	private ArrayList<EncodedFile> documenti;
	
	public RisposteVerbale() {
		
	}
	
	public long getVerbale_id() {
		return verbale_id;
	}
	public void setVerbale_id(long verbale_id) {
		this.verbale_id = verbale_id;
	}
	
	public ArrayList<Risposta> getRisposte() {
		return risposte;
	}
	public void setRisposte(ArrayList<Risposta> risposte) {
		this.risposte = risposte;
	}
	
	public ArrayList<EncodedFile> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(ArrayList<EncodedFile> documenti) {
		this.documenti = documenti;
	}
}
