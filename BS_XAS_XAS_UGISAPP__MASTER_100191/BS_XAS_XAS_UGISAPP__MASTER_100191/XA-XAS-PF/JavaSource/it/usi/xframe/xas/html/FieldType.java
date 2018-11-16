package it.usi.xframe.xas.html;

public class FieldType
{
	public static FieldType TYPE_UNDEFINED	= new FieldType(FieldType.VALUE_UNDEFINED);
	public static FieldType TYPE_TEXT		= new FieldType(FieldType.VALUE_TEXT);
	public static FieldType TYPE_NUMBER		= new FieldType(FieldType.VALUE_NUMBER);
	public static FieldType TYPE_PHONE		= new FieldType(FieldType.VALUE_PHONE);
	public static FieldType TYPE_TIME		= new FieldType(FieldType.VALUE_TIME);
	public static FieldType TYPE_EMAIL		= new FieldType(FieldType.VALUE_EMAIL);
	public static FieldType TYPE_PASSWORD	= new FieldType(FieldType.VALUE_PASSWORD);
	public static FieldType TYPE_FILE		= new FieldType(FieldType.VALUE_FILE);
	public static FieldType TYPE_HIDDEN		= new FieldType(FieldType.VALUE_HIDDEN);

	private static final String VALUE_UNDEFINED	= "undefined";
	private static final String VALUE_TEXT		= "text";
	private static final String VALUE_NUMBER	= "number";
	private static final String VALUE_PHONE		= "phone";
	private static final String VALUE_TIME		= "time";
	private static final String VALUE_EMAIL		= "email";
	private static final String VALUE_PASSWORD	= "password";
	private static final String VALUE_FILE		= "file";
	private static final String VALUE_HIDDEN	= "hidden";

	private String t = null;
	
	private FieldType (String t) {
		this.t = t;
	}
	
	public boolean isEqual(FieldType type)
	{
		if(this.t.compareTo(type.t)==0)
			return true;
		return false;
	}
}
