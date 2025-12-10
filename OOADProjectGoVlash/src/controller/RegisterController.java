package controller;

import util.Connect;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

public class RegisterController {

    private Connect connect = Connect.getInstance();

    public String registerUser(String username, String email, String pass, String confirmPass, 
                               String gender, LocalDate dob) {
        
        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || gender == null || dob == null) {
            return "All fields must be filled!";
        }

        if (isUsernameExists(username)) return "Username already taken!";

        if (!email.endsWith("@email.com")) {
            return "Email must end with '@email.com'";
        }
        if (isEmailExists(email)) return "Email already registered!";

        if (pass.length() < 6) return "Password must be at least 6 characters!";
        if (!pass.equals(confirmPass)) return "Passwords do not match!";

        if (!gender.equals("Male") && !gender.equals("Female")) {
            return "Gender must be Male or Female";
        }

        if (Period.between(dob, LocalDate.now()).getYears() < 12) {
            return "You must be at least 12 years old to register!";
        }
        String query = "INSERT INTO users (username, email, password, gender, dob, role) VALUES (?, ?, ?, ?, ?, 'Customer')";
        
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.setString(4, gender);
            ps.setDate(5, Date.valueOf(dob));
            
            ps.executeUpdate();
            return "Success";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    private boolean isUsernameExists(String username) {
        return check("SELECT username FROM users WHERE username = ?", username);
    }

    private boolean isEmailExists(String email) {
        return check("SELECT email FROM users WHERE email = ?", email);
    }

    private boolean check(String query, String param) {
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}