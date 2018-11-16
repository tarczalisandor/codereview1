/*
 * Created on Mar 26, 2012
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
public class SmsDelivery implements Serializable {
	// international phone number format(e.g. 0039xxxxxxx )
	private String phoneNumber;
	/**
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param string
	 */
	public void setPhoneNumber(String string) {
		phoneNumber = string;
	}

}
