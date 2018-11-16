package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

import it.usi.xframe.xas.bfimpl.sms.configuration.MamAccountRouter; // #380
import it.usi.xframe.xas.bfutil.XASException;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadConfigFile {
	private static Log log = LogFactory.getLog(ReadConfigFile.class);
	
	private HashMap mAccounts = new HashMap();
	private MamAccountRouter mMamAccountRouter = new MamAccountRouter(); // #380
	private MamAccount mMamAccount = null;
	
	private HashMap mClients = new HashMap();
	private Client mClient = null;
	private PopAccount mPopAccount = null; 
	private String mDefaultClientName = null; 
		
	private static int NONE                                   = 0;

	private static int VODAFONE                               = 1;
	 
	private static int VODAFONE_ACCOUNTS                      = 2;
	private static int VODAFONE_ACCOUNTS_ACCOUNT              = 3;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID     = 4;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD     = 5;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS      = 6;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_ALIAS        = 7;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER = 8;
	private static int VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID	  = 9;
	
	private static int VODAFONE_POPACCOUNTS                   = 10;
	private static int VODAFONE_POPACCOUNTS_ACCOUNT          = 11;
	private static int VODAFONE_POPACCOUNTS_ACCOUNT_USER     = 12;
	private static int VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD = 13;

	private static int VODAFONE_CLIENTS                      = 14;
	private static int VODAFONE_CLIENTS_CLIENT               = 15;
	private static int VODAFONE_CLIENTS_CLIENT_ACCOUNT       = 16;


	private static String TAG_VODAFONE                                         = "Vodafone";

	private static String TAG_VODAFONE_ACCOUNTS                                = "Accounts";
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT                        = "Account";
	private static String ATT_VODAFONE_ORIGINATOR_ROUTER		 			   = "routerRegex";	// #380
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID               = "SystemId";
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD               = "Password";
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS                = "Address";
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_ALIAS                  = "Alias";
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER           = "TypeOfNumber"; // #380
	private static String TAG_VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID	           = "NumPlanId"; // #380

	private static String TAG_VODAFONE_POPACCOUNTS                             = "POP_Accounts";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT                     = "Account";
	private static String ATT_VODAFONE_POPACCOUNTS_ACCOUNT_PROTOCOL            = "protocol";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT_USER                = "User";
	private static String TAG_VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD            = "Password";

	private static String TAG_VODAFONE_CLIENTS                                 = "Clients";
	private static String ATT_VODAFONE_CLIENTS_DEFAULTNAME                     = "default_name";
	private static String TAG_VODAFONE_CLIENTS_CLIENT                          = "Client";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_NAME                     = "name";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_DESCRIPTION              = "description";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_ALIAS                    = "alias";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_USEBLACKLIST			   = "useBlackList";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_NORMALIZEPHONEMESSAGE    = "normalizePhoneMessage";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_VALIDATEPHONENUMBER      = "validatePhoneNumber";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_VALIDATEPHONENUMBERREGEX = "validatePhoneNumberRegEx";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_DEFAULTVALIDITYPERIOD	   = "defaultValidityPeriod";  // #369
	private static String ATT_VODAFONE_CLIENTS_CLIENT_PREFIX				   = "prefix";  	// #370
	private static String ATT_VODAFONE_CLIENTS_CLIENT_PREFIXRE				   = "prefixRE";  	// #370
	private static String TAG_VODAFONE_CLIENTS_CLIENT_ACCOUNT                  = "Account";
	private static String ATT_VODAFONE_CLIENTS_CLIENT_ACCOUNT_SYSTEMID         = "SystemId";

	public void read(URL fileURL) throws Exception
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler()
			{
				int state = 0;

				public void startElement(String uri,
						String localName,
						String qName,
						Attributes attributes)
						throws SAXException {

					//System.out.println("Start Element :" + qName);

					// ROOT
					if (state==NONE && qName.equalsIgnoreCase(TAG_VODAFONE))
						state = VODAFONE;
					
					// MAM ACCOUNTS
					if (state==VODAFONE && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS))
						state = VODAFONE_ACCOUNTS;
					
					if (state==VODAFONE_ACCOUNTS && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT)){
						state = VODAFONE_ACCOUNTS_ACCOUNT;
						mMamAccount = new MamAccount();
						String routerRegex = attributes.getValue(ATT_VODAFONE_ORIGINATOR_ROUTER); // #380
						if (routerRegex != null) { mMamAccount.setRouterRegex(routerRegex); }	  // #380
						
					}
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID))
						state = VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD))
						state = VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS))
						state = VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_ALIAS))
						state = VODAFONE_ACCOUNTS_ACCOUNT_ALIAS;

					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER))
						state = VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER;

					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID))
						state = VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID;
					
					// POP ACCOUNT
					if (state==VODAFONE && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS))
						state = VODAFONE_POPACCOUNTS;

					if (state==VODAFONE_POPACCOUNTS && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT)){
						state = VODAFONE_POPACCOUNTS_ACCOUNT;
						mPopAccount = new PopAccount();
						String protocol = attributes.getValue(ATT_VODAFONE_POPACCOUNTS_ACCOUNT_PROTOCOL);
						mPopAccount.setProtocol(protocol);
					}

					if (state==VODAFONE_POPACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_USER))
						state = VODAFONE_POPACCOUNTS_ACCOUNT_USER;
					
					if (state==VODAFONE_POPACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD))
						state = VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD;

					// CLIENTS
					if (state==VODAFONE && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS)) {
						state = VODAFONE_CLIENTS;
						String defaultId = attributes.getValue(ATT_VODAFONE_CLIENTS_DEFAULTNAME);
						mDefaultClientName = defaultId; 
					}
					
					if (state==VODAFONE_CLIENTS && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS_CLIENT)){
						state = VODAFONE_CLIENTS_CLIENT;
						mClient = new Client();
						String name = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_NAME);
						String description = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_DESCRIPTION);
						String alias = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_ALIAS);
 						String normalizePhoneMessage = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_NORMALIZEPHONEMESSAGE);
						String useBlackList = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_USEBLACKLIST);
						String validatePhoneNumber = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_VALIDATEPHONENUMBER);
						String validatePhoneNumberRegEx = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_VALIDATEPHONENUMBERREGEX);
						String validityPeriod = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_DEFAULTVALIDITYPERIOD); // #369
						String prefix = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_PREFIX); 	 // #370
						String prefixRE = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_PREFIXRE); // #370
						mClient.setName(name);
						mClient.setDescription(description);
						mClient.setAlias(alias);
						mClient.setNormalizePhoneMessage(Boolean.valueOf(normalizePhoneMessage).booleanValue());
						mClient.setUseBlackList(Boolean.valueOf(useBlackList).booleanValue());
						mClient.setValidatePhoneNumber(Boolean.valueOf(validatePhoneNumber).booleanValue());
						mClient.setValidatePhoneNumberRegEx(validatePhoneNumberRegEx);

						mClient.setPrefix(prefix); 		// #370
						mClient.setPrefixRE(prefixRE);	// #370
						if (!"".equals(validityPeriod) && validityPeriod != null) // #369
							try { 
								int validityMinutes = Integer.parseInt(validityPeriod);
								mClient.setValidityPeriod(new Integer(validityMinutes));
							} catch(NumberFormatException e) {
								throw new SAXException("Validity period " + validityPeriod + " is not valid");
							} // #369
						
					}

					if (state==VODAFONE_CLIENTS_CLIENT && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS_CLIENT_ACCOUNT)) {
						state = VODAFONE_CLIENTS_CLIENT_ACCOUNT;
						String systemId = attributes.getValue(ATT_VODAFONE_CLIENTS_CLIENT_ACCOUNT_SYSTEMID);
						mClient.addAccount(systemId);
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException
				{
					//System.out.println("End Element :" + qName);

					if (state==VODAFONE && qName.equalsIgnoreCase(TAG_VODAFONE))
						state = NONE;

					// MAM ACCOUNTS
					if (state==VODAFONE_ACCOUNTS && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS))
						state = VODAFONE;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT)) {
						if (mMamAccount.getRouterRegex() == null) { // Register MamAccount 		// #380 
							// log.debug("Registering MamAccount " + mMamAccount.getSystemId() + " (" + mMamAccount.getAddress() + ", " + mMamAccount.getAlias() + ", " + mMamAccount.getTypeOfNumber() + ", " + mMamAccount.getNumPlanId() + ")");
							mAccounts.put(mMamAccount.getSystemId(), mMamAccount);
						} else { // Register MamAccount router									// #380
							// log.debug("Registering MamAccount Router " + mMamAccount.getSystemId() + " for " + mMamAccount.getRouterRegex() +  " (" + mMamAccount.getAddress() + ", " + mMamAccount.getAlias() + ", " + mMamAccount.getTypeOfNumber() + ", " + mMamAccount.getNumPlanId() + ")");
							mMamAccountRouter.put(mMamAccount);	// #380
						}

						mMamAccount = null;
						state = VODAFONE_ACCOUNTS;
					}
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID)) {
						state = VODAFONE_ACCOUNTS_ACCOUNT;
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD))
						state = VODAFONE_ACCOUNTS_ACCOUNT;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS))
						state = VODAFONE_ACCOUNTS_ACCOUNT;
					
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_ALIAS && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_ALIAS))
						state = VODAFONE_ACCOUNTS_ACCOUNT;

					if (state==VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER))
						state = VODAFONE_ACCOUNTS_ACCOUNT;

					if (state==VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID && qName.equalsIgnoreCase(TAG_VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID))
						state = VODAFONE_ACCOUNTS_ACCOUNT;
										
					// POP ACCOUNT
					if (state==VODAFONE_POPACCOUNTS && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS))
						state = VODAFONE;

					if (state==VODAFONE_POPACCOUNTS_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT)) {
						state = VODAFONE_POPACCOUNTS;
					}

					if (state==VODAFONE_POPACCOUNTS_ACCOUNT_USER && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_USER))
						state = VODAFONE_POPACCOUNTS_ACCOUNT;

					if (state==VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD && qName.equalsIgnoreCase(TAG_VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD))
						state = VODAFONE_POPACCOUNTS_ACCOUNT;

					// CLIENTS
					if (state==VODAFONE_CLIENTS && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS))
						state = VODAFONE;

					if (state==VODAFONE_CLIENTS_CLIENT && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS_CLIENT)) {
						mClients.put(mClient.getName(), mClient);
						mClient = null;
						state = VODAFONE_CLIENTS;
					}

					if (state==VODAFONE_CLIENTS_CLIENT_ACCOUNT && qName.equalsIgnoreCase(TAG_VODAFONE_CLIENTS_CLIENT_ACCOUNT))
						state = VODAFONE_CLIENTS_CLIENT;
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					
					String val = new String(ch, start, length);
					//System.out.println("characters : " + val);

					// POP ACCOUNT
					if (state==VODAFONE_POPACCOUNTS_ACCOUNT_USER) {
						mPopAccount.setUser(val);
					}
					if (state==VODAFONE_POPACCOUNTS_ACCOUNT_PASSWORD) {
						mPopAccount.setPassword(val);
					}

					// MAM ACCOUNT
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_SYSTEMID) {
						mMamAccount.setSystemId(val);
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_PASSWORD) {
						mMamAccount.setPassword(val);
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_ADDRESS) {
						mMamAccount.setAddress(val);
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_ALIAS) {
						mMamAccount.setAlias(val);
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_TYPEOFNUMBER) {
						mMamAccount.setTypeOfNumber(Integer.parseInt(val));
					}
					if (state==VODAFONE_ACCOUNTS_ACCOUNT_NUMPLANID) {
						mMamAccount.setNumPlanId(Integer.parseInt(val));
					}
										
					// CLIENT
				}

			};

			saxParser.parse(new File(fileURL.getFile()), handler);

		} catch (Exception e) {
			throw e;
		}

	}

	public HashMap getAccounts()
	{
		return mAccounts;
	}
	public MamAccountRouter getAccountRouter() { // #380
		return mMamAccountRouter;	
	}

	public HashMap getClients()
	{
		//return (Client[]) clients.toArray(new Client[clients.size()]);
		return mClients;
	}

	public PopAccount getPopAccount()
	{
		return mPopAccount;
	}

	public String getDefaultClientName()
	{
		return mDefaultClientName;
	}
}
