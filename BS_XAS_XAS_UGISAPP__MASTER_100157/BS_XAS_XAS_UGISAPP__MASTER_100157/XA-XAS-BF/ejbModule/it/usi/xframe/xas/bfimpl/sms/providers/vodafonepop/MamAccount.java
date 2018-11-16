package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ie.omk.smpp.util.GSMConstants;

public class MamAccount {
	private static Log log = LogFactory.getLog(MamAccount.class);
	
	private String systemId = null;
	private String password = null;
	private String address = null;
	private String alias = null;
	private int typeOfNumber = GSMConstants.GSM_TON_UNKNOWN;
	private int numPlanId = GSMConstants.GSM_NPI_UNKNOWN;
	private String routerRegex = null;
	
	public String getOriginator() {
		return (this.alias != null && this.alias.length()!=0) ? this.alias : this.address;
	}
	
	public int getNumPlanId() {
    	return numPlanId;
    }
	public void setNumPlanId(int numPlanId) {
    	this.numPlanId = numPlanId;
    }
	public String getRouterRegex() {
    	return routerRegex;
    }
	public void setRouterRegex(String routerRegex) {
    	this.routerRegex = routerRegex;
    }
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		//log.debug("setSystemId:" + systemId);
		this.systemId = systemId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getTypeOfNumber() {
		return typeOfNumber;
	}
	public void setTypeOfNumber(int typeOfNumber) {
		this.typeOfNumber = typeOfNumber;
	}

	
}
