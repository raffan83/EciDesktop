package it.ncsnetwork.EciDesktop.model;

import java.time.LocalDate;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class Report {

	private SimpleLongProperty id;
	private SimpleStringProperty descrVerifica;
	private SimpleStringProperty codVerifica;
	private SimpleStringProperty codCategoria;
	private Label statoLbl;
	private Button completeRep;
	private SimpleLongProperty intervId;

	static long reportId;

	public Report() {
		this.id = new SimpleLongProperty();
		this.descrVerifica = new SimpleStringProperty();
		this.codVerifica = new SimpleStringProperty();
		this.codCategoria = new SimpleStringProperty();
		this.statoLbl = new Label("Da compilare");
		this.completeRep = new Button("");
		this.intervId = new SimpleLongProperty();
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
			this.statoLbl.setText("In lavorazione");
		else if (i == 2)
			this.statoLbl.setText("Completo");
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

}
