package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfutil.XASException;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigurationReader extends DefaultHandler {
	
	private static Log log = LogFactory.getLog(ConfigurationReader.class);
	
	private StringBuffer value = new StringBuffer("");
	private Configuration configuration;
	private String userId = null;
	private String clientId = null;
	private ProviderData currentProvider = null;

	public static final int NONE = 0x0000;

	public static final int CONFIGURATION = 0x1000;

	public static final int CACHE_TIMEOUT = 0x1010;
	public static final int JMS_TIME_ZONE = 0x1020;
	public static final int SMS_GATEWAY = 0x1100;
	public static final int DEFAULT_GATEWAY = 0x1110;
	public static final int BACKUP_GATEWAY = 0x1120;
	public static final int USER_GATEWAY = 0x1140;
	public static final int USER_DEFAULT_GATEWAY = 0x1141;
	public static final int USER_BACKUP_GATEWAY = 0x1142;
	public static final int CLIENT_GATEWAY = 0x1150;
	public static final int CLIENT_DEFAULT_GATEWAY = 0x1151;
	public static final int CLIENT_BACKUP_GATEWAY = 0x1152;

	public static final int GENERAL_ORIGINATORS = 0x1200;
	public static final int GENERAL_ORIGINATOR = 0x1210;

	public static final int CLIENTS = 0x1300;
	public static final int CLIENT = 0x1310;

	public static final int PROVIDERS = 0x1400;
	public static final int PROVIDER = 0x1410;
	public static final int OPEN_PROVIDER = 0x1500;
	
	/**
	 *  STATES 0x8000 to 0x8FFF are for providers readers
	 *  - 0x8100: VodafonePop
	 *  - 0x8200: Virtual
	 *  - 0x8300: Acotel
	 *  - 0x8400: Vodafone
	 */

	public static String TAG_CONFIGURATION = "Configuration";
	
	public static String TAG_CACHE_TIMEOUT = "CacheTimeout";
	public static String TAG_JMS_TIME_ZONE = "JmsTimeZone";
	public static String TAG_SMS_GATEWAY = "SmsGateway";
	public static String TAG_DEFAULT_GATEWAY ="Default";
	public static String TAG_BACKUP_GATEWAY = "Backup";
	public static String TAG_USER_GATEWAY = "User";
	public static String ATT_USER_ID = "id";
	public static String TAG_USER_DEFAULT_GATEWAY = "Default";
	public static String TAG_USER_BACKUP_GATEWAY = "Backup";
	public static String TAG_CLIENT_GATEWAY = "Client";
	public static String ATT_CLIENT_ID = "id";
	public static String TAG_CLIENT_DEFAULT_GATEWAY = "Default";
	public static String TAG_CLIENT_BACKUP_GATEWAY = "Backup";

	public static String TAG_GENERAL_ORIGINATORS = "GeneralOriginators";
	public static String TAG_GENERAL_ORIGINATOR = "GeneralOriginator";

	public static String TAG_CLIENTS = "Clients";
	public static String TAG_CLIENT = "Client";
	public static String ATT_CLIENT_NAME = "name";
	public static String ATT_CLIENT_NORMALIZEPHONEMESSAGE = "normalizePhoneMessage";
	public static String ATT_CLIENT_VALIDATEPHONENUMBER = "validatePhoneNumber";
	public static String ATT_CLIENT_VALIDATEPHONENUMBERREGEX = "validatePhoneNumberRegEx";
	public static String ATT_CLIENT_USEBLACKLIST = "useBlackList";
	public static String ATT_CLIENT_ORIGINATOR = "originator";
	public static String ATT_CLIENT_ALIAS = "alias";
	public static String ATT_CLIENT_DESCRIPTION  = "description";
	public static String ATT_CLIENT_DEFAULT_VALIDITY  = "defaultValidity";

	public static String TAG_PROVIDERS = "Providers";
	public static String TAG_PROVIDER = "Provider";
	public static String ATT_PROVIDER_NAME = "name";
	public static String ATT_PROVIDER_TYPE = "type";
	public static String ATT_PROVIDER_DESCRIPTION = "description";
	public static String ATT_PROVIDER_LOGPREFIX = "logprefix";
	public static String ATT_PROVIDER_SMSLOGLEVEL = "smsloglevel";

	private int state = NONE;
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {

		value = new StringBuffer("");
		//log.debug("startElement: current state= " + state + ", value=" + value.toString());

		switch (state) {
		case NONE:
			if (qName.equalsIgnoreCase(TAG_CONFIGURATION)) state = CONFIGURATION;
			else throw new XASConfigurationException("Invalid Tag in Root: " + qName);
			//log.debug("Found tag " + qName + " in state NONE");
			break;
		
		case CONFIGURATION:
			if (qName.equalsIgnoreCase(TAG_CACHE_TIMEOUT)) state = CACHE_TIMEOUT;
			else if (qName.equalsIgnoreCase(TAG_JMS_TIME_ZONE)) state = JMS_TIME_ZONE;
			else if (qName.equalsIgnoreCase(TAG_SMS_GATEWAY)) state = SMS_GATEWAY;
			else if (qName.equalsIgnoreCase(TAG_GENERAL_ORIGINATORS)) state = GENERAL_ORIGINATORS;
			else if (qName.equalsIgnoreCase(TAG_CLIENTS)) state = CLIENTS;
			else if (qName.equalsIgnoreCase(TAG_PROVIDERS)) state = PROVIDERS;
			else throw new XASConfigurationException("Invalid Tag in Configuration: " + qName);
			//log.debug("Found tag " + qName + " in state CONFIGURATION");
			break;
			
		case SMS_GATEWAY:
			if (qName.equalsIgnoreCase(TAG_DEFAULT_GATEWAY)) state = DEFAULT_GATEWAY;
			else if (qName.equalsIgnoreCase(TAG_BACKUP_GATEWAY)) state = BACKUP_GATEWAY;
			else if (qName.equalsIgnoreCase(TAG_USER_GATEWAY)) {
				state = USER_GATEWAY;
				userId = attributes.getValue(ATT_USER_ID);
				if (userId == null) throw new XASConfigurationException("User id attribute missing");
			}
			else if (qName.equalsIgnoreCase(TAG_CLIENT_GATEWAY)) {
				state = CLIENT_GATEWAY;
				clientId = attributes.getValue(ATT_CLIENT_ID);
				if (clientId == null) throw new XASConfigurationException("Client id attribute missing");
			}
			else throw new XASConfigurationException("Invalid Tag in SmsGateway: " + qName);
			//log.debug("Found tag " + qName + " in state SMS_GATEWAY");
			break;
			
		case USER_GATEWAY:
			if (qName.equalsIgnoreCase(TAG_USER_DEFAULT_GATEWAY)) state = USER_DEFAULT_GATEWAY;
			else if (qName.equalsIgnoreCase(TAG_USER_BACKUP_GATEWAY)) state = USER_BACKUP_GATEWAY;
			else throw new XASConfigurationException("Invalid Tag in User Gateway: " + qName);
			//log.debug("Found tag " + qName + " in state USER_GATEWAY");
			break;
			
		case CLIENT_GATEWAY:
			if (qName.equalsIgnoreCase(TAG_CLIENT_DEFAULT_GATEWAY)) state = CLIENT_DEFAULT_GATEWAY;
			else if (qName.equalsIgnoreCase(TAG_CLIENT_BACKUP_GATEWAY)) state = CLIENT_BACKUP_GATEWAY;
			else throw new XASConfigurationException("Invalid Tag in Client Gateway: " + qName);
			//log.debug("Found tag " + qName + " in state CLIENT_GATEWAY");
			break;
			
		case GENERAL_ORIGINATORS:
			if (qName.equalsIgnoreCase(TAG_GENERAL_ORIGINATOR)) state = GENERAL_ORIGINATOR;
			else throw new XASConfigurationException("Invalid Tag in GeneralOriginators: " + qName);
			//log.debug("Found tag " + qName + " in state GENERAL_ORIGINATORS");
			break;
			
		case CLIENTS:
			if (qName.equalsIgnoreCase(TAG_CLIENT)) {
				state = CLIENT;
				Client client = new Client();
				client.setAlias(attributes.getValue(ATT_CLIENT_ALIAS));
				client.setDescription(attributes.getValue(ATT_CLIENT_DESCRIPTION));
				client.setName(attributes.getValue(ATT_CLIENT_NAME));
				client.setNormalizePhoneMessage(Boolean.valueOf(attributes.getValue(ATT_CLIENT_NORMALIZEPHONEMESSAGE)).booleanValue());
				client.setOriginator(attributes.getValue(ATT_CLIENT_ORIGINATOR));
				client.setUseBlackList(Boolean.valueOf(attributes.getValue(ATT_CLIENT_USEBLACKLIST)).booleanValue());
				client.setValidatePhoneNumber(Boolean.valueOf(attributes.getValue(ATT_CLIENT_VALIDATEPHONENUMBER)).booleanValue());
				client.setValidatePhoneNumberRegEx(attributes.getValue(ATT_CLIENT_VALIDATEPHONENUMBERREGEX));
				String defaultValidity = attributes.getValue(ATT_CLIENT_DEFAULT_VALIDITY);
				if (defaultValidity != null) {
					try {
						client.setDefaultValidity(new Integer(defaultValidity));
					} catch (NumberFormatException e) {
						throw new XASConfigurationException("Invalid value for defaultValidity: " + defaultValidity);
					}
				}
				configuration.addClient(client);
			}
			else throw new XASConfigurationException("Invalid Tag in Clients: " + qName);
			//log.debug("Found tag " + qName + " in state CLIENTS");
			break;
			
		case PROVIDERS:
			if (qName.equalsIgnoreCase(TAG_PROVIDER)) {
				state = PROVIDER;
				ProviderData pd = new ProviderData();
				pd.setName(attributes.getValue(ATT_PROVIDER_NAME));
				pd.setType(attributes.getValue(ATT_PROVIDER_TYPE));
				pd.setDescription(attributes.getValue(ATT_PROVIDER_DESCRIPTION));
				pd.setLogPrefix(attributes.getValue(ATT_PROVIDER_LOGPREFIX));
				pd.setSmsLogLevel(attributes.getValue(ATT_PROVIDER_SMSLOGLEVEL));
				//log.debug("pd.createConfigurationReader provider:" + attributes.getValue(ATT_PROVIDER_NAME));
				pd.createConfigurationReader(configuration);
				configuration.addProviderData(pd);
				currentProvider = pd;
			}
			else throw new XASConfigurationException("Invalid Tag in Providers: " + qName);
			//log.debug("Found tag " + qName + " in state PROVIDERS");
			break;
			
		case CACHE_TIMEOUT:
		case JMS_TIME_ZONE:
		case DEFAULT_GATEWAY:
		case BACKUP_GATEWAY:
		case USER_DEFAULT_GATEWAY:
		case USER_BACKUP_GATEWAY:
		case CLIENT_DEFAULT_GATEWAY:
		case CLIENT_BACKUP_GATEWAY:
		case GENERAL_ORIGINATOR:
		case CLIENT:
			throw new XASConfigurationException("Invalid Tag; " + qName + " in state " + state);
			
		case PROVIDER:
		case OPEN_PROVIDER:
		    currentProvider.getConfigurationReader().startElement(uri, localName, qName, attributes);
		    state = OPEN_PROVIDER;
		    break;
		
		default: throw new XASConfigurationException("Unrecognized state: " + state + " opening tag " + qName);

		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		//log.debug("endElement: current state= " + state + ", value=" + value.toString());

		switch(state) {
		case CONFIGURATION:
			state = NONE; 
			break; 
		
		case CACHE_TIMEOUT: 
			configuration.setCacheTimeout(value.toString()); 
			state = CONFIGURATION; 
			break;
		case JMS_TIME_ZONE:
			configuration.setJmsTimeZone(value.toString()); 
			state = CONFIGURATION; 
			break;
		case SMS_GATEWAY:
		case GENERAL_ORIGINATORS:
		case CLIENTS:
		case PROVIDERS:
			state = CONFIGURATION; 
			break;
		
		case DEFAULT_GATEWAY: 
			configuration.setDefaultProvider(value.toString()); 
			state = SMS_GATEWAY; 
			break;
		case BACKUP_GATEWAY:
			configuration.addBackupProvider(value.toString()); 
			state = SMS_GATEWAY; 
			break;
		case USER_GATEWAY:
		case CLIENT_GATEWAY:
			state = SMS_GATEWAY; 
			break;
			
		case USER_DEFAULT_GATEWAY: 
			configuration.addUserProvider(userId, value.toString());
			state = USER_GATEWAY; 
			break;
		case USER_BACKUP_GATEWAY:
			configuration.addUserProvider(userId, value.toString());
			state = USER_GATEWAY; 
			break;
			
		case CLIENT_DEFAULT_GATEWAY:
			configuration.addClientProvider(clientId, value.toString());
			state = CLIENT_GATEWAY; 
			break;
		case CLIENT_BACKUP_GATEWAY:
			configuration.addClientProvider(clientId, value.toString());
			state = CLIENT_GATEWAY; 
			break;
			
		case GENERAL_ORIGINATOR:
			configuration.addOriginator(value.toString());
			state = GENERAL_ORIGINATORS; 
			break;
			
		case CLIENT:
			state = CLIENTS; 
			break;
			
		case PROVIDER:
			state = PROVIDERS; 
			break;
	
		case OPEN_PROVIDER: 
			currentProvider.getConfigurationReader().characters(new String(value).toCharArray(), 0, value.length());
			currentProvider.getConfigurationReader().endElement(uri, localName, qName);
			if (currentProvider.getConfigurationReader().isEnded()) {
				try {
					currentProvider.createProvider();
				} catch(XASException e) {
					e.printStackTrace();
					throw new XASConfigurationException("Unable to create Provider: " + currentProvider.getName() + " - see Stack Trace");
				}
				state = PROVIDERS;
			}
			break;
		
		default: 
			throw new XASConfigurationException("Unrecognized state: " + state + " closing tag " + qName);

		}

	}

	public void characters(char ch[], int start, int length)
	throws SAXException {
		value.append(ch, start, length);
	};

	public void read(URL fileURL, Configuration configuration) throws XASConfigurationException {
		try {
			this.configuration = configuration;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(fileURL.getFile()), this);
		} catch(Exception e) {
			throw new XASConfigurationException("Error reading configuration file", e);
		}
	}

}
