package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsRequest3;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsResponse3;
import it.usi.xframe.xas.util.Crypto;
import it.usi.xframe.xas.util.json.XConstants;

import java.rmi.RemoteException;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

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
public class SendSms2 {

	private static Logger logger = LoggerFactory.getLogger(SendSms2.class);

	/**
	 * Version 2 for sendSms.
	 * @param smsRequest
	 * @return smsResponse
	 * @throws ServiceFactoryException
	 * @throws RemoteException
	 * @throws XASException
	 */
	public SmsResponse sendSms2(SmsRequest	smsRequest) throws ServiceFactoryException, RemoteException, XASException {
		IXasSendsmsServiceFacade facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
		SmsResponse smsResponse = null;
		try{ 
			SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, SendSms2.class.getName(), ConstantsSms.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG); 
			if (logger.isDebugEnabled()) { 
				logger.debug(sl.logIt(SmartLog.K_METHOD, "sendSms2", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(smsRequest), SmartLog.K_CRID, (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_CRID)).getLogRow());
			}
			smsResponse = facade.sendSms2(smsRequest);	
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
		
		return (SmsResponse) smsResponse.clone(); // CLONE operation due to WebService introspection that explode the SmsResponse3 object passed by the EJB 
	}

	/**
	 * Version 3 for sendSms interface sendSms2:3 (new version interface 3).
	 * @param smsRequest3
	 * @return smsResponse3
	 * @throws RemoteException
	 */
	public SmsResponse3 sendSms3(SmsRequest3 smsRequest) throws RemoteException {
		IXasSendsmsServiceFacade facade;
        try {
	        facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
        } catch (ServiceFactoryException e) {
        	throw new RemoteException("ServiceFactoryException", e);
        }
        SmsResponse3 smsResponse = null;
		try{ 
			// UUID retrieved from SmsRequest3 or from MDC, Header, Generated
			String myUUID = smsRequest.getUuid();
			
			SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, SendSms2.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "sendSms3", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, ConstantsSms.K_UUID_TYPE, myUUID).preset("default"); 

// 			// Write to log the associated UUID and CRID if present
//			String myCRID = (String) MDC.get(Constants.MY_CRID_KEY);
// 			if (myCRID != null) {
// 				logger.info(sl.logIt(SmartLog.K_STEP, "correlating", SmartLog.K_CRID, myCRID).getLogRow(true));
// 			}

 			// If debug dump parameters
 			if (logger.isDebugEnabled()) { 
				logger.debug(sl.reload("default").logIt(SmartLog.K_STEP, "displayParams", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(smsRequest)).getLogRow());
			}
 			
			smsResponse = facade.sendSms3(smsRequest);	
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
		
		return smsResponse;
	}

}
