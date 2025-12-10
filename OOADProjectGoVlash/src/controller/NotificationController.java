package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Notification;
import util.Connect;

import java.sql.*;

public class NotificationController {
	private Connect connect = Connect.getInstance();
	
	public void createNotification(int customerId) {
		String msg = "Your order is finished and ready for pickup. Thank you for choosing our service!";
		String newId = generateNextId();
		
		String query = "INSERT INTO notifications (notification_id, recipient_id, message, created_at, is_read) VALUES (?, ?, ?, NOW(), FALSE)";
		
		try(Connection connection = connect.getConnection();
			PreparedStatement ps = connection.prepareStatement(query)) {
			
			ps.setString(1, newId);
			ps.setInt(2, customerId);
			ps.setString(3, msg);
			ps.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Notification> getNotificationsForUser(int userId) {
		ObservableList<Notification> list = FXCollections.observableArrayList();
		String query = "SELECT * FROM notifications WHERE recipient_id = ? ORDER BY created_at DESC";
		
		try(Connection connection = connect.getConnection(); 
			PreparedStatement ps = connection.prepareStatement(query)) {
			
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Notification(
						rs.getString("notification_id"),
						rs.getInt("recipient_id"),
						rs.getString("message"),
						rs.getTimestamp("created_at"),
						rs.getBoolean("is_read")
				));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void deleteNotification(String notifId) {
		String query = "DELETE FROM notifications WHERE notification_id = ?";
		
		try(Connection connection = connect.getConnection(); 
			PreparedStatement ps = connection.prepareStatement(query)) {
			
			ps.setString(1, notifId);
			ps.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String generateNextId() {
		String query = "SELECT notification_id FROM notifications ORDER BY notification_id DESC LIMIT 1";
		
		try(Connection connection = connect.getConnection(); 
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery()) {
			
			if(rs.next()) {
				String lastId = rs.getString("notification_id");
				int num = Integer.parseInt(lastId.substring(2)) + 1;
				return String.format("NT%03d", num);
			}
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return "NT001";
	}
	
	public void markAsRead(String notifId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, notifId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
