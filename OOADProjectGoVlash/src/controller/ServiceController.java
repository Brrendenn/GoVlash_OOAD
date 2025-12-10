package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Service;
import util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceController {

    private Connect connect = Connect.getInstance();

    public ObservableList<Service> getAllServices() {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String query = "SELECT * FROM services";

        try(Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                services.add(new Service(
                    rs.getString("service_id"),
                    rs.getString("service_name"),
                    rs.getString("service_desc"),
                    rs.getDouble("service_price"),
                    rs.getInt("service_duration")
                ));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public String addService(String id, String name, String desc, String priceStr, String durationStr) {
        String validationError = validateInput(id, name, desc, priceStr, durationStr);
        if(validationError != null) return validationError;

        if(isIdExists(id)) return "Service ID must be unique!";

        String query = "INSERT INTO services VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, desc);
            ps.setDouble(4, Double.parseDouble(priceStr));
            ps.setInt(5, Integer.parseInt(durationStr));
            ps.executeUpdate();
            return "Success";

        } catch(SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String updateService(String id, String name, String desc, String priceStr, String durationStr) {
        String validationError = validateInput(id, name, desc, priceStr, durationStr);
        if(validationError != null) return validationError;

        String query = "UPDATE services SET service_name=?, service_desc=?, service_price=?, service_duration=? WHERE service_id=?";
        try(Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setDouble(3, Double.parseDouble(priceStr));
            ps.setInt(4, Integer.parseInt(durationStr));
            ps.setString(5, id);
            ps.executeUpdate();
            return "Success";

        } catch(SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String deleteService(String id) {
        String query = "DELETE FROM services WHERE service_id=?";
        try(Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            ps.executeUpdate();
            return "Success";

        } catch(SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    private String validateInput(String id, String name, String desc, String priceStr, String durationStr) {
        if(id.isEmpty() || name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
            return "All fields must be filled!";
        }
        if(name.length() > 50) return "Name must be <= 50 characters";
        if(desc.length() > 250) return "Description must be <= 250 characters";

        try {
            double price = Double.parseDouble(priceStr);
            if(price <= 0) return "Price must be greater than 0";
        } catch(NumberFormatException e) {
            return "Price must be a valid number";
        }

        try {
            int duration = Integer.parseInt(durationStr);
            if(duration < 1 || duration > 30) return "Duration must be between 1 and 30 days";
        } catch(NumberFormatException e) {
            return "Duration must be a valid integer";
        }

        return null;
    }

    private boolean isIdExists(String id) {
        String query = "SELECT service_id FROM services WHERE service_id = ?";
        try(Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}