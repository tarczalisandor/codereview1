/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfutil.XASException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewayA2pSmsFactory {

	private static GatewayA2pSmsFactory instance = new GatewayA2pSmsFactory();

	/**
	 * A2Psms configuration.
	 * 
	 * WARNING: always use getConfiguration() method.
	 */
	
	protected GatewayA2pSmsFactory() {
	}

	public static GatewayA2pSmsFactory getInstance() {
		return instance;
	}

	/**
	 * Select proper GatewaySms for sendSms.
	 * Allows to select specific GatewaySms for certain userid.
	 * 
	 * @param userid - UBIS user requester
	 * @return
	 * @throws XASException
	 * @throws XASConfigurationException 
	 */
	public Provider getProvider(XasUser xasUser) throws XASException {
 		return getConfiguration().getProvider(xasUser);
  	}

	/**
	 * Configuration for a2psms.
	 * WARNING: always use this method to access Configuration
	 */
	public synchronized Configuration getConfiguration() throws XASException {
		return Configuration.getInstance();
	}
	
}
