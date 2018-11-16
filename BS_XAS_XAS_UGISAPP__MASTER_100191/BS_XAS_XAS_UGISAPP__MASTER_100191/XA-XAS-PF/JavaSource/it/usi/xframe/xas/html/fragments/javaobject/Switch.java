package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

public class Switch extends HtmlObject
{
	private static final String COMPONENT_NAME = "switch_";

	private boolean checked = true;
	private String label = null;
	private String[] linkedFields = null;
	
	/**
	 * @param name
	 * @param checked
	 */
	public Switch(String name, boolean checked, String label)
	{
		super();
		this.setName(name);
		this.checked = checked;
		this.label = label;
	}

	/**
	 * @return
	 */
	public String[] getLinkedFields()
	{
		return this.linkedFields;
	}

	/**
	 * @param linkedField
	 */
	public void setLinkedFields(String[] linkedFields)
	{
		this.linkedFields = linkedFields;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "checked", this.checked==true ? "checked" : "");
		replace(COMPONENT_NAME + "label", this.label);
		StringBuffer sbLinkedFields = new StringBuffer();
		if(this.linkedFields!=null)
			for(int i=0; i<this.linkedFields.length; i++)
				if(this.linkedFields[i]!=null)
					sbLinkedFields.append("$(\"#" + this.linkedFields[i] + "\").prop('disabled', !$(this).prop('checked'));\n");
		replace(COMPONENT_NAME + "linkedFields", sbLinkedFields.toString());
		
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
