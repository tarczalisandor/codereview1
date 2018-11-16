package it.usi.xframe.xas.bfimpl.sms.providers.acotel;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.configuration.AbstractProviderConfigurationReader;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.ProviderData;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASException;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReader {

/*	DEPRECATED 2016.07.18	
extends AbstractProviderConfigurationReader {
	
	ArrayList pages = new ArrayList();
	String phonePattern;
	String pageUrl;
	String pageDesc;
	String defaultUrl;
	
	 //  STATES 0x8000 to 0x8FFF are for providers readers

	public static final int NONE                                  = 0x8300;

	public static final int PAGE                                  = 0x8310;
	public static final int PAGE_DESC		                      = 0x8311;
	public static final int PAGE_PHONE_RE                         = 0x8312;
	public static final int PAGE_URL                              = 0x8313;
	
	public static final int DEFAULT_URL                           = 0x8320;

	private static String TAG_PAGE                                = "Page";
	private static String TAG_PAGE_DESC                           = "Desc";
	private static String TAG_PAGE_PHONE_RE                       = "PhoneRE";
	private static String TAG_PAGE_URL                            = "Url";

	private static String TAG_DEFAULT_URL                         = "Url";

	private int state = NONE;
	
	public ConfigurationReader(Configuration mainConfig) {
		super(mainConfig);
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {

		switch (state) {
		
		case NONE:
			if (qName.equalsIgnoreCase(TAG_PAGE)) {
				state = PAGE;
				phonePattern = null; pageUrl = null; pageDesc = null;
			}
			else if (qName.equalsIgnoreCase(TAG_DEFAULT_URL)) state = DEFAULT_URL;
			else throw new XASConfigurationException("Invalid Tag in Acotel Provider: " + qName);
			break;
		
		case PAGE:
			if (qName.equalsIgnoreCase(TAG_PAGE_DESC)) state = PAGE_DESC;
			else if (qName.equalsIgnoreCase(TAG_PAGE_PHONE_RE)) state = PAGE_PHONE_RE;
			else if (qName.equalsIgnoreCase(TAG_PAGE_URL)) state = PAGE_URL;
			else throw new XASConfigurationException("Invalid Tag in Acotel Page: " + qName);
			break;
			
		case PAGE_DESC:
		case PAGE_PHONE_RE:
		case PAGE_URL:
		case DEFAULT_URL:
			throw new XASConfigurationException("Invalid tag " + qName + " in state " + state);

		default: throw new XASConfigurationException("Unrecognized state in Acotel Configuration Reader: " + state + " opening tag " + qName);

		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		switch(state) {
		
		case NONE:
			ended = true;
			break;

		case PAGE:
			AcotelPageDestination page = new AcotelPageDestination(phonePattern, pageUrl, pageDesc);
			pages.add(page);
			state = NONE; break;
		case DEFAULT_URL:
			state = NONE; break; 
		
		case PAGE_DESC:
		case PAGE_PHONE_RE:
		case PAGE_URL:
			state = PAGE; break;
			
		default: throw new XASConfigurationException("Unrecognized state in Acotel Configuration Reader: " + state + " closing tag " + qName);
		}

	}

	public void characters(char ch[], int start, int length)
	throws SAXException {

		String val = new String(ch, start, length);

		switch (state) {
		case PAGE_DESC: pageDesc = val; break;
		case PAGE_PHONE_RE: phonePattern = val; break;
		case PAGE_URL: pageUrl = val; break;
		case DEFAULT_URL: defaultUrl = val; break;

		case NONE:
		case PAGE:
			 break; \\ nothing to do
		
		default: throw new XASConfigurationException("Unrecognized state in Acotel Configuration Reader: " + state + " while reading node text");
		}

	};
	
	public GatewaySms createProvider(ProviderData providerData) throws XASException {
		AcotelPageDestination[] pageDestinations = new AcotelPageDestination[pages.size()];
		pageDestinations = (AcotelPageDestination[])pages.toArray( pageDestinations );
		GatewaySms provider = new GatewaySmsAcotel(providerData.getLogPrefix(), pageDestinations, defaultUrl, providerData.getSmsLogLevel());
		return provider;
	}
	*/
}
