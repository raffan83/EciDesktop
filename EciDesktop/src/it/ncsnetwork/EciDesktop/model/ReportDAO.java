package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportDAO {
	
    public static ObservableList<Report> searchReports () throws SQLException, ClassNotFoundException {
    	int intervId = Intervention.getIntervId();
        String selectStmt = "SELECT * FROM report WHERE intervention_id = "+intervId;
 
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
        	report.setIdRep(rs.getInt("id"));
            report.setNameRep(rs.getString("name"));
            report.setState(rs.getInt("state"));
            if (rs.getInt("state") == 2) {
            	report.setNullCompleteRep();
            	// data completamento
            }else {
            	//report.setDetailRep("Completa");
            }
            reportList.add(report);
        }
        return reportList;
    }
    
    public static void changeState (int s) throws ClassNotFoundException, SQLException {
    	int reportId = Report.getReportId();
    	String stmt = "UPDATE report SET state = " + s + " WHERE id = " + reportId;
        DBUtil.dbExecuteUpdate(stmt);
    }
	
}