/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.acotel;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewaySmsAcotel {
	/*	DEPRECATED 2016.07.18	
							extends GatewaySms {


	private static Log log = LogFactory.getLog(GatewaySmsAcotel.class);
	public static final String TYPE = "acotel";

	private static final String ACOTEL_SMS_HTTP_EXCEPTION_CODE = "XAS.GatewaySmsAcotel.SmsHttpError";
	private static final String ACOTEL_HTTP_EXCEPTION_CODE     = "XAS.GatewaySmsAcotel.HttpError";
	private static final String ACOTEL_IO_EXCEPTION_CODE       = "XAS.GatewaySmsAcotel.IOError";

	private static String crlf = System.getProperty("line.separator");

	private String logprefix;
	private AcotelPageDestination[] pageDestinations;
	private String defaultPageUrl;
	private String smsloglevel;

	public GatewaySmsAcotel(String logprefix, AcotelPageDestination[] pages, String defUrl, String smsloglevel) {
		this.logprefix = logprefix;
		this.pageDestinations = pages;
		this.defaultPageUrl = defUrl;
		this.smsloglevel = smsloglevel;
	}

	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException {
		LogData logData = new LogData();
		logData.channel = InternalSmsRequest.CHANNEL_WS;
		logData.userid = userid;
		logData.logPrefix = this.logprefix;
		logData.logLevel = this.smsloglevel;
		logData.gatewayContacted = false;

		try
		{
			// validate parameters
			if(sms==null)
				throw new XASException("sms is null");
			if(userid==null)
				throw new XASException("userid is null");

			if(sms.getMsg()==null || sms.getMsg().length()==0)
				throw new XASException("no text in sms message");

			if(sms.getPhoneNumber()==null || sms.getPhoneNumber().length()==0)
				throw new XASException("no destination phone number");

			logData.userid = userid;
			logData.originalAbiCode = sender.getABICode();
			logData.originalAlias = sender.getAlias();
			logData.originalMsg = sms.getMsg();
			logData.originalMsgLength = sms.getMsg().length();
			logData.originalDestinationNumber = sms.getPhoneNumber();
			logData.messageLength = logData.originalMsgLength;
			logData.messagePart = "1/1";
			logData.messagePartLength = logData.messageLength +"/"+ logData.messageLength;
			logData.smsEncoding = "UNKNOWN";

			callAcotel(sms, sender, userid, logData);
		}
		catch(XASException e)
		{
			e.printStackTrace();
			log.error(e);
			logData.errorMessage = e.getMessage();
			securityLogger.errorLog(logData);
			throw e;
		}
	}

	private void callAcotel(SmsMessage sms, SmsSenderInfo sender, String userid, LogData logData) throws XASException
	{
		// Get correct Acotel page url based on phone number pattern match
		String currentUrl = null;
		for (int priority=0; priority<this.pageDestinations.length ; ++priority)
		{
			AcotelPageDestination page =this.pageDestinations[priority]; 
			if ( page.matches( sms.getPhoneNumber() ) ) {
				log.info("Phone number matches Acotel page ["+page.getDescription()+"].");
				currentUrl = page.getPageUrl();
				break;
			}
		}
		if ( currentUrl==null ) {
			log.info("Phone number doesn't match any predefined pattern. Used default Acotel page url ["+this.defaultPageUrl+"]");
			currentUrl = this.defaultPageUrl;
		}

		// update logData
		logData.gatewayContacted = true;

		PostMethod method = new PostMethod(currentUrl);
		method.addParameter("RIF", sms.getPhoneNumber());
		method.addParameter("MSG", sms.getMsg());
		HttpClient client = new HttpClient();
		try
		{
			int statusCode = client.executeMethod(method);
			// error decoding XML response from Acotel
			if (statusCode != HttpStatus.SC_OK) {
				StringBuffer err = new StringBuffer();
				err.append(crlf);
				err.append("Http Error calling: " + currentUrl + crlf);
				err.append("Http Status Code: " + statusCode + crlf);
				err.append("Http Error Description: " + HttpStatus.getStatusText(statusCode));
				securityLogger.errorLog("HttpException: " + err.toString(), logprefix, sms, sender, smsloglevel, userid);
				throw new XASException(ACOTEL_SMS_HTTP_EXCEPTION_CODE, err.toString());
			}
			String responseBody = new String(method.getResponseBody());
			log.info(responseBody);

			AcotelResponse resp = new AcotelResponse();
			resp.verifyAcotelXMLResponse(responseBody);
			
			log.info("Acotel status: " + resp.getSmsStatus());
			log.info("Acotel status description: " + resp.getSmsStatusDescription());
			log.info("The sms has been sent correctly.");
			
			// log requested by UGIS Security Office
			String status = "Acotel status: " + resp.getSmsStatus() + " - " + resp.getSmsStatusDescription();

			// update logData
			logData.errorMessage = status;
			securityLogger.securityLog(logData);
		}
		catch (HttpException e)
		{
			throw new XASException(ACOTEL_HTTP_EXCEPTION_CODE, e);
		}
		catch (IOException e)
		{
			throw new XASException(ACOTEL_IO_EXCEPTION_CODE, e);
		}
		catch (XASException e)
		{
			throw e;
		}
		finally {
			method.releaseConnection();
		}
	}
	

 	public String getLogprefix() {
		return logprefix;
	}

 	public void setLogprefix(String string) {
		logprefix = string;
	}

 	public String getUrl() {
		return defaultPageUrl;
	}

 	/*public void setUrl(String string) {
		defaultPageUrl = string;
	}*/
}
