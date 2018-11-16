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
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IGatewaySms {
	/**
	 * Actually sends an sms.
	 *  
	 * @param message - sms message bean
	 * @param sender -  sender info (optional)
	 * @param userid - UBIS user requester (for logging purposes)
	 * @throws XASException
	 * @deprecated  Replaced by {@link #sendSms(it.usi.xframe.xas.bfutil.data.SmsMessage, it.usi.xframe.xas.bfutil.data.SmsSenderInfo, java.lang.String, java.lang.Boolean)} 
	 */
	void sendSms(SmsMessage message, SmsSenderInfo sender, String userid) throws XASException;

	/**
	 * Actually sends an sms.
	 *  
	 * @param message - sms message bean
	 * @param sender -  sender info (optional)
	 * @param userid - UBIS user requester (for logging purposes)
	 * @throws XASException
	 */
	void sendSms(SmsMessage message, SmsSenderInfo sender, String userid, Boolean isInRole) throws XASException;
	/**
	 * Actually sends an sms.
	 *  
	 * @param message - sms message bean
	 * @param sender -  sender info (optional)
	 * @param userid - UBIS user requester (for logging purposes)
	 * @throws XASException
	 * @throws XASConfigurationException 
	 */
	SmsResponse sendSms2(InternalSmsRequest request, Client client, String userid, boolean isInRole) throws XASWrongRequestException, XASConfigurationException;
}
