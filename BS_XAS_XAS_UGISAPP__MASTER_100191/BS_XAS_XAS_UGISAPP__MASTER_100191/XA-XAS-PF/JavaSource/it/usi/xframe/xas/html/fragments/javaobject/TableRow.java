package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlObject;


public class TableRow extends HtmlObject
{
	private static final String COMPONENT_NAME = "tablerow_";

	private String[] values = null;
	
	/**
	 * @param name
	 * @param values
	 */
	public TableRow(String name, String[] values)
	{
		super();
		this.setName(name);
		this.values = values;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceHeaderPlaceholder()
	 */
	public void replaceHeaderPlaceholder() 
	{
		StringBuffer sb = new StringBuffer();
		if(values!=null)
			for(int i=0; i<values.length; i++)
				sb.append("<td>" + values[i] + "</td>");
		replace(COMPONENT_NAME + "values", sb.toString());
	}
}
