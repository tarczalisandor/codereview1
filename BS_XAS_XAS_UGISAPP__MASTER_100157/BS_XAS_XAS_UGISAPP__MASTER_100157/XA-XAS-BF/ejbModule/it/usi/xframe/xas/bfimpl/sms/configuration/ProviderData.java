package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.providers.acotel.GatewaySmsAcotel;
import it.usi.xframe.xas.bfimpl.sms.providers.virtual.GatewaySmsVirtual;
import it.usi.xframe.xas.bfimpl.sms.providers.vodafone.GatewaySmsVodafone;
import it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.GatewaySmsVodafonePop;
import it.usi.xframe.xas.bfutil.XASException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProviderData
{
	private static Log log = LogFactory.getLog(ProviderData.class);

	private String mName = null;
	private String mType = null;
	private String mDescription = null;
	private String mLogPrefix = null;
	private String mSmsLogLevel = null;
	private GatewaySms mProvider = null;
	private ProviderConfigurationReader configurationReader;

	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}

	public String getType() {
		return mType;
	}
	public void setType(String type) {
		this.mType = type;
	}

	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getLogPrefix() {
		return mLogPrefix;
	}
	public void setLogPrefix(String logprefix) {
		this.mLogPrefix = logprefix;
	}

	public String getSmsLogLevel() {
		return mSmsLogLevel;
	}
	public void setSmsLogLevel(String smsloglevel) {
		this.mSmsLogLevel = smsloglevel;
	}

	public GatewaySms getProvider() {
		return mProvider;
	}
	public void setProvider(GatewaySms provider) {
		this.mProvider = provider;
	}
	
	public void createConfigurationReader(Configuration mainConfiguration) throws XASConfigurationException {
		if (mType.equalsIgnoreCase(GatewaySmsVodafonePop.TYPE)) configurationReader = new it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.ConfigurationReader(mainConfiguration);
		/*	DEPRECATED 2016.07.18
		else if (mType.equalsIgnoreCase(GatewaySmsVodafone.TYPE)) configurationReader = new it.usi.xframe.xas.bfimpl.sms.providers.vodafone.ConfigurationReader(mainConfiguration);
		else if (mType.equalsIgnoreCase(GatewaySmsAcotel.TYPE)) configurationReader = new it.usi.xframe.xas.bfimpl.sms.providers.acotel.ConfigurationReader(mainConfiguration);
		else if (mType.equalsIgnoreCase(GatewaySmsVirtual.TYPE)) configurationReader = new it.usi.xframe.xas.bfimpl.sms.providers.virtual.ConfigurationReader(mainConfiguration);
		 */
		else throw new XASConfigurationException("Unrecognized provider type: " + mType);
	}
	
	public ProviderConfigurationReader getConfigurationReader() {
		return configurationReader;
	}
	
	public void createProvider() throws XASException {
		this.mProvider = configurationReader.createProvider(this);
	}
}
