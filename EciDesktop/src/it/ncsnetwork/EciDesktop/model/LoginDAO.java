package it.ncsnetwork.EciDesktop.model;

import java.sql.*;

import it.ncsnetwork.EciDesktop.Utility.DBUtil;

public class LoginDAO {
		
	public static boolean isLogin(String user, String pass) throws SQLException, ClassNotFoundException {
		Connection conn = DBUtil.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select * from user where username = ? and password = ?";
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int userId = resultSet.getInt("id");
				User.setUserId(userId);
				return true;
			} 
			else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
	}
}
