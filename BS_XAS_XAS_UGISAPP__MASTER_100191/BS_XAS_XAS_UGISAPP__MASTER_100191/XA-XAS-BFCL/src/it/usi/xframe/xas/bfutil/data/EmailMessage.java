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
public class EmailMessage implements Serializable {
	private String mailFrom, mailFromName, replyTo, mailTo, mailCc, mailBcc, mailSubject, mailMessage;
	private boolean isHtml;

	public EmailMessage() {
	}

	public EmailMessage(
		String mailFrom,
		String mailFromName,
		String replyTo,
		String mailTo,
		String mailCc,
		String mailBcc,
		String mailSubject,
		String mailMessage,
		boolean isHtml) {
		this.mailFrom = mailFrom;
		this.mailFromName = mailFromName;
		this.replyTo = replyTo;
		this.mailTo = mailTo;
		this.mailCc = mailCc;
		this.mailBcc = mailBcc;
		this.mailSubject = mailSubject;
		this.mailMessage = mailMessage;
		this.isHtml = isHtml;
	}
	/**
	 * @return
	 */
	public boolean isHtml() {
		return isHtml;
	}

	/**
	 * @return
	 */
	public String getMailBcc() {
		return mailBcc;
	}

	/**
	 * @return
	 */
	public String getMailCc() {
		return mailCc;
	}

	/**
	 * @return
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @return
	 */
	public String getMailMessage() {
		return mailMessage;
	}

	/**
	 * @return
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * @return
	 */
	public String getMailTo() {
		return mailTo;
	}

	/**
	 * @param b
	 */
	public void setHtml(boolean b) {
		isHtml = b;
	}

	/**
	 * @param string
	 */
	public void setMailBcc(String string) {
		mailBcc = string;
	}

	/**
	 * @param string
	 */
	public void setMailCc(String string) {
		mailCc = string;
	}

	/**
	 * @param string
	 */
	public void setMailFrom(String string) {
		mailFrom = string;
	}

	/**
	 * @param string
	 */
	public void setMailMessage(String string) {
		mailMessage = string;
	}

	/**
	 * @param string
	 */
	public void setMailSubject(String string) {
		mailSubject = string;
	}

	/**
	 * @param string
	 */
	public void setMailTo(String string) {
		mailTo = string;
	}
	/**
	 * @return
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * @param string
	 */
	public void setReplyTo(String string) {
		replyTo = string;
	}

	/**
	 * @return
	 */
	public String getMailFromName() {
		return mailFromName;
	}

	/**
	 * @param string
	 */
	public void setMailFromName(String string) {
		mailFromName = string;
	}

}
