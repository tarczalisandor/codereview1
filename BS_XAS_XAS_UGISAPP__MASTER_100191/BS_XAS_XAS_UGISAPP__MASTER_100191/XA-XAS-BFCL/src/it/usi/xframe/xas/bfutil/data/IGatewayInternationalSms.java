/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

import java.security.Principal;

import it.usi.xframe.xas.bfutil.XASException;


public interface IGatewayInternationalSms {
	
	public InternationalSmsResponse sendInternationalSms(
			InternationalSmsMessage sms, 
			SmsDelivery delivery, 
			SmsBillingInfo billing,
			Principal user,
			boolean isUserInRole
			) throws XASException;
}
