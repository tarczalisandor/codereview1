/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.xas.bfutil.ConstantsEmail;
import it.usi.xframe.xas.bfutil.ConstantsSplunk;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.AbstractEmailAttachment;
import it.usi.xframe.xas.bfutil.data.EmailMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.usertype.LoggableUserType;

import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SLF_Exception;
import eu.unicredit.xframe.slf.SmartAnalytic;
import eu.unicredit.xframe.slf.SmartLog;

/**
 * @author us01170
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SendEmail
{
	private static Log log = LogFactory.getLog(SendEmail.class);
	private static SendEmail instance = new SendEmail();

	public static SendEmail getInstance()
	{
		return instance;
	}

	protected SendEmail()
	{
	}

	/*
	 * private void createMessage( EmailMessage emailMsg,
	 * AbstractEmailAttachment[] list) throws XASException {
	 * 
	 * InitialContext ctx; Session mailSession; MimeMessage msg;
	 * 
	 * SmartAnalytic myLog = null; try { myLog =
	 * (SmartAnalytic)SmartLogFactory.getInstance
	 * ().createAnalyticWithoutId(this); //reference lookup ctx = new
	 * InitialContext(); mailSession = (Session)
	 * ctx.lookup("java:comp/env/mail/default");
	 * 
	 * mailSession.setDebug(true); log.info(" mailFrom[" +
	 * emailMsg.getMailFrom() + "]" + " replyTo[" + emailMsg.getReplyTo() + "]"
	 * + " mailTo[" + emailMsg.getMailTo() + "]" + " mailCc[" +
	 * emailMsg.getMailCc() + "]" + " mailBcc[" + emailMsg.getMailBcc() + "]" +
	 * " mailSubject[" + emailMsg.getMailSubject() + "]");
	 * 
	 * //Define message msg = new MimeMessage(mailSession);
	 * //msg.setHeader("Disposition-Notification-To",
	 * "fabio.santagostinobietti@unicreditgroup.eu"); // read receipt
	 * msg.setFrom( new InternetAddress(emailMsg.getMailFrom(),
	 * emailMsg.getMailFromName()) ); if (emailMsg.getReplyTo() != null)
	 * msg.setReplyTo( InternetAddress.parse(emailMsg.getReplyTo()) ); if
	 * (emailMsg.getMailTo() != null) msg.setRecipients(
	 * Message.RecipientType.TO, InternetAddress.parse(emailMsg.getMailTo()));
	 * if (emailMsg.getMailCc() != null) msg.setRecipients(
	 * Message.RecipientType.CC, InternetAddress.parse(emailMsg.getMailCc()));
	 * if (emailMsg.getMailBcc() != null) msg.setRecipients(
	 * Message.RecipientType.BCC, InternetAddress.parse(emailMsg.getMailBcc()));
	 * if (emailMsg.getMailSubject() != null)
	 * msg.setSubject(emailMsg.getMailSubject());
	 * 
	 * //Create the body message part MimeBodyPart msgBody = new MimeBodyPart();
	 * if (emailMsg.isHtml()) { msgBody.setContent(emailMsg.getMailMessage(),
	 * "text/html"); } else { msgBody.setContent(emailMsg.getMailMessage(),
	 * "text/plain"); }
	 * 
	 * // Create message part and fill messages Multipart msgMultipart = new
	 * MimeMultipart(); msgMultipart.addBodyPart(msgBody);
	 * 
	 * if (list != null) { //Create body parts for each file MultipartAppender
	 * appender = new MultipartAppender();
	 * appender.setMsgMultipart(msgMultipart); for (int i = 0; i < list.length;
	 * ++i) { AbstractEmailAttachment att = list[i]; att.apply(appender); } }
	 * //Put parts in message msg.setContent(msgMultipart); //Send the message
	 * Transport.send(msg); int i = 3; //if (i == 1 * 3) throw new
	 * MessagingException("Errore provocato"); logData(myLog,emailMsg); } catch
	 * (AddressException e) { //log.error(e.getMessage(), e);
	 * logError(myLog,emailMsg); throw new XASException(ADRESS_EXCEPTION_CODE,
	 * e); } catch (NamingException e) { logError(myLog,emailMsg);
	 * //log.error(e.getMessage(), e); throw new
	 * XASException(NAMING_EXCEPTION_CODE, e); } catch (MessagingException e) {
	 * logError(myLog,emailMsg); //log.error(e.getMessage(), e); throw new
	 * XASException(MESSAGE_EXCEPTION_CODE, e); } catch
	 * (UnsupportedEncodingException e) { logError(myLog,emailMsg);
	 * //e.printStackTrace(); throw new XASException(e); } catch (SLF_Exception
	 * e) { //e.printStackTrace(); throw new XASException(e); } }
	 */

	/**
	 * @param emailMsg
	 * @param attachemnts
	 */
	public void sendMessage(EmailMessage emailMsg, AbstractEmailAttachment[] list, String userid,
			boolean isUserInRole) throws XASException
	{
		Chronometer chrono = new Chronometer(true);
		SmartAnalytic myLog = null;

		try
		{
			myLog = (SmartAnalytic) SmartLogFactory.getInstance().createAnalyticWithoutId(this);
			myLog.collectIt(ConstantsSplunk.K_FUNCTION, ConstantsSplunk.V_FUNCTION_SENDMESSAGE /* SplunkConstants */
			, ConstantsSplunk.TMP_USERID, userid /* SplunkConstants */
			, ConstantsSplunk.TMP_ISUSERINROLE, Boolean.toString(isUserInRole) /* SplunkConstants */
			);
			MimeMessage msg = generateMessage(emailMsg, list);
			// Send the message
			Transport.send(msg);
			chrono.stop();
			myLog.collectIt(SmartAnalytic.K_TIME_ELAPSED,
					it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chrono.getElapsed()));
			logData(myLog, emailMsg, list);
		} catch (AddressException e)
		{
			chrono.stop();
			myLog.collectIt(SmartAnalytic.K_TIME_ELAPSED, it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chrono.getElapsed()));
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.ADDRESS_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.ADDRESS_EXCEPTION_MESSAGE, e);
		} catch (NamingException e)
		{
			chrono.stop();
			myLog.collectIt(SmartAnalytic.K_TIME_ELAPSED,it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chrono.getElapsed()));
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.NAMING_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.NAMING_EXCEPTION_MESSAGE, e);
		} catch (MessagingException e)
		{
			chrono.stop();
			myLog.collectIt(SmartAnalytic.K_TIME_ELAPSED, it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chrono.getElapsed()));
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.MESSAGE_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.MESSAGE_EXCEPTION_MESSAGE, e);
		} catch (UnsupportedEncodingException e)
		{
			chrono.stop();
			myLog.collectIt(SmartAnalytic.K_TIME_ELAPSED, it.usi.xframe.xas.bfimpl.sms.Chronometer.formatMillis(chrono.getElapsed()));
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, "UnsupportedEncodingException " + e.getMessage(), list);
			throw new XASException(e);
		} catch (SLF_Exception e)
		{
			throw new XASException(e);
		}
	}

	public byte[] exportMessage(EmailMessage emailMsg, AbstractEmailAttachment[] list, String userid,
			boolean isUserInRole) throws XASException
	{

		SmartAnalytic myLog = null;
		try
		{
			myLog = (SmartAnalytic) SmartLogFactory.getInstance().createAnalyticWithoutId(this);
			myLog.collectIt(ConstantsSplunk.K_FUNCTION, ConstantsSplunk.V_FUNCTION_EXPORTMESSAGE /* ConstantsSplunk */
			, ConstantsSplunk.TMP_USERID, userid /* SplunkConstants */
			, ConstantsSplunk.TMP_ISUSERINROLE, Boolean.toString(isUserInRole) /* SplunkConstants */
			);
			MimeMessage msg = generateMessage(emailMsg, list);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			msg.writeTo(stream);
			logData(myLog, emailMsg, list);
			return stream.toByteArray();
		} catch (AddressException e)
		{
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.ADDRESS_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.ADDRESS_EXCEPTION_MESSAGE, e);
		} catch (NamingException e)
		{
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.NAMING_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.NAMING_EXCEPTION_MESSAGE, e);
		} catch (MessagingException e)
		{
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, ConstantsEmail.MESSAGE_EXCEPTION_MESSAGE + " " + e.getMessage(), list);
			throw new XASException(ConstantsEmail.MESSAGE_EXCEPTION_MESSAGE, e);
		} catch (UnsupportedEncodingException e)
		{
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, "UnsupportedEncodingException " + e.getMessage(), list);
			throw new XASException(e);
		} catch (IOException e)
		{
			logError(myLog, emailMsg, ConstantsEmail.MESSAGE_ERROR_CODE, "IOException", list);
			throw new XASException(e);
		} catch (SLF_Exception e)
		{
			throw new XASException(e);
		}

	}

	private MimeMessage generateMessage(EmailMessage emailMsg, AbstractEmailAttachment[] list)
			throws NamingException, UnsupportedEncodingException, MessagingException
	{
		final String DEFAULT_CHARSET = "UTF-8";
		InitialContext ctx = new InitialContext();
		Session mailSession = (Session) ctx.lookup(ConstantsEmail.MAILSRV_JNDI);
		mailSession.setDebug(true);
		MimeMessage msg = new MimeMessage(mailSession);
		// msg.setHeader("Disposition-Notification-To",
		// "fabio.santagostinobietti@unicreditgroup.eu"); // read receipt
		msg.setFrom(new InternetAddress(emailMsg.getMailFrom(), emailMsg.getMailFromName()));
		if (emailMsg.getReplyTo() != null)
			msg.setReplyTo(InternetAddress.parse(emailMsg.getReplyTo()));
		if (emailMsg.getMailTo() != null)
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMsg.getMailTo()));
		if (emailMsg.getMailCc() != null)
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailMsg.getMailCc()));
		if (emailMsg.getMailBcc() != null)
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailMsg.getMailBcc()));
		if (emailMsg.getMailSubject() != null)
			msg.setSubject(emailMsg.getMailSubject(), DEFAULT_CHARSET);

		// Create the body message part
		MimeBodyPart msgBody = new MimeBodyPart();
		if (emailMsg.isHtml())
		{
			msgBody.setContent(emailMsg.getMailMessage(), "text/html; charset=" + DEFAULT_CHARSET);
		} else
		{
			msgBody.setContent(emailMsg.getMailMessage(), "text/plain; charset=" + DEFAULT_CHARSET);
		}

		// Create message part and fill messages
		Multipart msgMultipart = new MimeMultipart();
		msgMultipart.addBodyPart(msgBody);

		if (list != null)
		{
			// Create body parts for each file
			MultipartAppender appender = new MultipartAppender();
			appender.setMsgMultipart(msgMultipart);
			for (int i = 0; i < list.length; ++i)
			{
				AbstractEmailAttachment att = list[i];
				att.apply(appender);
			}
		}
		// Put parts in message
		msg.setContent(msgMultipart);
		return msg;

	}

	private void logData(SmartAnalytic smartLog, EmailMessage msg, AbstractEmailAttachment[] list)
			throws SLF_Exception
	{
		smartLog.collectIt(SmartLog.K_STATUS_CODE, ConstantsEmail.MESSAGE_OK_CODE, SmartLog.K_STATUS_MSG, ConstantsEmail.MESSAGE_OK);
		logEmailData(smartLog, msg, list);
		log.info(smartLog.getAnalyticRow());
	}

	private void logError(SmartAnalytic smartLog, EmailMessage msg, String errorCode, String errorMessage,
			AbstractEmailAttachment[] list)
	{
		try
		{
			smartLog.collectIt(SmartLog.K_STATUS_CODE, errorCode, SmartLog.K_STATUS_MSG, errorMessage);
			logEmailData(smartLog, msg, list);
			log.error(smartLog.getAnalyticRow());
		} catch (SLF_Exception e)
		{
			e.printStackTrace(System.out);
			log.error("SmartLogException while handling Exception: " + e.getMessage());
		}

	}

	// #325
	private static String K_MAILFROM = "a_mailFrom", K_MAILTO = "a_mailTo", K_REPLYTO = "a_replyTo",
			K_MAILCC = "a_mailCc", K_MAILBCC = "a_mailBcc", K_MAILSUBJECT = "a_mailSubject";
	private static String K_MAILFROM_ENC = "a_mailFrom_enc", K_MAILTO_ENC = "a_mailTo_enc",
			K_REPLYTO_ENC = "a_replyTo_enc", K_MAILCC_ENC = "a_mailCc_enc", K_MAILBCC_ENC = "a_mailBcc_enc",
			K_MAILSUBJECT_ENC = "a_mailSubject_enc";

	private void logEmailData(SmartAnalytic smartLog, EmailMessage msg, AbstractEmailAttachment[] list)
	{
		// #325
		final String ANONYMIZE_KEY = "Hitchhiker";
		final String[] SENSIBLE_DATA_KEYS =
		{K_MAILFROM_ENC, K_MAILTO_ENC, K_REPLYTO_ENC, K_MAILCC_ENC, K_MAILBCC_ENC, K_MAILSUBJECT_ENC};

		smartLog.collectIt(K_MAILFROM, msg.getMailFrom(), K_MAILTO, msg.getMailTo(), K_REPLYTO,
				msg.getReplyTo(), K_MAILCC, msg.getMailCc(), K_MAILBCC, msg.getMailBcc(), K_MAILSUBJECT,
				msg.getMailSubject(), ConstantsSplunk.TMP_ISHTML, Boolean.toString(msg.isHtml()) /* SplunkConstants */
		);
		smartLog.anonymize(ANONYMIZE_KEY, SENSIBLE_DATA_KEYS); // #325
		smartLog.collectIt(K_MAILFROM_ENC, msg.getMailFrom(), K_MAILTO_ENC, msg.getMailTo(), K_REPLYTO_ENC,
				msg.getReplyTo(), K_MAILCC_ENC, msg.getMailCc(), K_MAILBCC_ENC, msg.getMailBcc(),
				K_MAILSUBJECT_ENC, msg.getMailSubject());

		for (int i = 0; i < (list != null ? list.length : 0); ++i)
		{
			smartLog.collectIt(ConstantsSplunk.TMP_ATTACHMENTDESC + i, list[i].getAttachmentDescription() /* SplunkConstants */
			, ConstantsSplunk.TMP_ATTACHMENTMIME + i, list[i].getAttachmentMimeType()); /* SplunkConstants */
		}

	}

}
