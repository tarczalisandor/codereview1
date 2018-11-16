/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.data.IGatewayInternationalSms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.xframe.slf.SLF_Exception;

/**
 * @author us01170
 *
 * //#20150511 replace SecurityLog model from GatewaySms for InternationalSms
 */
public abstract class GatewayInternationalSms implements IGatewayInternationalSms
{
	protected SecurityLog securityLogger = new SecurityLog();
	private static Log log = LogFactory.getLog(GatewayInternationalSms.class);

	
	protected void logData(SlfLogger slfLogger, LogData logData) throws SLF_Exception {
		slfLogger.logData(log, logData);
	}
	
	protected void logError(SlfLogger slfLogger, LogData logData) {
		slfLogger.logError(log, logData);
	}
	
}
