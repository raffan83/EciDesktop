package it.ncsnetwork.EciDesktop.Utility;

import java.sql.*;

import com.sun.rowset.CachedRowSetImpl;

public class DBUtil {

	private static Connection conn;

	public static void dbConnect() throws SQLException, ClassNotFoundException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your JDBC?");
			e.printStackTrace();
			throw e;
		}
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:ECI.sqlite");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console" + e);
			e.printStackTrace();
			throw e;
		}
	}

	// Close Connection
	public static void dbDisconnect() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean isDbConnected() throws ClassNotFoundException, SQLException {
		dbConnect();
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		dbConnect();
		return conn;
	}

	// DB Execute Query Operation
	public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			// Connect to DB (Establish Oracle Connection)
			dbConnect();
			// System.out.println("Select statement: " + queryStmt + "\n");

			// Create statement
			stmt = conn.createStatement();

			// Execute select (query) operation
			resultSet = stmt.executeQuery(queryStmt);

			// CachedRowSet Implementation
			// In order to prevent "java.sql.SQLRecoverableException: Closed Connection:
			// next" error
			// We are using CachedRowSet
			crs = new CachedRowSetImpl();
			crs.populate(resultSet);
		} catch (SQLException e) {
			System.out.println("Problem occurred at executeQuery operation : " + e);
			throw e;
		} finally {
			if (resultSet != null) {
				// Close resultSet
				resultSet.close();
			}
			if (stmt != null) {
				// Close Statement
				stmt.close();
			}
			// Close connection
			dbDisconnect();
		}
		// Return CachedRowSet
		return crs;
	}

	// DB Execute Update (For Update/Insert/Delete) Operation
	public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
		// Declare statement as null
		Statement stmt = null;
		try {
			// Connect to DB (Establish Oracle Connection)
			dbConnect();
			//System.out.println("Query: " + sqlStmt + "\n");
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
		} catch (SQLException e) {
			System.out.println("Problem occurred at executeUpdate operation : " + e);
			throw e;
		} finally {
			if (stmt != null) {
				// Close statement
				stmt.close();
			}
			// Close connection
			dbDisconnect();
		}
	}
	
	// DB Execute Update (For Update/Insert/Delete) Operation
	public static long dbExecuteUpdateGetId(String sqlStmt) throws SQLException, ClassNotFoundException {
		// Declare statement as null
		Statement stmt = null;
		long id = 0;
		try {
			// Connect to DB (Establish Oracle Connection)
			dbConnect();
			//System.out.println("Query: " + sqlStmt + "\n");
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
			
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					id = generatedKeys.getLong(1);
				}
			}
		} catch (SQLException e) {
			System.out.println("Problem occurred at executeUpdate operation : " + e);
			throw e;
		} finally {
			if (stmt != null) {
				// Close statement
				stmt.close();
			}
			// Close connection
			dbDisconnect();
		}
		
		return id;
	}

}
