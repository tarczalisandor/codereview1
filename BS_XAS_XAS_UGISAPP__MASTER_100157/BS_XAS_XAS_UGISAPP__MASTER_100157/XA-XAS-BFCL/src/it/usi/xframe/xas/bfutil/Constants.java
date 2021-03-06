package it.usi.xframe.xas.bfutil;

import java.text.SimpleDateFormat;

/**
 * Global constants.
 */
public final class Constants {
	/** Utility class. */
	private Constants() {
		super();
	}
	public final static SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	
	public final static String DB_JNDI = "jdbc/XA-XAS-DS";
	public final static int PROVIDER_DEFAULT_VALIDITY_DAYS = 2;
	
	public final static String XAS_MO_COMMAND_PREFIX = "XASCMD";
	public final static String XAS_MO_SEPARATOR = ":";
	public final static String XAS_MO_COMMAND_ROUTE = "RT";
	/** EJB. */
	public final static String CHANNEL_EJ = "EJ";
	/** MQ Queue. */
	public final static String CHANNEL_MQ = "MQ";
	/** WebService. */
	public final static String CHANNEL_WS = "WS";
	/** Rest Service. */
	public final static String CHANNEL_RS = "RS";
	/** Ubiquity. */
	public final static String CHANNEL_UB = "UB";
	/** Ubiquity xasUser for sendInternationalSms forwarding */
	public final static String UNIKEY_XASUSER = "02008UNIKEY";
	/** Mail sender for setMailFrom for Xas Notification. */
	public final static String XAS_MAIL_SENDER = "XA-XAS";
	
	/** Name of this application. */
	public static final String MY_APPL_ID = "XAS";
	/** Current version of the log. */
	public static final String MY_LOG_VER = "0.1.0";
	public static final String MY_ANALYTIC_VER2 = "2.0";
	// Should match the version in pom.xml\project\version
	// and more_than_support.apt#change log
	/** Name of this application. */
	public static final String MY_UUID_KEY = "XAS_UUID";
	public static final String MY_UUID_TYPE = "XAS_UUID_TYPE";
	public static final String MY_UUID_INIT = "00000001-0001-0001-0001-000000000001"; // UUID used when an UUID is not available i.e. during initialization
	/** Name of this application. */
	public static final String MY_CRID_KEY = "XAS_CRID";

	public static final String K_CONFIG_FILE = "a_config_file";
	public static final String K_UUID_TYPE = "a_uuidType";
	
	public static final String SMS_AUTHORITY = "XAS002";

	public static final String SMS_ROLE = "SendSms";
	
	public static final String XAS00000I_CODE = "XAS00000I";
	public static final String XAS00000I_MESSAGE = "Message successfully processed";
	public static final String XAS00000I_MESSAGE2 = "Message successfully processed. UUID {0} bytes {1} on {2} messages using encoding {3}";

	public static final String XAS00001W_CODE = "XAS00001W";
	public static final String XAS00001W_MESSAGE = "Warning on text message {0}";

	public static final String XAS00002E_CODE = "XAS00002E";
	public static final String XAS00002E_MESSAGE = "Caught exception {0}";

	/* ONLY FOR MESSAGES PUT IN MQ */
	public static final String XAS00003I_CODE = "XAS00003I";
	public static final String XAS00003I_MESSAGE = "Message successfully enqueued";
	
	public static final String XAS00004E_CODE = "XAS00004E";
	public static final String XAS00004E_MESSAGE = "User {0} does not have the role for sms send";

	public static final String XAS00005E_CODE = "XAS00005E";
	public static final String XAS00005E_MESSAGE = "Specified delivery time {0} is not valid";

	public static final String XAS00006E_CODE = "XAS00006E";
	public static final String XAS00006E_MESSAGE = "Delivery time {0} is after validity expiration time {1}";

	public static final String XAS00007E_CODE = "XAS00007E";
	public static final String XAS00007E_MESSAGE = "Message validity {0} is expired";
	
	public static final String XAS00008E_CODE = "XAS00008E";
	public static final String XAS00008E_MESSAGE = "Specified validity time {0} out of limit {1}";
	
	public static final String XAS00009E_CODE = "XAS00009E";
	public static final String XAS00009E_MESSAGE = "Invalid phoneNumber {0} cause {1}";

	public static final String XAS00010E_CODE = "XAS00010E";
	public static final String XAS00010E_MESSAGE = "Default region unsupported {0} for xasUser {1}";

	public static final String XAS00011E_CODE = "XAS00011E";
	public static final String XAS00011E_MESSAGE = "Missing or empty message";

	public static final String XAS00012E_CODE = "XAS00012E";
	public static final String XAS00012E_MESSAGE = "xasUserName {0} and defaultXasUserName {1} not found in Configuration for this interface version";
	
	public static final String XAS00013E_CODE = "XAS00013E";
	public static final String XAS00013E_MESSAGE = "User {0} has not the role {1} needed to send SMS";
	
	public static final String XAS00014E_CODE = "XAS00014E";
	public static final String XAS00014E_MESSAGE = "Message length {0} exceeds the max length of {1} characters using encoding {2}";
	
	public static final String XAS00015E_CODE = "XAS00015E";
	public static final String XAS00015E_MESSAGE = "Phone number {0} matches more than one originator router {1}, {2}";
	
	public static final String XAS00016E_CODE = "XAS00016E";
	public static final String XAS00016E_MESSAGE = "Undefined originator {0}";

	public static final String XAS00017E_CODE = "XAS00017E";
	public static final String XAS00017E_MESSAGE = "Undefined provider {0} for xasUser {1}";

	public static final String XAS00018E_CODE = "XAS00018E";
	public static final String XAS00018E_MESSAGE = "Interface version {0} not available for xasUser {1}, please use interface version {2}";

	public static final String XAS00019E_CODE = "XAS00019E";
	public static final String XAS00019E_MESSAGE = "Delivery report error {0}";

	public static final String XAS00020E_CODE = "XAS00020E";
	public static final String XAS00020E_MESSAGE = "Invalid UUID format {0}";

	public static final String XAS00023E_CODE = "XAS00023E";
	public static final String XAS00023E_MESSAGE = "Error looking for configuration file";

	public static final String XAS00024E_CODE = "XAS00024E";
	public static final String XAS00024E_MESSAGE = "Error loading XML configuration file: {0}";

	public static final String XAS00025E_CODE = "XAS00025E";
	public static final String XAS00025E_MESSAGE = "Configuration invalid value {0} for key {1}";

	public static final String XAS00026E_CODE = "XAS00026E";
	public static final String XAS00026E_MESSAGE = "Configuration error: provider type not implemented {0} for provider name {1}";

	public static final String XAS00027E_CODE = "XAS00027E";
	public static final String XAS00027E_MESSAGE = "Configuration error: missing required attribute in path {0}";

	public static final String XAS00028E_CODE = "XAS00028E";
	public static final String XAS00028E_MESSAGE = "Configuration error: provider type {0} for provider name {1} does not implement service {2} requested by xasUser {3}, warning if providerWorkloadPct parameter is defined check defaultProvider too";

	public static final String XAS00029E_CODE = "XAS00029E";
	public static final String XAS00029E_MESSAGE = "Configuration error: path {0} xasUser {1} specify an moDestinator {2} already used by xasUser {3}";

	public static final String XAS00030E_CODE = "XAS00030E";
	public static final String XAS00030E_MESSAGE = "Configuration error: path {0} protocolId already used by xasUser {1} for originator {2}";

	public static final String XAS00031E_CODE = "XAS00031E";
	public static final String XAS00031E_MESSAGE = "Class {0} not available for the xasUser {1}, defined class in configuration are {2}";

	public static final String XAS00032E_CODE = "XAS00032E";
	public static final String XAS00032E_MESSAGE = "Workload provider defined without defining the provider {0}";

	public static final String XAS00033E_CODE = "XAS00033E";
	public static final String XAS00033E_MESSAGE = "Configuration error: attribute in path {0} requires attribute missing in path {1}" ;

	public static final String XAS00034E_CODE = "XAS00034E";
	public static final String XAS00034E_MESSAGE = "Configuration error: feature {0} not available without database in path {1}";

	public static final String XAS00035E_CODE = "XAS00035E";
	public static final String XAS00035E_MESSAGE = "Configuration invalid value {0} for key {1} allowed values {2}";

	public static final String XAS00036E_CODE = "XAS00036E";
	public static final String XAS00036E_MESSAGE = "Configuration error: provider type {0} not support Environment customization for provider name {1}";

	public static final String XAS00037E_CODE = "XAS00037E";
	public static final String XAS00037E_MESSAGE = "Undefined provider {0} in {1}";

	public static final String XAS00038E_CODE = "XAS00038E";
	public static final String XAS00038E_MESSAGE = "Undefined xasUser {0} in {1}";

	public static final String XAS00080I_CODE = "XAS00080I";
	public static final String XAS00080I_MESSAGE = "Application notificated successfully";

	public static final String XAS00081E_CODE = "XAS00081E";
	public static final String XAS00081E_MESSAGE = "Notification temporary error - retry delivery. Application reported: {0}";

	public static final String XAS00082E_CODE = "XAS00082E";
	public static final String XAS00082E_MESSAGE = "Notification fatal error - do not retry delivery. Application reported: {0}";

	public static final String XAS00084E_CODE = "XAS00084E";
	public static final String XAS00084E_MESSAGE = "Notification error. Unexpected application code: {0}";


	public static final String XAS00085E_CODE = "XAS00085E";
	public static final String XAS00085E_MESSAGE = "Application specific error: {0} with code {1}";

	public static final String XAS00086E_CODE = "XAS00086E";
	public static final String XAS00086E_MESSAGE = "XasUser undefined for moDestinator {0}, phone number not registered";

	public static final String XAS00095E_CODE = "XAS00095E";
	public static final String XAS00095E_MESSAGE = "JNDI Exception {0} for {1}, action {2}, cause {3}";

	public static final String XAS00096E_CODE = "XAS00096E";
	public static final String XAS00096E_MESSAGE = "Encryption error: during {0} library {1} not implemented";

	public static final String XAS00097E_CODE = "XAS00097E";
	public static final String XAS00097E_MESSAGE = "Encryption error: during {0} for {1} with code {1}";

	public static final String XAS00098E_CODE = "XAS00098E";
	public static final String XAS00098E_MESSAGE = "Provider specific error: {0} with code {1}";
	// Provider specific error: Incorrect bind status for given command --> wrong user or password for provider login
 
	public static final String XAS00099E_CODE = "XAS00099E";
	public static final String XAS00099E_MESSAGE = "Unexpected exception {0}, please check stacktrace in logger.error";


	
	// *** [identification] ***
	public static final String LOG_KEY_FUNCTION			= "a_function";
	public static final String LOG_KEY_CHANNEL			= "a_channel";
	public static final String LOG_KEY_VERSION			= "a_version";
	public static final String LOG_KEY_USERID			= "a_userId";
	public static final String LOG_KEY_USERINROLE		= "a_userInRole";

	// *** [request] ***
	/**
	 * {@link it.usi.xframe.xas.bfutil.data.SmsRequest#getXasUser()}
	 */
	public static final String LOG_KEY_SRCXASUSER		= "a_srcXasUser";
	/**
	 * {@link it.usi.xframe.xas.bfutil.data.SmsRequest#getPhoneNumber()}
	 */
	public static final String LOG_KEY_SRCPHONENUM		= "a_srcPhoneNumber";
	/**
	 * {@link it.usi.xframe.xas.bfutil.data.SmsRequest#getDeliveryTime()
	 */
	public static final String LOG_KEY_SRCDELIVERY		= "a_srcDelivery";
	/**
	 * {@link it.usi.xframe.xas.bfutil.data.SmsRequest#getValidity()
	 */
	public static final String LOG_KEY_SRCVALIDITY		= "a_srcValidity";
	/**
	 * {@link it.usi.xframe.xas.bfutil.data.SmsRequest#getValidityPeriod()
	 */
	public static final String LOG_KEY_SRCVALPERIOD		= "a_srcValidityPeriod";
	public static final String LOG_KEY_SRCMSGLENGTH   	= "a_srcMsgLength";
	public static final String LOG_KEY_DSTXASUSER		= "a_dstXasUser";
	public static final String LOG_KEY_DSTPHONENUM		= "a_dstPhoneNumber";
	public static final String LOG_KEY_DSTDELIVERY		= "a_dstDelivery";
	public static final String LOG_KEY_DSTVALIDITY		= "a_dstValidity";

	
	public static final String LOG_KEY_DSTBYTELENGTH	= "a_dstByteLength";
	public static final String LOG_KEY_SMSENC			= "a_smsEnc";

	// *** [xasUser] ***
	public static final String LOG_KEY_DEF_VAL_PER		= "a_defaultValidityPeriod"; // ???
	public static final String LOG_KEY_DEF_REGION		= "a_defaultRegion"; // ???
	public static final String LOG_KEY_MO_DESTINATION	= "a_moDestination";	//#TIM
	public static final String LOG_KEY_END_POINT		= "a_endPoint";			//#TIM
	public static final String LOG_KEY_EP_STATUS_CODE	= "a_endPointSC";		//#TIM
	public static final String LOG_KEY_EP_STATUS_MSG 	= "a_endPointSM";		//#TIM

	// *** [provider] ***
	public static final String LOG_KEY_RESPONSE_PROVIDER	= "a_provider";

	// *** [originator] ***
	public static final String LOG_KEY_ORIGNAME		= "a_originatorName";
	public static final String LOG_KEY_ROUTERREGEX		= "a_routerRegEx";
	public static final String LOG_KEY_ORIGINATOR		= "a_originator";
	public static final String LOG_KEY_TYPEOFNUMBER	= "a_typeOfNumber";
	public static final String LOG_KEY_NUMPLANID		= "a_numPlanId";

	// *** [response] ***
	// 				   SmartLog.K_STATUS_CODE
	// 				   SmartLog.K_STATUS_MSG
	public static final String LOG_KEY_SMSSTATUS		= "a_smsStatus";	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
	public static final String LOG_KEY_SMSID			= "a_smsId";
	public static final String LOG_KEY_TOTALSMS		= "a_totalSms";
	public static final String LOG_KEY_GATEWAYCONTACTED = "a_gatewaycontacted";	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
	//			  SmartAnalytic.K_TIME_ELAPSED

	public static final String LOG_KEY_GATEWAYSTATUSCODE = "a_gatewayStatusCode";
	
	public static final String LOG_VALUE_SMSSTATUS_OK		= "OK";
	public static final String LOG_VALUE_SMSSTATUS_ERROR	= "ERR";
	
	
}

