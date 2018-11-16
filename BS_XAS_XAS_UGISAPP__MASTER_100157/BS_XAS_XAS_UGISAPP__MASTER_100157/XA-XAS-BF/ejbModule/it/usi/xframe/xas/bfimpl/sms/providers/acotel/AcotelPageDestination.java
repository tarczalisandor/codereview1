/*
 * Created on Jan 4, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.sms.providers.acotel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author US00081
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AcotelPageDestination {

	/*	DEPRECATED 2016.07.18

	private static Log log = LogFactory.getLog(AcotelPageDestination.class);
	
	private Pattern phoneNumberPat = null; 
	private String pageUrl = null;
	private String description = null;
	
	
	public AcotelPageDestination(String phonePattern, String url, String desc)
			throws PatternSyntaxException {
		phoneNumberPat = Pattern.compile(phonePattern);
		pageUrl = url;
		description = desc;
		
		log.debug("Acotel page destination ["+description+"]");
		log.debug("Acotel page url ["+pageUrl+"]");
		log.debug("Acotel phone number pattern ["+phoneNumberPat.pattern()+"]");
	}

	public boolean matches(String phoneNumber) {
		Matcher m = phoneNumberPat.matcher( phoneNumber );
		return m.matches();
	}

	public String getPageUrl() {
		return pageUrl;
	}
	
	public String getDescription() {
		return description;
	}
	*/
}
