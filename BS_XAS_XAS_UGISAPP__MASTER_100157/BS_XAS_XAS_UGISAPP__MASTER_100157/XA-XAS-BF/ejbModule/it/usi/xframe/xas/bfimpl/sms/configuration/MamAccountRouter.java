package it.usi.xframe.xas.bfimpl.sms.configuration;

//import ie.omk.smpp.util.GSMConstants;
import it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop.MamAccount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MamAccountRouter {

	private static Log log = LogFactory.getLog(MamAccountRouter.class);
//  Moved to Configuration&Cache	
//	private static MamAccountRouter instance = new MamAccountRouter();
	private static Map routers;
	
// #380	private static String PREFIX_HUNGARY = "^(\\+|00)?36";
	
// #380	private static final String HUNGARY = "HungarySC";
	
	static {
		routers = new HashMap();
//	#380	MamAccount hungary = getHungary();	// #380 Moved to SmsConfiguration.xml  
//	#380	Pattern p = Pattern.compile(PREFIX_HUNGARY);
//	#380	routers.put(p,hungary);
	}
	
//  Moved to Configuration&Cache 	
//	public static MamAccountRouter getInstance() {
//		return instance;
//	}
	
	public void put(MamAccount mamAccount) { // #380
		Pattern p = Pattern.compile(mamAccount.getRouterRegex());
		routers.put(p, mamAccount);
	}

	/* #380 Moved to SmsConfiguration.xml
	private static MamAccount getHungary() {
		MamAccount acct = new MamAccount();
		acct.setSystemId(HUNGARY);
		acct.setAddress("06305555566");
		acct.setTypeOfNumber(GSMConstants.GSM_TON_NATIONAL);
		return acct;
	}
	*/
	
	public MamAccount search(String number) {
		Set keys = routers.keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			Pattern key = (Pattern)iter.next();
			Matcher m = key.matcher(number);
			if (m.find()) {
				// log.debug("TROVATO! " + number + " - " + key);
				return (MamAccount)routers.get(key);
			} // else log.debug("NON QUAGLIA! " + number + " - " + key);

		}
		return null;
	}
	public void reset() {
		routers = new HashMap();
	}
	public Map getMap() {
		return routers;
	}
}
