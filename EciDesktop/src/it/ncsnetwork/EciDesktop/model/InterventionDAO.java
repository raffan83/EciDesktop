package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InterventionDAO {
	
	public static ObservableList<Intervention> searchInterventions() throws SQLException, ClassNotFoundException {

		String selectStmt = "SELECT * FROM intervention WHERE user_id = "+ User.getUserId() + " ORDER BY stato";

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
			interv.setStatoLbl(rs.getInt("stato"));
			interv.setCodCategoria(rs.getString("codice_categoria"));
			interv.setDescrCategoria(rs.getString("descrizione_categoria"));
			interv.setCodVerifica(rs.getString("codice_verifica"));
			interv.setDescrVerifica(rs.getString("descrizione_verifica"));
			interv.setNote(rs.getString("note"));
			if (rs.getInt("stato") != 2) interv.setNullInviaInterv();

			intervList.add(interv);
		}
		return intervList;
	}

	//cambia lo stato dell'intervento
	public static void setState() throws SQLException, ClassNotFoundException {
		long id = Intervention.getIntervId();
		String selectReportState = "SELECT stato FROM report WHERE intervention_id = " + id;
		ObservableList<Integer> stateList = FXCollections.observableArrayList();
		ResultSet rs2 = DBUtil.dbExecuteQuery(selectReportState);
		while (rs2.next()) {
			stateList.add(rs2.getInt("stato"));
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

	public static void saveNote(long intervId, String note) throws ClassNotFoundException, SQLException {
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
		
		String selectStmt = "SELECT * FROM intervention WHERE user_id = " + User.getUserId() + " ORDER BY stato";
		
		if (stato != 4) {		
			selectStmt = "SELECT * FROM intervention WHERE stato = " + stato + " AND user_id = " + User.getUserId();
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
				+ "(id, data_creazione, sede, codice_categoria, codice_verifica, descrizione_categoria, descrizione_verifica, user_id) VALUES"
				+ " ("+ i.getId() + ",'" + i.getDataCreazione()+"','"+i.getSede()+"','"+i.getCodCategoria()+"','"+i.getCodVerifica()+"','"+i.getDescrCategoria()+"','"
				+i.getDescrVerifica()+"','"+User.getUserId()+"')";
		
		DBUtil.dbExecuteUpdate(stmt);
	}
	
	public static ObservableList<Long> serchIntervCompleto(long idInterv) throws SQLException, ClassNotFoundException {
		String stmt = "SELECT report.id "
				+ "FROM intervention "
				+ "INNER JOIN report ON intervention.id = report.intervention_id "
				+ "WHERE intervention.id="+idInterv+" AND intervention.user_id=" + User.getUserId();
		
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(stmt);
			ObservableList<Long> verbali = getReportIdList(rs);
			return verbali;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
		
	}

	private static ObservableList<Long> getReportIdList(ResultSet rs) throws SQLException {

		ObservableList<Long> verbali = FXCollections.observableArrayList();

		while (rs.next()) {
			long id = rs.getInt("id");
			verbali.add(id);
		}
		return verbali;
	}
	
	
	public static ObservableList<Long> serchIntervCompleti() throws SQLException, ClassNotFoundException {
		String stmt = "SELECT id FROM intervention WHERE user_id=" + User.getUserId() + " AND stato = 2";
		
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(stmt);
			ObservableList<Long> interventi = getIntervIdList(rs);
			return interventi;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
		
	}

	private static ObservableList<Long> getIntervIdList(ResultSet rs) throws SQLException {

		ObservableList<Long> interventi = FXCollections.observableArrayList();

		while (rs.next()) {
			long id = rs.getInt("id");
			interventi.add(id);
		}
		return interventi;
	}
	
	
	
		
}
