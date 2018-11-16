/*
 * Created on Mar 28, 2012
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
public class SmsBillingInfo implements Serializable {
	// Legal entity that pays for the sms (mandatory)
	// E.g. 'UBIS', 'Unicredit', 'Bank Austria', etc.
	private String legalEntity;
	
	// Sms usually is sent to accomplish a service. (optional)
	// E.g. 'pinSafe', 'smsPremium', etc.
	private String serviceName;
	

	/**
	 * @return
	 */
	public String getLegalEntity() {
		return legalEntity;
	}

	/**
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param string
	 */
	public void setLegalEntity(String string) {
		legalEntity = string;
	}

	/**
	 * @param string
	 */
	public void setServiceName(String string) {
		serviceName = string;
	}

}
