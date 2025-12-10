package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Transaction;
import util.Connect;

import java.sql.*;
import java.time.LocalDate;

public class TransactionController {

    private Connect connect = Connect.getInstance();

    public String createTransaction(int customerId, String serviceId, String weightStr, String notes) {
        if (weightStr.isEmpty() || notes.isEmpty()) return "All fields must be filled";
        
        int weight;
        try {
            weight = Integer.parseInt(weightStr);
            if (weight < 2 || weight > 50) return "Weight must be between 2 and 50 Kg";
        } catch (NumberFormatException e) {
            return "Weight must be a number";
        }
        
        if (notes.length() > 250) return "Notes cannot exceed 250 characters";

        String newId = generateId();
        String query = "INSERT INTO transactions (transaction_id, customer_id, service_id, transaction_date, total_weight, transaction_notes, status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";

        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newId);
            ps.setInt(2, customerId);
            ps.setString(3, serviceId);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setInt(5, weight);
            ps.setString(6, notes);
            ps.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public void assignStaff(String transactionId, int receptionistId, int staffId) {
        String query = "UPDATE transactions SET receptionist_id = ?, staff_id = ? WHERE transaction_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, receptionistId);
            ps.setInt(2, staffId);
            ps.setString(3, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void finishTransaction(String transactionId) {
        String query = "UPDATE transactions SET status = 'Finished' WHERE transaction_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public ObservableList<Transaction> getAllTransactions() {
        return fetchTransactions("SELECT * FROM transactions ORDER BY transaction_date DESC");
    }
    
    public ObservableList<Transaction> getTransactionsByStaff(int staffId) {
        return fetchTransactions("SELECT * FROM transactions WHERE staff_id = " + staffId + " ORDER BY transaction_date DESC");
    }

    public ObservableList<Transaction> getPendingTransactions() {
        return fetchTransactions("SELECT * FROM transactions WHERE status = 'Pending' AND staff_id IS NULL ORDER BY transaction_date DESC");
    }

    private ObservableList<Transaction> fetchTransactions(String query) {
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        try (Connection connection = connect.getConnection();
             ResultSet rs = connection.createStatement().executeQuery(query)) {
            while (rs.next()) {
                Integer recepId = rs.getObject("receptionist_id") != null ? rs.getInt("receptionist_id") : null;
                Integer staffId = rs.getObject("staff_id") != null ? rs.getInt("staff_id") : null;

                list.add(new Transaction(
                    rs.getString("transaction_id"),
                    rs.getInt("customer_id"),
                    rs.getString("service_id"),
                    recepId,
                    staffId,
                    rs.getDate("transaction_date"),
                    rs.getInt("total_weight"),
                    rs.getString("transaction_notes"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private String generateId() {
        String query = "SELECT transaction_id FROM transactions ORDER BY transaction_id DESC LIMIT 1";
        try (Connection connection = connect.getConnection();
             ResultSet rs = connection.createStatement().executeQuery(query)) {
            if (rs.next()) {
                String lastId = rs.getString("transaction_id");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("TR%03d", num);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "TR001";
    }
}