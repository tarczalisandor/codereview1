package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.TooltipPosition;

public class Field extends HtmlObject
{
	private static final String COMPONENT_NAME = "field_";

	// redefined here type values necessary to html code
	private static final String TYPE_TEXT		= "text";
	private static final String TYPE_EMAIL		= "email";
	private static final String TYPE_FILE		= "file";
	private static final String TYPE_PASSWORD	= "password";
	private static final String TYPE_NUMBER		= "number";
	private static final String TYPE_HIDDEN		= "hidden";

	private static final String FORMAT_EMPTY	= "";
	private static final String FORMAT_PHONE	= "+dd (ddd) ddddddd";

	private static final String CLASS_EMPTY		= "";

	private static final String TYPE_DEFAULT	= TYPE_TEXT;
	private static final String CLASS_DEFAULT	= CLASS_EMPTY;
	private static final String FORMAT_DEFAULT	= FORMAT_EMPTY;

	private String text							= null;
	private String label						= "";
	private FieldType type						= FieldType.TYPE_UNDEFINED;
	private String suggestion					= "";
	private String icon							= "";
	private boolean required 					= false;
	private boolean enabled						= true;
	private TooltipPosition tooltip_position	= TooltipPosition.TOP;

	/**
	 * @param name
	 * @param text
	 * @param label
	 * @param type
	 */
	public Field(String name, String text, String label, FieldType type)
	{
		super();
		this.setName(name);
		this.text = text;
		this.label = label==null ? "" : label;
		this.type = type==null ? FieldType.TYPE_UNDEFINED : type;
	}
	
	
	/**
	 * @return the tooltip_position
	 */
	public TooltipPosition getTooltip_position() {
		return tooltip_position;
	}


	/**
	 * @param tooltip_position the tooltip_position to set
	 */
	public void setTooltip_position(TooltipPosition tooltip_position) {
		this.tooltip_position = tooltip_position;
	}


	/**
	 * @return
	 */
	public String getSuggestion() {
		return suggestion;
	}

	/**
	 * @param suggestion
	 */
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion==null ? "" : suggestion;
	}

	/**
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon==null ? "" : icon;
	}

	/**
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceAllPlaceholder()
	 */
	public void replaceAllPlaceholder() 
	{
		String field_type   = TYPE_DEFAULT;
		String field_class  = CLASS_DEFAULT;
		String field_format = FORMAT_DEFAULT;
		String field_hide   = "";
		String field_icon   = this.icon;
		String field_others_attributes = "";
		
		if(type.isEqual(FieldType.TYPE_EMAIL)) {
			field_type = TYPE_EMAIL;
			field_icon = "fa-envelope";
		}
		else if(type.isEqual(FieldType.TYPE_FILE)) {
			field_type = TYPE_FILE;
			field_class = "form-control-file";
			field_icon = "fa-file";
		}
		else if(type.isEqual(FieldType.TYPE_HIDDEN)) {
			field_type = TYPE_HIDDEN;
			field_class = "form-text";
			field_hide = "d-none";
		}
		else if(type.isEqual(FieldType.TYPE_NUMBER)) {
			field_type = TYPE_NUMBER;
			field_class = "currency";
			//field_others_attributes = "value=\"0\" min=\"0\" step=\"1\" data-number-to-fixed=\"0\" data-number-stepfactor=\"1\"";
			field_icon = "fa-calculator";
		}
		else if(type.isEqual(FieldType.TYPE_PASSWORD)) {
			field_type = TYPE_PASSWORD;
			field_icon = "fa-unlock";
			//tmpIcon = this.icon.length()>0 ? this.icon : "fa-user-secret";
		}
		else if(type.isEqual(FieldType.TYPE_PHONE)) {
			field_type = TYPE_TEXT;
			field_format = FORMAT_PHONE;
			field_class = "bfh-phone";
			field_icon = "fa-phone";
		}
		else if(type.isEqual(FieldType.TYPE_TEXT)) {
			field_type = TYPE_TEXT;
			field_icon = "fa-pencil-alt";
		}
		else if(type.isEqual(FieldType.TYPE_TIME)) {
			field_icon = "fa-calendar";
		}
		
		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "value", encodeHtml(this.text));
		replace(COMPONENT_NAME + "label", encodeHtml(this.label));
		replace(COMPONENT_NAME + "type", field_type);
		replace(COMPONENT_NAME + "format", field_format);
		replace(COMPONENT_NAME + "class", field_class);
		replace(COMPONENT_NAME + "hide", field_hide);
		replace(COMPONENT_NAME + "tooltip_position", this.tooltip_position.toString());
		replace(COMPONENT_NAME + "suggestion", encodeHtml(this.suggestion));
		replace(COMPONENT_NAME + "required", this.required==true ? "required" : "");
		replace(COMPONENT_NAME + "enabled", this.enabled==true ? "" : "disabled");
		replace(COMPONENT_NAME + "icon", "fa " + (this.icon.length()>0 ? this.icon : field_icon));
		replace(COMPONENT_NAME + "others_attributes", field_others_attributes);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		if(this.type.isEqual(FieldType.TYPE_PHONE)) {
			super.add(HtmlCssPackage.CSS_BOOSTRAP_FORMHELPERS);
		}
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		if(this.type.isEqual(FieldType.TYPE_PHONE)) {
			super.add(HtmlJsPackage.JS_POPPER);
			super.add(HtmlJsPackage.JS_BOOSTRAP_FORMHELPERS);
			super.add(HtmlJsPackage.JS_BOOSTRAP_FORMHELPERS_PHONE);
		}
	}
}
