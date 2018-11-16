package it.usi.xframe.xas.html;

import it.usi.xframe.xas.servlet.ServletBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserType
{
	private static Logger logger = LoggerFactory.getLogger(BrowserType.class);

	public static BrowserType IE		= new BrowserType(BrowserType.VALUE_IE);
	public static BrowserType IE8		= new BrowserType(BrowserType.VALUE_IE8);
	public static BrowserType IE11		= new BrowserType(BrowserType.VALUE_IE11);
	public static BrowserType FIREFOX	= new BrowserType(BrowserType.VALUE_FIREFOX);
	public static BrowserType CHROME	= new BrowserType(BrowserType.VALUE_CHROME);

	private static final String VALUE_IE		= "ie";
	private static final String VALUE_IE8		= "ie8";
	private static final String VALUE_IE11		= "ie11";
	private static final String VALUE_FIREFOX	= "firefox";
	private static final String VALUE_CHROME	= "chrome";

	private static final Pattern patternIE8		= Pattern.compile("msie [5-8](?:\\.[0-9]+)");
	private static final Pattern patternIE11	= Pattern.compile("msie (([9])|1[0-1])(?:\\.[0-9]+)");
	private static final Pattern patternFIREFOX	= Pattern.compile("firefox");
	private static final Pattern patternCHROME	= Pattern.compile("chrome");

	private String t	= null;	// type
	private int v 		= 0;	// version
	
	private BrowserType (String t) {
		this.t = t;
	}

	public void setVersion(int v) {
		this.v = v;
	}

	public int getVersion() {
		return this.v;
	}

	public boolean isEqual(BrowserType type)
	{
		if(this.t.compareTo(type.t)==0)
			return true;
		return false;
	}
	
	public String toString()
	{
		return t;
	}
	
	public static BrowserType getBrowserType(HttpServletRequest req)
	{
		BrowserType ret = null;
		String useragent = req.getHeader("User-Agent").toLowerCase();
		logger.debug("useragent:" + useragent);
		if(useragent!=null)
			ret = getBrowserTypeEx(useragent);
		if(ret==null)
			ret = BrowserType.IE8;
		logger.debug("BrowserType:" + ret.toString());
		return ret;
	}
	
	private static BrowserType getBrowserTypeEx(String useragent)
	{
		Matcher matcher = patternIE8.matcher(useragent);
		if(matcher.find())
			return BrowserType.IE8;

		matcher = patternIE11.matcher(useragent);
		if(matcher.find())
			return BrowserType.IE11;

		matcher = patternFIREFOX.matcher(useragent);
		if(matcher.find())
			return BrowserType.FIREFOX;

		matcher = patternCHROME.matcher(useragent);
		if(matcher.find())
			return BrowserType.CHROME;

		return null;
	}

	public static void main (String [] args)
	{
	}
}
