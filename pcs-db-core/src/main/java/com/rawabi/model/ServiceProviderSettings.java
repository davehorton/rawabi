package com.rawabi.model;

// Generated Sep 19, 2012 12:44:25 PM by Hibernate Tools 3.4.0.CR1

/**
 * ServiceProviderSettings generated by hbm2java
 */
public class ServiceProviderSettings implements java.io.Serializable {

	private ServiceProviderSettingsId id;
	private ServiceProvider serviceProvider;
	private String spValue;

	public ServiceProviderSettings() {
	}

	public ServiceProviderSettings(ServiceProviderSettingsId id,
			ServiceProvider serviceProvider) {
		this.id = id;
		this.serviceProvider = serviceProvider;
	}

	public ServiceProviderSettings(ServiceProviderSettingsId id,
			ServiceProvider serviceProvider, String spValue) {
		this.id = id;
		this.serviceProvider = serviceProvider;
		this.spValue = spValue;
	}

	public ServiceProviderSettingsId getId() {
		return this.id;
	}

	public void setId(ServiceProviderSettingsId id) {
		this.id = id;
	}

	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getSpValue() {
		return this.spValue;
	}

	public void setSpValue(String spValue) {
		this.spValue = spValue;
	}

}
