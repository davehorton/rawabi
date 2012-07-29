package com.rawabi.model;

// Generated Jul 24, 2012 12:30:09 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * ServiceProviderSettingsId generated by hbm2java
 */
public class ServiceProviderSettingsId implements java.io.Serializable {

	private String name;
	private BigDecimal serviceProviderId;

	public ServiceProviderSettingsId() {
	}

	public ServiceProviderSettingsId(String name, BigDecimal serviceProviderId) {
		this.name = name;
		this.serviceProviderId = serviceProviderId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getServiceProviderId() {
		return this.serviceProviderId;
	}

	public void setServiceProviderId(BigDecimal serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ServiceProviderSettingsId))
			return false;
		ServiceProviderSettingsId castOther = (ServiceProviderSettingsId) other;

		return ((this.getName() == castOther.getName()) || (this.getName() != null
				&& castOther.getName() != null && this.getName().equals(
				castOther.getName())))
				&& ((this.getServiceProviderId() == castOther
						.getServiceProviderId()) || (this
						.getServiceProviderId() != null
						&& castOther.getServiceProviderId() != null && this
						.getServiceProviderId().equals(
								castOther.getServiceProviderId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		result = 37
				* result
				+ (getServiceProviderId() == null ? 0 : this
						.getServiceProviderId().hashCode());
		return result;
	}

}
