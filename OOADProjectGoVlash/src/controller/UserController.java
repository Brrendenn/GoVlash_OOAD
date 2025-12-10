package controller;

import model.User;
import util.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
	
	private Connect connect = Connect.getInstance();
	
	public User login(String username, String password) {
		User user = null;
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		
		try(Connection connection = connect.getConnection();
			PreparedStatement ps = connection.prepareStatement(query)) {
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				user = new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("gender"),
					rs.getDate("dob"),
					rs.getString("role")
				);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

}
