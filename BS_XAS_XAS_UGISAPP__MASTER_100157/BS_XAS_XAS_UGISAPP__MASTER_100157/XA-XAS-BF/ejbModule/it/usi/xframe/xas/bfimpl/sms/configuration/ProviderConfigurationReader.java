package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfutil.XASException;

import org.xml.sax.ContentHandler;

public interface ProviderConfigurationReader extends ContentHandler {

	public GatewaySms createProvider(ProviderData pd) throws XASException;
	
	public Configuration getMainConfiguration();
	
	public boolean isEnded();
}
