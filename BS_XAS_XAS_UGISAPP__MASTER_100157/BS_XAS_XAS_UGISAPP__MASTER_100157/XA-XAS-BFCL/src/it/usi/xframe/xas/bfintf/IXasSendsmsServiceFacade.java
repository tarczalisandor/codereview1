/*
 * Created on Jan 18, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import it.usi.xframe.system.eservice.IServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.DeliveryResponse;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.NotificationResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsRequest3;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsResponse3;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;
import it.usi.xframe.xas.wsutil.DeliveryReport;
import it.usi.xframe.xas.wsutil.MobileOriginated;

import java.rmi.RemoteException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IXasSendsmsServiceFacade extends IServiceFacade {

	public static String KEY_ID= "XasSendsms";
	
	/**
	 * Send SMS.
	 * @param sms - required message info (text and destination)
	 * @param sender - sender info (optional)
	 * @throws RemoteException
	 * @throws XASException
	 */
	public void sendSms(SmsMessage	sms, SmsSenderInfo sender) throws RemoteException, XASException;
	//public void sendSms(SmsMessage	sms) throws RemoteException, XASException;

	/**
	 * Send SMS version 2.
	 * @param request - SmsRequest object
	 * @return SmsResponse
	 * @throws RemoteException
	 */
	public SmsResponse sendSms2(SmsRequest	request) throws RemoteException;

	/**
	 * Send SMS version 3.
	 * @param request - SmsRequest3 object
	 * @return SmsResponse
	 * @throws RemoteException
	 */
	public SmsResponse3 sendSms3(SmsRequest3	request) throws RemoteException;

	/**
	 * Get configuration loaded from file.
	 * @return A string representation of the configuration.
	 * @throws RemoteException
	 */
	public String getConfiguration(String param) throws RemoteException;
	
	/**
	 * Receive a delivery report by the provider.
	 * @param deliveryRequest
	 * @throws RemoteException
	 */
	public DeliveryResponse receiveTelecomDeliveryReport(DeliveryReport deliveryReport) throws RemoteException;
	
	/**
	 * Receive a mobile originated by the provider.
	 * @param deliveryRequest
	 * @throws RemoteException
	 */
	public DeliveryResponse receiveTelecomMobileOriginated(MobileOriginated mobileOriginated) throws RemoteException;
	
	/**
	 * Send SMS abroad to multiple numbers at once.
	 * Since July 2013 is deprecated. Use sendSms() instead.
	 */
	public InternationalSmsResponse sendInternationalSms(
		InternationalSmsMessage sms, 
		SmsDelivery delivery,
		SmsBillingInfo billing
		) throws RemoteException, XASException;

	public NotificationResponse xasAppNotify(String myUUID, Object object) throws RemoteException;

}

