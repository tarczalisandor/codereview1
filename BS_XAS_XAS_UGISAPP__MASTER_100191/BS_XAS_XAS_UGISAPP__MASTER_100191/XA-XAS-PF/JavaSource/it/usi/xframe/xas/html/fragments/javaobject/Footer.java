package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;

public class Footer extends HtmlObject
{
	private static final String COMPONENT_NAME = "footer_";

	private String title = null;
	private String chrono = null;
	
	/**
	 * @param title
	 */
	public Footer(String name, String title)
	{
		super();
		this.setName(name);
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getChrono()
	{
		return chrono;
	}

	/**
	 * @param chrono
	 */
	public void setChrono(String chrono)
	{
		this.chrono = chrono;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "title", this.title);
		replace(COMPONENT_NAME + "chrono", this.chrono);
	}
}
