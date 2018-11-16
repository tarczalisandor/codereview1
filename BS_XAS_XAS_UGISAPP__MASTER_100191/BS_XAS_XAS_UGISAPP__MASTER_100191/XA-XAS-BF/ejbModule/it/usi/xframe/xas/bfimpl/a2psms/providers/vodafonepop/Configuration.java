package it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;

public class Configuration {
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	public  static final String TYPE = "vodafonepop";
	private static final String TAG_POP_ACCOUNT 	= "POP_Account";
	private static final String ATT_POP_USER		= "user";
	private static final String ATT_POP_PASSWORD	= "password";
	
	public static final String JNDI_POP_SOAP_URL = "java:comp/env/url/VodafonePopSoapUrl";

	/**
	 * Load the Sms configuration file.
	 * 
	 * @throws XASConfigurationException
	 */
	public static Object load(XMLConfiguration config, String customizationPath) throws XASException {
		final String K_PASSWORD = "a_password", K_USER = "a_user";
		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD,"load");
//			.anonymize("vodafonepop", new String[] {K_PASSWORD, K_USER}) ;
			

		String path, value;
		Customization customization = new Customization();
		
		sl.logIt("a_step", customizationPath + "." + TAG_POP_ACCOUNT);
		
		path = customizationPath + "." + TAG_POP_ACCOUNT + "[@" + ATT_POP_USER + "]";  
		value = config.getString(path); 
		if (value != null) customization.setUser(value);
		else throw new XASException(ConstantsSms.XAS02027E_MESSAGE, null, ConstantsSms.XAS02027E_CODE, new Object[] {path});

        sl.logIt(K_USER, value);
		
		path = customizationPath + "." + TAG_POP_ACCOUNT + "[@" + ATT_POP_PASSWORD + "]";  
		value = config.getString(path); 
		if (value != null) customization.setPassword(value);
		else throw new XASException(ConstantsSms.XAS02027E_MESSAGE, null, ConstantsSms.XAS02027E_CODE, new Object[] {path});
        sl.logIt(K_PASSWORD, value);
        
        logger.debug(sl.getLogRow(true));
		
		return customization;
	}

}