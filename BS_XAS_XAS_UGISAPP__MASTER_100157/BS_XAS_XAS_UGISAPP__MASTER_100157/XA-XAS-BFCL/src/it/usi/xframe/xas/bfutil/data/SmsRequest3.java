/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfutil.data;
        

/**
 * Request for version 3 of the sendSms interface.
 */
public class SmsRequest3 extends SmsRequest {
	/**
	 * Service class for sms message replacement on mobile terminal.  
	 * 
	 * Allowed value A to Z or null.
	 */
	private String replaceClass;
	
	private String uuid;
	
	private ENC_TYPE encryptionType;

	public String getReplaceClass() {
    	return replaceClass;
    }

	public void setReplaceClass(String replaceClass) {
    	this.replaceClass = replaceClass;
    }

	public String getUuid() {
    	return uuid;
    }

	public void setUuid(String uuid) {
    	this.uuid = uuid;
    }

	public void setEncryptionType(ENC_TYPE encryptionType) {
	    this.encryptionType = encryptionType;
    }

	public ENC_TYPE getEncryptionType() {
	    return encryptionType;
    }

}
