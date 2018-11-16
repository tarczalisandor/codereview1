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
public abstract class AbstractEmailAttachment implements Serializable {
	private String attachmentDescription;
	private String attachmentMimeType;

	public AbstractEmailAttachment() {
	}

	public AbstractEmailAttachment(String description, String mimeType) {
		this.attachmentDescription = description;
		this.attachmentMimeType = mimeType;
	}

	public abstract void apply(EmailAttachmentVisitor visitor);

	/**
	 * @return
	 */
	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	/**
	 * @param string
	 */
	public void setAttachmentDescription(String string) {
		attachmentDescription = string;
	}

	/**
	 * @return
	 */
	public String getAttachmentMimeType() {
		return attachmentMimeType;
	}

	/**
	 * @param string
	 */
	public void setAttachmentMimeType(String string) {
		attachmentMimeType = string;
	}
}
