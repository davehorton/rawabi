package com.rawabi.constants;

public class PactolusConstants {

	public static final int SUCCESS = 0;
	
    //Subscriber expiration types
	public static final int NO_EXPIRATION = 0;
	public static final int LAST_USE_EXPIRATION = 1;
	public static final int FIXED_DATE_EXPIRATION = 2;
	public static final int FIRST_USE_EXPIRATION = 3;
	public static final int DAYS_FROM_ACTIVATION = 4;
	public static final int FIRST_USE_FIXED_DATE_EXPIRATION = 5;

	// prepaid lot generation errors
	public static final int LOT_GEN_ERROR_REPORT = -2;
	public static final int LOT_GEN_ERROR_INVALID_RANGE = -3;
	public static final int LOT_GEN_ERROR_INVALID_BATCH = -4;
	
	public static final long LOT_SEQ_START_VALUE_0 = 10000000;
	public static final long LOT_SEQ_START_VALUE_1 = 10000001;
	public static final int LOT_SEQ_NUMBER_LENGTH = 8;
	public static final int LOT_MAX_QUERY_PIN_STATUS = 10000;
	public static final int LOT_MAX_ROWNUM_UPDATE = 10000;
	public static final int LOT_MAX_API_PIN_GENERATION = 10000;
	public static final int LOT_MAX_ACE_PIN_GENERATION = 1000;
	
	//auth ani codes
	public static final String ANI_NOT_RECOGNIZED = "0";
	public static final String ANI_VALID = "1";
	public static final String ANI_VALID_PROMPT_PIN = "2";
	public static final String ANI_INACTIVE = "3";
	
	public static final int PROV_ERROR_INVALID_DATA = 99;
	
	//CDR field lengths
	public static final int CDR_LENGTH_SIP_CALLID = 100;
	public static final int CDR_LENGTH_SIP_TOFROM = 256;
	public static final int CDR_LENGTH_INFO_DIGITS = 4;
	public static final int CDR_LENGTH_TRUNK_GROUP = 40;
	
	
	public static final int UNBILLED_REASON_NOT_CONNECTED = 1;
	public static final int UNBILLED_REASON_BELOW_MINIMUM_DURATION = 2;
	public static final int UNBILLED_REASON_OPERATOR_CALL = 3;
	public static final int UNBILLED_POPD_NO_RATING = 4;
	public static final int UNBILLED_CSR_TRANSFER = 5;
	
	
	public static final String SYSTEM_DB_NAME = "pcssystemdb";
	public static final String REPORTING_DB_NAME = "reportingdb";
	public static final String PROD_DB_NAME = "pactolusdb";
	
	public static final int DB_VENDOR_ORACLE = 1;
	public static final int DB_VENDOR_SYBASE = 2;
	public static final int DB_VENDOR_TIMES_TEN = 3;
	public static final int DB_VENDOR_MS_SQL_SERVER = 4;
	public static final int DB_VENDOR_MYSQL = 5;
	public static final int DB_VENDOR_POSTGRES = 6;
	
	public static final int PROC_DB = 1;
	public static final int SYS_DB = 2;
	public static final int REPORT_DB = 3;
	
	    public static final String DB_TO_CHAR_FORMAT = "9999999999";
	
	//Termination Reasons
	public static final int NO_CALL_TERM_RSN = 1; //A call was not placed for this session
	
	//Service Element Types
	public static final int UNKNOWN_SERVICE = 0;
	public static final int PREPAID_CALLING_CARD_SERVICE = 1;
	public static final int POSTPAID_CONFERENCING_SERVICE = 2;
	public static final int POSTPAID_CALLING_SERVICE = 3;
	public static final int PERSONAL_TOLLFREE_SERVICE=4;
	public static final int PREPAID_CONFERENCING_SERVICE = 5;
	public static final int VOIP_CALLING_SERVICE = 6;
	public static final int VOICEMAIL_SERVICE = 7;
	public static final int PRPD_OR_POPD_SERVICE_TYPE = 8;
	public static final int EVENT_CONFERENCING_SERVICE_TYPE = 9;
	public static final int NON_EVENT_CONFERENCING_SERVICE_TYPE = 10;
	public static final int CONFERENCING_SERVICE = 99;  // Generic conferencing for call history
	
	
	//
	public static final int MAX_PP_CALL_DURATION = 7200;//max allowed prepaid call, only used if no metered rates apply
	
	// Number of Rates
	public static final int TOTAL_POPD_RATES = 12;   // Used when looping through all potential rates
	public static final int TOTAL_PRPD_RATES = 16;
	public static final int RATE_END_DATE_YEARS_OUT = 10;
	
	// Maximum number of rates to bring back in a query or to update
	public static final int MAX_QUERY_RATES = 10000;
	// Maximum number of rows to bring back for payphone numbers
	public static final int MAX_PAYPHONE_ROWS = 100;
	// Maximum number of subscriber records to update at a given time
	public static final int MAX_SUBSCRIBER_UPDATES = 5000;
	
	// Maximum types of rates allowed
	public static final int MAX_PAYPHONE_SURCHARGE_RATES = 2;
	public static final int MAX_ACCESS_NUMBER_RATES = 2;
	
	// Rate Precision
	public static final int RATE_DIGIT_PRECISION = 6;
	
	//Rate Types  
	public static final int PER_EVENT_RATE = 1;  //fixed (e.g. 5c per call)
	public static final int METERED_RATE = 2;    //cents per minute (e.g. 5c/min per call)
	public static final int FIXED_FEE = 3;
	public static final int VARIABLE_FEE = 4;
	
	// Sub Rate Types
	public static final int LOCATION_BASED_RATE = 1;
	public static final int TIME_AND_DAY_OF_WEEK_RATE = 2;
	public static final int ACCESS_NUMBER_RATE = 3;
	public static final int DESTINATION_BASED_RATE = 4;
	public static final int ORIGINATION_BASED_RATE = 5;
	public static final int CALLING_REGION_RATE = 6;
	public static final int ON_NET_SUB_RATE = 7;
	public static final int BUCKET_SUB_RATE = 8;
	
	//Rate Calculation Types
	public static final int USER_DEFINED_RATE_CALC = 0;
	
	
	public static final int DOM_ORIG_DEST_TDW_CALC = 1;
	public static final int DOM_DEST_CALC = 2;
	public static final int INTL_ORIG_CALC = 3;
	public static final int INTL_DEST_CALC = 4;
	public static final int DOM_ORIG_CALC = 5;
	public static final int PAYPHONE_SURCHARGE_RATE_CALC = 6;
	public static final int ACCESS_NUM_RATE_CALC = 7;
	public static final int MATRIX_RATE_CALC = 8;
	public static final int ON_NET_RATE_CALC = 9;
	public static final int GLOBAL_MATRIX_RATE_CALC = 10; 
	public static final int BUCKET_RATE_CALC = 11;
	public static final int ALL_CALC_TYPES = 999;  // for gui dropdown
	
	//Subrate not found options
	public static final int NO_SUBRATE_USE_DEFAULT = 1;
	public static final int NO_SUBRATE_NO_RATE = 2;
	public static final int NO_SUBRATE_BLOCK_EVENT = 3;
	
	//Subrate not found option descriptions - can't use code_reference table because of potential null values
	public static final String NO_SUBRATE_USE_DEFAULT_DESC = "Apply Default Rate";
	public static final String NO_SUBRATE_NO_RATE_DESC = "Apply No Rate";
	public static final String NO_SUBRATE_BLOCK_EVENT_DESC = "Block Event";
	
	    
	//Subrate not found options for parent Bucket.
	public static final int NO_SUBRATE_USE_DEFAULT_FOR_PARENT = 1;
	    
	public static final int NO_SUBRATE_BLOCK_EVENT_FOR_PARENT = 3;
	    
	//Subrate not found option descriptions - can't use code_reference table because of potential null values
	
	public static final String NO_SUBRATE_USE_DEFAULT_DESC_FOR_PARENT = "Use Parent Bucket";
	
	public static final String NO_SUBRATE_BLOCK_EVENT_DESC_FOR_PARENT = "Block Call";
	
	
	
	
	
	
	
	//Event Types
	public static final int PREPAID_CALL_EVENT = 1;
	public static final int OFFERING_SIGNUP_EVENT = 2;
	public static final int CREDIT_CARD_RECHARGE_EVENT = 3;
	public static final int MONTHLY_FEE_EVENT = 4;
	public static final int BALANCE_XFER_EVENT = 5;
	public static final int SUB_EXPIRATION_EVENT = 6;
	public static final int BAL_ADJUSTMENT_EVENT = 7;
	public static final int CONF_CALL_EVENT = 8;
	public static final int FIRST_USE_EVENT = 9;
	public static final int MAINT_FEE_EVENT = 10;
	public static final int SCHEDULE_CONF_CALL_EVENT = 11;
	public static final int BALANCE_SWEEP_EVENT = 12;
	public static final int POSTPAID_CALL_EVENT = 13;
	public static final int BAL_ADJUSTMENT_CREDIT_USED_EVENT = 14;
	public static final int POS_RECHARGE_EVENT = 15;
	
	public static final int POS_REFUND_EVENT = 16;
	public static final int BROADBAND_CALL_EVENT = 17;
	public static final int PREPAID_CALL_SESSION_EVENT = 18;
	public static final int EXTERNAL_PAYMENT_EVENT = 19;
	public static final int EXTERNAL_BALANCE_ADJ_EVENT = 20;
	public static final int BAL_ADJUSTMENT_CREDIT_LIMIT_EVENT = 21;
	public static final int BAL_ADJUSTMENT_MINUTES_BALANCE_EVENT = 23;
	public static final int BAL_ADJUSTMENT_MINUTES_USED_EVENT = 24;
	public static final int SUBSCRIPTION_FEE_EVENT = 25;
	public static final int ADD_ON_PURCHASE_EVENT = 26;
	public static final int RECURRING_ADD_ON_FEE_EVENT = 27;
	public static final int TELECOM_SERVICE_SURCHARGE = 28;
	public static final int POSTPAID_CONFERENCE_EVENT = 29;
	public static final int MAINT_FEE2_EVENT = 30;
	public static final int MAINT_FEE3_EVENT = 31;
	
	//Batch Process Types 
	//DON'T ADD A VALUE HERE UNLESS YOU ADD THE CORRESPONDING VALUE CODE_REFERENCE
	//There are records in there that are not listed here because they are not used by the java.
	//Code_reference should be the master list.
	public static final int RECURRING_RECHARGE_BATCH_TYPE = 1;
	public static final int SUB_EXPIRATION_BATCH_TYPE = 2;
	public static final int PIN_GEN_BATCH_TYPE = 3;
	public static final int MONTHLY_SURCHARGE_BATCH_TYPE = 4;
	public static final int MAINT_FEE_BATCH_TYPE = 34;
	public static final int UPDATE_NETXUSA_ORDER_STATUS_BATCH_TYPE = 43;
	public static final int MOVE_EXPIRED_RATES_BATCH_TYPE = 53;
	public static final int DB_PURGE_BATCH_TYPE = 57;
	public static final int COST_ROLLUP_TYPE = 63;
	public static final int POPD_CREDIT_RESET_BATCH_TYPE = 66;
	public static final int ACD_DATA_COLLECTION_TYPE = 67 ;
	public static final int UNUSED_PIN_EXP_BATCH_TYPE = 70;
	public static final int SUB_ADJUSTMENTS_BATCH_TYPE = 82;
	public static final int SP_CREDIT_USAGE_ROLLUP_BATCH_TYPE = 98;
	public static final int BILLING_CYCLE_MAINT_BATCH_TYPE = 99;
	public static final int RECURRING_BATCH_TYPE = 101;
	
	public static final int VOIP_RELEASE_PHONE_NUMBERS_BATCH_TYPE = 105;
	
	public static final int VOIP_EXPIRE_TEMPORARY_PHONE_NUMBERS_BATCH_TYPE = 110;
	public static final int VOIP_RELEASE_LOCKED_PHONE_NUMBERS_BATCH_TYPE = 115;
	
	// bucket notification types
	public static final String BUCKET_NOTIFICATION_DAY = "DAY";
	public static final String BUCKET_NOTIFICATION_MONTH = "MONTH";
	public static final String BUCKET_NOTIFICATION_SUSPENDED = "S";
	public static final String BUCKET_NOTIFICATION_NOTSUSPENDED = "";
	public static final String BUCKET_NOTIFICATION_DAILY_USAGE = "0";
	public static final String BUCKET_NOTIFICATION_BUCKET_REFILL = "1";
	public static final String BUCKET_NOTIFICATION_BUCKET_EXHAUST = "2";
	
	public static final String BUCKET_NOTIFICATION_BUCKET_UNREFILL = "3";
	
	//Session Types
	public static final int PLATFORM_SESSION = 1;
	public static final int WEB_SESSION = 2;
	public static final int BATCH_SESSION = 3;
	public static final int PROVISIONING_SESSION = 4;
	// Web Session Types
	public static final int ADMIN_SESSION = 1;
	public static final int POPD_SESSION = 2;
	public static final int PRPD_SESSION = 3;
	public static final int ACD_SESSION = 4;
	public static final int API_SESSION = 5;
	public static final int BROADBAND_SESSION = 6;
	public static final int OFFICE_MANAGER_SESSION = 7;
	
	//Domestic/International Flags
	public static final String INTL_FLAG = "I";
	public static final String DOM_FLAG = "D";
	public static final String DOM_INTL_FLAG = "U";
	
	//Debit or Credit
	public static final String CC_DEBIT = "D";
	public static final String CC_CREDIT = "C";
	public static final String CC_AUTH = "A";
	
	//Origination or Destination
	public static final String ORIGINATION = "O";
	public static final String DESTINATION = "D";
	
	
	//The following are for disabled reason code for a subscriber
	public static final int DISABLED_FOR_FRAUD = 1;
	public static final int DISABLED_FOR_BAL_XFER = 2;
	public static final int DISABLED_EXPIRED = 3;
	public static final int DISABLED_EXCEED_MAX_BAL_XFER = 4;
	public static final int POPD_DISABLED_PENDING_AUTHORIZATION = 5;
	public static final int POPD_DISABLED_REJECTED_APPLICATION = 6;
	public static final int POPD_CANCELLED_ACCOUNT = 7;
	public static final int DISABLED_CANCELLED_LOT = 8;
	public static final int DISABLED_SUSPENDED_LOT = 9;
	public static final int DISABLED_PENDING_PAYMENT = 10;
	public static final int DISABLED_POS_REFUND = 11;
	public static final int DISABLED_BATCH_INTERFACE = 12;
	public static final int POPD_DISABLED_FRAUD_SUSPICION = 13;
	public static final int POPD_DISABLED_CREDIT_LIMIT_EXCEEDED = 14;
	public static final int SUSPENDED_SUBSCRIBER = 15;
	public static final int POPD_DISABLED_BUCKET_EXHAUSTED = 16;
	public static final int UNSPECIFIED_PIN_CANCELLATION = 17;
	public static final int POPD_DISABLED_BATCH_CREDIT_CARD_FAILURE = 18;
	public static final int DISABLED_FAILED_CREDIT_RISK_CHECK = 19;
	public static final int DISABLED_PENDING_E911_SETUP = 20;
	public static final int POPD_DISABLED_SUBSCRIPTION_FEE_FAILURE = 21;
	public static final int MANUAL_PIN_CANCELLATION = 22;
	
	// Subscriber statuses
	public static final int SUB_ACTIVE_STATUS = 0;
	public static final int SUB_PENDING_STATUS = 1;
	
	// General Statuses
	public static final int GEN_ACTIVE_STATUS = 0;
	public static final int GEN_INACTIVE_STATUS = 1;
	
	// General Statuses (code_reference)
	public static final int STATUS_ACTIVE   = 0;
	public static final int STATUS_PENDING  = 1;
	public static final int STATUS_INACTIVE = 2;
	public static final int STATUS_DISABLED = 3;
	
	//Credit card recharge initiated types
	public static final int CC_INITIATED_SUBSCRIBER = 0;
	public static final int CC_INITIATED_ADMIN = 1;
	public static final int CC_INITIATED_AUTO = 2;
	public static final int CC_INITIATED_ACD = 3;
	public static final int CC_INITIATED_PORTAL = 4;
	public static final int CC_INITIATED_EXTERNAL = 5;
	
	//Credit card fraud types
	public static final int CC_PER_MONTH_VIOLATION = -2;
	public static final int CC_PER_WEEK_VIOLATION = -3;
	public static final int SUB_PER_MONTH_VIOLATION = -4;
	public static final int SUB_PER_WEEK_VIOLATION = -5;
	
	//default cc settings in case not set in product offering
	float MIN_PP_BALANCE = (float) 0;
	float MAX_PP_BALANCE = (float) 2000;
	float MIN_CC_RECHARGE = (float) 1;
	float MAX_CC_RECHARGE = (float) 500;
	float CC_MAX_SUB_PER_WEEK = (float) 300;
	float CC_MAX_SUB_PER_MONTH = (float) 500;
	float CC_MAX_CARD_PER_WEEK = (float) 300;
	float CC_MAX_CARD_PER_MONTH = (float) 500;
	
	// Credit Card Processor
	public static final int CC_PROCESSOR_CYBERSOURCE = 1;
	public static final int CC_PROCESSOR_AUTH_NET = 2;
	public static final int CC_PROCESSOR_GENERIC = 3;
	public static final int CC_PROCESSOR_PCI_COMPLIANT = 4;
	
	// Credit Card Encryption Types
	public static final int CC_ENCRYPTION_PACTOLUS = 1;
	public static final int CC_ENCRYPTION_PGP = 2;
	
	// Credit Card Types
	public static final int CC_TYPE_VISA = 1;
	public static final int CC_TYPE_MC = 2;
	public static final int CC_TYPE_AMEX = 3;
	public static final int CC_TYPE_DSC = 4;
	public static final int CC_TYPE_JCB = 5;
	public static final int CC_TYPE_DINERS = 6;
	
	// Credit Card Number Prefixes
	String[] CC_PREFIX_VISA = {"4"};    
	String[] CC_PREFIX_MC = {"51","52","53","54","55"};
	String[] CC_PREFIX_AMEX = {"34","37"};
	String[] CC_PREFIX_DSC = {"6011"};
	String[] CC_PREFIX_DINERS = {"36","38","300","301","302","303","304","305"};    
	String[] CC_PREFIX_JCB_A = {"3"};
	String[] CC_PREFIX_JCB_B = {"1800", "2131"};
	
	// Credit Card Number Lengths
	int[] CC_LENGTH_VISA = {13,16};
	int[] CC_LENGTH_MC = {16};
	int[] CC_LENGTH_AMEX = {15};
	int[] CC_LENGTH_DSC = {16};
	int[] CC_LENGTH_DINERS = {14};      
	int[] CC_LENGTH_JCB_A = {16};
	int[] CC_LENGTH_JCB_B = {15};
	   
	    // Product Offering Billing Types
	public static final int PREPAID_PRODUCT_OFFERING = 1;
	public static final int POSTPAID_PRODUCT_OFFERING = 2;
	
	//Admin Audit Types
	public static final int BAD_ANI_DELETE_AUDIT = 1;
	public static final int PIN_LOCK_DELETE_AUDIT = 2;
	public static final int SERVICE_PROVIDER_CREATE_AUDIT = 3;
	public static final int SERVICE_PROVIDER_MODIFY_AUDIT = 4;
	public static final int POPD_SUBSCRIBER_MODIFY_AUDIT = 5;
	public static final int PRPD_SUBSCRIBER_PHONE_CREATE_AUDIT = 6;
	public static final int PRPD_SUBSCRIBER_PHONE_MODIFY_AUDIT = 7;
	public static final int PRPD_SUBSCRIBER_PHONE_DELETE_AUDIT = 8;
	public static final int SUBSCRIBER_REVERSE_CC_AUDIT = 9;
	public static final int PRPD_SUBSCRIBER_REVERSE_CALL_AUDIT = 10;
	public static final int PRPD_SUBSCRIBER_CC_MODIFY_AUDIT = 11;
	public static final int SUBSCRIBER_RECHARGE_ONE_TIME_AUDIT = 12;
	public static final int SUBSCRIBER_RECHARGE_RECURRING_AUDIT = 13;
	public static final int SUBSCRIBER_RECHARGE_DELETE_AUDIT = 14;
	public static final int SERVICE_PROVIDER_PROMPT_CONFIG_AUDIT = 15;
	public static final int SERVICE_PROVIDER_PROMPT_INSERT_AUDIT = 16;
	public static final int SERVICE_PROVIDER_PROMPT_MODIFY_AUDIT = 17;
	public static final int SERVICE_PROVIDER_PROMPT_DELETE_AUDIT = 18;
	public static final int SVC_PRVDR_ACCESS_NBR_INSERT_AUDIT = 19;
	public static final int SVC_PRVDR_ACCESS_NBR_MODIFY_AUDIT = 20;
	public static final int SVC_PRVDR_LOT_CREATE_AUDIT = 21;
	public static final int SVC_PRVDR_LOT_MODIFY_AUDIT = 22;
	public static final int SVC_PRVDR_LOT_CANCEL_AUDIT = 23;
	public static final int SVC_PRVDR_LOT_ACTIVATE_AUDIT = 24;
	public static final int SVC_PRVDR_LOT_GENERATE_AUDIT = 25;
	public static final int SVC_PRVDR_X_PIN_RANGE_INSERT_AUDIT = 26;
	public static final int SVC_PRVDR_X_PIN_RANGE_MODIFY_AUDIT = 27;
	public static final int SVC_PRVDR_X_PIN_RANGE_DELETE_AUDIT = 28;
	public static final int SVC_THRESHOLD_PROMPTS_INSERT_AUDIT = 29;
	public static final int SVC_THRESHOLD_PROMPTS_MODIFY_AUDIT = 30;
	public static final int CALL_RESTRICTION_GROUP_INSERT_AUDIT = 31;
	public static final int CALL_RESTRICTION_GROUP_MODIFY_AUDIT = 32;
	public static final int CALL_RESTRICTION_GROUP_DELETE_AUDIT = 33;
	public static final int CALL_RESTRICTIONS_INSERT_AUDIT = 34;
	public static final int CALL_RESTRICTIONS_MODIFY_AUDIT = 35;
	public static final int CALL_RESTRICTIONS_DELETE_AUDIT = 36;
	public static final int CALL_RESTRICTION_SERVICE_CONFIG_AUDIT = 37;
	public static final int PRPD_SUBSCRIBER_TO_BALANCE_XFER_AUDIT = 38;
	public static final int PRPD_SUBSCRIBER_FROM_BALANCE_XFER_AUDIT = 39;
	public static final int ACCESS_NUMBER_SERVICE_CONFIG_AUDIT = 40;
	public static final int DAY_OF_WEEK_RATE_INSERT_AUDIT = 41;
	public static final int DAY_OF_WEEK_RATE_MODIFY_AUDIT = 42;
	public static final int DAY_OF_WEEK_RATE_DELETE_AUDIT = 43;
	public static final int LOCATION_BASED_RATE_INSERT_AUDIT = 44;
	public static final int LOCATION_BASED_RATE_MODIFY_AUDIT = 45;
	public static final int LOCATION_BASED_RATE_DELETE_AUDIT = 46;
	public static final int CURRENCY_SUPPORT_INSERT_AUDIT = 47;
	public static final int CURRENCY_SUPPORT_MODIFY_AUDIT = 48;
	public static final int CURRENCY_SUPPORT_DELETE_AUDIT = 49;
	public static final int LANGUAGE_SUPPORT_AUDIT = 50;
	public static final int PRPD_SUBSCRIBER_REVERSE_SIGNUP_FEE_AUDIT = 51;
	public static final int PRPD_SUBSCRIBER_REVERSE_MONTHLY_FEE_AUDIT = 52;
	public static final int RATE_INSERT_AUDIT = 53;
	public static final int RATE_MODIFY_AUDIT = 54;
	public static final int RATE_DELETE_AUDIT = 55;
	public static final int PRODUCT_OFFERING_INSERT_AUDIT = 56;
	public static final int PRODUCT_OFFERING_MODIFY_AUDIT = 57;
	public static final int SUB_RATE_EXPIRED_CREATED_AUDIT = 58;
	public static final int RATE_EXPIRED_CREATED_AUDIT = 59;
	public static final int BALANCE_ADJUSTMENT_AUDIT = 60;
	public static final int RATE_SHARE_AUDIT = 61;
	public static final int RATE_COPY_AUDIT = 62;
	public static final int ACCESS_NUMBER_RATE_INSERT_AUDIT = 63;
	public static final int ACCESS_NUMBER_RATE_MODIFY_AUDIT = 64;
	public static final int ACCESS_NUMBER_RATE_DELETE_AUDIT = 65;
	public static final int ACCEPT_PENDING_SUBSCRIBER_AUDIT = 66;
	public static final int REJECT_PENDING_SUBSCRIBER_AUDIT = 67;
	public static final int POPD_PIN_MODIFY_AUDIT = 68;
	public static final int POPD_SUBSCRIBER_INSERT_AUDIT = 69;
	public static final int CALLING_SVC_INSERT_POPD_AUDIT = 70;
	public static final int CALLING_SVC_MODIFY_POPD_AUDIT = 71;
	public static final int POPD_SUBSCRIBER_OFFERING_ASSOC_AUDIT = 72;
	public static final int PIN_INSERT_POPD_AUDIT = 73;
	public static final int POPD_SUB_CONFERENCE_INSERT_AUDIT = 74;
	public static final int POPD_SUB_CONFERENCE_MODIFY_AUDIT = 75;
	public static final int POPD_SUB_CONFERENCE_DELETE_AUDIT = 76;
	public static final int POPD_CORP_ACCOUNT_INSERT_AUDIT = 77;
	public static final int POPD_CORP_ACCOUNT_MODIFY_AUDIT = 78;
	public static final int POPD_CORP_ACCOUNT_CANCEL_AUDIT = 79;
	public static final int POPD_SUBSCRIBER_CANCEL_AUDIT = 80;
	public static final int MUSIC_ON_HOLD_SUPPORT_AUDIT = 81;
	public static final int OFFERING_EMAIL_CREATE_AUDIT = 82;
	public static final int OFFERING_EMAIL_MODIFY_AUDIT = 83;
	public static final int PRPD_SUBSCRIBER_INSERT_AUDIT = 84;
	public static final int PRPD_SUBSCRIBER_MODIFY_AUDIT = 85;
	public static final int CALLING_SVC_INSERT_PRPD_AUDIT = 86;
	public static final int CALLING_SVC_MODIFY_PRPD_AUDIT = 87;
	public static final int PRPD_SUB_OPTIONS_INSERT_AUDIT = 88;
	public static final int PRPD_SUB_OPTIONS_MODIFY_AUDIT = 89;
	public static final int SVC_PRVDR_LOT_SUSPEND_AUDIT = 90;
	public static final int SVC_PRVDR_LOT_UNSUSPEND_AUDIT = 91;
	public static final int SUBSCRIBER_REVERSE_MAINTENANCE_FEE_AUDIT = 92;
	public static final int SUBSCRIBER_REVERSE_BALANCE_SWEEP_AUDIT = 93;
	public static final int SUBSCRIBER_REVERSE_DEFAULT_AUDIT = 94;
	public static final int SERVICE_PROVIDER_DELETE_AUDIT = 95;
	public static final int CONF_SUB_ACCESS_NBR_CONFIG_AUDIT = 96;
	public static final int VOIP_CALLING_SVC_INSERT_AUDIT = 97;
	public static final int VOIP_CALLING_SVC_MODIFY_AUDIT = 98;
	public static final int VOICEMAIL_SVC_INSERT_AUDIT = 99;
	public static final int VOICEMAIL_SVC_MODIFY_AUDIT = 100;
	public static final int POPD_SUB_VOIP_ACCESS_LINE_INSERT_AUDIT = 101;
	public static final int POPD_SUB_VOIP_ACCESS_LINE_MODIFY_AUDIT = 102;
	public static final int POPD_SUB_VOIP_ACCESS_LINE_STATUS_AUDIT = 103;
	public static final int CALLING_REGION_INSERT_AUDIT = 104;
	public static final int CALLING_REGION_DELETE_AUDIT = 105;
	public static final int CALLING_REGION_UPDATE_AUDIT = 106;
	public static final int SUBSCRIBER_DISABLED_AUDIT = 107;
	public static final int SUBSCRIBER_ENABLED_AUDIT = 108;
	public static final int PRODUCT_OFFERING_DELETE_AUDIT = 109;
	public static final int CALLING_SVC_DELETE_AUDIT = 110;
	public static final int VOICEMAIL_SVC_DELETE_AUDIT = 111;
	public static final int VOIP_CALLING_SVC_DELETE_AUDIT = 112;
	public static final int CONFERENCE_SVC_INSERT_AUDIT = 113;
	public static final int CONFERENCE_SVC_MODIFY_AUDIT = 114;
	public static final int CONFERENCE_SVC_DELETE_AUDIT = 115;
	public static final int SVC_PRVDR_LOT_GROUP_SUSPEND_AUDIT = 116;
	public static final int SVC_PRVDR_LOT_GROUP_UNSUSPEND_AUDIT = 117;   
	public static final int SVC_PRVDR_LOT_GROUP_CANCEL_AUDIT = 118;
	public static final int SVC_PRVDR_LOT_GROUP_MODIFY_AUDIT = 119;
	public static final int SVC_PRVDR_LOT_GROUP_ACTIVATE_AUDIT = 120;
	public static final int SUBSCRIBER_CHANGE_PRIMARY_OFFERING_AUDIT = 121;
	public static final int SUBSCRIBER_REVERSE_VOIP_CALL_EVENT_AUDIT = 122;
	public static final int SUBSCRIBER_REVERSE_FIRST_USE_FEE_AUDIT = 123;
	public static final int SUBSCRIBER_REVERSE_SCHED_CONF_FEE_AUDIT = 124;
	public static final int SUBSCRIBER_REVERSE_CONF_CALL_AUDIT = 125;
	public static final int SUBSCRIBER_CREDIT_LIMIT_AUDIT = 126;
	public static final int SUBSCRIBER_CREDIT_USAGE_AUDIT = 127;
	public static final int SUBSCRIBER_EXTERNAL_PAYMENT_AUDIT = 128;
	public static final int SVC_PRVDR_LANGUAGE_MENU_INSERT_AUDIT = 129;
	public static final int SVC_PRVDR_LANGUAGE_MENU_MODIFY_AUDIT = 130;
	public static final int SVC_PRVDR_CREDIT_BAL_ADJUSTMENT = 131;
	public static final int CREATE_ACCOUNT_AUDIT = 132;
	public static final int UNKNOWN_FAILED_AUDIT = 133;
	public static final int AUTHORIZED_ANI_INSERT_AUDIT = 134;
	public static final int AUTHORIZED_ANI_DELETE_AUDIT = 135;
	public static final int AUTHORIZED_ANI_MODIFY_AUDIT = 136;
	public static final int AUTHORIZED_ANI_MODIFY_STATUS_AUDIT = 137;
	public static final int ACCOUNT_SUMMARY_AUDIT = 138;
	public static final int POPD_MINUTE_BALANCE_ADJUST_AUDIT = 139;
	public static final int POPD_MINUTE_USAGE_ADJUST_AUDIT = 140;
	public static final int CALL_DETAIL_AUDIT = 141;
	public static final int RATE_BUCKET_CYCLE_DATE_AUDIT = 142;
	public static final int UPDATE_ACCOUNT_STATUS_ENABLE_AUDIT = 143;
	public static final int UPDATE_ACCOUNT_STATUS_DISABLE_AUDIT = 144;
	public static final int UPDATE_ACCOUNT_STATUS_SUSPEND_AUDIT = 145;
	public static final int ADD_SPEED_DIAL_AUDIT = 146;
	public static final int REMOVE_SPEED_DIAL_AUDIT = 147;
	public static final int POPD_SUBSCRIBER_REVERSE_CALL_AUDIT = 148;
	public static final int SUBSCRIBER_BALANCE_THRESHOLD_MODIFY_AUDIT = 149;
	public static final int SUBSCRIBER_IVR_LANGUAGE_MODIFY_AUDIT = 150;
	public static final int PIN_RANGE_STATUS_MODIFY_AUDIT = 151;
	public static final int SUBSCRIBER_BALANCE_THRESHOLD_QUERY_AUDIT = 152;
	public static final int SUBSCRIBER_ACCOUNT_CREDIT_LIST_AUDIT = 153;
	public static final int SPEED_DIAL_QUERY_AUDIT = 154;
	public static final int SUBSCRIBER_SUBSCRIPTION_FEE_AUDIT = 155;
	public static final int REVERSE_SUBSCRIPTION_FEE_EVENT_AUDIT = 156;
	public static final int ADD_ON_PURCHASE_AUDIT = 157;
	public static final int REVERSE_ADD_ON_PURCHASE_AUDIT = 158;
	public static final int RECURRING_ADD_ON_FEE_AUDIT = 159;
	public static final int REVERSE_RECURRING_ADD_ON_FEE_AUDIT = 160;
	public static final int REVERSE_CREDIT_USAGE_AUDIT = 161;
	public static final int REVERSE_TELECOM_SERVICE_SURCHARGE_AUDIT = 162;
	public static final int SUBRATE_INSERT_INTO_STOREDBALANCE_AUDIT = 163;
	public static final int ACCOUNT_BALANCES_AUDIT = 164;
	public static final int CREATE_SUBSCRIBER_ACCOUNT_AUDIT = 165;
	public static final int UPDATE_ACCOUNT_AUDIT = 166;
	public static final int DELETE_ACCOUNT_AUDIT = 167;
	public static final int AUTHORIZED_PHONE_NUMBER_INSERT_AUDIT = 168;
	public static final int AUTHORIZED_PHONE_NUMBER_DELETE_AUDIT = 169;
	public static final int GET_VOICEMAIL_COUNTS_AUDIT = 170;
	    
	//Used by Administrative Application Security
	public static final long PLATFORM_OWNER_SERVICE_PROVIDER_ID = 0;
	public static final long PLATFORM_OWNER_ADMIN_USER_ID = 0;
	public static final long PLATFORM_OWNER_ADMIN_ROLE_ID = 0;
	public static final long SERVICE_PROVIDER_ADMIN_USER_ID = 1;
	public static final long PROVISIONING_API_USER_ID = 3;
	public static final long BATCH_PROCESS_USER_ID = 4;
	
	 //Lot's Batch Status
	public static final int LOT_NEW = 1;  
	public static final int LOT_PROCESSED = 2; // the pins in the the lot have been generated
	public static final int LOT_ACTIVATED = 3;
	public static final int LOT_CANCELLED = 4; //do not use pins from this lot
	public static final int LOT_ERROR = 5;   //Error in processing the lot. Do not attempt further
	//the minimum number of records required to be generated before issuing a commit.
	// If the number of records generated is less than this number then the commit is
	//issued before the process ends.
	public static final int LOT_SUSPENDED = 6;
	public static final int LOT_PROCESSING = 7;
	public static final int LOT_UNSUSPENDED = 8;
	public static final int LOT_GROUPS_ACTIVATED = 9;      // not in code_reference
	public static final int LOT_NO_PINS_AVAILABLE = 10;    // not in code reference
	public static final int LOT_EXPIRED = 11;
	
	//prepaid activation current status codes
	public static final int PINS_ACTIVATED = 1;
	public static final int PINS_SUSPENDED = 2;
	public static final int PINS_CANCELLED = 3;
	public static final int PINS_PURGED = 4;
	public static final int PINS_UNSUSPENDED = 15;  // only used for gui
	
	//prepaid activation log action codes
	public static final int GROUP_ACTIVATED = 1;
	public static final int GROUP_SUSPENDED = 2;
	public static final int GROUP_CANCELLED = 3;
	public static final int GROUP_PURGED = 4;
	public static final int GROUP_SETTINGS_MODIFIED = 5; 
	public static final int GROUP_UNSUSPENDED = 15;  // only used for gui
	public static final int PREACTIVATED_SETTINGS_SAVE =9;//only used for gui    
    
}
