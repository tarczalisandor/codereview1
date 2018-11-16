package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import it.usi.xframe.xas.bfutil.data.ENC_TYPE;

import java.util.HashMap;
import java.util.regex.Pattern;

public class XasUser implements Cloneable {  
	private String name                     	= null;
	private Integer interfaceVersion			= null;
	private String originator                	= null;
	private String provider					 	= null;
	private Integer providerWorkload		 	= null;
	/** Not exposed. */
	private String providerBackup			 	= null;
	private boolean defaultForceAsciiEncoding   = false;
	private Integer defaultValidityPeriod       = null;
	private String defaultRegion			 	= null;
	private boolean useBackup					= false;

	private Pattern validatePhoneNumberRegEx	= null;
	private String endPoint					 	= null;  //#TIM
	private boolean deliveryReport				= false; //#TIM
	private String[] moDestinators			 	= null;  //#TIM
	private ENC_TYPE moEncryption			 	= null;  //#TIM
	private HashMap replaceMap					= null;
	private Integer moTimeOut			        = null;
	private Integer drTimeOut			        = null;

	public Integer getMOtimeOut() {
    	return moTimeOut;
    }
	public void setMOtimeOut(Integer moTimeOut) {
    	this.moTimeOut = moTimeOut;
    }
	public Integer getDRtimeOut() {
    	return drTimeOut;
    }
	public void setDRtimeOut(Integer drTimeOut) {
    	this.drTimeOut = drTimeOut;
    }
	protected Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}  
	/**
	 * Get full name.
	 * @return
	 */
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	/**
	 * Get name 5 + 3.
	 * @return
	 */
	public String getNameSmall() {
		return name.substring(0, name.length() > 8 ? 8 : name.length());
	}

	/**
	 * Get name 5 + 3 + 3.
	 * @return
	 */
	public String getNameMedium() {
		return name.substring(0, name.length() > 11 ? 11 : name.length());
	}
	public Integer getInterfaceVersion() {
    	return interfaceVersion;
    }
	public void setInterfaceVersion(Integer interfaceVersion) {
    	this.interfaceVersion = interfaceVersion;
    }
	public Integer getProviderWorkload() {
    	return providerWorkload;
    }
	public void setProviderWorkload(Integer providerWorkload) {
    	this.providerWorkload = providerWorkload;
    }
	
	public String getOriginator() {
    	return originator;
    }
	public void setOriginator(String originator) {
    	this.originator = originator;
    }
	public String getProvider() {
    	return provider;
    }
	public void setProvider(String provider) {
    	this.provider = provider;
    }
	public boolean isDefaultForceAsciiEncoding() {
    	return defaultForceAsciiEncoding;
    }
	public void setDefaultForceAsciiEncoding(boolean defaultForceAsciiEncoding) {
    	this.defaultForceAsciiEncoding = defaultForceAsciiEncoding;
    }
	public Integer getDefaultValidityPeriod() {
    	return defaultValidityPeriod;
    }
	public void setDefaultValidityPeriod(Integer defaultValidityPeriod) {
    	this.defaultValidityPeriod = defaultValidityPeriod;
    }
	public String getDefaultRegion() {
    	return defaultRegion;
    }
	public void setDefaultRegion(String defaultRegion) {
    	this.defaultRegion = defaultRegion;
    }
	public boolean getUseBackup() {
    	return useBackup;
    }
	public void setUseBackup(boolean useBackup) {
    	this.useBackup = useBackup;
    }
 	public String getProviderBackup() {
    	return providerBackup;
    }
	public void setProviderBackup(String providerBackup) {
    	this.providerBackup = providerBackup;
    }
	public static String normalizeName(String xasUserName) {
		return xasUserName == null ? "" : xasUserName.trim();
	}
	public Pattern getValidatePhoneNumberRegEx() {
    	return validatePhoneNumberRegEx;
    }
	public void setValidatePhoneNumberRegEx(Pattern validatePhoneNumberRegEx) {
    	this.validatePhoneNumberRegEx = validatePhoneNumberRegEx;
    }
	public String getEndPoint() { //#TIM
    	return endPoint;
    }
	public void setEndPoint(String endPoint) {	  //#TIM
    	this.endPoint = endPoint;
    }
	public boolean isDeliveryReport() {   //#TIM
    	return deliveryReport;
    }
	public boolean getDeliveryReport() { //#TIM
    	return deliveryReport;
    }
	public void setDeliveryReport(boolean deliveryReport) {   //#TIM
    	this.deliveryReport = deliveryReport;
    }
	public String[] getMoDestinators() {   //#TIM
    	return moDestinators;
    }
	public void setMoDestinators(String[] moDestinators) { 
    	this.moDestinators = moDestinators;
    }
	public HashMap getReplaceMap() {
    	return replaceMap;
    }
	public void setReplaceMap(HashMap replaceMap) {
    	this.replaceMap = replaceMap;
    }
	public void setMoEncryption(ENC_TYPE moEncryption) {
	    this.moEncryption = moEncryption;
    }
	public ENC_TYPE getMoEncryption() {
	    return moEncryption;
    }
}
