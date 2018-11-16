package it.usi.xframe.xas.wsutil;

import java.rmi.RemoteException;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.DeliveryRequest;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;
import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Web Service implementation.
 * 
 * --- WARNING ---  WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING ---
 * WHEN CREATING WEB SERVICE BOTTOM UP WITH
 * Web Services \ Create Web Service
 * --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING --- WARNING ---
 * 
 * After the wizard completion re-setup the security authentication method for the SendSms & SendSms2 web services.
 * 
 * In WebContent \ WEB-INF \ webservices.xml \ Security Extensions 
 * \ Web Service Description Externsion [SendSmsService & SendSms2Service] \ Port Component Binding [SendSms & SendSms2] 
 * \ Login Config \ Add [BasicAuth & LTPA]
 * 
 * For Service it.eng.service.mgp.sms.SmsNotification add only BasicAuth.
 */
public class SendSms {
	private static Log log = LogFactory.getLog(SendSms.class);
	/**
	 * 
	 * @param sms
	 * @param sender - sender info bean (optional)
	 * @throws ServiceFactoryException
	 * @throws RemoteException
	 * @throws XASException
	 */
	public void sendSms(SmsMessage	sms, SmsSenderInfo sender) throws ServiceFactoryException, RemoteException, XASException{
		IXasSendsmsServiceFacade facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
		if (sender == null) sender = new SmsSenderInfo();
		try{ // 
			if (log.isDebugEnabled()) {
				SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, SendSms.class.getName(), Constants.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG); 
				log.debug(sl.logIt(SmartLog.K_METHOD, "sendSms", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, sms.getPhoneNumber(), "a_sender", sender.toString(), SmartLog.K_CRID, (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_CRID)).getLogRow());
			}
			facade.sendSms(sms, sender);	// at the moment 'sender' is ignored
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
	}
	
	/**
	 * 
	 * @param sms
	 * @param sender - sender info bean (optional)
	 * @throws ServiceFactoryException
	 * @throws RemoteException
	 * @throws XASException
	 */
	public void receiveDeliveryReport(DeliveryRequest deliveryRequest) throws ServiceFactoryException, RemoteException, XASException{
		IXasSendsmsServiceFacade facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
 		try{ // UUID retrieved from SOAP Header
			if (log.isDebugEnabled()) {
				SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, SendSms.class.getName(), Constants.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG); 
				log.debug(sl.logIt(SmartLog.K_METHOD, "receiveDeliveryReport", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, deliveryRequest.getPhoneNumber(), SmartLog.K_CRID, (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_CRID)).getLogRow());
			}
//			facade.receiveDeliveryReport(deliveryRequest);	// at the moment 'sender' is ignored
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
	}
	
	public InternationalSmsResponse sendInternationalSms(InternationalSmsMessage sms, SmsDelivery delivery, SmsBillingInfo billing) throws ServiceFactoryException, RemoteException, XASException{
		IXasSendsmsServiceFacade facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
		try{
			return facade.sendInternationalSms(sms, delivery, billing);
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
	}
}
