package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;

public class Alert extends HtmlObject
{
	private static final String COMPONENT_NAME = "alert_";

	private String text = null;
	private AlertType type = null;
	
	/**
	 * @param name
	 * @param text
	 * @param type
	 */
	public Alert(String name, String text, AlertType type)
	{
		super();
		this.setName(name);
		this.text = text;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		if(type.isEqual(AlertType.SUCCESS))
			replace(COMPONENT_NAME + "level", "alert-success");
		else if(type.isEqual(AlertType.INFO))
			replace(COMPONENT_NAME + "level", "alert-info");
		else if(type.isEqual(AlertType.WARNING))
			replace(COMPONENT_NAME + "level", "alert-warning");
		else if(type.isEqual(AlertType.DANGER))
			replace(COMPONENT_NAME + "level", "alert-danger");
		replace(COMPONENT_NAME + "text", this.text);
	}
}
