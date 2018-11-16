/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.vodafone;

import ie.omk.smpp.Address;
import ie.omk.smpp.Connection;
import ie.omk.smpp.message.BindResp;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SMPPResponse;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.SubmitSMResp;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.util.AlphabetEncoding;
import ie.omk.smpp.util.DefaultAlphabetEncoding;
import ie.omk.smpp.util.GSMConstants;
import ie.omk.smpp.util.UCS2Encoding;
import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;
import it.usi.xframe.xas.util.xml.StructData;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.soap.encoding.Hex;
//import it.usi.xframe.xas.util.xml.data.StructData;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewaySmsVodafone {
/*	DEPRECATED 2016.07.18	
	extends GatewaySms {
	private static Log log = LogFactory.getLog(GatewaySmsVodafone.class);
	public static final String TYPE = "vodafonesmpp";
	
	
	// max size (in byte) for PDU payload when used with GSM transport
	private static final int PDU_PAYLOAD_MAX_SIZE = 140; //254;	

	// singleton object for this class
	//private static GatewaySmsVodafone singleton = new GatewaySmsVodafone();
	
	// log info
	private String logprefix;
	private String smsloglevel;
	
	// Config data loaded
	private StructData configData = null;
	
	// New configuration
	private List SMPPAccounts;
	private List urls;
	private boolean clientLogEnabled;

	private static byte smppReferenceNumber;
	static{
		Random rnd = new Random();
		smppReferenceNumber = (byte)rnd.nextInt(256);
	}
	
	public GatewaySmsVodafone(String logprefix, String smsloglevel) {
		this(logprefix, smsloglevel, true);
	}

	public GatewaySmsVodafone(String logprefix, String smsloglevel, boolean readOldConfig) {
		this.logprefix = logprefix;
		this.smsloglevel = smsloglevel;
	
		if (readOldConfig)	configData = loadConfigFile();
	}

	public void sendSms(SmsMessage message, SmsSenderInfo sender, String userid, Boolean isInRole) throws XASException {
		sendSms(message, sender, userid);
	}

	public void sendSms(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException {  
    	
    	// normalize input params (phone number, etc.)
    	normalizeInputInfo(sms);
    	
    	// bind remote service
    	SMPPSession session = getSMPPSession(sms, sender, userid);
    	
    	// sender ask for an alphanumeric alias to be used ?
		boolean useSenderAlias = (sender!=null && sender.getAlias()!=null) ?true :false;
    	
		try {
			// send single message synchronously
			sendSMPPMessage(session, sms, sender, useSenderAlias, userid);
		} finally {
			// unbind 
			releaseSMPPSession(session);
		}
    }
    
    
     //* Gets a SMPP session (aka Connection).
     //* 
     //* @param sms
     //* @param userid - UBIS user requester (for logging purposes)
     //* @return
     //* @throws XASException
     private SMPPSession getSMPPSession(SmsMessage sms, SmsSenderInfo sender, String userid) throws XASException  {
    	
    	SMPPSession smppSession = new SMPPSession();
		try {
			SMPPAccount[] accounts = getAvailableSMPPAccounts(sender);
			if (accounts==null || accounts.length==0)
				throw new XASException("No account satisfies your request!");
			
			int idxSelectedAccount = 0;
			int tries = accounts.length;
			// try to bind a session
			while (true) {
				
				//*String serverURL = getServerURL();
				//*String host = getHostFromURL( serverURL );
				//*int port = getPortFromURL( serverURL );
				//*conn = new Connection(host, port);
				//conn.autoAckLink(true);
				//conn.autoAckMessages(true);
	
				// try bind with selected account
				//*account = accounts[idxSelectedAccount];
				//*log.debug("Binding to the SMSC with account ["+account.getId()+"]");
	            //*BindResp resp = conn.bind(
	               //*     Connection.TRANSMITTER,
	               //*     account.getId(), //systemID
	               //*     account.getPassword(), //password
	               //*    null //systemType (not needed)
	               //*     );
				
				// try bind with selected account
				smppSession.setAccount( accounts[idxSelectedAccount] );
				BindResp resp = bindSMPPService(smppSession);
				if (resp!=null) {
		            if (resp.getCommandStatus() != BindResp.ESME_ROK) {
		            	String errDesc = SMPPUtil.getSMPPErrorDescription(resp.getCommandStatus());
		                log.error("SMSC bind failed: "+errDesc);
		                //throw new XASException("SMSC bind failed: "+errDesc);
		            }
		            if (smppSession.getConnection().getState()==Connection.BOUND) {
		            	log.debug("Bind successful");
		            	break; // bounded
		            } 
				}
	           
            	if (--tries==0) {
            		log.error("No more tries in binding. Give up with an exception!");
            		throw new XASException("Impossible connect remote sms provider");
            	}
            	// try again with another account
            	idxSelectedAccount=++idxSelectedAccount%accounts.length;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// log error
			securityLogger.errorLog(e.getMessage(), logprefix, sms, sender, smsloglevel, userid);
			throw new XASException(e);
		}

		return smppSession;
    }

     
     //* Synchronously send a sms in a given session.
     //* 
     //* @param session
     //* @param sms
     //* @param userid
     //* @throws XASException
     private void sendSMPPMessage(SMPPSession session, SmsMessage sms, SmsSenderInfo sender, boolean useSenderAlias, String userid) throws XASException {
    	
		try {
			Connection conn = session.getConnection();
			SMPPAccount account = session.getAccount();
			
			// Submit message
	        SubmitSM sm = (SubmitSM) conn.newInstance(SMPPPacket.SUBMIT_SM);

	        if (useSenderAlias)
	        {
	        	// source address (string alias)
		        sm.setSource( new Address(	GSMConstants.GSM_TON_ALPHANUMERIC, // sourceTON 
											GSMConstants.GSM_NPI_UNKNOWN, //sourceNPI
											account.getAlias() //sourceAddress
										 ) 
		        			);
	        }
	        else
	        {
		        // source address (internal Vodafone number)
		        sm.setSource( new Address(	GSMConstants.GSM_TON_SUBSCRIBER, // sourceTON 
											GSMConstants.GSM_NPI_E164, //sourceNPI
											account.getAddress() //sourceAddress
										 ) 
		        			);
	        }
	        // destination address
			sm.setDestination( new Address(	GSMConstants.GSM_TON_INTERNATIONAL, // destinationTON 
											GSMConstants.GSM_NPI_E164, // destination NPI
											sms.getPhoneNumber()
										  ) 
							 );

			AlphabetEncoding encoding = new DefaultAlphabetEncoding();	// default GSM 03.38 encoding
			// test message for default encoding
			String self = encoding.decodeString(encoding.encodeString(sms.getMsg()));
			boolean useDefaultEncoding = (sms.getMsg().equals(self)) ?true :false;
			if (useDefaultEncoding==true)
				log.debug("Use default encoding (GSM 03.38)");
			else {
				encoding = new UCS2Encoding();	// Unicode
				log.debug("Use Unicode encoding (UCS2) ");
			}
			
			byte[] msgBytes = encoding.encodeString(sms.getMsg());
			SMPPResponse smr = null;
			if ( msgBytes.length>=PDU_PAYLOAD_MAX_SIZE ) {
				// long msg to be splitted into concatenated msg
				smr = sendMessageAsConcatenated(conn, sm, msgBytes, encoding);
			} else {
				// msg can fit into a single sms 
				sm.setMessage(msgBytes, encoding);
				smr = (SubmitSMResp) conn.sendRequest(sm);
				if ( smr.getCommandStatus()!=SubmitSMResp.ESME_ROK ) {
					String errDesc = SMPPUtil.getSMPPErrorDescription(smr.getCommandStatus());
					log.error(errDesc);
					throw new Exception(errDesc);
				}
				log.debug("Submitted message ID: " + smr.getMessageId());
			}
			
			// log if no error
			String status = SMPPUtil.getSMPPErrorDescription(smr.getCommandStatus());
			securityLogger.securityLog(logprefix, sms, sender, status, smsloglevel, userid);
		} catch (Exception e) {
			e.printStackTrace();
			// log error
			securityLogger.errorLog(e.getMessage(), logprefix, sms, sender,  smsloglevel, userid);
			throw new XASException(e);
		} 
        
    }
 
    private synchronized byte nextReferenceNumber() {
		return ++GatewaySmsVodafone.smppReferenceNumber ;
	}

	private SMPPResponse sendMessageAsConcatenated(Connection conn, SubmitSM sm, byte msg[], AlphabetEncoding encoding) throws Exception{
    	final byte UDH_LEN = 0x05; // UDH length (5 bytes)
    	final byte NUMBERING_8BITS = 0x00;	// 8bit numbering for concatenated message
    	final byte INFO_LEN = 0x03;	// info length (3 more bytes)
    	
    	final int REFERENCE_NUMBER_IDX = 3;
    	final int TOT_PARTS_IDX = 4;
    	final int PART_NUM_IDX = 5;
    	
    	byte[] udh_template = {
    			UDH_LEN,
    			NUMBERING_8BITS,	
    			INFO_LEN,
				0x00,	// reference number for this concatenated message
				0x00,	// total parts number 
				0x00	// current part number 
		};
    	
    	
    	sm.setEsmClass(0x40);	//set UDHI flag to true (enable concatenated messages)
    	sm.setRegistered(1);	// set Delivery Report
    	byte reference_number = nextReferenceNumber();
    	
    	SMPPResponse smr = null;
    	
    	// split msg in parts and send them
    	int maxBytesPerPart = PDU_PAYLOAD_MAX_SIZE - udh_template.length;
    	int totParts = (int) Math.ceil( (1.0*msg.length)/maxBytesPerPart );
    	log.debug("Sending message as concatenated message ("+totParts+" parts)");
    	int count=0;
    	int partNumb = 1;
    	while (count<msg.length) {
    		int n = Math.min(maxBytesPerPart, msg.length-count);
    		log.debug("Part payload: "+n);
    		byte[] part = new byte[n];
    		System.arraycopy(msg, count, part, 0, n);
    		
    		byte[] udh = udh_template;
    		udh[REFERENCE_NUMBER_IDX] = reference_number;	// not relevant since only 1 concatenated msg is sent
    		udh[TOT_PARTS_IDX] = (byte) totParts;
    		udh[PART_NUM_IDX] = (byte) partNumb;
    		log.debug("UDH: "+Hex.encode(udh));
    		
    		byte[] partPayload = concatBytes(udh, part);
    		sm.setMessage( partPayload, encoding );
    		// send single part
    		log.debug("Sending part "+partNumb+"/"+totParts+"...");
    		smr = (SubmitSMResp) conn.sendRequest(sm);
    		if ( smr.getCommandStatus()!=SubmitSMResp.ESME_ROK ) {
    			String errDesc = SMPPUtil.getSMPPErrorDescription(smr.getCommandStatus());
    			log.error(errDesc);
    			throw new Exception(errDesc);
    		}
    		
    		++partNumb;
    		count += n;
    	}
    	return smr;
    }
    
    
    //* Close a session (aka Connection).
    //* 
    //* @param session
    //*
    private void releaseSMPPSession(SMPPSession session) {
    	try {
			// Unbind.
            UnbindResp ubr = session.getConnection().unbind();
			 if (ubr.getCommandStatus() == UnbindResp.ESME_ROK) {
	                log.debug("Successfully unbound from the SMSC");
	         } else {
	                log.error("There was an error unbinding.");
	         }
		} catch (Exception e) {
			// no handling for exception during unbind 
			e.printStackTrace();
		}
    }
    

	
    private static void normalizeInputInfo(SmsMessage sms) throws XASException {
    	// remove spaces from phone number 
    	sms.setPhoneNumber( sms.getPhoneNumber().trim() );
    }
    
     //* Loads in memory Vodafone SMPP config file.
     //*  
     //* @return
     //*
	private static StructData loadConfigFile() {
		StructData config = null;
		try {
			Context ctx = new InitialContext();
			URL fileURL = (URL)ctx.lookup("java:comp/env/url/VodafoneSmppConfig"); 
			log.info("JNDI url/VodafoneSmppConfig ["+fileURL.toString()+"]");
			
			config = StructData.loadStructDataFromXML(new File(fileURL.getFile()));
			//log.debug(config.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return config;
	}
	
	
	private BindResp bindSMPPService(SMPPSession session) throws Exception {
		
		if (configData==null) {
			String err = "No SMPP config file loaded!";
			throw new Exception(err);
		}
		StructData urls = configData.getByKey("ServerURLs");
		if ( urls.isNull() ) {
			String err = "No 'ServerURLs' found in SMPP config file!";
			throw new Exception(err);
		}
		
		SMPPAccount account = session.getAccount();
		
		Connection conn = null;
		BindResp bindResp = null;
		Random rnd = new Random();
		int idx = rnd.nextInt(urls.size());
		
		// look for first valid url
		for (int c=urls.size(); c>0 ;--c, idx=(++idx)%urls.size()) {
			String url = null;
			try {
				url =  urls.getByIndex(idx).getString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ( url==null )
				continue;	// skip it
			
			// test url for binding
			try {
				String host = getHostFromURL( url );
				int port = getPortFromURL( url );
				conn = new Connection(host, port);
				
				// try bind with selected account
				log.debug("Binding to the SMSC with account ["+account.getId()+"]");
				bindResp = conn.bind(
		                Connection.TRANSMITTER,
		                account.getId(), //systemID
		                account.getPassword(), //password
		                null //systemType (not needed)
		                );
			} catch (Exception e) {
				//e.printStackTrace();
				log.error("Bind with url ["+url+"] not possible: "+e);
				if (conn!=null) {
					try {
						conn.unbind();
					} catch (Exception e1) {}
					conn = null;
				}
				continue;	//skip it
			}
			
			// Valid Connection ... set it back
			if (conn!=null) {
				session.setConnection( conn );
				return bindResp;
			}
		}
		
		// binding impossible 
		return null;
	}
	

	
	 //* Select a set of accounts suitable for the request.
	 //* For example if sender wants to send sms with alias 'Unicredit'
	 //* this method selects only accounts with that alias enabled.
	 //*  
	 //* sender - sender info (ex. alias, ABI code)
	 //* @return
	 //* @throws Exception
	private SMPPAccount[] getAvailableSMPPAccounts(SmsSenderInfo sender) throws Exception {
		if (configData==null) {
			String err = "No SMPP config file loaded!";
			throw new Exception(err);
		}
		String senderAlias = (sender!=null)?sender.getAlias() :null;	// 'null' if no alias is set by sender
		
		Vector accounts = new Vector();
		StructData list = configData.getByKey("Accounts");
		for (int i=0; i<list.size() ;++i) {
			StructData acc = list.getByIndex(i);
			
			SMPPAccount smppAcc = new SMPPAccount();
			smppAcc.setId( acc.getByKey("SystemId").getString() );
			smppAcc.setPassword( acc.getByKey("Password").getString() );
			smppAcc.setAddress( acc.getByKey("Address").getString() );
			smppAcc.setAlias( acc.getByKey("Alias").getString() );
			
			if ( senderAlias==null || 	// no alias -> account is always ok
				 senderAlias.equals(smppAcc.getAlias()) )	// only account with matching alias are ok 
				accounts.add( smppAcc );
		}
		
		return (SMPPAccount[]) accounts.toArray(new SMPPAccount[accounts.size()]);
	}
	
	
	private static String getHostFromURL(String url) {
		String host_port = url.substring( url.indexOf("://")+3 );
		return host_port.split(":")[0];
	}
	
	private static int getPortFromURL(String url) throws Exception {
		String host_port = url.substring( url.indexOf("://")+3 );
		String toks[] =  host_port.split(":");
		if (toks.length==2)
			return Integer.parseInt(toks[1]);
		else
			throw new Exception("No port available for remote Vodafone server");
	}
	
	private static byte[] concatBytes(byte aa[], byte[] bb) {
		byte res[] = new byte[aa.length+bb.length];
		System.arraycopy(aa, 0, res, 0,        aa.length);
		System.arraycopy(bb, 0, res, aa.length, bb.length);
		
		return res;
	}
	
	public List getSMPPAccounts() {
		return SMPPAccounts;
	}

	public void setSMPPAccounts(List sMPPAccounts) {
		SMPPAccounts = sMPPAccounts;
	}

	public List getUrls() {
		return urls;
	}

	public void setUrls(List urls) {
		this.urls = urls;
	}

	public boolean isClientLogEnabled() {
		return clientLogEnabled;
	}

	public void setClientLogEnabled(boolean clientLogEnabled) {
		this.clientLogEnabled = clientLogEnabled;
	}

*/	
}


