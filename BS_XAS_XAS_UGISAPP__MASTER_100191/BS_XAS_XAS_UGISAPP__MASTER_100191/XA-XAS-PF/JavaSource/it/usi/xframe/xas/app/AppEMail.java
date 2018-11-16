package it.usi.xframe.xas.app;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasEmailServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.EmailMessage;
import it.usi.xframe.xas.bfutil.data.FileEmailAttachment;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Radio;
import it.usi.xframe.xas.html.fragments.javaobject.RadioGroup;
import it.usi.xframe.xas.html.fragments.javaobject.TextArea;
import it.usi.xframe.xas.html.fragments.javaobject.Toogle;
import it.usi.xframe.xas.servlet.ServletBase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 	1.0
 * @author
 */
public class AppEMail extends BaseApplication
{
	private static Log logger = LogFactory.getLog(AppEMail.class);
	private final String MY_UUID = UUID.randomUUID().toString();

	private static final long serialVersionUID = 5L;

	public static final String APPLICATIONNAME			= "email";
	public static final String APPLICATIONTITLE			= "EMail";
	public static final String ACTION_VALUE_SENDEMAIL 	= "sendemail";
	public static final String ACTION_VALUE_EXPORTEMAIL = "exportemail";


	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseApplication#getApplicationName()
	 */
	public String getName() {
		return APPLICATIONNAME;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseApplication#getApplicationTitle()
	 */
	public String getTitle() {
		return APPLICATIONTITLE;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseApplication#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, it.usi.xframe.xas.html.fragments.javaobject.Page)
	 */
	public void execute(HttpServletRequest req, HttpServletResponse resp, Page page)
	{
		try
		{
			String method = getRequestParameter(req, METHOD_KEY, "");
			String action =getRequestParameter(req,ACTION_KEY, "");

			if (method == null || method.compareTo("") == 0)
				methodNull();
			else if (method.compareTo(ACTION_VALUE_SENDEMAIL) == 0)
				method_sendEMail(req, resp, page, method, action);
			else if (method.compareTo(ACTION_VALUE_EXPORTEMAIL) == 0)
				method_exportEMail(req, resp, page, method, action);
			else
				methodUknown(page);
		}
		catch (Exception e) {
			e.printStackTrace();
			this.pageError(page, e);
		}
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseApplication#prepareMethodList()
	 */
	protected ApplicationMethod[] prepareMethodList()
	{
		List methodList = new ArrayList();
		methodList.add(new ApplicationMethod(ACTION_VALUE_SENDEMAIL, "Send EMail", new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.EMAIL)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_EXPORTEMAIL, ACTION_VALUE_EXPORTEMAIL, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.EMAIL)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}

	/**
	 *
	 */
	private class DataLogSendEmail extends MethodData
	{
		public final String FROM				= "from";
		public final String TO					= "to";
		public final String CC					= "cc";
		public final String CCN					= "ccn";
		public final String SUBJECT				= "subject";
		public final String MESSAGE				= "message";
		public final String ATTACHMENT			= "attachment";
		public final String TOOGLEATTACHMENT	= "toogleattachment";
		public final String HTML				= "html";
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_sendEMail(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{		
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, "use_logReferrer", MY_UUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0); 
		
		String current_action = ACTION_VALUE_SENDEMAIL;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataLogSendEmail data = new DataLogSendEmail();
		
		data.set(data.FROM, getRequestParameter(req,data.FROM, null), "from@domain.com");
		data.set(data.TO, getRequestParameter(req,data.TO, null), "to@domain.com");
		data.set(data.CC, getRequestParameter(req,data.CC, null), "cc@domain.com");
		data.set(data.CCN, getRequestParameter(req,data.CCN, null), "ccn@domain.com");
		data.set(data.SUBJECT,  getRequestParameter(req,data.SUBJECT, null), "subject");
		data.set(data.MESSAGE,  getRequestParameter(req,data.MESSAGE, null), new Date() + ": test message");
		data.set(data.TOOGLEATTACHMENT,  getRequestParameter(req,data.TOOGLEATTACHMENT, null), Toogle.STATUS_OFF);
		data.set(data.ATTACHMENT,  getRequestParameter(req,data.ATTACHMENT, null), "x:\\email.txt");
		boolean bool_attachment = Toogle.STATUS_ON.equals(data.getString(data.TOOGLEATTACHMENT));
		data.set(data.HTML,  getRequestParameter(req,data.HTML, null), "false");

		if (logger.isDebugEnabled())
			data.dump();
		
		logger.debug(sl.logIt("a_subject", data.getString(data.SUBJECT),"a_message", data.getString(data.MESSAGE)).getLogRow());

		try
		{
			Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "EMail:");
			form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
			form.add(new Field(data.FROM, data.getString(data.FROM), "From", FieldType.TYPE_EMAIL));
			form.add(new Field(data.TO, data.getString(data.TO), "To", FieldType.TYPE_EMAIL));
			form.add(new Field(data.CC, data.getString(data.CC), "CC", FieldType.TYPE_EMAIL));
			form.add(new Field(data.CCN, data.getString(data.CCN), "CCN", FieldType.TYPE_EMAIL));
			form.add(new Field(data.SUBJECT, data.getString(data.SUBJECT), "Subject", FieldType.TYPE_TEXT));
			form.add(new TextArea(data.MESSAGE, data.getString(data.MESSAGE), "Message", 10));
//			+ ":<textarea rows='10' cols='100' name='"
			Toogle toogle_attachment = new Toogle(data.TOOGLEATTACHMENT, bool_attachment, "");
			toogle_attachment.setDisabledFields(new String[] {data.ATTACHMENT});
			Field field = new Field(data.ATTACHMENT, data.getString(data.ATTACHMENT), "Attachment", FieldType.TYPE_TEXT);
			field.add(toogle_attachment, true);
			field.setEnabled(false);
			form.add(field);
			RadioGroup radiogroup = new RadioGroup("", "Html"); 
			radiogroup.add(new Radio("html_yes", "html", "true", "yes", "true".equals(data.getString(data.HTML)) ? true : false));
			radiogroup.add(new Radio("html_no", "html", "false", "no", "false".equals(data.getString(data.HTML)) ? true : false));
			form.add(radiogroup);
			container.add(form);
			
			if (action.compareTo(method) == 0)
			{
				Boolean bHtml = new Boolean("true".equals(data.getString(data.HTML)));
				FileEmailAttachment[] attachments = null;
				if(bool_attachment && data.getString(data.ATTACHMENT).length()!=0)
				{
					attachments = new FileEmailAttachment[1];
					attachments[0] = new FileEmailAttachment(data.getString(data.ATTACHMENT), "text/plain", data.getString(data.ATTACHMENT));
				}				
				EmailMessage email = new EmailMessage();
				email.setMailFrom(data.getString(data.FROM));
				email.setMailTo(data.getString(data.TO));
				email.setMailCc(data.getString(data.CC));
				email.setMailBcc(data.getString(data.CCN));
				email.setMailSubject(data.getString(data.SUBJECT));
				email.setMailMessage(data.getString(data.MESSAGE));
				email.setHtml(bHtml.booleanValue());
				IXasEmailServiceFacade service = XasServiceFactory.getInstance().getXasEmailServiceFacade();
				service.sendEmailMessage(email, attachments);
				
				this.pageSmsSentSuccesfully(page);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceFactoryException e) {
			e.printStackTrace();
		} catch (XASException e) {
		   this.pageSmsSentError(page, e);
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_exportEMail(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_EXPORTEMAIL;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataLogSendEmail data = new DataLogSendEmail();

		data.set(data.FROM, getRequestParameter(req,data.FROM, null), "from@domain.com");
		data.set(data.TO, getRequestParameter(req,data.TO, null), "to@domain.com");
		data.set(data.CC, getRequestParameter(req,data.CC, null), "cc@domain.com");
		data.set(data.CCN, getRequestParameter(req,data.CCN, null), "ccn@domain.com");
		data.set(data.SUBJECT, getRequestParameter(req,data.SUBJECT, null), "subject");
		data.set(data.MESSAGE, getRequestParameter(req,data.MESSAGE, null), new Date() + ": test message");
		data.set(data.TOOGLEATTACHMENT, getRequestParameter(req,data.TOOGLEATTACHMENT, null), Toogle.STATUS_OFF);
		data.set(data.ATTACHMENT, getRequestParameter(req,data.ATTACHMENT, null), "x:\\email.txt");
		boolean bool_attachment = Toogle.STATUS_ON.equals(data.getString(data.TOOGLEATTACHMENT));

		if (logger.isDebugEnabled())
			data.dump();

		try
		{
			if (action.compareTo(method) == 0)
			{
				FileEmailAttachment[] attachments = null;
				if(bool_attachment && data.getString(data.ATTACHMENT).length()!=0)
				{
					attachments = new FileEmailAttachment[1];
					attachments[0] = new FileEmailAttachment(data.getString(data.ATTACHMENT), "text/plain", data.getString(data.ATTACHMENT));
				}				
				EmailMessage email = new EmailMessage();
				email.setMailFrom(data.getString(data.FROM));
				email.setMailTo(data.getString(data.TO));
				email.setMailCc(data.getString(data.CC));
				email.setMailBcc(data.getString(data.CCN));
				email.setMailSubject(data.getString(data.SUBJECT));
				email.setMailMessage(data.getString(data.MESSAGE));
				IXasEmailServiceFacade service = XasServiceFactory.getInstance().getXasEmailServiceFacade();
				byte[] output = service.exportEmailMessage(email, attachments);

				// tell to page object to skip serialization to avoid conflict with resp.getOutputStream();
				page.skipSerialization(true);

				// write binary stream to response outputStream
				resp.setContentType("application/vnd.ms-outlook");
				resp.setContentLength(output.length);
				resp.setHeader("Content-Disposition","attachment; filename=Email.eml");
				OutputStream out = resp.getOutputStream();
				out.write(output);
				out.close();
				//this.pageSmsSentSuccesfully(req, resp);
			}
			else
			{
				Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "EMail:");
				form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
				form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
				form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
				form.add(new Field(data.FROM, data.getString(data.FROM), "From", FieldType.TYPE_EMAIL));
				form.add(new Field(data.TO, data.getString(data.TO), "To", FieldType.TYPE_EMAIL));
				form.add(new Field(data.CC, data.getString(data.CC), "CC", FieldType.TYPE_EMAIL));
				form.add(new Field(data.CCN, data.getString(data.CCN), "CCN", FieldType.TYPE_EMAIL));
				form.add(new Field(data.SUBJECT, data.getString(data.SUBJECT), "Subject", FieldType.TYPE_TEXT));
				form.add(new TextArea(data.MESSAGE, data.getString(data.MESSAGE), "Message", 10));
				Toogle toogle_attachment = new Toogle(data.TOOGLEATTACHMENT, bool_attachment, Toogle.STATUS_OFF);
				toogle_attachment.setDisabledFields(new String[] {data.ATTACHMENT});
				Field field = new Field(data.ATTACHMENT, data.getString(data.ATTACHMENT), "Attachment", FieldType.TYPE_TEXT);
				field.add(toogle_attachment, true);
				field.setEnabled(false);
				form.add(field);
				
				container.add(form);

				// remove spinner to avoid screen remain gray after form submission
				page.detach(ServletBase.OBJECT_SPINNER);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceFactoryException e) {
			e.printStackTrace();
		} catch (XASException e) {
		   this.pageSmsSentError(page, e);
		}
	}

	/**
	 * @param page
	 * @param err
	 */
	private void pageSmsSentError(Page page, Exception err)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("EMAIL has not been sent.\n");
		ret.append("Error code: " + err.getMessage() + "\n");
		ret.append("Error description: " + err.getCause().getMessage());
		page.add(new Alert(null, ret.toString(), AlertType.DANGER), true);
	}

	/**
	 * @param page
	 */
	private void pageSmsSentSuccesfully(Page page)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("EMAIL sent succesfully.");
		page.add(new Alert(null, ret.toString(), AlertType.INFO), true);

	}
}
