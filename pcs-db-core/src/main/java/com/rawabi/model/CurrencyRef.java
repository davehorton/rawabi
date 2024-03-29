package com.rawabi.model;

// Generated Sep 19, 2012 12:44:25 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

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

	public CurrencyRef() {
	}

	public CurrencyRef(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public CurrencyRef(BigDecimal currencyId, String name,
			BigDecimal decimalPrecision, String isoCurrencyCode,
			String isoCountryCode, String isoLanguageCode) {
		this.currencyId = currencyId;
		this.name = name;
		this.decimalPrecision = decimalPrecision;
		this.isoCurrencyCode = isoCurrencyCode;
		this.isoCountryCode = isoCountryCode;
		this.isoLanguageCode = isoLanguageCode;
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

}
