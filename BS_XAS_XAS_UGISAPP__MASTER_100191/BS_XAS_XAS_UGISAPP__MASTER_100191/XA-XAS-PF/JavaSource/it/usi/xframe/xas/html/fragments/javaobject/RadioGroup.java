package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class RadioGroup extends HtmlObject
{
	private static final String COMPONENT_NAME = "radiogroup_";

	private String label = null;
	
	/**
	 * @param name
	 */
	public RadioGroup(String name, String label)
	{
		super();

		this.setName(name);
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "label", this.label);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		//super.add(HtmlCssPackage.CSS_BOOSTRAP_MDB);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		//super.add(HtmlJsPackage.JS_BOOSTRAP_MDB);
	}
}
