package it.usi.xframe.xas.util;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseDeliveryReport
{
	private static Logger log = Logger.getLogger(ParseDeliveryReport.class);

	private String mDestinator = null; 
	private String mMessageId = null; 
	private String mMessageState = null; 
	private HashMap mIds = new HashMap();
	private String mCurrentIdName = null; 
	private String mCurrentIdValue = null; 
		
	private static int NONE = 0;

	private static int ENVELOPE = 1;
	private static int ENVELOPE_BODY = 2;
	private static int ENVELOPE_BODY_MULTIREF = 3;
	private static int ENVELOPE_BODY_MULTIREF_MESSAGEID = 4;
	private static int ENVELOPE_BODY_MULTIREF_SOURCEADDR = 5;
	private static int ENVELOPE_BODY_MULTIREF_MESSAGESTATE = 6;
	private static int ENVELOPE_BODY_MULTIREF_UNKNOWNSUBTAG = 7;
	
	private static String TAG_ENVELOPE = "Envelope";
	private static String TAG_ENVELOPE_BODY = "Body";
	private static String TAG_ENVELOPE_BODY_MULTIREF = "multiRef";
	private static String ATT_ENVELOPE_BODY_MULTIREF_ID = "id";
	private static String TAG_ENVELOPE_BODY_MULTIREF_MESSAGEID = "receipted_message_id";
	private static String TAG_ENVELOPE_BODY_MULTIREF_SOURCEADDR = "source_addr";
	private static String TAG_ENVELOPE_BODY_MULTIREF_MESSAGESTATE = "message_state";
	private static String ATT_ENVELOPE_BODY_MULTIREF_MESSAGESTATE_HREF = "href";
	private static String TAG_ENVELOPE_BODY_MULTIREF_UNKNOWNSUBTAG = null;

	public void read(String soapMessage) throws Exception
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler()
			{
				int state = NONE;

				public void startElement(String uri,
						String localName,
						String qName,
						Attributes attributes)
						throws SAXException {

					//System.out.println("Start Element :" + qName);

					String tagname = normalizeQName(qName);
					
					if (state==NONE && tagname.equalsIgnoreCase(TAG_ENVELOPE))
						state = ENVELOPE;
					
					else if (state==ENVELOPE && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY))
						state = ENVELOPE_BODY;
					
					else if (state==ENVELOPE_BODY && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF)) {
						mCurrentIdName = attributes.getValue(ATT_ENVELOPE_BODY_MULTIREF_ID);
						//System.out.println("mCurrentIdName=" + mCurrentIdName);
						state = ENVELOPE_BODY_MULTIREF;
					}
					
					else if (state==ENVELOPE_BODY_MULTIREF && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_MESSAGEID))
						state = ENVELOPE_BODY_MULTIREF_MESSAGEID;

					else if (state==ENVELOPE_BODY_MULTIREF && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_SOURCEADDR))
						state = ENVELOPE_BODY_MULTIREF_SOURCEADDR;

					else if (state==ENVELOPE_BODY_MULTIREF && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_MESSAGESTATE)) {
						mMessageState = attributes.getValue(ATT_ENVELOPE_BODY_MULTIREF_MESSAGESTATE_HREF);
						state = ENVELOPE_BODY_MULTIREF_MESSAGESTATE;
					}

					// put unmkown as last tag
					else if (state==ENVELOPE_BODY_MULTIREF)
						state = ENVELOPE_BODY_MULTIREF_UNKNOWNSUBTAG;
				}

				public void endElement(String uri,
						String localName,
						String qName)
						throws SAXException {

					//System.out.println("End Element :" + qName);

					String tagname = normalizeQName(qName);

					if (state==ENVELOPE && tagname.equalsIgnoreCase(TAG_ENVELOPE))
						state = NONE;

					else if (state==ENVELOPE_BODY && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY))
						state = ENVELOPE;
					
					else if (state==ENVELOPE_BODY_MULTIREF && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF)) {
						if(mCurrentIdName!=null) {
							mIds.put("#"+mCurrentIdName, mCurrentIdValue);
							mCurrentIdName = null;
							mCurrentIdValue = null;
						}
						state = ENVELOPE_BODY;
					}
					
					else if (state==ENVELOPE_BODY_MULTIREF_MESSAGEID && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_MESSAGEID))
						state = ENVELOPE_BODY_MULTIREF;

					else if (state==ENVELOPE_BODY_MULTIREF_SOURCEADDR && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_SOURCEADDR))
						state = ENVELOPE_BODY_MULTIREF;

					else if (state==ENVELOPE_BODY_MULTIREF_MESSAGESTATE && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_MESSAGESTATE))
						state = ENVELOPE_BODY_MULTIREF;

					else if (state==ENVELOPE_BODY_MULTIREF_MESSAGESTATE && tagname.equalsIgnoreCase(TAG_ENVELOPE_BODY_MULTIREF_MESSAGESTATE))
						state = ENVELOPE_BODY_MULTIREF;

					else if (state==ENVELOPE_BODY_MULTIREF_UNKNOWNSUBTAG)
						state = ENVELOPE_BODY_MULTIREF;
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					
					String val = new String(ch, start, length);
					//System.out.println("characters : " + val);

					if (state==ENVELOPE_BODY_MULTIREF) {
						mCurrentIdValue = val;
						//System.out.println("mCurrentIdValue=" + mCurrentIdValue);
					}
					if (state==ENVELOPE_BODY_MULTIREF_SOURCEADDR)
						mDestinator = val;

					if (state==ENVELOPE_BODY_MULTIREF_MESSAGEID)
						mMessageId = val;

					if (state==ENVELOPE_BODY_MULTIREF_MESSAGESTATE)
						mMessageState = val;

				}
			};

			saxParser.parse(new InputSource(new StringReader(soapMessage)), handler);
			
			// convert message state if it's a #reference
			if(mMessageState.startsWith("#"))
				mMessageState = (String) mIds.get(mMessageState);

		} catch (Exception e) {
			log.error(e);
			throw e;
		}

	}

	public String normalizeQName(String arg)
	{
		String norm = null;
		String[] val = arg.split(":");
		if(val.length>1)
			norm = val[1];
		else
			norm = arg;
		return norm;
	}

	public String getDestinator()
	{
		return mDestinator;
	}

	public String getMessageId()
	{
		return mMessageId;
	}

	public String getMessageState()
	{
		return mMessageState;
	}
}
