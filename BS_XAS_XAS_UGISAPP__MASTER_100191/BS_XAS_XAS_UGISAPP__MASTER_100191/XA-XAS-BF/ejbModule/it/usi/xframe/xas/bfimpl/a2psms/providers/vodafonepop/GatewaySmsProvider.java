package it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop;

import ie.omk.smpp.util.DefaultAlphabetEncoding;
import ie.omk.smpp.util.GSMConstants;
import ie.omk.smpp.util.PacketStatus;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfimpl.a2psms.providers.GatewayA2Psms;
import it.usi.xframe.xas.bfimpl.sms.SMPPUtil;
import it.usi.xframe.xas.bfimpl.sms.SMPPUtilities;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.ConstantsSplunk;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.SmsResponse3;
import it.usi.xframe.xas.util.json.XConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.MDC;
import org.apache.soap.encoding.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ws.webservices.engine.client.Stub;

import SoapSmppGW.GWService;
import SoapSmppGW.GWServiceServiceLocator;
import SoapSmppGW.GWSession;
import SoapSmppGW.Submit_resp;
import SoapSmppGW.Submit_sm;
import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class GatewaySmsProvider extends GatewayA2Psms {
	private static Logger logger = LoggerFactory.getLogger(GatewaySmsProvider.class);

	final static Byte DELIVERY_REPORT_NO = new Byte((byte)0);
	final static Byte DELIVERY_REPORT_YES = new Byte((byte)1);

	// *** MULTIPART DEFINITION ***
 	final static byte UDH_LEN = 0x05; 						// UDH length (5 bytes)
	final static byte NUMBERING_8BITS = 0x00;				// 8bit numbering for concatenated message
	final static byte INFO_LEN = 0x03;						// info length (3 more bytes)
	final static Byte SM_UDH_GSM = new Byte((byte)0x40);	// Set User Data Header Indicator(UDHI) which is done by setting 0x40 in ESM Class of the SMS
	final static Byte SM_REPLACE_TYPE_1 = new Byte((byte)0x41);	// Replacement for CARDS
	
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
	final static int MAX_BYTES_PER_PART = 140 - UDH_TEMPLATE.length;			// cut the header length from message length
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
 		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, GatewaySmsProvider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendMessage").preset("default");
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
					, "a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest)
					, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
					, "a_provider", XConstants.XSTREAMER.toXML(provider)
					, "a_originator", XConstants.XSTREAMER.toXML(originator)
					).getLogRow(true)); // Debug and keep row
		}
		
		sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT).preset("default");
		
		SmsResponse3 smsResponse3 = new SmsResponse3();
		internalSmsResponse = new InternalSmsResponse(smsResponse3);
		
		GWServiceServiceLocator locator = new GWServiceServiceLocator();
		GWService service;
        try {
            service = locator.getGWService(getVodafonePopURL());
			if(service == null) 
				throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Failed to instantiate SMS VodafonePop proxy for " + Configuration.JNDI_POP_SOAP_URL});
			setTimeOut((Stub) service, provider.getTimeOut());
        } catch (ServiceException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
    		throw new XASException(ConstantsSms.XAS04098E_MESSAGE, errors.toString(), ConstantsSms.XAS04098E_CODE, new Object[] {"ServiceException for " + Configuration.JNDI_POP_SOAP_URL, errors.toString()});
        }

		// instantiate the service
		Submit_sm sm = new Submit_sm();
		
		// set authentication account
		Customization customization = (Customization)provider.getCustomization();
		GWSession gws = new GWSession();
		gws.setAccountName(customization.getUser());									// user Pop
		gws.setAccountPassword(customization.getDecryptedPassword());						// password Pop


		
		// service type
		// sm.setService_type(null);													// null = default value
		sm.setSource_addr_npi(IntToByte(originator.getNumPlanId().intValue()));
		sm.setSource_addr_ton(IntToByte(originator.getTypeOfNumber().intValue()));	
		sm.setSource_addr(originator.getOriginator()); 

		sm.setDest_addr_ton(IntToByte(GSMConstants.GSM_TON_INTERNATIONAL));			// costante - tipo destinatario 
		sm.setDest_addr_npi(IntToByte(GSMConstants.GSM_NPI_E164));					// costante - tipo destinatario
		sm.setDestination_addr(internalSmsRequest.getPhoneNumber());							// numero telefono destinatario
//		sm.setEsm_class(IntToByte(0x40));											// set UDHI flag to true (enable concatenated messages)

		// Override protocolId for sms message if present
		if (internalSmsRequest.getReplaceClass() != null) {
			logger.debug(sl.logIt(SmartLog.K_STEP, "Using protocolID: " + IntToByte(internalSmsRequest.getProtocolId())).getLogRow(true));
//			sm.setProtocol_id(IntToByte(internalSmsRequest.getProtocolId()));
		}

		//		sm.setPriority_flag(null);			 										// ? null
		if (internalSmsRequest.getValidity() != null) {
			String validityString = SMPPUtilities.getInstance().convertDate(internalSmsRequest.getValidity());
			sm.setValidity_period(validityString);
		}
		if (internalSmsRequest.getDeliveryTime() != null) {
			String delivery = SMPPUtilities.getInstance().convertDate(internalSmsRequest.getDeliveryTime());
			sm.setSchedule_delivery_time(delivery);
		}
//		sm.setSchedule_delivery_time(null);											// 
		sm.setRegistered_delivery(DELIVERY_REPORT_YES);								// set 1 = Delivery Report 0 = No delivery Report
//		sm.setReplace_if_present_flag(null);										// Replace on SMSC 
		sm.setData_coding(new Byte(internalSmsRequest.getEncoding()));				// codifica messaggio (ascii 7bit) o (UTF-8)
//		sm.setSm_default_msg_id(null);												// 
//		sm.setSm_length(null);														// lunghezza corpo messaggio


		Submit_resp resp;
		String[] smsIdS = null;
		int totSMSes = 1;
		if (!multipart) { 
			if(internalSmsRequest.isEncodingGSM338()) {
				logger.debug(sl.logIt(SmartLog.K_STEP, "Encoding GSM338").getLogRow(true));
				sm.setShort_message(new DefaultAlphabetEncoding().decodeString(internalSmsRequest.getMsgBytes()));
			} else if(internalSmsRequest.isEncodingUCS2()) {						
				logger.debug(sl.logIt(SmartLog.K_STEP, "Encoding UCS2").getLogRow(true));
				sm.setMessage_payload(Hex.encode(internalSmsRequest.getMsgBytes()));
		    } else throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Encoding message not yet implemented", new Byte(internalSmsRequest.getEncoding())});
			internalSmsResponse.setGatewayContacted(true);
			internalSmsResponse.setTotalSms(1);
        	resp = sendVodafone(myUUID, service, sm, gws, chronometer, 1);
     		smsIdS = new String[] {resp.getMessage_id()};
		} else {
			byte[] encodedBytes = 
					internalSmsRequest.isEncodingGSM338()
							? (new DefaultAlphabetEncoding().decodeString(internalSmsRequest.getMsgBytes())).getBytes()
							: internalSmsRequest.getMsgBytes();
			 
			// *** MULTIPART DEFINITION ***
			sm.setEsm_class(SM_UDH_GSM);											// set UDHI flag to true (enable concatenated messages)
	        byte multipartReference = nextMultipartReferenceNumber();
	    	// split msg in parts and send them
	    	int totParts = (int) Math.ceil( (1.0*encodedBytes.length) / MAX_BYTES_PER_PART );		// max length of each message part
//	    	logger.debug("Sending message as concatenated message (" + totParts + " parts)");

	    	ArrayList smsIdList = new ArrayList();
	    	int nByte = 0;
	    	internalSmsResponse.setGatewayContacted(true);
		 	internalSmsResponse.setTotalSms(totParts);
	    	for (int byteCount=0, partNumb = 1;
	    		 byteCount < encodedBytes.length;
	    		 byteCount += nByte, partNumb += 1) {
	    		
	    		nByte = Math.min(MAX_BYTES_PER_PART, encodedBytes.length - byteCount);
	    		byte[] part = new byte[nByte];
	    		System.arraycopy(encodedBytes, byteCount, part, 0, nByte);
	    		
	    		byte[] udh = UDH_TEMPLATE;
	    		udh[IX_REFERENCE_NUMBER] = multipartReference;							// not relevant since only 1 concatenated msg is sent
	    		udh[IX_TOT_PARTS] = (byte) totParts;
	    		udh[IX_PART_NUM] = (byte) partNumb;
	    		
				sm.setMessage_payload(Hex.encode(udh) + Hex.encode(part));										// the sms message part
	

//				logger.debug("Sending part " + partNumb + "/" + totParts + "...");
	
				// send sms		
		
	    		
	        	resp = sendVodafone(myUUID, service, sm, gws, chronometer, partNumb);
	        	smsIdList.add(resp.getMessage_id());
 	    	}
   		 	smsIdS = (String[]) smsIdList.toArray(new String[0]);
   		 	totSMSes = totParts;
	    }
		// Success
		smsResponse3.setSmsIds(smsIdS);
		smsResponse3.setCode(ConstantsSms.XAS00000I_CODE);
		smsResponse3.setMessage(MessageFormat.format(ConstantsSms.XAS00000I_MESSAGE2, new String[]{internalSmsRequest.getUuid(), Integer.toString(internalSmsRequest.getDstByteLength()), Integer.toString(totSMSes), internalSmsRequest.getEncodingDescription()}));
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow());
		}
		
		return internalSmsResponse;
	}

 	/**
 	 * Send sms via Vodafone service.
 	 * @param service
 	 * @param sm
 	 * @param gws
 	 * @return Submit_resp response from the service.
 	 * @throws XASException
 	 */
	private Submit_resp sendVodafone(String myUUID, GWService service, Submit_sm sm, GWSession gws, Chronometer chronometer, int index) throws XASException {
		Submit_resp resp;
        try {
    		chronometer.partial(Configuration.TYPE + ".send[" + index + "]." + ConstantsSplunk.K_TIME_SECTION_START);
    		try {
    			resp = service.submit(sm, gws);
    		} finally {
        		chronometer.partial(Configuration.TYPE + ".send[" + index + "]." + ConstantsSplunk.K_TIME_SECTION_END);
            }
         } catch (RemoteException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(myUUID + ":" + errors.toString());
    		throw new XASException(ConstantsSms.XAS04098E_MESSAGE, errors.toString(), ConstantsSms.XAS04098E_CODE, new Object[] {"RemoteException for " + Configuration.JNDI_POP_SOAP_URL, e.getCause()});
        } 
		if(resp == null)
			throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Null response object returned by Vodafone service"});

		if (resp.getCommand_status().intValue() != PacketStatus.OK) {
			// Failure
			String errorMessage = SMPPUtil.getSMPPErrorDescription(resp.getCommand_status().intValue());
			throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {errorMessage, resp.getError_code()});
		}  
		return resp;
	}
 	
	/**
	 * Get the URP from the JNDI.
	 * @return
	 * @throws XASException
	 */
   public static URL getVodafonePopURL() throws XASException {
		URL vodafonePopURL;
        try {
        	Context ctx = new InitialContext();
            vodafonePopURL= (URL)ctx.lookup(Configuration.JNDI_POP_SOAP_URL);
        } catch (NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	logger.error(errors.toString());
    		throw new XASException(ConstantsSms.XAS04098E_MESSAGE, errors.toString(), ConstantsSms.XAS04098E_CODE, new Object[] {"NamingException for " + Configuration.JNDI_POP_SOAP_URL, errors.toString()});
        }
        return vodafonePopURL;
    }
   }
