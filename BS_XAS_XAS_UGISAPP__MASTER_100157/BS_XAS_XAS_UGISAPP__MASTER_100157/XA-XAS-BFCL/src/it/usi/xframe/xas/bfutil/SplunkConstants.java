package it.usi.xframe.xas.bfutil;

/**
 * Global constants to be used for ANALYTIC purpose.
 * 
 * DO NOT CHANGE THE CONSTANT'S VALUE BECAUSE THE VALUES ARE USED BY SPLUNK ANALYTIC DASHBOARD. 
 */
public final class SplunkConstants {
	/** Utility class. */
	private SplunkConstants() {
		super();
	}
	// *** [identification] ***
	public static final String K_FUNCTION			= "a_function";
	public static final String V_FUNCTION_SENDSMS          = "sendSms";
	public static final String V_FUNCTION_SENDMESSAGE      = "sendMessage";
	public static final String V_FUNCTION_EXPORTMESSAGE    = "exportMessage";
	public static final String V_FUNCTION_MOBILEORIGINATED = "mobileOriginated";
	public static final String V_FUNCTION_DELIVERYREQUEST  = "deliveryRequest";
	public static final String K_CHANNEL			= "a_channel";
	public static final String K_VERSION			= "a_version";
	public static final String K_USERID			    = "a_userId";
	public static final String K_USERINROLE		    = "a_userInRole";
	public static final String K_CORRELATIONID	    = "a_correlationId";

	// *** [request] ***
	public static final String K_SRCXASUSER		    = "a_srcXasUser";
	public static final String K_SRCPHONENUM		= "a_srcPhoneNumber";
	public static final String K_SRCDELIVERY		= "a_srcDelivery";
	public static final String K_SRCVALIDITY		= "a_srcValidity";
	public static final String K_SRCVALPERIOD	    = "a_srcValidityPeriod";
	public static final String K_SRCMSGLENGTH       = "a_srcMsgLength";
	public static final String K_DSTXASUSER		    = "a_dstXasUser";
	public static final String K_DSTPHONENUM		= "a_dstPhoneNumber";
	public static final String K_DSTDELIVERY		= "a_dstDelivery";
	public static final String K_DSTVALIDITY		= "a_dstValidity";

	public static final String K_NOTGSMAT			= "a_notGsmAt";
	public static final String K_NOTGSMUNICODE		= "a_notGsmUnicode";
	public static final String K_NOTGSMCHAR			= "a_notGsmChar";
	
	public static final String K_DSTBYTELENGTH	    = "a_dstByteLength";
	public static final String K_SMSENC			    = "a_smsEnc";

	// *** [xasUser] *** 
	public static final String K_DEF_VAL_PER		= "a_defaultValidityPeriod"; // ???
	public static final String K_DEF_REGION		    = "a_defaultRegion"; // ???
	public static final String K_MO_DESTINATOR	    = "a_moDestinator";		//#TIM
	public static final String K_END_POINT		    = "a_endPoint";			//#TIM

	// *** [provider] ***
	public static final String K_PROVIDER	        = "a_provider";
	public static final String K_PROVIDER_TYPE      = "a_providerType";

	// *** [originator] ***
	public static final String K_ORIGNAME		    = "a_originatorName";
	public static final String K_ROUTERREGEX		= "a_routerRegEx";
	public static final String K_ORIGINATOR		    = "a_originator";
	public static final String K_TYPEOFNUMBER	    = "a_typeOfNumber";
	public static final String K_NUMPLANID		    = "a_numPlanId";

	// *** [deliveryResponse/mobileOriginated] ***
	public static final String K_DELIVERYDATE		= "a_deliveryDate";
	public static final String K_PROVIDERDATE		= "a_providerDate";
	public static final String K_MODATE				= "a_moDate";

	// *** [response] ***
	// 				   SmartLog.K_STATUS_CODE
	// 				   SmartLog.K_STATUS_MSG
	public static final String K_SMSSTATUS		    = "a_smsStatus";	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
	public static final String V_SMSSTATUS_OK		= "OK";
	public static final String V_SMSSTATUS_ERROR	= "ERR";
	public static final String K_SMSID			    = "a_smsId";
	public static final String K_TOTALSMS		    = "a_totalSms";
	public static final String K_GATEWAYCONTACTED   = "a_gatewaycontacted";	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
	//			  SmartAnalytic.K_TIME_ELAPSED
	public static final String K_TIME_SECTION_WAIT  = "wait";
	public static final String K_TIME_SECTION_START = "start";
	public static final String K_TIME_SECTION_END   = "end";
	public static final String K_TIME_SEC_XAS		= "xas";
	public static final String K_TIME_SEC_SEND		= "send";
	public static final String K_TIME_SEC_TABLE		= "table";
	public static final String K_TIME_SEC_APPL		= "appl";
	

	public static final String K_GATEWAYSTATUSCODE  = "a_gatewayStatusCode";
	public static final String K_ACCEPTED			= "a_accepted";
	
	public static final String TMP_ISUSERINROLE     = "a_isUserInRole";
	public static final String TMP_USERID           = "a_userid";
	public static final String TMP_USERINROLE       = "a_userinrole";
	public static final String TMP_SRCPHONENUM      = "a_srcphonenumber";
	public static final String TMP_DSTPHONENUM      = "a_dstphonenumber";
	public static final String TMP_SMSENC           = "a_smsenc";
	public static final String TMP_SMSSTATUS        = "a_smsstatus";
	public static final String TMP_SMSID            = "a_smsid";
	
	//constants instead of raw strings
	public static final String TMP_STATUSAT         = "a_status_at";
	public static final String TMP_ISHTML           = "a_isHtml";
	public static final String TMP_ATTACHMENTDESC   = "a_attachment_desc";
	public static final String TMP_ATTACHMENTMIME   = "a_attachment_mime";
	public static final String TMP_SMSGATEWAYRESP   = "a_smsgatewayresponse";
	public static final String TMP_ERROR_MESSAGE    = "a_errormessage";
	
}
