package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InterventionDAO {

	static int userId = User.getUserId();
	
	public static ObservableList<Intervention> searchInterventions() throws SQLException, ClassNotFoundException {

		String selectStmt = "SELECT * FROM intervention WHERE user_id = "+ userId;

		try {
			ResultSet rsInt = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Intervention> intervList = getInterventionList(rsInt);
			return intervList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	}

	private static ObservableList<Intervention> getInterventionList(ResultSet rs)
			throws SQLException, ClassNotFoundException {
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

	//cambia lo stato dell'intervento
	public static void setState() throws SQLException, ClassNotFoundException {
		int id = Intervention.getIntervId();
		String selectReportState = "SELECT state FROM report WHERE intervention_id = " + id;
		ObservableList<Integer> stateList = FXCollections.observableArrayList();
		ResultSet rs2 = DBUtil.dbExecuteQuery(selectReportState);
		while (rs2.next()) {
			stateList.add(rs2.getInt("state"));
		}
		int newState = 1;
		int state0 = 0, state2 = 0;
		for (int state : stateList) {
			if (state == 1) break;
			else if (state == 2) state2++;
			else if (state == 0) state0++;
		}
		if(state0 == stateList.size()) newState = 0;
		else if (state2 == stateList.size()) newState = 2;
		
		String stmt = "UPDATE intervention SET stato = " + newState + " WHERE id = " + id;
		DBUtil.dbExecuteUpdate(stmt);
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

	// filtra interventi per stato
	public static ObservableList<Intervention> searchIntervention(int stato) throws SQLException, ClassNotFoundException {
		
		String selectStmt = "SELECT * FROM intervention WHERE user_id = " + userId;
		
		if (stato != 3) {		
			selectStmt = "SELECT * FROM intervention WHERE stato = " + stato + " AND user_id = " + userId;
		}	
		try {
			ResultSet rsInt = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Intervention> intervList = getInterventionList(rsInt);
			return intervList;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	// salva sul db gli interventi dal JSON
	public static void saveJSON(Intervention i) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO intervention "
				+ "(data_creazione, sede, codice_categoria, codice_verifica, descrizione_categoria, descrizione_verifica, user_id) VALUES"
				+ " ('" + i.getDataCreazione()+"','"+i.getSede()+"','"+i.getCodCategoria()+"','"+i.getCodVerifica()+"','"+i.getDescrCategoria()+"','"
				+i.getDescrVerifica()+"','"+userId+"')";
		
		DBUtil.dbExecuteUpdate(stmt);
	}

		
}
