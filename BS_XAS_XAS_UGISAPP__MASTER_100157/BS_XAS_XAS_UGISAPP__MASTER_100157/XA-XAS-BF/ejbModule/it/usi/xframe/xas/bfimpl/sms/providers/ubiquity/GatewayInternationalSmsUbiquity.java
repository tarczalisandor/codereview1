/*
 * Created on Mar 28, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.ubiquity;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.GatewayInternationalSms;  //#20150511 replace SecurityLog
import it.usi.xframe.xas.bfimpl.sms.SlfLogger; 				  //#20150511 using SmartLogFacility
import eu.unicredit.xframe.slf.SLF_Exception;				  //#20150511 using SmartLogFacility
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfimpl.sms.providers.PhoneNumber;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.IGatewayInternationalSms;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;

import java.io.StringWriter;
import java.security.Principal;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author US00081
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewayInternationalSmsUbiquity extends GatewayInternationalSms implements IGatewayInternationalSms {
	 //#20150511 replaced Extends SecurityLog with GatewayInternationalSms
	private static final String LOG_PREFIX          = "UBIQUITYSMS";
	
	private static Log log = LogFactory.getLog(GatewayInternationalSmsUbiquity.class);
		
	// Queue connection factory name
	final static String QCF_NAME = "jms/UbiquityQueueConnectionFactory";
	// Queue connection factory object
	// NB: A ConnectionFactory object is a JMS administered object and supports concurrent use.
	protected QueueConnectionFactory factory = null;
	
	// Queue name
	final static String QUEUE_NAME = "jms/SendSmsUbiquityQueue";
	// Queue object
	protected Queue queue = null;
	
	//
	// Validation constants
	//
	//static final String ALL_DIGITS_RE = "[0-9]+";
	static final String INTERNATIONAL_PREFIX_PLACEHOLDER = "+";
	static final String EUROPEAN_INTERNATIONAL_PREFIX    = "00";	//see http://en.wikipedia.org/wiki/International_dialing_prefix
	static final int LEGAL_ENTITY_NAME_MAX_SIZE          = 25; 
	static final int SERVICE_NAME_MAX_SIZE               = 25;
	static final int MESSAGE_TEXT_MAX_SIZE               = 160;
	
	
	// singleton instance
	static private GatewayInternationalSmsUbiquity instance = null;
	
	static
	{
		try
		{
			instance = new GatewayInternationalSmsUbiquity();
		}
		catch (NamingException ex)
		{
			log.error( ex.toString() +" [see full stacktrace in system.err]");
			ex.printStackTrace();
		}
	}
	
	
	// get singleton instance
	public static GatewayInternationalSmsUbiquity getInstance() throws XASException
	{
		if ( instance==null ) 
			throw new XASException("SMS service not available");
		
		return instance;
	}
	

	/*
	 * Cconstructor : protected so the only way to create object is to call getInstance().
	 * Caches JNDI objects
	 */ 
	protected GatewayInternationalSmsUbiquity() throws NamingException
	{
		Context ctx = null;
		Context localCtx = null;
		try
		{
			ctx = new InitialContext();
			localCtx = (Context)ctx.lookup("java:comp/env"); // local context-sensitive JNDI namespace
	
			// Lookup the queue connection factory
			this.factory = (QueueConnectionFactory)localCtx.lookup(QCF_NAME);
	
			// Lookup the queue connection factory
			this.queue = (Queue)localCtx.lookup(QUEUE_NAME);
			
			log.info("QueueConnectionFactory ["+QCF_NAME+"] and Queue ["+QUEUE_NAME+"] objs looked up and cached successfully!");
		}
		finally
		{
			// no more used
			if (localCtx!=null) localCtx.close();
			if (ctx!=null) ctx.close();
		}
	}


	/** 
	 * Use A2PSMS.
	 * @deprecated
	 * @see it.usi.xframe.xas.bfutil.data.IGatewayInternationalSms#sendInternationalSms(it.usi.xframe.xas.bfutil.data.InternationalSmsMessage, it.usi.xframe.xas.bfutil.data.SmsDelivery[], it.usi.xframe.xas.bfutil.data.SmsBillingInfo, boolean)
	 */
	public InternationalSmsResponse sendInternationalSms(
		InternationalSmsMessage sms,
		SmsDelivery delivery,
		SmsBillingInfo billing,
		Principal user,
		boolean isUserInRole)
		throws XASException {
		
		log.debug("Method sendInternationalSms() invoked!");
		
		LogData logData = new LogData();
		logData.channel = Constants.CHANNEL_UB;
		logData.version = "1";
		SlfLogger slfLogger = new SlfLogger();	//#20150511 using SmartLogFacility
		logData.chrono.start();
		logData.userid = user.getName();
		logData.isUserInRole = new Boolean(isUserInRole);
		logData.logPrefix = LOG_PREFIX;
		logData.logLevel = SlfLogger.LOG_LEVEL_DEFAULT; //#20150511 replaced SecurityLog.LOG_LEVEL_DEFAULT
		logData.smsEncoding = "UNKNOWN";
		logData.usedAbiCode = logData.originalAbiCode;
		logData.gatewayContacted = true;
		logData.messagePart = "1/1";

		String throwMsg = "";
		if (sms!=null) {
			logData.originalMsg = sms.getText();
			logData.originalMsgLength = sms.getText().length();
			logData.messageLength = logData.originalMsgLength;
			logData.messagePartLength = logData.messageLength + "/" + logData.messageLength;
		} else {
			throwMsg += "InternationalSmsMessage object is null";
		} 
		if (delivery!=null) {
			logData.originalDestinationNumber = delivery.getPhoneNumber();
			logData.normalizedDestinationNumber = delivery.getPhoneNumber();	//added April 2015
		} else {
			throwMsg +=( "".equals(throwMsg) ? "" : ", " ) 
				     + "SmsDelivery object is null";
		}
		if (billing!=null) {
			logData.originalAbiCode = billing.getLegalEntity();
		} else {
			throwMsg +=( "".equals(throwMsg) ? "" : ", " ) 
			         + "SmsBillingInfo object is null";
		}
		if (throwMsg != null && !"".equals(throwMsg)) {
			logData.errorMessage = throwMsg;
			logError(slfLogger, logData); //#20150511 added Extends GatewayInternationalSms ....
			throw new NullPointerException(throwMsg);
		}


		/*
		if ( deliveries!=null )
			for (int i=0; i<deliveries.length ;++i)
			log.debug("deliveries[i].phoneNumber: "+deliveries[i].getPhoneNumber());
		if ( billing!=null ) {
			log.debug("billing.legalEntity: "+billing.getLegalEntity());
			log.debug("billing.serviceName: "+billing.getServiceName());
		}*/
		
		// Validate input data
		InternationalSmsResponse internationalSmsResponse = validateInputData(sms, delivery, billing);
		if (internationalSmsResponse==null) {
			internationalSmsResponse = new InternationalSmsResponse();
			QueueConnection connection = null;
			QueueSession session = null;
 			try {
				// Create a JMS connection.  This will actually connect
				// to WebSphere MQ.
				connection = this.factory.createQueueConnection();
				connection.start();
				
				// All work is done on a session.  Let's create one.
				//
				// We won't be too particular about transactions.
				// If we were, we'd acknowledge everything ourselves.
				boolean transacted = false;
				session = connection.createQueueSession(transacted, Session.AUTO_ACKNOWLEDGE);
							
				// Build text message
				String xmlMsg = buildMessage(sms, delivery, billing);
				log.debug("XML msg:\n[\n"+xmlMsg+"]");
				TextMessage outMessage = session.createTextMessage();
				outMessage.setText(xmlMsg);
				
				// Use the Queue  to create the publisher
				QueueSender sender = session.createSender(this.queue);
				sender.send(outMessage);
				sender.close();
	
				log.info("XML message ["+billing.getLegalEntity()+"/"+billing.getServiceName()+"] sent successfully by ["+user.getName()+"]");
			} catch (Exception ex) {
				ex.printStackTrace();
				internationalSmsResponse = new InternationalSmsResponse( InternationalSmsResponse.GENERIC_ERROR, "Impossible to send SMS message due to technical problems");
				
				log.error( ex.toString() +" [see full stacktrace in system.err]");
				//throw new XASException("Error in sending SMS!");
			} finally {
				logData.chrono.stop();
				// clean up
				if ( session!=null )
					try	{
						session.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				if ( connection!=null ) {
					try {
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		} 

		/*
		 *  May 2015: #20150511
		 *  - switched logging from securityLogger to slfLogger
		 */
		if(internationalSmsResponse.getCode()==InternationalSmsResponse.OK)	{
			// security log
			//securityLog(logData);  //#20150511 removed Extends SecurityLog
			try {
	            logData(slfLogger, logData);  //#20150511 added Extends GatewayInternationalSms ....  
            } catch (SLF_Exception e) {
    			log.error("SmartLogException during analytic: ",e);
            }
		} else {
			// security log
			//errorLog(logData); //#20150511 removed Extends SecurityLog
			logData.errorMessage = internationalSmsResponse.getDescr();
			logError(slfLogger, logData); //#20150511 added Extends GatewayInternationalSms ....
		}
		return internationalSmsResponse;
	}

	/**
	 * @param sms
	 * @param deliveries
	 * @param billing
	 * @return error response, otherwise null (i.e. no error)
	 */
	private InternationalSmsResponse validateInputData(InternationalSmsMessage sms, SmsDelivery delivery, SmsBillingInfo billing)
	{
		//
		// Phone numbers validation
		//
		//Pattern allDigitPat = Pattern.compile( ALL_DIGITS_RE );		
		
		String phoneNumber = delivery.getPhoneNumber();
		
		// phone number in canonical form
		
		try {
			phoneNumber = PhoneNumber.normalizePhoneNumber(phoneNumber);
		} catch (Exception e) {
			return new InternationalSmsResponse( InternationalSmsResponse.PHONE_NUMBER_FORMAT_ERROR, e.getMessage() );
		}

		// check for international prefix
		boolean hasValidPrefix = false;
		if ( phoneNumber.startsWith(INTERNATIONAL_PREFIX_PLACEHOLDER) || phoneNumber.startsWith(EUROPEAN_INTERNATIONAL_PREFIX) )
			hasValidPrefix = true;
		if ( !hasValidPrefix ) 
			return new InternationalSmsResponse( InternationalSmsResponse.INTERNATIONAL_PREFIX_ERROR, "Phone number must start with international prefix (ex. +xxxxxxxxxx or 00xxxxxxxx)!" );
		// substitue generic prefix ('+') with european international prefix ('00'), if needed 
		phoneNumber = applyInternationalPrefix(phoneNumber, EUROPEAN_INTERNATIONAL_PREFIX);
		
		// phone numbers must contain only digits
		try
		{
			PhoneNumber.validatePhoneNumber(phoneNumber, PhoneNumber.FORMAT_MSISDN);
		}
		catch(XASException e)
		{
			return new InternationalSmsResponse( InternationalSmsResponse.PHONE_NUMBER_FORMAT_ERROR, e.getMessage() );
		}
		
//		if ( allDigitPat.matcher(phoneNumber).matches()==false ) 
//			return new InternationalSmsResponse( InternationalSmsResponse.PHONE_NUMBER_FORMAT_ERROR, "Phone number must contain only digits!" );
		
		// set phone number changes
		delivery.setPhoneNumber( phoneNumber );
		
		//
		// Legal entity 
		//
		if ( billing.getLegalEntity()==null )
			return new InternationalSmsResponse( InternationalSmsResponse.LEGAL_ENTITY_MISSING_ERROR, "Legal entity is mandatory! " );
			
		if ( billing.getLegalEntity().length() > LEGAL_ENTITY_NAME_MAX_SIZE ) 
			return new InternationalSmsResponse( InternationalSmsResponse.LEGAL_ENTITY_NAME_TOO_LONG_ERROR, "Legal entity string cannot exceed "+LEGAL_ENTITY_NAME_MAX_SIZE+" chars!" );
			
		//
		// Service name 
		//
		if ( billing.getServiceName()==null )
			return new InternationalSmsResponse( InternationalSmsResponse.SERVICE_NAME_MISSING_ERROR, "Service name is mandatory! " );
		if ( billing.getServiceName().length()>SERVICE_NAME_MAX_SIZE ) 
			return new InternationalSmsResponse( InternationalSmsResponse.SERVICE_NAME_TOO_LONG_ERROR, "Service string cannot exceed "+SERVICE_NAME_MAX_SIZE+" chars!" );
		
		//
		// Message text length
		//
		if ( sms.getText().length() > MESSAGE_TEXT_MAX_SIZE ) 
			return new InternationalSmsResponse( InternationalSmsResponse.MESSAGE_TEXT_TOO_LONG_ERROR, "Message text cannot exceed "+MESSAGE_TEXT_MAX_SIZE+" chars!" );
		
		return null;	// no error
	}


	/**
	 * Replace '+' prefix with an international call prefix
	 * @param phoneNumber
	 * @param prefix - international cal prefix
	 * @return
	 */
	private String applyInternationalPrefix(String phoneNumber, String prefix)
	{
		if ( phoneNumber.startsWith(INTERNATIONAL_PREFIX_PLACEHOLDER) )
			phoneNumber = prefix + phoneNumber.substring(INTERNATIONAL_PREFIX_PLACEHOLDER.length());
		return phoneNumber;
	}


	/**
	 * Generates XML message.
	 * For XML syntax see 'TRACCIATO RICEVUTO DALLA BANCA SU CODA MQ' chapter
	 * in 'docs/Unicredit_SMSEstero_AnalisiTecnica.pdf'.
	 * 
	 * Example:
	 * 
	 * <body>
	 * <header>
	 *	<recapiti>
	 *		<recapito>
	 *			<tipoRecapito>SMS</tipoRecapito>
	 *			<valoreRecapito>003912345678</valoreRecapito>
	 *			<profiloServizio>bulk</profiloServizio>
	 *		</recapito>
	 *	</recapiti>
	 *	<tipo>f</tipo>
	 *	<codiceTipologia>yyyyyyyy</codiceTipologia>
	 *	<mandatory>true</mandatory>
	 *	<fatturazione>xxxxxxx</fatturazione>
	 * </header>
	 * <footer>
	 *	<messaggio>testo del messaggio</messaggio>
	 * </footer>
     * </body>
	 * 
	 * 
	 * 
	 * @param sms
	 * @param deliveries
	 * @param billing
	 * @return
	 */
	private String buildMessage(InternationalSmsMessage sms, SmsDelivery delivery, SmsBillingInfo billing) throws Exception
	{
		// We need a Document
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		// <body>
		Element body = doc.createElement("body");
		doc.appendChild(body);
		
		// . <header>
		Element header = doc.createElement("header");
		body.appendChild(header);
		
		// . . <recapiti>
		Element recapiti = doc.createElement("recapiti");
		header.appendChild(recapiti);
		
		// list of 
		// . . . <recapito>
		if ( delivery!=null ) {
				Element recapito = doc.createElement("recapito");
				recapiti.appendChild(recapito);
		// . . . . <tipoRecapito>SMS</tipoRecapito>
				Element tipoRecapito = doc.createElement("tipoRecapito");
				recapito.appendChild(tipoRecapito);
				tipoRecapito.appendChild( doc.createTextNode("SMS") );
		// . . . . <valoreRecapito>003912345678</valoreRecapito>
				Element valoreRecapito = doc.createElement("valoreRecapito");
				recapito.appendChild(valoreRecapito);
				valoreRecapito.appendChild( doc.createTextNode(delivery.getPhoneNumber()) );
		// . . . . <tipoRecapito>bulk</profiloServizio>				
				Element profiloServizio = doc.createElement("profiloServizio");
				recapito.appendChild(profiloServizio);
				profiloServizio.appendChild( doc.createTextNode("bulk") );
		}

		// . . <tipo>f</tipo>
		Element tipo = doc.createElement("tipo");
		header.appendChild(tipo);
		tipo.appendChild( doc.createTextNode("f") );
		
		// . . <codiceTipologia>yyyyyyyy</codiceTipologia>
		Element codiceTipologia = doc.createElement("codiceTipologia");
		header.appendChild(codiceTipologia);
		codiceTipologia.appendChild( doc.createTextNode(billing.getServiceName()) );
		
		// . . <mandatory>true</mandatory>
		Element mandatory = doc.createElement("mandatory");
		header.appendChild(mandatory);
		mandatory.appendChild( doc.createTextNode("true") );
		
		// . . <fatturazione>xxxxxxx</fatturazione>
		Element fatturazione = doc.createElement("fatturazione");
		header.appendChild(fatturazione);
		fatturazione.appendChild( doc.createTextNode(billing.getLegalEntity()) );
		
		// . <footer>
		Element footer = doc.createElement("footer");
		body.appendChild(footer);
		
		// . . <messaggio>
		Element messaggio = doc.createElement("messaggio");
		footer.appendChild(messaggio);
		//add a text element to the child
		messaggio.appendChild( doc.createTextNode(sms.getText()) );
	
		//set up a transformer
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
	
		//create string from xml tree
		StringWriter sw = new StringWriter();
		trans.transform(new DOMSource(doc), new StreamResult(sw));
		String xmlString = sw.toString();
		
		return xmlString;
	}

	protected void finalize()
	{
		log.info("Method finalize() invoked!");
	}

}
