package com.rawabi.model;

// Generated Jul 30, 2012 10:19:37 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Lot generated by hbm2java
 */
public class Lot implements java.io.Serializable {

	private BigDecimal lotId;
	private ServiceProvider serviceProvider;
	private ProductOffering productOffering;
	private Date createdDate;
	private BigDecimal scheduledLotSize;
	private BigDecimal lotStatus;
	private BigDecimal initialBalance;
	private Date activationDate;
	private Date processBeginDate;
	private Date processEndDate;
	private Date scheduledDate;
	private BigDecimal actualLotSize;
	private BigDecimal expirationType;
	private Date expirationDate;
	private BigDecimal numExpirationDays;
	private String spLotNumber;
	private String batchId;
	private String lotDescription;
	private String lotControlNumber;
	private BigDecimal unusedExpirationType;
	private BigDecimal unusedExpDays;
	private Date unusedExpDate;
	private Date purgeDate;
	private BigDecimal numPinsPurged;
	private String pinPrefix;
	private char custSerialnumFlag;
	private char allowPinOnlyLoginFlag;
	private Set<PreActivatedSubscribers> preActivatedSubscriberses = new HashSet<PreActivatedSubscribers>(
			0);

	public Lot() {
	}

	public Lot(BigDecimal lotId, ServiceProvider serviceProvider,
			ProductOffering productOffering, Date createdDate,
			BigDecimal scheduledLotSize, BigDecimal lotStatus,
			BigDecimal initialBalance, char custSerialnumFlag,
			char allowPinOnlyLoginFlag) {
		this.lotId = lotId;
		this.serviceProvider = serviceProvider;
		this.productOffering = productOffering;
		this.createdDate = createdDate;
		this.scheduledLotSize = scheduledLotSize;
		this.lotStatus = lotStatus;
		this.initialBalance = initialBalance;
		this.custSerialnumFlag = custSerialnumFlag;
		this.allowPinOnlyLoginFlag = allowPinOnlyLoginFlag;
	}

	public Lot(BigDecimal lotId, ServiceProvider serviceProvider,
			ProductOffering productOffering, Date createdDate,
			BigDecimal scheduledLotSize, BigDecimal lotStatus,
			BigDecimal initialBalance, Date activationDate,
			Date processBeginDate, Date processEndDate, Date scheduledDate,
			BigDecimal actualLotSize, BigDecimal expirationType,
			Date expirationDate, BigDecimal numExpirationDays,
			String spLotNumber, String batchId, String lotDescription,
			String lotControlNumber, BigDecimal unusedExpirationType,
			BigDecimal unusedExpDays, Date unusedExpDate, Date purgeDate,
			BigDecimal numPinsPurged, String pinPrefix, char custSerialnumFlag,
			char allowPinOnlyLoginFlag,
			Set<PreActivatedSubscribers> preActivatedSubscriberses) {
		this.lotId = lotId;
		this.serviceProvider = serviceProvider;
		this.productOffering = productOffering;
		this.createdDate = createdDate;
		this.scheduledLotSize = scheduledLotSize;
		this.lotStatus = lotStatus;
		this.initialBalance = initialBalance;
		this.activationDate = activationDate;
		this.processBeginDate = processBeginDate;
		this.processEndDate = processEndDate;
		this.scheduledDate = scheduledDate;
		this.actualLotSize = actualLotSize;
		this.expirationType = expirationType;
		this.expirationDate = expirationDate;
		this.numExpirationDays = numExpirationDays;
		this.spLotNumber = spLotNumber;
		this.batchId = batchId;
		this.lotDescription = lotDescription;
		this.lotControlNumber = lotControlNumber;
		this.unusedExpirationType = unusedExpirationType;
		this.unusedExpDays = unusedExpDays;
		this.unusedExpDate = unusedExpDate;
		this.purgeDate = purgeDate;
		this.numPinsPurged = numPinsPurged;
		this.pinPrefix = pinPrefix;
		this.custSerialnumFlag = custSerialnumFlag;
		this.allowPinOnlyLoginFlag = allowPinOnlyLoginFlag;
		this.preActivatedSubscriberses = preActivatedSubscriberses;
	}

	public BigDecimal getLotId() {
		return this.lotId;
	}

	public void setLotId(BigDecimal lotId) {
		this.lotId = lotId;
	}

	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public ProductOffering getProductOffering() {
		return this.productOffering;
	}

	public void setProductOffering(ProductOffering productOffering) {
		this.productOffering = productOffering;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getScheduledLotSize() {
		return this.scheduledLotSize;
	}

	public void setScheduledLotSize(BigDecimal scheduledLotSize) {
		this.scheduledLotSize = scheduledLotSize;
	}

	public BigDecimal getLotStatus() {
		return this.lotStatus;
	}

	public void setLotStatus(BigDecimal lotStatus) {
		this.lotStatus = lotStatus;
	}

	public BigDecimal getInitialBalance() {
		return this.initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public Date getActivationDate() {
		return this.activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getProcessBeginDate() {
		return this.processBeginDate;
	}

	public void setProcessBeginDate(Date processBeginDate) {
		this.processBeginDate = processBeginDate;
	}

	public Date getProcessEndDate() {
		return this.processEndDate;
	}

	public void setProcessEndDate(Date processEndDate) {
		this.processEndDate = processEndDate;
	}

	public Date getScheduledDate() {
		return this.scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public BigDecimal getActualLotSize() {
		return this.actualLotSize;
	}

	public void setActualLotSize(BigDecimal actualLotSize) {
		this.actualLotSize = actualLotSize;
	}

	public BigDecimal getExpirationType() {
		return this.expirationType;
	}

	public void setExpirationType(BigDecimal expirationType) {
		this.expirationType = expirationType;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getNumExpirationDays() {
		return this.numExpirationDays;
	}

	public void setNumExpirationDays(BigDecimal numExpirationDays) {
		this.numExpirationDays = numExpirationDays;
	}

	public String getSpLotNumber() {
		return this.spLotNumber;
	}

	public void setSpLotNumber(String spLotNumber) {
		this.spLotNumber = spLotNumber;
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getLotDescription() {
		return this.lotDescription;
	}

	public void setLotDescription(String lotDescription) {
		this.lotDescription = lotDescription;
	}

	public String getLotControlNumber() {
		return this.lotControlNumber;
	}

	public void setLotControlNumber(String lotControlNumber) {
		this.lotControlNumber = lotControlNumber;
	}

	public BigDecimal getUnusedExpirationType() {
		return this.unusedExpirationType;
	}

	public void setUnusedExpirationType(BigDecimal unusedExpirationType) {
		this.unusedExpirationType = unusedExpirationType;
	}

	public BigDecimal getUnusedExpDays() {
		return this.unusedExpDays;
	}

	public void setUnusedExpDays(BigDecimal unusedExpDays) {
		this.unusedExpDays = unusedExpDays;
	}

	public Date getUnusedExpDate() {
		return this.unusedExpDate;
	}

	public void setUnusedExpDate(Date unusedExpDate) {
		this.unusedExpDate = unusedExpDate;
	}

	public Date getPurgeDate() {
		return this.purgeDate;
	}

	public void setPurgeDate(Date purgeDate) {
		this.purgeDate = purgeDate;
	}

	public BigDecimal getNumPinsPurged() {
		return this.numPinsPurged;
	}

	public void setNumPinsPurged(BigDecimal numPinsPurged) {
		this.numPinsPurged = numPinsPurged;
	}

	public String getPinPrefix() {
		return this.pinPrefix;
	}

	public void setPinPrefix(String pinPrefix) {
		this.pinPrefix = pinPrefix;
	}

	public char getCustSerialnumFlag() {
		return this.custSerialnumFlag;
	}

	public void setCustSerialnumFlag(char custSerialnumFlag) {
		this.custSerialnumFlag = custSerialnumFlag;
	}

	public char getAllowPinOnlyLoginFlag() {
		return this.allowPinOnlyLoginFlag;
	}

	public void setAllowPinOnlyLoginFlag(char allowPinOnlyLoginFlag) {
		this.allowPinOnlyLoginFlag = allowPinOnlyLoginFlag;
	}

	public Set<PreActivatedSubscribers> getPreActivatedSubscriberses() {
		return this.preActivatedSubscriberses;
	}

	public void setPreActivatedSubscriberses(
			Set<PreActivatedSubscribers> preActivatedSubscriberses) {
		this.preActivatedSubscriberses = preActivatedSubscriberses;
	}

}