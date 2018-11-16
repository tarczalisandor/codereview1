package it.usi.xframe.xas.bfimpl.sms.configuration;

import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractProviderConfigurationReader extends DefaultHandler implements ProviderConfigurationReader {
	
	private Configuration mainConfiguration;
	
	protected boolean ended;
	
	public AbstractProviderConfigurationReader(Configuration mainConfig) {
			mainConfiguration = mainConfig;
	}
	
	public Configuration getMainConfiguration() {
		return mainConfiguration;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
}
