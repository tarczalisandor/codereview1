package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class DropdownRow extends HtmlObject
{
	private static final String COMPONENT_NAME = "dropdownrow_";

	private String text = null;
	private boolean selected = true;
	
	/**
	 * @param name
	 * @param text
	 * @param selected
	 */
	public DropdownRow(String name, String text, boolean selected)
	{
		super();
		this.setName(name);
		this.text = text;
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "text", this.text);
		replace(COMPONENT_NAME + "selected", (this.selected==true ? "selected" : ""));
	}
}
