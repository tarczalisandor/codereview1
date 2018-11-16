/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil.data;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FileEmailAttachment extends AbstractEmailAttachment {
	private String filePath;

	public FileEmailAttachment() {
	}

	public FileEmailAttachment(
		String fileDescription,
		String mimeType,
		String filePath) {
		super(fileDescription, mimeType);
		this.filePath = filePath;
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
	public void setFilePath(String string) {
		filePath = string;
	}

	/**
	 * @see it.usi.xframe.jgh.bfutil.data.AbstractEmailAttachment#apply(it.usi.xframe.jgh.bfutil.data.EmailAttachmentVisitor)
	 */
	public void apply(EmailAttachmentVisitor visitor) {
		visitor.visit(this);
	}
}
