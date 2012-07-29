package com.rawabi.model;

// Generated Jul 24, 2012 12:30:09 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * CurrencyRef generated by hbm2java
 */
public class CurrencyRef implements java.io.Serializable {

	private BigDecimal currencyId;
	private String name;
	private BigDecimal decimalPrecision;
	private String isoCurrencyCode;
	private String isoCountryCode;
	private String isoLanguageCode;
	private Set<Subscriber> subscribers = new HashSet<Subscriber>(0);

	public CurrencyRef() {
	}

	public CurrencyRef(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public CurrencyRef(BigDecimal currencyId, String name,
			BigDecimal decimalPrecision, String isoCurrencyCode,
			String isoCountryCode, String isoLanguageCode,
			Set<Subscriber> subscribers) {
		this.currencyId = currencyId;
		this.name = name;
		this.decimalPrecision = decimalPrecision;
		this.isoCurrencyCode = isoCurrencyCode;
		this.isoCountryCode = isoCountryCode;
		this.isoLanguageCode = isoLanguageCode;
		this.subscribers = subscribers;
	}

	public BigDecimal getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getDecimalPrecision() {
		return this.decimalPrecision;
	}

	public void setDecimalPrecision(BigDecimal decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public String getIsoCurrencyCode() {
		return this.isoCurrencyCode;
	}

	public void setIsoCurrencyCode(String isoCurrencyCode) {
		this.isoCurrencyCode = isoCurrencyCode;
	}

	public String getIsoCountryCode() {
		return this.isoCountryCode;
	}

	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}

	public String getIsoLanguageCode() {
		return this.isoLanguageCode;
	}

	public void setIsoLanguageCode(String isoLanguageCode) {
		this.isoLanguageCode = isoLanguageCode;
	}

	public Set<Subscriber> getSubscribers() {
		return this.subscribers;
	}

	public void setSubscribers(Set<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}

}
