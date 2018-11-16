/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment;
import it.usi.xframe.xas.bfutil.data.EmailAttachmentVisitor;
import it.usi.xframe.xas.bfutil.data.FileEmailAttachment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MultipartAppender implements EmailAttachmentVisitor {
	private static Log log = LogFactory.getLog(MultipartAppender.class);

	private Multipart msgMultipart;

	/**
	 * 
	 */
	public MultipartAppender() {
		super();
	}

	/**
	 * @see it.usi.xframe.jgh.bfutil.data.EmailAttachmentVisitor#visit(it.usi.xframe.jgh.bfutil.data.BinaryEmailAttachment)
	 */
	public void visit(BinaryEmailAttachment attachment) {
		String fileDescription = attachment.getAttachmentDescription();
		byte[] binaryAttachment = attachment.getBinaryAttachment();
		try {
			DataSource dataSource =
				new BinaryDataSource(
					fileDescription,
					binaryAttachment,
					attachment.getAttachmentMimeType());
			MimeBodyPart msgAttachment = new MimeBodyPart();
			msgAttachment.setDataHandler(new DataHandler(dataSource));
			msgAttachment.setFileName(fileDescription);
			msgAttachment.setDescription(fileDescription);
			msgMultipart.addBodyPart(msgAttachment);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @see it.usi.xframe.jgh.bfutil.data.EmailAttachmentVisitor#visit(it.usi.xframe.jgh.bfutil.data.FileEmailAttachment)
	 */
	public void visit(FileEmailAttachment attachment) {
		String fileDescription = attachment.getAttachmentDescription();
		DataSource dataSource = new FileDataSource(attachment.getFilePath());
		MimeBodyPart msgAttachment = new MimeBodyPart();
		try {
			msgAttachment.setDataHandler(new DataHandler(dataSource));
			msgAttachment.setFileName(fileDescription);
			msgMultipart.addBodyPart(msgAttachment);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @return
	 */
	public Multipart getMsgMultipart() {
		return msgMultipart;
	}

	/**
	 * @param multipart
	 */
	public void setMsgMultipart(Multipart multipart) {
		msgMultipart = multipart;
	}
}
