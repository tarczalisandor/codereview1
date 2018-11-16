package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.TooltipPosition;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FieldDate extends HtmlObject
{
	private static final String COMPONENT_NAME	= "fielddate_";

	private static final String DATE_FORMAT		= "dd/MM/yyyy HH:mm";
	
	private String label = null;
	private String value = null;
	private String suggestion = null;
	private TooltipPosition tooltip_position	= TooltipPosition.TOP;

	/**
	 * @param name
	 * @param value
	 * @param label
	 */
	public FieldDate(String name, Calendar value, String label)
	{
		super();
		String s = null;
		if (value == null)
			s = "";
		else {
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			s = formatter.format(value.getTime());
		}
		this.setName(name);
		this.value = s;
		this.label = label;
	}
	
	/**
	 * @return the tooltip_position
	 */
	public TooltipPosition getTooltip_position() {
		return tooltip_position;
	}

	/**
	 * @param tooltip_position the tooltip_position to set
	 */
	public void setTooltip_position(TooltipPosition tooltip_position) {
		this.tooltip_position = tooltip_position;
	}

	/**
	 * @return
	 */
	public String getSuggestion() {
		return suggestion;
	}

	/**
	 * @param suggestion
	 */
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion==null ? "" : suggestion;
	}

	/**
	 * @param value
	 * @return
	 * 
	 * return the date in Calendar format
	 */
	public static Calendar toCalendar(String value)
	{
		Date d = null;
		try {
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			d = formatter.parse(value);
		} catch (ParseException e) {
			return null;
			//throw new IllegalArgumentException("Cannot convert [" + value + "] to date");
		}
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		//log.debug("DatatypeConverter: original date:" + date + " applay format=" + format +  " normalized:" + c.getTime().toGMTString());
		return c;
	}

	/**
	 * @param value
	 * @return
	 * 
	 * return the date in Date format
	 */
	public static Date toDate (String value, String format) throws XASException
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat(format);
			Date date = formatter.parse(value);
			return date;
		}
		catch (ParseException e)
		{
			throw new XASException("Validity date " + value + " is not valid");
		}
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "value", this.value);
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "suggestion", this.suggestion);
		replace(COMPONENT_NAME + "tooltip_position", this.tooltip_position.toString());
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_TEMPUSDOMINUS);
	}
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_JQUERY);
		super.add(HtmlJsPackage.JS_MOMENT_LOCALE);
		super.add(HtmlJsPackage.JS_TEMPUSDOMINUS);
	}
}
