package com.rawabi.model;

// Generated Mar 1, 2012 2:19:03 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EvtPrepaidActivation generated by hbm2java
 */
public class EvtPrepaidActivation implements java.io.Serializable {

	private String activationId;
	private Lot lot;
	private ProductOffering productOffering;
	private BigDecimal totalPins;
	private BigDecimal status;
	private Date timeStamp;
	private Date unusedExpirationDate;
	private Date expirationDate;
	private Date purgeDate;
	private BigDecimal startLotSeq;
	private BigDecimal endLotSeq;
	private BigDecimal initialBalance;
	private BigDecimal numPinsPurged;
	private BigDecimal expirationType;
	private BigDecimal numExpirationDays;
	private String description;
	private Set preActivatedSubscriberses = new HashSet(0);

	public EvtPrepaidActivation() {
	}

	public EvtPrepaidActivation(String activationId, BigDecimal totalPins,
			BigDecimal status) {
		this.activationId = activationId;
		this.totalPins = totalPins;
		this.status = status;
	}

	public EvtPrepaidActivation(String activationId, Lot lot,
			ProductOffering productOffering, BigDecimal totalPins,
			BigDecimal status, Date timeStamp, Date unusedExpirationDate,
			Date expirationDate, Date purgeDate, BigDecimal startLotSeq,
			BigDecimal endLotSeq, BigDecimal initialBalance,
			BigDecimal numPinsPurged, BigDecimal expirationType,
			BigDecimal numExpirationDays, String description,
			Set preActivatedSubscriberses) {
		this.activationId = activationId;
		this.lot = lot;
		this.productOffering = productOffering;
		this.totalPins = totalPins;
		this.status = status;
		this.timeStamp = timeStamp;
		this.unusedExpirationDate = unusedExpirationDate;
		this.expirationDate = expirationDate;
		this.purgeDate = purgeDate;
		this.startLotSeq = startLotSeq;
		this.endLotSeq = endLotSeq;
		this.initialBalance = initialBalance;
		this.numPinsPurged = numPinsPurged;
		this.expirationType = expirationType;
		this.numExpirationDays = numExpirationDays;
		this.description = description;
		this.preActivatedSubscriberses = preActivatedSubscriberses;
	}

	public String getActivationId() {
		return this.activationId;
	}

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	public Lot getLot() {
		return this.lot;
	}

	public void setLot(Lot lot) {
		this.lot = lot;
	}

	public ProductOffering getProductOffering() {
		return this.productOffering;
	}

	public void setProductOffering(ProductOffering productOffering) {
		this.productOffering = productOffering;
	}

	public BigDecimal getTotalPins() {
		return this.totalPins;
	}

	public void setTotalPins(BigDecimal totalPins) {
		this.totalPins = totalPins;
	}

	public BigDecimal getStatus() {
		return this.status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public Date getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getUnusedExpirationDate() {
		return this.unusedExpirationDate;
	}

	public void setUnusedExpirationDate(Date unusedExpirationDate) {
		this.unusedExpirationDate = unusedExpirationDate;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getPurgeDate() {
		return this.purgeDate;
	}

	public void setPurgeDate(Date purgeDate) {
		this.purgeDate = purgeDate;
	}

	public BigDecimal getStartLotSeq() {
		return this.startLotSeq;
	}

	public void setStartLotSeq(BigDecimal startLotSeq) {
		this.startLotSeq = startLotSeq;
	}

	public BigDecimal getEndLotSeq() {
		return this.endLotSeq;
	}

	public void setEndLotSeq(BigDecimal endLotSeq) {
		this.endLotSeq = endLotSeq;
	}

	public BigDecimal getInitialBalance() {
		return this.initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public BigDecimal getNumPinsPurged() {
		return this.numPinsPurged;
	}

	public void setNumPinsPurged(BigDecimal numPinsPurged) {
		this.numPinsPurged = numPinsPurged;
	}

	public BigDecimal getExpirationType() {
		return this.expirationType;
	}

	public void setExpirationType(BigDecimal expirationType) {
		this.expirationType = expirationType;
	}

	public BigDecimal getNumExpirationDays() {
		return this.numExpirationDays;
	}

	public void setNumExpirationDays(BigDecimal numExpirationDays) {
		this.numExpirationDays = numExpirationDays;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getPreActivatedSubscriberses() {
		return this.preActivatedSubscriberses;
	}

	public void setPreActivatedSubscriberses(Set preActivatedSubscriberses) {
		this.preActivatedSubscriberses = preActivatedSubscriberses;
	}

}
