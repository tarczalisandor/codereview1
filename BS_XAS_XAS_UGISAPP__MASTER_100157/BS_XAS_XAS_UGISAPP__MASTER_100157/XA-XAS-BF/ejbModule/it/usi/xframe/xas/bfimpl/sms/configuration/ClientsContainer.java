package it.usi.xframe.xas.bfimpl.sms.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientsContainer extends HashMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ClientsContainer.class);
	
	public Client getFinalClient(String clientId) throws XASConfigurationException {
		Client client = (Client)get(clientId);
		if (client == null) throw new XASConfigurationException("Client not found: " + clientId);
		ArrayList stack = new ArrayList();
		while(client.getAlias() != null && client.getAlias().length() != 0) {
			// avoid circular reference into alias
			if(stack.contains(client.getAlias()))
				throw new XASConfigurationException("Aliases are referenced in a circular way: XAS user id =" + clientId + ", alias=" + client.getAlias());

			// add an element to the stack
			stack.add(client.getAlias());

			Client aliasClient = (Client) get(client.getAlias());
	    	if ( aliasClient == null )
				throw new XASConfigurationException("Alias client not defined: " + client.getAlias() + " for clientId " + clientId);

			client = aliasClient.clone(client.getName());
		}
		return client;
	}
	
	

}
