package it.usi.xframe.xas.bfimpl.a2psms.providers.virtual;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;

public class Configuration {
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);

	public  static final String TYPE = "virtual";
	private static final String TAG_POP_ACCOUNT 	= "POP_Account";
	private static final String ATT_POP_USER		= "user";
	private static final String ATT_POP_PASSWORD	= "password";
	

	/**
	 * Load the Sms configuration file.
	 * 
	 * @throws XASException
	 */
	public static Object load(XMLConfiguration config, String customizationPath) throws XASException {
		String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD,"load");
			
		String path, value;
		Customization customization = new Customization();
		
		path = customizationPath + "." + TAG_POP_ACCOUNT + "[@" + ATT_POP_USER + "]";  
		value = config.getString(path); if (value != null) customization.setUser(value);
        logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
		
		path = customizationPath + "." + TAG_POP_ACCOUNT + "[@" + ATT_POP_PASSWORD + "]";  
		value = config.getString(path); if (value != null) customization.setPassword(value);
        logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
		
		return customization;
	}

}