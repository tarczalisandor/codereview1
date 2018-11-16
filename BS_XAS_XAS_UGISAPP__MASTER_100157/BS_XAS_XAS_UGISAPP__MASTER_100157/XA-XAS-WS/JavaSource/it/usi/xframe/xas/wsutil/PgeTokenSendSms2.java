/**
 * SendSms2.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsResponse;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;

public class PgeTokenSendSms2 {

	private static Log log = LogFactory.getLog(PgeTokenSendSms2.class);

	/**
	 * Version 2 for sendSms.
	 * @param smsRequest
	 * @return smsResponse
	 * @throws ServiceFactoryException
	 * @throws RemoteException
	 * @throws XASException
	 */
	public SmsResponse sendSms2(SmsRequest	smsRequest) throws ServiceFactoryException, RemoteException, XASException{
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "SendSms2", (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_UUID), SmartLog.V_SCOPE_DEBUG); 
		log.debug(sl.logIt(SmartLog.K_METHOD, "sendSms2", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, smsRequest.toString(), SmartLog.K_CRID, (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_CRID)).getLogRow());
		SendSms2 sendSms2 = new SendSms2();
		
		return sendSms2.sendSms2(smsRequest);
	}

}
