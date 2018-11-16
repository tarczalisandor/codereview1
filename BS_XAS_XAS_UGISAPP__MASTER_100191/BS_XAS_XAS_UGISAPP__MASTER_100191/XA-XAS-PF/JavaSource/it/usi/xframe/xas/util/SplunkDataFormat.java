package it.usi.xframe.xas.util;

public class SplunkDataFormat
{
	public static SplunkDataFormat XML		= new SplunkDataFormat(SplunkDataFormat.VALUE_XML);
	public static SplunkDataFormat JSON		= new SplunkDataFormat(SplunkDataFormat.VALUE_JSON);
	public static SplunkDataFormat CSV		= new SplunkDataFormat(SplunkDataFormat.VALUE_CSV);

	private static final String VALUE_XML		= "xml";
	private static final String VALUE_JSON		= "json";
	private static final String VALUE_CSV		= "csv";

	private String t = null;
	
	private SplunkDataFormat (String t) {
		this.t = t;
	}
	
	public String toString(){
		if (this.isEqual(XML))
			return "xml";
		else if (this.isEqual(JSON))
			return "json";
		else if (this.isEqual(CSV))
			return "csv";
		return "json";
	}
	
	public boolean isEqual(SplunkDataFormat type)
	{
		if(this.t.compareTo(type.t)==0)
			return true;
		return false;
	}
}
