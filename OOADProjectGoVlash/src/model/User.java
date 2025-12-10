package model;

import java.sql.Date;

public class User {
	private int userId;
	private String username;
	private String email;
	private String password;
	private String gender;
	private Date dob;
	private String role;
	
	public User(int userId, String username, String email, String password, String gender, Date dob, String role) {
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.dob = dob;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
