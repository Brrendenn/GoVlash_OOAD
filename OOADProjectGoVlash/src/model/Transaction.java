package model;

import java.sql.Date;

public class Transaction {
	
	private String transactionId;
	private int customerId;
	private String serviceId;
	private Integer receptionistId;
	private Integer staffId;
	private Date transactionDate;
	private int totalWeight;
	private String notes;
	private String status;
	
	public Transaction(String transactionId, int customerId, String serviceId, Integer receptionistId, Integer staffId,
			Date transactionDate, int totalWeight, String notes, String status) {
		this.transactionId = transactionId;
		this.customerId = customerId;
		this.serviceId = serviceId;
		this.receptionistId = receptionistId;
		this.staffId = staffId;
		this.transactionDate = transactionDate;
		this.totalWeight = totalWeight;
		this.notes = notes;
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public Integer getReceptionistId() {
		return receptionistId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public String getNotes() {
		return notes;
	}

	public String getStatus() {
		return status;
	}
	
	
}
