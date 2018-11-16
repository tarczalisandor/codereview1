package it.usi.xframe.xas.bfimpl.a2psms.providers.telecom;

import ie.omk.smpp.util.GSMConstants;
import it.eng.service.mgp.sms.GsmEncoding;
import it.eng.service.mgp.sms.MtMessage;
import it.eng.service.mgp.sms.MtSendingResult;
import it.eng.service.mgp.sms.Result;
import it.eng.service.mgp.sms.SmsSendingServicePortType;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfimpl.a2psms.providers.GatewayA2Psms;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.SplunkConstants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;
import com.ibm.ws.webservices.engine.client.Stub;

public class GatewaySmsProvider extends GatewayA2Psms {
	private static Logger logger = LoggerFactory.getLogger(GatewaySmsProvider.class);

	final static 		String SERVICE_LOOKUP 		= "java:comp/env/service/SmsSendingService";
	
	final static Byte DELIVERY_REPORT_NO = new Byte((byte)0);
	final static Byte DELIVERY_REPORT_YES = new Byte((byte)1);
	final static boolean DELIVERY_REPORT_NO_BOOLEAN = false;
	final static boolean DELIVERY_REPORT_YES_BOOLEAN = true;

	// *** MULTIPART DEFINITION ***
 	final static byte UDH_LEN = 0x05; 						// UDH length (5 bytes)
	final static byte NUMBERING_8BITS = 0x00;				// 8bit numbering for concatenated message
	final static byte INFO_LEN = 0x03;						// info length (3 more bytes)
	final static Byte SM_UDH_GSM = new Byte((byte)0x40);	// Set User Data Header Indicator(UDHI) which is done by setting 0x40 in ESM Class of the SMS
	
	final static int IX_REFERENCE_NUMBER = 3;
	final static int IX_TOT_PARTS = 4;
	final static int IX_PART_NUM = 5;

	final static byte[] UDH_TEMPLATE = {
			UDH_LEN,						// user data header
			NUMBERING_8BITS,	
			INFO_LEN,
			0x00,							// reference number for this concatenated message
			0x00,							// total parts number 
			0x00							// current part number 
	};
	final static int MAX_BYTES_PER_GSM7_PART = 153;			// cut the header length from message length
	final static int MAX_BYTES_PER_UCS2_PART = 134;			// cut the header length from message length
	private static byte multipartSmppReferenceNumber;
	static {
		Random rnd = new Random();
		multipartSmppReferenceNumber = (byte)rnd.nextInt(256);
	}
    private synchronized byte nextMultipartReferenceNumber() {
		return ++GatewaySmsProvider.multipartSmppReferenceNumber ;
	}
	// *** MULTIPART DEFINITION ***

 	public InternalSmsResponse sendMessage(InternalSmsResponse internalSmsResponse, InternalSmsRequest internalSmsRequest, XasUser xasUser, Provider provider, Originator originator, boolean multipart, Chronometer chronometer) throws XASException {
  		String myUUID = internalSmsRequest.getUuid() != null 
		  ? internalSmsRequest.getUuid()
			  : (String) MDC.get(Constants.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, GatewaySmsProvider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendMessage").preset("myPreset");
		
		if (logger.isDebugEnabled()) { 
 			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
					, "a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest)
					, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
					, "a_provider", XConstants.XSTREAMER.toXML(provider)
					, "a_originator", XConstants.XSTREAMER.toXML(originator)
					).getLogRow(true)); // Debug and keep row
			sl.reload("myPreset");
		}
		
		// WARNING: using direct lookup to enable the WSSE Outbound handler to be functional and override the standard IBM wsse.
		
		SmsSendingServicePortType service;
        try {
        	
    		service = ((it.eng.service.mgp.sms.SmsSendingService)new InitialContext().lookup(SERVICE_LOOKUP)).getSmsSendingServicePort(getTelecomPopURL());
			if(service == null) 
				throw new XASException(Constants.XAS00098E_MESSAGE, null, Constants.XAS00098E_CODE, new Object[] {"Failed to instantiate SMS TelecomPop proxy for " + Configuration.JNDI_POP_SOAP_URL});
			setTimeOut((Stub) service, provider.getTimeOut());
			// WSSE authentication account setup by the web-service client handler.

//  		HTTP Basic Authentication
//			SmsSendingServiceSoapBindingStub bindingProvider = (SmsSendingServiceSoapBindingStub) service;
//			Customization customization = (Customization)provider.getCustomization();
//			bindingProvider.setUsername(customization.getUser());
//			bindingProvider.setPassword(customization.getDecodedPassword());
        } catch (ServiceException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
    		throw new XASException(Constants.XAS00098E_MESSAGE, errors.toString(), Constants.XAS00098E_CODE, new Object[] {"ServiceException for " + Configuration.JNDI_POP_SOAP_URL, errors.toString()});
        } catch (NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
    		throw new XASException(Constants.XAS00098E_MESSAGE, errors.toString(), Constants.XAS00098E_CODE, new Object[] {"NamingException for " + Configuration.JNDI_POP_SOAP_URL, errors.toString()});
        }
        
        
		MtMessage mtMessage = new MtMessage();
		mtMessage.setSrcAddrNpi(originator.getNumPlanId().intValue());
		mtMessage.setSrcAddrTon(originator.getTypeOfNumber().intValue());	
		mtMessage.setSrcAddr(originator.getOriginator()); 

		mtMessage.setDestAddrTon(GSMConstants.GSM_TON_INTERNATIONAL); 
		mtMessage.setDestAddrNpi(GSMConstants.GSM_NPI_E164);
		mtMessage.setDestAddr(internalSmsRequest.getPhoneNumber());
		
		mtMessage.setProtocolId(IntToByte(internalSmsRequest.getProtocolId()).toString());

		if (internalSmsRequest.getValidity() != null) {
			Calendar validityPeriod = Calendar.getInstance();
			validityPeriod.setTime(internalSmsRequest.getValidity());
			mtMessage.setValidityPeriod(validityPeriod);
		}
		if (internalSmsRequest.getDeliveryTime() != null) {
			Calendar deliveryDate = Calendar.getInstance();
			deliveryDate.setTime(internalSmsRequest.getDeliveryTime());
			mtMessage.setDeliveryDate(deliveryDate);
		}
		mtMessage.setRegisteredDelivery(DELIVERY_REPORT_YES_BOOLEAN);
		
		switch (internalSmsRequest.getEncoding()) {
			case InternalSmsRequest.ENCODING_GSM338:
				 mtMessage.setPayloadUddc(GsmEncoding.GSM7);
				 break;
			case InternalSmsRequest.ENCODING_UCS2:
				 mtMessage.setPayloadUddc(GsmEncoding.UCS2);
				 break;
		}

		MtSendingResult mtSendingResult;
		String[] smsIdS = null;
		int totSMSes = 1;
		boolean errorExit = false;
		if (!multipart) {
			byte[] part = null;
			mtMessage.setTxId(UUID.randomUUID().toString());
    		mtMessage.setMsgId(internalSmsRequest.getUuid());
			if(internalSmsRequest.isEncodingGSM338()) {
				logger.debug(sl.logIt("a_step", "Encoding GSM338").getLogRow(true));
                part = internalSmsRequest.getMsgBytes();
 				mtMessage.setPayloadUd(part);
			} else if(internalSmsRequest.isEncodingUCS2()) {						
				logger.debug(sl.logIt("a_step", "Encoding UCS2").getLogRow(true));
                part = internalSmsRequest.getMsgBytes();
				mtMessage.setPayloadUd(part);
		    } else throw new XASException(Constants.XAS00098E_MESSAGE, null, Constants.XAS00098E_CODE, new Object[] {"Encoding message not yet implemented", new Byte(internalSmsRequest.getEncoding())});
			int nByte = part.length;
			internalSmsResponse.setGatewayContacted(true);
			logger.debug(sl.logIt("a_step","sendTelecom", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(mtMessage)).getLogRow(true));
			logger.info(sl.reload("myPreset").logIt("a_step","sendTelecom"
					, "a_correlationId", internalSmsRequest.getCorrelationID()
					, "a_uuid", internalSmsRequest.getUuid()
					, "a_txId", mtMessage.getTxId()
					, "a_destAddr", mtMessage.getDestAddr()
					, "a_byte", Integer.toString(nByte)
					, "a_encoding", internalSmsRequest.getEncodingDescription()
					).getLogRow(true));
        	mtSendingResult = sendTelecom(myUUID, service, mtMessage, chronometer);
    		if (Result.KO.equals(mtSendingResult.getResult())) {
    			// Failure
    			internalSmsResponse.setCode(Constants.XAS00098E_CODE);
    			internalSmsResponse.setMessage(MessageFormat.format(Constants.XAS00098E_MESSAGE, new Object[] {mtSendingResult.getReason().getValue(), mtSendingResult.getReason()}));
    			errorExit = true;
    		}
    		if (mtSendingResult.getSmsId() != null) {
    			smsIdS = new String[] {mtSendingResult.getSmsId()};
    		}
		} else {
			byte[] encodedBytes = internalSmsRequest.getMsgBytes();
//					internalSmsRequest.isEncodingGSM338()
//							? (new DefaultAlphabetEncoding().decodeString(internalSmsRequest.getMsgBytes())).getBytes()
//							: internalSmsRequest.getMsgBytes();
			 
			// *** MULTIPART DEFINITION ***
//VODAFONE	mtMessage.setEsm_class(SM_UDH_GSM);											// set UDHI flag to true (enable concatenated messages)
	        byte multipartReference = nextMultipartReferenceNumber();
	    	// split msg in parts and send them
	        int maxBytesPerPart = internalSmsRequest.isEncodingGSM338() ? MAX_BYTES_PER_GSM7_PART :
	        		 			  internalSmsRequest.isEncodingUCS2() ? MAX_BYTES_PER_UCS2_PART : 140;
	    	int totParts = (int) Math.ceil( (1.0*encodedBytes.length) / maxBytesPerPart );		// max length of each message part
//	    	logger.debug("Sending message as concatenated message (" + totParts + " parts)");

	    	ArrayList smsIdList = new ArrayList();
	    	int nByte = 0;
	    	internalSmsResponse.setGatewayContacted(true);
//		 	internalSmsResponse.setTotalSms(totParts);
 	    	for (int byteCount=0, partNumb = 1;
	    		 byteCount < encodedBytes.length && !errorExit;
	    		 byteCount += nByte, partNumb += 1) {
	    		nByte = Math.min(maxBytesPerPart, encodedBytes.length - byteCount);
//	    		System.out.println("maxBytesPerPart=" + maxBytesPerPart + ", nByte=" + nByte + ", encodedBytes.length=" + encodedBytes.length + ", byteCount=" + byteCount);
	    		byte[] part = new byte[nByte];
	    		System.arraycopy(encodedBytes, byteCount, part, 0, nByte);
	    		
	    		byte[] udh = UDH_TEMPLATE;
	    		udh[IX_REFERENCE_NUMBER] = multipartReference;							// not relevant since only 1 concatenated msg is sent
	    		udh[IX_TOT_PARTS] = (byte) totParts;
	    		udh[IX_PART_NUM] = (byte) partNumb;
	    		
	    		mtMessage.setTxId(UUID.randomUUID().toString());
	    		mtMessage.setMsgId(internalSmsRequest.getUuid());
				mtMessage.setPayloadUdh(udh);		// The sms header part
				mtMessage.setPayloadUd(part);		// The sms message part
				mtMessage.setPartNum(partNumb); 	// Sms part number
		    	mtMessage.setNumOfParts(totParts);	// Sms total part number

				logger.info(sl.reload("myPreset").logIt("a_step","sendTelecom"
						, "a_correlationId", internalSmsRequest.getCorrelationID()
						, "a_uuid", internalSmsRequest.getUuid()
						, "a_txId", mtMessage.getTxId()
						, "a_destAddr", mtMessage.getDestAddr()
						, "a_byte", Integer.toString(nByte)
						, "a_encoding", internalSmsRequest.getEncodingDescription()
						, "a_part", partNumb + "/" + totParts
						).getLogRow(true));
				
	        	mtSendingResult = sendTelecom(myUUID, service, mtMessage, chronometer);
	        	smsIdList.add(mtSendingResult.getSmsId());
	    		if (Result.KO.equals(mtSendingResult.getResult())) {
	    			// Failure
	    			internalSmsResponse.setCode(Constants.XAS00098E_CODE);
	    			internalSmsResponse.setMessage(MessageFormat.format(Constants.XAS00098E_MESSAGE, new Object[] {mtSendingResult.getReason().getValue(), mtSendingResult.getReason()}));
	    			errorExit = true;
	    		} 
 	    	}
   		 	smsIdS = (String[]) smsIdList.toArray(new String[0]);
   		 totSMSes = totParts;
	    }
		// Success
		internalSmsResponse.setSmsIds(smsIdS);
		internalSmsResponse.setTotalSms(smsIdS != null ? smsIdS.length : 0);
		if (!errorExit) {
			internalSmsResponse.setCode(Constants.XAS00000I_CODE);
			internalSmsResponse.setMessage(MessageFormat.format(Constants.XAS00000I_MESSAGE2, new String[]{internalSmsRequest.getUuid(), Integer.toString(internalSmsRequest.getDstByteLength()), Integer.toString(totSMSes), internalSmsRequest.getEncodingDescription()}));
		}
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.reload("myPreset").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, "a_internalSmsResponse", XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow());
		}
		
		return internalSmsResponse;
	}

 	static long fakeCounter = 0;
 	/**
 	 * Send sms via Telecom service.
 	 * @param service
 	 * @param sm
 	 * @param gws
 	 * @return Submit_resp response from the service.
 	 * @throws XASException
 	 */
	private MtSendingResult sendTelecom(String myUUID, SmsSendingServicePortType service, MtMessage mtMessage, Chronometer chronometer) throws XASException {
		MtSendingResult mtSendingResult;
        try {
        		chronometer.partial(Configuration.TYPE + ".send." + SplunkConstants.K_TIME_SECTION_START);
        		mtSendingResult = service.sendMessage(mtMessage);
        		chronometer.partial(Configuration.TYPE + ".send." + SplunkConstants.K_TIME_SECTION_END);
        } catch (RemoteException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(myUUID + ":" + errors.toString());
    		throw new XASException(Constants.XAS00098E_MESSAGE, errors.toString(), Constants.XAS00098E_CODE, new Object[] {"RemoteException for " + Configuration.JNDI_POP_SOAP_URL, e.getCause()});
        }  
		if(mtSendingResult == null)
			throw new XASException(Constants.XAS00098E_MESSAGE, null, Constants.XAS00098E_CODE, new Object[] {"Null response object returned by Telecom service"});

		return mtSendingResult;
	}
 	
	/**
	 * Get the URP from the JNDI.
	 * @return
	 * @throws XASException
	 */
   public static URL getTelecomPopURL() throws XASException {
		URL telecomPopURL;
        try {
        	Context ctx = new InitialContext();
            telecomPopURL= (URL)ctx.lookup(Configuration.JNDI_POP_SOAP_URL);
        } catch (NameNotFoundException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00095E_MESSAGE, errors.toString(), Constants.XAS00095E_CODE, new Object[] {"NameNotFoundException", Configuration.JNDI_POP_SOAP_URL, "Check jndiName presence in /XA-XAS/META-INF/ibmconfig/cells/defaultCell/applications/defaultApp/deployments/defaultApp/resources.xml", errors.toString()});
        } catch (NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(Constants.XAS00095E_MESSAGE, errors.toString(), Constants.XAS00095E_CODE, new Object[] {"NamingException", Configuration.JNDI_POP_SOAP_URL, "none", errors.toString()});
        }
        return telecomPopURL;
    }
 }
