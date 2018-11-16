package it.usi.xframe.xas.bfimpl.sms.providers.virtual;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.configuration.AbstractProviderConfigurationReader;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.ProviderData;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReader { 
/*	DEPRECATED 2016.07.18	
	extends AbstractProviderConfigurationReader {
	
 	 //  STATES 0x8000 to 0x8FFF are for providers readers
 
	public static final int NONE                                  = 0x8200;

	private int state = NONE;
	
	public ConfigurationReader(Configuration mainConfig) {
		super(mainConfig);
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {

		switch (state) {
		
		case NONE:
			throw new XASConfigurationException("Invalid tag " + qName + " in state " + state);

		default: throw new XASConfigurationException("Unrecognized state in Virtual Configuration Reader: " + state + " opening tag " + qName);

		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		switch(state) {
		
		case NONE:
			ended = true;
			break;

		default: throw new XASConfigurationException("Unrecognized state in Virtual Configuration Reader: " + state + " closing tag " + qName);
		}

	}

	public void characters(char ch[], int start, int length)
	throws SAXException {

		String val = new String(ch, start, length);

		switch (state) {

		case NONE:
			 break; // nothing to do
		
		default: throw new XASConfigurationException("Unrecognized state in Virtual Configuration Reader: " + state + " while reading node text");
		}

	};
	
	public GatewaySms createProvider(ProviderData providerData) throws XASException {
		GatewaySms provider = new GatewaySmsVirtual(providerData.getLogPrefix(), providerData.getSmsLogLevel());
		return provider;
	}
	*/
}
