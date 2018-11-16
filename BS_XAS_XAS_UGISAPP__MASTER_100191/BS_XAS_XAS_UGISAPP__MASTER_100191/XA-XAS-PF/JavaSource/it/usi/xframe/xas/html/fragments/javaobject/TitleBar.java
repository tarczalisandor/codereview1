package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class TitleBar extends HtmlObject
{
	private static final String COMPONENT_NAME = "titlebar_";

	private String href = null;
	private String text = null;
	
	/**
	 * @param href
	 * @param text
	 */
	public TitleBar(String name, String href, String text)
	{
		super();
//		this.setName("navbarSupportedContent");
		this.setName(name);
		this.href = href;
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "href", this.href);
		replace(COMPONENT_NAME + "text", this.text);
	}
}
