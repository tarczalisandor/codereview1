package it.usi.xframe.xas.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class Console
 */
public class Console extends HttpServlet
{
	private static Logger logger = LoggerFactory.getLogger(Console.class);

	private static final long serialVersionUID = 1L;

	public static final String SERVLETNAME  = "console";
	public static final String ACTION       = "action";
	public static final String ACTION_VOID  = "";

	// public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Console()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		this.execute(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		this.execute(request, response);
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
			//else if (ACTION_DELIVERY.equals(page))
			//	delivery(req, resp);
			else
				pageNull(req, resp);
			pageFooter(req, resp);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param req
	 * @param resp
	 */
	private void pageNull(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("<b>method</b>: unknown<br/>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	public void pageHeader(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		resp.setContentType("text/html;charset=" + PAGE_CHARSET);
		PrintWriter out = resp.getWriter();
		String[] methods = null;

		String user = req.getParameter(ServletParameters.USERNAME);
		if (user == null)
			user = "";
		String password = req.getParameter(ServletParameters.PASSWORD);
		if (password == null)
			password = "";
		String reqString = "&" + ServletParameters.USERNAME + "=" + user
						 + "&" + ServletParameters.PASSWORD + "=" + password;
		resp.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=" + PAGE_CHARSET + "\">");
		out.println("<style type='text/css'>");
		out.println("body {font-size:12px;font-family:Arial, Helvetica, sans-serif;}");
		out.println("table {border-width: 1px; border-spacing: 0px; border-style: outset; border-collapse: separate; border-color: gray; background-color: white; font-size:12px; font-family:Arial, Helvetica, sans-serif;}");
		out.println("table th {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 240, 230); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("table td {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 250, 250); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("CONSOLE v" + ServletParameters.CONSOLEVERSION + " del " + ServletParameters.CONSOLEDATA + "<br/>");
		out.println("<br/>");
		out.println("SMS METHODS QUEUE/EJB:<br/>");
		methods = Sms.getMethods();
		for(int i=0; i<methods.length; i++)
			out.println("<a href='" + req.getContextPath() + "/" + Sms.SERVLETNAME + "?page=" + methods[i] + reqString + "'>" + methods[i] + "</a>&nbsp;");
		out.println("<br />");
		out.println("<br/>SMS INTERNATIONAL METHODS:<br/>");
		methods = SmsInternational.getMethods();
		for(int i=0; i<methods.length; i++)
			out.println("<a href='" + req.getContextPath() + "/" + SmsInternational.SERVLETNAME + "?page=" + methods[i] + reqString + "'>" + methods[i] + "</a>&nbsp;");
		out.println("<br/>");
		out.println("<br/>EMAIL METHODS:<br/>");
		methods = EMail.getMethods();
		for(int i=0; i<methods.length; i++)
			out.println("<a href='" + req.getContextPath() + "/" + EMail.SERVLETNAME + "?page=" + methods[i] + reqString + "'>" + methods[i] + "</a>&nbsp;");
		out.println("<hr />");
	}
	
	
	/**
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	public void pageFooter(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		String srvname = req.getServerName();
		int srvport = req.getServerPort();
		PrintWriter out = resp.getWriter();
		out.print("<b>Server name</b>: " + srvname + " <b>Server port</b>: " + srvport);
		out.println("</body></html>");
	}
}
