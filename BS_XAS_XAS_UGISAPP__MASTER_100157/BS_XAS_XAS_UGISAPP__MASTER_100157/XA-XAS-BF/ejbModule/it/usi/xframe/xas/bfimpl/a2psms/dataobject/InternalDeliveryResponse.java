/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfimpl.a2psms.dataobject;

import it.usi.xframe.xas.bfutil.data.DeliveryResponse;


/**
 * @author us01391
 */
public class InternalDeliveryResponse {
	private static final long serialVersionUID = 1L;
	
	private DeliveryResponse source;
	private String wsEndpointSC;
	private String wsEndpointSM;

	public InternalDeliveryResponse(DeliveryResponse source) {
		this.source = source;
	}
	
	public String getCode() { return source.getCode(); }
	public String getMessage() { return source.getMessage(); }
	public String[] getSmsIds() { return source.getSmsIds(); }
	public DeliveryResponse getDeliveryResponse() { return source; }

	/**
	 * EndPoint status message.
	 * @return
	 */
	public String getWsEndpointSM() {
    	return wsEndpointSM;
    }

	/**
	 * EndPoint status message.
	 * @return
	 */
	public void setWsEndpointSM(String wsEndpointSM) {
    	this.wsEndpointSM = wsEndpointSM;
    }

	/**
	 * EndPoint status code.
	 * @return
	 */
	public String getWsEndpointSC() {
    	return wsEndpointSC;
    }

	/**
	 * EndPoint status code.
	 * @return
	 */
	public void setWsEndpointSC(String wsEndpointSC) {
    	this.wsEndpointSC = wsEndpointSC;
    }


}
