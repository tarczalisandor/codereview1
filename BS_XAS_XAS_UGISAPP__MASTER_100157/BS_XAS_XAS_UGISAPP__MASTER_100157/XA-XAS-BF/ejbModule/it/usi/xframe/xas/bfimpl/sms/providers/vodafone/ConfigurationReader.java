package it.usi.xframe.xas.bfimpl.sms.providers.vodafone;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.configuration.AbstractProviderConfigurationReader;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.ProviderData;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASException;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReader {
/*	DEPRECATED 2016.07.18	
	extends AbstractProviderConfigurationReader {
	
	private List accounts = new ArrayList();
	private List urls = new ArrayList();
	private boolean clientLogEnabled; 
	private SMPPAccount account;
	
 	 //  STATES 0x8000 to 0x8FFF are for providers readers
 
	public static final int NONE                                  = 0x8400;
	public static final int SMPP_CONFIG                           = 0x8401;
	
	public static final int CLIENT_LOG_ENABLED                    = 0x8410;
	public static final int SERVER_URLS                           = 0x8420;
	public static final int SERVER_URL                            = 0x8421;
	
	public static final int ACCOUNTS                              = 0x8430;
	public static final int ACCOUNT                               = 0x8431;
	public static final int ACCOUNT_SYSTEM_ID                     = 0x8432;
	public static final int ACCOUNT_PASSWORD                      = 0x8433;
	public static final int ACCOUNT_ADDRESS                       = 0x8434;
	public static final int ACCOUNT_ALIAS                         = 0x8435;
	
	private static String TAG_SMPP_CONFIG                         = "SMPPConfig";
	private static String TAG_CLIENT_LOG_ENABLED                  = "ClientLogEnabled";
	private static String TAG_SERVER_URLS                         = "ServerURLs";
	private static String TAG_SERVER_URL                          = "URL";

	private static String TAG_ACCOUNTS                            = "Accounts";
	private static String TAG_ACCOUNT                             = "Account";
	private static String TAG_ACCOUNT_SYSTEM_ID                   = "SystemId";
	private static String TAG_ACCOUNT_PASSWORD                    = "Password";
	private static String TAG_ACCOUNT_ADDRESS                     = "Address";
	private static String TAG_ACCOUNT_ALIAS                       = "Alias";

	private int state = NONE;
	
	public ConfigurationReader(Configuration mainConfig) {
		super(mainConfig);
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {

		switch (state) {
		
		case NONE:
			if (qName.equalsIgnoreCase(TAG_SMPP_CONFIG)) state = SMPP_CONFIG;
			else throw new XASConfigurationException("Invalid Tag in Vodafone Provider: " + qName);
			break;
		
		case SMPP_CONFIG:
			if (qName.equalsIgnoreCase(TAG_CLIENT_LOG_ENABLED)) state = CLIENT_LOG_ENABLED;
			else if (qName.equalsIgnoreCase(TAG_SERVER_URLS)) state = SERVER_URLS;
			else if (qName.equalsIgnoreCase(TAG_ACCOUNTS)) state = ACCOUNTS;
			else throw new XASConfigurationException("Invalid Tag in Vodafone SMPPConfig: " + qName);
			break;
		
		case SERVER_URLS:
			if (qName.equalsIgnoreCase(TAG_SERVER_URL)) state = SERVER_URL;
			else throw new XASConfigurationException("Invalid Tag in Vodafone ServerURLs: " + qName);
			break;
			
		case ACCOUNTS:
			if (qName.equalsIgnoreCase(TAG_ACCOUNT)) {
				state = ACCOUNT;
				account = new SMPPAccount();
				accounts.add(account);
			}
			else throw new XASConfigurationException("Invalid Tag in Vodafone Accounts: " + qName);
			break;
		
		case ACCOUNT:
			if (qName.equalsIgnoreCase(TAG_ACCOUNT_SYSTEM_ID)) state = ACCOUNT_SYSTEM_ID;
			else if (qName.equalsIgnoreCase(TAG_ACCOUNT_PASSWORD)) state = ACCOUNT_PASSWORD;
			else if (qName.equalsIgnoreCase(TAG_ACCOUNT_ADDRESS)) state = ACCOUNT_ADDRESS;
			else if (qName.equalsIgnoreCase(TAG_ACCOUNT_ALIAS)) state = ACCOUNT_ALIAS;
			else throw new XASConfigurationException("Invalid Tag in Vodafone Account: " + qName);
			break;
		
		case CLIENT_LOG_ENABLED:
		case SERVER_URL:
		case ACCOUNT_SYSTEM_ID:
		case ACCOUNT_PASSWORD:
		case ACCOUNT_ADDRESS:
		case ACCOUNT_ALIAS:
			throw new XASConfigurationException("Invalid tag " + qName + " in state " + state);

		default: throw new XASConfigurationException("Unrecognized state in Vodafone Configuration Reader: " + state + " opening tag " + qName);

		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		switch(state) {
		
		case NONE:
			ended = true;
			break;

		case SMPP_CONFIG:
			state = NONE; break; 

		case CLIENT_LOG_ENABLED:
		case SERVER_URLS:
		case ACCOUNTS:
			state = SMPP_CONFIG; break;
			
		case SERVER_URL:
			state = SERVER_URLS; break;
		
		case ACCOUNT:
			state = ACCOUNTS; break;

		case ACCOUNT_SYSTEM_ID:
		case ACCOUNT_PASSWORD:
		case ACCOUNT_ADDRESS:
		case ACCOUNT_ALIAS:
			state = ACCOUNT; break;
			
		default: throw new XASConfigurationException("Unrecognized state in Vodafone Configuration Reader: " + state + " closing tag " + qName);
		}

	}

	public void characters(char ch[], int start, int length)
	throws SAXException {

		String val = new String(ch, start, length);

		switch (state) {
		case CLIENT_LOG_ENABLED: clientLogEnabled = Boolean.getBoolean(val); break;
		case SERVER_URL: urls.add(val); break;
		case ACCOUNT_SYSTEM_ID: account.setId(val); break;
		case ACCOUNT_PASSWORD: account.setPassword(val); break;
		case ACCOUNT_ADDRESS: account.setAddress(val); break;
		case ACCOUNT_ALIAS: account.setAlias(val); break;

		case NONE:
		case SMPP_CONFIG:
		case SERVER_URLS:
		case ACCOUNTS:
		case ACCOUNT:
			 break; // nothing to do
		
		default: throw new XASConfigurationException("Unrecognized state in Vodafone Configuration Reader: " + state + " while reading node text");
		}

	};
	
	public List getAccounts() {
		return accounts;
	}
	
	public List getUrls() {
		return urls;
	}
	
	public boolean getClientLogEnabled() {
		return clientLogEnabled;
	}
 
	public GatewaySms createProvider(ProviderData providerData) throws XASException {
		GatewaySmsVodafone provider =  new GatewaySmsVodafone(providerData.getLogPrefix(), providerData.getSmsLogLevel(), false);
		provider.setUrls(urls);
		provider.setSMPPAccounts(accounts);
		provider.setClientLogEnabled(clientLogEnabled);
		return provider;
	}
	*/
}
