/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.configuration.Client;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.xframe.slf.SLF_Exception;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class GatewaySms implements IGatewaySms
{
	protected SecurityLog securityLogger = new SecurityLog();
	private static Log log = LogFactory.getLog(GatewaySms.class);

	public void sendSms(SmsMessage message, SmsSenderInfo sender, String userid, Boolean isInRole) throws XASException {
		throw new XASException("This method needs to be overridden");
	}

	public SmsResponse sendSms2(InternalSmsRequest request, Client client, String userid, boolean isInRole) throws XASWrongRequestException, XASConfigurationException {
		throw new XASRuntimeException("This method needs to be overridden");
	}
	
	protected void logData(SlfLogger slfLogger, LogData logData) throws SLF_Exception {
		slfLogger.logData(log, logData);
	}
	
	protected void logError(SlfLogger slfLogger, LogData logData) {
		slfLogger.logError(log, logData);
	}
	
	public boolean isActive() {
		return true;
	}
	
}
