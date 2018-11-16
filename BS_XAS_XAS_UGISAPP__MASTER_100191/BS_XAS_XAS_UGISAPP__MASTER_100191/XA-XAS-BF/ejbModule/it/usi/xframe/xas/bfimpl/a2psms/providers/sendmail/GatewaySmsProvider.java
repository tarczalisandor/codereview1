package it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail;

import ie.omk.smpp.util.DefaultAlphabetEncoding;
import ie.omk.smpp.util.GSMConstants;
import ie.omk.smpp.util.UCS2Encoding;
import it.eng.service.mgp.sms.GsmEncoding;
import it.eng.service.mgp.sms.MtMessage;
import it.eng.service.mgp.sms.MtSendingResult;
import it.eng.service.mgp.sms.Reason;
import it.eng.service.mgp.sms.Result;
import it.usi.xframe.xas.bfimpl.SendEmail;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfimpl.a2psms.providers.GatewayA2Psms;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.ConstantsSplunk;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.EmailMessage;
import it.usi.xframe.xas.util.json.XConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

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
			  : (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, GatewaySmsProvider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendMessage");
		
		if (logger.isDebugEnabled()) { 
	 		sl.preset("myPreset");
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
					, "a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest)
					, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
					, "a_provider", XConstants.XSTREAMER.toXML(provider)
					, "a_originator", XConstants.XSTREAMER.toXML(originator)
					).getLogRow(true)); // Debug and keep row
			sl.reload("myPreset");
		}
		
		// WARNING: using direct lookup to enable the WSSE Outbound handler to be functional and override the standard IBM wsse.
		
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
		    } else throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Encoding message not yet implemented", new Byte(internalSmsRequest.getEncoding())});
			int nByte = part.length;
			internalSmsResponse.setGatewayContacted(true);
			internalSmsResponse.setTotalSms(1);
			logger.debug(sl.logIt("a_step","sendTelecom", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(mtMessage)).getLogRow(true));
			logger.info(sl.reload("myPreset").logIt("a_step","sendMail"
					, "a_correlationId", internalSmsRequest.getCorrelationID()
					, "a_uuid", internalSmsRequest.getUuid()
					, "a_txId", mtMessage.getTxId()
					, "a_destAddr", mtMessage.getDestAddr()
					, "a_byte", Integer.toString(nByte)
					, "a_encoding", internalSmsRequest.getEncodingDescription()
					).getLogRow(true));
        	mtSendingResult = sendMail(myUUID, xasUser, provider, originator, mtMessage, 1, internalSmsRequest.getMsg(), chronometer, 1);
     		smsIdS = new String[] {mtSendingResult.getSmsId()};
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
		 	internalSmsResponse.setTotalSms(totParts);
	    	for (int byteCount=0, partNumb = 1;
	    		 byteCount < encodedBytes.length;
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
				
	        	mtSendingResult = sendMail(myUUID, xasUser, provider, originator, mtMessage, totParts, internalSmsRequest.getMsg(), chronometer, partNumb);
	        	smsIdList.add(mtSendingResult.getSmsId());
 	    	}
   		 	smsIdS = (String[]) smsIdList.toArray(new String[0]);
   		 totSMSes = totParts;
	    }
		// Success
		internalSmsResponse.setSmsIds(smsIdS);
		internalSmsResponse.setCode(ConstantsSms.XAS00000I_CODE);
		internalSmsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00000I_MESSAGE2, new String[]{internalSmsRequest.getUuid(), Integer.toString(internalSmsRequest.getDstByteLength()), Integer.toString(totSMSes), internalSmsRequest.getEncodingDescription()}));
		
		if (logger.isDebugEnabled()) { 
			logger.debug(sl.reload("myPreset").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, "a_internalSmsResponse", XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow());
		}
		
		return internalSmsResponse;
	}

 	private static int mailId = 0;
 	/**
 	 * Send sms via Telecom service.
 	 * @param service
 	 * @param sm
 	 * @param gws
 	 * @return Submit_resp response from the service.
 	 * @throws XASException
 	 */
	private MtSendingResult sendMail(String myUUID, XasUser xasUser, Provider provider, Originator originator, MtMessage mtMessage, int totParts, String message, Chronometer chronometer, int index) throws XASException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, GatewaySmsProvider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
        .logIt(SmartLog.K_METHOD, "sendMail");
	
	sl.preset("myPreset");
		MtSendingResult mtSendingResult = null;
		Customization customization = (Customization)provider.getCustomization();
		
		EmailMessage emailMsg = new EmailMessage();
		String messagePart = null;
		try {
			messagePart = 
	        	GsmEncoding.GSM7.equals(mtMessage.getPayloadUddc()) ? (new DefaultAlphabetEncoding()).decodeString(mtMessage.getPayloadUd())
	        			: GsmEncoding.UCS2.equals(mtMessage.getPayloadUddc()) ?(new UCS2Encoding()).decodeString(mtMessage.getPayloadUd())
	        					: "unknown encoding " + mtMessage.getPayloadUddc().toString();
        } catch (UnsupportedEncodingException e1) {
	        // TODO Auto-generated catch block
        	messagePart = "UnsupportedEncodingException";
        }
		emailMsg.setMailSubject("Send Sms Request from " + xasUser.getName() + " to " + mtMessage.getDestAddr() + " " + mtMessage.getPartNum() + "/" + mtMessage.getNumOfParts() + ": " + message);
		String mailTo = customization.get(xasUser.getNameMedium());
		if (mailTo == null)
			mailTo = customization.get(xasUser.getNameSmall());
		if (mailTo == null) 
			throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"No mail address defined for " + xasUser.getNameMedium() + " or " + xasUser.getNameSmall(), "N/A"});

		logger.debug(sl.reload("myPreset").logIt("a_xasUser", xasUser.getNameMedium(), "a_mailTo", mailTo).getLogRow());

		emailMsg.setMailTo(mailTo);
		emailMsg.setMailFrom(ConstantsSms.XAS_MAIL_SENDER);
		emailMsg.setHtml(true);
		// Print pretty configuration
		final XStream xstreamer = new XStream(new JsonHierarchicalStreamDriver() { 
		       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
		         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
		                              new JsonWriter.Format(XConstants.XSTREAMER_PRETTY_DELIMITER, XConstants.XSTREAMER_PRETTY_NEWLINE,JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
		         } 
		     });
		
		String body = "You have received this message because your email address is registered in XA-XAS configuration.<br/><br/>"
			+ "<br/>Message part:<i>" + messagePart+ "</i><br/>"
			+ "<br/>Warning: multipart message will be recomposed on mobile phone and full message will be displayed (as in the subject of this email) but all multiparts message will be payed<br/>"
			+ "<br/>smsRequest:<br/><pre>"
			+ xstreamer.toXML(mtMessage)
			+ "</pre>"
			+ "<br/>xasUser:<br/><pre>"
			+ xstreamer.toXML(xasUser)
			+ "</pre>"
			+ "<br/>provider:<br/><pre>"
			+ xstreamer.toXML(provider)
			+ "</pre>"
			+ "<br/>originator:<br/><pre>"
			+ xstreamer.toXML(originator)
			+ "</pre>"
			;
        emailMsg.setMailMessage(body);
		SendEmail email = SendEmail.getInstance();
		InternalSmsResponse internalSmsResponse = new InternalSmsResponse();
		try {
			String[] noSmsIdS;
			if (!mailTo.endsWith("@no.where")) { 
        		chronometer.partial(Configuration.TYPE + ".send[" + index + "]." + ConstantsSplunk.K_TIME_SECTION_START);
 				email.sendMessage(emailMsg, null, "XA-XAS", true);
        		chronometer.partial(Configuration.TYPE + ".send[" + index + "]." + ConstantsSplunk.K_TIME_SECTION_END);
				noSmsIdS = new String[] {"MailSent"};
				internalSmsResponse.setGatewayContacted(true);
				internalSmsResponse.setTotalSms(1);
			} else {
				noSmsIdS = new String[] {"MailSent to no.where"};
				internalSmsResponse.setGatewayContacted(false);
				internalSmsResponse.setTotalSms(1);
			}
			internalSmsResponse.setSmsIds(noSmsIdS);
			internalSmsResponse.setCode(ConstantsSms.XAS00000I_CODE);
			internalSmsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00000I_MESSAGE2, new String[]{myUUID, Integer.toString(mtMessage.getPayloadUd().length), Integer.toString(totParts), mtMessage.getPayloadUddc().getValue()}));
			mtSendingResult = new MtSendingResult();
			mtSendingResult.setResult(Result.OK);
			mtSendingResult.setSmsId("MailId" + mailId);
			mailId++;
        } catch (XASException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
        	internalSmsResponse = new InternalSmsResponse(ConstantsSms.XAS04098E_CODE, ConstantsSms.XAS04098E_MESSAGE, new String[] {errors.toString()}, null);
			mtSendingResult = new MtSendingResult();
			mtSendingResult.setResult(Result.KO);
			mtSendingResult.setReason(Reason.UNEXPECTED_ERROR);
			mtSendingResult.setSmsId("MailId" + mailId);
			mailId++;
        }

		if (Result.KO.equals(mtSendingResult.getResult())) {
			// Failure
			String errorMessage = mtSendingResult.getReason().getValue();
			throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {errorMessage, mtSendingResult.getReason()});
		}  
		return mtSendingResult;
	}
}
