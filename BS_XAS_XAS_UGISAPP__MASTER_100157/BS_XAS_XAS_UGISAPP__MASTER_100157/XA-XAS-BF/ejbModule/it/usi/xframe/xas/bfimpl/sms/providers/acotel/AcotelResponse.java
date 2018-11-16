/*
 * Created on Jan 21, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.acotel;

import it.usi.xframe.xas.bfutil.XASException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AcotelResponse extends DefaultHandler {
	/*	Example of response message from Acotel SMS Gateway:
	  
	   <?xml version="1.0"?>
	   <ROOT>
	   <RESULT>
	   <STATUS>0</STATUS>
	   </RESULT>
	   </ROOT>
		
	   <?xml version="1.0"?>
	   <ROOT>
	   <RESULT>
	   <STATUS>1</STATUS>
	   <TEXT>Attenzione! Specificare il Messaggio da inviare</TEXT>
	   </RESULT>
	   </ROOT>
	   
	*/

	/*	DEPRECATED 2016.07.18	
	private static final String ACOTEL_SMS_RESPONSE_EXCEPTION_CODE = "XAS.AcotelResponse.Error";
	private static Log log = LogFactory.getLog(AcotelResponse.class);


	private static int NODE_NULL = 0;
	private static int NODE_ROOT = 1;
	private static int NODE_RESULT = 2;
	private static int NODE_STATUS = 3;
	private static int NODE_TEXT = 4;
	private int status = NODE_NULL;
	private String smsStatus = "";
	private String smsStatusDescription = "SMS sent correctly";

	public AcotelResponse() {
	}

	public void verifyAcotelXMLResponse(String xmlResponse)	throws XASException {
		DefaultHandler handler = this;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(new ByteArrayInputStream(xmlResponse.getBytes()), handler);
			String msg = " status = " + smsStatus;
			msg += " description = " + smsStatusDescription;
			if ((status != NODE_NULL) || (! "0".equals(smsStatus))) {
				Exception e = new Exception(msg);
				log.error(e.getMessage(), e);
				throw new XASException(ACOTEL_SMS_RESPONSE_EXCEPTION_CODE, e);
			} else {
				log.info("SMS sent succesfully: " + msg);			
			}
			
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage(), e);
			throw new XASException(ACOTEL_SMS_RESPONSE_EXCEPTION_CODE, e);
		} catch (SAXException e) {
			log.error(e.getMessage(), e);
			throw new XASException(ACOTEL_SMS_RESPONSE_EXCEPTION_CODE, e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new XASException(ACOTEL_SMS_RESPONSE_EXCEPTION_CODE, e);
		}
	}

	public void startElement(
		String namespaceURI,
		String lName,
		String qName,
		Attributes attrs)
		throws SAXException {
		log.info(
			"*** startElement()->|status|qname| : |"
				+ status
				+ "|,|"
				+ qName
				+ "|");
		if (qName.equals("ROOT") && status == NODE_NULL) {
			status = NODE_ROOT;
		} else if (qName.equals("RESULT") && status == NODE_ROOT) {
			status = NODE_RESULT;
		} else if (qName.equals("STATUS") && status == NODE_RESULT) {
			status = NODE_STATUS;
		} else if (qName.equals("TEXT") && status == NODE_RESULT) {
			status = NODE_TEXT;
		}
	}

	public void endElement(String namespaceURI, String sName, String qName)
		throws SAXException {
		log.info(
			"*** endElement()->|status|qname| : |"
				+ status
				+ "|,|"
				+ qName
				+ "|");
		if (qName.equals("ROOT") && status == NODE_ROOT) {
			status = NODE_NULL;
		} else if (qName.equals("RESULT") && status == NODE_RESULT) {
			status = NODE_ROOT;
		} else if (qName.equals("STATUS") && status == NODE_STATUS) {
			status = NODE_RESULT;
		} else if (qName.equals("TEXT") && status == NODE_TEXT) {
			status = NODE_RESULT;
		}
	}

	public void characters(char buf[], int offset, int len)
		throws SAXException {
		if (status == NODE_STATUS) {
			this.smsStatus = new String(buf, offset, len);
			log.info("*** read |smsStatus| : |" + this.smsStatus + "|");
		}

		if (status == NODE_TEXT) {
			this.smsStatusDescription = new String(buf, offset, len);
			log.info(
				"*** read |smsStatusDescription| : |"
					+ this.smsStatusDescription
					+ "|");
		}
	}

	public String getSmsStatus() {
		return smsStatus;
	}

	public String getSmsStatusDescription() {
		return smsStatusDescription;
	}
*/
}
