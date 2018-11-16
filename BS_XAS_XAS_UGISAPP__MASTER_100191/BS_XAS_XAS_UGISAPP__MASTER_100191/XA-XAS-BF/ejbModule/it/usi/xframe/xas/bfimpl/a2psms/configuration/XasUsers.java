package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;

public class XasUsers extends HashMap {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(XasUsers.class);

	private String defaultXasUserName = null;
	private String defaultProvider = null;
	private String defaultProviderBackup = null;
	private String defaultRegion = null;
	private Integer defaultDRtimeOut = null;
	private Integer defaultMOtimeOut = null;
	
	public synchronized Integer getDefaultDRtimeOut() {
    	return defaultDRtimeOut;
    }

	public synchronized void setDefaultDRtimeOut(Integer defaultDRtimeOut) {
    	this.defaultDRtimeOut = defaultDRtimeOut;
    }

	public synchronized Integer getDefaultMOtimeOut() {
    	return defaultMOtimeOut;
    }

	public synchronized void setDefaultMOtimeOut(Integer defaultMOtimeOut) {
    	this.defaultMOtimeOut = defaultMOtimeOut;
    }

	public synchronized String getDefaultXasUserName() {
    	return defaultXasUserName;
    }

	/**
	 * USED ONLY BY SendSms V1.
	 * @param defaultXasUserName
	 */
	public synchronized void setDefaultXasUserName(String defaultXasUserName) {
    	this.defaultXasUserName = defaultXasUserName;
    }

	public synchronized String getDefaultProvider() {
    	return defaultProvider;
    }

	public synchronized void setDefaultProvider(String defaultProvider) {
    	this.defaultProvider = defaultProvider;
    }

	public synchronized String getDefaultProviderBackup() {
    	return defaultProviderBackup;
    }

	public synchronized void setDefaultProviderBackup(String defaultProviderBackup) {
    	this.defaultProviderBackup = defaultProviderBackup;
    }

	public synchronized String getDefaultRegion() {
    	return defaultRegion;
    }

	public synchronized void setDefaultRegion(
            String defaultRegion) {
    	this.defaultRegion = defaultRegion;
    }

	/** 
	 * Search a useful xasUser in the configuration.
	 * 
	 * @param xasUserName
	 * @param defaultXasUserName
	 * @return the xasUser object found
	 * @throws XASException
	 */
	public synchronized XasUser getFinalXasUser(String myUUID, String xasUserName, String defaultXasUserName) throws XASException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasUsers.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "getFinalXasUser").preset("default");

		logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, "a_xasUserName", xasUserName, "a_defaultXasUserName", defaultXasUserName).getLogRow(true));

		// Check XasUserMedium 
		XasUser xasUser = (XasUser)get(xasUserName);
		sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT).preset("default");
		logger.debug(sl.reload("default").logIt(SmartLog.K_STEP, "xasUserMedium", "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)).getLogRow(true));
		if (xasUser == null) { // Check XasUserSmall 
			xasUser = (XasUser)get(xasUserName.substring(0, xasUserName.length() > 8 ? 8 : xasUserName.length()));
			logger.debug(sl.logIt(SmartLog.K_STEP, "xasUserSmall", "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)).getLogRow(true));
		}
		if (xasUser == null && defaultXasUserName != null) { // Check defaultXasUserName
			xasUser = (XasUser)get(defaultXasUserName);
			logger.debug(sl.logIt(SmartLog.K_STEP, "defaultXasUserName", "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)).getLogRow(true));
		}

		if (xasUser == null) 
			throw new XASException(ConstantsSms.XAS02012E_MESSAGE, null, ConstantsSms.XAS02012E_CODE, new Object[] {xasUserName, defaultXasUserName});

		try {
	        xasUser = (XasUser) xasUser.clone();
        } catch (CloneNotSupportedException e) {
			e.printStackTrace(System.out);
			SmartLog slException = new SmartLog(SmartLog.COUPLING_LOOSE_I)
        		.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasUsers.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
        		.logIt(SmartLog.K_METHOD, "getFinalXasUser"); 
			slException.logIt(SmartLog.K_SCOPE, SmartLog.V_SCOPE_DEBUG);
			slException.logIt(SmartLog.K_STATUS_MSG, e.getMessage());
			logger.error(slException.getLogRow());
        }
		logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, XConstants.XSTREAMER.toXML(xasUser)).getLogRow());
        return xasUser;
	}
	/**
	 * Get final XasUser by moDestinator.
	 * @param moDestinator
	 * @return
	 */
	public XasUser getFinalXasUser(String moDestinator) { //#TIM
		return (XasUser)get(Configuration.XASUSER_MO_PREFIX + moDestinator);
    }

	/**
	 * Set default value if needed and store the XasUser.
	 * @return the HashMap it self.
	 */
	public synchronized Object put(Object key, Object value) {
	    // TODO Auto-generated method stub
		if (((XasUser) value).getProvider() == null) {
			((XasUser) value).setProvider(defaultProvider);
		}
		if (((XasUser) value).getDefaultRegion() == null) {
			((XasUser) value).setDefaultRegion(defaultRegion);
		}
		if (((XasUser) value).getUseBackup()) {
			((XasUser) value).setProviderBackup(defaultProviderBackup);
		}
	    return super.put(key, value);
    }


}
