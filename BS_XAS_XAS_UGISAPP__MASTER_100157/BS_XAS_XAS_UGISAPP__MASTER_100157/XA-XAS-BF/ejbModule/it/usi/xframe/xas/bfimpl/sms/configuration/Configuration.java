package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.xframe.slf.SmartLog;

public class Configuration {
	
	private static Log log = LogFactory.getLog(Configuration.class);
	private static Configuration instance;

	private static final String JNDI_SMS_CONFIG = "java:comp/env/url/smsconfig";

	private List generalOriginators = new ArrayList();
	private ClientsContainer clients = new ClientsContainer();
	private NewProvidersManager providersManager = new NewProvidersManager();
	private MamAccountRouter mamAccountRouter = new MamAccountRouter();
 
	private boolean mConfigInitialized = false;

	private long DEFAULT_CACHE_INTERVAL_TIMEOUT = 60*60*1000;		// millisec for cache timeout, default = 1 hour
	private long mCacheTimeout = DEFAULT_CACHE_INTERVAL_TIMEOUT;	// cache invalidation timeout in millisec
	private long mCacheInvalidationTime = 0;						// time for cache invalidation in millisec
	private TimeZone jmsTimeZone = TimeZone.getTimeZone("UCT");
	
	private Configuration() throws XASConfigurationException {
		//log.debug("Configuration:initialize()");
		resetCacheTimeout();
		initialize();
	}

 	public static synchronized Configuration getInstance() throws XASConfigurationException {
		if (instance == null) instance = new Configuration();
		return instance;
	}
	
	public NewProvidersManager getProvidersManager() throws XASConfigurationException {
		check();
		return providersManager;
	}
	
	public List getGeneralOriginators() {
		return generalOriginators;
	}
	
	public MamAccountRouter getMamAccountRouter() {
		return mamAccountRouter;
	}
	public ClientsContainer getClients() {
		return clients;
	}
	
	public TimeZone getJmsTimeZone() {
		return jmsTimeZone;
	}

	public void addOriginator(String originator) {
		generalOriginators.add(originator);
	}
	
	public void addClient(Client client) {
		clients.put(client.getName(), client);
	}
	
	public Client getClient(String key) {
		return (Client)clients.get(key);
	}
	
	public void addProviderData(ProviderData pd) throws XASConfigurationException {
		providersManager.addProvider(pd);
	}
	
	public void setDefaultProvider(String p) {
		providersManager.setDefaultProvider(p);
	}
	
	public void addBackupProvider(String p) throws XASConfigurationException {
		providersManager.addBackupProvider(p);
	}
	
	public void addUserProvider(String userid, String p) throws XASConfigurationException {
		providersManager.addUserProvider(userid, p);
	}
	
	public void addClientProvider(String client, String p) throws XASConfigurationException {
		providersManager.addClientProvider(client, p);
	}
	
	
	public void setCacheTimeout(String value) throws XASConfigurationException {
		try {
			int seconds = Integer.parseInt(value);
			this.mCacheTimeout = seconds * 1000;
		} catch(NumberFormatException e) {
			throw new XASConfigurationException("Invalid value for cache timeout");
		}
	}
	
	public void setJmsTimeZone(String jmsTimeZone) {
		this.jmsTimeZone = TimeZone.getTimeZone(jmsTimeZone);
	}

	private synchronized void check() throws XASConfigurationException {
		if (! isCacheValid()) updateCache();
		if (! mConfigInitialized) {
			//log.debug("check:initialize()");
			initialize();
		}
	}

	/**
	 * reset the data forcing the reload of configuration
	 *
	 * @throws XASException
	 */
	private void reset()
	{
		generalOriginators.clear();
		clients.clear();
		providersManager.reset();
		mamAccountRouter.reset();
		mConfigInitialized = false;
	}

	/**
	 * Initialize the manager
	 * 
	 * @throws XASException
	 */
	private void initialize() throws XASConfigurationException
	{
		//log.debug("initialize()");
		// set the configuration
		loadConfig();

		// set the class as initialized
		mConfigInitialized = true;
	}

	/**
	 * Load the first sms configuration properties.
	 * This load the special user list and the default provider name
	 * 
	 * @throws XASException
	 */
	private void loadConfig() throws XASConfigurationException {
		try {
			SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", "", SmartLog.V_SCOPE_DEBUG).logReferrer(0);

			URL fileURL = null;
			Context ctx = new InitialContext();
			fileURL = (URL)ctx.lookup(JNDI_SMS_CONFIG); 

			try {
	            fileURL = new URL(fileURL.getProtocol(),fileURL.getHost(),fileURL.getFile().replaceAll("\\.xml", "_v2\\.xml"));  // VERSION2
            } catch (MalformedURLException e) {
	            e.printStackTrace();
            }

            sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_INIT, Constants.K_CONFIG_FILE, fileURL.getFile());
			log.debug(sl.getLogRow());
			ConfigurationReader r = new ConfigurationReader();
			//log.info("Going to read Sms Configuration file:" + fileURL);
			r.read(fileURL, this);
		} catch(NamingException e) {
			throw new XASConfigurationException("Error looking for configuration file",e);
		}
	}
	
	/**
	 * Return a true if cache is valid or false if not
	 *  
	 * @return
	 */
	private boolean isCacheValid()
	{
		if(this.mCacheInvalidationTime > System.currentTimeMillis())
			return true;
		return false;
	}

	/**
	 * Invalidate data and reset the expire period of cache.
	 */
	private void updateCache()
	{
		reset();
        // as LAST operation, update the new cache timeout
		resetCacheTimeout();
	}

	/**
	 * Reset the expire period of cache.
	 */
	private void resetCacheTimeout()
	{
		this.mCacheInvalidationTime = System.currentTimeMillis() + mCacheTimeout;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date timeNow = new Date(System.currentTimeMillis());
        Date timeExpire = new Date(mCacheInvalidationTime);
		if (log.isDebugEnabled()) log.debug("Set cache timeout to " + sdf.format(timeExpire) + ", now the time is " + sdf.format(timeNow));
	}
	
}
