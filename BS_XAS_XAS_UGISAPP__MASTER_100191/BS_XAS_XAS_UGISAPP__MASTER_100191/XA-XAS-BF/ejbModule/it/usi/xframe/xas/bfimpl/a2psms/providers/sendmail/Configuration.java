package it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;

public class Configuration {
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);

	public  static final String TYPE 		= "sendmail";
	private static final String TAG_MAILMAP = "MailMap";
	private static final String ATT_USER	= "xasUser";
	private static final String ATT_ADDRESS	= "mailAddress";
	

	/**
	 * Load the Sms configuration file.
	 * 
	 * @throws XASException
	 */
	public static Object load(XMLConfiguration config, String customizationPath) throws XASException {
		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD,"load");

		logger.debug(sl.getLogRow(true));

		String path, value;
		Customization customization = new Customization();
		
		path = customizationPath + "." + TAG_MAILMAP + "[@" + ATT_USER + "]";
		
		String[] mailMapArray = config.getStringArray(path);
		if (mailMapArray.length == 0) 
			throw new XASException(ConstantsSms.XAS02027E_MESSAGE, null, ConstantsSms.XAS02027E_CODE, new Object[] {path});
		for (int oi = 0; oi < mailMapArray.length; oi++) {
			path = customizationPath + "." + TAG_MAILMAP + "(" + oi + ")[@" + ATT_USER + "]";  
			String user = config.getString(path);
			if (user == null) throw new XASException(ConstantsSms.XAS02027E_MESSAGE, null, ConstantsSms.XAS02027E_CODE, new Object[] {path});

			
			path = customizationPath + "." + TAG_MAILMAP + "(" + oi + ")[@" + ATT_ADDRESS + "]";  
			value = config.getString(path); 
			if (value == null) throw new XASException(ConstantsSms.XAS02027E_MESSAGE, null, ConstantsSms.XAS02027E_CODE, new Object[] {path});
			
			if (user != null && value != null) customization.add(user, value);
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_LOOP_ITERATE, "a_step", path, "a_user", user, "a_address", value).getLogRow(true));
		}		
		return customization;
	}

}