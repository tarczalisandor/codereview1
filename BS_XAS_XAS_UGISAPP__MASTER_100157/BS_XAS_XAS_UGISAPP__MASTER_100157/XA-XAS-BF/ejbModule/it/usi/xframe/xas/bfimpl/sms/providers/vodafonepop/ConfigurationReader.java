package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.configuration.AbstractProviderConfigurationReader;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.MamAccountRouter;
import it.usi.xframe.xas.bfimpl.sms.configuration.ProviderData;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReader extends AbstractProviderConfigurationReader {
	
	private static Log log = LogFactory.getLog(ConfigurationReader.class);

	private StringBuffer value = new StringBuffer("");
	private HashMap mAccounts = new HashMap();
	private MamAccount mMamAccount = null;
	private MamAccountRouter mMamAccountRouter = new MamAccountRouter();
	private PopAccount mPopAccount = null; 
	private URL popSoapUrl = null;		//USELESS as of 9/12/15; JNDI resource used instead
	
	/**
	 *  STATES 0x8000 to 0x8FFF are for providers readers
	 */

	public static final int NONE                                  = 0x8100;

	public static final int VODAFONE_ORIGINATORS                  = 0x8110;
	public static final int VODAFONE_ORIGINATOR		              = 0x8111;
	public static final int VODAFONE_ORIGINATOR_ID                = 0x8112;
	public static final int VODAFONE_ORIGINATOR_ADDRESS           = 0x8113;
	public static final int VODAFONE_ORIGINATOR_ALIAS             = 0x8114;
	public static final int VODAFONE_ORIGINATOR_TYPEOFNUMBER      = 0x8115; // #380
	public static final int VODAFONE_ORIGINATOR_NUMPLANID	      = 0x8116; // #380
	
	public static final int VODAFONE_POPSOAPURL                   = 0X8117;	//useless

	
	public static final int VODAFONE_POPACCOUNTS                  = 0x8120;
	public static final int VODAFONE_POPACCOUNTS_ACCOUNT          = 0x8121;
	public static final int VODAFONE_POPACCOUNTS_ACCOUNT_USER     = 0x8122;
	public static final int VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD = 0x8123;

	private static String TAG_VODAFONE_ORIGINATORS                             = "Originators";
	private static String TAG_VODAFONE_ORIGINATOR                              = "Originator";
	private static String ATT_VODAFONE_ORIGINATOR_ROUTER		 			   = "routerRegex";	// #380
	private static String TAG_VODAFONE_ORIGINATOR_ID                           = "Id";
	private static String TAG_VODAFONE_ORIGINATOR_ADDRESS                      = "Address";
	private static String TAG_VODAFONE_ORIGINATOR_ALIAS                        = "Alias";
	private static String TAG_VODAFONE_ORIGINATOR_TYPEOFNUMBER                 = "TypeOfNumber"; // #380
	private static String TAG_VODAFONE_ORIGINATOR_NUMPLANID		               = "NumPlanId"; // #380
	
	private static String TAG_VODAFONE_POPSOAPURL                              = "TargetURL";	//useless

	private static String TAG_VODAFONE_POPACCOUNTS                             = "POP_Accounts";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT                     = "Account";
	private static String ATT_VODAFONE_POPACCOUNTS_ACCOUNT_PROTOCOL            = "protocol";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT_USER                = "User";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD            = "Password";

	private int state = NONE;
	
	public ConfigurationReader(Configuration mainConfig) {
		super(mainConfig);
		//log.debug("Configuration reader vodafonepop");
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {

		value = new StringBuffer("");
		//log.debug("startElement: current state= " + state + ", value=" + value.toString());

		switch (state) {
		
		case NONE:
			if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATORS)) state = VODAFONE_ORIGINATORS;
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_POPSOAPURL)) state = VODAFONE_POPSOAPURL;	//useless
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS)) state = VODAFONE_POPACCOUNTS;
			else throw new XASConfigurationException("Invalid Tag in VodafonePop Provider: " + qName);
			//log.debug("VodafonePop: Found tag " + qName + " in state NONE");
			break;
		
		case VODAFONE_ORIGINATORS:
			if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR)) {
				state = VODAFONE_ORIGINATOR;
				mMamAccount = new MamAccount();
				
				String routerRegex = attributes.getValue(ATT_VODAFONE_ORIGINATOR_ROUTER); // #380
				if (routerRegex != null) { mMamAccount.setRouterRegex(routerRegex); }	  // #380
			}
			else throw new XASConfigurationException("Invalid Tag in VodafonePop Originators: " + qName);
			//log.debug("VodafonePop: Found tag " + qName + " in state VODAFONE_ORIGINATORS");
			break;
			
		case VODAFONE_ORIGINATOR:
			if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR_ID)) state = VODAFONE_ORIGINATOR_ID;
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR_ADDRESS)) state = VODAFONE_ORIGINATOR_ADDRESS;
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR_ALIAS)) state = VODAFONE_ORIGINATOR_ALIAS;
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR_TYPEOFNUMBER)) state = VODAFONE_ORIGINATOR_TYPEOFNUMBER;	 // #380
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_ORIGINATOR_NUMPLANID)) state = VODAFONE_ORIGINATOR_NUMPLANID;	 // #380
			else throw new XASConfigurationException("Invalid Tag in VodafonePop Originator: " + qName);
			//log.debug("VodafonePop: Found tag " + qName + " in state VODAFONE_ORIGINATOR");
			break;
			
		case VODAFONE_POPACCOUNTS:
			if (qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT)) {
				state = VODAFONE_POPACCOUNTS_ACCOUNT;
				mPopAccount = new PopAccount();
				mPopAccount.setProtocol(attributes.getValue(ATT_VODAFONE_POPACCOUNTS_ACCOUNT_PROTOCOL));
			}
			else throw new XASConfigurationException("Invalid Tag in VodafonePop PopAccounts: " + qName);
			//log.debug("VodafonePop: Found tag " + qName + " in state VODAFONE_POPACCOUNTS");
			break;
			
		case VODAFONE_POPACCOUNTS_ACCOUNT:
			if (qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_USER)) state = VODAFONE_POPACCOUNTS_ACCOUNT_USER;
			else if (qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD)) state = VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD;
			else throw new XASConfigurationException("Invalid Tag in VodafonePop Account: " + qName);
			//log.debug("VodafonePop: Found tag " + qName + " in state VODAFONE_POPACCOUNTS_ACCOUNT");
			break;
			
		case VODAFONE_ORIGINATOR_ID:
		case VODAFONE_ORIGINATOR_ADDRESS:
		case VODAFONE_ORIGINATOR_ALIAS:
		case VODAFONE_ORIGINATOR_TYPEOFNUMBER:	 // #380
		case VODAFONE_ORIGINATOR_NUMPLANID:	 // #380
		case VODAFONE_POPSOAPURL:	//useless
		case VODAFONE_POPACCOUNTS_ACCOUNT_USER:
		case VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD:
			throw new XASConfigurationException("Invalid tag " + qName + " in state " + state);

		default: throw new XASConfigurationException("Unrecognized state in VodafonePop Configuration Reader: " + state + " opening tag " + qName);
		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		//log.debug("endElement: current state= " + state + ", value=" + value.toString());
		switch(state) {
		
		case NONE:
			ended = true;
			break;
		case VODAFONE_POPSOAPURL: 
			try {		//useless
				popSoapUrl = new URL(value.toString());
			} catch(MalformedURLException e) {
				throw new XASConfigurationException("Malformed URL Exception: " + value);
			}; 
			state = NONE;  
			break;
		case VODAFONE_ORIGINATORS:
		case VODAFONE_POPACCOUNTS:
			state = NONE; 
			break; 
		case VODAFONE_ORIGINATOR:
			if (mMamAccount.getRouterRegex() == null) { // Register MamAccount 		// #380 
				//log.debug("Registering MamAccount " + mMamAccount.getSystemId() + " (" + mMamAccount.getAddress() + ", " + mMamAccount.getAlias() + ", " + mMamAccount.getTypeOfNumber() + ", " + mMamAccount.getNumPlanId() + ")");
				mAccounts.put(mMamAccount.getSystemId(), mMamAccount);
			} else { // Register MamAccount router									// #380
				//log.debug("Registering MamAccount Router " + mMamAccount.getSystemId() + " for " + mMamAccount.getRouterRegex() +  " (" + mMamAccount.getAddress() + ", " + mMamAccount.getAlias() + ", " + mMamAccount.getTypeOfNumber() + ", " + mMamAccount.getNumPlanId() + ")");
				mMamAccountRouter.put(mMamAccount);	// #380
			}
			mMamAccount = null;
			state = VODAFONE_ORIGINATORS; 
			break;
		case VODAFONE_ORIGINATOR_ID:
			mMamAccount.setSystemId(value.toString());  //JJ 
			state = VODAFONE_ORIGINATOR; 
			break;
		case VODAFONE_ORIGINATOR_ADDRESS:
			mMamAccount.setAddress(value.toString()); 
			state = VODAFONE_ORIGINATOR; 
			break;
		case VODAFONE_ORIGINATOR_ALIAS: 
			mMamAccount.setAlias(value.toString()); 
			state = VODAFONE_ORIGINATOR; 
			break;
		case VODAFONE_ORIGINATOR_TYPEOFNUMBER:	 // #380
			mMamAccount.setTypeOfNumber(Integer.parseInt(value.toString())); // #380 
			state = VODAFONE_ORIGINATOR; 
			break;
		case VODAFONE_ORIGINATOR_NUMPLANID:	 // #380
			mMamAccount.setNumPlanId(Integer.parseInt(value.toString())); // #380 
			state = VODAFONE_ORIGINATOR; 
			break;
		case VODAFONE_POPACCOUNTS_ACCOUNT:
			state = VODAFONE_POPACCOUNTS; 
			break;
		case VODAFONE_POPACCOUNTS_ACCOUNT_USER: 
			mPopAccount.setUser(value.toString()); 
			state = VODAFONE_POPACCOUNTS_ACCOUNT; 
			break;
		case VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD: 
			mPopAccount.setPassword(value.toString()); 
			state = VODAFONE_POPACCOUNTS_ACCOUNT; 
			break;
			
		default: throw new XASConfigurationException("Unrecognized state in VodafonePop Configuration Reader: " + state + " closing tag " + qName);
		}
		value = new StringBuffer("");
		//log.debug("new state = " + state);

	}

	public void characters(char ch[], int start, int length) throws SAXException {
		value.append(ch, start, length);
	};
	
	public HashMap getOriginators() {
		return mAccounts;
	}

	public MamAccountRouter getOriginatorRouter() {
		return mMamAccountRouter;
	}
	
	public PopAccount getPopAccount() {
		return mPopAccount;
	}
 
	public GatewaySms createProvider(ProviderData providerData) throws XASException {
		GatewaySms provider =  new GatewaySmsVodafonePop(providerData.getLogPrefix(), providerData.getSmsLogLevel(), this);
		return provider;
	}
	
	/**
	 * @deprecated use JNDI resource instead
	 * @return URL
	 */
	public URL getPopSoapUrl() {	
		return popSoapUrl;
	}


}
