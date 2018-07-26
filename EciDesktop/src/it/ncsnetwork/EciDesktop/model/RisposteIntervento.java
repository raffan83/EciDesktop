package it.ncsnetwork.EciDesktop.model;

import java.util.ArrayList;

public class RisposteIntervento {
	private long intervento_id;
	private ArrayList<RisposteVerbale> verbali;
	
	public RisposteIntervento() {
		
	}
	
	public long getIntervento_id() {
		return intervento_id;
	}
	public void setIntervento_id(long intervento_id) {
		this.intervento_id = intervento_id;
	}
	public ArrayList<RisposteVerbale> getRisposteVerbale() {
		return verbali;
	}
	public void setRisposteVerbale(ArrayList<RisposteVerbale> risposteVerbale) {
		this.verbali = risposteVerbale;
	}
}
