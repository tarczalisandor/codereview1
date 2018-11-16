package it.usi.xframe.xas.bfimpl;

import ie.omk.smpp.util.AlphabetEncoding;
import ie.omk.smpp.util.DefaultAlphabetEncoding;
import ie.omk.smpp.util.UCS2Encoding;
import it.usi.xframe.system.bfinfo.BaselineInfo;
import it.usi.xframe.system.bfinfo.BaselineInfoFactory;
import it.usi.xframe.system.bfutil.db.DBConnectionFactory;
import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.system.ifutils.EnvironmentLoader;
import it.usi.xframe.system.ifutils.IEnvironment;
import it.usi.xframe.xas.bfimpl.a2psms.ApplicationGateway;
import it.usi.xframe.xas.bfimpl.a2psms.IGatewayA2Psms;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationBasic;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalDeliveryResponse;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfimpl.sms.GatewayA2pSmsFactory;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.ConstantsSplunk;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.data.DeliveryResponse;
import it.usi.xframe.xas.bfutil.data.ENC_TYPE;
import it.usi.xframe.xas.bfutil.data.EmailMessage;
import it.usi.xframe.xas.bfutil.data.InternalDeliveryReport;
import it.usi.xframe.xas.bfutil.data.InternalMobileOriginated;
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
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SecurityUtility;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.util.Crypto;
import it.usi.xframe.xas.util.PreSmsRoleCache;
import it.usi.xframe.xas.util.json.XConstants;
import it.usi.xframe.xas.wsutil.DeliveryReport;
import it.usi.xframe.xas.wsutil.MobileOriginated;
import it.usi.xframe.xas.wsutil.StatusCodeType;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.MDC;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SLF_Exception;
import eu.unicredit.xframe.slf.SmartAnalytic;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;
 

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasSendsmsServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasSendsmsServiceFacade {

	private static Logger logger = LoggerFactory.getLogger(XasSendsmsServiceFacade.class);
	public final static int MAX_SIZE_GSM7_PAYLOAD	= 160;
	public final static int MAX_SIZE_UCS2_PAYLOAD	= 140;
 	final static 	    int UDH_LENGTH = 6; 			
	
	//**** JMSMessageConsumer.java
    
    
    
    final static String K_STEP = "a_step";

	/**
	 * SendSms V1 interface.
	 * 
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#sendSms(it.usi.xframe.xas.bfutil.data.Sms)
	 * 
	 * @param sms
	 * @param sender
	 * @throws RemoteException
	 * @throws XASException
	 */
	public void sendSms(SmsMessage sms, SmsSenderInfo sender) throws RemoteException, XASException {
		SmsRequest request = new SmsRequest();
		request.setPhoneNumber(sms.getPhoneNumber());
		request.setMsg(sms.getMsg());
		request.setXasUser(sender.getABICode()); // XasUserName Normalization
		InternalSmsRequest internalSmsRequest = new InternalSmsRequest(request);
		internalSmsRequest.setInterfaceV1();
		internalSmsRequest.setChannel(getChannel());
 		internalSmsRequest.setUuid(UUID.randomUUID().toString()); // Not present in v1 generate it
 		MDC.put(ConstantsSms.MY_UUID_KEY, internalSmsRequest.getUuid());
 		try {
			InternalSmsResponse internalSmsResponse = sendSmsInternal(internalSmsRequest);
			SmsResponse smsResponse = internalSmsResponse.getSmsResponse();
			if (smsResponse.isError())
				throw new RemoteException(smsResponse.getCode() + " " + smsResponse.getMessage() );
 		} finally {
			MDC.remove(ConstantsSms.MY_UUID_KEY);

 		}
	}

	/**
	 * SendSms V2 interface.
	 * @param request
	 * @return
	 * @throws RemoteException
	 */
	public SmsResponse sendSms2(SmsRequest request) throws RemoteException {
		InternalSmsRequest internalSmsRequest = new InternalSmsRequest(request);
		internalSmsRequest.setInterfaceV2();
 		internalSmsRequest.setChannel(getChannel());
 		internalSmsRequest.setUuid(UUID.randomUUID().toString()); // Not present in v2 generate it
 		MDC.put(ConstantsSms.MY_UUID_KEY, internalSmsRequest.getUuid());
 		InternalSmsResponse internalSmsResponse = null;
 		
 		try {
 			internalSmsResponse = sendSmsInternal(internalSmsRequest);
 		} finally {
			MDC.remove(ConstantsSms.MY_UUID_KEY);

 		}
 		return internalSmsResponse.getSmsResponse();
	}

	/**
	 * SendSms V3 interface.
	 * @param request SmsRequest3 object
	 * @return an smsResponse
	 * @throws RemoteException
	 */
	public SmsResponse3 sendSms3(SmsRequest3 smsRequest) throws RemoteException {
		if (smsRequest.getUuid() == null) {
 			smsRequest.setUuid(UUID.randomUUID().toString());
 		}
 		MDC.put(ConstantsSms.MY_UUID_KEY, smsRequest.getUuid());
 		SmsResponse3 smsResponse3 = null;

 		try {
	 		if (smsRequest.getEncryptionType() != null) {
		 		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
		        	.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), smsRequest.getUuid(), SmartLog.V_SCOPE_DEBUG)
					.logIt(SmartLog.K_METHOD, "sendSms3").preset("default"); 
				logger.debug(sl.reload("default").logIt(SmartLog.K_STEP, "decrypting", SmartLog.K_PARAMS, smsRequest.getEncryptionType().getValue()).getLogRow());
				if (!ENC_TYPE.NO_ENCRYPTION.equals(smsRequest.getEncryptionType())) {
					try {
						String [] encryptParams = new String[] {smsRequest.getUuid()};
						smsRequest.setPhoneNumber(Crypto.decrypt(smsRequest.getEncryptionType(), smsRequest.getPhoneNumber(), encryptParams));
						smsRequest.setMsg(Crypto.decrypt(smsRequest.getEncryptionType(), smsRequest.getMsg(), encryptParams));
		            } catch (XASException e) {
		                throw new RemoteException(e.getCode() + " - " + MessageFormat.format(e.getMessage(), e.getParameters()),e);
		            }
				}
			}
			InternalSmsRequest internalSmsRequest = new InternalSmsRequest(smsRequest);
			internalSmsRequest.setInterfaceV3();
	 		internalSmsRequest.setChannel(getChannel());
			InternalSmsResponse internalSmsResponse = sendSmsInternal(internalSmsRequest);
			smsResponse3 = internalSmsResponse.getSmsResponse3();
  		} finally {
			MDC.remove(ConstantsSms.MY_UUID_KEY);

 		}

		return smsResponse3;
	}

 	/**
	 * Reroot the sendInternationalSms to the sendSmsInternal.
	 */
	public InternationalSmsResponse sendInternationalSms(InternationalSmsMessage sms, SmsDelivery delivery,	SmsBillingInfo billing) throws RemoteException, XASException {
		SmsRequest request = new SmsRequest();
		
		request.setMsg(sms.getText());
		request.setPhoneNumber(delivery.getPhoneNumber());
		request.setXasUser(ConstantsSms.UNIKEY_XASUSER);
		
		InternalSmsRequest internalSmsRequest = new InternalSmsRequest(request);
		internalSmsRequest.setInterfaceV1();
		internalSmsRequest.setChannel(getChannel());
	
		InternationalSmsResponse isr = new InternationalSmsResponse();
		InternalSmsResponse internalSmsResponse = sendSmsInternal(internalSmsRequest);
		SmsResponse smsResponse = internalSmsResponse.getSmsResponse();
		if (! smsResponse.isOk()) {
			isr.setCode(InternationalSmsResponse.GENERIC_ERROR);
			isr.setDescr(smsResponse.getMessage());
		}
		return isr;
	}

	

	//**** JMSMessageConsumer.java
    
	/**
	 * SendSms Internal Implementation.
	 * @param request
	 * @return an SmsResponse with code and message
	 * @throws RemoteException
	 */
	private InternalSmsResponse sendSmsInternal(InternalSmsRequest internalSmsRequest) {
		InternalSmsResponse internalSmsResponse = new InternalSmsResponse();
		XasUser xasUser = null;
		Originator originator = null;
		Provider provider = null;
		
		// JSON logging utility
  		String myUUID = internalSmsRequest.getUuid() != null 
  					  ? internalSmsRequest.getUuid()
  		 			  : (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		else try { // if SmsRequest UUID is invalid must get a valid myUUID for logging purpose
				UUID.fromString(internalSmsRequest.getUuid());
			} catch (Exception e) {
				myUUID = UUID.randomUUID().toString();
			}

		Chronometer chronometer = new Chronometer(true, ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_START + "@" + ConstantsSms.MY_APPL_ID + " " + " MY_UUID:" + myUUID);
				
		if (internalSmsRequest.getUuid() == null)
			internalSmsRequest.setUuid(myUUID);
		
 		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
        	.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "sendSmsInternal").preset("default"); 

		if (logger.isDebugEnabled()) { 
			logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(internalSmsRequest)).getLogRow(true)); // Debug and keep row
		}
		
		sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT).preset("default");
//		String[] smsIdsSave = null;
		try {
 			// XasUserName Normalization
			logger.debug(sl.logIt(K_STEP, "XasUserName Normalization").getLogRow(true)); // Debug and keep row
			internalSmsRequest.setXasUser(XasUser.normalizeName(internalSmsRequest.getXasUserName())); 

			// SmsRequest Normalization 
			logger.debug(sl.logIt(K_STEP, "SmsRequest Normalization").getLogRow(true)); // Debug and keep row

			// Security Validation
 			logger.debug(sl.logIt(K_STEP, "Security Validation - Verify user in role", SmartLog.K_PARAMS, SingleSecurityProfile.MT.toString()).getLogRow(true)); // Debug and keep row
			if (!ConstantsSms.CHANNEL_MQ.equals(internalSmsRequest.getChannel())) {
				internalSmsRequest.setUserId(getSupport().getPrincipal().getName());
				boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MT).add(SingleSecurityProfile.ADMIN));
				internalSmsRequest.setUserInRole(new Boolean(isUserInRole));

				// original Josè code
//				boolean isUserInRole = false;
//				internalSmsRequest.setUserId(getSupport().getPrincipal().getName());
//				isUserInRole = getSupport().isUserInRole(ConstantsSms.SMS_ROLE);
//				internalSmsRequest.setUserInRole(new Boolean(isUserInRole));
			} // else --- settedup by caller.
			if (!internalSmsRequest.isInterfaceV1() && !internalSmsRequest.getUserInRole().booleanValue()) 
				throw new XASException(ConstantsSms.XAS05013E_MESSAGE, null, ConstantsSms.XAS05013E_CODE, new Object[] {internalSmsRequest.getUserId(), SingleSecurityProfile.MT.toString()});
			
			// Configuration Load
			logger.debug(sl.reload("default").logIt(K_STEP, "Configuration Load").getLogRow(true)); // Debug and keep row
//			GatewayA2pSmsFactory.getInstance().getConfiguration(); // follows
			// XasUser validation & load
			logger.debug(sl.logIt(K_STEP, "XasUser validation & load").getLogRow(true)); // Debug and keep row
			xasUser = GatewayA2pSmsFactory.getInstance().getConfiguration()
				.getFinalXasUser(myUUID, internalSmsRequest.getXasUserName(), internalSmsRequest.isInterfaceV1());
			
			internalSmsRequest.setXasUser(xasUser.getName()); // Set Used xasUserName
			
			if (internalSmsRequest.getInterfaceVersion() < xasUser.getInterfaceVersion().intValue())
				throw new XASException(ConstantsSms.XAS01018E_MESSAGE, null, ConstantsSms.XAS01018E_CODE, new Object[] {new Integer(internalSmsRequest.getInterfaceVersion()), xasUser.getName(), xasUser.getInterfaceVersion()});
			
			// PhoneNumber Normalization
				// xasUser.getDefaultRegion();
				// throw new XASException(Constants.XAS00010E_MESSAGE, null, Constants.XAS00010E_CODE, new Object[] {xasUser.getDefaultRegion(), xasUser.getName()});
			
			logger.debug(sl.logIt(K_STEP, "PhoneNumber Normalization - pre", SmartLog.K_PARAMS, internalSmsRequest.getPhoneNumber()).getLogRow(true)); // Debug and keep row
			String phoneNumber = internalSmsRequest.getPhoneNumber();
			if (phoneNumber == null)
				throw new XASException(ConstantsSms.XAS01009E_MESSAGE, null, ConstantsSms.XAS01009E_CODE, new String[] {"", "Missing"} );

			// Validate PhoneNumber Regular Expression
			Pattern regEx = xasUser.getValidatePhoneNumberRegEx(); 
    		if (regEx != null && !regEx.matcher(phoneNumber).matches())
				throw new XASException(ConstantsSms.XAS01009E_MESSAGE, null, ConstantsSms.XAS01009E_CODE, new String[] {phoneNumber, "Must match " + regEx.pattern()} );

    		// PhoneNumber Normalization
    		phoneNumber = phoneNumber.replaceAll("\\D|^00",""); // Remove leading + or 00 and non digit chars
			if ("".equals(phoneNumber))
				throw new XASException(ConstantsSms.XAS01009E_MESSAGE, null, ConstantsSms.XAS01009E_CODE, new String[] {"", "Empty"} );
			internalSmsRequest.setPhoneNumber(phoneNumber);  
			logger.debug(sl.logIt(K_STEP, "PhoneNumber Normalization - post", SmartLog.K_PARAMS, internalSmsRequest.getPhoneNumber()).getLogRow(true)); // Debug and keep row

			// Sms Request validation
			logger.debug(sl.reload("default").logIt(K_STEP, "Sms Request validation").getLogRow(true)); // Debug and keep row
			logger.debug(sl.reload("default").logIt(K_STEP, "Uuid validation").getLogRow(true)); // Debug and keep row
			try {
				UUID.fromString(internalSmsRequest.getUuid());
			} catch (Exception e) {
				throw new XASException(ConstantsSms.XAS01020E_MESSAGE, null, ConstantsSms.XAS01020E_CODE, new String[] {internalSmsRequest.getUuid()});
				
			}

			logger.debug(sl.reload("default").logIt(K_STEP, "Replace Class validation").getLogRow(true)); // Debug and keep row
			String classFromService = internalSmsRequest.getReplaceClass();

			// Override protocolId for sms message if present
			if (classFromService != null) {
				classFromService = classFromService.toUpperCase();
				int protocolId = 0;
				if (xasUser.getReplaceMap() != null) {
					// Replace on terminal - http://opensmpp.org/specs/smppv34_gsmumts_ig_v10.pdf
		 			if (classFromService != null && xasUser.getReplaceMap().get(classFromService) != null) {
		 				protocolId = 64 + ((Integer)xasUser.getReplaceMap().get(classFromService)).intValue();
						internalSmsRequest.setProtocolId(protocolId);
					} 
				}
				if (protocolId == 0)
			      throw new XASException(ConstantsSms.XAS02031E_MESSAGE, null, ConstantsSms.XAS02031E_CODE, new Object[] {classFromService, xasUser.getName(), xasUser.getReplaceMap() != null ? xasUser.getReplaceMap().toString() : "empty"});
			}
 
			// Text message validation
			logger.debug(sl.reload("default").logIt(K_STEP, "Text message validation").getLogRow(true)); // Debug and keep row
			if (internalSmsRequest.getMsg() == null || internalSmsRequest.getMsg().length() == 0) {
				throw new XASException(ConstantsSms.XAS01011E_MESSAGE, null, ConstantsSms.XAS01011E_CODE, null);
			}
			
			if (!internalSmsRequest.isInterfaceV1()) {
				// Validity & ValidityPeriod & DeliveryTime validation
				if (internalSmsRequest.getValidityPeriod() == null && internalSmsRequest.getValidity() == null && xasUser.getDefaultValidityPeriod()!= null) {
					logger.debug(sl.logIt(K_STEP, "ValidityPeriod applying default").getLogRow(true)); // Debug and keep row
					internalSmsRequest.setValidityPeriod(xasUser.getDefaultValidityPeriod());
				}
				logger.debug(sl.logIt(K_STEP, "Validate Validity").getLogRow(true)); // Debug and keep row
				
				DateTime validity = internalSmsRequest.validateValidity(internalSmsRequest.getValidityPeriod());

				if (validity != null) {
					internalSmsRequest.setValidity(validity.toDate());
    				logger.debug(sl.logIt(K_STEP, "Validate Delivery - Validity", SmartLog.K_PARAMS, validity.toDate().toString()).getLogRow(true)); // Debug and keep row
				}
				internalSmsRequest.validateDelivery(validity);
				
				logger.debug(sl.logIt(K_STEP, "Validate Delivery - Period", SmartLog.K_PARAMS, "ValidityPeriod=" + internalSmsRequest.getValidityPeriod() + ",Validity=" + internalSmsRequest.getValidity()).getLogRow(true)); // Debug and keep row


			} else {
				logger.debug(sl.logIt(K_STEP, "Validate Validity & Delivery skipped due to V1 sendSms", SmartLog.K_PARAMS, "").getLogRow(true)); // Debug and keep row
			}

			// Choose Provider
			logger.debug(sl.reload("default").logIt(K_STEP, "Choose Provider").getLogRow(true)); // Debug and keep row
			provider = GatewayA2pSmsFactory.getInstance().getProvider(xasUser); 
			logger.debug(sl.logIt(K_STEP, "Choose Provider - providerType", SmartLog.K_PARAMS, provider.getType()).getLogRow(true)); // Debug and keep row
			IGatewayA2Psms gatewaySms = provider.getGatewaySms();

			// Build Sms
			sl.logIt(SmartLog.K_PHASE, "build Sms");

			logger.debug(sl.logIt(K_STEP, "ForceAsciiEncoding", SmartLog.K_PARAMS, internalSmsRequest.getForceAsciiEncoding() != null ? internalSmsRequest.getForceAsciiEncoding().toString() : "null" ).getLogRow(true)); // Debug and keep row
			if (internalSmsRequest.getForceAsciiEncoding() == null) {
				logger.debug(sl.reload("default").logIt(K_STEP, "ForceAsciiEncoding applying default").getLogRow(true)); // Debug and keep row
				internalSmsRequest.setForceAsciiEncoding(new Boolean(xasUser.isDefaultForceAsciiEncoding()));
			}
		
			logger.debug(sl.logIt(K_STEP, "Ascii 7 Bit or UCS2 Encoding").getLogRow(true)); // Debug and keep row
			
			// Doing encoding & decoding and verify if match
			AlphabetEncoding encoding = new DefaultAlphabetEncoding();	// default GSM 03.38 encoding
			byte[] msgBytes  = encoding.encodeString(internalSmsRequest.getMsg());
			String msgString = encoding.decodeString(msgBytes);

 			int maxSmsAllowed = provider.getMaxSmsAllowed().intValue();
 			int maxPayload = 0, maxByteLen = 0;;
 			boolean gsm7bit = internalSmsRequest.getForceAsciiEncoding().booleanValue() || internalSmsRequest.getMsg().equals(msgString); 
			if (gsm7bit) {
				logger.debug(sl.logIt(K_STEP, "Using GSM 03.38 encoding", SmartLog.K_PARAMS, (internalSmsRequest.getForceAsciiEncoding().booleanValue() ? "forceAsciiEncoding" :"")).getLogRow(true)); // Debug and keep row
				maxPayload = 140;
				maxByteLen = MAX_SIZE_GSM7_PAYLOAD;
				internalSmsRequest.setEncodingGSM338();
				if (it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE.equals(provider.getType())) {
					// Message for Vodafone provider should be normalized
					internalSmsRequest.setMsg(normalizePhoneMessage(internalSmsRequest.getMsg()));
					msgBytes = encoding.encodeString(internalSmsRequest.getMsg());
				}
			} else {
				// Retrieve first not GSM character in original message that switch to UCS2 encoding skipping original question marks "?" not produced by encodeString()
				int firstNotGsm = -1;
				do {
					firstNotGsm = msgString.indexOf("?", firstNotGsm + 1);
				} while (internalSmsRequest.getMsg().charAt(firstNotGsm) == '?' && firstNotGsm != -1);
				internalSmsRequest.setFirstNotGsm(firstNotGsm);
				logger.debug(sl.reload("default").logIt(K_STEP, "Using UCS2 unicode encoding", "a_charAt", Integer.toString(firstNotGsm), "a_charUnicode", Integer.toHexString(internalSmsRequest.getMsg().charAt(firstNotGsm)), "a_character", internalSmsRequest.getMsg().substring(firstNotGsm, firstNotGsm + 1)).getLogRow(true)); // Debug and keep row
				maxPayload = MAX_SIZE_UCS2_PAYLOAD;
				maxByteLen = MAX_SIZE_UCS2_PAYLOAD;
				internalSmsRequest.setEncodingUCS2();
				try {
	                encoding = new UCS2Encoding();
                } catch (UnsupportedEncodingException e) {
                	StringWriter errors = new StringWriter();
                	e.printStackTrace(new PrintWriter(errors));
    		    	logger.error(errors.toString());
     				throw new XASException(ConstantsSms.XAS03099E_MESSAGE, errors.toString(), ConstantsSms.XAS03099E_CODE, new Object[] {internalSmsRequest.getMsg()});
                 }	
                
				msgBytes = encoding.encodeString(internalSmsRequest.getMsg());
			}
			internalSmsRequest.setMsgBytes(msgBytes);
//			logger.debug("msgLength=" + internalSmsRequest.getMsg().length() + ", msgByteLength=" + msgBytes.length);
			
			// verify that message length is contained into MULTIPART_SMS_MAX_SIZE
 			logger.debug(sl.reload("default").logIt(K_STEP, "Check Sms message length").getLogRow(true)); // Debug and keep row
			int maxByte = ((maxPayload - ((maxSmsAllowed == 1) ? 0 : UDH_LENGTH)) * maxSmsAllowed) * 8 / (gsm7bit ? 7 : 8); // Remove header length if more than 1 message allowed
			if (msgBytes.length > maxByte) {
 				throw new XASException(ConstantsSms.XAS01014E_MESSAGE, null, ConstantsSms.XAS01014E_CODE, new Object[] {new Integer(msgBytes.length / (gsm7bit ? 1 : 2)), new Integer(maxByte / (gsm7bit ? 1 : 2)), internalSmsRequest.getEncodingDescription()});
			}

			logger.debug(sl.logIt(K_STEP, "Choose originator").getLogRow(true)); // Debug and keep row
			originator = provider.getFinalOriginator(myUUID, internalSmsRequest.getPhoneNumber(), xasUser);
	
			
 			// Send Sms
			if (logger.isDebugEnabled()) { 
				logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_CALL, SmartLog.K_CALLED, "gatewaySms.sendMessage")
						.logIt("a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest))
						.logIt("a_xasUser", XConstants.XSTREAMER.toXML(xasUser))
						.logIt("a_provider", XConstants.XSTREAMER.toXML(provider))
						.logIt("a_originator", XConstants.XSTREAMER.toXML(originator))
						.getLogRow(true)); // Debug and keep row
			}
			chronometer.partial(ConstantsSplunk.K_TIME_SEC_SEND + "." + ConstantsSplunk.K_TIME_SECTION_START);
			try {
				internalSmsResponse = gatewaySms.sendMessage(internalSmsResponse, internalSmsRequest, xasUser, provider, originator, msgBytes.length > maxByteLen, chronometer);
//			smsIdsSave = internalSmsResponse != null ? internalSmsResponse.getSmsIds() : null;
			} finally { 
				chronometer.partial(ConstantsSplunk.K_TIME_SEC_SEND + "." + ConstantsSplunk.K_TIME_SECTION_END);
			}

 			if (it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.getInstance().isDatabasePresent() 
 				&& internalSmsResponse != null && internalSmsResponse.getSmsIds() != null) { // If database and smsIds present insert for delivery report 
 				// Doing DB stats
 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_WAIT); 
				Connection connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_START); 
				try {
	    			if (xasUser.getDeliveryReport()) { // Store delivery report request into the DB
	
		    			logger.debug(sl.reload("default").logIt(K_STEP, "tableInsertSmsIntoDB","a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest), "a_internalSmsResponse", XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow(true)); // Debug and keep row
		    			tableInsertDRrequest(myUUID, connection, internalSmsRequest, internalSmsResponse);
	    			}
	    			logger.debug(sl.reload("default").logIt(K_STEP, "tableUpdateMTstats","a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest), "a_internalSmsResponse", XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow(true)); // Debug and keep row
					tableUpdateMTstats(myUUID, connection, provider, xasUser, internalSmsRequest, internalSmsResponse);
				} finally {
	    			connection.close();
	 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_END); 
				}
 			}

			
		} catch (XASException e) { // Handled exceptions logger.debug
			logger.debug(sl.logIt(SmartLog.K_STATUS_CODE, e.getCode(), SmartLog.K_STATUS_MSG, MessageFormat.format(e.getMessage(), e.getParameters()), SmartLog.K_PARAMS, MessageFormat.format(e.getMessage(), e.getParameters())).getLogRow(true)); // Debug and keep row
			if (internalSmsResponse == null) { // If not present a response create a new, if present reuse it
				internalSmsResponse = new InternalSmsResponse(); 
			}
//			internalSmsResponse = new InternalSmsResponse(e.getCode(), MessageFormat.format(e.getMessage(), e.getParameters()), null, smsIdsSave);
			internalSmsResponse.setCode(e.getCode());
			internalSmsResponse.setMessage(MessageFormat.format(e.getMessage(), e.getParameters()));
		} catch (Exception e) { // Un-handled exceptions logger.error
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, ConstantsSms.XAS03099E_CODE, SmartLog.K_STATUS_MSG, MessageFormat.format(ConstantsSms.XAS03099E_MESSAGE, new String[]{e.getMessage()}), SmartLog.K_PARAMS, errors.toString()).getLogRow(true)); // Debug and keep row
			if (internalSmsResponse == null) { // If not present a response create a new, if present reuse it
				internalSmsResponse = new InternalSmsResponse(); 
			}
//			internalSmsResponse = new InternalSmsResponse(Constants.XAS00002E_CODE, MessageFormat.format(Constants.XAS00002E_MESSAGE, new String[]{errors.toString()}), null, smsIdsSave);
			internalSmsResponse.setCode(ConstantsSms.XAS00002E_CODE);
			internalSmsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00002E_MESSAGE, new String[]{errors.toString()}));
		} finally {
			chronometer.partial(ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_END);
			chronometer.stop();
			// Doing Analytics
			doAnalytic(myUUID, internalSmsRequest, xasUser, provider, originator, internalSmsResponse, chronometer); 
			if (logger.isDebugEnabled()) { 
				logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow()); // Debug and keep row
			}
		}
		internalSmsResponse.setUuid(myUUID);
		
		return internalSmsResponse;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#receiveTelecomDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport)
	 */
	public DeliveryResponse receiveTelecomDeliveryReport(DeliveryReport deliveryReport) throws RemoteException {
		InternalDeliveryReport internalDeliveryReport = new InternalDeliveryReport(deliveryReport);
		internalDeliveryReport.setChannel(getChannel());
		internalDeliveryReport.setInterfaceV1();
		// security
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MODR).add(SingleSecurityProfile.ADMIN));
		internalDeliveryReport.setUserId(getSupport().getPrincipal().getName());
		internalDeliveryReport.setUserInRole(new Boolean(isUserInRole));
		// service call
		internalDeliveryReport.setProviderType(it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.TYPE);
        return receiveNotification(internalDeliveryReport);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#receiveTelecomMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated)
	 */
	public DeliveryResponse receiveTelecomMobileOriginated(MobileOriginated mobileOriginated) throws RemoteException {
		InternalMobileOriginated internalMobileOriginated = new InternalMobileOriginated(mobileOriginated);
		internalMobileOriginated.setInterfaceV1();
		internalMobileOriginated.setChannel(getChannel());
		internalMobileOriginated.setUuid(mobileOriginated.getUuid());
		// security
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MODR).add(SingleSecurityProfile.ADMIN));
		internalMobileOriginated.setUserId(getSupport().getPrincipal().getName());
		internalMobileOriginated.setUserInRole(new Boolean(isUserInRole));
		// service call
		internalMobileOriginated.setProviderType(it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.TYPE);
 		return receiveNotification(internalMobileOriginated);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#receiveVodafonePopDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport)
	 */
	public DeliveryResponse receiveVodafonePopDeliveryReport(DeliveryReport deliveryReport) throws RemoteException {
		if (deliveryReport.getUuid() == null) { 
			// Fetch UUID from table for the corresponding deliveryReport.getSmsIds()
			deliveryReport.setUuid(UUID.randomUUID().toString());
		}
		InternalDeliveryReport internalDeliveryReport = new InternalDeliveryReport(deliveryReport);
		internalDeliveryReport.setChannel(getChannel());
		internalDeliveryReport.setInterfaceV1();
		// security
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MODR).add(SingleSecurityProfile.ADMIN));
		internalDeliveryReport.setUserId(getSupport().getPrincipal().getName());
		internalDeliveryReport.setUserInRole(new Boolean(isUserInRole));
		// service call
		internalDeliveryReport.setProviderType(it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE);
        return receiveNotification(internalDeliveryReport);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#receiveVodafonePopMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated)
	 */
	public DeliveryResponse receiveVodafonePopMobileOriginated(MobileOriginated mobileOriginated) throws RemoteException {
		InternalMobileOriginated internalMobileOriginated = new InternalMobileOriginated(mobileOriginated);
		internalMobileOriginated.setInterfaceV1();
		internalMobileOriginated.setChannel(getChannel());
		internalMobileOriginated.setUuid(mobileOriginated.getUuid());
		// security
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MODR).add(SingleSecurityProfile.ADMIN));
		internalMobileOriginated.setUserId(getSupport().getPrincipal().getName());
		internalMobileOriginated.setUserInRole(new Boolean(isUserInRole));
		// service call
		internalMobileOriginated.setProviderType(it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE);
 		return receiveNotification(internalMobileOriginated);
	}
	
  	//**** JMSMessageConsumer.java
 
	/**
	 * Write analytic row for each operation.
	 * @param internalRequest
	 * @param xasUser
	 * @param provider
	 * @param originator
	 * @param internalResponse
	 * @param chronometer
	 */
	private void doAnalytic(String myUUID, Object internalRequest, XasUser xasUser, Provider provider, Originator originator, Object internalResponse, Chronometer chronometer) {  		
		try {
			
			// Analytic[Identification]
			SmartAnalytic sa = new SmartAnalytic().collectItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_ANALYTIC_VER2, XasSendsmsServiceFacade.class.getName(), myUUID)
					.collectIt(SmartLog.K_METHOD,"doAnalytic");
	
			// Analytic[internalRequest] @ InternalSmsRequest
			if (internalRequest != null && InternalSmsRequest.class.isInstance(internalRequest)) {
				InternalSmsRequest internalSmsRequest = (InternalSmsRequest) internalRequest;
				sa.collectIt(
					  ConstantsSplunk.K_FUNCTION, ConstantsSplunk.V_FUNCTION_SENDSMS
					, ConstantsSplunk.K_CHANNEL, internalSmsRequest.getChannel()					
					, ConstantsSplunk.K_VERSION, Integer.toString(internalSmsRequest.getInterfaceVersion()) 
					, ConstantsSplunk.K_USERID, internalSmsRequest.getUserId()
					, ConstantsSplunk.K_USERINROLE, internalSmsRequest.getUserInRole().toString()
					, ConstantsSplunk.K_SRCXASUSER, internalSmsRequest.getSrcXasUserName()
					, ConstantsSplunk.K_SRCPHONENUM, internalSmsRequest.getSrcPhoneNumber()
					, ConstantsSplunk.K_SRCDELIVERY, internalSmsRequest.getSrcDelivery() != null ? ConstantsSms.ISO_DATE_FORMAT.format(internalSmsRequest.getSrcDelivery()) : ""
				    , ConstantsSplunk.K_SRCVALIDITY, internalSmsRequest.getSrcValidity() != null ? ConstantsSms.ISO_DATE_FORMAT.format(internalSmsRequest.getSrcValidity()) : ""
				    , ConstantsSplunk.K_SRCVALPERIOD, internalSmsRequest.getSrcValidityPeriod() != null ? internalSmsRequest.getSrcValidityPeriod().toString() : ""
				    , ConstantsSplunk.K_SRCMSGLENGTH, Integer.toString(internalSmsRequest.getSrcMsgLength())
					, ConstantsSplunk.K_DSTXASUSER, internalSmsRequest.getXasUserName()
					, ConstantsSplunk.K_DSTPHONENUM, internalSmsRequest.getPhoneNumber()
					, ConstantsSplunk.K_DSTDELIVERY, internalSmsRequest.getDeliveryTime() != null ? ConstantsSms.ISO_DATE_FORMAT.format(internalSmsRequest.getDeliveryTime()) : ""
				    , ConstantsSplunk.K_DSTVALIDITY, internalSmsRequest.getValidity() != null ? ConstantsSms.ISO_DATE_FORMAT.format(internalSmsRequest.getValidity()) : ""
					, ConstantsSplunk.K_DSTBYTELENGTH, Integer.toString(internalSmsRequest.getDstByteLength())
					, ConstantsSplunk.K_SMSENC, internalSmsRequest.getEncodingDescription()
					, ConstantsSplunk.K_CORRELATIONID, internalSmsRequest.getCorrelationID()
					);
				if (internalSmsRequest.getMsg() != null) {
					sa.collectIt(ConstantsSplunk.K_MSGHASHCODE, Integer.toString(internalSmsRequest.getMsg().hashCode()));
				}
				if (internalSmsRequest.getFirstNotGsm() > -1) {
					sa.collectIt(ConstantsSplunk.K_NOTGSMAT, Integer.toString(internalSmsRequest.getFirstNotGsm()), 
							     ConstantsSplunk.K_NOTGSMUNICODE, Integer.toHexString(internalSmsRequest.getMsg().charAt(internalSmsRequest.getFirstNotGsm())), 
							     ConstantsSplunk.K_NOTGSMCHAR, internalSmsRequest.getMsg().substring(internalSmsRequest.getFirstNotGsm(), internalSmsRequest.getFirstNotGsm() + 1)
								 );
				}
			}
	
	  		// Analytic[internalRequest] @ InternalDeliveryReport
			if (internalRequest != null && InternalDeliveryReport.class.isInstance(internalRequest)) {
				InternalDeliveryReport internalDeliveryReport = (InternalDeliveryReport) internalRequest;

				
		    	String[] xasStatus = xasDRstatus(internalDeliveryReport);
		    	String statusCode, statusMessage = null;
		    	if (ConstantsSplunk.V_SMSSTATUS_OK.equals(xasStatus[CustomizationBasic.IX_SMS_STATUS])) {
		    		statusCode = ConstantsSms.XAS00000I_CODE;
		    		statusMessage = ConstantsSms.XAS00000I_MESSAGE;
		    	} else {
		    		statusCode = ConstantsSms.XAS06019E_CODE;
		    		statusMessage = MessageFormat.format(ConstantsSms.XAS06019E_MESSAGE, new String[]{xasStatus[CustomizationBasic.IX_ERROR_MESSAGE]});
		    	}
		    	String deliveryDate = "", providerDate = "";
		    	if (internalDeliveryReport.getDeliveryReport() != null) {
		    		if (internalDeliveryReport.getDeliveryReport().getDeliveryDate() != null)
			    		deliveryDate = ConstantsSms.ISO_DATE_FORMAT.format(internalDeliveryReport.getDeliveryReport().getDeliveryDate().getTime());
			    	if (internalDeliveryReport.getDeliveryReport().getProviderDate() != null)
			    		providerDate = ConstantsSms.ISO_DATE_FORMAT.format(internalDeliveryReport.getDeliveryReport().getProviderDate().getTime());
		    	}
				sa.collectIt(
					  ConstantsSplunk.K_FUNCTION, ConstantsSplunk.V_FUNCTION_DELIVERYREQUEST
					, ConstantsSplunk.K_CHANNEL, internalDeliveryReport.getChannel()					
					, ConstantsSplunk.K_VERSION, internalDeliveryReport.isInferfaceV1() ? "1" : "unknow"
					, ConstantsSplunk.K_USERID, internalDeliveryReport.getUserId()
					, ConstantsSplunk.K_USERINROLE, internalDeliveryReport.getUserInRole().toString()
					, ConstantsSplunk.K_PROVIDER_TYPE, internalDeliveryReport.getProviderType()
					, ConstantsSplunk.K_DSTPHONENUM, internalDeliveryReport.getPhoneNumber()
					, ConstantsSplunk.K_SMSID, internalDeliveryReport.getSmsIds() != null ? XConstants.XSTREAMER.toXML(internalDeliveryReport.getSmsIds()) : "" 
					, ConstantsSplunk.TMP_SMSGATEWAYRESP, xasStatus[CustomizationBasic.IX_GATEWAY_RESPONSE]
					, ConstantsSplunk.TMP_ERROR_MESSAGE, xasStatus[CustomizationBasic.IX_ERROR_MESSAGE]
				    , ConstantsSplunk.K_SMSSTATUS, xasStatus[CustomizationBasic.IX_SMS_STATUS]
					, SmartLog.K_STATUS_CODE, statusCode
					, SmartLog.K_STATUS_MSG, statusMessage
					, ConstantsSplunk.K_ACCEPTED, internalDeliveryReport.getAccepted().toString()
					, ConstantsSplunk.K_DSTXASUSER, xasUser != null ? xasUser.getName() : ""
 				    , ConstantsSplunk.K_END_POINT, xasUser != null ? stringValue(xasUser.getEndPoint()) : "" 
 	 				, ConstantsSplunk.K_DELIVERYDATE, deliveryDate 
 	 				, ConstantsSplunk.K_PROVIDERDATE, providerDate 
					);
			}

	  		// Analytic[internalRequest] @ InternalMobileOriginated
			if (internalRequest != null && InternalMobileOriginated.class.isInstance(internalRequest)) {
				InternalMobileOriginated internalMobileOriginated = (InternalMobileOriginated) internalRequest;
		    	String moDate = "", providerDate = "";
		    	if (internalMobileOriginated.getMobileOriginated() != null) {
		    		if (internalMobileOriginated.getMobileOriginated().getMoDate() != null)
			    		moDate = ConstantsSms.ISO_DATE_FORMAT.format(internalMobileOriginated.getMobileOriginated().getMoDate().getTime());
			    	if (internalMobileOriginated.getMobileOriginated().getProviderDate() != null)
			    		providerDate = ConstantsSms.ISO_DATE_FORMAT.format(internalMobileOriginated.getMobileOriginated().getProviderDate().getTime());
		    	}
				sa.collectIt(
					  ConstantsSplunk.K_FUNCTION, ConstantsSplunk.V_FUNCTION_MOBILEORIGINATED
					, ConstantsSplunk.K_CHANNEL, internalMobileOriginated.getChannel()					
					, ConstantsSplunk.K_VERSION, internalMobileOriginated.isInferfaceV1() ? "1" : "unknow"
					, ConstantsSplunk.K_USERID, internalMobileOriginated.getUserId()
					, ConstantsSplunk.K_USERINROLE, internalMobileOriginated.getUserInRole().toString()
					, ConstantsSplunk.K_PROVIDER_TYPE, internalMobileOriginated.getProviderType()
					, ConstantsSplunk.K_DSTPHONENUM, internalMobileOriginated.getPhoneNumber()
					, ConstantsSplunk.K_SMSID, internalMobileOriginated.getSmsIds() != null ? XConstants.XSTREAMER.toXML(internalMobileOriginated.getSmsIds()) : "" // Sovrascritto se presente da internalSmsResponse
					, ConstantsSplunk.K_DSTXASUSER, xasUser != null ? xasUser.getName() : ""
					, ConstantsSplunk.K_DSTBYTELENGTH, Integer.toString(internalMobileOriginated.getMsgByteLength())
					, ConstantsSplunk.K_ACCEPTED, internalMobileOriginated.getAccepted().toString()
 	 				, ConstantsSplunk.K_MODATE, moDate 
 	 				, ConstantsSplunk.K_PROVIDERDATE, providerDate 
				);
	
				if (internalMobileOriginated.getMsg() != null) {
					sa.collectIt(ConstantsSplunk.K_MSGHASHCODE, Integer.toString(internalMobileOriginated.getMsg().hashCode()));
				}
				// Collect moDestination for MO
				if (internalMobileOriginated.getMoDestinator() != null) {
					sa.collectIt(
	 					   ConstantsSplunk.K_END_POINT, xasUser != null ? stringValue(xasUser.getEndPoint()) : "" 
						 , ConstantsSplunk.K_MO_DESTINATOR , internalMobileOriginated.getMoDestinator()
							);
				}
			}

	
			// Analytic[XasUser]
			if (xasUser != null) {
				sa.collectIt(
					  ConstantsSplunk.K_DEF_VAL_PER, xasUser.getDefaultValidityPeriod() != null ? xasUser.getDefaultValidityPeriod().toString() : ""
					, ConstantsSplunk.K_DEF_REGION, stringValue(xasUser.getDefaultRegion())
					);
			}

			// Analytic[Provider]
			if (provider != null) {
				sa.collectIt(
						ConstantsSplunk.K_PROVIDER, provider.getName()
					  , ConstantsSplunk.K_PROVIDER_TYPE, provider.getType()
					);
			}
	
			// Analytic[Originator]
			if (originator != null) {
				sa.collectIt(
					  ConstantsSplunk.K_ORIGNAME, originator.getName()
					, ConstantsSplunk.K_ROUTERREGEX, (originator.getRouterRegex() != null) ? originator.getRouterRegex().pattern() : ""
					, ConstantsSplunk.K_ORIGINATOR, originator.getOriginator()
					, ConstantsSplunk.K_TYPEOFNUMBER, originator.getTypeOfNumber().toString()
					, ConstantsSplunk.K_NUMPLANID, originator.getNumPlanId().toString()
					);
	 		}
	
			// Analytic[Response] @ InternalSmsResponse
			if (internalResponse != null && InternalSmsResponse.class.isInstance(internalResponse)) {
				InternalSmsResponse internalSmsResponse = (InternalSmsResponse) internalResponse;
				if (internalSmsResponse != null && internalSmsResponse.getSmsResponse() != null) {
					if (internalSmsResponse.getSmsResponse().isOk()) {
						sa.collectIt(
							  SmartLog.K_STATUS_CODE, ConstantsSms.XAS00000I_CODE
						    , SmartLog.K_STATUS_MSG, internalSmsResponse.getMessage()
						    , ConstantsSplunk.K_SMSSTATUS, ConstantsSplunk.V_SMSSTATUS_OK
						    );
					} else {
						sa.collectIt(
							  SmartLog.K_STATUS_CODE, internalSmsResponse.getCode()
							, SmartLog.K_STATUS_MSG, internalSmsResponse.getMessage()
							, ConstantsSplunk.K_SMSSTATUS, ConstantsSplunk.V_SMSSTATUS_ERROR
							);
						
					}
					sa.collectIt(
						  ConstantsSplunk.K_SMSID, internalSmsResponse.getSmsResponse().getSmsIds() != null ? XConstants.XSTREAMER.toXML(internalSmsResponse.getSmsResponse().getSmsIds()) : ""
						, ConstantsSplunk.K_TOTALSMS, Integer.toString(internalSmsResponse.getTotalSms())
						, ConstantsSplunk.K_GATEWAYCONTACTED, String.valueOf(internalSmsResponse.getGatewayContacted())
						);
				}
			}
	
			// Analytic[Response] @ InternalDeliveryResponse
			if (internalResponse != null && InternalDeliveryResponse.class.isInstance(internalResponse)) {
				InternalDeliveryResponse internalDeliveryResponse = (InternalDeliveryResponse) internalResponse;
				if (internalDeliveryResponse != null && internalDeliveryResponse.getDeliveryResponse() != null) {
					if (internalDeliveryResponse.getDeliveryResponse().isOk()) {
						sa.collectIt(
							  SmartLog.K_STATUS_CODE, internalDeliveryResponse.getCode()
						    , SmartLog.K_STATUS_MSG, internalDeliveryResponse.getMessage()
						    , ConstantsSplunk.K_SMSSTATUS, ConstantsSplunk.V_SMSSTATUS_OK
						    );
					} else {
						String[] messageRows = internalDeliveryResponse.getMessage() != null ? internalDeliveryResponse.getMessage().split("\\n|\\r") : new String[]{""};
						String firstRow = messageRows.length > 0 ? messageRows[0].replaceAll("\\n|\\r","") : "";
						sa.collectIt(
							  SmartLog.K_STATUS_CODE, internalDeliveryResponse.getCode()
							, SmartLog.K_STATUS_MSG,  firstRow
							, ConstantsSplunk.K_SMSSTATUS, ConstantsSplunk.V_SMSSTATUS_ERROR
							);
						
					}
					sa.collectIt(
						  ConstantsSplunk.K_SMSID, internalDeliveryResponse.getDeliveryResponse().getSmsIds() != null ? XConstants.XSTREAMER.toXML(internalDeliveryResponse.getDeliveryResponse().getSmsIds()) : ""
						);
				}
			}
	
			if (chronometer != null) {
				sa.collectIt(SmartAnalytic.K_TIME_ELAPSED , it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chronometer.getElapsed())
						   , SmartAnalytic.K_TIME_TABLE, chronometer.toString());
			}
			
			logger.info(sa.getAnalyticRow());
	    } catch (SLF_Exception e) {
	    	// Handling Analytics error
			e.printStackTrace(System.err);
			logger.error("SmartLogException while handling Exception: " + e.getMessage());
			SmartLog slException = new SmartLog(SmartLog.COUPLING_LOOSE_I)
	    		.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
	    		.logIt(SmartLog.K_METHOD, "doAnalytic"); 
			slException.logIt(SmartLog.K_SCOPE, SmartLog.V_SCOPE_DEBUG);
			String[] messageRows = e.getMessage() != null ? e.getMessage().split("\\n|\\r") : new String[]{""};
			String firstRow = messageRows.length > 0 ? messageRows[0].replaceAll("\\n|\\r","") : "";
 			slException.logIt(SmartLog.K_STATUS_MSG, firstRow);
			logger.error(slException.getLogRow(), e);
	    }
	}

	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade#xasAppNotify(java.lang.String, java.lang.Object)
	 */
	public NotificationResponse xasAppNotify(String myUUID, Object object) throws RemoteException {
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
		.logIt(SmartLog.K_METHOD, "xasAppNotify", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).preset("default"); 
		
		logger.info(sl.reload("default").logIt(SmartLog.K_STEP, "displayParams", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(object)).getLogRow());

		// Security Validation
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.UNAUTHENTICATED));
		if(!isUserInRole)
		{
			// TODO
		}
	
		NotificationResponse notificationResponse = new NotificationResponse();
		notificationResponse.setStatusCode(StatusCodeType.XAS00080I.getValue());
		notificationResponse.setStatusMsg(ConstantsSms.XAS06080I_MESSAGE);

		String emailNotificationAddress = null;
		try {
			emailNotificationAddress = Configuration.getInstance().getEmailNotification();
            if (emailNotificationAddress != null) {
				// SEND an Email need authentication to send email
				EmailMessage emailMessage = new EmailMessage();
				emailMessage.setHtml(true);
				emailMessage.setMailFrom(ConstantsSms.XAS_MAIL_SENDER);
				emailMessage.setMailTo(emailNotificationAddress);
				if (object instanceof MobileOriginated) {
					MobileOriginated mobileOriginated = (MobileOriginated) object;
					emailMessage.setMailSubject("XAS - Mobile Originated");
					emailMessage.setMailMessage("<body>"
											+ "MO message from " + mobileOriginated.getPhoneNumber() + " to " + mobileOriginated.getMoDestinator() 
											+ "<br/>Msg:" + mobileOriginated.getMsg() 
											+ "<br/>SmsId:" + XConstants.XSTREAMER.toXML(mobileOriginated.getSmsIds())
											+ "<br/>UuId:" + mobileOriginated.getUuid()
											+ "<br/>MoDate: " + (mobileOriginated.getMoDate() != null ? ConstantsSms.ISO_DATE_FORMAT.format(mobileOriginated.getMoDate().getTime()) : "")
											+ "<br/>ProviderDate: " + (mobileOriginated.getProviderDate() != null ? ConstantsSms.ISO_DATE_FORMAT.format(mobileOriginated.getProviderDate().getTime()) : "")
											+ "</body>"
											);
					String CMD_RETURN = "return", CMD_SLEEP = "sleep";
					String cmd = mobileOriginated.getMsg() != null ? mobileOriginated.getMsg() : "";
					String[] param = cmd.split(" ");
					if (param[0].equals(CMD_SLEEP)) {
					    try {
					    	int sleepInt = 2000;
					    	try {
					    		sleepInt = Integer.parseInt(param[1]);
					    	} catch (Exception e) {
					    	}
					    	logger.debug("sleep begin: " + param[1] + "->" + sleepInt);
					        Thread.sleep(sleepInt);
					    	logger.debug("sleep end");
					    } catch (InterruptedException e) {
					        e.printStackTrace();
					    }
					} else if (param[0].equals(CMD_RETURN)) {
							if (param.length > 1) notificationResponse.setStatusCode(param[1]);
							if (param.length > 2) notificationResponse.setStatusMsg(cmd.substring(CMD_RETURN.length() + param[1].length() + 2));
					    	logger.debug("MO returning: " + cmd);
					}


				} else if (object instanceof DeliveryReport) {
					DeliveryReport deliveryReport = (DeliveryReport) object;
					emailMessage.setMailSubject("XAS - Delivery Report " + deliveryReport.getUuid() + " " + deliveryReport.getStatus().getValue());
					emailMessage.setMailMessage("<body>"
											+ "DR message from " + deliveryReport.getPhoneNumber() 
											+ "<br/>SmsId: " + XConstants.XSTREAMER.toXML(deliveryReport.getSmsIds())
											+ "<br/>Uuid: " + deliveryReport.getUuid()
											+ "<br/>Status: " + deliveryReport.getStatus().getValue()
											+ "<br/>DeliveryDate: " + (deliveryReport.getDeliveryDate() != null ? ConstantsSms.ISO_DATE_FORMAT.format(deliveryReport.getDeliveryDate().getTime()) : "")
											+ "<br/>ProviderDate: " + (deliveryReport.getProviderDate() != null ? ConstantsSms.ISO_DATE_FORMAT.format(deliveryReport.getProviderDate().getTime()) : "")
											+ "</body>"
											);
					String PHONE_RETURN81 = "395550000081", PHONE_RETURN82 = "395550000082", PHONE_SLEEP2 = "395550000002", PHONE_SLEEP4 = "395550000004";
					String cmd = deliveryReport.getPhoneNumber() != null ? deliveryReport.getPhoneNumber() : "";
					if (cmd.equals(PHONE_SLEEP2)) {
					    try {
					    	int sleepInt = 2000;
					    	logger.debug("sleep begin: " +  sleepInt);
					        Thread.sleep(sleepInt);
					    	logger.debug("sleep end");
					    } catch (InterruptedException e) {
					        e.printStackTrace();
					    }
					} else if (cmd.equals(PHONE_SLEEP4)) {
					    try {
					    	int sleepInt = 4000;
					    	logger.debug("sleep begin: " +  sleepInt);
					        Thread.sleep(sleepInt);
					    	logger.debug("sleep end");
					    } catch (InterruptedException e) {
					        e.printStackTrace();
					    }
					} else if (cmd.equals(PHONE_RETURN81)) {
							notificationResponse.setStatusCode("XAS00081E");
							notificationResponse.setStatusMsg("DR testing error with retry");
					    	logger.debug("MO returning: " + cmd);
					} else if (cmd.equals(PHONE_RETURN82)) {
						notificationResponse.setStatusCode("XAS00082E");
						notificationResponse.setStatusMsg("DR testing fatal error with no-retry");
				    	logger.debug("MO returning: " + cmd);
					}
				}
					
	            SendEmail.getInstance().sendMessage(emailMessage, null, "XA-XAS", true);
            }
        } catch (XASException e) {
            throw new java.rmi.RemoteException("XASException:" + e.getCode() + " " + e.getMessage(), e.getCause());
        }
		logger.info(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(notificationResponse)).getLogRow());
        
		return notificationResponse;
	    
    }

	// OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD 
	// OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD 
	// OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD OLD 

	/**
	 * Get JSON representation of current configuration and JNDI names.
	 */
	public String getConfiguration(String params) throws RemoteException {
		String userid = getSupport().getPrincipal().getName();
		if(!SecurityUtility.isUserInRole(getSupport(), SingleSecurityProfile.AUTHENTICATED))
		{
			//TODO
		}

		String[] param = params.split(",");
		boolean bFile = false, bCache = false, bJson = false, bXml = false, bPretty = false, bTest = false;
		String fileName = null;
		for (int i = 0; i < param.length; i++) {
			if ("file".equals(param[i].toLowerCase())) bFile = true;
			if ("cache".equals(param[i].toLowerCase())) bCache = true;
			if ("json".equals(param[i].toLowerCase())) bJson = true;
			if ("xml".equals(param[i].toLowerCase())) bXml = true;
			if ("pretty".equals(param[i].toLowerCase())) bPretty = true;
			if ("test".equals(param[i].toLowerCase())){ bTest = true; fileName = param[1]; }
		}
		// Print pretty configuration
		String stringConfig = "";
		if (bCache) {
 			LinkedHashMap hm = new LinkedHashMap();
			try {
				try {
					BaselineInfo baselineInfo = BaselineInfoFactory.getInstance().getInfo("xas");
					hm.put("BaselineInfo", baselineInfo.getModel() + " - " + baselineInfo.getTarget() + " - " + baselineInfo.getVersion() + " - " +baselineInfo.getDate());
				} catch (Throwable e) {
				// Catch everything in baselineinfo error
					logger.error("Can't read baselineinfo");
					hm.put("BaselineInfo", "Can't read baselineinfo");
				}
				try {
					Connection connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
					DatabaseMetaData metadata = connection.getMetaData();
					hm.put("DatabaseMetaData", ConstantsSms.DB_JNDI + " - " + metadata.getURL() + " - " + metadata.getUserName() + " - DatabasePresent:" + GatewayA2pSmsFactory.getInstance().getConfiguration().isDatabasePresent());
					connection.close();
				} catch (Throwable e) {
				// Catch everything in baselineinfo error
					hm.put("DatabaseMetaData", "Can't read DatabaseMetaData - DatabasePresent:" + GatewayA2pSmsFactory.getInstance().getConfiguration().isDatabasePresent());
					if (GatewayA2pSmsFactory.getInstance().getConfiguration().isDatabasePresent()) { // Log error only if database is required
						logger.error("Can't read DatabaseMetaData");
					} else {
						logger.info("Can't read DatabaseMetaData");
					}
				}
				try {
					String currentEnvironment = ((IEnvironment) EnvironmentLoader.getDefault()).get(IEnvironment.TOWER);
					hm.put("EnvironmentInfo", IEnvironment.TOWER + ":" + currentEnvironment);
				} catch (Throwable e) {
					// Catch everything in baselineinfo error
						logger.error("Can't read EnvironmentInfo");
						hm.put("EnvironmentInfo", "Can't read EnvironmentInfo");
				}
				hm.put(it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.JNDI_POP_SOAP_URL, it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.GatewaySmsProvider.getVodafonePopURL());
				hm.put(it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.JNDI_POP_SOAP_URL,it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.GatewaySmsProvider.getTelecomPopURL());
				hm.put(it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.JNDI_SMS_CONFIG, it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.getConfigFileName());
				hm.put("Configuration",GatewayA2pSmsFactory.getInstance().getConfiguration());
	        } catch (XASException e) {
	        	throw new RemoteException("XASException:" + MessageFormat.format(e.getMessage(), e.getParameters()));
	        }
	        if (bJson) {
				XStream xstreamer = null;
				if (bPretty)
					xstreamer = new XStream(new JsonHierarchicalStreamDriver() { 
				       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
				         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
	                              			  new JsonWriter.Format(XConstants.XSTREAMER_PRETTY_DELIMITER, XConstants.XSTREAMER_PRETTY_NEWLINE,JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
				         } 
				     });
				else xstreamer = new XStream(new JsonHierarchicalStreamDriver() { 
				       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
					         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
		                              new JsonWriter.Format(XConstants.XSTREAMER_DELIMITER, XConstants.XSTREAMER_NEWLINE,JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
					         } 
					     });
				xstreamer.alias("XasUser", it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser.class);
				stringConfig = xstreamer.toXML(hm);
				/* Parsable in splunk with:
				XAS a_configuration  
					| spath input=f output=xasUser {2}{}.xasUsers{}.XasUser 
					| mvexpand xasUser 
					| spath input=xasUser 
					| table name interfaceVersion originator provider defaultForceAsciiEncoding defaultRegion useBackup validatePhoneNumberRegEx.pattern 
				*/
	        } else if (bXml){
	        	stringConfig ="Unsupported format";
	        }
		} else if (bFile) {
			try {
				BufferedReader bufferedReader;
				String line = null;
	            bufferedReader = new BufferedReader(
	                								new InputStreamReader(
	                								new FileInputStream(it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.getConfigFileName())));
	            while ((line = bufferedReader.readLine()) != null) {
	            		if (bPretty) {
	            			stringConfig += line + "\r\n";
	            		} else
	    	            	stringConfig += line.replaceAll("[\t\r\n]","");
	
	            	}
	        	bufferedReader.close();
            } catch (Exception e) {
                stringConfig = "error reading: " + e.getMessage();
            } 
		} else if (bTest)
	        try {
				XStream xstreamer = null;
					xstreamer = new XStream(new JsonHierarchicalStreamDriver() { 
				       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
				         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
	                              			  new JsonWriter.Format(XConstants.XSTREAMER_PRETTY_DELIMITER, XConstants.XSTREAMER_PRETTY_NEWLINE,JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
				         } 
				     });
				xstreamer.alias("XasUser", it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser.class);
	        	it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration config = GatewayA2pSmsFactory.getInstance().getConfiguration(); 
	            config.testLoad(fileName);
				stringConfig = xstreamer.toXML(config);
            } catch (XASException e) {
	            stringConfig = "Test:" + fileName + " " + e.getCode() + " " + MessageFormat.format(e.getMessage(), e.getParameters());
            }
        else stringConfig ="Should choose type: cache or file";
		
        return stringConfig;
    }
	
	//#TIM
	public DeliveryResponse receiveNotification(InternalDeliveryReport internalDeliveryReport) {

		internalDeliveryReport.setAccepted(Boolean.FALSE);
		
		if (internalDeliveryReport.getDeliveryReport().getUuid() != null) {
			MDC.put(ConstantsSms.MY_UUID_KEY, internalDeliveryReport.getDeliveryReport().getUuid());
		}
  		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
  			
		Chronometer chronometer = new Chronometer(true, ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_START + "@" + ConstantsSms.MY_APPL_ID + " " + " MY_UUID:" + myUUID);

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
	    	.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "receiveNotification(deliveryReport)").preset("default"); 

 		XasUser xasUser = null;
 		InternalDeliveryResponse internalDeliveryResponse = null;
 		DeliveryResponse deliveryResponse = new DeliveryResponse();
 		try {
			// Security Validation
 			if(internalDeliveryReport.getUserInRole().booleanValue()==false)
				throw new XASException(ConstantsSms.XAS05014E_MESSAGE, null, ConstantsSms.XAS05014E_CODE, new Object[] {internalDeliveryReport.getUserId(), SingleSecurityProfile.MODR.toString()});
 			
 			if (it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.getInstance().isDatabasePresent()) {
				// get xasUser for DeliveryReport message
				if (internalDeliveryReport.getSmsIds() != null) {
					String xasUserName = null;
					String[] smsIds = null;
	 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_WAIT); 
 					Connection connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
 	 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_START); 
					try {
			 			logger.debug(sl.logIt(K_STEP, "tableFetchDeliveryReport", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(internalDeliveryReport)).getLogRow(true)); // Debug and keep row
		// 			*** MsgUuid row table lock region BEGIN
						TableManager.TableInfo fetchedValues = tableFetchDeliveryReport(myUUID, connection, internalDeliveryReport);
 						xasUserName = fetchedValues.getXasUserName();
						smsIds = fetchedValues.getListId();
						ResultSet rsLockRegion = fetchedValues.getRsLock();	// For Update
						try {
							PreparedStatement stmtLockRegion = fetchedValues.getStmtLock(); // For Update
							try {
								if (xasUserName == null) {
									tableUpdateDeliveryReport(myUUID, connection, internalDeliveryReport,  false);
									// Set the delivery response to the sms has been successfully sent registering the status in the table
									deliveryResponse = new DeliveryResponse(ConstantsSms.XAS00000I_CODE, ConstantsSms.XAS00000I_MESSAGE);
									deliveryResponse.setSmsIds(internalDeliveryReport.getDeliveryReport().getSmsIds());
									internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
								} 
							} finally {
								stmtLockRegion.close();		// ForUpdate
							} 
						} finally {
							rsLockRegion.close();       // ForUpdate
						} 
		//			*** MsgUuid row table lock region END
					} finally {
						connection.close();
		 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_END); 
  					}
					
					if (xasUserName != null) {  // Last multipart delivery report received, deliver to the application
			 			logger.debug(sl.logIt(K_STEP, "getFinalXasUser", SmartLog.K_PARAMS, xasUserName).getLogRow(true)); // Debug and keep row
						internalDeliveryReport.getDeliveryReport().setSmsIds(smsIds);
						xasUser = GatewayA2pSmsFactory.getInstance().getConfiguration()
								.getFinalXasUser(myUUID, xasUserName, false);
						if (xasUser != null) { // if defined call WS endPoint
							logger.debug(sl.logIt(K_STEP, "sendDeliveryReport EndPoint", SmartLog.K_PARAMS, xasUser.getEndPoint()).getLogRow(true)); // Debug and keep row
							
							internalDeliveryResponse = (new ApplicationGateway()).sendDeliveryReport(xasUser, internalDeliveryReport, internalDeliveryResponse, chronometer);
							
							deliveryResponse = internalDeliveryResponse.getDeliveryResponse();
							if (internalDeliveryResponse != null 
									&& (StatusCodeType._XAS00080I.equals(internalDeliveryResponse.getWsEndpointSC()) || StatusCodeType._XAS00082E.equals(internalDeliveryResponse.getWsEndpointSC())) ) {
									// If success of fatal do not retry delivery
								internalDeliveryReport.setAccepted(Boolean.TRUE);
								}
							if (ConstantsSms.XAS06080I_CODE.equals(internalDeliveryResponse.getWsEndpointSC())
								|| ConstantsSms.XAS06082E_CODE.equals(internalDeliveryResponse.getWsEndpointSC())) { 
								// Delivery report successfully delivered to the application (or fatal error)
					 			logger.debug(sl.reload("defaul").logIt(K_STEP, "tableUpdateDeliveryReport", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(internalDeliveryReport.getSmsIds())).getLogRow(true)); // Debug and keep row
				 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_START); 
 								connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
								try {
									tableUpdateDeliveryReport(myUUID, connection, internalDeliveryReport,  true);

									logger.debug(sl.reload("defaul").logIt(K_STEP, "tableUpdateDRstats","a_internalMobileOriginated", XConstants.XSTREAMER.toXML(internalDeliveryReport)).getLogRow(true)); // Debug and keep row
									tableUpdateDRstats(myUUID, connection, xasUser.getProvider(), xasUser, internalDeliveryReport.getDeliveryReport());
						    			
								} finally {
									connection.close();
					 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_END); 
 								}
							}
		 	 			} else {
							deliveryResponse = new DeliveryResponse(ConstantsSms.XAS03099E_CODE, ConstantsSms.XAS03099E_MESSAGE, "Missing xasUserName:" + xasUserName);
							deliveryResponse.setSmsIds(smsIds);
							internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
							internalDeliveryResponse.setWsEndpointSC(ConstantsSms.XAS06081E_CODE);
							internalDeliveryResponse.setWsEndpointSM(ConstantsSms.XAS06081E_MESSAGE);
			 			}
					} 
				}
 			} else { // DATABASE not present
 				internalDeliveryReport.setAccepted(Boolean.TRUE);
		    	String[] xasStatus = xasDRstatus(internalDeliveryReport);
		    	if (ConstantsSplunk.V_SMSSTATUS_OK.equals(xasStatus[CustomizationBasic.IX_SMS_STATUS])) {
					deliveryResponse = new DeliveryResponse(ConstantsSms.XAS00000I_CODE, ConstantsSms.XAS00000I_MESSAGE);
		    	} else {
					deliveryResponse = new DeliveryResponse(ConstantsSms.XAS06019E_CODE, MessageFormat.format(ConstantsSms.XAS06019E_MESSAGE, new String[]{xasStatus[CustomizationBasic.IX_ERROR_MESSAGE]}));
		    	}
				deliveryResponse.setSmsIds(internalDeliveryReport.getSmsIds());
				internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
 			}

		} catch (XASException e) { // Handled exceptions logger.debug
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, e.getCode(), SmartLog.K_STATUS_MSG, MessageFormat.format(e.getMessage(), e.getParameters()), SmartLog.K_PARAMS, e.getClass().getName()).getLogRow(true), new XASException(MessageFormat.format(e.getMessage(), e.getParameters()), e.getCause()));
			deliveryResponse.setCode(e.getCode());
			deliveryResponse.setMessage(MessageFormat.format(e.getMessage(), e.getParameters()));
			internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
			internalDeliveryResponse.setWsEndpointSC(ConstantsSms.XAS06081E_CODE);
			internalDeliveryResponse.setWsEndpointSM(ConstantsSms.XAS06081E_MESSAGE);
		} catch (Exception e) { // Un-handled exceptions logger.error
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
	    	logger.error(errors.toString());
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, ConstantsSms.XAS03099E_CODE, SmartLog.K_STATUS_MSG, MessageFormat.format(ConstantsSms.XAS03099E_MESSAGE, new String[]{e.getMessage()}), SmartLog.K_PARAMS, e.getClass().getName()).getLogRow(true), e);
			deliveryResponse.setCode(ConstantsSms.XAS00002E_CODE);
			deliveryResponse.setMessage(errors.toString());
			internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
			internalDeliveryResponse.setWsEndpointSC(ConstantsSms.XAS06081E_CODE);
			internalDeliveryResponse.setWsEndpointSM(ConstantsSms.XAS06081E_MESSAGE);
		} finally {
			chronometer.partial(ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_END);
			chronometer.stop();
			// Doing Analytics
            doAnalytic(myUUID, internalDeliveryReport, xasUser, null, null, internalDeliveryResponse, chronometer);

            if (logger.isDebugEnabled()) { 
				logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, XConstants.XSTREAMER.toXML(deliveryResponse)).getLogRow()); // Debug and keep row
			}
		}  
 		
		return deliveryResponse;
    }

	/**
	 * Store the sms in the DB waiting for the Delivery Report.
	 * @param internalSmsRequest
	 * @param internalSmsResponse
	 * @throws SQLException 
	 * @throws NamingException 
	 * @throws XASException 
	 */
	private void tableInsertDRrequest(String myUUID, Connection connection, InternalSmsRequest internalSmsRequest, InternalSmsResponse internalSmsResponse) throws SQLException {
		Date validity = internalSmsRequest.getValidity();
		if (validity == null) {
			validity = new Date();
			Calendar cal = Calendar.getInstance();
	        cal.setTime(validity);
	        cal.add(Calendar.DATE, ConstantsSms.PROVIDER_DEFAULT_VALIDITY_DAYS + 1); // One day more than provider default validity days.
			validity = cal.getTime();
		}
		TableManager.insertDRrequest(myUUID, connection, internalSmsRequest.getUuid(), internalSmsRequest.getPhoneNumber(), internalSmsResponse.getSmsIds(), internalSmsRequest.getXasUserName(), validity);
    }

	/**
	 * Retrieve the xasUser for the corresponding messageId.
	 * @param internalDeliveryReport
	 * @return
	 * @throws NamingException 
	 * @throws XASException 
	 */
	private TableManager.TableInfo tableFetchDeliveryReport(String myUUID, Connection connection, InternalDeliveryReport internalDeliveryReport) throws SQLException {
	    return TableManager.fetchDeliveryReport(myUUID, connection, internalDeliveryReport.getUuid(), internalDeliveryReport.getSmsIds()[0], internalDeliveryReport.getStatus());
    }

	/**
	 * Retrieve the xasUser for the corresponding messageId.
	 * @param internalDeliveryReport
	 * @return
	 * @throws NamingException 
	 * @throws XASException 
	 */
	private void tableUpdateDeliveryReport(String myUUID, Connection connection, InternalDeliveryReport internalDeliveryReport, boolean delete) throws SQLException {
	    TableManager.updateDeliveryReport(myUUID, connection, internalDeliveryReport.getUuid(), internalDeliveryReport.getSmsIds()[0], internalDeliveryReport.getStatus(), delete);
    }

	static SimpleDateFormat RANGE_FORMAT = new SimpleDateFormat("yyyy-MM");

	static Pattern INTERNATIONAL_PREFIX;
	static {
		INTERNATIONAL_PREFIX = Pattern.compile("^(" +
		        	    "1" // North America 
				+ "|" + "2" // Africa 

				+ "|" + "30" // Greece
			    + "|" + "31" // Netherlands
			    + "|" + "32" // Belgium
			    + "|" + "33" // France
			    + "|" + "34" // Spain
			    + "|" + "350" // Gibraltar
			    + "|" + "351" // Portugal
			    + "|" + "352" // Luxembourg
			    + "|" + "353" // Ireland
			    + "|" + "354" // Iceland
			    + "|" + "355" // Albania
			    + "|" + "356" // Malta
			    + "|" + "357" // Cyprus
			    + "|" + "358" // Finland
			    + "|" + "359" // Bulgaria
			    + "|" + "36" // Hungary
//			    + "|" + "37" // Discontinued (was assigned to the  German Democratic Republic. See Germany's country code 49)
			    + "|" + "370" // Lithuania
			    + "|" + "371" // Latvia
			    + "|" + "372" // Estonia
			    + "|" + "373" // Moldova
			    + "|" + "374" // Armenia
			    + "|" + "375" // Belarus
			    + "|" + "376" // Andorra (formerly + "|" + "33 628)
			    + "|" + "377" // Monaco (formerly + "|" + "33 93)
			    + "|" + "378" // San Marino (formerly + "|" + "39 549)
			    + "|" + "379" //  Vatican City assigned but uses Italian + "|" + "39 06698.
//			    + "|" + "38"  // Discontinued (was assigned to  Socialist Federal Republic of Yugoslavia until its break-up)
			    + "|" + "380" // Ukraine (formerly used by  SFR Yugoslavia)
			    + "|" + "381" // Serbia (formerly used by  FR Yugoslavia and  Serbia and Montenegro before Montenegro's independence)
			    + "|" + "382" // Montenegro
			    + "|" + "383" // Kosovo[a] (Assigned in 2016, not yet in use)
//			    + "|" + "384" // unassigned
			    + "|" + "385" // Croatia
			    + "|" + "386" // Slovenia
			    + "|" + "387" // Bosnia and Herzegovina
//			    + "|" + "388" // Discontinued (was assigned to the European Telephony Numbering Space)[1][2]
			    + "|" + "389" // Macedonia
			    + "|" + "39" // Italy
//		        + "|" + "39 06 698" //  Vatican City (assigned + "|" + "379 but not in use)
			    + "|" + "40" // Romania
			    + "|" + "41" // Switzerland
//			    + "|" + "42" // Discontinued (was assigned to  Czechoslovakia until its breakup)
			    + "|" + "420" // Czech Republic
			    + "|" + "421" // Slovakia
			    + "|" + "423" // Liechtenstein (formerly + "|" + "41 75)
			    + "|" + "43" // Austria
			    + "|" + "44" // United Kingdom
			    + "|" + "45" // Denmark
			    + "|" + "46" // Sweden
			    + "|" + "47" // Norway
			    + "|" + "48" // Poland
			    + "|" + "49" // Germany

			    + "|" + "5" // South America 
				+ "|" + "6" // South Asia - Oceania 
				+ "|" + "7" // Russia 
				+ "|" + "8" // East Asia 
				+ "|" + "9" // Asia e Medioriente 
				+")"
				);
	}
	/**
	 * Update statistics on table.
	 * @param provider
	 * @param internalSmsRequest
	 * @param internalSmsResponse
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	private void tableUpdateMTstats(String myUUID, Connection connection, Provider provider, XasUser xasUserName,InternalSmsRequest internalSmsRequest, InternalSmsResponse internalSmsResponse) throws SQLException, NamingException {
		Matcher matcher = INTERNATIONAL_PREFIX.matcher(internalSmsRequest.getPhoneNumber());
		String prefix =  matcher.find() ? matcher.group() : null;
		if (prefix == null || "".equals(prefix)) prefix = "Other";
		
	    TableManager.updateStats(myUUID, connection, TableManager.XAS_TYPE_MOBILE_TERMINATED, provider.getName(), xasUserName.getNameMedium(), RANGE_FORMAT.format(new Date()), prefix, internalSmsResponse.getTotalSms());
    }

	private void tableUpdateMOstats(String myUUID, Connection connection, XasUser xasUserName, InternalMobileOriginated internalMobileOriginated) throws SQLException, NamingException {
		Matcher matcher = INTERNATIONAL_PREFIX.matcher(internalMobileOriginated.getPhoneNumber());
		String prefix =  matcher.find() ? matcher.group() : null;
		if (prefix == null || "".equals(prefix)) prefix = "Other";
		
	    TableManager.updateStats(myUUID, connection, TableManager.XAS_TYPE_MOBILE_ORIGINATED, internalMobileOriginated.getMoDestinator(), xasUserName.getNameMedium(), RANGE_FORMAT.format(new Date()), prefix, 1);
    }

	private void tableUpdateDRstats(String myUUID, Connection connection, String providerName, XasUser xasUserName, DeliveryReport deliveryReport) throws SQLException, NamingException {
		Matcher matcher = INTERNATIONAL_PREFIX.matcher(deliveryReport.getPhoneNumber());
		String prefix =  matcher.find() ? matcher.group() : null;
		if (prefix == null || "".equals(prefix)) prefix = "Other";
		
	    TableManager.updateStats(myUUID, connection, TableManager.XAS_TYPE_DELIVERY_REPORT, providerName, xasUserName.getNameMedium(), RANGE_FORMAT.format(new Date()), prefix, 1);
    }

	//#TIM
	public DeliveryResponse receiveNotification(InternalMobileOriginated internalMobileOriginated) {
		internalMobileOriginated.setAccepted(Boolean.FALSE);
		
		if ((internalMobileOriginated.getMobileOriginated() != null && internalMobileOriginated.getMobileOriginated().getUuid() != null)) {
			MDC.put(ConstantsSms.MY_UUID_KEY, internalMobileOriginated.getMobileOriginated().getUuid());
		}
  		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
  			
		Chronometer chronometer = new Chronometer(true, ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_START + "@" + ConstantsSms.MY_APPL_ID + " " + " MY_UUID:" + myUUID);

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
	    	.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "receiveNotification(InternalMobileOriginated)").preset("default"); 

 		XasUser xasUser = null;
 		InternalDeliveryResponse internalDeliveryResponse = null;
 		DeliveryResponse deliveryResponse = new DeliveryResponse();
 		deliveryResponse.setSmsIds(internalMobileOriginated.getMobileOriginated().getSmsIds());

 		// Security Validation
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.MODR).add(SingleSecurityProfile.ADMIN));
		internalMobileOriginated.setUserInRole(new Boolean(isUserInRole));
		internalMobileOriginated.setUserId(getSupport().getPrincipal().getName());
		if(!isUserInRole)
		{
			//TODO
		}
		
 		try {
 			// XAS COMMAND PROCESSING
 			String xasCmd = internalMobileOriginated.getMsg();
 			if (xasCmd != null && xasCmd.startsWith(ConstantsSms.XAS_MO_COMMAND_PREFIX + ConstantsSms.XAS_MO_SEPARATOR)) {
 				String[] params = xasCmd.split(ConstantsSms.XAS_MO_SEPARATOR);
 				if (params.length > 1) { 
					logger.debug(sl.logIt(K_STEP, "processing XasCmd", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(params)).getLogRow(true)); // Debug and keep row
 					if (ConstantsSms.XAS_MO_COMMAND_ROUTE.equals(params[1])) {
 		 				if (params.length > 2) { 
 		 					internalMobileOriginated.getMobileOriginated().setMoDestinator(params[2]);
 	 		 				if (params.length > 3) { 
 	 		 					// Get full message starting by the 3rd colon due to splitting not complete message in params[3] -> XASCMD:RT:3933900000003:Message:TEST
 	 		 					internalMobileOriginated.getMobileOriginated().setMsg(xasCmd.substring(params[0].length()+params[1].length()+params[2].length()+3));
 	 	 					}
 	 					}
						logger.info(sl.logIt(K_STEP, "XasCmd Routing", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(params)).getLogRow(true)); // Debug and keep row
 					}
 				}
 			}
 			
 			// get xasUser for MobileOriginated message 
			if (internalMobileOriginated.getMoDestinator() != null) {
	 			logger.debug(sl.logIt(K_STEP, "getFinalXasUser", SmartLog.K_PARAMS, internalMobileOriginated.getPhoneNumber() + "-->" + internalMobileOriginated.getMoDestinator()).getLogRow(true)); // Debug and keep row
				xasUser = GatewayA2pSmsFactory.getInstance().getConfiguration()
						.getFinalXasUser(internalMobileOriginated.getMoDestinator());
				if (xasUser != null) { // if defined call WS endPoint
					logger.debug(sl.logIt(K_STEP, "sendMobileOriginated EndPoint", SmartLog.K_PARAMS, xasUser.getEndPoint()).getLogRow(true)); // Debug and keep row
					
 					internalDeliveryResponse = (new ApplicationGateway()).sendMobileOriginated(xasUser, internalMobileOriginated, internalDeliveryResponse, chronometer);
 					
					deliveryResponse = internalDeliveryResponse.getDeliveryResponse();
					if (internalDeliveryResponse != null 
						&& (StatusCodeType._XAS00080I.equals(internalDeliveryResponse.getWsEndpointSC()) || StatusCodeType._XAS00082E.equals(internalDeliveryResponse.getWsEndpointSC())) ) {
						// If success of fatal do not retry delivery
						internalMobileOriginated.setAccepted(Boolean.TRUE);
					}

		 			if (it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration.getInstance().isDatabasePresent()) {
		 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_WAIT); 
 						Connection connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
 		 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_START); 
						try {
			    			logger.debug(sl.reload("defaul").logIt(K_STEP, "tableUpdateMOstats","a_internalMobileOriginated", XConstants.XSTREAMER.toXML(internalMobileOriginated)).getLogRow(true)); // Debug and keep row
							tableUpdateMOstats(myUUID, connection, xasUser, internalMobileOriginated);
			    			
						} finally {
			    			connection.close();
			 				chronometer.partial(ConstantsSplunk.K_TIME_SEC_TABLE + "." + ConstantsSplunk.K_TIME_SECTION_END); 
 						}
		 			}
			} else {
				deliveryResponse = new DeliveryResponse(ConstantsSms.XAS02086E_CODE, ConstantsSms.XAS02086E_MESSAGE, internalMobileOriginated.getMoDestinator());
				deliveryResponse.setSmsIds(internalMobileOriginated.getMobileOriginated().getSmsIds());
 				internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
 				}
 			}
		} catch (XASException e) { // Handled exceptions logger.debug
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, e.getCode(), SmartLog.K_STATUS_MSG, MessageFormat.format(e.getMessage(), e.getParameters()), SmartLog.K_PARAMS, e.getClass().getName()).getLogRow(true), new XASException(MessageFormat.format(e.getMessage(), e.getParameters()), e.getCause()));
			deliveryResponse.setCode(e.getCode());
			deliveryResponse.setMessage(MessageFormat.format(e.getMessage(), e.getParameters()));
			internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
			internalDeliveryResponse.setWsEndpointSC(ConstantsSms.XAS06081E_CODE);
			internalDeliveryResponse.setWsEndpointSM(ConstantsSms.XAS06081E_MESSAGE);
		} catch (Exception e) { // Un-handled exceptions logger.error
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
	    	logger.error(errors.toString());
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, ConstantsSms.XAS03099E_CODE, SmartLog.K_STATUS_MSG, MessageFormat.format(ConstantsSms.XAS03099E_MESSAGE, new String[]{e.getMessage()}), SmartLog.K_PARAMS, e.getClass().getName()).getLogRow(true), e);
			deliveryResponse.setCode(ConstantsSms.XAS00002E_CODE);
			deliveryResponse.setMessage(errors.toString());
			internalDeliveryResponse = new InternalDeliveryResponse(deliveryResponse);
			internalDeliveryResponse.setWsEndpointSC(ConstantsSms.XAS06081E_CODE);
			internalDeliveryResponse.setWsEndpointSM(ConstantsSms.XAS06081E_MESSAGE);
		} finally {
			chronometer.partial(ConstantsSplunk.K_TIME_SEC_TRAN + "." + ConstantsSplunk.K_TIME_SECTION_END);
			chronometer.stop();
			// Doing Analytics
            doAnalytic(myUUID, internalMobileOriginated, xasUser, null, null, internalDeliveryResponse, chronometer);

            if (logger.isDebugEnabled()) { 
				logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, XConstants.XSTREAMER.toXML(deliveryResponse)).getLogRow()); // Debug and keep row
			}
		}  
 		
		return deliveryResponse;
    }

//**** JMSMessageConsumer.java

	
	//private static final String USER_ID = "MDB";
	private static final String USER_ID_PROPERTY = "JMSXUserID";
	private static final String MSG_DATE_PROPERTY = "JMS_IBM_PutDate";
	private static final String MSG_TIME_PROPERTY = "JMS_IBM_PutTime";
	
	/**
	 * onMessage
	 */
	public void onMessage(javax.jms.Message msg) {
		if (logger.isDebugEnabled()) {
			String prpValue, prpName = null;
			try {
				prpName = "JMSXAppID"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMSXUserID"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Encoding"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_MsgType"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMSXDeliveryCount"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutTime"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Format"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutApplType"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutDate"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Character_Set"; prpValue = msg.getStringProperty(prpName);	 logger.debug(prpName + " = " + prpValue);
				prpName = "JMS CorrelationID"; prpValue = msg.getJMSCorrelationID();	 logger.debug(prpName + " = " + prpValue);
			} catch(JMSException e) {
				logger.debug("Errore sulla proprietà " + prpName);
				return;
			}
		}
			
			//PreServiceFactory factory = PreServiceFactory.getInstance();
			//IPreServiceFacade facade = null;
			try {
				String userId = msg.getStringProperty(USER_ID_PROPERTY);
				userId = (userId == null ? null : userId.trim());
				boolean isUserInRole = PreSmsRoleCache.getInstance().isInRole(userId);
				InternalSmsRequest request = getRequest(msg);
				request.setChannel(ConstantsSms.CHANNEL_MQ);
				request.setUserId(userId);
				request.setUserInRole(new Boolean(isUserInRole));
				request.setInterfaceV2();

				sendSmsInternal(request);
				return;
			} catch (XASWrongRequestException e) {
				e.printStackTrace(System.out);
			} catch (XASConfigurationException e) {
				throw new XASRuntimeException(e);
			} catch (JMSException e) {
				throw new XASRuntimeException(e);
			} catch (ParseException e) {
				throw new XASRuntimeException(e);
            }
			
	}
	
	private InternalSmsRequest getRequest(javax.jms.Message msg) throws XASWrongRequestException, JMSException, ParseException, XASConfigurationException {
		SmsRequest req = null;
		if (msg instanceof MapMessage) {
			req = getRequest((MapMessage)msg);
		} else if (msg instanceof TextMessage) {
			req = getRequest((TextMessage)msg);
		} else if (msg instanceof ObjectMessage) {
			req = getRequest((ObjectMessage)msg);
		} else throw new XASWrongRequestException("Message of unexpected class: " + msg.getClass().getName());
		InternalSmsRequest intreq = new InternalSmsRequest(req);
		intreq.setRequestTime(getRequestTime(msg));
		intreq.setChannel(ConstantsSms.CHANNEL_MQ);
		//logger.debug("Letto messaggio: timezone = " + MSG_TIME_ZONE);
		return intreq;
	}
	
	private Date getRequestTime(javax.jms.Message msg) throws JMSException, XASConfigurationException {
		String writeDate = msg.getStringProperty(MSG_DATE_PROPERTY);
		String writeTime = msg.getStringProperty(MSG_TIME_PROPERTY);
		int year = Integer.parseInt(writeDate.substring(0, 4));
		int month = Integer.parseInt(writeDate.substring(4,6));
		int day = Integer.parseInt(writeDate.substring(6,8));
		int hours = Integer.parseInt(writeTime.substring(0,2));
		int minutes = Integer.parseInt(writeTime.substring(2,4));
		int seconds = Integer.parseInt(writeTime.substring(4,6));
		Calendar cal;
        try {
	        cal = Calendar.getInstance(Configuration.getInstance().getJmsTimeZone());
        } catch (XASException e) {
	        e.printStackTrace();
	        throw new XASConfigurationException("XASException", e);
        }
		cal.set(year, month-1, day, hours, minutes, seconds);
		return cal.getTime();
	}
	
	private SmsRequest getRequest(MapMessage msg) throws XASWrongRequestException {
		try {
			if (logger.isDebugEnabled()) logger.debug("Request is a MapMessage");
			SmsRequest request = new SmsRequest();
			request.setCorrelationID(msg.getString("correlationID"));
			request.setForceAsciiEncoding(new Boolean(msg.getBoolean("forceAsciiEncoding")));
			request.setMsg(msg.getString("msg"));
			request.setPhoneNumber(msg.getString("phoneNumber"));
			request.setXasUser(msg.getString("xasUser"));
			request.setSmsResponse(new Boolean(msg.getBoolean("smsResponse")));
			SimpleDateFormat sdf = new SimpleDateFormat(SmsRequest.DATE_FORMAT); 
			try {
				if (msg.getString("validity") != null) request.setValidity(sdf.parse(msg.getString("validity")));
				if (msg.getString("validityPeriod") != null) request.setValidityPeriod(new Integer(Integer.parseInt(msg.getString("validityPeriod"))));
				if (msg.getString("deliveryTime") != null) request.setDeliveryTime(sdf.parse(msg.getString("deliveryTime")));
			} catch (ParseException e) {
				throw new XASWrongRequestException("Error parsing DateTime value in MapMessage",e);
			}
			return request;
		} catch (JMSException e) {
			throw new XASWrongRequestException(e);
		}
	}
	
	private SmsRequest getRequest(TextMessage msg) throws XASWrongRequestException {
		try {
			if (logger.isDebugEnabled()) logger.debug("Request is a TextMessage");
			String text = msg.getText();
			XStream xstream = null;
			if (text.length() == 0) throw new XASWrongRequestException("Empty request");
			boolean bool = text.startsWith("<");
			xstream = (bool ? new XStream(new DomDriver()) : new XStream(new JettisonMappedXmlDriver()));
			xstream.alias("smsRequest", SmsRequest.class);
			//logger.debug("Testo ricevuto: " + text + "E' un xml? " + bool + "--");
			//logger.debug("Convertitore usato: " + (bool ? "xml" : "json"));
			SmsRequest smsRequest = (SmsRequest) xstream.fromXML(text);
			return smsRequest;
		} catch(JMSException e) {
			throw new XASWrongRequestException("Method getText() failed",e);
		}
	}
	
	private SmsRequest getRequest(ObjectMessage msg) throws XASWrongRequestException {
		if (logger.isDebugEnabled()) logger.debug("Request is a ObjectMessage");
		Serializable obj = null;
		try {
			obj = msg.getObject( );
		} catch(JMSException e) {
			throw new XASWrongRequestException("Method getObject() failed",e);
		}
		if (obj instanceof SmsRequest) return (SmsRequest)obj;
		throw new XASWrongRequestException("ObjectMessage contains an Object of type " + obj.getClass().getName());
	}

	protected void initializationCleanup() throws javax.ejb.CreateException {
	}

	private String getChannel() {
		StackTraceElement[] ste = new Throwable().getStackTrace(); // Clone the
		for (int i = 0; i < ste.length; i++) {
			if (ste[i].getClassName().startsWith("com.ibm.ws.webservices.engine"))
				return ConstantsSms.CHANNEL_WS; 
			if (ste[i].getClassName().startsWith("com.ibm.ejs.jms.listener.MDBWrapper"))
				return ConstantsSms.CHANNEL_MQ; 
		}
		return ConstantsSms.CHANNEL_EJ;
	}
	
	private String stringValue(String s) {
		return s != null ? s : "";
		
	}
	
	/**
	 * Convert from provider status to xasStatus.
	 * @param internalDeliveryReport
	 * @return
	 */
	private String[] xasDRstatus(InternalDeliveryReport internalDeliveryReport) {
		String status = internalDeliveryReport.getStatus() != null ? internalDeliveryReport.getStatus() : "null";
		String[] xasStatus = {"","",""};
		if (it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.TYPE.equals(internalDeliveryReport.getProviderType())) {
			xasStatus = it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Customization.xasDRstatus(status);
		} else
		if (it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE.equals(internalDeliveryReport.getProviderType())) {
			xasStatus = it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Customization.xasDRstatus(status);		
		}
		return xasStatus;
 	}
	/**
	 * replace some letters with accent, trim spaces
	 * @param message
	 * @return
	 */
	public static String normalizePhoneMessage(String message) {
		// log.debug("Message to normalize: [" + message + "]");
		StringBuffer normal = new StringBuffer("");
		for (int i=0; i<message.length() ;++i)
		{
			String a = message.substring(i,i+1);
//			if(a.equals("è"))
//				normal.append("e'");
//			else if(a.equals("é"))
//				normal.append("e'");
//			else if(a.equals("ù"))
//				normal.append("u'");
//			else if(a.equals("ì"))
//				normal.append("i'");
//			else if(a.equals("ò"))
//				normal.append("o'");
//			else if(a.equals("ä"))
//				normal.append("a");
//			else if(a.equals("ö"))
//				normal.append("o");
//			else if(a.equals("ñ"))
//				normal.append("n");
//			else if(a.equals("ü"))
//				normal.append("u");
//			else if(a.equals("à"))
//				normal.append("a'");
			if(a.equals("€"))
				normal.append("EUR");
			else
				normal.append( a );
		}
		String ret = normal.toString();
		ret = ret.trim();
		// log.debug("Message normalized: [" + ret + "]");
		return ret;
	}

	public String[][] fetchStats(String[] filters) throws RemoteException {
			Connection connection;
			String[][] returnValue;
            try {
	            connection = DBConnectionFactory.getInstance().retrieveConnection(ConstantsSms.DB_JNDI);
	            returnValue = TableManager.fetchStats(UUID.randomUUID().toString(), connection, filters);
            } catch (NamingException e) {
	            throw new RemoteException("NamingException", e);
            } catch (SQLException e) {
	            throw new RemoteException("SQLException", e);
            }
			return returnValue;
		
	}
}
