package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.TooltipPosition;

public class Check extends HtmlObject
{
	private static final String COMPONENT_NAME = "check_";

	private String label = null;
	private boolean checked = false;
	private String suggestion	= "";
	private TooltipPosition tooltip_position	= TooltipPosition.TOP;
	
	/**
	 * @param name
	 * @param value
	 * @param label
	 */
	public Check(String name, String label, boolean checked)
	{
		super();
		this.setName(name);
		this.label = label;
		this.checked = checked;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "checked", checked==true ? "checked" : "");
		replace(COMPONENT_NAME + "value", "true");
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "suggestion", this.suggestion);
		replace(COMPONENT_NAME + "tooltip_position", this.tooltip_position.toString());

	}

	/**
	 * @param tooltip_position the tooltip_position to set
	 */
	public void setTooltip_position(TooltipPosition tooltip_position) {
		this.tooltip_position = tooltip_position;
	}

	/**
	 * @return the tooltip_position
	 */
	public TooltipPosition getTooltip_position() {
		return tooltip_position;
	}

	/**
	 * @return the suggestion
	 */
	public String getSuggestion() {
		return suggestion;
	}

	/**
	 * @param suggestion the suggestion to set
	 */
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}
