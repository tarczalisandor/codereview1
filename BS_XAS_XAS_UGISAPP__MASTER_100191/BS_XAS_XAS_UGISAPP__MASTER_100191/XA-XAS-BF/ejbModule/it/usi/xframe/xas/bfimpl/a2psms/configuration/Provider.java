package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import it.usi.xframe.xas.bfimpl.a2psms.IGatewayA2Psms;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class Provider implements Cloneable {
	private static Logger logger = LoggerFactory.getLogger(Provider.class);
	private String name					= null;
	private String type					= null;
	private Integer maxSmsAllowed		= new Integer(2);
	private Integer timeOut				= null;
	private String provider				= null;
	private HashMap originators			= null;
	private HashMap originatorRouters	= null;
	private Object customization		= null;
	private IGatewayA2Psms gatewaySms	= null;

	protected Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}  
	public Integer getTimeOut() {
    	return timeOut;
    }
	public void setTimeOut(Integer timeOut) {
    	this.timeOut = timeOut;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public String getType() {
    	return type;
    }
	public void setType(String type) {
    	this.type = type;
    }
	public Integer getMaxSmsAllowed() {
    	return maxSmsAllowed;
    }
	public void setMaxSmsAllowed(Integer maxSmsAllowed) {
    	this.maxSmsAllowed = maxSmsAllowed;
    }
	public String getProvider() {
    	return provider;
    }
	public void setProvider(String provider) {
    	this.provider = provider;
    }
	public HashMap getOriginators() {
    	return originators;
    }
	public void setOriginators(HashMap originators) {
    	this.originators = originators;
    }
	public HashMap getOriginatorRouters() {
    	return originatorRouters;
    }
	public void setOriginatorRouters(HashMap originatorRouters) {
    	this.originatorRouters = originatorRouters;
    }
	public Object getCustomization() {
    	return customization;
    }
	public void setCustomization(Object customization) {
    	this.customization = customization;
    }
	public IGatewayA2Psms getGatewaySms() {
    	return gatewaySms;
    }
	public void setGatewaySms(IGatewayA2Psms gatewaySms) {
    	this.gatewaySms = gatewaySms;
    }
	public Originator getFinalOriginator(String myUUID, String phoneNumber, XasUser xasUser) throws XASException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, Provider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "getFinalOriginator").preset("default");
 
		logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, "a_phoneNumber", phoneNumber, SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(xasUser)).getLogRow(true)); 
		
		String[] originatorArray = xasUser.getOriginator().split(Configuration.ATT_XU_ORIGINATOR_SEPARATOR);
		
		Originator originator = null;
		HashMap originatorRouters = this.getOriginatorRouters();
		if (!originatorRouters.isEmpty()) {
			Iterator iterator = originatorRouters.entrySet().iterator();
			while (iterator.hasNext()) {
			  Entry entry = (Entry) iterator.next();
			  String key = (String) entry.getKey();
			  Originator value = (Originator) entry.getValue();
			  if (value.getRouterRegex() != null && value.getRouterRegex().matcher(phoneNumber).matches())
				  if (originator == null) 
					  originator = value;
				  else
					  throw new XASException(ConstantsSms.XAS01015E_MESSAGE, null, ConstantsSms.XAS01015E_CODE, new Object[] {phoneNumber, originator.getName(), key});
					  
			  		
			}
			if (originator != null) {
				// If originatorRouters apply use only if present in the xasUser.originators list
				int oi = 0;
				while (oi < originatorArray.length && !originator.getName().equals(originatorArray[oi])) 
					oi++;
				if (oi >= originatorArray.length) // Router Originator not found, invalidate it 
					originator = null;
				else // Router Originator found, use it
					xasUser.setOriginator(originator.getName());
			}
		}

		// If originatorRouters don't apply use the first defined originator
		if (originator == null) 
			originator = (Originator) this.getOriginators().get(originatorArray[0]);
		
		if (originator == null)	
			throw new XASException(ConstantsSms.XAS01016E_MESSAGE, null, ConstantsSms.XAS01016E_CODE, new Object[] {xasUser.getOriginator()});
		
		
		logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, SmartLog.K_RETURN_VALUE, originator != null ? originator.getName() : "null").getLogRow(true)); 
	    return originator;
    }
    
}
