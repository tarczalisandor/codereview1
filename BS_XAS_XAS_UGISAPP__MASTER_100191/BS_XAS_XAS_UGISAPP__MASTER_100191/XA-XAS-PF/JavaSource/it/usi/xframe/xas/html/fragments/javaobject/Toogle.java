package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

public class Toogle extends HtmlObject
{
	private static final String COMPONENT_NAME = "toogle_";

	private boolean status					= true;
	private String label					= null;
	private String[] disabledFields			= null;
	
	public final static String STATUS_ON	= "on";
	public final static String STATUS_OFF	= "off";

	/**
	 * @param name
	 * @param status
	 * @param label
	 */
	public Toogle(String name, boolean status, String label)
	{
		super();
		this.setName(name);
		this.status = status;
		this.label = label;
	}

	/**
	 * @return
	 */
	public String[] getDisabledFields()
	{
		return this.disabledFields;
	}

	/**
	 * @param disabledField
	 */
	public void setDisabledFields(String[] disabledFields)
	{
		this.disabledFields = disabledFields;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "checked", this.status==true ? "checked" : "");
		replace(COMPONENT_NAME + "label", this.label);
		StringBuffer sbDisabledFields = new StringBuffer();
		if(this.disabledFields!=null)
			for(int i=0; i<this.disabledFields.length; i++)
				if(this.disabledFields[i]!=null)
					sbDisabledFields.append("$(\"#" + this.disabledFields[i] + "\").prop('disabled', !$(this).prop('checked'));\n");
		replace(COMPONENT_NAME + "disabledFields", sbDisabledFields.toString());
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_TOOGLE);
	}
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_TOOGLE);
	}
}
