package it.usi.xframe.xas.servlet;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author
 */
public class SmsInternational extends HttpServlet implements Servlet
{
	private static final long serialVersionUID = 1L;
	public static final String SERVLETNAME = "smsinternational";
	public static final String ACTION = "action";
	public static final String ACTION_SENDSMS = "sendinternationalsms";
	// public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *      (javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		this.execute(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *      (javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		this.execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp)
	{
		try
		{
			String charEnc = req.getCharacterEncoding();
			req.setCharacterEncoding(PAGE_CHARSET);
			charEnc = req.getCharacterEncoding();
			// resp.setCharacterEncoding(PAGE_CHARSET);
			pageHeader(req, resp);
			String page = req.getParameter("page");
			if (page == null)
				pageNull(req, resp);
			else if (page.compareTo(ACTION_SENDSMS) == 0)
				sendSms(req, resp);
			else
				pageNull(req, resp);
			pageFooter(req, resp);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * 
	 *         return an array with all test medhods
	 */
	public static String[] getMethods()
	{
		List actionList = new ArrayList();
		actionList.add(ACTION_SENDSMS);
		String[] stringArray = (String[]) actionList.toArray(new String[actionList.size()]);
		return stringArray;
	}

	private void sendSms(HttpServletRequest req, HttpServletResponse resp)
	{
		String method = ACTION_SENDSMS;
		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String number = req.getParameter("number");
		if (number == null)
			number = "39335******";
		String message = req.getParameter("message");
		if (message == null)
			message = "";
		String legalentity = req.getParameter("legalentity");
		if (legalentity == null)
			legalentity = "UBIS";
		String servicename = req.getParameter("servicename");
		if (servicename == null)
			servicename = "PINSAFE";

		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME
					+ "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + method + "' />");
			out.println("<input type='hidden' name='action' value='" + method + "' />");
			addField(req, resp, "number", number, true);
			addField(req, resp, "message", message, false);
			out.println("&nbsp;example:Hello Ë<br />");
			addField(req, resp, "legalentity", legalentity, true);
			addField(req, resp, "servicename", servicename, true);
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			if (action.compareTo(method) == 0)
			{
				InternationalSmsMessage sms = new InternationalSmsMessage();
				sms.setText(message);

				SmsDelivery delivery = new SmsDelivery();
				delivery.setPhoneNumber(number);

				SmsBillingInfo billing = new SmsBillingInfo();
				billing.setLegalEntity(legalentity);
				billing.setServiceName(servicename);

				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance()
						.getXasSendsmsServiceFacade();
				InternationalSmsResponse result = service.sendInternationalSms(sms, delivery,
						billing);
				if (result.getCode() == InternationalSmsResponse.OK)
					this.pageSmsSentSuccesfully(req, resp, sms, delivery);
				else
					this.pageSmsSentError(req, resp, result);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		} catch (XASException e)
		{
			this.pageSmsSentError(req, resp, e);
		}
	}

	private void pageSmsSentError(HttpServletRequest req, HttpServletResponse resp,
			InternationalSmsResponse err)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS has not been sent.<br>");
			out.print("Error code: " + err.getCode() + "<br>");
			out.print("Error description: " + err.getDescr() + "<br>");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pageSmsSentError(HttpServletRequest req, HttpServletResponse resp, Exception err)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS has not been sent.<br>");
			out.print("Error code: " + err.getMessage() + "<br>");
			out.print("Error description: " + err.getCause().getMessage() + "<br>");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pageSmsSentSuccesfully(HttpServletRequest req, HttpServletResponse resp,
			InternationalSmsMessage sms, SmsDelivery delivery)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS sent succesfully<br>");
			out.print("Phone number: " + delivery.getPhoneNumber() + "<br>");
			out.print("Message: " + sms.getText() + "<br>");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pageNull(HttpServletRequest req, HttpServletResponse resp)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("method unknown");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pageHeader(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		resp.setContentType("text/html;charset=" + PAGE_CHARSET);
		PrintWriter out = resp.getWriter();
		String user = req.getParameter("user");
		if (user == null)
			user = "";
		String password = req.getParameter("password");
		if (password == null)
			password = "";
		String reqString = "&user=" + user + "&password=" + password;
		resp.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=" + PAGE_CHARSET
				+ "\">");
		out.println("<style type='text/css'>");
		out.println("body {font-size:12px;font-family:Arial, Helvetica, sans-serif;}");
		out.println("table {border-width: 1px; border-spacing: 1px; border-style: outset; border-collapse: separate; border-color: gray; background-color: white; font-size:12px; font-family:Arial, Helvetica, sans-serif;}");
		out.println("table th {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 240, 230); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("table td {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 250, 250); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<a href='" + req.getContextPath() + "/" + Console.SERVLETNAME + "'>Home</a>&nbsp;");
		out.println(SERVLETNAME.toUpperCase() + " v" + ServletParameters.CONSOLEVERSION + " del " + ServletParameters.CONSOLEDATA + "<br/>");
		out.println("<br/>METHODS:");
		String[] methods = getMethods();
		for(int i=0; i<methods.length; i++)
			out.println("<a href='" + req.getContextPath() + "/" + SERVLETNAME + "?page=" + methods[i] + reqString + "'>" + methods[i] + "</a>&nbsp;");
		out.println("<hr />");
	}
	public void pageFooter(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		String srvname = req.getServerName();
		int srvport = req.getServerPort();
		PrintWriter out = resp.getWriter();
		out.print("Server name: " + srvname + " Server port: " + srvport);
		out.println("</body></html>");
	}

	public void addField(HttpServletRequest req, HttpServletResponse resp, String field,
			String value, boolean br) throws IOException
	{
		PrintWriter out = resp.getWriter();
		out.print(field + ":<input type='text' name='" + field + "' value='" + value + "' />");
		if (br)
			out.println("<br />");
	}
}
