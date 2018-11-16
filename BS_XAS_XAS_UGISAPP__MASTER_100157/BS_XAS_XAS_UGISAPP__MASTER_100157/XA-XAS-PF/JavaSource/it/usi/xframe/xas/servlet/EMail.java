package it.usi.xframe.xas.servlet;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasEmailServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.EmailMessage;
import it.usi.xframe.xas.bfutil.data.FileEmailAttachment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 	1.0
 * @author
 */
public class EMail extends HttpServlet implements Servlet {
	private static Log log = LogFactory.getLog(EMail.class);
	private final String MY_UUID = UUID.randomUUID().toString();
	
	private static final long serialVersionUID = 5L;
	public static final String SERVLETNAME = "email";
	public static final String ACTION = "action";
	public static final String ACTION_SENDEMAIL = "sendemail";
	public static final String ACTION_EXPORTEMAIL = "exportemail";

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		this.execute(req, resp);
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		this.execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String page = req.getParameter("page");
			if (page == null) {
				pageHeader(req, resp);
				pageNull(req, resp);
				pageFooter(req, resp);
			} else if (page.compareTo(ACTION_EXPORTEMAIL) == 0){
				exportEMail(req, resp);
			}		
			else if (page.compareTo(ACTION_SENDEMAIL) == 0){
				pageHeader(req, resp);
				sendEMail(req, resp);
				pageFooter(req, resp);
			} else {
				pageHeader(req, resp);
				pageNull(req, resp);
				pageFooter(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * 
	 * return an array with all test medhods
	 */
	public static String[] getMethods() {
		List actionList = new ArrayList();
		actionList.add(ACTION_SENDEMAIL);
		actionList.add(ACTION_EXPORTEMAIL);
		String[] stringArray = (String[]) actionList.toArray(new String[actionList.size()]);
		return stringArray;
	}

	private void sendEMail(HttpServletRequest req, HttpServletResponse resp) {
		
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "use_logReferrer", MY_UUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0); 
		
		String method = ACTION_SENDEMAIL;

		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String from = req.getParameter("from");
		if (from == null)
			from = "from@domain.com";
		String to = req.getParameter("to");
		if (to == null)
			to = "to@domain.com";
		String cc = req.getParameter("cc");
		if (cc == null)
			cc = "cc@domain.com";
		String ccn = req.getParameter("ccn");
		if (ccn == null)
			ccn = "ccn@domain.com";
		String subject = req.getParameter("subject");
		if (subject == null)
			subject = "subject";
		String message = req.getParameter("message");
		if (message == null)
			message = new Date() + ": test message ";
		String attachment = req.getParameter("attachment");
		if (attachment == null)
			attachment = "x:\\email.txt";
		
		Boolean bHtml = new Boolean("true".equals(req.getParameter("html")));
		
		log.debug(sl.logIt("a_subject", subject,"a_message", message).getLogRow());

		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println(
				"<form action='"
					+ req.getContextPath()
					+ "/"
					+ SERVLETNAME
					+ "' method='GET'>");
			out.println(
				"<input type='hidden' name='page' value='" + method + "' />");
			out.println(
				"<input type='hidden' name='action' value='" + method + "' />");
			addField(req, resp, "from", from);
			addField(req, resp, "to", to);
			addField(req, resp, "cc", cc);
			addField(req, resp, "ccn", ccn);
			addField(req, resp, "subject", subject);
			// addField(req, resp, "message", message);
			String field="message", value=message;
			out.println(
				field
					+ ":<textarea rows='10' cols='100' name='"
					+ field
					+ "'>"+value+"</textarea><br />");

			
			addField(req, resp, "attachment", attachment);
			addField(req, resp, "html", bHtml.toString(), "false,true".split(","), true);			
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");

			if (action.compareTo(method) == 0)
			{
				FileEmailAttachment[] attachments = null;
				if(attachment.length()!=0)
				{
					attachments = new FileEmailAttachment[1];
					attachments[0] = new FileEmailAttachment(attachment, "text/plain", attachment);
				}				
				EmailMessage email = new EmailMessage();
				email.setMailFrom(from);
				email.setMailTo(to);
				email.setMailCc(cc);
				email.setMailBcc(ccn);
				email.setMailSubject(subject);
				email.setMailMessage(message);
				email.setHtml(bHtml.booleanValue());
				IXasEmailServiceFacade service = XasServiceFactory.getInstance().getXasEmailServiceFacade();
				service.sendEmailMessage(email, attachments);
				
				this.pageSmsSentSuccesfully(req, resp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceFactoryException e) {
			e.printStackTrace();
		} catch (XASException e) {
		   this.pageSmsSentError(req, resp, e);
		}
	}

	private void pageSmsSentError(
		HttpServletRequest req,
		HttpServletResponse resp,
		Exception err) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("EMAIL has not been sent.<br>");
			out.print("Error code: " + err.getMessage() + "<br>");
			out.print("Error description: " + err.getCause().getMessage() + "<br>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pageSmsSentSuccesfully(
		HttpServletRequest req,
		HttpServletResponse resp) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("EMAIL sent succesfully<br>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pageNull(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("method unknown");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pageHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		final String DEFAULT_CHARSET = "UTF-8";

		resp.setHeader("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        //resp.setHeader("Content-Disposition","attachment;filename=myFile.aaa");
        //resp.setCharacterEncoding("UTF-8");   BASELINE BUILD NON RICONOSCE IL METODO
        
        PrintWriter out = resp.getWriter();
		String user = req.getParameter("user");
		if (user == null)
			user = "";
		String password = req.getParameter("password");
		if (password == null)
			password = "";
		String reqString = "&user=" + user + "&password=" + password;
		resp.setContentType("text/html; charset=" + DEFAULT_CHARSET);
		out.println("<html>");
		out.println("<head>");
		out.println("<style type='text/css'>");
		out.println(
			"body {font-size:12px;font-family:Arial, Helvetica, sans-serif;}");
		out.println(
			"table {border-width: 1px; border-spacing: 1px; border-style: outset; border-collapse: separate; border-color: gray; background-color: white; font-size:12px; font-family:Arial, Helvetica, sans-serif;}");
		out.println(
			"table th {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 240, 230); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println(
			"table td {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 250, 250); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("METHODS:");
		out.println(
			"<a href='"
				+ req.getContextPath()
				+ "/"
				+ SERVLETNAME
				+ "?page="
				+ ACTION_SENDEMAIL
				+ reqString
				+ "'>"
				+ ACTION_SENDEMAIL
				+ "</a>&nbsp;");
		out.println(
				"<br/><a href='"
					+ req.getContextPath()
					+ "/"
					+ SERVLETNAME
					+ "?page="
					+ ACTION_EXPORTEMAIL
					+ reqString
					+ "'>"
					+ ACTION_EXPORTEMAIL
					+ "</a>&nbsp;");
		out.println("<hr />");
	}

	public void pageFooter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String srvname = req.getServerName();
		int srvport = req.getServerPort();
		PrintWriter out = resp.getWriter();
		out.print("Server name: " + srvname + " Server port: " + srvport);
		out.println("</body></html>");
	}

	public void addField(
		HttpServletRequest req,
		HttpServletResponse resp,
		String field,
		String value)
		throws IOException {
		PrintWriter out = resp.getWriter();
		out.println(
			field
				+ ":<input type='text' name='"
				+ field
				+ "' value='"
				+ value
				+ "' /><br />");
	}
	public void addField(HttpServletRequest req, HttpServletResponse resp,
			String field, String value,	String[] options,
			boolean br)	throws IOException {
			
			PrintWriter out = resp.getWriter();
			if (options != null) {
				out.print(field + ":<select name='" + field + "'>");
				for (int i = 0; i < options.length; i++) {
					out.print("<option value='" + options[i] + "'" + (options[i].equals(value) ? "SELECTED" : "") + ">" + options[i] + "</option>");
				}
				out.print("</select>");
			} else {
				out.print(field	+ ":<input type='text' name='"	+ field + "' value='" + value + "' />");
			}
			if(br) {
				out.println("<br />");
			}
		}
		
	
	private void exportEMail(HttpServletRequest req, HttpServletResponse resp)
	{
		String method = ACTION_EXPORTEMAIL;

		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String from = req.getParameter("from");
		if (from == null)
			from = "from@domain.com";
		String to = req.getParameter("to");
		if (to == null)
			to = "to@domain.com";
		String cc = req.getParameter("cc");
		if (cc == null)
			cc = "cc@domain.com";
		String ccn = req.getParameter("ccn");
		if (ccn == null)
			ccn = "ccn@domain.com";
		String subject = req.getParameter("subject");
		if (subject == null)
			subject = "subject";
		String message = req.getParameter("message");
		if (message == null)
			message = new Date() + ": test message ";
		String attachment = req.getParameter("attachment");
		if (attachment == null)
		attachment = "x:\\email.txt";

		try
		{

			if (action.compareTo(method) == 0)
			{
				FileEmailAttachment[] attachments = null;
				if(attachment.length()!=0)
				{
					attachments = new FileEmailAttachment[1];
					attachments[0] = new FileEmailAttachment(attachment, "text/plain", attachment);
				}				
				EmailMessage email = new EmailMessage();
				email.setMailFrom(from);
				email.setMailTo(to);
				email.setMailCc(cc);
				email.setMailBcc(ccn);
				email.setMailSubject(subject);
				email.setMailMessage(message);
				IXasEmailServiceFacade service = XasServiceFactory.getInstance().getXasEmailServiceFacade();
				byte[] output = service.exportEmailMessage(email, attachments);
				resp.setContentType("application/vnd.ms-outlook");
				resp.setContentLength(output.length);
				resp.setHeader("Content-Disposition","attachment; filename=Email.eml");
				//PrintWriter out = resp.getWriter();
				OutputStream out = resp.getOutputStream();
				out.write(output);
				//this.pageSmsSentSuccesfully(req, resp);
			} else {
				pageHeader(req, resp);
				PrintWriter out = resp.getWriter();
				out.println(method + "<br/>");
				out.println(
					"<form action='"
						+ req.getContextPath()
						+ "/"
						+ SERVLETNAME
						+ "' method='GET'>");
				out.println(
					"<input type='hidden' name='page' value='" + method + "' />");
				out.println(
					"<input type='hidden' name='action' value='" + method + "' />");
				addField(req, resp, "from", from);
				addField(req, resp, "to", to);
				addField(req, resp, "cc", cc);
				addField(req, resp, "ccn", ccn);
				addField(req, resp, "subject", subject);
				addField(req, resp, "message", message);
				addField(req, resp, "attachment", attachment);
				out.println("<input type='submit' value='submit' /><br />");
				out.println("</form>");
				out.println("<hr />");
				pageFooter(req, resp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceFactoryException e) {
			e.printStackTrace();
		} catch (XASException e) {
		   this.pageSmsSentError(req, resp, e);
		}
	}

	
}
