package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.SmartLogFactory;
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.SplunkConstants;

import org.apache.commons.logging.Log;

import eu.unicredit.xframe.slf.SLF_Exception;
import eu.unicredit.xframe.slf.SmartAnalytic;
import eu.unicredit.xframe.slf.SmartLog;

public class SlfLogger {

	private SmartAnalytic smartLog;
	private SmartLog backupSmartLog;
	
	private static final boolean BACKWARD_COMPATIBILITY = false;	//as of 16/9/2015

	/*  
	 * These constants are copied from SecurityLog to be used for backward compatibility logging
	 */
	
	private static final String SECURITY_LOG_ERROR            = Constants.XAS00002E_CODE;
	private static final String SECURITY_LOG_OK               = Constants.XAS00000I_CODE;
	private static final String MONITOR_LOG_OK                = "OK";
	private static final String MONITOR_LOG_ERROR             = "ERR";

	public static final String LOG_LEVEL_NONE        = "none";
	public static final String LOG_LEVEL_DEFAULT     = "default";
	public static final String LOG_LEVEL_DEBUG       = "full";
	private static final int LOG_NONE = 0;
	private static final int LOG_DEFAULT = 1;
	private static final int LOG_DEBUG = 2;

	private static final String SECURITY_LOG_KEY_LOGPREFIX    = "smslogprefix";
	private static final String SECURITY_LOG_KEY_SMSSTATUS    = "smsstatus";
	private static final String SECURITY_LOG_KEY_ERRORMESSAGE = "errormessage";
	private static final String SECURITY_LOG_KEY_LOGLEVEL     = "dbglevel";
	private static final String SECURITY_LOG_KEY_USERID       = "userid";
	private static final String SECURITY_LOG_KEY_SRCALIAS     = "srcAlias";
	private static final String SECURITY_LOG_KEY_USEDALIAS    = "usedAlias";
	private static final String SECURITY_LOG_KEY_SRCABI       = "srcABI";
	private static final String SECURITY_LOG_KEY_USEDABI      = "usedABI";
	private static final String SECURITY_LOG_KEY_ORIGINATOR   = "originator";
	private static final String SECURITY_LOG_KEY_DSTPHONENUM  = "dstphonenumber";
	private static final String SECURITY_LOG_KEY_SMSMESSAGE   = "smsmessage";
	private static final String SECURITY_LOG_KEY_SMSID        = "smsid";
	private static final String SECURITY_LOG_KEY_SMSPART      = "smspart";
	private static final String SECURITY_LOG_KEY_SMSLENGTH    = "smslength";
	private static final String SECURITY_LOG_KEY_SMSENC       = "smsenc";
	private static final String SECURITY_LOG_KEY_MSGLENGTH    = "msglength";
	private static final String SECURITY_LOG_KEY_GATEWAYCONTACTED = "gatewaycontacted";
	private static final String SECURITY_LOG_KEY_TIMEELAPSED  = "timeelapsed";
	
	private static final String SECURITY_LOG_RESPONSE_PROVIDER = "Provider";
	private static final String SECURITY_LOG_RESPONSE_MESSAGEID = "message_id";
	private static final String SECURITY_LOG_RESPONSE_ERRORCODE = "error_code";
	private static final String SECURITY_LOG_RESPONSE_COMMANDSTATUS = "command_status";
	private static final String SECURITY_LOG_RESPONSE_SEQNUMBER = "sequence_number";

	private static final String LOG_KEY_CHANNEL		 = SplunkConstants.K_CHANNEL; //"a_channel";
	private static final String LOG_KEY_FUNCTION	 = SplunkConstants.K_FUNCTION; //"a_function";
	private static final String LOG_KEY_VERSION		 = SplunkConstants.K_VERSION; //"a_version";
	private static final String LOG_KEY_LOGPREFIX    = "a_smslogprefix";
	private static final String LOG_KEY_SMSSTATUS    = SplunkConstants.TMP_SMSSTATUS; /*SplunkConstants*/	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
	private static final String LOG_KEY_ERRORMESSAGE = "a_errormessage";
	private static final String LOG_KEY_LOGLEVEL     = "a_dbglevel";
	private static final String LOG_KEY_USERID       = SplunkConstants.TMP_USERID; /*SplunkConstants*/ //"a_userid";
	private static final String LOG_KEY_USERINROLE   = SplunkConstants.TMP_USERINROLE; /*SplunkConstants*///"a_userinrole";
	private static final String LOG_KEY_SRCALIAS     = "a_srcAlias";
	private static final String LOG_KEY_USEDALIAS    = "a_usedAlias";
	private static final String LOG_KEY_SRCABI       = "a_srcABI";
	private static final String LOG_KEY_USEDABI      = "a_usedABI";
	private static final String LOG_KEY_TYPEOFNUMBER = SplunkConstants.K_TYPEOFNUMBER; //"a_typeOfNumber";
	private static final String LOG_KEY_NUMPLANID	 = SplunkConstants.K_NUMPLANID; //"a_numPlanId";
	private static final String LOG_KEY_ORIGINATOR   = SplunkConstants.K_ORIGINATOR; //"a_originator";
	private static final String LOG_KEY_SRCPHONENUM  = SplunkConstants.TMP_SRCPHONENUM; /*SplunkConstants*/ //"a_srcphonenumber";
	private static final String LOG_KEY_DSTPHONENUM  = SplunkConstants.TMP_DSTPHONENUM; /*SplunkConstants*/ //"a_dstphonenumber";
	private static final String LOG_KEY_SMSMESSAGE   = "a_smsmessage";
	private static final String LOG_KEY_VALIDITY     = "a_validity";
	private static final String LOG_KEY_DELIVERY     = "a_delivery";
	private static final String LOG_KEY_SRC_VALIDITY = SplunkConstants.K_SRCVALIDITY; //"a_srcValidity";
	private static final String LOG_KEY_SRC_VAL_PER  = SplunkConstants.K_SRCVALPERIOD; //"a_srcValidityPeriod";
	private static final String LOG_KEY_DEF_VAL_PER  = SplunkConstants.K_DEF_VAL_PER; //"a_defaultValidityPeriod";
	private static final String LOG_KEY_SRC_DELIVERY = SplunkConstants.K_SRCDELIVERY; //"a_srcDelivery";
	private static final String LOG_KEY_SMSID        = SplunkConstants.TMP_SMSID; /*SplunkConstants*/ //"a_smsid";
	private static final String LOG_KEY_SMSPART      = "a_smspart";
	private static final String LOG_KEY_SMSLENGTH    = "a_smslength";
	private static final String LOG_KEY_SMSENC       = SplunkConstants.TMP_SMSENC; /*SplunkConbstants*/ //"a_smsenc";
	private static final String LOG_KEY_MSGLENGTH    = "a_msglength";
	private static final String LOG_KEY_GATEWAYCONTACTED = SplunkConstants.K_GATEWAYCONTACTED; //"a_gatewaycontacted";	//WARNING: USED BY IOC MONITORING - DO NOT CHANGE OR REMOVE
//	private static final String LOG_KEY_TIMEELAPSED  = "a_timeelapsed";
//	not used -> use the SmartAnalytic.K_TIME_ELAPSED keyword (a_time_elapsed)
	
	private static final String LOG_RESPONSE_PROVIDER = SplunkConstants.K_PROVIDER; //"a_provider";
	private static final String LOG_RESPONSE_MESSAGEID = "a_message_id";
	private static final String LOG_RESPONSE_ERRORCODE = "a_error_code";
	private static final String LOG_RESPONSE_COMMANDSTATUS = "a_command_status";
	private static final String LOG_RESPONSE_SEQNUMBER = "a_sequence_number";
	
 	public SlfLogger() {
 		if (BACKWARD_COMPATIBILITY) {
			smartLog = (SmartAnalytic)SmartLogFactory.getInstance().createLooseAnalyticWithoutId(this);
		} else {
			smartLog = (SmartAnalytic)SmartLogFactory.getInstance().createAnalyticWithoutId(this);
		}
	}
	public void logData(Log log, LogData logData) throws SLF_Exception {
		smartLog.collectIt(SmartLog.K_STATUS_CODE, SECURITY_LOG_OK,
				LOG_KEY_SMSSTATUS, MONITOR_LOG_OK);
		if (BACKWARD_COMPATIBILITY) smartLog.collectIt(SECURITY_LOG_KEY_SMSSTATUS, MONITOR_LOG_OK);
		logCommonData(logData);
		log.info(smartLog.getAnalyticRow(true));
	}
	
	public void logError(Log log, LogData logData) {
		try {
			smartLog.logReferrer(2)
					.collectIt(SmartLog.K_STATUS_CODE, SECURITY_LOG_ERROR, SmartLog.K_STATUS_MSG, logData.errorMessage,
							SplunkConstants.TMP_STATUSAT, logData.errorAt, /*SplunkConstants*/
					LOG_KEY_SMSSTATUS, MONITOR_LOG_ERROR /*,
					LOG_KEY_ERRORMESSAGE, logData.errorMessage*/);
			if (BACKWARD_COMPATIBILITY) smartLog.collectIt(SECURITY_LOG_KEY_SMSSTATUS, SECURITY_LOG_ERROR,
					SECURITY_LOG_KEY_ERRORMESSAGE, logData.errorMessage);
			logCommonData(logData);
			log.error(smartLog.getAnalyticRow(true));
		} catch(SLF_Exception e) {
			e.printStackTrace(System.out);
			log.error("SmartLogException while handling Exception: " + e.getMessage());
			backupSmartLog = SmartLogFactory.getInstance().createLoggerWithoutId(this);
			backupSmartLog.logIt(SmartLog.K_SCOPE, SmartLog.V_SCOPE_DEBUG);
			backupSmartLog.logIt(SmartLog.K_STATUS_CODE, SECURITY_LOG_ERROR, SmartLog.K_STATUS_MSG, logData.errorMessage,
					SplunkConstants.TMP_STATUSAT, logData.errorAt, /*SplunkConstants*/
					LOG_KEY_SMSSTATUS, MONITOR_LOG_ERROR /*,
					LOG_KEY_ERRORMESSAGE, logData.errorMessage*/);
			logCommonDataOnBackup(logData);
			log.error(backupSmartLog.getLogRow(true));
		}
	}
	
	private void logCommonData(LogData logData) {
		int logLevel = handleLogLevel(logData);
		if (logLevel >= LOG_DEFAULT) {
			smartLog.collectIt(
					LOG_KEY_CHANNEL, logData.channel,					
					LOG_KEY_FUNCTION, SplunkConstants.V_FUNCTION_SENDSMS,
					LOG_KEY_VERSION, logData.version,
					LOG_KEY_LOGPREFIX, logData.logPrefix,
					LOG_KEY_LOGLEVEL, logData.logLevel,
					LOG_KEY_USERID, logData.userid,
					LOG_KEY_USERINROLE, logData.isUserInRole == null ? null : logData.isUserInRole.toString(),
					LOG_KEY_SRCALIAS, logData.originalAlias,
					LOG_KEY_USEDALIAS, logData.usedAlias,
					LOG_KEY_SRCABI, logData.originalAbiCode,
					LOG_KEY_USEDABI, logData.usedAbiCode,
					LOG_KEY_TYPEOFNUMBER, Integer.toString(logData.typeOfNumber),
					LOG_KEY_NUMPLANID, Integer.toString(logData.numPlanId),
					LOG_KEY_ORIGINATOR, logData.originator,
					LOG_KEY_DSTPHONENUM, logData.normalizedDestinationNumber,
					LOG_KEY_SRCPHONENUM, logData.originalDestinationNumber,
					LOG_KEY_SMSID, logData.smsId,
					LOG_KEY_SMSLENGTH, String.valueOf(logData.messageLength),
					LOG_KEY_SMSENC, logData.smsEncoding,
					LOG_KEY_SMSPART, logData.messagePart
					);
			smartLog.collectIt(
					LOG_KEY_MSGLENGTH, logData.messagePartLength,
					LOG_KEY_VALIDITY, logData.validity,
					LOG_KEY_DELIVERY, logData.delivery,
					LOG_KEY_SRC_VALIDITY, logData.srcValidity,
					LOG_KEY_SRC_VAL_PER, logData.srcValidityPeriod,
					LOG_KEY_DEF_VAL_PER, logData.defaultValidityPeriod,
					LOG_KEY_SRC_DELIVERY, logData.srcDelivery,
					LOG_KEY_GATEWAYCONTACTED, String.valueOf(logData.gatewayContacted),
					SmartAnalytic.K_TIME_ELAPSED , Chronometer.formatMillis(logData.chrono.getElapsed())
//					LOG_KEY_TIMEELAPSED, Chronometer.formatMillis(logData.chrono.getElapsed())
					);
		}
		if (logLevel >= LOG_DEBUG) {
			smartLog.collectIt(
					LOG_KEY_SMSMESSAGE, logData.originalMsg
					);
		}
		if (logData.showResponseData) {
			smartLog.collectIt(
					LOG_RESPONSE_PROVIDER, logData.provider,
					LOG_RESPONSE_MESSAGEID, logData.responseMessageId,
					LOG_RESPONSE_ERRORCODE, logData.responseErrorCode,
					LOG_RESPONSE_COMMANDSTATUS, logData.responseCommandStatus,
					LOG_RESPONSE_SEQNUMBER, logData.responseSequenceNumber
					);
		}
		if (BACKWARD_COMPATIBILITY) backwardCompatibilityLog(logData);
	}
	
	private void backwardCompatibilityLog(LogData logData) {
		int logLevel = handleLogLevel(logData);
		if (logLevel >= LOG_DEFAULT) {
			smartLog.collectIt(
					SECURITY_LOG_KEY_LOGPREFIX, logData.logPrefix,
					SECURITY_LOG_KEY_LOGLEVEL, logData.logLevel,
					SECURITY_LOG_KEY_USERID, logData.userid,
					SECURITY_LOG_KEY_SRCALIAS, logData.originalAlias,
					SECURITY_LOG_KEY_USEDALIAS, logData.usedAlias,
					SECURITY_LOG_KEY_SRCABI, logData.originalAbiCode,
					SECURITY_LOG_KEY_USEDABI, logData.usedAbiCode,
					SECURITY_LOG_KEY_ORIGINATOR, logData.originator,
					SECURITY_LOG_KEY_DSTPHONENUM, logData.normalizedDestinationNumber,
					SECURITY_LOG_KEY_SMSID, logData.smsId,
					SECURITY_LOG_KEY_SMSLENGTH, String.valueOf(logData.messageLength),
					SECURITY_LOG_KEY_SMSENC, logData.smsEncoding,
					SECURITY_LOG_KEY_SMSPART, logData.messagePart,
					SECURITY_LOG_KEY_MSGLENGTH, logData.messagePartLength,
					SECURITY_LOG_KEY_GATEWAYCONTACTED, String.valueOf(logData.gatewayContacted),
					SECURITY_LOG_KEY_TIMEELAPSED, Chronometer.formatMillis(logData.chrono.getElapsed())
					);
		}
		if (logLevel >= LOG_DEBUG) {
			smartLog.collectIt(
					SECURITY_LOG_KEY_SMSMESSAGE, logData.originalMsg
					);
		}
		if (logData.showResponseData) {
			smartLog.collectIt(
					SECURITY_LOG_RESPONSE_PROVIDER, logData.provider,
					SECURITY_LOG_RESPONSE_MESSAGEID, logData.responseMessageId,
					SECURITY_LOG_RESPONSE_ERRORCODE, logData.responseErrorCode,
					SECURITY_LOG_RESPONSE_COMMANDSTATUS, logData.responseCommandStatus,
					SECURITY_LOG_RESPONSE_SEQNUMBER, logData.responseSequenceNumber
					);
		}
	}
	
	private void logCommonDataOnBackup(LogData logData) {
		int logLevel = handleLogLevel(logData);
		if (logLevel >= LOG_DEFAULT) {
			backupSmartLog.logIt(
					LOG_KEY_CHANNEL, logData.channel,					
					LOG_KEY_FUNCTION, SplunkConstants.V_FUNCTION_SENDSMS,
					LOG_KEY_VERSION, logData.version,
					LOG_KEY_LOGPREFIX, logData.logPrefix,
					LOG_KEY_LOGLEVEL, logData.logLevel,
					LOG_KEY_USERID, logData.userid,
					LOG_KEY_USERINROLE, logData.isUserInRole == null ? null : logData.isUserInRole.toString(),
					LOG_KEY_SRCALIAS, logData.originalAlias,
					LOG_KEY_USEDALIAS, logData.usedAlias,
					LOG_KEY_SRCABI, logData.originalAbiCode,
					LOG_KEY_USEDABI, logData.usedAbiCode,
					LOG_KEY_TYPEOFNUMBER, Integer.toString(logData.typeOfNumber),
					LOG_KEY_NUMPLANID, Integer.toString(logData.numPlanId),
					LOG_KEY_ORIGINATOR, logData.originator,
					LOG_KEY_DSTPHONENUM, logData.normalizedDestinationNumber,
					LOG_KEY_SRCPHONENUM, logData.originalDestinationNumber,
					LOG_KEY_SMSID, logData.smsId,
					LOG_KEY_SMSLENGTH, String.valueOf(logData.messageLength),
					LOG_KEY_SMSENC, logData.smsEncoding,
					LOG_KEY_SMSPART, logData.messagePart
 					);
			backupSmartLog.logIt(
					LOG_KEY_MSGLENGTH, logData.messagePartLength,
					LOG_KEY_VALIDITY, logData.validity,
					LOG_KEY_DELIVERY, logData.delivery,
					LOG_KEY_SRC_VALIDITY, logData.srcValidity,
					LOG_KEY_SRC_VAL_PER, logData.srcValidityPeriod,
					LOG_KEY_DEF_VAL_PER, logData.defaultValidityPeriod,
					LOG_KEY_SRC_DELIVERY, logData.srcDelivery,
					LOG_KEY_GATEWAYCONTACTED, String.valueOf(logData.gatewayContacted),
					SmartLog.K_TIME_ELAPSED, Chronometer.formatMillis(logData.chrono.getElapsed())
//					LOG_KEY_TIMEELAPSED, Chronometer.formatMillis(logData.chrono.getElapsed())
					);
		}
		if (logLevel >= LOG_DEBUG) {
			backupSmartLog.logIt(
					LOG_KEY_SMSMESSAGE, logData.originalMsg
					);
		}
		if (logData.showResponseData) {
			backupSmartLog.logIt(
					LOG_RESPONSE_PROVIDER, logData.provider,
					LOG_RESPONSE_MESSAGEID, logData.responseMessageId,
					LOG_RESPONSE_ERRORCODE, logData.responseErrorCode,
					LOG_RESPONSE_COMMANDSTATUS, logData.responseCommandStatus,
					LOG_RESPONSE_SEQNUMBER, logData.responseSequenceNumber
					);
		}
	}
	
	private int handleLogLevel(LogData logData) {
		if (LOG_LEVEL_DEFAULT.equalsIgnoreCase(logData.logLevel)) return LOG_DEFAULT;
		if (LOG_LEVEL_DEBUG.equalsIgnoreCase(logData.logLevel)) return LOG_DEBUG;
		return LOG_NONE;
	}
	
}
