package it.ncsnetwork.EciDesktop.model;

import java.time.LocalDate;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class Report {
	
	private SimpleIntegerProperty idRep;
	private SimpleStringProperty nameRep;
	private ImageView state;
	private ObjectProperty<LocalDate> date;
	private SimpleStringProperty detailRep;
	private Button completeRep;
	
	static int reportId;
	
	public Report () {
		this.idRep = new SimpleIntegerProperty();
        this.nameRep = new SimpleStringProperty();
        this.state = new ImageView("/it/ncsnetwork/EciDesktop/img/0.png");
        this.date = new SimpleObjectProperty<LocalDate>();
        this.detailRep = new SimpleStringProperty();
        this.completeRep = new Button("Completa");
	}
	
	//id
	public int getIdRep() {
		return idRep.get();
	}
	
	public void setIdRep(int idR) {
		this.idRep.set(idR);
	}
	
	public IntegerProperty idRepProperty() {
		return idRep;
	}

	//nome
	public String getNameRep() {
		return nameRep.get();
	}
	
	public void setNameRep(String nameR) {
		this.nameRep.set(nameR);
	}
	
	public StringProperty nameRepProperty() {
		return nameRep;
	}
	
	//dettagli
	public String getDetailRep() {
		return detailRep.get();
	}
	
	public void setDetailRep(String nameR) {
		this.detailRep.set(nameR);
	}
	
	public StringProperty detailRepProperty() {
		return detailRep;
	}
	
	//data
	public LocalDate getDate() {
		return date.get();
	}

	public void setDate(LocalDate d) {
		this.date.set(d);
	}

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    
    //image  
    public ImageView getState() {
    	return state;
    }
    
    public void setState(int i) {
    	if (i == 1) this.state = new ImageView(new Image("/it/ncsnetwork/EciDesktop/img/1.png"));
    	else if (i == 2) this.state = new ImageView(new Image("/it/ncsnetwork/EciDesktop/img/2.png"));	
    }
    
    //completeReport
    public Button getCompleteRep() {
    	return completeRep;
    }
    
	public void setCompleteRep(Button button) {
    	this.completeRep = button;
    }
	
	public void setNullCompleteRep() {
    	this.completeRep = null;
    }

	// reportId
    public static int getReportId() {
    	return reportId;
    }
    
    public static void setReportId(int i) {
    	reportId = i;
    }

}
