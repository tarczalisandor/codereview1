/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfutil.data;

/**
 * Response for version 3 of the sendSms interface.
 */
public class SmsResponse3 extends SmsResponse {

	private String uuid = null;

	public String getUuid() {
    	return uuid;
    }

	public void setUuid(String uuid) {
    	this.uuid = uuid;
    }
}
