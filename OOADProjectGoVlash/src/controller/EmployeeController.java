package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import util.Connect;

public class EmployeeController {
	private Connect connect = Connect.getInstance();
	
	public ObservableList<User> getAllEmployees() {
		ObservableList<User> employees = FXCollections.observableArrayList();
		String query = "SELECT * FROM users";
		
		try(Connection connection = connect.getConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery()) {
			
			while(rs.next()) {
				employees.add(new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("email"),
					rs.getString("password"),
					rs.getString("gender"),
					rs.getDate("dob"),
					rs.getString("role")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public String addEmployee(String username, String email, String pass, String confirmPass,
			String gender, LocalDate dob, String role) {
		
		//Validation
		if(username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || gender == null || dob == null || role == null) {
			return "All fields must be filled";
		}
		
		if(!email.endsWith("@govlash.com")) {
			return "Email must end with '@govlash.com'";
		}
		
		if(isUsernameExists(username)) return "Username already taken!";
		if(isEmailExists(email)) return "Email already registered!";
		
		if(pass.length() < 6) return "Password must be at least 6 characters!";
		if(!pass.equals(confirmPass)) return "Passwords do not match!";
		
		if(!gender.equals("Male") && !gender.equals("Female")) {
			return "Gender must be Male or Female";
		}
		
		if(Period.between(dob, LocalDate.now()).getYears() < 17) {
			return "Employee must at least be 17 years old!";
		}
		
		if(!role.equals("Admin") && !role.equals("Laundry Staff") && !role.equals("Receptionist")) {
			return "Invalid Role selected!";
		}
		
		String query = "INSERT INTO users (username, email, password, gender, dob, role) VALUES (?, ?, ?, ?, ?, ?)";
		try(Connection connection = connect.getConnection();
			PreparedStatement ps = connection.prepareStatement(query)) {
			
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, pass);
			ps.setString(4, gender);
			ps.setDate(5, Date.valueOf(dob));
			ps.setString(6, role);
			
			ps.executeUpdate();
			return "Success";
			
		} catch(SQLException e) {
			e.printStackTrace();
			return "Database Error: " + e.getMessage();
		}
	}
	
	public boolean isUsernameExists(String username) {
		return checkExists("SELECT username FROM users WHERE username = ?", username);
	}
	
	public boolean isEmailExists(String email) {
		return checkExists("SELECT email FROM users WHERE email = ?", email);
	}
	
	public boolean checkExists(String query, String param) {
		try(Connection connection = connect.getConnection();
			PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, param);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	
}
