/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.configuration.ClientsContainer;
import it.usi.xframe.xas.bfimpl.sms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.sms.configuration.ProvidersManager;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GatewaySmsFactory
{
	private static Log log = LogFactory.getLog(GatewaySmsFactory.class);

	private static GatewaySmsFactory instance = new GatewaySmsFactory();

	private ProvidersManager providers = new ProvidersManager();	// maintain the provider configuration 
	
	private Configuration configuration;	//new sms configuration - WARNING: always use getConfiguration() method
	
	protected GatewaySmsFactory() {
	}

	public static GatewaySmsFactory getInstance() {
		return instance;
	}

	/**
	 * Select proper GatewaySms.
	 * Allows to select specific GatewaySms for certain userid.
	 * 
	 * @param userid - UBIS user requester
	 * @return
	 * @throws XASException
	 */
	public IGatewaySms getGatewaySms(String userid) throws XASException
	{
		IGatewaySms provider = providers.getProvider(userid);

		return provider;
	}
	
	public IGatewaySms getGatewaySms(String userid, InternalSmsRequest request) throws XASConfigurationException {
		IGatewaySms provider = getConfiguration().getProvidersManager().getProvider(userid, request.getNormalizedXasUser());
		return provider;
	}
	
	public ClientsContainer getClients() throws XASConfigurationException {
		//call getProvidersManager() first to be sure that configuration is up to date - 2/12/2015 should be useless after refactoring Configuration to be loaded as it is instantiated
		Configuration conf = getConfiguration();
		//NewProvidersManager pm = conf.getProvidersManager();
		return conf.getClients();
	}
	
	//WARNING: always use this method to access Configuration
	private synchronized Configuration getConfiguration() throws XASConfigurationException {
		if (configuration == null) configuration = Configuration.getInstance(); 
		return configuration;
	}
	
}
