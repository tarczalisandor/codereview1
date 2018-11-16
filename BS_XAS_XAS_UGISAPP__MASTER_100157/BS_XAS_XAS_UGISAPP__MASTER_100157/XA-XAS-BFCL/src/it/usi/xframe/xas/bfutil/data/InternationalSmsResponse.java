/*
 * Created on Mar 29, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 * @author US00081
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InternationalSmsResponse implements Serializable {

	static public final int OK = 0;
	static public final int GENERIC_ERROR = -1;
	static public final int PHONE_NUMBER_FORMAT_ERROR = -2;
	static public final int INTERNATIONAL_PREFIX_ERROR = -3;
	static public final int SERVICE_NAME_MISSING_ERROR = -4;
	static public final int SERVICE_NAME_TOO_LONG_ERROR = -5;
	static public final int LEGAL_ENTITY_MISSING_ERROR = -6;
	static public final int LEGAL_ENTITY_NAME_TOO_LONG_ERROR = -7;
	static public final int MESSAGE_TEXT_TOO_LONG_ERROR = -8;
	

	// status code
	private int code;
	// status description
	private String descr;
	
	
	public InternationalSmsResponse(int statusCode, String statusDescription) {
		this.code = statusCode;
		this.descr = statusDescription;
	}
	
	public InternationalSmsResponse() {
			this.code = OK;
			this.descr = "SMS message enqueued succesfully";
	}
	
	/**
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * @param i
	 */
	public void setCode(int i) {
		code = i;
	}

	/**
	 * @param string
	 */
	public void setDescr(String string) {
		descr = string;
	}

}
