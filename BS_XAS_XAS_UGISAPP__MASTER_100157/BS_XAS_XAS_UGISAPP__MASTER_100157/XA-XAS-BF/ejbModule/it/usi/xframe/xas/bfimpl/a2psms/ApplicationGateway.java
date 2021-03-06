package it.usi.xframe.xas.bfimpl.a2psms;

import com.ibm.ws.webservices.engine.client.Stub;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalDeliveryResponse;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.DeliveryResponse;
import it.usi.xframe.xas.bfutil.data.ENC_TYPE;
import it.usi.xframe.xas.bfutil.data.InternalDeliveryReport;
import it.usi.xframe.xas.bfutil.data.InternalMobileOriginated;
import it.usi.xframe.xas.util.Crypto;
import it.usi.xframe.xas.util.json.XConstants;
import it.usi.xframe.xas.wsutil.DeliveryReport;
import it.usi.xframe.xas.wsutil.MobileOriginated;
import it.usi.xframe.xas.wsutil.NotificationResponse;
import it.usi.xframe.xas.wsutil.StatusCodeType;
import it.usi.xframe.xas.wsutil.XasNotification;
//import it.usi.xframe.xas.wsutil.XasNotificationServiceLocator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.MessageFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * WebService application gateway for sms notification service (MO/DR).
 */
public class ApplicationGateway { //#TIM
	private static Logger logger = LoggerFactory.getLogger(ApplicationGateway.class);
	private static String XAS_NOTIFICATION_SERVICE = XasNotification.class.getName().replaceAll(".*\\.", "");
	final static 		String SERVICE_LOOKUP 		= "java:comp/env/service/XasNotificationService";

	final static Byte DELIVERY_REPORT_NO = new Byte((byte)0);
	final static Byte DELIVERY_REPORT_YES = new Byte((byte)1);

	public InternalDeliveryResponse sendDeliveryReport(XasUser xasUser, InternalDeliveryReport internalDeliveryReport, InternalDeliveryResponse internalDeliveryResponse) throws XASException {
  		String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, ApplicationGateway.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendDeliveryReport").preset("default");
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
					, "a_internalDeliveryReport", XConstants.XSTREAMER.toXML(internalDeliveryReport)
					, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
					).getLogRow(true)); // Debug and keep row
			sl.reload("default");
		}
		
//		XasNotificationServiceLocator locator = new XasNotificationServiceLocator();
		XasNotification service;
		String endPoint = xasUser.getEndPoint() + XAS_NOTIFICATION_SERVICE;
        try {
    		// WARNING: using direct lookup to enable the WSSE Outbound handler to be functional and override the standard IBM wsse.
//    		service = locator.getXasNotification(new URL(endPoint));
    		service = ((it.usi.xframe.xas.wsutil.XasNotificationService)new InitialContext().lookup(SERVICE_LOOKUP)).getXasNotification(new URL(endPoint));
			if(service == null) 
				throw new XASException(Constants.XAS00085E_MESSAGE, null, Constants.XAS00085E_CODE, new Object[] {"Failed to instantiate Application endPoint for " + endPoint});
			setTimeOut((Stub) service, xasUser.getDRtimeOut());
        } catch (ServiceException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"ServiceException for " + endPoint, errors.toString()});
        } catch (NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"NamingException for " + endPoint, errors.toString()});
        } catch (MalformedURLException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"ServiceException for " + endPoint, errors.toString()});
        }

		// instantiate the service
		DeliveryReport deliveryReport = internalDeliveryReport.getDeliveryReport();

		NotificationResponse response;
		DeliveryResponse deliveryResponse;
        try {
        	response = service.notifyDeliveryReport(deliveryReport);
        	if (StatusCodeType.XAS00080I.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00000I_CODE, Constants.XAS00000I_MESSAGE);
        		logger.info(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else if (StatusCodeType.XAS00081E.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00081E_CODE, Constants.XAS00081E_MESSAGE, response.getStatusMsg());
        		logger.info(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else if (StatusCodeType.XAS00082E.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00082E_CODE, Constants.XAS00082E_MESSAGE, response.getStatusMsg());
        		logger.error(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00084E_CODE, Constants.XAS00084E_MESSAGE, response.getStatusMsg());
        		logger.error(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	}
        	deliveryResponse.setSmsIds(deliveryReport.getSmsIds());
    		internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
    		internalDeliveryResponse.setWsEndpointSC(response.getStatusCode().toString());
    		internalDeliveryResponse.setWsEndpointSM(response.getStatusMsg());

        } catch (RemoteException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
        	deliveryResponse = new DeliveryResponse(Constants.XAS00085E_CODE, MessageFormat.format(Constants.XAS00085E_MESSAGE, new Object[]{"RemoteException for " + endPoint, e.getCause()})); 
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"RemoteException for " + endPoint, e.getCause()});
        }  
        return internalDeliveryResponse;
	}

	public InternalDeliveryResponse sendMobileOriginated(XasUser xasUser, InternalMobileOriginated internalMobileOriginated, InternalDeliveryResponse internalDeliveryResponse) throws XASException {
 		String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, ApplicationGateway.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendMobileOriginated").preset("default");
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
					, "a_internalMobileOriginated", XConstants.XSTREAMER.toXML(internalMobileOriginated)
					, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
					).getLogRow(true)); // Debug and keep row
			sl.reload("default");
		}
		
//		XasNotificationServiceLocator locator = new XasNotificationServiceLocator();
		XasNotification service;
		String endPoint = xasUser.getEndPoint() + XAS_NOTIFICATION_SERVICE;
        try {
    		// Instantiate the service
    		// WARNING: using direct lookup to enable the WSSE Outbound handler to be functional and override the standard IBM wsse.
//    		service = locator.getXasNotification(new URL(endPoint));
    		service = ((it.usi.xframe.xas.wsutil.XasNotificationService)new InitialContext().lookup(SERVICE_LOOKUP)).getXasNotification(new URL(endPoint));
			if(service == null) 
				throw new XASException(Constants.XAS00085E_MESSAGE, null, Constants.XAS00085E_CODE, new Object[] {"Failed to instantiate Application endpoint for " + endPoint});
			setTimeOut((Stub) service, xasUser.getMOtimeOut());
        } catch (ServiceException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"ServiceException for " + endPoint, errors.toString()});
        } catch (NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"NamingException for " + endPoint, errors.toString()});
        } catch (MalformedURLException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"ServiceException for " + endPoint, errors.toString()});
        }

    	MobileOriginated mobileOriginatedRequest = internalMobileOriginated.getMobileOriginated();
    	// Cloning due to possible encryption
    	MobileOriginated mobileOriginatedClone = new MobileOriginated();
    	mobileOriginatedClone.setMoDate(mobileOriginatedRequest.getMoDate());
    	mobileOriginatedClone.setMoDestinator(mobileOriginatedRequest.getMoDestinator());
    	mobileOriginatedClone.setProviderDate(mobileOriginatedRequest.getProviderDate() != null ? mobileOriginatedRequest.getProviderDate() : mobileOriginatedRequest.getMoDate());
    	mobileOriginatedClone.setSmsIds(mobileOriginatedRequest.getSmsIds());
    	mobileOriginatedClone.setUuid(mobileOriginatedRequest.getUuid());
		if (xasUser.getMoEncryption() != null && !ENC_TYPE.NO_ENCRYPTION.equals(xasUser.getMoEncryption())) {
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_STEP, "encrypting", SmartLog.K_PARAMS, xasUser.getMoEncryption().getValue()).getLogRow(true));
			String [] encryptParams = new String[] {mobileOriginatedRequest.getUuid()};
			mobileOriginatedClone.setPhoneNumber(Crypto.encrypt(xasUser.getMoEncryption(), mobileOriginatedRequest.getPhoneNumber(), encryptParams));
			mobileOriginatedClone.setMsg(Crypto.encrypt(xasUser.getMoEncryption(), mobileOriginatedRequest.getMsg(), encryptParams));
		} else {
	    	mobileOriginatedClone.setPhoneNumber(mobileOriginatedRequest.getPhoneNumber());
	    	mobileOriginatedClone.setMsg(mobileOriginatedRequest.getMsg());
		}
    	
		NotificationResponse response;
		DeliveryResponse deliveryResponse;
		try {
			
			response = service.notifyMobileOriginated(mobileOriginatedClone);
        	if (StatusCodeType.XAS00080I.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00000I_CODE, Constants.XAS00000I_MESSAGE);
        		logger.info(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else if (StatusCodeType.XAS00081E.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00081E_CODE, Constants.XAS00081E_MESSAGE, response.getStatusMsg());
        		logger.info(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else if (StatusCodeType.XAS00082E.equals(response.getStatusCode())) {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00082E_CODE, Constants.XAS00082E_MESSAGE, response.getStatusMsg());
        		logger.error(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	} else {
            	deliveryResponse = new DeliveryResponse(Constants.XAS00084E_CODE, Constants.XAS00084E_MESSAGE, response.getStatusMsg());
        		logger.error(
        				sl.reload("default").logIt(
        						Constants.LOG_KEY_EP_STATUS_CODE, response.getStatusCode().toString()
        					  , Constants.LOG_KEY_EP_STATUS_MSG, response.getStatusMsg()).getLogRow()
        					);
        	}
			
        	deliveryResponse.setSmsIds(mobileOriginatedClone.getSmsIds());
   		    internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
    		internalDeliveryResponse.setWsEndpointSC(response.getStatusCode().toString());
    		internalDeliveryResponse.setWsEndpointSM(response.getStatusMsg());
        } catch (RemoteException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString()); 
        	deliveryResponse = new DeliveryResponse(Constants.XAS00085E_CODE, MessageFormat.format(Constants.XAS00085E_MESSAGE, new Object[]{"RemoteException for " + endPoint, e.getCause()})); 
    		throw new XASException(Constants.XAS00085E_MESSAGE, errors.toString(), Constants.XAS00085E_CODE, new Object[] {"RemoteException for " + endPoint, e.getCause()});
        }  
        
        return internalDeliveryResponse;
	}

	// Not less than 1000 otherwise websphere will multiply by 1000.
	private void setTimeOut(Stub stub, Integer timeOut) {
		if (timeOut != null) {
			logger.debug("setTimeOut " + timeOut.intValue());
			((Stub) stub).setTimeout(timeOut.intValue()); // Not less than 1000 otherwise websphere will multiply by 1000.
//			((Stub) stub).setWriteTimeout(timeOut.intValue()); // Not less than 1000 otherwise websphere will multiply by 1000.
		}
	}
	
 }
