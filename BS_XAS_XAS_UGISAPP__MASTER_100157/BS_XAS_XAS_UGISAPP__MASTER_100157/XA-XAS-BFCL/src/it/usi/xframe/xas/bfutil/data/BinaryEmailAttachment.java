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
public class BinaryEmailAttachment extends AbstractEmailAttachment {
	byte[] binaryAttachment;

	public BinaryEmailAttachment() {
	}

	public BinaryEmailAttachment(
		String attachmentDescription,
		String mimeType,
		byte[] binaryAttachment) {
		super(attachmentDescription, mimeType);
		this.binaryAttachment = binaryAttachment;
	}

	/**
	 * @return
	 */
	public byte[] getBinaryAttachment() {
		return binaryAttachment;
	}

	/**
	 * @param bs
	 */
	public void setBinaryAttachment(byte[] bs) {
		binaryAttachment = bs;
	}

	/**
	 * @see it.usi.xframe.jgh.bfutil.data.AbstractEmailAttachment#apply(it.usi.xframe.jgh.bfutil.data.EmailAttachmentVisitor)
	 */
	public void apply(EmailAttachmentVisitor visitor) {
		visitor.visit(this);
	}
}
