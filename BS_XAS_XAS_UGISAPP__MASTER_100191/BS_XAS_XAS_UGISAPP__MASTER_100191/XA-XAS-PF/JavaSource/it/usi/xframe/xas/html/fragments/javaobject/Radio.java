package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class Radio extends HtmlObject
{
	private static final String COMPONENT_NAME = "radio_";

	private String group = null;
	private String value = null;
	private String label = null;
	private boolean checked = false;
	private String[] disabledFields = null;
	private String[] enabledFields = null;
	
	/**
	 * @param name
	 * @param group
	 * @param value
	 * @param label
	 * @param checked
	 */
	public Radio(String name, String group, String value, String label, boolean checked)
	{
		super();
		this.setName(name);
		this.group = group;
		this.value = value;
		this.label = label;
		this.checked = checked;
	}

	/**
	 * @return
	 */
	public String[] getEnabledFields()
	{
		return this.enabledFields;
	}

	/**
	 * @param enabledField
	 */
	public void setEnabledFields(String[] enabledFields)
	{
		this.enabledFields = enabledFields;
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
		replace(COMPONENT_NAME + "group", this.group);
		replace(COMPONENT_NAME + "value", this.value);
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "checked", (checked==true ? "checked=\"cheched\"" : ""));
		StringBuffer sbEnabledFields = new StringBuffer();
		if(this.enabledFields!=null)
			for(int i=0; i<this.enabledFields.length; i++)
				if(this.enabledFields[i]!=null)
					sbEnabledFields.append("$(\"#" + this.enabledFields[i] + "\").prop('disabled', $(this).prop('checked'));\n");
		replace(COMPONENT_NAME + "enabledFields", sbEnabledFields.toString());
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
//		super.add(HtmlCssPackage.CSS_BOOSTRAP_MDB);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
//		super.add(HtmlJsPackage.JS_BOOSTRAP_MDB);
	}
	
}
