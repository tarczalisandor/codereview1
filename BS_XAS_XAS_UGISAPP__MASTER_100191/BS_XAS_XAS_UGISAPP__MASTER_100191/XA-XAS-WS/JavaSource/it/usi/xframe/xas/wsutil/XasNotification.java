/**
 * XasNotification.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class XasNotification  {
 	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(XasNotification.class);

 	public NotificationResponse notifyMobileOriginated(MobileOriginated mobileOriginated) throws java.rmi.RemoteException {
		String myUUID = mobileOriginated.getUuid() != null ? mobileOriginated.getUuid().toString() : UUID.randomUUID().toString();

		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasNotification.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
									.logIt(SmartLog.K_METHOD, "notifyMobileOriginated", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).preset("default"); 

			// Write to log the associated UUID and CRID if present
		String myCRID = (String) MDC.get(ConstantsSms.MY_CRID_KEY);
		if (myCRID != null) {
			logger.info(sl.logIt(SmartLog.K_STEP, "correlating", SmartLog.K_CRID, myCRID).getLogRow(true));
		}
		logger.info(sl.getLogRow(true));
		
		NotificationResponse notificationResponseType = new NotificationResponse();
		IXasSendsmsServiceFacade facade = null;
        try {
	        facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			it.usi.xframe.xas.bfutil.data.NotificationResponse nr = facade.xasAppNotify(myUUID, mobileOriginated);	
			notificationResponseType.setStatusCode(StatusCodeType.fromString(nr.getStatusCode()));
			notificationResponseType.setStatusMsg(nr.getStatusMsg());
        } catch (ServiceFactoryException e) {
        	e.printStackTrace();
        	logger.error(myUUID + ".checkstacktrace");
            throw new java.rmi.RemoteException("XASException", e.getCause());
		} finally {
			if (facade != null) XasServiceFactory.getInstance().dispose(facade);
        }
		
        return notificationResponseType;
    }

    public NotificationResponse notifyDeliveryReport(DeliveryReport deliveryReport) throws java.rmi.RemoteException {
		// UUID retrieved from MDC, Header, Generated
		String myUUID = deliveryReport.getUuid() != null ? deliveryReport.getUuid().toString() : UUID.randomUUID().toString();

		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasNotification.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
									.logIt(SmartLog.K_METHOD, "notifyDeliveryReport", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).preset("default"); 

			// Write to log the associated UUID and CRID if present
		String myCRID = (String) MDC.get(ConstantsSms.MY_CRID_KEY);
		if (myCRID != null) {
			logger.info(sl.logIt(SmartLog.K_STEP, "correlating", SmartLog.K_CRID, myCRID).getLogRow(true));
		}
		logger.info(sl.getLogRow(true));

		NotificationResponse notificationResponseType = new NotificationResponse();
		IXasSendsmsServiceFacade facade = null;
        try {
	        facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			it.usi.xframe.xas.bfutil.data.NotificationResponse nr = facade.xasAppNotify(myUUID, deliveryReport);	
			notificationResponseType.setStatusCode(StatusCodeType.fromString(nr.getStatusCode()));
			notificationResponseType.setStatusMsg(nr.getStatusMsg());
        } catch (ServiceFactoryException e) {
        	e.printStackTrace();
        	logger.error(myUUID + ".checkstacktrace");
            throw new java.rmi.RemoteException("XASException", e.getCause());
		} finally {
			if (facade != null) XasServiceFactory.getInstance().dispose(facade);
        }

        return notificationResponseType;
    }

}
