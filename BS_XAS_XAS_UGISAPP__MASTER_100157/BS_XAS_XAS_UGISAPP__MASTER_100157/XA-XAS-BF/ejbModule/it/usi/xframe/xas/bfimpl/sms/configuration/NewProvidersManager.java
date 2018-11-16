package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author US01391
 *
 */
public class NewProvidersManager
{
	private static Log log = LogFactory.getLog(NewProvidersManager.class);

	// if you add a member variable, remember to manage it in the reset method
	
	private Map providers = new HashMap();			//contains ProviderData objects
	private Map userProviders = new HashMap();		//contains provider names (String)
	private Map clientProviders = new HashMap();	//contains provider names (String)
	private String defaultProvider = null;
	private List backupProviders = new ArrayList();	//contains provider names (String)
	private List defaultProviders;					//default provider + backup providers

	public void addProvider(ProviderData provider) throws XASConfigurationException {
		if (provider.getName() == null) throw new XASConfigurationException("ProviderData contains null name");
		providers.put(provider.getName(), provider);
	}
	
	public void addUserProvider(String userid, String providerName) throws XASConfigurationException {
		if (userid == null) throw new XASConfigurationException("userid is null");
		if (providerName == null) throw new XASConfigurationException("providerName is null");
		putElementInList(userProviders,userid,providerName);
	}
	
	public void addClientProvider(String client, String providerName) throws XASConfigurationException {
		if (client == null) throw new XASConfigurationException("client is null");
		if (providerName == null) throw new XASConfigurationException("providerName is null");
		putElementInList(clientProviders,client,providerName);
	}
	
	private void putElementInList(Map map, String key, String element) {
		List valueList = (List)map.get(key);
		if (valueList == null) {
			valueList = new ArrayList();
			map.put(key, valueList);
		}
		valueList.add(element);
	}
	
	public String getDefaultProvider() {
		return defaultProvider;
	}

	public void setDefaultProvider(String defaultProvider) {
		this.defaultProvider = defaultProvider;
	}

	public void addBackupProvider(String providerName) throws XASConfigurationException {
		if (providerName == null) throw new XASConfigurationException("providerName is null");
		backupProviders.add(providerName);
	}
	
	/**
	 * reset the data in the manager forcing the reload of configuration
	 */
	public void reset()
	{
		providers.clear();
		userProviders.clear();
		clientProviders.clear();
		defaultProvider = null;
		backupProviders.clear();
		defaultProviders = null;
	}

	/**
	 * Try to find or load the provider
	 * 
	 * @param userid
	 * @return
	 * @throws XASException
	 */
	public GatewaySms getProvider(String userid, String client) throws XASConfigurationException
	{
		List providersList = (List)clientProviders.get(client);
		if (providersList == null) providersList = (List)userProviders.get(userid);
		if (providersList == null) providersList = loadDefaultProviders();
		for (int i=0; i<providersList.size(); i++) {
			GatewaySms provider = getSmsProvider((String)providersList.get(i));
			if (provider.isActive()) return provider;
		}
		throw new XASRuntimeException("No active provider found");

	}

	public synchronized List loadDefaultProviders() {
		if (defaultProviders == null) {
			defaultProviders = new ArrayList();
			defaultProviders.add(defaultProvider);
			defaultProviders.addAll(backupProviders);
		}
		return defaultProviders;
	}
	
	/**
	 * Try to find or load the provider
	 * 
	 * @param providerName
	 * @return
	 * @throws XASException
	 */
	private GatewaySms getSmsProvider(String providerName) throws XASConfigurationException
	{
		ProviderData providerData = (ProviderData) providers.get(providerName);
		if (providerData == null)
			throw new XASConfigurationException("ProviderData not found: " + providerName);
		
		return providerData.getProvider();
	}

}
