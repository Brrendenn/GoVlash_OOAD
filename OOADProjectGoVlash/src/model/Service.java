package model;

public class Service {
	private String serviceId;
	private String serviceName;
	private String serviceDesc;
	private double servicePrice;
	private int serviceDuration;
	
	public Service(String serviceId, String serviceName, String serviceDesc, double servicePrice, int serviceDuration) {
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceDesc = serviceDesc;
		this.servicePrice = servicePrice;
		this.serviceDuration = serviceDuration;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public int getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(int serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	@Override
	public String toString() {
		return "Service [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceDesc=" + serviceDesc
				+ ", servicePrice=" + servicePrice + ", serviceDuration=" + serviceDuration + "]";
	}
}
