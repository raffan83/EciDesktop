package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InterventionDAO {
		
	public static ObservableList<Intervention> searchInterventions() throws SQLException, ClassNotFoundException {

		int userId = User.getUserId();
        String selectStmt = "SELECT * FROM intervention WHERE user_id = "+userId;
 
        try {
            ResultSet rsInt = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<Intervention> intervList = getInterventionList(rsInt);
            return intervList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
	}

    private static ObservableList<Intervention> getInterventionList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Intervention> intervList = FXCollections.observableArrayList();
 
        while (rs.next()) {
            Intervention interv = new Intervention();
            interv.setId(rs.getInt("id"));
            interv.setSede(rs.getString("sede"));
            interv.setDataCreazione(rs.getString("data_creazione"));
            interv.setStato(rs.getInt("stato"));
            interv.setCodCategoria(rs.getString("codice_categoria"));
            interv.setDescrCategoria(rs.getString("descrizione_categoria"));
            interv.setCodVerifica(rs.getString("codice_verifica"));
            interv.setDescrVerifica(rs.getString("descrizione_verifica"));
            interv.setNote(rs.getString("note"));
      
            intervList.add(interv);
        }
        return intervList;
    }
    
	public static void setState() throws SQLException, ClassNotFoundException {
		int userId = User.getUserId();
        String selectIntervId = "SELECT id FROM intervention WHERE user_id = "+userId;
        ObservableList<Integer> idList = FXCollections.observableArrayList();
        ResultSet rs1 = DBUtil.dbExecuteQuery(selectIntervId);
		while (rs1.next()) {     
            idList.add(rs1.getInt("id"));
		}
		for (int id: idList) {
	        String selectReportState= "SELECT state FROM report WHERE intervention_id = "+id;
			ObservableList<Integer> stateList = FXCollections.observableArrayList();
			ResultSet rs2 = DBUtil.dbExecuteQuery(selectReportState);
			while (rs2.next()) {     
	            stateList.add(rs2.getInt("state"));
			}
			boolean isComplete = true;
			for (int state: stateList) {
				if (state == 1) {
			    	String stmt = "UPDATE intervention SET stato = 1 WHERE id = " + id;
				    DBUtil.dbExecuteUpdate(stmt);
				    isComplete = false;
				    break;		
				}
				if (state != 1 && state != 2 ) {
					isComplete = false;
				}
			}
			if (isComplete) {
				String stmt = "UPDATE intervention SET stato = 2 WHERE id = " + id;
			    DBUtil.dbExecuteUpdate(stmt);
			}
		}
	}
	
	public static void saveNote(int intervId, String note) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE intervention SET note = '" + note + "' WHERE id = " + intervId;
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	}
	
}
