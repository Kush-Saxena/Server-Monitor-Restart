package com.cg.servermonitorrest.resources;

import java.io.Serializable;
import java.util.Date;

public class ServiceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serviceID;

	private String serviceName;

	private ServiceTypeEnum serviceType;
	
	private Date timeCreated = new Date();

	
	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public ServiceBean() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public ServiceBean(String serviceID, String serviceName, ServiceTypeEnum serviceType) {
		super();
		this.serviceID = serviceID;
		this.serviceName = serviceName;
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public ServiceTypeEnum getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceTypeEnum serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "ServiceBean [serviceID=" + serviceID + ", serviceName=" + serviceName + ", serviceType=" + serviceType
				+ ", timeCreated=" + timeCreated + "]";
	}

	

}
