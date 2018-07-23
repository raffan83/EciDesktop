package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportDAO {

	public static ObservableList<Report> searchReports() throws SQLException, ClassNotFoundException {

		String selectStmt = "SELECT * FROM report WHERE intervention_id = " + Intervention.getIntervId();

		try {
			ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);

			ObservableList<Report> reportList = getReportList(rs);

			return reportList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	}

	private static ObservableList<Report> getReportList(ResultSet rs) throws SQLException, ClassNotFoundException {
		ObservableList<Report> reportList = FXCollections.observableArrayList();

		while (rs.next()) {
			Report report = new Report();
			report.setId(rs.getInt("id"));
			report.setDescrVerifica(rs.getString("descrizione_verifica"));
			report.setCodVerifica(rs.getString("codice_verifica"));
			report.setCodCategoria(rs.getString("codice_categoria"));
			report.setStatoLbl(rs.getInt("stato"));
			if (rs.getInt("stato") == 3) report.setNullCompleteRep();
			if (rs.getInt("stato") != 2) report.setNullInviaRep();

			reportList.add(report);
		}
		return reportList;
	}

	public static void changeState(int s) throws ClassNotFoundException, SQLException {
		
		String stmt = "UPDATE report SET stato = " + s + " WHERE id = " + Report.getReportId();
		DBUtil.dbExecuteUpdate(stmt);
	}
	
	// filtra verbali per stato
	public static ObservableList<Report> searchIntervention(int stato) throws SQLException, ClassNotFoundException {
		
		String selectStmt = "SELECT * FROM report";
		
		if (stato != 3) {		
			selectStmt = "SELECT * FROM intervention WHERE stato = " + stato; //+ " AND user_id = " + userId;
		}
		
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Report> reportList = getReportList(rs);
			return reportList;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	// salva sul db i verbali dal JSON
	public static void saveJSON(Report r, long intervId) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO report "
				+ "(id, descrizione_verifica, intervention_id, codice_categoria, codice_verifica) VALUES"
				+ " ("+ r.getId() + ",'" + r.getDescrVerifica()+"',"+intervId+",'"+r.getCodCategoria()+"','"+r.getCodVerifica()+"')";
		
		DBUtil.dbExecuteUpdate(stmt);
	}

}