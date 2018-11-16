package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

public class Chart extends HtmlObject
{
	private static final String COMPONENT_NAME = "chart_";

	private String label = null;
	
	/**
	 * @param name
	 * @param label
	 */
	public Chart(String name, String label)
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
		//super.add(HtmlCssPackage.CSS_TESTCHARTJS);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_CHARTJS);
	}
}
