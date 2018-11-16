package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

public class TestTag extends HtmlObject
{
	private static final String COMPONENT_NAME = "testtag_";

	private String text = null;
	
	/**
	 * @param name
	 * @param text
	 */
	public TestTag(String name, String text)
	{
		super();
		this.setName(name);
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "text", this.text);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_BOOSTRAP_MDB);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_BOOSTRAP_MDB);
	}
}
