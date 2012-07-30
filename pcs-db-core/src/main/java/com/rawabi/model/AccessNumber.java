package com.rawabi.model;

// Generated Jul 30, 2012 10:19:37 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * AccessNumber generated by hbm2java
 */
public class AccessNumber implements java.io.Serializable {

	private BigDecimal accessNumberId;
	private LanguageRef languageRef;
	private ServiceProvider serviceProvider;
	private BigDecimal accessNumberGroupId;
	private String accessNumber;
	private BigDecimal dialingPlanId;
	private BigDecimal status;
	private BigDecimal countryId;
	private char domIntlFlag;
	private char acdEnabled;
	private char acdCsrGreet;
	private char acdCsrOnMaxLogins;
	private char musicHoldFlag;
	private char thirdPartyAcdAvailable;
	private char applyPayphoneSurcharges;
	private char payphoneSwipeFlag;
	private char callbackFlag;
	private char applicationType;
	private char collectPinFlag;
	private char ivrChargeNcCallsFlag;
	private char directCallFlag;
	private Character mainMenuFlag;
	private Character tollNumberFlag;
	private Character eventFlag;
	private BigDecimal prepaidPinLength;
	private BigDecimal conferencePasscodeLength;
	private BigDecimal thirdPartyOutdialDuration;
	private BigDecimal languageMenuId;
	private String dialedNumber;
	private String thirdPartyOutdialNumber;
	private String description;
	private BigDecimal prepaidCardNumberLength;
	private BigDecimal postpaidAcctNumberLength;

	public AccessNumber() {
	}

	public AccessNumber(BigDecimal accessNumberId, LanguageRef languageRef,
			String accessNumber, BigDecimal dialingPlanId, BigDecimal status,
			BigDecimal countryId, char domIntlFlag, char acdEnabled,
			char acdCsrGreet, char acdCsrOnMaxLogins, char musicHoldFlag,
			char thirdPartyAcdAvailable, char applyPayphoneSurcharges,
			char payphoneSwipeFlag, char callbackFlag, char applicationType,
			char collectPinFlag, char ivrChargeNcCallsFlag, char directCallFlag) {
		this.accessNumberId = accessNumberId;
		this.languageRef = languageRef;
		this.accessNumber = accessNumber;
		this.dialingPlanId = dialingPlanId;
		this.status = status;
		this.countryId = countryId;
		this.domIntlFlag = domIntlFlag;
		this.acdEnabled = acdEnabled;
		this.acdCsrGreet = acdCsrGreet;
		this.acdCsrOnMaxLogins = acdCsrOnMaxLogins;
		this.musicHoldFlag = musicHoldFlag;
		this.thirdPartyAcdAvailable = thirdPartyAcdAvailable;
		this.applyPayphoneSurcharges = applyPayphoneSurcharges;
		this.payphoneSwipeFlag = payphoneSwipeFlag;
		this.callbackFlag = callbackFlag;
		this.applicationType = applicationType;
		this.collectPinFlag = collectPinFlag;
		this.ivrChargeNcCallsFlag = ivrChargeNcCallsFlag;
		this.directCallFlag = directCallFlag;
	}

	public AccessNumber(BigDecimal accessNumberId, LanguageRef languageRef,
			ServiceProvider serviceProvider, BigDecimal accessNumberGroupId,
			String accessNumber, BigDecimal dialingPlanId, BigDecimal status,
			BigDecimal countryId, char domIntlFlag, char acdEnabled,
			char acdCsrGreet, char acdCsrOnMaxLogins, char musicHoldFlag,
			char thirdPartyAcdAvailable, char applyPayphoneSurcharges,
			char payphoneSwipeFlag, char callbackFlag, char applicationType,
			char collectPinFlag, char ivrChargeNcCallsFlag,
			char directCallFlag, Character mainMenuFlag,
			Character tollNumberFlag, Character eventFlag,
			BigDecimal prepaidPinLength, BigDecimal conferencePasscodeLength,
			BigDecimal thirdPartyOutdialDuration, BigDecimal languageMenuId,
			String dialedNumber, String thirdPartyOutdialNumber,
			String description, BigDecimal prepaidCardNumberLength,
			BigDecimal postpaidAcctNumberLength) {
		this.accessNumberId = accessNumberId;
		this.languageRef = languageRef;
		this.serviceProvider = serviceProvider;
		this.accessNumberGroupId = accessNumberGroupId;
		this.accessNumber = accessNumber;
		this.dialingPlanId = dialingPlanId;
		this.status = status;
		this.countryId = countryId;
		this.domIntlFlag = domIntlFlag;
		this.acdEnabled = acdEnabled;
		this.acdCsrGreet = acdCsrGreet;
		this.acdCsrOnMaxLogins = acdCsrOnMaxLogins;
		this.musicHoldFlag = musicHoldFlag;
		this.thirdPartyAcdAvailable = thirdPartyAcdAvailable;
		this.applyPayphoneSurcharges = applyPayphoneSurcharges;
		this.payphoneSwipeFlag = payphoneSwipeFlag;
		this.callbackFlag = callbackFlag;
		this.applicationType = applicationType;
		this.collectPinFlag = collectPinFlag;
		this.ivrChargeNcCallsFlag = ivrChargeNcCallsFlag;
		this.directCallFlag = directCallFlag;
		this.mainMenuFlag = mainMenuFlag;
		this.tollNumberFlag = tollNumberFlag;
		this.eventFlag = eventFlag;
		this.prepaidPinLength = prepaidPinLength;
		this.conferencePasscodeLength = conferencePasscodeLength;
		this.thirdPartyOutdialDuration = thirdPartyOutdialDuration;
		this.languageMenuId = languageMenuId;
		this.dialedNumber = dialedNumber;
		this.thirdPartyOutdialNumber = thirdPartyOutdialNumber;
		this.description = description;
		this.prepaidCardNumberLength = prepaidCardNumberLength;
		this.postpaidAcctNumberLength = postpaidAcctNumberLength;
	}

	public BigDecimal getAccessNumberId() {
		return this.accessNumberId;
	}

	public void setAccessNumberId(BigDecimal accessNumberId) {
		this.accessNumberId = accessNumberId;
	}

	public LanguageRef getLanguageRef() {
		return this.languageRef;
	}

	public void setLanguageRef(LanguageRef languageRef) {
		this.languageRef = languageRef;
	}

	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public BigDecimal getAccessNumberGroupId() {
		return this.accessNumberGroupId;
	}

	public void setAccessNumberGroupId(BigDecimal accessNumberGroupId) {
		this.accessNumberGroupId = accessNumberGroupId;
	}

	public String getAccessNumber() {
		return this.accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public BigDecimal getDialingPlanId() {
		return this.dialingPlanId;
	}

	public void setDialingPlanId(BigDecimal dialingPlanId) {
		this.dialingPlanId = dialingPlanId;
	}

	public BigDecimal getStatus() {
		return this.status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public BigDecimal getCountryId() {
		return this.countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public char getDomIntlFlag() {
		return this.domIntlFlag;
	}

	public void setDomIntlFlag(char domIntlFlag) {
		this.domIntlFlag = domIntlFlag;
	}

	public char getAcdEnabled() {
		return this.acdEnabled;
	}

	public void setAcdEnabled(char acdEnabled) {
		this.acdEnabled = acdEnabled;
	}

	public char getAcdCsrGreet() {
		return this.acdCsrGreet;
	}

	public void setAcdCsrGreet(char acdCsrGreet) {
		this.acdCsrGreet = acdCsrGreet;
	}

	public char getAcdCsrOnMaxLogins() {
		return this.acdCsrOnMaxLogins;
	}

	public void setAcdCsrOnMaxLogins(char acdCsrOnMaxLogins) {
		this.acdCsrOnMaxLogins = acdCsrOnMaxLogins;
	}

	public char getMusicHoldFlag() {
		return this.musicHoldFlag;
	}

	public void setMusicHoldFlag(char musicHoldFlag) {
		this.musicHoldFlag = musicHoldFlag;
	}

	public char getThirdPartyAcdAvailable() {
		return this.thirdPartyAcdAvailable;
	}

	public void setThirdPartyAcdAvailable(char thirdPartyAcdAvailable) {
		this.thirdPartyAcdAvailable = thirdPartyAcdAvailable;
	}

	public char getApplyPayphoneSurcharges() {
		return this.applyPayphoneSurcharges;
	}

	public void setApplyPayphoneSurcharges(char applyPayphoneSurcharges) {
		this.applyPayphoneSurcharges = applyPayphoneSurcharges;
	}

	public char getPayphoneSwipeFlag() {
		return this.payphoneSwipeFlag;
	}

	public void setPayphoneSwipeFlag(char payphoneSwipeFlag) {
		this.payphoneSwipeFlag = payphoneSwipeFlag;
	}

	public char getCallbackFlag() {
		return this.callbackFlag;
	}

	public void setCallbackFlag(char callbackFlag) {
		this.callbackFlag = callbackFlag;
	}

	public char getApplicationType() {
		return this.applicationType;
	}

	public void setApplicationType(char applicationType) {
		this.applicationType = applicationType;
	}

	public char getCollectPinFlag() {
		return this.collectPinFlag;
	}

	public void setCollectPinFlag(char collectPinFlag) {
		this.collectPinFlag = collectPinFlag;
	}

	public char getIvrChargeNcCallsFlag() {
		return this.ivrChargeNcCallsFlag;
	}

	public void setIvrChargeNcCallsFlag(char ivrChargeNcCallsFlag) {
		this.ivrChargeNcCallsFlag = ivrChargeNcCallsFlag;
	}

	public char getDirectCallFlag() {
		return this.directCallFlag;
	}

	public void setDirectCallFlag(char directCallFlag) {
		this.directCallFlag = directCallFlag;
	}

	public Character getMainMenuFlag() {
		return this.mainMenuFlag;
	}

	public void setMainMenuFlag(Character mainMenuFlag) {
		this.mainMenuFlag = mainMenuFlag;
	}

	public Character getTollNumberFlag() {
		return this.tollNumberFlag;
	}

	public void setTollNumberFlag(Character tollNumberFlag) {
		this.tollNumberFlag = tollNumberFlag;
	}

	public Character getEventFlag() {
		return this.eventFlag;
	}

	public void setEventFlag(Character eventFlag) {
		this.eventFlag = eventFlag;
	}

	public BigDecimal getPrepaidPinLength() {
		return this.prepaidPinLength;
	}

	public void setPrepaidPinLength(BigDecimal prepaidPinLength) {
		this.prepaidPinLength = prepaidPinLength;
	}

	public BigDecimal getConferencePasscodeLength() {
		return this.conferencePasscodeLength;
	}

	public void setConferencePasscodeLength(BigDecimal conferencePasscodeLength) {
		this.conferencePasscodeLength = conferencePasscodeLength;
	}

	public BigDecimal getThirdPartyOutdialDuration() {
		return this.thirdPartyOutdialDuration;
	}

	public void setThirdPartyOutdialDuration(
			BigDecimal thirdPartyOutdialDuration) {
		this.thirdPartyOutdialDuration = thirdPartyOutdialDuration;
	}

	public BigDecimal getLanguageMenuId() {
		return this.languageMenuId;
	}

	public void setLanguageMenuId(BigDecimal languageMenuId) {
		this.languageMenuId = languageMenuId;
	}

	public String getDialedNumber() {
		return this.dialedNumber;
	}

	public void setDialedNumber(String dialedNumber) {
		this.dialedNumber = dialedNumber;
	}

	public String getThirdPartyOutdialNumber() {
		return this.thirdPartyOutdialNumber;
	}

	public void setThirdPartyOutdialNumber(String thirdPartyOutdialNumber) {
		this.thirdPartyOutdialNumber = thirdPartyOutdialNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrepaidCardNumberLength() {
		return this.prepaidCardNumberLength;
	}

	public void setPrepaidCardNumberLength(BigDecimal prepaidCardNumberLength) {
		this.prepaidCardNumberLength = prepaidCardNumberLength;
	}

	public BigDecimal getPostpaidAcctNumberLength() {
		return this.postpaidAcctNumberLength;
	}

	public void setPostpaidAcctNumberLength(BigDecimal postpaidAcctNumberLength) {
		this.postpaidAcctNumberLength = postpaidAcctNumberLength;
	}

}
