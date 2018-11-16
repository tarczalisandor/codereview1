/*
 * Created on Sep 29, 2013
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.minipop;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.sms.serverweb.SendSMS_Port;
import eu.unicredit.sms.serverweb.SendSMS_ServiceLocator;
//import it.usi.xframe.xas.util.xml.data.StructData;

/**
 * @author C305373
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewaySmsMinipop { 
/*	DEPRECATED 2016.07.18	
	extends GatewaySms {

	private static Log log = LogFactory.getLog(GatewaySmsMinipop.class);
	public static final String TYPE = "minipop";
	
	private static final String LOG_PREFIX = "SmsMinipop";
	private static final String LOG_STATUS = "SmsMinipop status: msg correctly sent";

	private String prefix;
	private String smsloglevel;

	public GatewaySmsMinipop(String prefix, String smsloglevel) {
		this.prefix=prefix;
		this.smsloglevel=smsloglevel;
	}

	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException
	{
		try
		{
			SendSMS_ServiceLocator locator = new SendSMS_ServiceLocator();
			SendSMS_Port service= locator.getSendSMSImplPort(new URL("http://guupzm000102:9000/sendSMS"));
			if(service == null)
				throw new RemoteException("fail to instantiate SMS minipop proxy");
	
			service.send(sms.getPhoneNumber(), sms.getMsg(), sender.getAlias(), sender.getABICode());

			// log requested by UGIS Security Office
			securityLogger.securityLog(LOG_PREFIX, sms, sender, LOG_STATUS, smsloglevel, userid);
		}
		catch(Exception e)
		{
			throw new XASException(e); 
		}
	}

 	public String getPrefix() {
		return prefix;
	}

 	public void setPrefix(String string) {
		prefix = string;
	}
	*/
}


