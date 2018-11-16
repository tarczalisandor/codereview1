/**
 * 
 */
package it.usi.xframe.xas.util;

import java.io.Serializable;

/**
 * @author us03064
 *
 */
public class SplunkDataConstant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// --------------------------------CONNECTION PARAMETERS--------------------------------
	/**
	 * Splunk Server Name
	 */
	public static final String HOSTNAME = "https://ussplpl001.usinet.it"; 
	
	public static final String REQUESTURI = "/services/search/jobs";
	
	
	/**
	 * Splunk User Name
	 */
	public static final String USERNAME = "admin";

	/**
	 * Splunk User Password
	 */
	public static final String PASSWORD = "admin60";

	public static final String SEPARATOR = ":";
	
	/**
	 * Rest Connection Port
	 */
	public static final int PORT = 8089; 

	/**
	 * Rest Connection Schema
	 */
	public static final String SCHEMA = "https";
	
	
	public static final String TLS12 = "TLSv1.2";

	// --------------------------------SEARCH PARAMETERS--------------------------------
	
	/**
	 * Serach to run
	 */

// Production	
/*	
	public static final String SPLUNK_QUERY_SEARCH_XAS_REST_SMS1 = "search index=xfr sourcetype=log4j a_appl_id=XAS a_scope=analytics"
		+ " a_function=sendSms a_log_ver=\"2.0\" a_dstPhoneNumber=\"[PARAM_PHONENUMBER]\" earliest=\"[PARAM_EARLIEST]\" latest=\"[PARAM_LATEST]\"  |"
		+ " addinfo |"
		+ " eval counter=1 |"
		+ " accum counter as LineNumber |"
		+ " eval numSms = \"SMS\"+ LineNumber |"
		+ " eval a_smsid=if(isnull(a_smsid),a_smsId,a_smsid) |"
		+ " eval a_smsid=coalesce(a_smsid, \"\") |"
		+ " eval a_smsid=split(a_smsid, \",\") |" 
		+ " eval a_smsid=if(isnull(a_smsid),\"NoSMSId\",a_smsid) |" 
		+ " mvexpand a_smsid |" 
		+ " replace \"[\\\"*\" with \"*\" in a_smsid |"
		+ " replace \"\\\"*\" with \"*\" in a_smsid |"
		+ " replace \"*\\\"\" with \"*\" in a_smsid |"
		+ " replace \"*\\\"]\" with \"*\" in a_smsid |"
		+ " eval a_srcABI=if(isnull(a_srcABI),a_srcXasUser,a_srcABI) |"
		+ " eval a_smsstatus=if(isnull(a_smsstatus),a_smsStatus,a_smsstatus) |"
		+ " eval a_dstphonenumber=if(isnull(a_dstphonenumber),a_dstPhoneNumber,a_dstphonenumber) |"
		+ " eval \"XAS time\"= strftime(_time, \"%d/%m/%Y - %H:%M:%S\") |"
		+ " eval time1 = _time |"
		+ " rename numSms as \"SMS\" a_smsstatus as \"XAS Status\" a_dstphonenumber as \"Phone Number\" a_srcABI as \"XAS User\" a_smsid as SmsId"
		+ " a_status_code as \"XAS ret.code\" a_provider as Provider a_uuid as Uid|"
		+ " table Provider \"SMS\" SmsId \"XAS time\" \"XAS User\" \"Phone Number\" \"XAS ret.code\" \"XAS Status\" Uid |"
		+ " sort +_time";
	
	public static final String SPLUNK_QUERY_SEARCH_XAS_REST_SMS2 = "search index=xfr sourcetype=log4j a_appl_id=XAS a_scope=analytics"
		+ " a_function=sendSms a_log_ver=2.0 a_provider=Telecom a_uuid=\"[PARAM_DETAILS]\" earliest=\"[PARAM_EARLIEST]\" latest=\"[PARAM_LATEST]\"  |"
		+ " addinfo |"
		+ " eval XAS_dateRif = strftime(_time, \"%d/%m/%Y - %H:%M:%S\") |"
		+ " eval time1 = _time |"
		+ " eval XAS_Err =if(a_smsStatus=\"ERR\",\"Open XAS Incident\",\"\") |"
		+ " rename a_smsStatus as \"XAS Status\" |"
		+ " eval SmsId=rtrim(ltrim(a_smsId,\"[\"\"),\"\"]\") |" 
		+ " table time1 _time XAS_dateRif dataFine a_srcXasUser a_dstPhoneNumber a_uuid a_status_code \"XAS Status\" a_provider XAS_Err SmsId |"
		+ " where a_uuid=\"[PARAM_DETAILS]\" |"
		+ " sort +_time  |"
		+ " join type=outer a_uuid  ["
		+ " search index=xfr a_appl_id=XAS a_providerType=Telecom a_function=deliveryRequest a_log_ver=2.0 a_scope=analytics a_uuid=\"[PARAM_DETAILS]\""
		+ " earliest=\"[PARAM_EARLIEST]\" latest=\"[PARAM_LATEST]\" |"
		+ " fillnull value=\"\" |"
		+ " rename a_errormessage as gtw_message |"
		+ " rename a_smsgatewayresponse as gtw_response |"
		+ " rename a_dstPhoneNumber as gtw_phonenumber |"
		+ " dedup a_uuid gtw_message _time gtw_response |"
		+ " table a_uuid gtw_message _time gtw_response |"
		+ " eval GTW_dateRif = strftime(_time, \"%d/%m/%Y - %H:%M:%S\") |"
		+ " table GTW_dateRif a_uuid gtw_response gtw_message gtw_phonenumber ] |"
		+ " fillnull value=\"\" |"
		+ " eval a_smsStatus = if(gtw_response = \"2\",\"OK\",\"KO\") |"
		+ " eval tmpAct=if(a_smsStatus=\"KO\",\"Open TIM Incident\",\"Email to TIM for info\") |"
		+ " eval \" \"= if(XAS_Err != \"\" , XAS_Err,tmpAct) |"
		+ " eval a_errormessage = if(gtw_message != \"\",gtw_message , \"Error Delivery Report not received in 48h from Gateway\") |"
		+ " rename XAS_dateRif as \"XAS time\" , a_srcXasUser as \"XAS user\" , a_dstPhoneNumber as \"Phone number\" , a_status_code as \"XAS ret.code\" ,"
		+ " a_smsStatus as \"DR status\" , a_errormessage as \"DR message\" , GTW_dateRif as \"DR time\" a_uuid as Uid , a_provider as Provider |"
		+ " table \" \" Provider SmsId \"XAS time\" \"XAS user\" \"Phone number\" Uid \"XAS Status\" \"XAS ret.code\" \"DR time\" "
		+ " \"DR status\" \"DR message\" ";
*/
// Test		
	public static final String SPLUNK_QUERY_SEARCH_XAS_REST_SMS1 = "search index=was earliest=\"[PARAM_EARLIEST]\" latest=\"[PARAM_LATEST]\" | stats count as total by sourcetype";
	
	public static final String SPLUNK_QUERY_SEARCH_XAS_REST_SMS2 = "search index=was earliest=\"[PARAM_EARLIEST]\" latest=\"[PARAM_LATEST]\" sourcetype=\"[PARAM_DETAILS]\" | stats count as total by host index source ";

// Test
	public static final String SPLUNK_DETAILS_KEY = "sourcetype";

// Production
//	public static final String SPLUNK_DETAILS_KEY = "Uid";
	
	// sid name of the search
	public static final String SPLUNK_SID_SMS1 = "XAS_REST_Sms1";
	
	public static final String SPLUNK_SID_SMS2 = "XAS_REST_Sms2";

	public static final String SPLUNK_EARLIEST = "-1d";
	
	public static final String SPLUNK_LATEST = "now";
	
	public static final String SPLUNK_SLASH = "/";
	
	public static final String SPLUNK_RESULT_PATH = "results";
	
	public static final int SPLUNK_NUM_LOOP = 20; 
	
	public static final int SPLUNK_AUTO_CANCEL_TIME = 10;
	
	public static final int SPLUNK_MAX_TIME = 30;
	
	public static final String SPLUNK_EARLIEST_KEY = "earliest";
	
	public static final String SPLUNK_LATEST_KEY = "latest";
	
	public static final String SPLUNK_SID_KEY = "sid";
	
	public static final String SPLUNK_PHONENUMBER_KEY = "a_dstPhoneNumber";

	public static final String SPLUNK_DATATYPE_KEY = "datatype";

	
}
