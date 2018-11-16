package it.usi.xframe.xas.util;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserSplunk
{
	private static Logger log = Logger.getLogger(SaxParserSplunk.class);

	private ArrayList columns			= new ArrayList();
	private ArrayList rows 				= new ArrayList();
	private ArrayList current_row		= null;
	private String currentColumnName	= null;
	private boolean debug 				= false;
	
	private static int STATE_NONE								= 0;

	private static int STATE_RESULTS							= 1;
	private static int STATE_RESULTS_META						= 2;
	private static int STATE_RESULTS_META_FIELDORDER			= 3;
	private static int STATE_RESULTS_META_FIELDORDER_FIELD		= 4;
	private static int STATE_RESULTS_RESULT						= 5;
	private static int STATE_RESULTS_RESULT_FIELD				= 6;
	private static int STATE_RESULTS_RESULT_FIELD_VALUE			= 7;
	private static int STATE_RESULTS_RESULT_FIELD_VALUE_TEXT	= 8;
	private static int STATE_RESULTS_RESULT_FIELD_V				= 9;
	private static int STATE_UNKNOWN							= 99;
	
	private static String TAG_RESULTS							= "results";
	private static String ATT_RESULTS_PREVIEW					= "preview";
	private static String TAG_RESULTS_META						= "meta";
	private static String TAG_RESULTS_META_FIELDORDER			= "fieldOrder";
	private static String TAG_RESULTS_META_FIELDORDER_FIELD		= "field";
	private static String TAG_RESULTS_RESULT					= "result";
	private static String ATT_RESULTS_RESULT_OFFSET				= "offset";
	private static String TAG_RESULTS_RESULT_FIELD				= "field";
	private static String ATT_RESULTS_RESULT_FIELD_K			= "k";
	private static String TAG_RESULTS_RESULT_FIELD_VALUE		= "value";
	private static String TAG_RESULTS_RESULT_FIELD_VALUE_TEXT	= "text";
	private static String TAG_RESULTS_RESULT_FIELD_V			= "v";
	private static String TAG_UNKNOWN							= "";

	public SaxParserSplunk (boolean debug)
	{
		this.debug = debug;
	}
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
				int state = STATE_NONE;

				public void startElement(String uri,
						String localName,
						String qName,
						Attributes attributes)
						throws SAXException {

					if(debug) System.out.println("Event[status:" + state + "]: Start Element: " + qName);

					String tagname = normalizeQName(qName);
					
					if (state==STATE_NONE && tagname.equalsIgnoreCase(TAG_RESULTS))
						state = STATE_RESULTS;

					else if (state==STATE_RESULTS && tagname.equalsIgnoreCase(TAG_RESULTS_META))
						state = STATE_RESULTS_META;

					else if (state==STATE_RESULTS_META && tagname.equalsIgnoreCase(TAG_RESULTS_META_FIELDORDER))
						state = STATE_RESULTS_META_FIELDORDER;

					else if (state==STATE_RESULTS_META_FIELDORDER && tagname.equalsIgnoreCase(TAG_RESULTS_META_FIELDORDER_FIELD))
						state = STATE_RESULTS_META_FIELDORDER_FIELD;

					else if (state==STATE_RESULTS && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT)) {
						state = STATE_RESULTS_RESULT;
						current_row = new ArrayList();
					}

					else if (state==STATE_RESULTS_RESULT && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD))
					{
						currentColumnName = attributes.getValue(ATT_RESULTS_RESULT_FIELD_K);
						state = STATE_RESULTS_RESULT_FIELD;
					}
					
					else if (state==STATE_RESULTS_RESULT_FIELD && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_VALUE))
						state = STATE_RESULTS_RESULT_FIELD_VALUE;


					else if (state==STATE_RESULTS_RESULT_FIELD_VALUE && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_VALUE_TEXT))
						state = STATE_RESULTS_RESULT_FIELD_VALUE_TEXT;

					else if (state==STATE_RESULTS_RESULT_FIELD && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_V))
						state = STATE_RESULTS_RESULT_FIELD_V;

					else {
						if(debug) System.out.println("Event unknown[status:" + state + "]: Tag: " + tagname);
						state = STATE_UNKNOWN;
					}
				}

				public void endElement(String uri,
						String localName,
						String qName)
						throws SAXException {

					if(debug) System.out.println("Event[status:" + state + "]: End Element: " + qName);

					String tagname = normalizeQName(qName);

					if (state==STATE_RESULTS && tagname.equalsIgnoreCase(TAG_RESULTS))
						state = STATE_NONE;

					else if (state==STATE_RESULTS_META && tagname.equalsIgnoreCase(TAG_RESULTS_META))
						state = STATE_RESULTS;

					else if (state==STATE_RESULTS_META_FIELDORDER && tagname.equalsIgnoreCase(TAG_RESULTS_META_FIELDORDER))
						state = STATE_RESULTS_META;

					else if (state==STATE_RESULTS_META_FIELDORDER_FIELD && tagname.equalsIgnoreCase(TAG_RESULTS_META_FIELDORDER_FIELD))
						state = STATE_RESULTS_META_FIELDORDER;

					else if (state==STATE_RESULTS_RESULT && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT)) {
						rows.add(current_row);
						current_row = null;
						state = STATE_RESULTS;
					}

					else if (state==STATE_RESULTS_RESULT_FIELD && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD))
						state = STATE_RESULTS_RESULT;

					else if (state==STATE_RESULTS_RESULT_FIELD_VALUE && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_VALUE))
						state = STATE_RESULTS_RESULT_FIELD;

					else if (state==STATE_RESULTS_RESULT_FIELD_VALUE_TEXT && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_VALUE_TEXT))
						state = STATE_RESULTS_RESULT_FIELD_VALUE;

					else if (state==STATE_RESULTS_RESULT_FIELD_V && tagname.equalsIgnoreCase(TAG_RESULTS_RESULT_FIELD_V))
						state = STATE_RESULTS_RESULT_FIELD;

					else if (state==STATE_UNKNOWN)
						state = STATE_UNKNOWN;
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					
					String val = new String(ch, start, length);
					if(debug) System.out.println("Event[status:" + state + "]: Characters: " + val);

					if (state==STATE_RESULTS_META_FIELDORDER_FIELD) {
						if(debug) System.out.println("-->added column:" + val);
						columns.add(val);
					}
					
					if (state==STATE_RESULTS_RESULT_FIELD_VALUE_TEXT
						|| state==STATE_RESULTS_RESULT_FIELD_V) {
						if(debug) System.out.println("-->added column:" + currentColumnName + " value:" + val);
						//currentColumnName
						current_row.add(val);
					}

				}
			};

			saxParser.parse(new InputSource(new StringReader(soapMessage)), handler);

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

	public ArrayList getColumns()
	{
		return this.columns;
	}

	public ArrayList getRows()
	{
		return this.rows;
	}
}
