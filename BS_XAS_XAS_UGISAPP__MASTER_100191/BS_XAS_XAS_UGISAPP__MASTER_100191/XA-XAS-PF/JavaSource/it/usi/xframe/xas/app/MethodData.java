package it.usi.xframe.xas.app;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodData
{
	private static Logger logger = LoggerFactory.getLogger(MethodData.class);
	
	public static final String STRING	= "String_";
	public static final String BOOLEAN	= "Boolean_";
	public static final String CALENDAR	= "Calendar_";

	HashMap params = new HashMap();
	
	public void set (String name, String value, String defValue)
	{
// To uncomment if you want use StringEscapeUtils class
//		value = StringEscapeUtils.unescapeHtml(value);
		if(value==null)
			value=defValue;
		if(value==null)
			value="";
		params.put(STRING + name, value);
	}

	public void set (String name, Calendar value)
	{
		params.put(CALENDAR + name, value);
	}

	public void set (String name, Boolean value)
	{
		params.put(BOOLEAN + name, value);
	}

	public String getString (String name)
	{
		return (String) params.get(STRING + name);
	}

	public Boolean getBoolean (String name)
	{
		return (Boolean) params.get(BOOLEAN + name);
	}

	public Calendar getCalendar (String name)
	{
		return (Calendar) params.get(CALENDAR + name);
	}
	
	public void dump()
	{
		logger.debug("--------------------------------------");
		int maxlength = 0;
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String key = ((String) pair.getKey()).substring(STRING.length());
			maxlength = key.length() > maxlength ? key.length() : maxlength ;  
		}

		it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String key = ((String) pair.getKey()).substring(STRING.length());
			logger.debug(pad(key, maxlength) + ": [" + pair.getValue() + "]");
		}
	}
	
	private String pad(String value, int len)
	{
		return new String(new char[len - value.length()]).replace('\0', ' ') + value;
	}
}
