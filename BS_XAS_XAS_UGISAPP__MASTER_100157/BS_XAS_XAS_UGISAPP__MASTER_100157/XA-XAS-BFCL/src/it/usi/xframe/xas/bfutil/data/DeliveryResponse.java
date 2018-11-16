/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfutil.data;

import it.usi.xframe.xas.bfutil.Constants;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Response to a {@link SmsRequest}.
 * <p/>
 */
public class DeliveryResponse implements Serializable {
	
	public DeliveryResponse() {}
	
	public DeliveryResponse(Exception e) {
		setCode(Constants.XAS00002E_CODE);
        Object[] msgArgs = {e.getMessage() 
					+ ((e.getCause() != null) ? " - " 
							+ e.getCause().getMessage() : "")};
		setMessage(MessageFormat.format(Constants.XAS00002E_MESSAGE, msgArgs));
	}

	public DeliveryResponse(String code, String message) {
		this(code, message, (String[])null);
	}
	
	public DeliveryResponse(String code, String message, String param) {
		this(code, message, new String[]{param});
	}
	
	public DeliveryResponse(String code, String message, String[] param) {
		setCode(code);
		if (param != null)  {
			Object[] args = param;
			try {
				message = MessageFormat.format(message, args);
			} catch(IllegalArgumentException e) {
				// Swallow exception to avoid something bad after sending sms; actually this constructor should not be used when everything's ok
			}
		}
		setMessage(message);
	}
	
	private static final long serialVersionUID = 1L;
	/** 
	 * Code.
	 * <p/>
	 * XAS00000I Ok
	 * XAS00001W Warning some chars were transalated 
	 * XAS00002E Error code, 
	 */
	private String code;

	/**
	 * Text message.
	 * <p/>
	 */
	private String message;

	/**
	 * ID Sms message.
	 */
	private String[] smsIds;

	/**
	 * @see #code
	 * @return The error code.
	 */
	public String getCode() {
    	return code;
    }

	/**
	 * @see #code
	 * @param code The error code.
	 */
	public void setCode(String code) {
    	this.code = code;
    }

	/**
	 * Test if the message code is Informational.
	 * @return true if message ends with I.
	 */
	public boolean isOk() {
		return code.matches("^.{3}[0-9]{5}I$");
	}

	/**
	 * Test if the message code is Warning.
	 * @return true if message ends with W.
	 */
	public boolean isWarning() {
		return code.matches("^.{3}[0-9]{5}W$");
	}

	/**
	 * Test if the message code is Error.
	 * @return true if message ends with E.
	 */
	public boolean isError() {
		return code.matches("^.{3}[0-9]{5}E$");
	}

	/**
	 * @see #message
	 * @return The message.
	 */
	public String getMessage() {
    	return message;
    }

	/**
	 * @see #message
	 * @param message the message.
	 */
	public void setMessage(String message) {
    	this.message = message;
    }

	/**
	 * @see #smsId
	 * @return the flag.
	 */
	public String[] getSmsIds() {
    	return smsIds;
    }

	/**
	 * @see #smsId
	 * @param smsId the flag.
	 */
	public void setSmsIds(String[] smsIds) {
    	this.smsIds = smsIds;
    }

}
