package it.usi.xframe.xas.bfimpl.a2psms.providers;

import it.usi.xframe.xas.jaxrpc.auth.IUsernameTokenCache;
import it.usi.xframe.xas.jaxrpc.auth.UTCacheFactory;
import it.usi.xframe.xas.jaxrpc.auth.UsernameToken;
import it.usi.xframe.xas.bfimpl.a2psms.ApplicationGateway;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;

/**
 * Cache UsernameToken for web-service's wsse security derived by the Custom configuration for the provider.
 * 
 * Handler documentation at: http://agora.wiki.intranet.unicredit.it/xwiki/bin/view/XFrame+1.5/JAX-RPC_WSSE_UT_HELL.
 * 
 */
public class XasUTCacheFactory extends UTCacheFactory {
	private static Logger logger = LoggerFactory.getLogger(XasUTCacheFactory.class);

	protected void init(IUsernameTokenCache usernameTokenCache) {
		HashMap providerAccounts;
        try {
    		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasUTCacheFactory.class.getName(), ConstantsSms.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG)
            	.logIt(SmartLog.K_METHOD, "init");
    		int i = 0;
	        providerAccounts = Configuration.getInstance().getProviderAccounts();
			Iterator iterator = providerAccounts.entrySet().iterator();
			while (iterator.hasNext()) {
				UsernameToken usernameToken = new UsernameToken();
				Map.Entry providerAccount = (Map.Entry) iterator.next();
				usernameToken.setUsername(((Properties)providerAccount.getValue()).getProperty(Configuration.PROVIDER_USERNAME));
				usernameToken.setPassword(((Properties)providerAccount.getValue()).getProperty(Configuration.PROVIDER_PASSWORD));
				usernameTokenCache.setUsernameToken((String)providerAccount.getKey(), usernameToken);
				sl.logIt("a_provider" + i, (String)providerAccount.getKey(), "a_userName" + i, usernameToken.getUsername());
				i += 1;
			}

			//			Adding user/password for application notification with sendMail if defined  
			if (Configuration.getInstance().getApplicationGWuser() != null) {
				UsernameToken usernameToken = new UsernameToken();
				usernameToken.setUsername(Configuration.getInstance().getApplicationGWuser()); 
				usernameToken.setPassword(Configuration.getInstance().getApplicationGWpassword());
				usernameTokenCache.setUsernameToken("service.applicationGateway", usernameToken);
				sl.logIt("a_provider" + i, "service.applicationGateway", "a_userName" + i, usernameToken.getUsername());
				i += 1;
			}
		
		logger.info(sl.getLogRow(true)); // Debug and keep row

        } catch (XASException e) {
			SmartLog slException = new SmartLog(SmartLog.COUPLING_LOOSE_I)
					.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasUTCacheFactory.class.getName(), ConstantsSms.MY_UUID_INIT, SmartLog.V_SCOPE_DEBUG)
		    		.logIt(SmartLog.K_METHOD, "init"); 
			slException.logIt(SmartLog.K_STATUS_MSG, e.getMessage());
			logger.error(slException.getLogRow());
	        throw new RuntimeException(e);
        }
	}

}
