package model;

import java.sql.Timestamp;

public class Notification {
	private String notificationId;
	private int recipientId;
	private String message;
	private Timestamp createdAt;
	private boolean isRead;
	
	public Notification(String notificationId, int recipientId, String message, Timestamp createdAt, boolean isRead) {
		this.notificationId = notificationId;
		this.recipientId = recipientId;
		this.message = message;
		this.createdAt = createdAt;
		this.isRead = isRead;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public boolean isRead() {
		return isRead;
	}
	
	public String getStatus() {
		return isRead ? "Read" : "Unread";
	}
}
