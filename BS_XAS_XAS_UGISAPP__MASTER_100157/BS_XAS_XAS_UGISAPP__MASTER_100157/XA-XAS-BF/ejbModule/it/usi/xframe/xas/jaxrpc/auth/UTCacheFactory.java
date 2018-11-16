package it.usi.xframe.xas.jaxrpc.auth;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author C305393
 *
 */
public abstract class UTCacheFactory {

	private static Log log = LogFactory.getLog( UTCacheFactory.class );
	
	/**
	 * Feed the given utCache with application-specific username tokens:
	 * Typically a set of technical users.
	 * This implementation is in charge of extender classes.
	 */
	protected abstract void init(IUsernameTokenCache utCache);
	
	private volatile static IUsernameTokenCache _cache = null;
//	private static boolean _flag = false;
	
	/**
	 * Default Constructor.
	 */
	public UTCacheFactory() {
		super();
	}
	// Semaforo per sincronizzazione a livello di classe dell'oggetto _cache
//	public final static synchronized boolean getAndSetCacheFlagToTrue() {
//		boolean returnFlag = _flag;
//		_flag = true;
//		return returnFlag;
//	}
	
	
	// T1 - T2
	// T1 [_cache=null] T2 [_cache=null]
	// T1 [setCacheInstance] T2 [setCacheInstance]
	// T1 [enter] T2 [wait]
	// T1 [exit _cache!=null] T2 [wait _cache=null]
	// T1 [continue] T2 [enter _cache=null find _cache!=null]
	
	public final static synchronized void setCacheInstance() {
		if (_cache == null){
			log.info("UTCacheFactory setCacheInstance() -> initializing cache");
			_cache = new UsernameTokenCacheImpl();
			log.info("UTCacheFactory setCacheInstance() -> cache : " + _cache.toString());
		}else{
			log.info("UTCacheFactory setCacheInstance() -> cache already initialized : " + _cache.toString());
		}
	}
	
	public final synchronized IUsernameTokenCache getUTCache()
	{	
		if (_cache == null)  // Se cache inizializzata non aspetto al semaforo.
		{	
			log.info(this.getClass().getName() + "getUTCache() -> _cache is null: call setCacheInstance()");
			setCacheInstance();
			init(_cache);
			log.info(this.getClass().getName()
					+ ":: successfully initialized: " + _cache.toString());
			if (log.isDebugEnabled()) {
				log.debug(this.getClass().getName()
						+ "::UsernameTokenCacheImpl classloader: "
						+ _cache.getClass().getClassLoader().toString());
			}
		}else{
			log.info(this.getClass().getName() + "getUTCache() -> _cache not null : " + _cache.toString());	
		}
			
//		if (!getAndSetCacheFlagToTrue()) {
//			_cache = new UsernameTokenCacheImpl();
//			init(_cache);
//			log.info(this.getClass().getName()
//					+ ":: successfully initialized: " + _cache.toString());
//			if (log.isDebugEnabled()) {
//				log.debug(this.getClass().getName()
//						+ "::UsernameTokenCacheImpl classloader: "
//						+ _cache.getClass().getClassLoader().toString());
//			}
//		}
		return _cache;
	}
}

/**
 * Default UsernameTokenCache implementation.
 */
final class UsernameTokenCacheImpl implements IUsernameTokenCache
{
	private final HashMap cache = new HashMap();
	
	public UsernameToken getUsernameToken(String profile) {
		return (UsernameToken)this.cache.get(profile) ;
	}

	public void setUsernameToken(String profile, UsernameToken ut) {
		this.cache.put(profile, ut);
	}
	
}