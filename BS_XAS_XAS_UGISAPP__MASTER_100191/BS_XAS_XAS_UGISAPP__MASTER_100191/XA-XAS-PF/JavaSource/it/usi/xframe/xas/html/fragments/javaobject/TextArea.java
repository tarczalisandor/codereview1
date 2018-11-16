package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class TextArea extends HtmlObject
{
	private static final String COMPONENT_NAME = "textarea_";

	private String text 		= null;	
	private String label		= null;
	private boolean prettyPrint	= false;	

	private int row=1;
	
	/**
	 * @param name
	 * @param label
	 * @param row
	 * @param text
	 */
	public TextArea(String name, String text, String label, int row)
	{
		super();
		this.setName(name);
		this.text = text;
		this.label = label;
		this.row = row;
	}

	/**
	 * @return
	 */
	public boolean isPrettyPrint()
	{
		return this.prettyPrint;
	}

	/**
	 * @param prettyPrint
	 */
	public void setPrettyPrint(boolean prettyPrint)
	{
		this.prettyPrint = prettyPrint;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "row", Integer.toString(this.row));
		replace(COMPONENT_NAME + "text", this.text);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		//if(prettyPrint)
		//	super.add(HtmlJsPackage.JS_PRETTYPRINT);
	}
}
