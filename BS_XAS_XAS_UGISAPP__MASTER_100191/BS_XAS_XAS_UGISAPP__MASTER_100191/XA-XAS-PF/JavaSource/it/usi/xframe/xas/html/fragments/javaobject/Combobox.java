package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

public class Combobox extends HtmlObject
{
	private static final String COMPONENT_NAME = "combobox_";

	private String label		= null;
	private String suggestion	= "";
	
	/**
	 * @param name
	 * @param label
	 */
	public Combobox(String name, String label)
	{
		super();
		this.setName(name);
		this.label = label;
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

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "suggestion", this.suggestion);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_BOOSTRAP_COMBOBOX);
	}
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_BOOSTRAP_COMBOBOX);
	}
}
