/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfimpl.a2psms.dataobject;

import java.text.MessageFormat;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsResponse3;


/**
 * @author us01391
 */
public class InternalSmsResponse {
	private static final long serialVersionUID = 1L;
	
	private SmsResponse3 source = new SmsResponse3();

	public InternalSmsResponse(String code, String message, String[] params, String[] smsIds) {
		source.setCode(code);
		if (params != null)  {
			Object[] args = params;
			try {
				message = MessageFormat.format(message, args);
			} catch(IllegalArgumentException e) {
				// Swallow exception to avoid something bad after sending sms; actually this constructor should not be used when everything's ok
			}
		}
		source.setMessage(message);
		source.setSmsIds(smsIds);
	}

	public InternalSmsResponse(Exception e) {
		source.setCode(ConstantsSms.XAS00002E_CODE);
	    Object[] msgArgs = {e.getMessage() 
					+ ((e.getCause() != null) ? " - " 
							+ e.getCause().getMessage() : "")};
		source.setMessage(MessageFormat.format(ConstantsSms.XAS00002E_MESSAGE, msgArgs));
	}
	
  	private boolean gatewayContacted = false;
	private int totalSms = 0;
	
	public boolean getGatewayContacted() {
    	return gatewayContacted;
    }

	public void setGatewayContacted(boolean gatewayContacted) {
    	this.gatewayContacted = gatewayContacted;
    }
	public int getTotalSms() {
    	return totalSms;
    }

	public void setTotalSms(int totalSms) {
    	this.totalSms = totalSms;
    }

	public InternalSmsResponse(SmsResponse3 source) {
		this.source = source;
	}

	public InternalSmsResponse() {
	    // TODO Auto-generated constructor stub
    }
	public String getCode() { return source.getCode(); }
	public String getMessage() { return source.getMessage(); }
	public String[] getSmsIds() { return source.getSmsIds(); }
	public String getUuid() { return source.getUuid(); }
	public void setUuid(String uuid) { source.setUuid(uuid); }
	public void setCode(String code) { source.setCode(code); }
	public void setMessage(String message) { source.setMessage(message); }
	public void setSmsIds(String[] smsIds) { source.setSmsIds(smsIds); }
	public SmsResponse getSmsResponse() { return source; }
	public SmsResponse3 getSmsResponse3() { return source; }

}
