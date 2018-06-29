package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Intervention {
	private SimpleIntegerProperty id;
	private SimpleStringProperty sede;
	private SimpleStringProperty dataCreazione;
	private String stato;
	private SimpleStringProperty codCategoria;
	private SimpleStringProperty descrCategoria;
	private SimpleStringProperty codVerifica;
	private SimpleStringProperty descrVerifica;
	private Button detailBtn;
	
	static int intervId;
	
	public Intervention() {
        this.id = new SimpleIntegerProperty();
        this.sede = new SimpleStringProperty();
        this.dataCreazione = new SimpleStringProperty();
        this.stato = new String("Da compilare");
        this.codCategoria = new SimpleStringProperty();
        this.descrCategoria = new SimpleStringProperty();
        this.codVerifica = new SimpleStringProperty();
        this.descrVerifica = new SimpleStringProperty();
        this.detailBtn = new Button("Dettagli");
	}
	
	//id
	public int getId() {
		return id.get();
	}
	
	public void setId(int intId) {
		this.id.set(intId);
	}
	
	public IntegerProperty idProperty() {
		return id;
	}

	//sede
	public String getSede() {
		return sede.get();
	}
	
	public void setSede(String sede) {
		this.sede.set(sede);
	}
	
	public StringProperty sedeProperty() {
		return sede;
	}

	//dataCreazione
	public String getDataCreazione() {
		return dataCreazione.get();
	}

	public void setDataCreazione(String d) {
		this.dataCreazione.set(d);
	}

    public StringProperty dataCreazioneProperty() {
        return dataCreazione;
    }
    
    //stato 
	public String getStato() {
		return stato;
	}
    
    public void setStato(int i) {
    	if (i == 1) this.stato = "In lavorazione";
    	else if (i == 2) this.stato = "Completo";	
    }
    
    //codice categoria
	public String getCodCategoria() {
		return codCategoria.get();
	}
	
	public void setCodCategoria(String cod) {
		this.codCategoria.set(cod);
	}
	
	public StringProperty codCategoriaProperty() {
		return codCategoria;
	}
	
    //descrizione categoria
	public String getDescrCategoria() {
		return descrCategoria.get();
	}
	
	public void setDescrCategoria(String descr) {
		this.descrCategoria.set(descr);
	}
	
	public StringProperty descrCategoriaProperty() {
		return descrCategoria;
	}
	
    //codice verifica
	public String getCodVerifica() {
		return codVerifica.get();
	}
	
	public void setCodVerifica(String cod) {
		this.codVerifica.set(cod);
	}
	
	public StringProperty codVerificaProperty() {
		return codVerifica;
	}
	
    //descrizione verifica
	public String getDescrVerifica() {
		return descrVerifica.get();
	}
	
	public void setDescrVerifica(String descr) {
		this.descrVerifica.set(descr);
	}
	
	public StringProperty descrVerificaProperty() {
		return descrVerifica;
	}

    //dettagliButton
    public Button getDetailBtn() {
    	return detailBtn;
    }
    
	public void setDetailBtn(Button button) {
    	this.detailBtn = button;
    }
	
	// intervId
    public static int getIntervId() {
    	return intervId;
    }
    
    public static void setIntervId(int i) {
    	intervId = i;
    }
	
}