/*
 * Created on Mar 26, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 * @author US00081
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InternationalSmsMessage implements Serializable {
	// Formatted text in Charset ISO 8859-1 
	private String text;
	
	
	/**
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param string
	 */
	public void setText(String string) {
		text = string;
	}

}
