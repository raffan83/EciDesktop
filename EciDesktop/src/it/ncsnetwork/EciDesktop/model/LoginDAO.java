package it.ncsnetwork.EciDesktop.model;

import java.sql.*;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;

public class LoginDAO {

	public static boolean isLogin(String user, String pass) throws SQLException, ClassNotFoundException {

		String stmt = "SELECT * FROM user WHERE username = '"+user+"' AND password = '" + pass+"'";
		
		ResultSet rs = DBUtil.dbExecuteQuery(stmt);
		
		if(rs.next()) {
			User.setUserId(rs.getLong("id"));
			return true;
		} else {
			return false;
		}

	}
}
