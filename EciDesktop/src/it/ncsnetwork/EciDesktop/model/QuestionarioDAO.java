package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class QuestionarioDAO {
	public static void saveJSON(Domanda d, long id) throws ClassNotFoundException, SQLException {
		//aggiorna tabella domande
		String stmtDomanda = "INSERT INTO domande "
				+ "(id_report, testo, obbligatoria, posizione) VALUES"
				+ " ("+id+",'"+d.getTesto()+"','"+d.isObbligatoria()+"','"+d.getPosizione()+"')";
		
		DBUtil.dbExecuteUpdate(stmtDomanda);
		
		//aggiorna tabella risposte
		Risposta r = d.getRisposta();
		String stmtRisposta = "INSERT INTO risposte "
				+ "(id_risposta, id_domanda, tipo, label1, label2, operatore, label_risultato) VALUES"
				+ " ("+r.getId()+","+d.getId()+",'"+r.getTipo()+"','"+r.getLabel1()+"','"+r.getLabel2()+"','"+r.getOperatore()+"','"+r.getLabelRisultato()+"')";
	
		DBUtil.dbExecuteUpdate(stmtRisposta);
	}
	
	 public static ObservableList<Domanda> searchDomande() throws ClassNotFoundException, SQLException{
		 String selectStmt = "SELECT domande.*, risposte.*\r\n" + 
		 		"FROM domande\r\n" + 
		 		"INNER JOIN risposte ON domande.id=risposte.id_domanda\r\n" + 
		 		"WHERE id_report="+Report.reportId+"\r\n" + 
		 		"ORDER BY posizione;";

			try {
				ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
				ObservableList<Domanda> questionario = getInterventionList(rs);
				return questionario;
			} catch (SQLException e) {
				System.out.println("SQL select operation has been failed: " + e);
				throw e;
			}
	 }
	 
	 private static ObservableList<Domanda> getInterventionList(ResultSet rs)
				throws SQLException, ClassNotFoundException {
			ObservableList<Domanda> questionario = FXCollections.observableArrayList();

			while (rs.next()) {
				Domanda d = new Domanda();
				Risposta r = new Risposta();
				
				r.setId(rs.getInt("id_risposta"));
				r.setTipo(rs.getString("tipo"));
				r.setLabel1(rs.getString("label1"));
				r.setLabel2(rs.getString("label2"));
				r.setOperatore(rs.getString("operatore"));
				r.setLabelRisultato(rs.getString("label_risultato"));

				d.setId(rs.getInt("id"));
				d.setTesto(rs.getString("testo"));
				d.setObbligatoria(rs.getBoolean("obbligatoria"));
				d.setPosizione(rs.getInt("posizione"));
				d.setRisposta(r);
				
				questionario.add(d);
			}
			return questionario;
		}
	
}
