/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.virtual;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewaySmsVirtual { 
/*	DEPRECATED 2016.07.18	
	extends GatewaySms {
	
	private static Log log = LogFactory.getLog(GatewaySmsVirtual.class);

	public static final String TYPE = "logtofile";

	private String prefix;
	private String smsloglevel;

	public GatewaySmsVirtual(String prefix, String smsloglevel) {
		this.prefix = prefix;
		this.smsloglevel = smsloglevel;
	}

	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException {
		sendSms(sms,sender,userid,null);
	}

	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid, Boolean isUserInRole) throws XASException
	{
		LogData logData = new LogData();
		logData.channel = InternalSmsRequest.CHANNEL_WS;
		logData.version = "1";
		logData.userid = userid;
		logData.logPrefix = this.prefix;
		logData.logLevel = this.smsloglevel;
		logData.isUserInRole = isUserInRole;
		logData.gatewayContacted = true;

		// validate parameters
		if(sms==null)
			throw new XASException("sms is null");
		if(userid==null)
			throw new XASException("userid is null");

		if(sms.getMsg()==null || sms.getMsg().length()==0)
			throw new XASException("no text in sms message");

		if(sms.getPhoneNumber()==null || sms.getPhoneNumber().length()==0)
			throw new XASException("no destination phone number");

		logData.originalMsg = sms.getMsg();
		logData.originalMsgLength = sms.getMsg().length();
		logData.originalDestinationNumber = sms.getPhoneNumber();
		logData.originalAbiCode = sender.getABICode();
		logData.originalAlias = sender.getAlias();
		logData.usedAbiCode = logData.originalAbiCode;
		logData.usedAlias = logData.originalAlias;

		logData.smsEncoding = "UNKNOWN";
		logData.messagePart = "1/1";
		logData.messageLength = logData.originalMsgLength;
		logData.messagePartLength = logData.messageLength + "/" + logData.messageLength;
		logData.gatewayContacted = true;
		
		// log requested by UGIS Security Office
		securityLogger.securityLog(logData);
	}

 	public String getPrefix() {
		return prefix;
	}

 	public void setPrefix(String string) {
		prefix = string;
	}
*/
}
