package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 */
public class DeliveryRequest implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -5778032791143202364L;
    private String message;
    private String code;
    private String phoneNumber;
    private String gatewayStatusCode;
    private String[] smsIds;
    private String moDestinator;	//#TIM
    private String uuid;	//#TIM
	public String getMessage() {
    	return message;
    }

	public void setMessage(String message) {
    	this.message = message;
    }

	public String getCode() {
    	return code;
    }

	public void setCode(String code) {
    	this.code = code;
    }

	public String getPhoneNumber() {
    	return phoneNumber;
    }

	public void setPhoneNumber(String phoneNumber) {
    	this.phoneNumber = phoneNumber;
    }

	public String getGatewayStatusCode() {
    	return gatewayStatusCode;
    }

	public void setGatewayStatusCode(String gatewayStatusCode) {
    	this.gatewayStatusCode = gatewayStatusCode;
    }

	public String[] getSmsIds() {
    	return smsIds;
    }

	public void setSmsIds(String[] smsIds) {
    	this.smsIds = smsIds;
    }

	public String getMoDestinator() { //#TIM
    	return moDestinator;
    }

	public void setMoDestinator(String moDestinator) { //#TIM
    	this.moDestinator = moDestinator;
    }

	public String getUuid() {
    	return uuid;
    }

	public void setUuid(String uuid) {
    	this.uuid = uuid;
    }
}
