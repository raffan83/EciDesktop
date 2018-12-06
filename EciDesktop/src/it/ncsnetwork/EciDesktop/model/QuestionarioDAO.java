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
	
	public static void saveJSONDomandeAnn(Domanda d, long idReport) throws ClassNotFoundException, SQLException {
		String stmtDomanda = "INSERT INTO domande "
				+ "(id, id_report, testo, obbligatoria, posizione, annidata) VALUES"
				+ " ("+d.getId()+","+idReport+",'"+d.getTesto()+"','"+d.isObbligatoria()+"',"+d.getPosizione()+","+d.getAnnidata()+")";
		
		DBUtil.dbExecuteUpdate(stmtDomanda);
		
	}
	
	public static void saveJSONDomandeCol(Domanda d, long idColonna, long idReport) throws ClassNotFoundException, SQLException {
		String stmtDomanda = "INSERT INTO domande "
				+ "(id, id_report, testo, obbligatoria, posizione, id_colonna) VALUES"
				+ " ("+d.getId()+","+idReport+",'"+d.getTesto()+"','"+d.isObbligatoria()+"',"+d.getPosizione()+","+idColonna+")";
		
		DBUtil.dbExecuteUpdate(stmtDomanda);
		
	}
	
	public static void saveJSONColonne(long id_colonna, long id_risposta) throws ClassNotFoundException, SQLException {
		String stmtDomanda = "INSERT INTO colonne "
				+ "(id, id_risposta) VALUES"
				+ " ("+id_colonna+","+id_risposta+")";
		
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
		 		"WHERE id_report="+Report.reportId+" AND domande.annidata IS NULL AND domande.id_colonna IS NULL\r\n" + 
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
	 
	 public static ObservableList<Domanda> searchDomandeAnnidate(long idOpzione) throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+Report.reportId+" AND domande.annidata="+idOpzione+"\r\n" + 
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
	 
	 public static ObservableList<Domanda> searchDomandeTabella(long idRisposta) throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
				"INNER JOIN colonne ON domande.id_colonna=colonne.id\r\n"+
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+Report.reportId+" AND colonne.id_risposta="+idRisposta+"\r\n" + 
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
	 
	 public static ObservableList<Domanda> searchRisposteTabella(long idRow) throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte_tabella.*\r\n" + 
			 		"FROM domande\r\n" + 
			 		"INNER JOIN risposte_tabella ON domande.id=risposte_tabella.id_domanda\r\n" + 
			 		"WHERE id_report="+Report.reportId+" AND risposte_tabella.id_row=" + idRow+ 
			 		" ORDER BY posizione;";
			
				try {
					ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
					ObservableList<Domanda> questionario = getRisposteTabellaList(rs);
					return questionario;
				} catch (SQLException e) {
					System.out.println("SQL select operation has been failed: " + e);
					throw e;
				}
	 }
	 
	 private static ObservableList<Domanda> getRisposteTabellaList(ResultSet rs)
				throws SQLException, ClassNotFoundException {
		 ObservableList<Domanda> questionario = FXCollections.observableArrayList();

			while (rs.next()) {
				Domanda d = new Domanda();
				Risposta r = new Risposta();

				String stmtOp = "SELECT * FROM opzioni_tabella WHERE id_risposta_tabella=" + rs.getInt("id_risposta_tabella");
				ResultSet rsOp = DBUtil.dbExecuteQuery(stmtOp);
				ArrayList<Opzione> opzioni = new ArrayList<Opzione>();
				while (rsOp.next()) {
					Opzione o = new Opzione();
					o.setId(rsOp.getInt("id"));
					o.setTesto(rsOp.getString("testo"));
					o.setChecked(Boolean.parseBoolean(rsOp.getString("checked")));
					
					opzioni.add(o);
				}
				
				//r.setId(rs.getInt("id_risposta_tabella"));
				r.setTipo(rs.getString("tipo"));
				r.setTestoRisposta(rs.getString("testo_risposta"));
				r.setInput1(rs.getString("input1"));
				r.setInput2(rs.getString("input2"));
				r.setOperatore(rs.getString("operatore"));
				r.setRisultato(rs.getString("risultato"));
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
			r.setTabellaCompleta(Boolean.parseBoolean(rs.getString("tabella_completa")));

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
		 		"WHERE id_report="+id+" AND domande.annidata IS NULL AND domande.id_colonna IS NULL\r\n" + 
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
	 
	 public static ObservableList<Domanda> searchRisposteAnn(long id) throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+id+" AND domande.annidata IS NULL AND domande.id_colonna IS NULL\r\n" + 
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
				
				r.setId(rs.getInt("id_risposta"));
				String tipo = rs.getString("tipo");
				r.setTipo(tipo);
						
				if(rs.getString("tipo").equals(Risposta.RES_TABLE)) {
					
					ArrayList<Colonna> colonne = new ArrayList<Colonna>();
					
					String stmtCol = "SELECT id FROM colonne WHERE id_risposta="+rs.getInt("id_risposta");
					ResultSet rsCol = DBUtil.dbExecuteQuery(stmtCol);
					
					while(rsCol.next()) {
						Colonna c = new Colonna();
						long idColonna = rsCol.getInt("id");
						c.setId(idColonna);
						
						ArrayList<Risposta> risposte = new ArrayList<Risposta>();
						
						String stmtRispCol = "SELECT * FROM risposte_tabella "
								+ "INNER JOIN domande ON risposte_tabella.id_domanda=domande.id "
								+ "WHERE domande.id_colonna="+idColonna+" ORDER BY id_row";
						ResultSet rsRispCol = DBUtil.dbExecuteQuery(stmtRispCol);
						
						int row = 0;
						while(rsRispCol.next()) {
							Risposta rCol = new Risposta();
							
							String stmtOpCol = "SELECT * FROM opzioni_tabella WHERE id_risposta_tabella=" + rsRispCol.getInt("id_risposta_tabella") + " ORDER BY posizione";
							ResultSet rsOpCol = DBUtil.dbExecuteQuery(stmtOpCol);
							
							ArrayList<Opzione> opzioniTabella = new ArrayList<Opzione>();
							
							while (rsOpCol.next()) {					
								Opzione o = new Opzione();
								o.setId(rsOpCol.getInt("id"));
								o.setPosizione(rsOpCol.getInt("posizione"));
								o.setChecked(Boolean.parseBoolean(rsOpCol.getString("checked")));								
								opzioniTabella.add(o);
							}
							
							rCol.setId(rsRispCol.getInt("id_risposta"));
							String tipoCol = rsRispCol.getString("tipo");
							rCol.setTipo(tipoCol);
							rCol.setRow(row);
							rCol.setTestoRisposta(rsRispCol.getString("testo_risposta"));
							rCol.setInput1(rsRispCol.getString("input1"));
							rCol.setInput2(rsRispCol.getString("input2"));
							rCol.setRisultato(rsRispCol.getString("risultato"));
							if(tipoCol.equals(Risposta.RES_CHOICE)) rCol.setOpzioni(opzioniTabella);
							
							risposte.add(rCol);
							row++;
						}
						
						c.setRisposte(risposte);
						colonne.add(c);					
						
					}
					r.setColonne(colonne);
					
				} else {
	
					String stmtOp = "SELECT * FROM opzioni WHERE id_risposta=" + rs.getInt("id_risposta") + " ORDER BY posizione";
					ResultSet rsOp = DBUtil.dbExecuteQuery(stmtOp);
					
					ArrayList<Opzione> opzioni = new ArrayList<Opzione>();
					
					while (rsOp.next()) {					
						Opzione o = new Opzione();
						o.setId(rsOp.getInt("id"));
						o.setChecked(Boolean.parseBoolean(rsOp.getString("checked")));
						
						ObservableList<Domanda> domandeAnnidate = FXCollections.observableArrayList();
						domandeAnnidate = getRisposteAnnidateList(rsOp.getInt("id"));
						
						ArrayList<Risposta> rispAnnList = new ArrayList<Risposta>();
	
						for (Domanda dAnn: domandeAnnidate) {				
							Risposta rAnn = dAnn.getRisposta();
							rispAnnList.add(rAnn);
						}
						if (!rispAnnList.isEmpty())
							o.setRisposte(rispAnnList);
						
						opzioni.add(o);
					}
					
	
					r.setTestoRisposta(rs.getString("testo_risposta"));
					r.setInput1(rs.getString("input1"));
					r.setInput2(rs.getString("input2"));
					r.setRisultato(rs.getString("risultato"));
					if(tipo.equals(Risposta.RES_CHOICE)) r.setOpzioni(opzioni);
				}
				
				d.setRisposta(r);
				
				questionario.add(d);
		
		}
		return questionario;
		
	}
	 
	 private static ObservableList<Domanda> getRisposteAnnidateList(long idOpzione) throws ClassNotFoundException, SQLException {
			// risposte annidate
			String selectStmt = "SELECT domande.*, risposte.*\r\n"+ 
					"FROM domande\r\n"+ 
					"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
					"WHERE domande.annidata="+idOpzione+"\r\n"+
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
	 
	 public static void saveResTextTable(Risposta r, long id_row, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO risposte_tabella (id_risposta, id_domanda, tipo, testo_risposta, id_row) VALUES ("+r.getId() +", "+idDomanda+", '"+r.getTipo()+"', '"+r.getTestoRisposta()+"', '"+id_row+"')";
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	
	 public static void saveResFormulaTable(Risposta r, long id_row, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO risposte_tabella (id_risposta, id_domanda, id_row, tipo, input1, input2, risultato, operatore) "
				+ "VALUES ("+r.getId() +", "+idDomanda+", '"+id_row+"', '"+r.getTipo()+"', '"+r.getInput1()+"', '"+r.getInput2()+"', '"+r.getRisultato()+"', '"+r.getOperatore()+"')";
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }

	 public static long saveResChoiceTable(Risposta r, long id_row, long idDomanda) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO risposte_tabella (id_risposta, id_domanda, id_row, tipo) VALUES ("+r.getId() +", "+idDomanda+ ", '"+id_row+"', '"+r.getTipo()+"')";
		long id = 0;
		try {
			id = DBUtil.dbExecuteUpdateGetId(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
		return id;
	 }
	 
	 public static void saveResChoiceOptionsTable(boolean checked, long id_risposta, Opzione o) throws ClassNotFoundException, SQLException {
		String stmt = "INSERT INTO opzioni_tabella (id_risposta_tabella, testo, checked, posizione) VALUES ('"+id_risposta + "', '"+o.getTesto()+"', '"+checked+"', '"+o.getPosizione()+"')";
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
	 }
	 
	 public static long getLastId() throws ClassNotFoundException, SQLException {
		String stmt = "SELECT * FROM risposte_tabella ORDER BY id_row DESC LIMIT 1";
		long id = 1;
		try {
			ResultSet rs = DBUtil.dbExecuteQuery(stmt);
			if(rs.next()) {
				id = rs.getLong("id_row") + 1;
			}
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}
		return id;
	 }
	 
	 public static void deleteRow(String id_row) throws ClassNotFoundException, SQLException {
		 String deleteOptions = "DELETE FROM opzioni_tabella " + 
		 		"WHERE opzioni_tabella.id_risposta_tabella IN " + 
		 		"(SELECT id_risposta_tabella FROM risposte_tabella " + 
		 		"WHERE id_row="+id_row+")";
		 
		 String deleteRisposte = "DELETE FROM risposte_tabella WHERE id_row="+id_row;
		 
			try {
				DBUtil.dbExecuteUpdate(deleteOptions);
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
			try {
				DBUtil.dbExecuteUpdate(deleteRisposte);
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
	 }
	 
	 public static ObservableList<Long> searchIdRowList(long idRisposta) throws ClassNotFoundException, SQLException{

		 String selectStmt = "SELECT DISTINCT id_row FROM risposte_tabella WHERE id_risposta="+idRisposta+" ORDER BY id_row";
		
			try {
				ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
				ObservableList<Long> idRowList = getIdRowList(rs);
				return idRowList;
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
	 }
	 
	 private static ObservableList<Long> getIdRowList(ResultSet rs)
				throws SQLException, ClassNotFoundException {
		ObservableList<Long> idRowList = FXCollections.observableArrayList();

		while (rs.next()) {
			idRowList.add(rs.getLong("id_row"));
		}
		
		return idRowList;
	}

	public static void setTabellaCompleta(long id) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE risposte SET tabella_completa = 'true' WHERE id_risposta = " + id;
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}	
	}
	public static void setTabellaCompletaNull(long id) throws ClassNotFoundException, SQLException {
		String stmt = "UPDATE risposte SET tabella_completa = NULL WHERE id_risposta = " + id;
		try {
			DBUtil.dbExecuteUpdate(stmt);
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;
		}	
	}
	
}
