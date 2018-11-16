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
public interface EmailAttachmentVisitor {
	/**
	 * @param attachment
	 */
	public abstract void visit(BinaryEmailAttachment attachment);

	/**
	 * @param attachment
	 */
	public abstract void visit(FileEmailAttachment attachment);
}
