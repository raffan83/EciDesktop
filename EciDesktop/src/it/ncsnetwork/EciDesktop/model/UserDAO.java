package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.Utility.DBUtil;


public class UserDAO {
	
	static int userId = User.getUserId();
	
	public static User getUser() throws ClassNotFoundException, SQLException {
		
		String stmt = "SELECT * FROM user WHERE id = " + userId;
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		User user = new User();
		if (rs.next()) {		
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
		}
		return user;
	}
	
	public static void updateUser(String user, String password) throws ClassNotFoundException, SQLException {
		
		String stmt = "SELECT * FROM user WHERE username = '"+user+"'";
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		if (rs.next()) {
			String stmt2 = "UPDATE user SET password = '" + password + "' WHERE username = '"+user+"'";
			DBUtil.dbExecuteUpdate(stmt2);
		} else {
			String stmt3 = "INSERT INTO user (username, password) VALUES ('" + user + "' , '" + password + "' )";
			DBUtil.dbExecuteUpdate(stmt3);
		}

	}

}
