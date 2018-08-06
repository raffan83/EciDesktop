package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import it.ncsnetwork.EciDesktop.Utility.config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class QuestionarioDAO {
	public static void saveJSONDomande(Domanda d, long idReport) throws ClassNotFoundException, SQLException {
		String stmtDomanda = "INSERT INTO domande "
				+ "(id, id_report, testo, obbligatoria, posizione) VALUES"
				+ " ("+d.getId()+","+idReport+",'"+d.getTesto()+"','"+d.isObbligatoria()+"',"+d.getPosizione()+")";
		
		DBUtil.dbExecuteUpdate(stmtDomanda);
		
	}
	
	// salva risposte di tipo RES_TEXT
	public static void saveJSONResText(Risposta r, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmtRisposta = "INSERT INTO risposte "
				+ "(id_risposta, id_domanda, tipo) VALUES"
				+ " ("+r.getId()+","+idDomanda+",'"+r.getTipo()+"')";
	
		DBUtil.dbExecuteUpdate(stmtRisposta);
	}
	// salva risposte di tipo RES_FORMULA
	public static void saveJSONResFormula(Risposta r, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmtRisposta = "INSERT INTO risposte "
				+ "(id_risposta, id_domanda, tipo, label1, label2, operatore, label_risultato) VALUES"
				+ " ("+r.getId()+","+idDomanda+",'"+r.getTipo()+"','"+r.getLabel1()+"','"+r.getLabel2()+"','"+r.getOperatore()+"','"+r.getLabelRisultato()+"')";
	
		DBUtil.dbExecuteUpdate(stmtRisposta);
	}
	// salva risposte di tipo RES_CHOICE
	public static void saveJSONResChoice(Risposta r, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmtRisposta = "INSERT INTO risposte "
				+ "(id_risposta, id_domanda, tipo, multipla) VALUES"
				+ " ("+r.getId()+","+idDomanda+",'"+r.getTipo()+"','"+r.isMultipla()+"')";
	
		DBUtil.dbExecuteUpdate(stmtRisposta);
	}
	// slva le opzioni per il tipo RES_CHOICE
	public static void saveJSONOpzioni(Opzione o, long idRisposta) throws ClassNotFoundException, SQLException {
		String stmtOpzioni = "INSERT INTO opzioni "
				+ "(id, testo, id_risposta, posizione) VALUES"
				+ " ("+o.getId()+",'"+o.getTesto()+"',"+idRisposta+","+o.getPosizione()+")";
		
		DBUtil.dbExecuteUpdate(stmtOpzioni);
	}
	
	 public static ObservableList<Domanda> searchDomande() throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+Report.reportId+"\r\n" + 
		 		"ORDER BY posizione;";
		
			try {
				ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
				ObservableList<Domanda> questionario = getDomandeList(rs);
				return questionario;
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
	 }
	 
	 private static ObservableList<Domanda> getDomandeList(ResultSet rs)
				throws SQLException, ClassNotFoundException {
		ObservableList<Domanda> questionario = FXCollections.observableArrayList();

		while (rs.next()) {
			Domanda d = new Domanda();
			Risposta r = new Risposta();

			String stmtOp = "SELECT * FROM opzioni WHERE id_risposta=" + rs.getInt("id_risposta") + " ORDER BY posizione";
			ResultSet rsOp = DBUtil.dbExecuteQuery(stmtOp);
			ArrayList<Opzione> opzioni = new ArrayList<Opzione>();
			while (rsOp.next()) {
				Opzione o = new Opzione();
				o.setId(rsOp.getInt("id"));
				o.setTesto(rsOp.getString("testo"));
				o.setPosizione(rsOp.getInt("posizione"));
				o.setChecked(Boolean.parseBoolean(rsOp.getString("checked")));
				
				opzioni.add(o);
			}
			
			r.setId(rs.getInt("id_risposta"));
			r.setTipo(rs.getString("tipo"));
			r.setTestoRisposta(rs.getString("testo_risposta"));
			r.setLabel1(rs.getString("label1"));
			r.setInput1(rs.getString("input1"));
			r.setLabel2(rs.getString("label2"));
			r.setInput2(rs.getString("input2"));
			r.setOperatore(rs.getString("operatore"));
			r.setLabelRisultato(rs.getString("label_risultato"));
			r.setRisultato(rs.getString("risultato"));
			r.setMultipla(Boolean.parseBoolean(rs.getString("multipla")));
			r.setOpzioni(opzioni);

			d.setId(rs.getInt("id"));
			d.setTesto(rs.getString("testo"));
			d.setObbligatoria(Boolean.parseBoolean(rs.getString("obbligatoria")));
			d.setPosizione(rs.getInt("posizione"));
			d.setRisposta(r);
			
			questionario.add(d);
		}
		
		return questionario;
	}
	 
	 public static void saveResText(Risposta r) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE risposte SET testo_risposta = '" + r.getTestoRisposta() + "' WHERE id_risposta = " + r.getId();
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	 public static void saveResFormula(Risposta r) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE risposte SET "
				+ "input1 = '" + r.getInput1() 
				+ "', input2 = '" + r.getInput2()
				+ "', risultato = '" + r.getRisultato()
				+ "' WHERE id_risposta = " + r.getId();
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	 public static void resetResFormula(Risposta r) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE risposte SET "
				+ "input1 = NULL " 
				+ ", input2 = NULL "
				+ ", risultato = NULL "
				+ " WHERE id_risposta = " + r.getId();
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	 public static void saveResChoice(Opzione o) throws ClassNotFoundException, SQLException {
	 String stmt = "UPDATE opzioni SET checked = '" + o.isChecked() + "' WHERE id = " + o.getId();
	 
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	 public static void resetChoice(long id) throws ClassNotFoundException, SQLException {
		 String stmt = "UPDATE opzioni SET checked = 'false' WHERE id_risposta = " + id;
	 
		 try {
			DBUtil.dbExecuteUpdate(stmt);
		 } catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		 } 
	 }
	 
	 public static ObservableList<Domanda> searchRisposte(long id) throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+id+"\r\n" + 
		 		"ORDER BY posizione;";
		
			try {
				ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
				ObservableList<Domanda> questionario = getRisposteList(rs);
				return questionario;
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
	 }
	 
	 private static ObservableList<Domanda> getRisposteList(ResultSet rs)
				throws SQLException, ClassNotFoundException {
		ObservableList<Domanda> questionario = FXCollections.observableArrayList();

		while (rs.next()) {
			Domanda d = new Domanda();
			Risposta r = new Risposta();

			String stmtOp = "SELECT * FROM opzioni WHERE id_risposta=" + rs.getInt("id_risposta") + " ORDER BY posizione";
			ResultSet rsOp = DBUtil.dbExecuteQuery(stmtOp);
			ArrayList<Opzione> opzioni = new ArrayList<Opzione>();
			while (rsOp.next()) {
				Opzione o = new Opzione();
				o.setId(rsOp.getInt("id"));
				o.setChecked(Boolean.parseBoolean(rsOp.getString("checked")));
				
				opzioni.add(o);
			}
			
			r.setId(rs.getInt("id_risposta"));
			String tipo = rs.getString("tipo");
			r.setTipo(tipo);
			r.setTestoRisposta(rs.getString("testo_risposta"));
			r.setInput1(rs.getString("input1"));
			r.setInput2(rs.getString("input2"));
			r.setRisultato(rs.getString("risultato"));
			if(tipo.equals(Risposta.RES_CHOICE)) r.setOpzioni(opzioni);

			d.setRisposta(r);
			
			questionario.add(d);
		}
		
		return questionario;
	}
	
}
