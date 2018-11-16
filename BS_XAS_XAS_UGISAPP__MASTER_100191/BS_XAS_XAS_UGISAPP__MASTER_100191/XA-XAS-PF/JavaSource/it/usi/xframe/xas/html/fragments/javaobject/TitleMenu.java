package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

import java.util.ArrayList;
import java.util.List;

public class TitleMenu extends HtmlObject
{
	private static final String COMPONENT_NAME = "titlemenu_";

	List menuVoices = new ArrayList();

	private String href = null;
	private String text = null;
	
	/**
	 * @param name
	 * @param href
	 * @param text
	 */
	public TitleMenu(String name, String href, String text)
	{
		super();
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
