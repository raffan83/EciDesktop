package it.ncsnetwork.EciDesktop.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Report {
	public final static String STATO_0 = "Da compilare";
	public final static String STATO_1 = "In lavorazione";
	public final static String STATO_2 = "Completo";
	public final static String STATO_3 = "Inviato";
	static long reportId;
	
	private SimpleLongProperty id;
	private SimpleStringProperty descrVerifica;
	private SimpleStringProperty codVerifica;
	private SimpleStringProperty codCategoria;
	private Label statoLbl;
	private int stato;
	private Button completeRep;
	private Button inviaRep;
	private SimpleLongProperty intervId;
	private boolean scheda_tecnica;
	private SimpleLongProperty verbaleId;
	
	public Report() {
		this.id = new SimpleLongProperty();
		this.descrVerifica = new SimpleStringProperty();
		this.codVerifica = new SimpleStringProperty();
		this.codCategoria = new SimpleStringProperty();
		this.statoLbl = new Label(STATO_0);
		this.completeRep = new Button("");
		this.inviaRep = new Button("");
		this.intervId = new SimpleLongProperty();
		this.verbaleId = new SimpleLongProperty();
	}

	// id
	public long getId() {
		return id.get();
	}

	public void setId(long idR) {
		this.id.set(idR);
	}

	public LongProperty idProperty() {
		return id;
	}

	// descrizione verifica
	public String getDescrVerifica() {
		return descrVerifica.get();
	}

	public void setDescrVerifica(String descr) {
		this.descrVerifica.set(descr);
	}

	public StringProperty descrVerificaProperty() {
		return descrVerifica;
	}
	// codice verifica
	public String getCodVerifica() {
		return codVerifica.get();
	}

	public void setCodVerifica(String cod) {
		this.codVerifica.set(cod);
	}

	public StringProperty codVerificaProperty() {
		return codVerifica;
	}
	// codice categoria
	public String getCodCategoria() {
		return codCategoria.get();
	}

	public void setCodCategoria(String cod) {
		this.codCategoria.set(cod);
	}

	public StringProperty codCategoriaProperty() {
		return codCategoria;
	}

	// label stato
	public Label getStatoLbl() {
		return statoLbl;
	}

	public void setStatoLbl(int i) {
		if (i == 1)
			this.statoLbl.setText(STATO_1);
		else if (i == 2)
			this.statoLbl.setText(STATO_2);
		else if (i == 3)
			this.statoLbl.setText(STATO_3);
	}

	// completeReport
	public Button getCompleteRep() {
		return completeRep;
	}

	public void setCompleteRep(Button button) {
		this.completeRep = button;
	}

	public void setNullCompleteRep() {
		this.completeRep = null;
	}

	// id intervento
	public long getIntervId() {
		return intervId.get();
	}

	public void setIntervId(long id) {
		this.intervId.set(id);
	}

	public LongProperty intervIdProperty() {
		return intervId;
	}
	
	// reportId
	public static long getReportId() {
		return reportId;
	}

	public static void setReportId(long i) {
		reportId = i;
	}
// invia verbale
	public Button getInviaRep() {
		return inviaRep;
	}

	public void setInviaRep(Button inviaRep) {
		this.inviaRep = inviaRep;
	}
	
	public void setNullInviaRep() {
		this.inviaRep = null;
	}

	public boolean isScheda_tecnica() {
		return scheda_tecnica;
	}

	public void setScheda_tecnica(boolean scheda_tecnica) {
		this.scheda_tecnica = scheda_tecnica;
	}

	public long getVerbaleId() {
		return verbaleId.get();
	}

	public void setVerbaleId(long id) {
		this.verbaleId.set(id);
	}

	public LongProperty verbaleIdProperty() {
		return verbaleId;
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

}
