/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfutil.data;

import java.util.Date;

/**
 * @author us01391
 */
public class SmsRequest {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final long serialVersionUID = 1L;
	
	/** 
	 * Destinator phone number.
	 * <p/>
	 * In accordance with the ITU-T E.123 standard for International Phone Number Planing,
	 * the phone number should be in the form. 
	 * 
	 * +yyyxxxxxxxxx
	 * 
	 * Where </br>
	 *  + is the International Access Code (Exit Code) </br>
	 *  yyy is 1 to 3 digit International Country Code (i.e. 39-Italy 49-Deutschland 1-America 7-Russian Federation 353-Ireland </br>
	 *  xxxxxxx is the national phone number </br>
	 * 
	 * Please consider that for backward compatibility instead of plus sign (+) will be accepted also the double o (00) standard prefix.
	 */
	private String phoneNumber;

	/**
	 * Text message.
	 * <p/>
	 * <b>Please note: the length/cost of sms message depends upon the type of character you use.</b><br/>
	 * Using <a href="http://en.wikipedia.org/wiki/GSM_03.38">ascii</a> characters ensure that the length is the count of the character you use.
	 * Other characters, for example the german's umlaut, will double the length/cost of your message forcing the 
	 * usage of <a href="http://en.wikipedia.org/wiki/GSM_03.38#UCS-2_Encoding">UCS-2</a>.
	 * @see #forceAsciiEncoding
	 * 
	 */
	private String msg;

	/**
	 * Technical Xas User.
	 */
	private String xasUser;
	
	/** 
	 * Force the encoding code page on ascii [Optional, default=false].
	 * <p/>
	 * Translate any extra-ascii char with an ascii-character, due to avoid enlargement of sms length an extra-cost.
	 * @see #msg  
	 */
	private Boolean forceAsciiEncoding = null;
	
	/** 
	 * Request the enqueueing or an smsResponse [Optional, default=false].
	 * <p/>
	 * <b>Available only for MQ</b>.
	 */
	private Boolean smsResponse = new Boolean(false);

	/**
	 * Correlation ID [Optional].
	 * <p/>
	 * Used to identify the log rows of the execution for this transaction.<br/>
	 * <b>If available please use your UUID (Universally Unique IDentifier).</b>
	 */
	private String correlationID;
	
	/**
	 * Validity dateTime.
	 * <p/>
	 * Used to set a time limit to send the sms.
	 */
	private Date validity;

	/**
	 * Validity period.
	 * <p/>
	 * Used to set a relative time limit to send the sms.
	 */
	private Integer validityPeriod;

	/**
	 * Delivery Time.
	 * <p/>
	 * Used to set a time to send the sms.
	 */
	private Date deliveryTime;
	
	public Date getValidity() {
    	return validity;
    }

	public void setValidity(Date validity) {
    	this.validity = validity;
    }

	public Integer getValidityPeriod() {
    	return validityPeriod;
    }

	public void setValidityPeriod(Integer validityPeriod) {
    	this.validityPeriod = validityPeriod;
    }

	public Date getDeliveryTime() {
    	return deliveryTime;
    }

	public void setDeliveryTime(Date deliveryTime) {
    	this.deliveryTime = deliveryTime;
    }

	/**
	 * @see #correlationID
	 * @return The correlation id.
	 */
	public String getCorrelationID() {
    	return correlationID;
    }

	/**
	 * @see #correlationID
	 * @param correlationID The correlation id.
	 */
	public void setCorrelationID(String correlationID) {
    	this.correlationID = correlationID;
    }

	/**
	 * @see #smsResponse
	 * @return the flag.
	 */
	public Boolean getSmsResponse() {
    	return smsResponse;
    }

	/**
	 * @see #smsResponse
	 * @param smsResponse the flag.
	 */
	public void setSmsResponse(Boolean smsResponse) {
    	this.smsResponse = smsResponse;
    }

	/**
	 * @see #forceAsciiEncoding
	 * @return the flag.
	 */
	public Boolean getForceAsciiEncoding() {
    	return forceAsciiEncoding;
    }

	/**
	 * @see #forceAsciiEncoding
	 * @param forceAsciiEncoding the flag.
	 */
	public void setForceAsciiEncoding(Boolean forceAsciiEncoding) {
    	this.forceAsciiEncoding = forceAsciiEncoding;
    }

	/**
	 * @see #xasUser
	 * @return the xas user.
	 */
	public String getXasUser() {
		return xasUser;
	}
	
	/**
	 * @see #xasUser
	 * @param xasUser xas user.
	 */
	public void setXasUser(String xasUser) {
		this.xasUser = xasUser;
	}

	/**
	 * @see #msg
	 * @return the message.
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @see #phoneNumber
	 * @return the phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @see #msg
	 * @param string the msg.
	 */
	public void setMsg(String string) {
		msg = string;
	}

	/**
	 * @see #phoneNumber
	 * @param string the phone number.
	 */
	public void setPhoneNumber(String string) {
		phoneNumber = string;
	}
	
}
