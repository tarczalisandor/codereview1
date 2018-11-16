package it.usi.xframe.xas.html;

public class AlertType
{
	public static AlertType SUCCESS	= new AlertType(AlertType.VALUE_SUCCESS);
	public static AlertType INFO	= new AlertType(AlertType.VALUE_INFO);
	public static AlertType WARNING	= new AlertType(AlertType.VALUE_WARNING);
	public static AlertType DANGER	= new AlertType(AlertType.VALUE_DANGER);

	private static final String VALUE_SUCCESS	= "success";
	private static final String VALUE_INFO		= "info";
	private static final String VALUE_WARNING	= "warning";
	private static final String VALUE_DANGER	= "danger";

	private String t = null;
	
	private AlertType (String t) {
		this.t = t;
	}
	
	public boolean isEqual(AlertType type)
	{
		if(this.t.compareTo(type.t)==0)
			return true;
		return false;
	}
}
