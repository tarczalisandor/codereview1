package it.usi.xframe.xas.bfimpl.sms.configuration;

import it.usi.xframe.xas.bfimpl.sms.GatewaySms;
import it.usi.xframe.xas.bfimpl.sms.providers.acotel.AcotelPageDestination;
import it.usi.xframe.xas.bfimpl.sms.providers.acotel.GatewaySmsAcotel;
import it.usi.xframe.xas.bfimpl.sms.providers.minipop.GatewaySmsMinipop;
import it.usi.xframe.xas.bfimpl.sms.providers.virtual.GatewaySmsVirtual;
import it.usi.xframe.xas.bfimpl.sms.providers.vodafone.GatewaySmsVodafone;
import it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.GatewaySmsVodafonePop;
import it.usi.xframe.xas.bfutil.XASException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author C305373
 *
 */
public class ProvidersManager
{
	private static Log log = LogFactory.getLog(ProvidersManager.class);
	private static final String JNDI_SMSGATEWAYPARAMETERS = "java:comp/env/url/smsgatewayparameters";
	private static final String PROPERTIES_EXCEPTION_CODE =	"XAS.GatewaySmsFactory.PropertiesError";
	private final static String PROPERTIES_FILE = "gatewaysms.properties";

	// if you add a member variable, remember to manage it in the reset method
	
	private Hashtable mSpecialUserList = new Hashtable();
	private Hashtable mProviderList = new Hashtable();
	private String mDefaultProvider = null;

	private Properties mProps = null;
	private boolean mConfigInitialized = false;

	private long DEFAULT_CACHE_INTERVAL_TIMEOUT = 60*60*1000;		// millisec for cache timeout, default = 3600 sec
	private long mCacheTimeout = DEFAULT_CACHE_INTERVAL_TIMEOUT;	// cache invalidation timeout in millisec
	private long mCacheInvalidationTime = 0;						// time for cache invalidation in millisec

	/**
	 * Try to find or load the provider
	 * 
	 * @param userid
	 * @return
	 * @throws XASException
	 */
	public synchronized GatewaySms getProvider(String userid) throws XASException
	{
		// validate the data cached 
		if(isCacheValid()==false)
			updateCache();

		String providerName = null;

		if(mConfigInitialized==false)
			initialize();

		// verify if there is special provider for the user
		providerName = (String) mSpecialUserList.get(userid.toLowerCase());
		if(providerName==null)
			providerName = mDefaultProvider;

		// get the provider
		GatewaySms provider = getSmsProvider(providerName);

		return provider;
	}

	/**
	 * reset the data in the manager forcing the reload of configuration
	 *
	 * @throws XASException
	 */
	private void reset() throws XASException
	{
		log.info("Cache: called Reset method");
		mSpecialUserList.clear();
		mProviderList.clear();
		mDefaultProvider = null;
		mProps = null;
		mConfigInitialized = false;
	}

	/**
	 * Initialize the manager
	 * 
	 * @throws XASException
	 */
	private void initialize() throws XASException
	{
		// load the properties
		mProps = loadProperties();
		
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
	private void loadConfig() throws XASException
	{
		try
		{
			//String smsGatewayName = null;
			// look for special route rules (based on userid)
			// Format: <userID1>|<GatewayName1>,<userID2>|<GatewayName2>,... 
			// ex. TA00555|AcotelVrmon,TA00666|AcotelVrmon
			String forceSmsGateway = mProps.getProperty("ForceSmsGateway");
			 //special route rules are present
			if ( forceSmsGateway!=null )
			{
				String[] specialRoutes = forceSmsGateway.split(",");
				log.debug("Special routes: "+StringUtils.join(specialRoutes, " "));
				for (int i=0; i<specialRoutes.length ;++i)
				{
					log.debug("Route: "+specialRoutes[i]);
					String[] toks = specialRoutes[i].split("\\|");
					if (toks.length!=2)
						continue;	//invalid. skip it!
					
					String userTA = toks[0];
					String gatewayName = toks[1];
					log.debug("user:" + userTA + ", provider:" + gatewayName);
					
					mSpecialUserList.put(userTA.toLowerCase(), gatewayName);
				}
			}

			mDefaultProvider = mProps.getProperty("DefaultSmsGateway.name");			

			String cacheTimeout = mProps.getProperty("cachetimeout");
			if(cacheTimeout!=null)
			{
				cacheTimeout = cacheTimeout.trim();
				try
				{
					mCacheTimeout = 1000 * Long.parseLong(cacheTimeout);
					ResetCacheTimeout();
					log.info("Set cache timeout every " + (mCacheTimeout/1000) +  " seconds.");
				}
				catch(NumberFormatException e)
				{
					log.error("Invalid configuration value for parameter cacheTmieout in sms configuration file.");
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			XASException err =	new XASException("Wrong SMS gateway configuration: check gatewaysms.properties file.");
			//log.error(err.getMessage(), err);
			throw new XASException(PROPERTIES_EXCEPTION_CODE, err);
		}
	}
	
	/**
	 * Try to find or load the provider
	 * 
	 * @param providerName
	 * @return
	 * @throws XASException
	 */
	private GatewaySms getSmsProvider(String providerName) throws XASException
	{
		if(providerName==null || providerName.length()==0)
			throw new XASException("null providerName or zero length string");

		// recover the provider from the hashtable
		ProviderData providerData = (ProviderData) mProviderList.get(providerName);
		if(providerData!=null && providerData.getProvider()!=null)
			 return providerData.getProvider();
		
		// if the provider don't exists, load try to it
		providerData = loadProvier(providerName);
		
		return providerData.getProvider();
	}
	
	/**
	 * Load the sms provider
	 * 
	 * @param providerData
	 * @return
	 * @throws XASException
	 */
	private ProviderData loadProvier(String providerName) throws XASException
	{
		GatewaySms provider = null;

		// load the provider data
		ProviderData providerData = loadProvierConfig(providerName);
		
		if(providerData==null || providerData.getName()==null)
			throw new XASException("null providerData or null providerData.name");

		try	{
						
			if (GatewaySmsVodafonePop.TYPE.toLowerCase().equals(providerData.getType().toLowerCase())) {
				// the SMS POP gateway is VODAFONEPOPSOAP
				provider =  new GatewaySmsVodafonePop(providerData.getLogPrefix(), providerData.getSmsLogLevel());
			} else {
				// sms gateways not configured in the properties file or
				// unknown SMS gateway
				XASException err = new XASException("Wrong or unknown SMS gateway configuration: check gatewaysms.properties file.");
				log.error(err.getMessage(), err);
				throw new XASException(PROPERTIES_EXCEPTION_CODE, err);
			}
				/*	DEPRECATED 2016.07.18
				   if (GatewaySmsVirtual.TYPE.toLowerCase().equals(providerData.getType().toLowerCase())) {
				// the SMS gateway is VIRTUAL
				provider = new GatewaySmsVirtual(providerData.getLogPrefix(), providerData.getSmsLogLevel());
			} else if (GatewaySmsAcotel.TYPE.toLowerCase().equals(providerData.getType().toLowerCase())) {
				// the SMS gateway is ACOTEL
				
				// Acotel pages array (url and phone number pattern RE)
				ArrayList pages = new ArrayList(); 
				for (int idx=0; ;++idx) {
					String pagePrefix = ".page["+idx+"]";
					
					String phonePattern = mProps.getProperty(providerData.getName() + pagePrefix +".phoneRE");
					String pageUrl      = mProps.getProperty(providerData.getName() + pagePrefix +".url");
					String pageDesc     = mProps.getProperty(providerData.getName() + pagePrefix +".desc", "<no description>");
					
					if ( pageUrl==null || phonePattern==null )	// no page with index 'idx'
						break; 	// stop iterating
					
					// Add to pages array
					try {
						AcotelPageDestination page = new AcotelPageDestination(phonePattern, pageUrl, pageDesc);
						pages.add( page );
					} catch(PatternSyntaxException ex) {
						log.error(ex); 
						//Discarded page destination
					}
				}
				// Convert pages ArrayList to fixed array of AcotelPageDestination objs
				AcotelPageDestination[] pageDestinations = new AcotelPageDestination[pages.size()];
				pageDestinations = (AcotelPageDestination[])pages.toArray( pageDestinations );
				
				// default url
				String defaultPageUrl = mProps.getProperty(providerData.getName() + ".url");
				
				provider = new GatewaySmsAcotel(providerData.getLogPrefix(), pageDestinations, defaultPageUrl, providerData.getSmsLogLevel());
			} else if (GatewaySmsVodafone.TYPE.toLowerCase().equals(providerData.getType().toLowerCase())) {
				// the SMS SMPP gateway is VODAFONE
				provider =  new GatewaySmsVodafone(providerData.getLogPrefix(), providerData.getSmsLogLevel());
			} else if (GatewaySmsMinipop.TYPE.toLowerCase().equals(providerData.getType().toLowerCase())) {
				// the SMS gateway is VODAFONE
				provider =  new GatewaySmsMinipop(providerData.getLogPrefix(), providerData.getSmsLogLevel());
			} else 
			 */
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			XASException err =	new XASException("Wrong SMS gateway configuration: check gatewaysms.properties file.");
			//log.error(err.getMessage(), err);
			throw new XASException(PROPERTIES_EXCEPTION_CODE, err);
		}
		
		providerData.setProvider(provider);

		// put the provider in the hastable
		mProviderList.put(providerData.getName(), providerData);
		
		return providerData;
	}

	/**
	 * Load the single sms provider configuration
	 * 
	 * @param providerName
	 * @return
	 * @throws XASException
	 */
	private ProviderData loadProvierConfig(String providerName) throws XASException
	{
		ProviderData providerData = new ProviderData();

		if(providerName==null)
			throw new XASException("null providerName");

		try
		{
			// commons SMSGateway properties
			providerData.setName(providerName);
			providerData.setType(mProps.getProperty(providerData.getName() + ".type"));
			providerData.setDescription(mProps.getProperty(providerData.getName() + ".description"));
			providerData.setLogPrefix(mProps.getProperty(providerData.getName() + ".logprefix"));
			providerData.setSmsLogLevel(mProps.getProperty(providerData.getName() + ".smsloglevel"));
						
			log.info("SmsGateway.name: " + providerData.getName());
			log.info(providerData.getName() + ".type: " + providerData.getType());
			log.info(providerData.getName() + ".description: " + providerData.getDescription());
			log.info(providerData.getName() + ".logprefix: " + providerData.getLogPrefix());
			log.info(providerData.getName() + ".smsloglevel: " + providerData.getSmsLogLevel());
			
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			XASException err =	new XASException("Wrong SMS gateway configuration: check gatewaysms.properties file.");
			//log.error(err.getMessage(), err);
			throw new XASException(PROPERTIES_EXCEPTION_CODE, err);
		}
		
		return providerData;
	}

	/**
	 * Load the properties file
	 *
	 * @return
	 * @throws XASException
	 */
	private Properties loadProperties() throws XASException
	{
		Properties props = null;

		try
		{
			// read the SMS Gateway properties from external properties file
			Context ctx = new InitialContext();
			URL u = (URL)ctx.lookup(JNDI_SMSGATEWAYPARAMETERS); 
			log.info("JNDI " + JNDI_SMSGATEWAYPARAMETERS + " = [" + u.toString() + "]");
			File file = new File(u.getFile());
			InputStream is = new FileInputStream(file);
			props = new Properties();
			props.load(is);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			XASException err =	new XASException("Wrong SMS gateway configuration: check gatewaysms.properties file.");
			//log.error(err.getMessage(), err);
			throw new XASException(PROPERTIES_EXCEPTION_CODE, err);
		}
		return props;
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
	 * 
	 * @throws Exception the exception
	 */
	private void updateCache() throws XASException
	{
		reset();
        // as LAST operation, update the new cache timeout
		ResetCacheTimeout();
	}

	/**
	 * Reset the expire period of cache.
	 * 
	 * @throws Exception the exception
	 */
	private void ResetCacheTimeout() throws XASException
	{
		this.mCacheInvalidationTime = System.currentTimeMillis() + mCacheTimeout;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date timeNow = new Date(System.currentTimeMillis());
        Date timeExpire = new Date(mCacheInvalidationTime);
		log.info("Set cache timeout to " + sdf.format(timeExpire) + ", now the time is " + sdf.format(timeNow));
	}
}
