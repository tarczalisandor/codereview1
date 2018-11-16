/**
 * Created on Oct 1, 2013
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

import ie.omk.smpp.message.SubmitSMResp;
import ie.omk.smpp.util.AlphabetEncoding;
import ie.omk.smpp.util.DefaultAlphabetEncoding;
import ie.omk.smpp.util.GSMConstants;
import ie.omk.smpp.util.PacketStatus;
import ie.omk.smpp.util.UCS2Encoding;
import it.usi.xframe.xas.bfimpl.SmartLogFactory;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.SMPPUtilities;
import it.usi.xframe.xas.bfimpl.sms.SlfLogger;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.MamAccountRouter;
//JJCONFIG import it.usi.xframe.xas.bfimpl.sms.configuration.ProviderData; 
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfimpl.sms.providers.LogData;
import it.usi.xframe.xas.bfimpl.sms.providers.PhoneMessage;
import it.usi.xframe.xas.bfimpl.sms.providers.PhoneNumber;
import it.usi.xframe.xas.bfimpl.sms.SMPPUtil;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;
import it.usi.xframe.xas.bfutil.XASUnauthorizedUserException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.PatternSyntaxException;

import javax.naming.Context;
import javax.naming.InitialContext;

//JJCONFIG import org.apache.commons.configuration.ConfigurationException;
//JJCONFIG import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.soap.encoding.Hex;
import org.joda.time.DateTime;

import SoapSmppGW.GWService;
import SoapSmppGW.GWServiceServiceLocator;
import SoapSmppGW.GWSession;
import SoapSmppGW.Submit_resp;
import SoapSmppGW.Submit_sm;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import eu.unicredit.xframe.slf.SmartLog;
/* ERRORE  class file has wrong version 49.0, should be 48.0 
 * tirare dentro una versione compilata con java 1.4
*/
import eu.unicredit.xframe.slf.UUID;

/**
 * Gateway Sms Vodafone Pop.
 */
public class GatewaySmsVodafonePop extends GatewaySms
{
	// every single sms may contain 140 byte
	// so using 7 bit ASCII encoding 140byte*8bit=1120bit 1120bit/7bit = 160 chars

	
	private static final String RETURN_CODE = "ret";
	private static final String RETURN_SMSIDS = "smsIds";
	private static Log log = LogFactory.getLog(GatewaySmsVodafonePop.class);
	
	public static final String TYPE = "vodafonepopsoap";
	
//	JJCONFIG private static final String JNDI_SMS_CONFIG = "java:comp/env/url/smsconfig"; // V2
	private static final String JNDI_VODAFONESOAPURL = "java:comp/env/url/VodafonePopSoapUrl";	// address of pop
	private static final String JNDI_VODAFONEACCOUNTS = "java:comp/env/url/VodafoneAccounts";	// provider configuration

	private static boolean localDebug = false;

	// log info
	private String logprefix;
	private String smsloglevel;

	// max size (in byte) for PDU payload when used with GSM transport
	private static final int PDU_PAYLOAD_MAX_SIZE	= 140;			// 254;
	private static final int MAX_ASCII_SMS_CHARS	= 160;			// numero massimo di caratteri per singolo sms ascii
	private static final int MAX_UCS2_SMS_CHARS		= 140;			// numero massimo di caratteri per singolo sms ucs2
	private static final int MAX_MULTIPART_SMS		= 5;			// numero massimo di sms per un multipart
	private static final int MULTIPART_SMS_MAX_SIZE	= PDU_PAYLOAD_MAX_SIZE * MAX_MULTIPART_SMS;
	
	private static final byte GSM338                = 0;			// encoding default GSM 03.38	
	private static final byte UCS2                  = 8;			// encoding UCS2 to support Unicode
	 

	private HashMap mMamAccounts					= null;			//
	private MamAccountRouter mMamAccountRouter		= null;
	private PopAccount mPopAccount					= null;			//
	private HashMap mClients						= null;			//
	//private ClientsContainer newClients             = null;
	private String mDefaultClient                   = null;			// default abi if request sender not valorized	

	private URL mUrlVodafone                        = null;			// end point web service vodafone
	
	private static final String HUNGARY_DESTINATION  = "HungarySC";  //to be used ONLY for sendSms()

/* JJCONFIG
	public static String TAG_CONFIGURATION = "Configuration";
	
	public static String TAG_VERSION = "Version2";
	
	public static String TAG_CACHE_TIMEOUT = "CacheTimeout";
	public static String TAG_JMS_TIME_ZONE = "JmsTimeZone";
	public static String TAG_SMS_GATEWAY = "SmsGateway";
	public static String TAG_DEFAULT_GATEWAY ="Default";
	public static String TAG_BACKUP_GATEWAY = "Backup";
	public static String TAG_USER_GATEWAY = "User";
	public static String ATT_USER_ID = "id";
	public static String TAG_USER_DEFAULT_GATEWAY = "Default";
	public static String TAG_USER_BACKUP_GATEWAY = "Backup";
	public static String TAG_CLIENT_GATEWAY = "Client";
	public static String ATT_CLIENT_ID = "id";
	public static String TAG_CLIENT_DEFAULT_GATEWAY = "Default";
	public static String TAG_CLIENT_BACKUP_GATEWAY = "Backup";

	public static String TAG_GENERAL_ORIGINATORS = "GeneralOriginators";
	public static String TAG_GENERAL_ORIGINATOR = "GeneralOriginator";

	public static String TAG_CLIENTS = "Clients";
	public static String ATT_CLIENTS_DEFAULT_PROVIDER = "defaultProvider";
	public static String ATT_CLIENTS_BACKUP_LIST = "defaultProviderBackupList";
	public static String TAG_CLIENT = "Client";
	public static String ATT_CLIENT_NAME = "name";
	public static String ATT_CLIENT_NORMALIZEPHONEMESSAGE = "normalizePhoneMessage";
	public static String ATT_CLIENT_VALIDATEPHONENUMBER = "validatePhoneNumber";
	public static String ATT_CLIENT_VALIDATEPHONENUMBERREGEX = "validatePhoneNumberRegEx";
	public static String ATT_CLIENT_USEBLACKLIST = "useBlackList";
	public static String ATT_CLIENT_ORIGINATOR = "originator";
	public static String ATT_CLIENT_ALIAS = "alias";
	public static String ATT_CLIENT_DESCRIPTION  = "description";
	public static String ATT_CLIENT_DEFAULT_VALIDITY  = "defaultValidity";
	public static String ATT_CLIENT_PROVIDER  = "provider";
	public static String ATT_CLIENT_BACKUP_LIST  = "providerBackupList";
	

	public static String TAG_PROVIDERS = "Providers";
	public static String TAG_PROVIDER = "Provider";
	public static String ATT_PROVIDER_NAME = "name";
	public static String ATT_PROVIDER_TYPE = "type";
	public static String ATT_PROVIDER_DESCRIPTION = "description";
	public static String ATT_PROVIDER_LOGPREFIX = "logprefix";
	public static String ATT_PROVIDER_SMSLOGLEVEL = "smsloglevel";
*/

	// smppReferenceNumber used to build multi part sms
	private static byte smppReferenceNumber;
	static {
		Random rnd = new Random();
		smppReferenceNumber = (byte)rnd.nextInt(256);
	}

	/**
	 * @param logprefix
	 * @param smsloglevel
	 * @param webservice
	 * @throws Exception
	 */
	public GatewaySmsVodafonePop(String logprefix, String smsloglevel) throws Exception
	{
		this.logprefix = logprefix;
		this.smsloglevel = smsloglevel;
		this.mUrlVodafone = getVodafonePopUrl();
		loadConfigFile();
	}

	public GatewaySmsVodafonePop(String logprefix, String smsloglevel, ConfigurationReader configReader) throws XASException
	{
		this.logprefix = logprefix;
		this.smsloglevel = smsloglevel;
		try {
			this.mUrlVodafone = getVodafonePopUrl();
		} catch(Exception e) {
			throw new XASException("Error trying to get VodafonePop URL",e);
		}
		loadNewConfiguration(configReader);
	}

/* JJCONFIG
	public GatewaySmsVodafonePop(String logprefix, String smsloglevel, ConfigurationReaderV2 configReader) throws XASException {
		this.logprefix = logprefix;
		this.smsloglevel = smsloglevel;
		try {
			this.mUrlVodafone = getVodafonePopUrl();
		} catch(Exception e) {
			throw new XASException("Error trying to get VodafonePop URL",e);
		}
	}
*/

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfutil.data.IGatewaySms#sendSms(it.usi.xframe.xas.bfutil.data.SmsMessage, it.usi.xframe.xas.bfutil.data.SmsSenderInfo, java.lang.String)
	 * @deprecated use sendSms(SmsMessage, SmsSenderInfo, String, Boolean) instead
	 */
	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException {
		log.warn("Deprecated sendSms() method used");
		sendSms(sms, sender, userid, null);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfutil.data.IGatewaySms#sendSms(it.usi.xframe.xas.bfutil.data.SmsMessage, it.usi.xframe.xas.bfutil.data.SmsSenderInfo, java.lang.String, java.lang.Boolean)
	 */
	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid, Boolean isInRole) throws XASException
	{
		LogData logData = new LogData();
		logData.channel = Constants.CHANNEL_WS;
		logData.version = "1";
		SlfLogger slfLogger = new SlfLogger();
		logData.chrono.start();
		logData.userid = userid;
		logData.isUserInRole = isInRole;
		logData.logPrefix = this.logprefix;
		logData.logLevel = this.smsloglevel;
		logData.gatewayContacted = false;
		
		try
		{
			// validate parameters
			if(userid==null)
				throw new VodafoneException("userid is null");

			if(sms==null)
				throw new VodafoneException("sms is null");

			if(sms.getMsg()==null || sms.getMsg().length()==0)
				throw new VodafoneException("no text in sms message");

			if(sms.getPhoneNumber()==null || sms.getPhoneNumber().length()==0)
				throw new VodafoneException("no destination phone number");

			// update logData
			logData.originalMsg = sms.getMsg();
			logData.originalMsgLength = sms.getMsg().length();
			logData.originalDestinationNumber = sms.getPhoneNumber();

			// if sender is null uses default
			if(sender==null)
				sender = new SmsSenderInfo();
			else
			{
				// update logData
				logData.originalAbiCode = sender.getABICode();
				logData.originalAlias = sender.getAlias();
			}

			// trim spaces from ABI
	    	if(sender.getABICode()!=null)
	    		sender.setABICode(sender.getABICode().trim());
	    	
	    	// cut the srcABI to 8 (ABI+APP.CODE) chars to map accounts in configuration file
	    	String usedAbi = sender.getABICode();
	    	if(usedAbi!=null && usedAbi.length()>8)
	    		usedAbi = usedAbi.substring(0,8);

	    	// if abi not set, use default
	    	if(usedAbi==null || usedAbi.length()==0)
	    	{
	    		if(mDefaultClient==null || mDefaultClient.length()==0)
	    			throw new VodafoneException("You must specify an abi code in the sender or valorize a default client in xas config");
	    		usedAbi= mDefaultClient;
				log.warn("ABI code not specified, use dafault ABI=" + mDefaultClient);
	    	}

	    	// get the right client from configuration
	    	Client client = (Client) mClients.get(usedAbi);
	    	if(client==null)
	    	{
	    		// probably abi don't match any configuration, use the default client
	    		if(mDefaultClient==null || mDefaultClient.length()==0)
	    			throw new VodafoneException("client not defined for abi code=" + usedAbi + " and no default client specified" );

	    		if(!usedAbi.equals(mDefaultClient))
	    			log.warn("ABI=" + usedAbi + " code not defined, use default ABI=" + mDefaultClient);

	    		usedAbi = mDefaultClient;

	    		client = (Client) mClients.get(usedAbi);
		    	if(client==null)
					throw new VodafoneException("client not defined for default abi code=" + usedAbi);
	    	}

	    	// update logData
	    	logData.usedAbiCode = usedAbi;

	    	// load a valid client configuration, follows alias if necessary
	    	ArrayList stack = new ArrayList();
	    	while(client.getAlias()!=null && client.getAlias().length()!=0)
	    	{
	    		// avoid circular reference into alias
	    		if(stack.contains(client.getAlias()))
					throw new VodafoneException("alias are referenced in a circular way, abi code=" + usedAbi + ", alias=" + client.getAlias());

				// add an element to the stack
				stack.add(client.getAlias());

				Client aliasClient = (Client) mClients.get(client.getAlias());
		    	if(aliasClient==null)
					throw new VodafoneException("alias client not defined for abi code=" + usedAbi);

		    	usedAbi = aliasClient.getName();

		    	// update log data
		    	logData.usedAbiCode = usedAbi;

		    	// take the attribute from alias client
				client = aliasClient.clone(client.getName());
	    	}

	    	// normalize phoneNumber
	    	sms.setPhoneNumber(PhoneNumber.normalizePhoneNumber(sms.getPhoneNumber()));

 	    	// do some validation specific for that client
			if(client.isValidatePhoneNumber())
				PhoneNumber.validatePhoneNumber(sms.getPhoneNumber(), client.getValidatePhoneNumberRegEx());
			
			// normalize input params (phone number, etc.)
	    	normalizeInputInfo(sms);

			logData.defaultValidityPeriod = client.getValidityPeriod() != null ? client.getValidityPeriod().toString() : "";	// #369
			InternalSmsRequest request = new InternalSmsRequest(new SmsRequest()); 		// #369 Used only for validityPeriod
			DateTime validity = request.validateValidity(client.getValidityPeriod()); 	// #369
			
	    	String prefix = client.getPrefix();	    	// #370
	    	String prefixRE = client.getPrefixRE();		// #370
	    	String phoneNumber = sms.getPhoneNumber();  // #370
			if (prefix!= null && prefix.length() != 00 && phoneNumber != null && prefixRE != null && prefixRE.length() != 0) {
		    	try {
		    		if (phoneNumber.matches(prefixRE)) {
						sms.setPhoneNumber(prefix + phoneNumber);
		    		}
		    	} catch (PatternSyntaxException ex)	{
					throw new XASException("PrefixParseException: Syntax error in the regular expression " + ex.getMessage());
		    	}
			} // #370
			
	    	
	    	// get a valid account from the pool to load the originator number
	    	String systemId = client.getRandomAccount();
			MamAccount account = (MamAccount) mMamAccounts.get(systemId);
			MamAccount routedAccount = mMamAccountRouter != null ? mMamAccountRouter.search(sms.getPhoneNumber()) : null;
			if (routedAccount != null) account = routedAccount;
			if (account==null)
				throw new VodafoneException("No account satisfies your request for [" + systemId + "] !");

	    	// sender ask for an alphanumeric alias to be used ?
//			boolean useSenderAlias = (sender!=null && sender.getAlias()!=null) ? true : false;

			// Come scoprire quale encoding utilizzare ?
			// faccio un encoding del messaggio con il default encoding e un decoding.
			// Se il risultato è diverso allora passo all'encoding UCS2.
			AlphabetEncoding encoding = new DefaultAlphabetEncoding();	// default GSM 03.38 encoding
			byte[] msgBytes = encoding.encodeString(sms.getMsg());
			String self = encoding.decodeString(msgBytes);
			boolean useDefaultEncoding = (sms.getMsg().equals(self)) ? true : false;
			if (useDefaultEncoding==true)
			{
		    	// if GSM3.38, normalize message replacing some characters
				{
					sms.setMsg(PhoneMessage.normalizePhoneMessage(sms.getMsg()));
					msgBytes = encoding.encodeString(sms.getMsg());
				}
				log.debug("Use encoding (GSM 03.38)");
			}
			else
			{
				encoding = new UCS2Encoding();	// Unicode
				msgBytes = encoding.encodeString(sms.getMsg());
				log.debug("Use unicode encoding (UCS2)");
			}

			// update log data
			logData.smsEncoding = getDataCodingDescription(encoding);

			// verify that message length is contained into MULTIPART_SMS_MAX_SIZE
			if( msgBytes.length >= MULTIPART_SMS_MAX_SIZE )
			{
				int size = MULTIPART_SMS_MAX_SIZE;
				if(getDataCoding(encoding).byteValue()==UCS2)
					size /= 2; 
				throw new VodafoneException("The message exceeds the max length of " + size + " characters.");
			}

			// for ascii sms the max chars content is 160 chars (7bit compression),
			// otherwise is 140
			int maxSmsCharsLength = PDU_PAYLOAD_MAX_SIZE;
			if(encoding instanceof DefaultAlphabetEncoding)
			{
				maxSmsCharsLength = MAX_ASCII_SMS_CHARS;
			}
			else if(encoding instanceof UCS2Encoding)
			{
				maxSmsCharsLength = MAX_UCS2_SMS_CHARS;
			}

			// validate alias
			PhoneNumber.validateAlias(account.getAlias(), "");

	    	// update log data
	    	logData.normalizedDestinationNumber = sms.getPhoneNumber();
			String ret = null;
			// choose to use multi part or single message sms
			if ( msgBytes.length > maxSmsCharsLength )
			{
				// long msg to be splitted into concatenated msg
				ret = sendMultipleMessage(sms, sender, userid, usedAbi, account, msgBytes, validity, encoding, logData, slfLogger);  // #369
			}
			else
			{
				// msg can fit into a single sms
				ret = sendSingleMessage(sms, sender, userid, usedAbi, account, msgBytes, validity, encoding, logData, slfLogger); // #369
			}
			log.debug("Submitted message with status: " + ret);			
		}
		/*
		 *  April 2015:
		 *  - removed printStackTrace() and logging the exception
		 *  - switched logging from securityLogger to slfLogger
		 */
		catch(VodafoneException e)
		{
			logData.chrono.stop();
			// e.printStackTrace();
			// log.error(e);
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");
			//securityLogger.errorLog(logData);
			logError(slfLogger, logData);
			throw new XASException(e);
		}
		catch(Exception e)
		{
			logData.chrono.stop();
			// e.printStackTrace();
			// log.error(e);
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");			
			//securityLogger.errorLog(logData);
			logError(slfLogger, logData);
			throw new XASException(e);
		}
	}

   /**
	 * Send a sms composed by a single message
     *
     * @param sms
     * @param sender
     * @param userid
     * @param usedABI
     * @param account
     * @param msg
     * @param encoding
     * @param logData
     * @return
     * @throws VodafoneException
     */
    private String sendSingleMessage(SmsMessage sms, SmsSenderInfo sender, String userid, String usedABI, MamAccount account,
    	byte msg[], DateTime validity, AlphabetEncoding encoding, LogData logData, SlfLogger slfLogger) throws VodafoneException // #369
    {
    	String ret = null;
    	try
    	{
			GWServiceServiceLocator locator = new GWServiceServiceLocator();
			GWService service = locator.getGWService(mUrlVodafone);
			if(service == null)
				throw new VodafoneException("fail to instantiate SMS VodafonePop proxy");
	
			// instantiate the service
			Submit_sm sm = new Submit_sm();

			// set authentication account
			GWSession gws = new GWSession();
			gws.setAccountName(mPopAccount.getUser());									// user Pop
			gws.setAccountPassword(mPopAccount.getPassword());							// password Pop

			// service type
//			sm.setService_type(null);													// null = default value

			sm.setSource_addr_npi(IntToByte(account.getNumPlanId()));
			sm.setSource_addr_ton(IntToByte(account.getTypeOfNumber()));						// GSM_TON_SUBSCRIBER
			sm.setSource_addr(account.getOriginator());
			// update log data
			logData.numPlanId = account.getNumPlanId();
			logData.typeOfNumber = account.getTypeOfNumber();
			logData.originator = account.getOriginator();

			sm.setDest_addr_ton(IntToByte(GSMConstants.GSM_TON_INTERNATIONAL));			// costante - tipo destinatario
			sm.setDest_addr_npi(IntToByte(GSMConstants.GSM_NPI_E164));					// costante - tipo destinatario
			sm.setDestination_addr(sms.getPhoneNumber());								// numero telefono destinatario
//			sm.setEsm_class(IntToByte(0x40));											// set UDHI flag to true (enable concatenated messages)
//			sm.setProtocol_id(null);													// ? null
//			sm.setPriority_flag(null);													// ? null
			if (validity != null) {
				String validityString = SMPPUtilities.getInstance().convertDate(validity);
				sm.setValidity_period(validityString);
				logData.validity = validityString;
			}
//			sm.setValidity_period(null);												// ? null
//			sm.setSchedule_delivery_time(null);											// ? delivery message per le risposte
			sm.setRegistered_delivery(IntToByte(1));									// set 1 = Delivery Report 0 = No delivery Report
//			sm.setReplace_if_present_flag(null);										// ?
			sm.setData_coding(getDataCoding(encoding));									// codifica messaggio (ascii 7bit) o (UTF-8)
//			sm.setSm_default_msg_id(null);												// 
//			sm.setSm_length(null);														// lunghezza corpo messaggio

			String msgToSend = soapEncodeSmMessage(msg, encoding);						// the sms message
			if(encoding instanceof DefaultAlphabetEncoding)
			{
				sm.setShort_message(msgToSend);											// the sms message in GSM 3.38 encoding
			}
			else if(encoding instanceof UCS2Encoding)
			{
				sm.setMessage_payload(msgToSend);										// the sms message in UCS2 encoding
			}

			// update logData
			logData.messagePart = "1/1";
			logData.messagePartLength = msg.length + "/" + msg.length;
			logData.messageLength = sms.getMsg().length();
			logData.gatewayContacted = true;

			// ---------------------------------------------
			// send sms
			// ---------------------------------------------
			Submit_resp resp = service.submit(sm, gws);
			if(resp==null)
			{
				throw new VodafoneException("Null response object returned by vodafone service");    			
			}

			logData.chrono.stop();
			
			ret = resp.getCommand_status().toString();

			String status = SMPPUtil.getSMPPErrorDescription(resp.getCommand_status().intValue());

			// update logData
			logData.smsId = resp.getMessage_id();
			logData.errorMessage = status;

			if(resp.getCommand_status().intValue() != PacketStatus.OK)
			{
				/* April 2015 this log was added to LogData to get a single log row
				log.info("Provider=\"VodafonePop\", message_id=\"" + resp.getMessage_id() + "\""
				+ ", error_code=\"" + resp.getError_code() + "\""
				+ ", command_status=\"" + resp.getCommand_status() + "\""
				+ ", sequence_number=\"" + resp.getSequence_number() + "\""); */
				logResponseData(logData,resp);

				throw new VodafoneException(status);
			}

			// write security log - April 2015 switched to slfLogger
			//securityLogger.securityLog(logData);
			logData(slfLogger, logData);
    	}
		catch(Exception e)
		{
			throw new VodafoneException(e.getMessage()); 
		}
		return ret; 
    }

    /**
	 * Send a sms composed by multiple messages
	 *
     * @param sms
     * @param sender
     * @param userid
     * @param usedABI
     * @param account
     * @param msg
     * @param encoding
     * @param logData
     * @return
     * @throws VodafoneException
     */
    private String sendMultipleMessage(SmsMessage sms, SmsSenderInfo sender, String userid, String usedABI, MamAccount account,
    		byte msg[], DateTime validity, AlphabetEncoding encoding, LogData logData, SlfLogger slfLogger) throws VodafoneException { // #369
    	
    	String ret = null;

     	final byte UDH_LEN = 0x05; 				// UDH length (5 bytes)
    	final byte NUMBERING_8BITS = 0x00;		// 8bit numbering for concatenated message
    	final byte INFO_LEN = 0x03;				// info length (3 more bytes)
    	
    	final int REFERENCE_NUMBER_IDX = 3;
    	final int TOT_PARTS_IDX = 4;
    	final int PART_NUM_IDX = 5;

    	byte[] udh_template = {
    			UDH_LEN,						// user data header
    			NUMBERING_8BITS,	
    			INFO_LEN,
				0x00,							// reference number for this concatenated message
				0x00,							// total parts number 
				0x00							// current part number 
		};

    	try {
			GWServiceServiceLocator locator = new GWServiceServiceLocator();
			GWService service = locator.getGWService(mUrlVodafone);
			if(service == null) {
				throw new VodafoneException("fail to instantiate SMS VodafonePop proxy");
			}
	
    		byte reference_number = nextReferenceNumber();

    		// instantiate the service
			Submit_sm sm = new Submit_sm();

			// set authentication account
			GWSession gws = new GWSession();
			gws.setAccountName(mPopAccount.getUser());									// user Pop
			gws.setAccountPassword(mPopAccount.getPassword());							// password Pop

			// service type
//			sm.setService_type(null);													// null = default value
 			sm.setSource_addr_npi(IntToByte(account.getNumPlanId()));
			sm.setSource_addr_ton(IntToByte(account.getTypeOfNumber()));			// #380 costante - tipo mittente
			sm.setSource_addr(account.getOriginator());									// numero telefono mittente 
			// update log data
			logData.numPlanId = account.getNumPlanId();
			logData.typeOfNumber = account.getTypeOfNumber();
			logData.originator = account.getOriginator();

			sm.setDest_addr_ton(IntToByte(GSMConstants.GSM_TON_INTERNATIONAL));			// costante - tipo destinatario 
			sm.setDest_addr_npi(IntToByte(GSMConstants.GSM_NPI_E164));					// costante - tipo destinatario
			sm.setDestination_addr(sms.getPhoneNumber());								// numero telefono destinatario

			sm.setEsm_class(IntToByte(0x40));											// set UDHI flag to true (enable concatenated messages)
//			sm.setProtocol_id(null);													// ? null
//			sm.setPriority_flag(null);													// ? null
			if (validity != null) {
				String validityString = SMPPUtilities.getInstance().convertDate(validity);
				sm.setValidity_period(validityString);
				logData.validity = validityString;
			}
//			sm.setValidity_period(null);												// ? null
//			sm.setSchedule_delivery_time(null);											// ? delivery message per le risposte
			sm.setRegistered_delivery(IntToByte(1));									// set 1 = Delivery Report 0 = No delivery Report
//			sm.setShort_message(null);													// ?
//			sm.setReplace_if_present_flag(null);										// ?
			sm.setData_coding(getDataCoding(encoding));									// codifica messaggio (ascii 7bit) o (UTF-8)
//			sm.setData_coding(IntToByte(0x40));
//			sm.setSm_default_msg_id(null);												// 
//			sm.setSm_length(null);														// lunghezza corpo messaggio
//			sm.setMessage_payload(null);												// messaggio

	    	// split msg in parts and send them
	    	int maxBytesPerPart = PDU_PAYLOAD_MAX_SIZE - udh_template.length;			// cut the header length from message length
	    	int totParts = (int) Math.ceil( (1.0*msg.length) / maxBytesPerPart );		// max length of each message part
	    	log.debug("Sending message as concatenated message (" + totParts + " parts)");
	    	int count=0;
	    	int partNumb = 1;
	    	
	    	while (count < msg.length) {
	    		int n = Math.min(maxBytesPerPart, msg.length-count);
	    		log.debug("Part payload: "+n);
	    		byte[] part = new byte[n];
	    		System.arraycopy(msg, count, part, 0, n);
	    		
	    		byte[] udh = udh_template;
	    		udh[REFERENCE_NUMBER_IDX] = reference_number;							// not relevant since only 1 concatenated msg is sent
	    		udh[TOT_PARTS_IDX] = (byte) totParts;
	    		udh[PART_NUM_IDX] = (byte) partNumb;
	    		log.debug("UDH: " + Hex.encode(udh));
	    		
	    		byte[] partPayload = concatBytes(udh, part);
	    		String msgHeader = Hex.encode(udh); 									// la testa va rappresentata in esadecimale
	    		String msgBody = Hex.encode(part);

//				if(encoding instanceof DefaultAlphabetEncoding)
//				{
//				}
//				else if(encoding instanceof UCS2Encoding)
//				{
//				}

	    		String msgToSend = msgHeader + msgBody;
				sm.setMessage_payload(msgToSend);										// the sms message part

	    		//sm.setMessage( partPayload, encoding );

				// update logData
				logData.messagePart = partNumb + "/" + totParts;
				logData.messagePartLength = n + "/" + msg.length;
				logData.messageLength = sms.getMsg().length();
				logData.gatewayContacted = true;

				// send single part
	    		log.debug("Sending part " + partNumb + "/" + totParts + "...");

				// send sms
				Submit_resp resp = service.submit(sm, gws);
				if(resp==null) {
	    			throw new VodafoneException("Null response object returned by vodafone");
				}
				logData.chrono.stop();

				ret = resp.getCommand_status().toString();

				String status = SMPPUtil.getSMPPErrorDescription(resp.getCommand_status().intValue());

				// update logData
				logData.smsId = resp.getMessage_id();
				logData.errorMessage = status;

				if ( resp.getCommand_status().intValue() != SubmitSMResp.ESME_ROK ) {
					/* April 2015 this log was added to LogData to get a single log row
					log.info("Provider=\"VodafonePop\", message_id=\"" + resp.getMessage_id() + "\""
							+ ", error_code=\"" + resp.getError_code() + "\""
							+ ", command_status=\"" + resp.getCommand_status() + "\""
							+ ", sequence_number=\"" + resp.getSequence_number() + "\"");*/
					logResponseData(logData,resp);
					throw new VodafoneException(status);
	    		}

				// write security log - April 2015 switched to slfLogger
				//securityLogger.securityLog(logData);
				logData(slfLogger, logData);

	    		++partNumb;
	    		count += n;
				logData.chrono.start();
	    	}
		} catch(Exception e) {
			throw new VodafoneException(e.getMessage()); 
		}
		return ret;     	
    }

	/**
	 * Method sendSms version 2.
	 * 
	 * Reference {@link "\\\\usnas000.intranet.unicredit.it\\usictcp$\\Progetti\\xas-2015-04-01 - (ictcp) (xas) - Invio sms batch"}
	 *  
	 */
	public SmsResponse sendSms2(InternalSmsRequest request, it.usi.xframe.xas.bfimpl.sms.configuration.Client client, String userid, boolean isUserInRole) throws XASWrongRequestException, XASConfigurationException {
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", (String) MDC.get(Constants.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG).logReferrer(0);
		if (log.isDebugEnabled()) { 
			String params = "";
			/* Use XStream for logging params as JSON */
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
			         } 
			     });
			params = xstream.toXML(request);
			/* ERRORE  class file has wrong version 49.0, should be 48.0
			params = request.getCorrelationID() + "," + request.getMsg() + "," + request.getPhoneNumber() + "," + request.getXasUser();
			*/
			log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, params, "a_userid", userid).getLogRow(true));
		}
		SmsResponse smsResponse = new SmsResponse();
		
		LogData logData = new LogData();
		logData.channel = request.getChannel();
		logData.version = "2";
		SlfLogger slfLogger = new SlfLogger();
		logData.chrono.start();
		logData.userid = userid;
		logData.isUserInRole = new Boolean(isUserInRole);
		logData.logPrefix = this.logprefix;
		logData.logLevel = this.smsloglevel;
		logData.gatewayContacted = false;
		
		
		try {
			// validate parameters
			if (! isUserInRole) {
				throw new XASUnauthorizedUserException(userid);
			}
			if (userid == null) {
				throw new XASWrongRequestException("userid is null");
			}

			if (request == null) {
				throw new XASWrongRequestException("request object is null");
			}
			
			if (request.getMsg() == null || request.getMsg().length() == 0) {
				throw new XASWrongRequestException("no text in sms message");
			}

			if (request.getPhoneNumber() == null || request.getPhoneNumber().length() == 0) {
				throw new XASWrongRequestException("no destination phone number");
			}
			
			if (request.getXasUser() == null || request.getXasUser().length() == 0) {
				throw new XASWrongRequestException("You must specify a XAS user code");
			}
			
			DateTime validity = request.validateValidity(client.getDefaultValidity());
			request.validateDelivery(validity);

			// update logData
			logData.originalMsg = request.getMsg();
			logData.originalMsgLength = request.getMsg().length();
			logData.originalDestinationNumber = request.getPhoneNumber();
			logData.originalAbiCode = request.getXasUser();
			logData.srcValidity = request.getValidity() != null ? LogData.LOG_DATE_FORMATTER.format(request.getValidity()) : "";
			logData.srcValidityPeriod = request.getValidityPeriod() != null ? request.getValidityPeriod().toString() : "";
			logData.defaultValidityPeriod = client.getDefaultValidity() != null ? client.getDefaultValidity().toString() : "";
			logData.srcDelivery = request.getDeliveryTime() != null ? LogData.LOG_DATE_FORMATTER.format(request.getDeliveryTime()) : "";

			//String usedAbi = request.getNormalizedXasUser();
	    	
	    	String usedAbi = client.getName();
	    	logData.usedAbiCode = usedAbi;

	    	// Check if use black list.
			//if (client.isUseBlackList()) {
				log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_CALL, SmartLog.K_PARAMS, "Blacklist checked").getLogRow(true));
				//PhoneNumber.checkBlackList(request.getPhoneNumber());
			//}

	    	normalizeInputInfo(request);

	    	String systemId = client.getOriginator();
			MamAccount account = (MamAccount) mMamAccounts.get(systemId);
			MamAccount routedAccount = mMamAccountRouter != null ? mMamAccountRouter.search(request.getPhoneNumber()) : null;
			if (routedAccount != null) account = routedAccount;
			if (account==null) {
				throw new XASConfigurationException("No account satisfies your request for originator [" + systemId + "] !");
			}


			// Come scoprire quale encoding utilizzare ?
			// faccio un encoding del messaggio con il default encoding e un decoding.
			// Se il risultato è diverso allora passo all'encoding UCS2.
			AlphabetEncoding encoding = new DefaultAlphabetEncoding();	// default GSM 03.38 encoding
			byte[] msgBytes = encoding.encodeString(request.getMsg());
			String self = encoding.decodeString(msgBytes);
			if (request.getMsg().equals(self)) { // useDefaultEncoding
		    	// if GSM3.38, normalize message replacing some characters
				if(client.isNormalizePhoneMessage()) {
					request.setMsg(PhoneMessage.normalizePhoneMessage(request.getMsg()));
					msgBytes = encoding.encodeString(request.getMsg());
				}
				log.debug("Use encoding (GSM 03.38)");
			} else { // Use UCS2 for Unicode
				encoding = new UCS2Encoding();	
				msgBytes = encoding.encodeString(request.getMsg());
				log.debug("Use unicode encoding (UCS2)");
			}

			// update log data
			logData.smsEncoding = getDataCodingDescription(encoding);

			// verify that message length is contained into MULTIPART_SMS_MAX_SIZE
			if( msgBytes.length >= MULTIPART_SMS_MAX_SIZE ) {
				int size = MULTIPART_SMS_MAX_SIZE;
				if(getDataCoding(encoding).byteValue() == UCS2) {
					size /= 2; 
				}
				throw new XASWrongRequestException("The message exceeds the max length of " + size + " characters.");
			}

			// for ascii sms the max chars content is 160 chars (7bit compression),
			// otherwise is 140
			int maxSmsCharsLength = PDU_PAYLOAD_MAX_SIZE;
			if(encoding instanceof DefaultAlphabetEncoding) {
				maxSmsCharsLength = MAX_ASCII_SMS_CHARS;
			} else if(encoding instanceof UCS2Encoding)	{
				maxSmsCharsLength = MAX_UCS2_SMS_CHARS;
			}

			Map retMap = new HashMap();
			String ret = null;
			// choose to use multi part or single message sms
	    	logData.normalizedDestinationNumber = request.getPhoneNumber();
			if ( msgBytes.length > maxSmsCharsLength ) {
				// long msg to be splitted into concatenated msg
				retMap = sendSms2MultipleMessage(request, userid, usedAbi, account, msgBytes, validity, encoding, logData, slfLogger);
				ret = (String) retMap.get(RETURN_CODE);
				smsResponse.setSmsIds((String []) retMap.get(RETURN_SMSIDS));
			} else {
				// msg can fit into a single sms
				retMap = sendSms2SingleMessage(request, userid, usedAbi, account, msgBytes, validity, encoding, logData, slfLogger);
				ret = (String) retMap.get(RETURN_CODE);
				smsResponse.setSmsIds((String []) retMap.get(RETURN_SMSIDS));
			}
			log.debug("Submitted message with status: " + ret);			
		} catch(VodafoneRuntimeException e) { 
			logData.chrono.stop();
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");			
			logError(slfLogger, logData);
			throw new XASRuntimeException(e);
		} catch(UnsupportedEncodingException e) { 
			logData.chrono.stop();
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");
			logError(slfLogger, logData);
			throw new XASRuntimeException(e);
		} catch(XASUnauthorizedUserException e) {
			logData.chrono.stop();
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");
			logError(slfLogger, logData);
			smsResponse = new SmsResponse();
			smsResponse.setCode(Constants.XAS00004E_CODE);
			smsResponse.setMessage(Constants.XAS00004E_MESSAGE, new String[] {userid});
			return smsResponse; 
		} catch(XASWrongRequestException e) { 
			logData.chrono.stop();
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");
			logError(slfLogger, logData);
			throw e;
		} catch(XASRuntimeException e) { 
			logData.chrono.stop();
			StackTraceElement[] ste = e.getStackTrace();
			logData.errorMessage = e.getMessage();
			logData.errorAt = ((ste.length > 0) ? ste[0].toString() : "");
			logError(slfLogger, logData);
			throw e;
		} finally {
			if (log.isDebugEnabled()) log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN,SmartLog.K_RETURN_VALUE, logData.errorMessage).getLogRow(true));
		}
		smsResponse.setCode(Constants.XAS00000I_CODE);
		smsResponse.setMessage(Constants.XAS00000I_MESSAGE);

		return smsResponse;
	} 
	
 	
	/**
	 * Send a sms composed by a single message version 2.
     *
     * @param request
     * @param userid
     * @param usedABI
     * @param account
     * @param msg
     * @param validity
     * @param encoding
     * @param logData
     * @param slfLogger
     * @return
     * @throws VodafoneException
     */
    private Map sendSms2SingleMessage(InternalSmsRequest request, String userid, String usedABI, MamAccount account,	byte msg[], DateTime validity, AlphabetEncoding encoding,
    	LogData logData, SlfLogger slfLogger) {

    	Map retMap = new HashMap();
    	ArrayList smsIds = new ArrayList();
    	String ret = null;

    	try {
			GWServiceServiceLocator locator = new GWServiceServiceLocator();
			GWService service = locator.getGWService(mUrlVodafone);
			if(service == null) {
				throw new VodafoneRuntimeException("fail to instantiate SMS VodafonePop proxy");
			}
	
			// instantiate the service
			Submit_sm sm = new Submit_sm();

			// set authentication account
			GWSession gws = new GWSession();
			gws.setAccountName(mPopAccount.getUser());									// user Pop
			gws.setAccountPassword(mPopAccount.getPassword());							// password Pop

			// service type
			// sm.setService_type(null);													// null = default value

 			sm.setSource_addr_npi(IntToByte(account.getNumPlanId()));
			sm.setSource_addr_ton(IntToByte(account.getTypeOfNumber()));	// #380 costante - tipo mittente
			sm.setSource_addr(account.getOriginator());									// numero telefono mittente
			// update log data
			logData.numPlanId = account.getNumPlanId();
			logData.typeOfNumber = account.getTypeOfNumber();
			logData.originator = account.getOriginator();

			sm.setDest_addr_ton(IntToByte(GSMConstants.GSM_TON_INTERNATIONAL));			// costante - tipo destinatario 
			sm.setDest_addr_npi(IntToByte(GSMConstants.GSM_NPI_E164));					// costante - tipo destinatario
			sm.setDestination_addr(request.getPhoneNumber());							// numero telefono destinatario
//			sm.setEsm_class(IntToByte(0x40));											// set UDHI flag to true (enable concatenated messages)
//			sm.setProtocol_id(null);													// ? null
//			sm.setPriority_flag(null);													// ? null
			if (validity != null) {
				String validityString = SMPPUtilities.getInstance().convertDate(validity);
				sm.setValidity_period(validityString);
				logData.validity = validityString;
			}
			if (request.getDeliveryTime() != null) {
				String delivery = SMPPUtilities.getInstance().convertDate(request.getDeliveryTime());
				sm.setSchedule_delivery_time(delivery);
				logData.delivery = delivery;
			}
//			sm.setSchedule_delivery_time(null);											// 
			sm.setRegistered_delivery(IntToByte(1));									// set 1 = Delivery Report 0 = No delivery Report
//			sm.setReplace_if_present_flag(null);										// ?
			sm.setData_coding(getDataCoding(encoding));									// codifica messaggio (ascii 7bit) o (UTF-8)
//			sm.setSm_default_msg_id(null);												// 
//			sm.setSm_length(null);														// lunghezza corpo messaggio

			String msgToSend = soapEncodeSmMessage(msg, encoding);						// the sms message
			if(encoding instanceof DefaultAlphabetEncoding) {
				sm.setShort_message(msgToSend);											// the sms message in GSM 3.38 encoding
			} else if(encoding instanceof UCS2Encoding) {
				sm.setMessage_payload(msgToSend);										// the sms message in UCS2 encoding
			}

			// update logData
			logData.messagePart = "1/1";
			logData.messagePartLength = msg.length + "/" + msg.length;
			logData.messageLength = request.getMsg().length();
			logData.gatewayContacted = true;

			// ---------------------------------------------
			// send sms
			// ---------------------------------------------
			Submit_resp resp = service.submit(sm, gws);
			if(resp == null) {
				throw new VodafoneRuntimeException("Null response object returned by vodafone service");    			
			}

			logData.chrono.stop();
			
			ret = resp.getCommand_status().toString();
			retMap.put(RETURN_CODE, ret);

			String status = SMPPUtil.getSMPPErrorDescription(resp.getCommand_status().intValue());

			// update logData
			logData.smsId = resp.getMessage_id();
			smsIds.add(logData.smsId);
			logData.errorMessage = status;

			if(resp.getCommand_status().intValue() != PacketStatus.OK) {
				/* April 2015 this log was added to LogData to get a single log row
				log.info("Provider=\"VodafonePop\", message_id=\"" + resp.getMessage_id() + "\""
				+ ", error_code=\"" + resp.getError_code() + "\""
				+ ", command_status=\"" + resp.getCommand_status() + "\""
				+ ", sequence_number=\"" + resp.getSequence_number() + "\""); */
				logResponseData(logData,resp);

				throw new VodafoneRuntimeException(status);
			}

			// write security log - April 2015 switched to slfLogger
			//securityLogger.securityLog(logData);
			logData(slfLogger, logData);
    	} catch(Exception e) {
			throw new VodafoneRuntimeException(e.getMessage(),e); 
		}
		retMap.put(RETURN_SMSIDS, (String []) smsIds.toArray(new String[1]));
		return retMap; 
    }

    /**
	 * Send a sms composed by multiple messages version 2.
	 *
     * @param request
     * @param userid
     * @param usedABI
     * @param account
     * @param msg
     * @param validity
     * @param encoding
     * @param logData
     * @param slfLogger
     * @return
     * @throws VodafoneException
     */
    private Map sendSms2MultipleMessage(InternalSmsRequest request,  
    	String userid, String usedABI, MamAccount account, byte msg[], DateTime validity, AlphabetEncoding encoding, 
    	LogData logData, SlfLogger slfLogger) {
    	
    	Map retMap = new HashMap();
    	ArrayList smsIds = new ArrayList();
    	String ret = null;

     	final byte UDH_LEN = 0x05; 				// UDH length (5 bytes)
    	final byte NUMBERING_8BITS = 0x00;		// 8bit numbering for concatenated message
    	final byte INFO_LEN = 0x03;				// info length (3 more bytes)
    	
    	final int REFERENCE_NUMBER_IDX = 3;
    	final int TOT_PARTS_IDX = 4;
    	final int PART_NUM_IDX = 5;

    	byte[] udh_template = {
    			UDH_LEN,						// user data header
    			NUMBERING_8BITS,	
    			INFO_LEN,
				0x00,							// reference number for this concatenated message
				0x00,							// total parts number 
				0x00							// current part number 
		};

    	try {
			GWServiceServiceLocator locator = new GWServiceServiceLocator();
			GWService service = locator.getGWService(mUrlVodafone);
			if(service == null) {
				throw new VodafoneRuntimeException("fail to instantiate SMS VodafonePop proxy");
			}
	
    		byte reference_number = nextReferenceNumber();

    		// instantiate the service
			Submit_sm sm = new Submit_sm();

			// set authentication account
			GWSession gws = new GWSession();
			gws.setAccountName(mPopAccount.getUser());									// user Pop
			gws.setAccountPassword(mPopAccount.getPassword());							// password Pop

			// service type
//			sm.setService_type(null);													// null = default value
 			sm.setSource_addr_npi(IntToByte(account.getNumPlanId()));
			sm.setSource_addr_ton(IntToByte(account.getTypeOfNumber()));	// #380 costante - tipo mittente
			sm.setSource_addr(account.getOriginator());									// numero telefono mittente 
			// update log data
			logData.numPlanId = account.getNumPlanId();
			logData.typeOfNumber = account.getTypeOfNumber();
			logData.originator = account.getOriginator();

			sm.setDest_addr_ton(IntToByte(GSMConstants.GSM_TON_INTERNATIONAL));			// costante - tipo destinatario 
			sm.setDest_addr_npi(IntToByte(GSMConstants.GSM_NPI_E164));					// costante - tipo destinatario
			sm.setDestination_addr(request.getPhoneNumber());								// numero telefono destinatario

			sm.setEsm_class(IntToByte(0x40));											// set UDHI flag to true (enable concatenated messages)
//			sm.setProtocol_id(null);													// ? null
//			sm.setPriority_flag(null);													// ? null
//			sm.setValidity_period(null);												// ? null
			if (validity != null) {
				String validityString = SMPPUtilities.getInstance().convertDate(validity);
				sm.setValidity_period(validityString);
				logData.validity = validityString;
			}
			if (request.getDeliveryTime() != null) {
				String delivery = SMPPUtilities.getInstance().convertDate(request.getDeliveryTime());
				sm.setSchedule_delivery_time(delivery);
				logData.delivery = delivery;
			}
//			sm.setSchedule_delivery_time(null);											// ? delivery message per le risposte
			sm.setRegistered_delivery(IntToByte(1));									// set 1 = Delivery Report 0 = No delivery Report
//			sm.setShort_message(null);													// ?
//			sm.setReplace_if_present_flag(null);										// ?
			sm.setData_coding(getDataCoding(encoding));									// codifica messaggio (ascii 7bit) o (UTF-8)
//			sm.setData_coding(IntToByte(0x40));
//			sm.setSm_default_msg_id(null);												// 
//			sm.setSm_length(null);														// lunghezza corpo messaggio
//			sm.setMessage_payload(null);												// messaggio

	    	// split msg in parts and send them
	    	int maxBytesPerPart = PDU_PAYLOAD_MAX_SIZE - udh_template.length;			// cut the header length from message length
	    	int totParts = (int) Math.ceil( (1.0*msg.length) / maxBytesPerPart );		// max length of each message part
	    	log.debug("Sending message as concatenated message (" + totParts + " parts)");
	    	int count=0;
	    	int partNumb = 1;
	    	
	    	while (count < msg.length) {
	    		int n = Math.min(maxBytesPerPart, msg.length-count);
	    		log.debug("Part payload: "+n);
	    		byte[] part = new byte[n];
	    		System.arraycopy(msg, count, part, 0, n);
	    		
	    		byte[] udh = udh_template;
	    		udh[REFERENCE_NUMBER_IDX] = reference_number;							// not relevant since only 1 concatenated msg is sent
	    		udh[TOT_PARTS_IDX] = (byte) totParts;
	    		udh[PART_NUM_IDX] = (byte) partNumb;
	    		log.debug("UDH: " + Hex.encode(udh));
	    		
	    		byte[] partPayload = concatBytes(udh, part);
	    		String msgHeader = Hex.encode(udh); 									// la testa va rappresentata in esadecimale
	    		String msgBody = Hex.encode(part);

//				if(encoding instanceof DefaultAlphabetEncoding)
//				{
//				}
//				else if(encoding instanceof UCS2Encoding)
//				{
//				}

	    		String msgToSend = msgHeader + msgBody;
				sm.setMessage_payload(msgToSend);										// the sms message part

	    		//sm.setMessage( partPayload, encoding );

				// update logData
				logData.messagePart = partNumb + "/" + totParts;
				logData.messagePartLength = n + "/" + msg.length;
				logData.messageLength = request.getMsg().length();
				logData.gatewayContacted = true;

				// send single part
	    		log.debug("Sending part " + partNumb + "/" + totParts + "...");

				// send sms
				Submit_resp resp = service.submit(sm, gws);
				if(resp==null) {
	    			throw new VodafoneRuntimeException("Null response object returned by vodafone");
				}
				logData.chrono.stop();

				ret = resp.getCommand_status().toString();
				retMap.put(RETURN_CODE, ret);

				String status = SMPPUtil.getSMPPErrorDescription(resp.getCommand_status().intValue());

				// update logData
				logData.smsId = resp.getMessage_id();
				smsIds.add(logData.smsId);
				logData.errorMessage = status;
				
				
				if ( resp.getCommand_status().intValue() != SubmitSMResp.ESME_ROK ) {
					/* April 2015 this log was added to LogData to get a single log row
					log.info("Provider=\"VodafonePop\", message_id=\"" + resp.getMessage_id() + "\""
							+ ", error_code=\"" + resp.getError_code() + "\""
							+ ", command_status=\"" + resp.getCommand_status() + "\""
							+ ", sequence_number=\"" + resp.getSequence_number() + "\"");*/
					logResponseData(logData,resp);
					throw new VodafoneRuntimeException(status);
	    		}

				// write security log - April 2015 switched to slfLogger
				//securityLogger.securityLog(logData);
				logData(slfLogger, logData);

	    		++partNumb;
	    		count += n;
				logData.chrono.start();
	    	}
		} catch(Exception e) {
			throw new VodafoneRuntimeException(e.getMessage()); 
		}
		retMap.put(RETURN_SMSIDS, (String []) smsIds.toArray(new String[1]));
		return retMap;     	
    }


    private void logResponseData(LogData logData, Submit_resp resp) {
    	logData.showResponseData = true;
    	logData.provider = "VodafonePop";
    	logData.responseMessageId = resp.getMessage_id();
    	logData.responseErrorCode = resp.getError_code();
    	logData.errorMessage = "Error in POP Server response";	//TODO this is because numeric status from SoapServerLayer is wrong
    	logData.responseCommandStatus = resp.getCommand_status() == null ? "" : resp.getCommand_status().toString();
    	logData.responseSequenceNumber = resp.getSequence_number() == null ? "" : resp.getSequence_number().toString();
    }
    
    /**
     * @param sms
     * @throws VodafoneException
     */
    private static void normalizeInputInfo(SmsMessage sms) throws VodafoneException
    {
    	if(sms==null)
    		throw new VodafoneException("Null SmsMessage");

    	if(sms.getPhoneNumber()==null)
    		throw new VodafoneException("Null phoneNumber returned by SmsMessage.getPhoneNumber()");

    	// remove spaces from phone number 
    	sms.setPhoneNumber( sms.getPhoneNumber().trim() );
    	
    	// remove + from international phone number
    	if(sms.getPhoneNumber().startsWith("+") && sms.getPhoneNumber().length()>1)
        	sms.setPhoneNumber( sms.getPhoneNumber().substring(1) );

    	// remove 00 from international phone number
    	if(sms.getPhoneNumber().startsWith("00") && sms.getPhoneNumber().length()>2)
        	sms.setPhoneNumber( sms.getPhoneNumber().substring(2) );
    }

    /**
     * @param SmsRequest
     * @throws VodafoneException
     */
    private void normalizeInputInfo(InternalSmsRequest request) //throws VodafoneException
    {
    	// remove + from international phone number
    	if(request.getPhoneNumber().startsWith("+") && request.getPhoneNumber().length()>1)
        	request.setPhoneNumber( request.getPhoneNumber().substring(1) );

    	// remove 00 from international phone number
    	if(request.getPhoneNumber().startsWith("00") && request.getPhoneNumber().length()>2)
        	request.setPhoneNumber( request.getPhoneNumber().substring(2) );
    }

    /**
	 * Convert an integer to a byte
	 *
     * @param value
     * @return
     * @throws Exception
     */
    private Byte IntToByte (int value) throws Exception
    {
    	if(value<0 || value >=128)
    		throw new Exception("Cannot convert from int to byte");
    	return new Byte((byte)value);
    }
    
	/**
	 *
	 * @param aa
	 * @param bb
	 * @return
	 */
	private static byte[] concatBytes(byte aa[], byte[] bb) {
		byte res[] = new byte[aa.length + bb.length];
		System.arraycopy(aa, 0, res, 0,         aa.length);
		System.arraycopy(bb, 0, res, aa.length, bb.length);		
		return res;
	}
   
    /**
     * Serialize
	 * @return
	 */
	public String UCS2ToString(byte[] msg) throws Exception
	{
		if((msg.length % 2) != 0)
			throw new Exception("the message has a wrong length");
    
		StringBuffer curr = new StringBuffer();
		for (int i = 0; i < msg.length; i++)
		{
			int unsignedvalue = msg[i] & 0xFF;
			String hexString = Integer.toHexString(unsignedvalue);
			if (hexString.length() < 2)
			    hexString = "0" + hexString;
			else if (hexString.length() > 2)
				throw new Exception("wrong hex conversion");

			curr.append(hexString);
		}
		String txt = curr.toString();
		return txt;
	}

	/**
	 * @return
	 */
	public String getPrefix() {
		return logprefix;
	}

	/**
	 * @param string
	 */
	public void setPrefix(String string) {
		logprefix = string;
	}
	
	/**
	 * Loads configuration for sms service v.2.0
	 */
	private void loadNewConfiguration(ConfigurationReader configReader) throws XASException {
		mMamAccounts = configReader.getOriginators();
		mMamAccountRouter = configReader.getOriginatorRouter();
		//newClients = configReader.getMainConfiguration().getClients();
		mPopAccount = configReader.getPopAccount();
		//mUrlVodafone = configReader.getPopSoapUrl();		9/12/2015 taken back to JNDI resource 
		//mDefaultClient = r.getDefaultClientName();
		if (log.isDebugEnabled()) { 
			String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
			if (myUUID == null) myUUID = UUID.randomUUID().toString();
			
			SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", myUUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0); 
			/* Use XStream for logging params as JSON */
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
			         } 
			     });
			log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT, "a_config_file", this.mUrlVodafone.toString(), "a_mam_accounts", xstream.toXML(mMamAccounts), "a_mam_router", xstream.toXML(mMamAccountRouter.getMap()), "a_pop_account", xstream.toXML(mPopAccount), "a_default_client", mDefaultClient).getLogRow(true));
		}
		
	}

    /**
     * Loads in memory Vodafone config file.
     * Not to be used by sendsms2
     *  
     * @return
     */
	private void loadConfigFile() throws Exception {
		URL fileURL = null;
		if(localDebug) {
			fileURL = new URL("file://d:/Projects/config/VodafoneAccounts.xml");
		} else {
			Context ctx = new InitialContext();
			fileURL = (URL)ctx.lookup(JNDI_VODAFONEACCOUNTS); 
			try {
				fileURL = new URL(fileURL.getProtocol(),fileURL.getHost(),fileURL.getFile().replaceAll("\\.xml", "_v2\\.xml"));  // VERSION2
            } catch (MalformedURLException e) {
	            e.printStackTrace();
            }
			
			log.info("JNDI " + JNDI_VODAFONEACCOUNTS + " ["+fileURL.toString()+"]");
		}

		ReadConfigFile r = new ReadConfigFile();
		r.read(fileURL);

		mMamAccounts = r.getAccounts();
		mMamAccountRouter = r.getAccountRouter();
		mClients = r.getClients();
		mPopAccount = r.getPopAccount();
		mDefaultClient = r.getDefaultClientName();
		
		if (log.isDebugEnabled()) { 
			String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
			if (myUUID == null) myUUID = UUID.randomUUID().toString();
			
			SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", myUUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0); 
			/* Use XStream for logging params as JSON */
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
			         } 
			     });
			log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT, Constants.K_CONFIG_FILE, fileURL.toString(), "a_mam_accounts", xstream.toXML(mMamAccounts), "a_mam_router", xstream.toXML(mMamAccountRouter.getMap()), "a_clients", xstream.toXML(mClients), "a_pop_account", xstream.toXML(mPopAccount), "a_default_client", mDefaultClient).getLogRow(true));
		}
		
	}

    /**
     * Loads in memory Vodafone config file.
     * Not to be used by sendsms2
     * 
     * NEWCONFIGURATION
     * 
     * @return
     */
	
	/* JJCONFIG
	private void loadConfigFileNew() throws Exception {
		URL fileURL = null;
		Context ctx = new InitialContext();
		fileURL = (URL)ctx.lookup(JNDI_SMS_CONFIG); 
		
		log.info("JNDI " + JNDI_SMS_CONFIG + " ["+fileURL.toString()+"]");

		ReadConfigFile r = new ReadConfigFile();
		r.read(fileURL);

		mMamAccounts = r.getAccounts();
		mClients = r.getClients();
		mPopAccount = r.getPopAccount();
		mDefaultClient = r.getDefaultClientName();

		
		
		
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", "", SmartLog.V_SCOPE_DEBUG).logReferrer(0);
        sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT, Constants.K_CONFIG_FILE, fileURL.getFile());
		log.debug(sl.getLogRow());
		
		XMLConfiguration config = new XMLConfiguration(fileURL.getFile());
		String path, value;

		path = TAG_VERSION + "." + TAG_CACHE_TIMEOUT;  
		value = config.getString(path); if (value != null) configuration.setCacheTimeout(value);
		System.out.println(path + ": " + value);

		path = TAG_VERSION + "." + TAG_JMS_TIME_ZONE;  
		value = config.getString(path); if (value != null) configuration.setJmsTimeZone(value);
		System.out.println(path + ": " + value);
		
		path = TAG_VERSION + "." + TAG_CLIENTS + "[@" + ATT_CLIENTS_DEFAULT_PROVIDER + "]";    
		value = config.getString(path);	if (value != null) configuration.setDefaultProvider(value);
		System.out.println(path + ": " + value);
		
		path = TAG_VERSION + "." + TAG_CLIENTS + "[@" + ATT_CLIENTS_BACKUP_LIST + "]";    

		String[] backupProvider = config.getStringArray(path);
		value = "";
		for (int i = 0; i < backupProvider.length; i++) {
			configuration.addBackupProvider((String) backupProvider[i]);
			value += ("".equals(value) ? "" : ";") + (String) backupProvider[i];
		}
		System.out.println(path + ": " + value);


		String clientPath = TAG_VERSION + "." + TAG_CLIENTS + "." + TAG_CLIENT + "[@" + ATT_CLIENT_NAME + "]";
		String clientName = null;
		String[] clients = config.getStringArray(clientPath);
		for (int i = 0; i < clients.length; i++) {
			System.out.println(clientPath + ": " + clients[i]);
			it.usi.xframe.xas.bfimpl.sms.configuration.Client client = new it.usi.xframe.xas.bfimpl.sms.configuration.Client();
			
			String clientPrefix = TAG_VERSION + "." + TAG_CLIENTS + "." + TAG_CLIENT  + "(" + i + ")"; 

			path = clientPrefix + "[@" + ATT_CLIENT_ALIAS + "]";
			value = config.getString(path);	if (value != null) client.setAlias(value);
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_DESCRIPTION + "]";
			value = config.getString(path);	if (value != null) client.setDescription(value);
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_NAME + "]";
			value = config.getString(path);	if (value != null) client.setName(value);
			clientName = value;
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_NORMALIZEPHONEMESSAGE + "]";
			value = config.getString(path);	if (value != null) client.setNormalizePhoneMessage(Boolean.valueOf(value).booleanValue());
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_ORIGINATOR + "]";
			value = config.getString(path);	if (value != null) client.setOriginator(value);
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_USEBLACKLIST + "]";
			value = config.getString(path);	if (value != null) client.setUseBlackList(Boolean.valueOf(value).booleanValue());
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_VALIDATEPHONENUMBER + "]";
			value = config.getString(path);	if (value != null) client.setValidatePhoneNumber(Boolean.valueOf(value).booleanValue());
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_VALIDATEPHONENUMBERREGEX + "]";
			value = config.getString(path);	if (value != null) client.setValidatePhoneNumberRegEx(value);
			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_DEFAULT_VALIDITY + "]";
			System.out.println(path + ": " + config.getString(path));
			String defaultValidity = config.getString(path);
			if (defaultValidity != null) {
				try {
					client.setDefaultValidity(new Integer(defaultValidity));
				} catch (NumberFormatException e) {
					throw new XASConfigurationException("Invalid value for defaultValidity: " + defaultValidity);
				}
			}

			path = clientPrefix + "[@" + ATT_CLIENT_PROVIDER + "]";
			value = config.getString(path);	if (value != null) configuration.addClientProvider(clientName, value);

			System.out.println(path + ": " + value);

			path = clientPrefix + "[@" + ATT_CLIENT_BACKUP_LIST + "]";
			backupProvider = config.getStringArray(path);
			value = "";
			for (int j = 0; j < backupProvider.length; j++) {
				configuration.addClientProvider(clientName, (String) backupProvider[j]);
				value += ("".equals(value) ? "" : ";") + (String) backupProvider[j];
			}
			System.out.println(path + ": " + value);

			configuration.addClient(client);
			}

		String providerPath = TAG_VERSION + "." + TAG_PROVIDERS + "." + TAG_PROVIDER + "[@" + ATT_PROVIDER_NAME + "]";
		String[] providers = config.getStringArray(providerPath);
		for (int i = 0; i < providers.length; i++) {
			System.out.println(providerPath + ": " + providers[i]);

			ProviderData providerData = new ProviderData();
			
			String providerPrefix = TAG_VERSION + "." + TAG_PROVIDERS + "." + TAG_PROVIDER  + "(" + i + ")"; 

			path = providerPrefix + "[@" + ATT_PROVIDER_NAME + "]";
			value = config.getString(path);	if (value != null) providerData.setName(value);
			System.out.println(path + ": " + value);

			path = providerPrefix + "[@" + ATT_PROVIDER_TYPE + "]";
			value = config.getString(path);	if (value != null) providerData.setType(value);
			System.out.println(path + ": " + value);

			path = providerPrefix + "[@" + ATT_PROVIDER_DESCRIPTION + "]";
			value = config.getString(path);	if (value != null) providerData.setDescription(value);
			System.out.println(path + ": " + value);

			path = providerPrefix + "[@" + ATT_PROVIDER_LOGPREFIX + "]";
			value = config.getString(path);	if (value != null) providerData.setLogPrefix(value);
			System.out.println(path + ": " + value);

			path = providerPrefix + "[@" + ATT_PROVIDER_SMSLOGLEVEL + "]";
			value = config.getString(path);	if (value != null) providerData.setSmsLogLevel(value);
			System.out.println(path + ": " + value);

			if (GatewaySmsVodafonePop.TYPE.equals(providerData.getType())) {
				it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.ConfigurationReaderV2 cr = new it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.ConfigurationReaderV2(configuration);
				
				cr.read(providerPrefix, providerData, config);

			}
			System.out.println("providerData: " + providerData.getName());
			
			configuration.addProviderData(providerData);
		}

} catch (ConfigurationException e) {
		throw new XASConfigurationException("Error looking for configuration file",e);
    }
}
		
		
		
		
		
		
		
		
		
		if (log.isDebugEnabled()) { 
			String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
			if (myUUID == null) myUUID = UUID.randomUUID().toString();
			
			SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", myUUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0); 
			// Use XStream for logging params as JSON 
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
			         } 
			     });
			log.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT, Constants.K_CONFIG_FILE, fileURL.toString(), "a_mam_accounts", xstream.toXML(mMamAccounts), "a_clients", xstream.toXML(mClients), "a_pop_account", xstream.toXML(mPopAccount), "a_default_client", mDefaultClient).getLogRow(true));
		}
		
	}
*/
	/**
	 * @return
	 * @throws Exception
	 */
	private URL getVodafonePopUrl() throws Exception
	{
		URL vodafonePopURL = null;
		if(localDebug)
		{
			vodafonePopURL = new URL("http://10.182.36.177:8080/NEWGWService/services/GWService");
		}
		else
		{
			Context ctx = new InitialContext();
			vodafonePopURL = (URL)ctx.lookup(JNDI_VODAFONESOAPURL);
			log.info("JNDI " + JNDI_VODAFONESOAPURL + " [" + vodafonePopURL.toString() + "]");
		}

		return vodafonePopURL;
	}

	/**
	 * tranform a byte message in a string encoded message to be tranfered by soap 
	 *  
	 * @param msg
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	private String soapEncodeSmMessage (byte[] msg, AlphabetEncoding enc) throws Exception
	{
		if(enc instanceof DefaultAlphabetEncoding)
		{
			String txt = enc.decodeString(msg);
			return txt;
		}
		else if(enc instanceof UCS2Encoding)
		{
			return UCS2ToString(msg);
		}

		throw new Exception("Encoding message not yet implemented");

	}

	/**
	 * Return the constant for the choosen encoder
	 * 
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	private Byte getDataCoding (AlphabetEncoding enc) throws XASRuntimeException
	{
//		0 0 0 0 0 0 0 0 SMSC Default Alphabet
//		0 0 0 0 0 0 0 1 IA5 (CCITT T.50)/ASCII (ANSI X3.4) b
//		0 0 0 0 0 0 1 0 Octet unspecified (8-bit binary) b
//		0 0 0 0 0 0 1 1 Latin 1 (ISO-8859-1) b
//		0 0 0 0 0 1 0 0 Octet unspecified (8-bit binary) a
//		0 0 0 0 0 1 0 1 JIS (X 0208-1990) b
//		0 0 0 0 0 1 1 0 Cyrllic (ISO-8859-5) b
//		0 0 0 0 0 1 1 1 Latin/Hebrew (ISO-8859-8) b
//		0 0 0 0 1 0 0 0 UCS2 (ISO/IEC-10646) a
//		0 0 0 0 1 0 0 1 Pictogram Encoding b
//		0 0 0 0 1 0 1 0 ISO-2022-JP (Music Codes) b
//		0 0 0 0 1 0 1 1 reserved
//		0 0 0 0 1 1 0 0 reserved
//		0 0 0 0 1 1 0 1 Extended Kanji JIS(X 0212-1990) b
//		0 0 0 0 1 1 1 0 KS C 5601 b
//		0 0 0 0 1 1 1 1 reserved
//		:
//		1 0 1 1 1 1 1 1 reserved
//		1 1 0 0 x x x x GSM MWI control - see [GSM 03.38] d
//		1 1 0 1 x x x x GSM MWI control - see [GSM 03.38] d
//		1 1 1 0 x x x x reserved
//		1 1 1 1 x x x x GSM message class control - see [GSM 03.38] e
		if(enc instanceof DefaultAlphabetEncoding)
		{
			return new Byte(GSM338);
		}
		else if(enc instanceof UCS2Encoding)
		{
			return new Byte(UCS2);
		}

		throw new XASRuntimeException("Encoding constant not yet implemented");
	}

	/**
	 * Return the description for the choosen encoder
	 * 
	 * @param enc
	 * @return
	 */
	private String getDataCodingDescription (AlphabetEncoding enc)
	{
		if(enc instanceof DefaultAlphabetEncoding)
		{
			return "GSM338";
		}
		else if(enc instanceof UCS2Encoding)
		{
			return "UCS2";
		}

		return "UNKNOWN";
	}

	/**
     * @return
     */
    private synchronized byte nextReferenceNumber() {
		return ++GatewaySmsVodafonePop.smppReferenceNumber ;
	}

    
    /**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try {
			localDebug = true;

			AlphabetEncoding encoding = new DefaultAlphabetEncoding();
			byte[] msgBytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
			String self = encoding.decodeString(msgBytes);
			
			SmsMessage sms = new SmsMessage();

			// test simple
//			sms.setMsg("Hello");
//			sms.setMsg("1");

			// test unicode char
//			sms.setMsg("");

			// test sms long
			sms.setMsg("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 XXXXXXXXX XXXXX XXXXXX XEND");

			// test sms long with unicode chars
//			sms.setMsg("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 XXXXXXXXX XXXXX XXXXXX XEND");

			// test telephon number without international prefix
			sms.setPhoneNumber("3474417850");
			// test telephon number wrong
//			sms.setPhoneNumber("39347441785A0");

			// test number with international prefix
//			sms.setPhoneNumber("393474417850");
//			sms.setPhoneNumber("+393474417850");
//			sms.setPhoneNumber("00393474417850");

			// test senza abi code

			// test lunghezza superiore ai 5 messaggi

			SmsSenderInfo sender = new SmsSenderInfo();
			sender.setABICode("02008");
			sender.setAlias(null);
			
			String userid = "";
			
			GatewaySmsVodafonePop gtw = new GatewaySmsVodafonePop("VODAFONESMS", "default");

			gtw.sendSms(sms, sender, userid);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}