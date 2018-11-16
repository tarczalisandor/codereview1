package it.usi.xframe.xas.html;

public class TooltipPosition
{
	public static TooltipPosition TOP		= new TooltipPosition(TooltipPosition.VALUE_TOP);
	public static TooltipPosition BOTTOM	= new TooltipPosition(TooltipPosition.VALUE_BOTTOM);
	public static TooltipPosition LEFT		= new TooltipPosition(TooltipPosition.VALUE_LEFT);
	public static TooltipPosition RIGHT		= new TooltipPosition(TooltipPosition.VALUE_RIGHT);

	private static final String VALUE_TOP		= "top";
	private static final String VALUE_BOTTOM	= "bottom";
	private static final String VALUE_LEFT		= "left";
	private static final String VALUE_RIGHT		= "right";

	private String t = null;
	
	private TooltipPosition (String t) {
		this.t = t;
	}
	
	public String toString(){
		if (this.isEqual(TOP))
			return "top";
		else if (this.isEqual(BOTTOM))
			return "bottom";
		else if (this.isEqual(LEFT))
			return "left";
		else if (this.isEqual(RIGHT))
			return "right";
		return "";
	}
	
	public boolean isEqual(TooltipPosition type)
	{
		if(this.t.compareTo(type.t)==0)
			return true;
		return false;
	}
}
