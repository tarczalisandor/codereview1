/*
 * Created on Jan 25, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.rmi.RemoteException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PgeTokenSendSms {
	/**
	 * 
	 * @param sms
	 * @param sender - sender info bean (optional)
	 * @throws ServiceFactoryException
	 * @throws RemoteException
	 * @throws XASException
	 */
	public void sendSms(SmsMessage	sms, SmsSenderInfo sender) throws ServiceFactoryException, RemoteException, XASException{
		new SendSms().sendSms(sms, sender);
	}
	
	public InternationalSmsResponse sendInternationalSms(InternationalSmsMessage sms, SmsDelivery delivery, SmsBillingInfo billing) throws ServiceFactoryException, RemoteException, XASException{
		return new SendSms().sendInternationalSms(sms, delivery, billing);
	}
}
