package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityLog
{
	private static Log log = LogFactory.getLog(SecurityLog.class);
	
	private static final String LOG_ERROR            = "ERR";
	private static final String LOG_OK               = "OK";

	public static final String LOG_LEVEL_NONE        = "none";
	public static final String LOG_LEVEL_DEFAULT     = "default";
	public static final String LOG_LEVEL_DEBUG       = "full";

	private static final String LOG_KEY_LOGPREFIX    = "smslogprefix=";
	private static final String LOG_KEY_SMSSTATUS    = "smsstatus=";
	private static final String LOG_KEY_ERRORMESSAGE = "errormessage=";
	private static final String LOG_KEY_LOGLEVEL     = "dbglevel=";
	private static final String LOG_KEY_USERID       = "userid=";
	private static final String LOG_KEY_SRCALIAS     = "srcAlias=";
	private static final String LOG_KEY_USEDALIAS    = "usedAlias=";
	private static final String LOG_KEY_SRCABI       = "srcABI=";
	private static final String LOG_KEY_USEDABI      = "usedABI=";
	private static final String LOG_KEY_ORIGINATOR   = "originator=";
	private static final String LOG_KEY_DSTPHONENUM  = "dstphonenumber=";
	private static final String LOG_KEY_SMSMESSAGE   = "smsmessage=";
	private static final String LOG_KEY_SMSID        = "smsid=";
	private static final String LOG_KEY_SMSPART      = "smspart=";
	private static final String LOG_KEY_SMSLENGTH    = "smslength=";
	private static final String LOG_KEY_SMSENC       = "smsenc=";
	private static final String LOG_KEY_MSGLENGTH    = "msglength=";
	private static final String LOG_KEY_GATEWAYCONTACTED = "gatewaycontacted=";
	private static final String LOG_KEY_TIMEELAPSED  = "timeelapsed=";


	//	this log is used by Security Office
	public void securityLog(LogData logData)
	{
		// smsloglevel: none|default|full
		String loglevel = logData.logLevel.toLowerCase();
		
		if ("default".equals(loglevel)){
			log(logData.logPrefix, LOG_OK, logData.errorMessage, LOG_LEVEL_DEFAULT
				,logData.userid, logData.originalAlias, logData.usedAlias, logData.originalAbiCode, logData.usedAbiCode
				,logData.originator, logData.normalizedDestinationNumber, "", logData.smsId
				,logData.messageLength, logData.smsEncoding, logData.messagePart, logData.messagePartLength
				,logData.gatewayContacted, logData.chrono.getElapsed());
		} else if ("full".equals(loglevel)) {
			log(logData.logPrefix, LOG_OK, logData.errorMessage, LOG_LEVEL_DEFAULT
				,logData.userid, logData.originalAlias, logData.usedAlias, logData.originalAbiCode, logData.usedAbiCode
				,logData.originator, logData.normalizedDestinationNumber, logData.originalMsg, logData.smsId
				,logData.messageLength, logData.smsEncoding, logData.messagePart, logData.messagePartLength
				,logData.gatewayContacted, logData.chrono.getElapsed());
	 	} else {
	 		// default smsloglevel --> none
			return;
		}
	}

	public void errorLog(LogData logData)
	{
		// smsloglevel: none|default|full
		String loglevel = logData.logLevel.toLowerCase();

		if ("default".equals(loglevel)){
			log(logData.logPrefix, LOG_ERROR, logData.errorMessage, LOG_LEVEL_DEFAULT
				,logData.userid, logData.originalAlias, logData.usedAlias, logData.originalAbiCode, logData.usedAbiCode
				,logData.originator, logData.normalizedDestinationNumber, "", logData.smsId
				,logData.messageLength, logData.smsEncoding, logData.messagePart, logData.messagePartLength
				,logData.gatewayContacted, logData.chrono.getElapsed());
		} else if ("full".equals(loglevel)) {
			log(logData.logPrefix, LOG_ERROR, logData.errorMessage, LOG_LEVEL_DEFAULT
				,logData.userid, logData.originalAlias, logData.usedAlias, logData.originalAbiCode, logData.usedAbiCode
				,logData.originator, logData.normalizedDestinationNumber, logData.originalMsg, logData.smsId
				,logData.messageLength, logData.smsEncoding, logData.messagePart, logData.messagePartLength
				,logData.gatewayContacted, logData.chrono.getElapsed());
		} else {
			// default smsloglevel --> none
			return;
		}
	}

	
	//	this log is used by Security Office
	private void log(String logPrefix, String smsStatus, String errorMessage, String logLevel,
			String userid, String srcAlias, String usedAlias, String srcAbiCode, String usedAbiCode,
			String originator, String destination, String smsMsg, String smsId,
			int smsLength, String smsEncoding, String messagePart, String messagePartLength,
			boolean gatewayContacted, long timeElapsed)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(LOG_KEY_LOGPREFIX + "\"" + logPrefix + "\"");
		sb.append("," + LOG_KEY_SMSSTATUS + "\"" + normalizeString(smsStatus) + "\"");
		sb.append("," + LOG_KEY_ERRORMESSAGE + "\"" + normalizeString(errorMessage) + "\"");
		sb.append("," + LOG_KEY_LOGLEVEL + "\"" + logLevel + "\"");
		sb.append("," + LOG_KEY_USERID + "\"" + userid + "\"");
		sb.append("," + LOG_KEY_SRCALIAS + "\"" + srcAlias + "\"");
		sb.append("," + LOG_KEY_USEDALIAS + "\"" + usedAlias + "\"");
		sb.append("," + LOG_KEY_SRCABI + "\"" + srcAbiCode + "\"");
		sb.append("," + LOG_KEY_USEDABI + "\"" + usedAbiCode + "\"");
		sb.append("," + LOG_KEY_ORIGINATOR + "\"" + originator + "\"");
		sb.append("," + LOG_KEY_DSTPHONENUM + "\"" + destination + "\"");
		sb.append("," + LOG_KEY_SMSMESSAGE + "\"" + normalizeString(smsMsg) + "\"");
		sb.append("," + LOG_KEY_SMSID + "\"" + normalizeString(smsId) + "\"");
		sb.append("," + LOG_KEY_SMSLENGTH + "\"" + smsLength + "\""); 
		sb.append("," + LOG_KEY_SMSENC + "\"" + smsEncoding + "\"");
		sb.append("," + LOG_KEY_SMSPART + "\"" + messagePart + "\"");
		sb.append("," + LOG_KEY_MSGLENGTH + "\"" + messagePartLength + "\"");
		sb.append("," + LOG_KEY_GATEWAYCONTACTED + "\"" + gatewayContacted + "\"");
		sb.append("," + LOG_KEY_TIMEELAPSED + "\"" + Chronometer.formatMillis(timeElapsed) + "\"");
		log.info(sb);
	}

	private String normalizeString(String value)
	{
		if(value==null)
			return null;
		StringBuffer normal = new StringBuffer("");
		for (int i=0; i<value.length(); ++i)
		{
			String a = value.substring(i,i+1);
			if(a.equals("\""))
				normal.append("'");
			else
				normal.append( a );
		}
		return normal.toString();
	}

	
	
	

	/*
	 *  THE NETX METHODS MAY BE DELETED 
	 */
	
	public void securityLog(String logPrefix,
			SmsMessage sms,
			SmsSenderInfo sender,
			String status,
			String smsloglevel,
			String userid)
	{
		String usedAbi = null;
		String usedAlias = null;
		if(sender!=null) {
			usedAbi = sender.getABICode();
			usedAlias = sender.getAlias();
		}
		securityLog(logPrefix, sms, sender, status, smsloglevel, userid, null, usedAbi, usedAlias, "", "");
	}
	
	//	this log is used by Security Office
	public void securityLog(String logPrefix,
			SmsMessage sms,
			SmsSenderInfo sender,
			String status,
			String smsloglevel,
			String userid,
			String originator, 
			String usedABI,
			String usedAlias,
			String msgid,
			String userdata)
	{
		// common logging utility
		StringBuffer sb = new StringBuffer();
		sb.append("smslogprefix=" + logPrefix);
		sb.append(",smsstatus=OK");
		
		// sender info
		String srcAlias = (sender!=null) ?sender.getAlias() :null;
		String srcABI = (sender!=null) ?sender.getABICode() :null;
		
		// smsloglevel: none|default|full
		String loglevel = smsloglevel.toLowerCase();
		
		if ("default".equals(loglevel)){
			sb.append("," + LOG_KEY_LOGLEVEL + LOG_LEVEL_DEFAULT);
			sb.append(",userid=" + userid);
			sb.append(",srcAlias=" + srcAlias);
			sb.append(",usedAlias=" + usedAlias);
			sb.append(",srcABI=" + srcABI);
			sb.append(",usedABI=" + usedABI);
			sb.append(",originator=" + originator);
			sb.append(",dstphonenumber=" + sms.getPhoneNumber());
			sb.append(",smsgatewayresponse=" + normalizeString(status));
			sb.append(",smsid=" + normalizeString(msgid));
			sb.append("," + normalizeString(userdata));
		} else if ("full".equals(loglevel)) {
			sb.append("," + LOG_KEY_LOGLEVEL + LOG_LEVEL_DEBUG);
			sb.append(",userid=" + userid);
			sb.append(",srcAlias=" + srcAlias);
			sb.append(",usedAlias=" + usedAlias);
			sb.append(",srcABI=" + srcABI);
			sb.append(",usedABI=" + usedABI);
			sb.append(",originator=" + originator);
			sb.append(",dstphonenumber=" + sms.getPhoneNumber());
			sb.append(",smsmessage=" + normalizeString(sms.getMsg()));
			sb.append(",smsgatewayresponse=" + normalizeString(status));
			sb.append(",smsid=" + msgid);
			sb.append("," + normalizeString(userdata));
	 	} else {
	 		// default smsloglevel --> none
			return;
		}
		log.info(sb);
	}
	

	public void errorLog(String errMsg,
			String logPrefix,
			SmsMessage sms,
			SmsSenderInfo sender,
			String smsloglevel,
			String userid)
	{
		String usedAbi = null;
		String usedAlias = null;
		if(sender!=null) {
			usedAbi = sender.getABICode();
			usedAlias = sender.getAlias();
		}
		errorLog(errMsg, logPrefix, sms, sender, smsloglevel, userid, null, usedAbi, usedAlias, "", "");
	}

	// this log is used by Security Office
	public void errorLog(String errMsg,
			String logPrefix,
			SmsMessage sms,
			SmsSenderInfo sender,
			String smsloglevel,
			String userid,
			String originator, 
			String usedABI,
			String usedAlias,
			String msgid,
			String userdata)
	{
		// common logging utility
		StringBuffer sb = new StringBuffer();
		sb.append("smslogprefix=" + logPrefix);
		sb.append(",smsstatus=ERR");
		sb.append(",errormessage=" + errMsg);
	
		// sender info
		String srcAlias = (sender!=null) ?sender.getAlias() :null;
		String srcABI = (sender!=null) ?sender.getABICode() :null;
		
		// smsloglevel: none|default|full
		String loglevel = smsloglevel.toLowerCase();
		
		if ("default".equals(loglevel)){
			sb.append("," + LOG_KEY_LOGLEVEL + LOG_LEVEL_DEFAULT);
			sb.append(",userid=" + userid);
			sb.append(",srcAlias=" + srcAlias);
			sb.append(",usedAlias=" + usedAlias);
			sb.append(",srcABI=" + srcABI);
			sb.append(",usedABI=" + usedABI);
			sb.append(",originator=" + originator);
			sb.append(",dstphonenumber=" + sms.getPhoneNumber());
			sb.append(",smsid=" + normalizeString(msgid));
			sb.append("," + normalizeString(userdata));
		} else if ("full".equals(loglevel)) {
			sb.append("," + LOG_KEY_LOGLEVEL + LOG_LEVEL_DEBUG);
			sb.append(",userid=" + userid);
			sb.append(",srcAlias=" + srcAlias);
			sb.append(",usedAlias=" + usedAlias);
			sb.append(",srcABI=" + srcABI);
			sb.append(",usedABI=" + usedABI);
			sb.append(",originator=" + originator);
			sb.append(",dstphonenumber=" + sms.getPhoneNumber());
			sb.append(",smsmessage=" + normalizeString(sms.getMsg()));
			sb.append(",smsid=" + normalizeString(msgid));
			sb.append("," + normalizeString(userdata));
		} else {
			// default smsloglevel --> none
			return;
		}
		log.info(sb);
	}
}
