package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import it.usi.xframe.system.bfutil.db.DBConnectionFactory;
import it.usi.xframe.system.ifutils.EnvironmentLoader;
import it.usi.xframe.system.ifutils.IEnvironment;
import it.usi.xframe.xas.bfimpl.SendEmail;
import it.usi.xframe.xas.bfimpl.TableManager;
import it.usi.xframe.xas.bfimpl.a2psms.IGatewayA2Psms;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.ENC_TYPE;
import it.usi.xframe.xas.bfutil.data.EmailMessage;
import it.usi.xframe.xas.util.json.XConstants;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class Configuration {
	private final static String TAG_VERSION 			= "Version2";
	
	private final static String TAG_CACHE_TIMEOUT		= "CacheTimeout";

	private final static String TAG_DATABASE			= "Database";
	private final static String TAG_DB_PRESENT			= "Present";
	private final static String TAG_DB_DR_RETENTION		= "DrRetentionPeriod"; // Delivery Report retention period

	private final static String TAG_NOTIFICATION_ADDRESS = "NotificationAddress";
	private final static String TAG_JMS_TIME_ZONE		= "JmsTimeZone";
	
	private final static String TAG_XASUSERS			= "XasUsers";
	private final static String ATT_XUS_DEF_USER_NAME	= "defaultXasUserName";
	private final static String ATT_XUS_DEF_PROVIDER	= "defaultProvider";
	private final static String ATT_XUS_DEF_PROVIDER_BK	= "defaultProviderBackup";
	private final static String ATT_XUS_DEF_REGION		= "defaultRegion";
	private final static String ATT_XUS_DEF_DR_TIMEOUT	= "defaultDRtimeOut";
	private final static String ATT_XUS_DEF_MO_TIMEOUT	= "defaultMOtimeOut";

	private final static String TAG_XASUSER					= "XasUser";
	private final static String ATT_XU_NAME			 		= "name";
	private final static String ATT_XU_INTERFACE			= "interface";
	private final static String ATT_XU_ORIGINATOR		 	= "originator";
	public  final static String ATT_XU_ORIGINATOR_SEPARATOR	= ":"; // REGEX
	private final static String ATT_XU_PROVIDER		 		= "provider";
	private final static String ATT_XU_DEF_FORCEASCII 		= "defaultForceAsciiEncoding";
	private final static String ATT_XU_DEF_VALIDITYP 		= "defaultValidityPeriod";
	private final static String ATT_XU_DEF_REGION	 		= "defaultRegion";
	private final static String ATT_XU_USE_BACKUP	 		= "useBackup";
	private final static String ATT_XU_PHONENUM_REGEX		= "validatePhoneNumberRegEx";
	private final static String ATT_XU_END_POINT			= "endPoint";		//#TIM
	private final static String ATT_XU_DELIVERY_REPORT		= "deliveryReport"; //#TIM
	private final static String ATT_XU_MO_DESTINATOR		= "moDestinator"; //#TIM
	public  final static String ATT_XU_MO_DESTINATOR_SEPARATOR	= ":"; // REGEX
	private final static String ATT_XU_MO_ENCRYPTION		= "moEncryption"; //#TIM
	private final static String ATT_XU_MO_TIMEOUT			= "moTimeOut"; //#TIM
	private final static String ATT_XU_DR_TIMEOUT			= "drTimeOut"; //#TIM
	private final static String ATT_XU_PROVIDER_WORKLOAD	= "providerWorkloadPct";
	

	public  final static int 	PROVIDER_WORKLOAD_STEP		= 5; // 1 - 100 default = 5% means 20 steps

	private final static String TAG_REPLACE_MAP				= "ReplaceMap";
	private final static String TAG_SERVICE					= "Service";
	private final static String ATT_RM_CLASS			 	= "class";
	private final static String ATT_RM_PROTOCOL_ID		 	= "protocolId";

	
	private final static String TAG_PROVIDERS 			= "Providers";
	private final static String TAG_PROVIDER				= "Provider";
	private final static String ATT_PROV_NAME			 	= "name";
	private final static String ATT_PROV_TYPE				= "type";
	private final static String ATT_PROV_MAXSMSALLOWED		= "maxSmsAllowed";
	private final static String ATT_PROV_TIMEOUT			= "timeOut";

	private final static String TAG_ORIGINATORS				= "Originators";
	private final static String TAG_ORIGINATOR					= "Originator";
	private final static String ATT_ORIG_NAME				 	= "name";
	private final static String ATT_ORIG_ROUTER	 				= "routerRegex";
	private final static String ATT_ORIG_TYPEOFNUM				= "typeOfNumber";
	private final static String ATT_ORIG_NUMPLANID	 			= "numPlanId";
	private final static String ATT_ORIG_ADDRESS	 			= "address";
	private final static String ATT_ORIG_ALIAS				 	= "alias";

	private final static String TAG_CUSTOMIZATION			= "Customization";

	private final static String TAG_ENVIRONMENTS 			= "Environments";
	private final static String TAG_ENVIRONMENT					= "Environment";
	private final static String ATT_ENV_NAME_REGEX				 	= "nameRegEx";
	
	private final static String TAG_SECURITY						= "Security";
	private final static String TAG_APPLICATION_GATEWAY					= "ApplicationGateway";
	private final static String TAG_ACCOUNT								= "Account";
	private final static String ATT_ACC_PROVIDER						 	= "provider";
	// Used for Account and for ApplicationGateway
	private final static String ATT_ACC_USER								= "user";		// Used for Account and for ApplicationGateway
	private final static String ATT_ACC_PASSWORD							= "password";   // Used for Account and for ApplicationGateway

	private final static String TAG_APPLICATION						= "Application";
	
	
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	private static Configuration instance;

	public static final String JNDI_SMS_CONFIG = "java:comp/env/url/smsconfig";
	protected static final String XASUSER_MO_PREFIX = "MO:";	//#TIM

	private XasUsers xasUsers = new XasUsers();
	private HashMap providers = new HashMap();
	private boolean isBackupOn = false;
	private HashMap xasUserCounter = new HashMap();
	/** is Configuration singleton inizialized? */
	private boolean configInitialized = false;
	private long configLastModified = 0;
	
	/** millisec for cache timeout, default = 1 hour. */
	private long DEFAULT_CACHE_INTERVAL_TIMEOUT = 60*60*1000;		
	//private long DEFAULT_CACHE_INTERVAL_TIMEOUT = 60*1000;		
	/** cache invalidation timeout in millisec. */
	private long configCacheTimeout = DEFAULT_CACHE_INTERVAL_TIMEOUT;	
	/** time for cache invalidation in millisec. */
	private long configInvalidationTime = 0;		
	/** Database feature present. */
	private boolean databasePresent = false;
	/** Retention period for delivery report in table. */
	private long drRetentionPeriod = 2; // Days		
	
	private String applicationGWuser = null;
	private String applicationGWpassword = null;
	private String emailNotification = null;
	
	private TimeZone jmsTimeZone = TimeZone.getTimeZone("UCT");
 	/**
	 * Singleton getInstance.
	 * @return Configuration.
	 * @throws XASException
	 */
 	public static synchronized Configuration getInstance() throws XASException {
		if (instance == null) instance = new Configuration();
		else instance.checkCacheValidity();
		return instance;
	}
 	
 	/**
 	 * Singleton private constructor.
 	 * @throws XASException
 	 */
 	private Configuration() throws XASException {
		resetCacheTimeout();
		configInitialize(false, null); // Load and setup the configuration
	}

	/**
	 * Reset the expiration period of cache.
	 */
	private synchronized void resetCacheTimeout() {
		this.configInvalidationTime = System.currentTimeMillis() + configCacheTimeout;

        Date timeNow = new Date(System.currentTimeMillis());
        Date timeExpire = new Date(configInvalidationTime);
		if (logger.isDebugEnabled()) logger.debug("Configuration Set expiration cache timeout to " + Constants.ISO_DATE_FORMAT.format(timeExpire) + ", now the time is " + Constants.ISO_DATE_FORMAT.format(timeNow));
	}

	/** 
	 * Check cache validity and initialize.
	 * @throws XASException
	 */
	private synchronized void checkCacheValidity() throws XASException {
		if (! isCacheValid()) {
			xasUsers = new XasUsers();
			providers = new HashMap();
			configInitialized = false;
			resetCacheTimeout(); // as LAST operation, update the new cache timeout
		}
		if (! configInitialized) {
			configInitialize(true, null); // Load and setup the configuration
 		}
	}
	/**
	 * Check cache validity. 
	 *  
	 * @return true if cache is valid or false if filechanged and cache invalidation time timedout .
	 * @throws XASException 
	 */
	private synchronized boolean isCacheValid() throws XASException {
		boolean configValidity = true;
		if (this.configInvalidationTime < System.currentTimeMillis()) {
			resetCacheTimeout();
			configValidity = configLastModified == getConfigLastModified();
 		} else {

		}
		return configValidity;
	}
	
	/**
	 * Set cache timeout value.
	 * @param value
	 * @throws XASException
	 */
	private synchronized void setCacheTimeout(String value, String name) throws XASException {
		try {
			int seconds = Integer.parseInt(value);
			this.configCacheTimeout = seconds * 1000;
			if (logger.isDebugEnabled()) logger.debug("Configuration Set cache timeout to " + seconds + " seconds ");
		} catch(NumberFormatException e) {
			throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, name});
			
		}
	}

 
	public synchronized TimeZone getJmsTimeZone() {
		return jmsTimeZone;
	}
	
	public synchronized void setJmsTimeZone(String jmsTimeZone) {
		this.jmsTimeZone = TimeZone.getTimeZone(jmsTimeZone);
	}
	
	private synchronized void configInitialize(boolean sendNotification, String fileName) throws XASException {
 		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), Constants.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "initialize");
 		// Load the configuration file
 		try {
 			load(sendNotification, fileName);
 		} catch (XASException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
	    	logger.error(errors.toString());
			logger.error(sl.logIt(SmartLog.K_STATUS_CODE, e.getCode(), SmartLog.K_STATUS_MSG, MessageFormat.format(e.getMessage(), e.getParameters())).getLogRow(true)); // Debug and keep row
	    	throw e;
 		}
 		
		 try {
			 String domain = java.net.InetAddress.getLocalHost().getCanonicalHostName();
            sl.logIt("a_domain", domain);
		 } catch (UnknownHostException e) {
		 }
 		
 		if (isDatabasePresent()) {
			try {
				Connection connection = DBConnectionFactory.getInstance().retrieveConnection(Constants.DB_JNDI);
				try {
					sl.logIt("a_drRetentionPeriod", Long.toString(getDrRetentionPeriod()));
					TableManager.cleanupDeliveryReport(connection, getDrRetentionPeriod());
				} finally {
					connection.close();
				}
			} catch (Exception e) { // Un-handled exceptions logger.error
		    	StringWriter errors = new StringWriter();
		    	e.printStackTrace(new PrintWriter(errors));
		    	logger.error(errors.toString());
				logger.error(sl.logIt(SmartLog.K_STATUS_CODE, Constants.XAS00099E_CODE, SmartLog.K_STATUS_MSG, MessageFormat.format(Constants.XAS00099E_MESSAGE, new String[]{e.getMessage()}), SmartLog.K_PARAMS, errors.toString()).getLogRow(true)); // Debug and keep row
		 	}
 		}
 		logger.debug(sl.getLogRow());
	}

 	/**
	 * Load the Sms configuration file.
	 * 
	 * @throws XASException
	 */
	private synchronized void load(boolean sendNotification, String fileName) throws XASException {
		String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();

		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD,"load",SmartLog.K_PHASE, SmartLog.V_PHASE_INIT);

		// Boolean values for configuration check 
		Boolean bAppGtwAccountRequired = new Boolean(false);
		Boolean bTestProviderOriginator = new Boolean((fileName != null));
		fileName = (fileName == null) ? getConfigFileName() : fileName;
		configLastModified = getConfigLastModified();
		
		if (logger.isDebugEnabled()) { 
	        sl.preset("myPreset").logIt(Constants.K_CONFIG_FILE, fileName, "a_lastmodified", Constants.ISO_DATE_FORMAT.format(new Date(configLastModified)));
			logger.debug(sl.getLogRow()); 
			sl.reload("myPreset");
		}
		
		XMLConfiguration config = new XMLConfiguration();
		// WARNING COMMA MUST BE ESCAPED DUE TO apache common XMLConfiguration list separated values 
		// checkout the XML file.
		// config.setDelimiterParsingDisabled(true);
		// config.setAttributeSplittingDisabled(true);

        try {
            config.load(fileName); // Load the configuration via apache.commons.configuration
        } catch (ConfigurationException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new XASException(Constants.XAS00024E_MESSAGE, null, Constants.XAS00024E_CODE, new Object[] {e.getMessage()});
			
        } 

        loadBaseConfiguration(config, sl);

		// Initialize new XasUsers map;
		xasUsers = new XasUsers();

        // List of the providers requested for DeliveryReport and MobileOriginated service.
		HashMap providerOriginatorList = new HashMap(); // Provider Originator list referenced in xasUsers configuration required to be defined in providers configuration
		HashMap drProviderList = new HashMap(); // DeliveryReport
		HashMap moProviderList = new HashMap(); // MobileOriginated
		HashMap rmProviderList = new HashMap(); // ReplaceMap
		
		loadXasUsers(config, providerOriginatorList, drProviderList, moProviderList, rmProviderList,
				bTestProviderOriginator, bAppGtwAccountRequired, sl);



		providers = loadProviders(config, providerOriginatorList, drProviderList, moProviderList, rmProviderList,
											 bTestProviderOriginator, sl);

		// logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_LOOP_END).getLogRow(true));
		if (bTestProviderOriginator.booleanValue() && !providerOriginatorList.isEmpty())
			throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {providerOriginatorList.toString(), "missing Provider:Originator for xasUsers"});

		
		loadEnvironments(config, bAppGtwAccountRequired, sl);
		
 		configInitialized = true; // Set the singleton initialization to true
		if (sendNotification && emailNotification != null) sendNotification(emailNotification); // Send email notification for cache update
		
		if (logger.isDebugEnabled()) { 
			sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG); 
			/* Use XStream for logging params as JSON */
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, "a_myconfiguration", XConstants.XSTREAMER.toXML(this)).getLogRow(true));
		}

	}

	public synchronized void testLoad(String fileName) throws XASException {
		xasUsers = new XasUsers();
		providers = new HashMap();
		configInitialized = false;
		configInitialize(false, fileName);
	}
	
	/**
	 * Get providers configuration.
	 * 
	 * @return xasUsers.
	 * @throws XASException 
	 */
	public synchronized Provider getProvider(XasUser xasUser) throws XASException {
		checkCacheValidity(); // synchronized  
		String providerName = xasUser.getProvider();

		if (xasUser.getProviderWorkload() != null && !workloadProvider(xasUser)) { // Workload with providerWorkload keyword otherwise use defaultProvider
			providerName = xasUsers.getDefaultProvider();
		}

		if (isBackupOn && xasUser.getUseBackup()) {
			providerName = xasUsers.getDefaultProviderBackup();
		}
		Provider provider = (Provider) providers.get(providerName);
		if (provider == null)
			throw new XASException(Constants.XAS00017E_MESSAGE, null, Constants.XAS00017E_CODE, new Object[] {providerName, xasUser.getName()});
		else {
			try {
				provider = (Provider) provider.clone();
	        } catch (CloneNotSupportedException e) {
				e.printStackTrace(System.out);
				String myUUID = (String) MDC.get(Constants.MY_UUID_KEY);
				if (myUUID == null) myUUID = UUID.randomUUID().toString();
				SmartLog slException = new SmartLog(SmartLog.COUPLING_LOOSE_I)
	        		.logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
	        		.logIt(SmartLog.K_METHOD, "getProvider"); 
				slException.logIt(SmartLog.K_SCOPE, SmartLog.V_SCOPE_DEBUG);
				slException.logIt(SmartLog.K_STATUS_MSG, e.getMessage());
				logger.error(slException.getLogRow());
	        }
		}
		return provider;
	}

	/**
	 * Handle the provider workload.
	 * 
	 * @param xasUser
	 * @return true if must use the provider defined in the xasUser
	 */
	public synchronized boolean workloadProvider(XasUser xasUser) {
		// Retrieve xasUser counter and increment
		Integer counter = (Integer) xasUserCounter.get(xasUser.getName());
		if (counter == null) counter = new Integer(0);
		int intCount = counter.intValue() + 1;
		counter = new Integer(intCount > (100 / Configuration.PROVIDER_WORKLOAD_STEP) ? 1 : intCount);
		// Store xasUser counter
		xasUserCounter.put(xasUser.getName(), counter);
		boolean useProvider = counter.compareTo(xasUser.getProviderWorkload()) <= 0;
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, Configuration.class.getName(), "", SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD,"workloadProvider"
					, "a_xasUserName", xasUser.getName()
					, "a_workloadProvider", counter + "/" + (100 / Configuration.PROVIDER_WORKLOAD_STEP) + ":" + xasUser.getProviderWorkload()
					, "a_useProvider", new Boolean(useProvider).toString());
		logger.debug(sl.getLogRow());
		return useProvider;
	}

	/**
	 * Get final XasUser for Mobile Terminated message.
	 * @param xasUserName
	 * @param useDefault
	 * @return
	 * @throws XASException
	 */
	public synchronized XasUser getFinalXasUser(String uuid, String xasUserName, boolean useDefault) throws XASException {
		checkCacheValidity(); // synchronized  
		return xasUsers.getFinalXasUser(uuid, xasUserName != null ? xasUserName.toUpperCase() : null, useDefault ? xasUsers.getDefaultXasUserName() : null);
	}
	/**
	 * Get final XasUser for Mobile Originated message.
	 * @param moDestinator
	 * @return
	 * @throws XASException
	 */
	public synchronized XasUser getFinalXasUser(String moDestinator) throws XASException { //#TIM
	    return xasUsers.getFinalXasUser(moDestinator);
    }

	/**
	 * Get configuration file name.
	 * @return config file name.
	 * @throws XASException for JNDI lookup problem.
	 */
	public static String getConfigFileName() throws XASException {
		URL fileURL = null;
		try {
			Context ctx = new InitialContext();
			fileURL = (URL)ctx.lookup(JNDI_SMS_CONFIG); 
		} catch(NamingException e) {
        	StringWriter errors = new StringWriter();
        	e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new XASException(Constants.XAS00023E_MESSAGE, null, Constants.XAS00023E_CODE, new Object[] {e.getMessage()});
		}
		return fileURL.getFile();
	}
	
	/**
	 * Get last modified time for configuration file.
	 * @return configuration time.
	 * @throws XASException
	 */
	private synchronized static long getConfigLastModified() throws XASException {
		File file = new File(getConfigFileName());
		long newLastModified = -1L;
		if (file.exists() && file.isFile())
			newLastModified = file.lastModified();
		else 
			newLastModified = -1L;
		return newLastModified;
	}
	
	/**
	 * Send email notification, if defined, of configuration changes.
	 * @param eMailTo
	 */
	private synchronized void sendNotification(String eMailTo) {
		EmailMessage emailMsg = new EmailMessage();
		emailMsg.setMailSubject("Configuration updated at " + Constants.ISO_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + " loaded file timestamp " + Constants.ISO_DATE_FORMAT.format(new Date(configLastModified)));
		emailMsg.setMailTo(eMailTo);
		emailMsg.setMailFrom(Constants.XAS_MAIL_SENDER);
		String configFileName = "Error reading config file name";
		try {
			configFileName = getConfigFileName();
        } catch (XASException e1) {
	        // Ignore exception
        }
        emailMsg.setMailMessage("ConfigFileName=" + configFileName);
		SendEmail email = SendEmail.getInstance();
		try {
	        email.sendMessage(emailMsg, null, "XA-XAS", true);
        } catch (XASException e) {
	        // Ignore exception
        }

	}
	
	public synchronized boolean isDatabasePresent() {
		return this.databasePresent;
	}
	public synchronized long getDrRetentionPeriod() {
		return this.drRetentionPeriod;
	}

	static final public String PROVIDER_USERNAME = "username"; 
	static final public String PROVIDER_PASSWORD = "password"; 
	static final 		String SERVICE_UT_PROFILE = "service."; // XA-XAS-BF\ejbModule\META-INF\webserviceclient.xml\Handlers\Initial parameters\UTProfile 

	public synchronized HashMap getProviders() {
		return providers;
	}
	public synchronized HashMap getProviderAccounts() throws XASException {
		HashMap providerAccounts = new HashMap();
		Iterator iterator = getProviders().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			Provider provider = (Provider) entry.getValue();
			if (provider.getCustomization() instanceof CustomizationBasic) {
				Properties properties = new Properties();
				properties.setProperty(PROVIDER_USERNAME, ((CustomizationBasic)provider.getCustomization()).getUser());
				properties.setProperty(PROVIDER_PASSWORD, ((CustomizationBasic)provider.getCustomization()).getDecryptedPassword());
				providerAccounts.put(SERVICE_UT_PROFILE + provider.getType(), properties);
			}
		}
	    return providerAccounts;
    }


	public synchronized String getApplicationGWpassword() {
	    return applicationGWpassword;
    }

	public synchronized String getApplicationGWuser() {
    	return applicationGWuser;
    }

	public synchronized String getEmailNotification() {
    	return emailNotification;
    }

	protected void loadBaseConfiguration(XMLConfiguration config,  
			SmartLog sl
			) throws XASException {
		String path, value;
	
		path = TAG_VERSION + "." + TAG_CACHE_TIMEOUT;  
		value = config.getString(path); if (value != null) this.setCacheTimeout(value, path);
	    // logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
		resetCacheTimeout();
	
		path = TAG_VERSION + "." + TAG_DATABASE + "." + TAG_DB_PRESENT;
		value = config.getString(path); if (value != null) {
			this.databasePresent = Boolean.valueOf(value).booleanValue();
			if (logger.isDebugEnabled()) logger.debug("Database Present: " + this.databasePresent);
		}
	
		path = TAG_VERSION + "." + TAG_DATABASE + "." + TAG_DB_DR_RETENTION;  
		value = config.getString(path); if (value != null) {
			try {
				this.drRetentionPeriod = new Long(value).longValue();
			} catch (NumberFormatException e) {
	        	StringWriter errors = new StringWriter();
	        	e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
			}
		}
	
		path = TAG_VERSION + "." + TAG_NOTIFICATION_ADDRESS;  
		emailNotification = config.getString(path); 
	    // logger.debug(sl.logIt("a_step", path, "a_value", emailNotification).getLogRow(true));
	
		path = TAG_VERSION + "." + TAG_JMS_TIME_ZONE;  
		value = config.getString(path); if (value != null) this.setJmsTimeZone(value);
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
	}
	protected void loadXasUsers(XMLConfiguration config,  
			HashMap providerOriginatorList, HashMap drProviderList, HashMap moProviderList, HashMap rmProviderList,
			Boolean bTestProviderOriginator, Boolean bAppGtwAccountRequired,
			SmartLog sl
			) throws XASException {
		String path, value;
		HashMap replaceList = new HashMap(); // Originator --> String[] to check replaceClass assignment

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_USER_NAME + "]";    
		value = config.getString(path);	
		if (value != null) xasUsers.setDefaultXasUserName(value.toUpperCase());
		else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_REGION + "]";    
		value = config.getString(path);
		if (value != null) xasUsers.setDefaultRegion(value);
		else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_PROVIDER + "]";    
		value = config.getString(path);	
		if (value != null) xasUsers.setDefaultProvider(value);
		else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_PROVIDER_BK + "]";
		value = config.getString(path);	if (value != null) xasUsers.setDefaultProviderBackup(value);
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_DR_TIMEOUT + "]";    
		value = config.getString(path);	if (value != null) {
			try {
				xasUsers.setDefaultDRtimeOut(new Integer(value));
			} catch (NumberFormatException e) {
	        	StringWriter errors = new StringWriter();
	        	e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
			}
		}
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

		path = TAG_VERSION + "." + TAG_XASUSERS + "[@" + ATT_XUS_DEF_MO_TIMEOUT + "]";    
		value = config.getString(path);	if (value != null) {
			try {
				xasUsers.setDefaultMOtimeOut(new Integer(value));
			} catch (NumberFormatException e) {
	        	StringWriter errors = new StringWriter();
	        	e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
			}
		}
		// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

 		String xasUserPath = TAG_VERSION + "." + TAG_XASUSERS + "." + TAG_XASUSER + "[@" + ATT_XU_NAME + "]";

		String[] xasUsersArray = config.getStringArray(xasUserPath);
		
		
        sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_LOOP_ITERATE);
		for (int xi = 0; xi < xasUsersArray.length; xi++) {
			// logger.debug(sl.logIt("a_step", xasUserPath, "a_value", xasUsersArray[xi]).getLogRow(true));
			XasUser xasUser = new XasUser();
			
			String xasUserPrefix = TAG_VERSION + "." + TAG_XASUSERS + "." + TAG_XASUSER  + "(" + xi + ")"; 

			path = xasUserPrefix + "[@" + ATT_XU_NAME + "]";
			value = config.getString(path);	
			if (value != null) { xasUser.setName(value.toUpperCase()); }
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});

			path = xasUserPrefix + "[@" + ATT_XU_INTERFACE + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					xasUser.setInterfaceVersion(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
				}
			}
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});

			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = xasUserPrefix + "[@" + ATT_XU_ORIGINATOR + "]";
			value = config.getString(path);	
			if (value != null) { xasUser.setOriginator(value); } 
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			
			String providerOriginator = null;
			boolean providerDefined = false;
			path = xasUserPrefix + "[@" + ATT_XU_PROVIDER + "]";
			value = config.getString(path);	
			if (value != null) {
				xasUser.setProvider(value); 
				providerOriginator = xasUser.getProvider();
				providerDefined = true;
			} else {
				providerOriginator = xasUsers.getDefaultProvider(); 
			}
			if (bTestProviderOriginator.booleanValue()) { // Check the list of provider:originator1, provider:originator2, provider:originator3
				String[] originatorArray = xasUser.getOriginator().split(ATT_XU_ORIGINATOR_SEPARATOR);
				for (int oi = 0; oi < originatorArray.length; oi++) {
 					String entry = (String) providerOriginatorList.get(providerOriginator + ":" + originatorArray[oi]);
					entry = xasUser.getName() + ((entry) == null ? "" : "," + entry); 
					providerOriginatorList.put(providerOriginator + ":" + originatorArray[oi], entry);
				}
 			}
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = xasUserPrefix + "[@" + ATT_XU_PROVIDER_WORKLOAD + "]";
			value = config.getString(path);	
			if (value != null) { 
				logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
				try {
					if (!providerDefined) {  // Workload defined without provider
						throw new XASException(Constants.XAS00032E_MESSAGE, null, Constants.XAS00032E_CODE, new Object[] {path});
					}
					int providerWorkload = Integer.parseInt(value) / PROVIDER_WORKLOAD_STEP;
					xasUser.setProviderWorkload(new Integer(providerWorkload));
					logger.debug(sl.logIt("a_step", path, "a_value", xasUser.getProviderWorkload().toString()).getLogRow(true));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
				}
			}
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			
			
			path = xasUserPrefix + "[@" + ATT_XU_DEF_FORCEASCII + "]";
			value = config.getString(path);	if (value != null) { xasUser.setDefaultForceAsciiEncoding(Boolean.valueOf(value).booleanValue()); }
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

 			path = xasUserPrefix + "[@" + ATT_XU_DEF_VALIDITYP + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					xasUser.setDefaultValidityPeriod(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
				}
			}
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = xasUserPrefix + "[@" + ATT_XU_DEF_REGION + "]";
			value = config.getString(path);	if (value != null) { xasUser.setDefaultRegion(value); } 
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			
			path = xasUserPrefix + "[@" + ATT_XU_USE_BACKUP + "]";
			value = config.getString(path);	if (value != null) { xasUser.setUseBackup(Boolean.valueOf(value).booleanValue()); }
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			
			path = xasUserPrefix + "[@" + ATT_XU_PHONENUM_REGEX + "]";
			value = config.getString(path);	if (value != null) {
		    	try {
					xasUser.setValidatePhoneNumberRegEx(Pattern.compile(value)); 
		    	} catch (PatternSyntaxException ex)	{
		        	StringWriter errors = new StringWriter();
		        	ex.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new String[] {value, path} );
				}
			}
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = xasUserPrefix + "[@" + ATT_XU_DELIVERY_REPORT + "]";										//#TIM
			value = config.getString(path);	if (value != null) {
				boolean drFeature = Boolean.valueOf(value).booleanValue();
				xasUser.setDeliveryReport(Boolean.valueOf(value).booleanValue());
				if (drFeature && !this.databasePresent) {
					throw new XASException(Constants.XAS00034E_MESSAGE, null, Constants.XAS00034E_CODE, new Object[] {"delivery report", path});		
					
				}
				} //#TIM
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true)); //#TIM

			path = xasUserPrefix + "[@" + ATT_XU_MO_DESTINATOR + "]";									//#TIM
			value = config.getString(path);	if (value != null) { xasUser.setMoDestinators(value.split(ATT_XU_MO_DESTINATOR_SEPARATOR)); } 		//#TIM
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));				//#TIM

			String moPath = path;
			path = xasUserPrefix + "[@" + ATT_XU_MO_ENCRYPTION + "]";									//#TIM
			value = config.getString(path);	if (value != null) {
					if (xasUser.getMoDestinators() == null) {
						throw new XASException(Constants.XAS00033E_MESSAGE, null, Constants.XAS00033E_CODE, new Object[] {path, moPath});		
					}
					try {
						xasUser.setMoEncryption(ENC_TYPE.fromString(value));
					} catch (IllegalStateException e) {
			        	StringWriter errors = new StringWriter();
			        	e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
						throw new XASException(Constants.XAS00035E_MESSAGE, null, Constants.XAS00035E_CODE, new Object[] {value, path, ENC_TYPE.getAllowedValues()});
					}
				} 		//#TIM
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));				//#TIM

 			path = xasUserPrefix + "[@" + ATT_XU_MO_TIMEOUT + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					xasUser.setMOtimeOut(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
				}
			} else {
				xasUser.setMOtimeOut(xasUsers.getDefaultMOtimeOut());
			}

 			path = xasUserPrefix + "[@" + ATT_XU_DR_TIMEOUT + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					xasUser.setDRtimeOut(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
				}
			} else {
				xasUser.setDRtimeOut(xasUsers.getDefaultDRtimeOut());
			}

			path = xasUserPrefix + "[@" + ATT_XU_END_POINT + "]";									//#TIM
			value = config.getString(path);	if (value != null) { xasUser.setEndPoint(value); } 		//#TIM
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));			//#TIM
			// If DR or MO requested the endPoint is required										//#TIM
			if (value == null) {
				if (xasUser.isDeliveryReport() || (xasUser.getMoDestinators() != null && xasUser.getMoDestinators().length != 0 ) ) { 	//#TIM
					throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});		//#TIM
				}
			} else {
				bAppGtwAccountRequired = new Boolean(true); // If present an endPoint we need an account 
				if (xasUser.getMoDestinators() != null && xasUser.getMoDestinators().length != 0 ) {
//					logger.debug("check xasUser.getMoDestinator()" + xasUser.getMoDestinator());
					// Register the moDestinator as a XasUser if not already present
					String[] moDestinators = xasUser.getMoDestinators();
					for (int mo = 0; mo < moDestinators.length; mo++) {
						XasUser existsMoDestinator = (XasUser) xasUsers.get(XASUSER_MO_PREFIX + moDestinators[mo]); 
						if (existsMoDestinator != null) {
							throw new XASException(Constants.XAS00029E_MESSAGE, null, Constants.XAS00029E_CODE, new Object[] {path, xasUser.getName(), moDestinators[mo], existsMoDestinator.getName()});		//#TIM
						}
						xasUsers.put(XASUSER_MO_PREFIX + moDestinators[mo], xasUser);	//#TIM
					}
				} else if (!xasUser.isDeliveryReport()) {
					throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path + ":" + ATT_XU_MO_DESTINATOR + " or " + ATT_XU_DELIVERY_REPORT});		//#TIM
				}
			}
			
	 		String replaceMapPath = xasUserPrefix + "." + TAG_REPLACE_MAP + "." + TAG_SERVICE + "[@" + ATT_RM_CLASS + "]";
			String[] replaceMapArray = config.getStringArray(replaceMapPath);

			HashMap replaceMap = new HashMap();
			for (int ri = 0; ri < replaceMapArray.length; ri++) {
				String replaceClass = null;
		 		String replaceMapPrefix = xasUserPrefix + "." + TAG_REPLACE_MAP + "." + TAG_SERVICE + "(" + ri + ")";

		 		path = replaceMapPrefix + "[@" + ATT_RM_CLASS + "]";
				value = config.getString(path);	
				if (value != null) { replaceClass = value.toUpperCase(); }
				else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});

		 		path = replaceMapPrefix + "[@" + ATT_RM_PROTOCOL_ID + "]"; // Valid values 1 to 7
				value = config.getString(path);	
				if (value != null) {
					Integer protocolId = null;
					try {
						protocolId = new Integer(value);
					} catch (NumberFormatException e) {
			        	StringWriter errors = new StringWriter();
			        	e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
						throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
					}

					int protocolIndex = protocolId.intValue();
					if (protocolIndex < 1 || protocolIndex > 7) {
						throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
					}

					String[] rl = (String[]) replaceList.get(xasUser.getOriginator());
					if (rl == null) {
						rl= new String[7];
						replaceList.put(xasUser.getOriginator(), rl);
					}
					if (rl[protocolIndex - 1] == null) { 
						rl[protocolIndex - 1] = replaceMapPrefix;
						replaceMap.put(replaceClass, protocolId);
					} else throw new XASException(Constants.XAS00030E_MESSAGE, null, Constants.XAS00030E_CODE, new Object[] {replaceMapPrefix, rl[protocolIndex - 1], xasUser.getOriginator()});
				} else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
			}
			
			if (!replaceMap.isEmpty()) {
				xasUser.setReplaceMap(replaceMap);
			}
			
			xasUsers.put(xasUser.getName(), xasUser);

			// *** WARNING xasUser.getProvider for default provider is setted up by xasUsers.put, the following lines MUST follow the xasUsers.put statement ***
			if (xasUser.isDeliveryReport()) {
				// Add the xasUser.name to the list of the provider for DeliveryReport service
				String nameList = (String) drProviderList.get(xasUser.getProvider());
				drProviderList.put(xasUser.getProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
				if (xasUser.getProviderWorkload() != null && xasUsers.getDefaultProvider() != null) { 
					// If xasUser use either defined provider and defaultProvider, add defaultProvider to the xxProviderList
					nameList = (String) drProviderList.get(xasUsers.getDefaultProvider());
					drProviderList.put(xasUsers.getDefaultProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
					
				}
			}
			if (xasUser.getMoDestinators() != null && xasUser.getMoDestinators().length != 0) {
				// Add the xasUser.name to the list of the provider for MobileOriginated service
				String nameList = (String) moProviderList.get(xasUser.getProvider());
				moProviderList.put(xasUser.getProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
				if (xasUser.getProviderWorkload() != null && xasUsers.getDefaultProvider() != null) { 
					// If xasUser use either defined provider and defaultProvider, add defaultProvider to the xxProviderList
					nameList = (String) moProviderList.get(xasUsers.getDefaultProvider());
					moProviderList.put(xasUsers.getDefaultProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
					
				}
			}
			if (xasUser.getReplaceMap() != null) {
				// Add the xasUser.name to the list of the provider for ReplaceMap service
				String nameList = (String) rmProviderList.get(xasUser.getProvider());
				rmProviderList.put(xasUser.getProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
				if (xasUser.getProviderWorkload() != null && xasUsers.getDefaultProvider() != null) { 
					// If xasUser use either defined provider and defaultProvider, add defaultProvider to the xxProviderList
					nameList = (String) rmProviderList.get(xasUsers.getDefaultProvider());
					rmProviderList.put(xasUsers.getDefaultProvider(), xasUser.getName() + (nameList != null ? ", " + nameList : ""));
					
				}
			}
			// *** WARNING end
		}
		
	}
	
	protected HashMap loadProviders(XMLConfiguration config,  
			HashMap providerOriginatorList, HashMap drProviderList, HashMap moProviderList, HashMap rmProviderList,
			Boolean bTestProviderOriginator, 
			SmartLog sl
			) throws XASException {
		String path, value;
		HashMap newProviders = new HashMap();

		//		logger.debug(sl.logIt("a_step", "providerlist", "a_drList", drProviderList.toString(), "a_moList", moProviderList.toString(), "a_rmList", rmProviderList.toString()).getLogRow(true));
		String providerPath = TAG_VERSION + "." + TAG_PROVIDERS + "." + TAG_PROVIDER + "[@" + ATT_PROV_NAME + "]";
		String[] providersArray = config.getStringArray(providerPath);
		for (int pi = 0; pi < providersArray.length; pi++) {
			// logger.debug(sl.logIt("a_step", providerPath, "a_value", providersArray[pi]).getLogRow(true));
			Provider provider = new Provider();
			
			String providerPrefix = TAG_VERSION + "." + TAG_PROVIDERS + "." + TAG_PROVIDER  + "(" + pi + ")"; 

			path = providerPrefix + "[@" + ATT_PROV_NAME + "]";
			value = config.getString(path);	
			if (value != null) { provider.setName(value); } 
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = providerPrefix + "[@" + ATT_PROV_TYPE + "]";
			value = config.getString(path);	
			if (value != null) { provider.setType(value); } 
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
			// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

			path = providerPrefix + "[@" + ATT_PROV_MAXSMSALLOWED + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					provider.setMaxSmsAllowed(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
					
				}
			}

			path = providerPrefix + "[@" + ATT_PROV_TIMEOUT + "]";
			value = config.getString(path);
			if (value != null) {
				try {
					provider.setTimeOut(new Integer(value));
				} catch (NumberFormatException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
					throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
					
				}
			}
// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			
			String originatorPath = providerPrefix + "." + TAG_ORIGINATORS + "." + TAG_ORIGINATOR + "[@" + ATT_ORIG_NAME + "]";
			String[] originatorsArray = config.getStringArray(originatorPath);
			HashMap originators = new HashMap();
			HashMap originatorRouters = new HashMap();
			for (int oi = 0; oi < originatorsArray.length; oi++) {
				// logger.debug(sl.logIt("a_step", originatorPath, "a_value", originatorsArray[oi]).getLogRow(true));
				Originator originator = new Originator();
				
				String originatorPrefix = providerPrefix + "." + TAG_ORIGINATORS + "." + TAG_ORIGINATOR  + "(" + oi + ")"; 

				path = originatorPrefix + "[@" + ATT_ORIG_NAME + "]";
				value = config.getString(path);	
				if (value != null) { 
					originator.setName(value);
					if (bTestProviderOriginator.booleanValue()) providerOriginatorList.remove(provider.getName() + ":" + originator.getName());
					} 
				else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

				path = originatorPrefix + "[@" + ATT_ORIG_ROUTER + "]";
				value = config.getString(path);	
				if (value != null) {
	    	    	try {
	    	    		originator.setRouterRegex(Pattern.compile(value)); 
	    	    	} catch (PatternSyntaxException ex)	{
	    	        	StringWriter errors = new StringWriter();
	    	        	ex.printStackTrace(new PrintWriter(errors));
	    				logger.error(errors.toString());
						throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new String[] {value, path} );
					}
				}
				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
				
				path = originatorPrefix + "[@" + ATT_ORIG_TYPEOFNUM + "]";
				value = config.getString(path);
				if (value != null) {
					try {
						originator.setTypeOfNumber(new Integer(value));
					} catch (NumberFormatException e) {
			        	StringWriter errors = new StringWriter();
			        	e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
						throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
						
					}
				} else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});

				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));

				path = originatorPrefix + "[@" + ATT_ORIG_NUMPLANID + "]";
				value = config.getString(path);
				if (value != null) {
					try {
						originator.setNumPlanId(new Integer(value));
					} catch (NumberFormatException e) {
			        	StringWriter errors = new StringWriter();
			        	e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
						throw new XASException(Constants.XAS00025E_MESSAGE, null, Constants.XAS00025E_CODE, new Object[] {value, path});
					}
				} else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});

				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
				
				boolean existsAddressOrAlias = false;
				path = originatorPrefix + "[@" + ATT_ORIG_ADDRESS + "]";
				value = config.getString(path);	
				if (value != null) { originator.setAddress(value); existsAddressOrAlias = true; } 
				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
				
				path = originatorPrefix + "[@" + ATT_ORIG_ALIAS + "]";
				value = config.getString(path);	
				if (value != null) { originator.setAlias(value); existsAddressOrAlias = true;} 
				// logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
				
				if (!existsAddressOrAlias)
					throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {originatorPrefix + "[@" + ATT_ORIG_ADDRESS + "|@" + ATT_ORIG_ALIAS + "]"});
				
				if (originator.getRouterRegex() == null)
					originators.put(originator.getName(), originator); // Register originator
				else
					originatorRouters.put(originator.getName(), originator); // Register originator router
			}
			provider.setOriginators(originators);
			provider.setOriginatorRouters(originatorRouters);
			
			String customizationPath = providerPrefix + "." + TAG_CUSTOMIZATION;
			// logger.debug(sl.logIt("a_step", "type configuration", "a_value", provider.getType() != null ? provider.getType() : "null type").getLogRow(true));
			
			// Load configuration customization for providerType and set customization object and gatewaySms specific for provider
			Object customization = null;
			IGatewayA2Psms gatewaySms = null;
			if (it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE.equals(provider.getType())) {
				customization = it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.load(config, customizationPath);
				gatewaySms = new it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.GatewaySmsProvider();
			} else if (it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.TYPE.equals(provider.getType())) { 			//#TIM
				customization = it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.load(config, customizationPath);	//#TIM
				gatewaySms = new it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.GatewaySmsProvider();							//#TIM
			} else if (it.usi.xframe.xas.bfimpl.a2psms.providers.virtual.Configuration.TYPE.equals(provider.getType())) {
				customization = it.usi.xframe.xas.bfimpl.a2psms.providers.virtual.Configuration.load(config, customizationPath);
				gatewaySms = new it.usi.xframe.xas.bfimpl.a2psms.providers.virtual.GatewaySmsProvider();
			} else if (it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail.Configuration.TYPE.equals(provider.getType())) {
				customization = it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail.Configuration.load(config, customizationPath);
				gatewaySms = new it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail.GatewaySmsProvider();
			} else 	throw new XASException(Constants.XAS00026E_MESSAGE, null, Constants.XAS00026E_CODE, new Object[] {provider.getType(), provider.getName()});
			provider.setCustomization(customization);
			provider.setGatewaySms(gatewaySms);
			
//			logger.debug(sl.logIt("a_step", "check drProviderList", "a_value", provider.getName(), "a_list", (String) drProviderList.get(provider.getName())).getLogRow(true));
			// Check for Delivery Report service
			if (drProviderList.get(provider.getName()) != null) {
				if (!((CustomizationAbstract)(provider.getCustomization())).isSupportDeliveryReport()) {
					throw new XASException(Constants.XAS00028E_MESSAGE, null, Constants.XAS00028E_CODE, new Object[] {provider.getType(), provider.getName(), "Delivery Report", drProviderList.get(provider.getName())});
				}
					
			}

 //			logger.debug(sl.logIt("a_step", "check moProviderList", "a_value", provider.getName(), "a_list", (String) moProviderList.get(provider.getName())).getLogRow(true));
			// Check for Mobile Originated service
			if (moProviderList.get(provider.getName()) != null) {
				if (!((CustomizationAbstract)(provider.getCustomization())).isSupportMobileOriginated()) {
					throw new XASException(Constants.XAS00028E_MESSAGE, null, Constants.XAS00028E_CODE, new Object[] {provider.getType(), provider.getName(), "Mobile Originated", moProviderList.get(provider.getName())});
				}
			}

//			logger.debug(sl.logIt("a_step", "check rmProviderList", "a_value", provider.getName(), "a_list", (String) rmProviderList.get(provider.getName())).getLogRow(true));
			// Check for Replace Map service
			if (rmProviderList.get(provider.getName()) != null) {
				if (!((CustomizationAbstract)(provider.getCustomization())).isSupportReplaceMap()) {
					throw new XASException(Constants.XAS00028E_MESSAGE, null, Constants.XAS00028E_CODE, new Object[] {provider.getType(), provider.getName(), "Replace Map", rmProviderList.get(provider.getName())});
				}
			}

			newProviders.put(provider.getName(), provider);
		}
		return newProviders;
	}

	public void loadEnvironments(XMLConfiguration config, Boolean bAppGtwAccountRequired,
			SmartLog sl
			) throws XASException {
		String path, value;
		String currentEnvironment = ((IEnvironment) EnvironmentLoader.getDefault()).get(IEnvironment.TOWER);
		logger.info(sl.reload("myPreset").logIt("a_step", "identifyingEnv", "a_currentEnvironment", currentEnvironment).getLogRow(true));
		sl.reload("myPreset");
		
		String environmentPath = TAG_VERSION + "." + TAG_ENVIRONMENTS + "." + TAG_ENVIRONMENT + "[@" + ATT_ENV_NAME_REGEX + "]";
		String[] environmentsArray = config.getStringArray(environmentPath);
		for (int pi = 0; pi < environmentsArray.length; pi++) {
//			logger.debug(sl.logIt("a_step", environmentPath, "a_value", environmentsArray[pi]).getLogRow(true));
			
			String environmentPrefix = TAG_VERSION + "." + TAG_ENVIRONMENTS + "." + TAG_ENVIRONMENT  + "(" + pi + ")"; 

			path = environmentPrefix + "[@" + ATT_ENV_NAME_REGEX + "]";
			value = config.getString(path);	
			if (value != null) { // If the execution environment satisfy the regEx set up the corresponding parameters
				if (currentEnvironment.matches(value)) {
					logger.info(sl.reload("myPreset").logIt("a_step", path, "a_value", value, "a_currentEnvironment", currentEnvironment, "a_message", "environizing").getLogRow(true));
					sl.reload("myPreset");

					// ApplicationGateway user/password
					path = environmentPrefix + "." + TAG_SECURITY + "." + TAG_APPLICATION_GATEWAY + "[@" + ATT_ACC_USER + "]";
					value = config.getString(path); 
					if (value != null) {
						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
						this.applicationGWuser = value;
					} else if (bAppGtwAccountRequired.booleanValue()) {
						throw new XASException(Constants.XAS00033E_MESSAGE, null, Constants.XAS00033E_CODE, new Object[] {TAG_XASUSER + "." + ATT_XU_END_POINT, path});
					}

					
					path = environmentPrefix + "." + TAG_SECURITY + "." + TAG_APPLICATION_GATEWAY + "[@" + ATT_ACC_PASSWORD + "]";
					value = config.getString(path); 
					if (value != null) {
						this.applicationGWpassword = CustomizationBasic.decryptPassword(value);
					} else if (bAppGtwAccountRequired.booleanValue()) { 
						throw new XASException(Constants.XAS00033E_MESSAGE, null, Constants.XAS00033E_CODE, new Object[] {TAG_XASUSER + "." + ATT_XU_END_POINT, path});
					}
										
					String securityPath = environmentPrefix + "." + TAG_SECURITY + "." + TAG_ACCOUNT + "[@" + ATT_ACC_PROVIDER + "]";
					String[] securitysArray = config.getStringArray(securityPath);
					for (int si = 0; si < securitysArray.length; si++) {
						logger.debug(sl.logIt("a_step", securityPath, "a_value", securitysArray[si]).getLogRow(true));
						Provider provider = null;
						String user = null, password = null;
						
						String accountPrefix = environmentPrefix + "." + TAG_SECURITY + "." + TAG_ACCOUNT  + "(" + si + ")";
						
						path = accountPrefix + "[@" + ATT_ACC_PROVIDER + "]";
						value = config.getString(path);	
						if (value != null) { 
							provider = (Provider) providers.get(value);
							if (provider == null) 
								throw new XASException(Constants.XAS00037E_MESSAGE, null, Constants.XAS00037E_CODE, new Object[] {value, path});
						} else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
	
						path = accountPrefix + "[@" + ATT_ACC_USER + "]";
						value = config.getString(path);	
						if (value != null) { user = value; } 
						else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
	
						path = accountPrefix + "[@" + ATT_ACC_PASSWORD + "]";
						value = config.getString(path);	
						if (value != null) { password = value;} 
						else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
	
						if (it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Configuration.TYPE.equals(provider.getType())) {
							((it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Customization) provider.getCustomization()).setUser(user);
							((it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Customization) provider.getCustomization()).setPassword(password);
						} else if (it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Configuration.TYPE.equals(provider.getType())) { 			
							((it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Customization) provider.getCustomization()).setUser(user);
							((it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Customization) provider.getCustomization()).setPassword(password);
						} else 	throw new XASException(Constants.XAS00036E_MESSAGE, null, Constants.XAS00036E_CODE, new Object[] {provider.getType(), provider.getName()});
						
					}

					String envXasUserPath = environmentPrefix + "." + TAG_APPLICATION + "." + TAG_XASUSER + "[@" + ATT_XU_NAME + "]";
					String[] envXasUserArray = config.getStringArray(envXasUserPath);
					for (int si = 0; si < envXasUserArray.length; si++) {
						logger.debug(sl.logIt("a_step", envXasUserPath, "a_value", envXasUserArray[si]).getLogRow(true));
						XasUser xasUser = null;
						
						String accountPrefix = environmentPrefix + "." + TAG_APPLICATION + "." + TAG_XASUSER  + "(" + si + ")";
						
						path = accountPrefix + "[@" + ATT_XU_NAME + "]";
						value = config.getString(path);	
						if (value != null) { 
							xasUser = (XasUser) xasUsers.get(value);
							if (xasUser == null) 
								throw new XASException(Constants.XAS00038E_MESSAGE, null, Constants.XAS00038E_CODE, new Object[] {value, path});
						} else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
	
						path = accountPrefix + "[@" + ATT_XU_END_POINT + "]";
						value = config.getString(path);	
						if (value != null) { xasUser.setEndPoint(value); } 
						else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//						logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
					}
				}
			}
			else throw new XASException(Constants.XAS00027E_MESSAGE, null, Constants.XAS00027E_CODE, new Object[] {path});
//			logger.debug(sl.logIt("a_step", path, "a_value", value).getLogRow(true));
			}
		
	}
}
