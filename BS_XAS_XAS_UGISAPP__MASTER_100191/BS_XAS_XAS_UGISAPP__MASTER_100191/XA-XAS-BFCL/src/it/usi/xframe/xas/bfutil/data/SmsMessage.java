/*
 * Created on Jan 18, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SmsMessage implements Serializable {
	String phoneNumber;
	String msg;
	
	public SmsMessage(){
	}

	/**
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param string
	 */
	public void setMsg(String string) {
		msg = string;
	}

	/**
	 * @param string
	 */
	public void setPhoneNumber(String string) {
		phoneNumber = string;
	}

}
