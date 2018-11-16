/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EmailAttachment implements Serializable {
	private String fileDescription, filePath;

	public EmailAttachment() {
	}

	public EmailAttachment(String fileDescription, String filePath) {
		this.fileDescription = fileDescription;
		this.filePath = filePath;
	}

	/**
	 * @return
	 */
	public String getFileDescription() {
		return fileDescription;
	}

	/**
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param string
	 */
	public void setFileDescription(String string) {
		fileDescription = string;
	}

	/**
	 * @param string
	 */
	public void setFilePath(String string) {
		filePath = string;
	}
}
