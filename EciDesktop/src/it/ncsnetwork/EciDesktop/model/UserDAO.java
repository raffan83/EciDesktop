package it.ncsnetwork.EciDesktop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import it.ncsnetwork.EciDesktop.Utility.DBUtil;
import it.ncsnetwork.EciDesktop.Utility.PasswordUtils;


public class UserDAO {
		
	public static void setUserId(String user, String pass) throws SQLException, ClassNotFoundException {
		String stmt = "SELECT * FROM user WHERE username = '"+user+"'";// AND password = '" + pass+"'";
		
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		
		if(rs.next()) {
			User.setUserId(rs.getLong("id"));
		}
	}
	
	public static User getUser() throws ClassNotFoundException, SQLException {
		
		String stmt = "SELECT * FROM user WHERE id = " + User.getUserId();
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		User user = new User();
		if (rs.next()) {		
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
		}

		return user;
		
	}
	
	public static void updateUser(String user, String password, String accessToken) throws ClassNotFoundException, SQLException {
		
        String securePassword = PasswordUtils.generateSecurePassword(password, PasswordUtils.SALT);
        
		String stmt = "SELECT * FROM user WHERE username = '"+user+"'";
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		if (rs.next()) {
			String stmt2 = "UPDATE user SET password = '" + securePassword + "', access_token = '" + accessToken + "' WHERE username = '"+user+"'";
			DBUtil.dbExecuteUpdate(stmt2);
		} else {
			String stmt3 = "INSERT INTO user (username, password, access_token) VALUES ('" + user + "' , '" + securePassword + "' , '"+ accessToken +"' )";
			DBUtil.dbExecuteUpdate(stmt3);
		}

	}
	
	public static boolean isLogin(String user, String pass) throws SQLException, ClassNotFoundException {

		String stmt = "SELECT * FROM user WHERE username = '"+user+"'";
		
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		
		if(rs.next()) {
			String securePassword = rs.getString("password");
			boolean passwordMatch = PasswordUtils.verifyUserPassword(pass, securePassword, PasswordUtils.SALT);
			if (passwordMatch) {
				User.setUserId(rs.getLong("id"));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}
	
	public static void deleteAccessToken() throws ClassNotFoundException, SQLException {
			String stmt2 = "UPDATE user SET access_token = NULL"; //WHERE id = " + User.getUserId();
			DBUtil.dbExecuteUpdate(stmt2);
	}

}
