package it.usi.xframe.xas.util;

import it.usi.xframe.pre.bfintf.IPreServiceFacade;
import it.usi.xframe.pre.bfutil.PGEabdb2;
import it.usi.xframe.pre.bfutil.PreServiceFactory;
import it.usi.xframe.pre.bfutil.EnvironmentVar.PreEnvironmentVar;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASRuntimeException;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.ReadablePeriod;

public class PreSmsRoleCache {
	
	private static PreSmsRoleCache instance = new PreSmsRoleCache();
	private static ReadablePeriod cacheDuration = Hours.ONE;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Map keys = new HashMap(); 
	private Map expirations = new HashMap();
	private Map locks = new HashMap();
	
	private PreSmsRoleCache(){}
	
	public static PreSmsRoleCache getInstance() {
		return instance;
	}
	
	public boolean isInRole(String userid) {
		Object lock = getLock(userid);
		synchronized(lock) {
			Boolean b = getFromCache(userid);
			if (b != null) {
				if (log.isDebugEnabled()) log.debug("Letto valore in cache per " + userid);
				return b.booleanValue();
			}
			PreServiceFactory factory = PreServiceFactory.getInstance();
			IPreServiceFacade facade = null;
			boolean isUserInRole = false;
			try {
				facade = factory.getPreServiceFacade();
				PreEnvironmentVar env = facade.getEnvVarExtended(false);
				PGEabdb2 user = facade.SoapRequestPGEabdb2(env.getCodBanca(), userid, Constants.MY_APPL_ID);
				String authority = user.getPG_COD_AUTHOR();
				if (! (authority == null)) authority = authority.trim();
				isUserInRole = Constants.SMS_AUTHORITY.equals(authority);
				//log.debug("AUTHORITY PER " + userid + ": " + user.getPG_COD_AUTHOR() + "-");
			} catch (Exception e) {
				throw new XASRuntimeException("Exception calling PRE for PreUserData",e);
			} finally {
				//factory.dispose(facade);    CANNOT DISPOSE FACADE AS MDB (UNAUTHENTICATED USER) GETS ERROR
			}
			keys.put(userid, new Boolean(isUserInRole));
			expirations.put(userid, createExpiration());
			if (log.isDebugEnabled()) log.debug("Chiamata PRE per " + userid);
			return isUserInRole;
		}
	}
	
	private Boolean getFromCache(String userid) {
		if (keys.containsKey(userid)) {	//key is in cache, check if expired
			DateTime expire = (DateTime)expirations.get(userid);
			if (expire == null) { //SHOULD NOT HAPPEN!
				expire = createExpiration();
				expirations.put(userid,expire);
			}
			if (expire.isBeforeNow()) {	//key is expired
				return null;
			} else return (Boolean)keys.get(userid);	//return value from cache
		} else return null;		//key is not in cache
	}
	
	private DateTime createExpiration() {
		DateTime now = new DateTime();
		return now.plus(cacheDuration);
	}
	
	private synchronized Object getLock(String userid) {
		Object lock = locks.get(userid);
		if (lock == null) {
			lock = new Object();
			locks.put(userid, lock);
		}
		return lock;
	}

}
