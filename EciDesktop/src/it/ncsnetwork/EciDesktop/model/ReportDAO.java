package it.ncsnetwork.EciDesktop.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import it.ncsnetwork.EciDesktop.Utility.config;
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
			report.setVerbaleId(rs.getInt("verbale_id"));
			report.setDescrVerifica(rs.getString("descrizione_verifica"));
			report.setCodVerifica(rs.getString("codice_verifica"));
			report.setCodCategoria(rs.getString("codice_categoria"));
			report.setStatoLbl(rs.getInt("stato"));
			report.setStato(rs.getInt("stato"));
			boolean isSchedaTecnica = Boolean.parseBoolean(rs.getString("scheda_tecnica"));
			report.setScheda_tecnica(isSchedaTecnica);
			//if (rs.getInt("stato") == 3) report.setNullCompleteRep();
			//if (rs.getInt("stato") != 2 || isSchedaTecnica) report.setNullInviaRep();
			if (rs.getInt("stato") != 2) report.setNullInviaRep();
			if (rs.getInt("stato") == 3 || isSchedaTecnica) report.setNullUploadDoc();
			String filePath = config.PATH_DOCUMENTI+Intervention.getIntervId()+"\\"+report.getId();
			if (isSchedaTecnica || !new File(filePath).exists()) report.setNullShowDoc();

			reportList.add(report);
		}
		return reportList;
	}

	public static void changeState(int s) throws ClassNotFoundException, SQLException {
		
		String stmt = "UPDATE report SET stato = " + s + " WHERE id = " + Report.getReportId();
		DBUtil.dbExecuteUpdate(stmt);
		
		//aggiorna stato intervento
		InterventionDAO.setState();
	}
	public static void setStateInviato(long id) throws ClassNotFoundException, SQLException {
		
		String stmt = "UPDATE report SET stato = 3 WHERE id = " + id;
		DBUtil.dbExecuteUpdate(stmt);
		
		//aggiorna stato intervento
		InterventionDAO.setState();
	}
	public static void updateStato(ObservableList<Long> listaVerbali) throws ClassNotFoundException, SQLException {
		for (long id: listaVerbali) {
			String stmt = "UPDATE report SET stato = 3 WHERE id = " + id;
			DBUtil.dbExecuteUpdate(stmt);
		}
	}
	
	// salva sul db i verbali dal JSON
	public static void saveJSON(Report r, long intervId) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO report "
				+ "(id, descrizione_verifica, intervention_id, codice_categoria, codice_verifica, scheda_tecnica, verbale_id) VALUES"
				+ " ("+ r.getId() + ",'" + r.getDescrVerifica()+"',"+intervId+",'"+r.getCodCategoria()+"','"+r.getCodVerifica()+"','"+r.isScheda_tecnica()+"',"+r.getVerbaleId()+")";
		DBUtil.dbExecuteUpdate(stmt);
	}
	
	public static Report getSchedaTecnica(long idVerbale) throws ClassNotFoundException, SQLException {
		String stmt = "SELECT id, stato FROM report WHERE verbale_id = " + idVerbale + " AND scheda_tecnica = 'true'";
		
		Report verbale = new Report();
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(stmt);
			if (rs.next()) {
				verbale.setId(rs.getInt("id"));
				verbale.setStato(rs.getInt("stato"));
			} else {
				verbale.setId(0);
			}
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
		
		return verbale;
	}
	
	
/*	// salva sul db i verbali dal JSON
	public static void saveJSONVerbale(Report r, long intervId) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO report "
				+ "(id, descrizione_verifica, intervention_id, codice_categoria, codice_verifica, scheda_tecnica) VALUES"
				+ " ("+ r.getId() + ",'" + r.getDescrVerifica()+"',"+intervId+",'"+r.getCodCategoria()+"','"+r.getCodVerifica()+"','"+r.isScheda_tecnica()+"')";
		
		DBUtil.dbExecuteUpdate(stmt);
	}
	// salva sul db le schede tecniche dal JSON
	public static void saveJSONSchedaTecnica(Report r, long intervId) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO report "
				+ "(id, intervention_id, scheda_tecnica, verbale_id) VALUES"
				+ " ("+ r.getId()+","+intervId+",'"+r.isScheda_tecnica()+"',"+r.getVerbaleId()+")";
		
		DBUtil.dbExecuteUpdate(stmt);
	}*/
	
	public static int getStato() throws ClassNotFoundException, SQLException {
		String stmt = "SELECT stato FROM report WHERE id = " + Report.getReportId();
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(stmt);
			
			if (rs.next()) {
				return rs.getInt("stato");
			} else {
				return 0;
			}

		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	}

}