package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.bfintf.IXasTestServiceFacade;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import eu.unicredit.xframe.slf.Chronometer;

/**
 * @version 	1.0
 * @author
 */
public class ServletTestOld extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 2L;
	public static final String SERVLETNAME = "test";
	public static final String PAGE = "page";
	public static final String PAGE_METHODLOG = "methodlog";
	public static final String PAGE_METHODNOLOG = "methodnolog";
	public static final String PAGE_METHODCOMMONLOG = "methodcommonlog";
	public static final String PAGE_TESTJODADATE = "methodjodadate";
	public static final String PAGE_TESTJAVADATE = "methodjavadate";
	public static final String PAGE_TESTTIMEZONE = "methodtimezone";
	public static final String ACTION = "action";
	public static final String ACTION_VIEW = "";
	public static final String ACTION_EXECUTE = "EXECUTE";

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
			pageHeader(req, resp);
			String page = getRequestParameter(req, PAGE);
			if (page == "")
				pageNull(req, resp);
			else if (page.compareTo(PAGE_METHODLOG) == 0)
				methodlog(req, resp);
			else if (page.compareTo(PAGE_METHODNOLOG) == 0)
				methodlog(req, resp);
			else if (page.compareTo(PAGE_METHODCOMMONLOG) == 0)
				methodlog(req, resp);
			else if (page.compareToIgnoreCase(PAGE_TESTJODADATE) == 0)
				jodaDate(req, resp);
			else if (page.compareToIgnoreCase(PAGE_TESTJAVADATE) == 0)
				javaDate(req, resp);
			else if (page.compareToIgnoreCase(PAGE_TESTTIMEZONE) == 0)
				timeZone(req, resp);
			else
				pageNull(req, resp);
			pageFooter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void methodlog(HttpServletRequest req, HttpServletResponse resp)
	{
		String page = getRequestParameter(req, "page");
		String action = getRequestParameter(req, "action");

		try
		{
			Chronometer chrono = new Chronometer(true, "step 1");		
			PrintWriter out = resp.getWriter();
			out.println(page + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + page + "' />");
			out.println("<input type='hidden' name='action' value='" + ACTION_EXECUTE + "' />");
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			chrono.partial("step 2");
			if (action.compareTo(ACTION_EXECUTE) == 0)
			{
				IXasTestServiceFacade service = XasServiceFactory.getInstance().getXasTestServiceFacade();
				if (page.compareTo(PAGE_METHODLOG) == 0)
					service.testlog();
				else if (page.compareTo(PAGE_METHODNOLOG) == 0)
					service.testnolog();
				else if (page.compareTo(PAGE_METHODCOMMONLOG) == 0)
					service.testcommonlog();
			}
			out.println("done");
			out.println("<hr />");
			chrono.stop();
			String res = chrono.toString();
			for(int i=0; i<res.length(); i++){
				if(res.charAt(i)=='\n')
					out.println("<br />");
				else
				out.print(res.charAt(i));
			}
		}
		catch(Exception e)
		{
		   this.pageError(req, resp, e);
		}
	}
	
	/**
	 * PLEASE DON'T CHANGE THIS METHOD'S CODE
	 * It outputs what follows:
	 * 
		Datetime 1: 2015-11-01T01:15:00.000-07:00 is in standard time: false 
		Datetime 5: 2015-11-01T02:15:00.000-08:00 is in standard time: true 
		Datetime 6: 2015-11-01T03:15:00.000-08:00 is in standard time: true 
		Datetime 2: 2015-10-25T01:20:00.000+02:00 is in standard time: false 
		Datetime 3: 2015-10-25T02:20:00.000+01:00 is in standard time: true 
		Datetime 4: 2015-10-25T03:20:00.000+01:00 is in standard time: true
	 *		
	 * This shows that Joda library has an inconsistent behavior about transitions from DST to standard time
	 * - In Los Angeles, DST ended on November 1st, 2015 at 2.00am, getting back to 1.00am
	 * - In Rome, DST ended on October 25th at 3.00am, rolling back to 2.00am		 
	 * This means that, in LA, the hour between 1.00am and 2.00am was repeated twice during transition, while in
	 * Rome the hout between 2.00am and 3.00am was repeated twice as well.
	 * This hour repetition leads to an ambiguity when we define an instant in that hour referring to the time zone
	 * that is transitioning to winter time.
	 * - If I say "November 1st, 2015 at 1.25am, Pacific Time Zone", do I mean "the first one" (still in DST, with UTC-07 offset) or
	 *   the second (back in standard time, with UTC-08 offset)?
	 * - Same way, if I say "October 25th, 2015 at 2.20am, Central Europe Time Zone", do I mean "the first
	 *   one" (still in DST, with UTC+02 offset) or the second (back in standard time, with UTC+01 offset)?
	 * This method's output shows that Joda library answers differently to the two questions.
	 * In Los Angeles' case, 1.15am is taken as "the first one", still in summer time
	 * In Rome's case, 2.20am is taken as if the switch to winter time has already happened.
	 * Why this???
	 * 
	 * See following method to see how the same situation is handled in java.util classes
	 * 
	 * Note: this happens with Joda 1.6.2 used in this project
	 * 
	 * @param req
	 * @param resp
	 */
	private void jodaDate(
			HttpServletRequest req,
			HttpServletResponse resp) {
		org.joda.time.DateTimeZone losAngelesTimeZone = org.joda.time.DateTimeZone.forID("America/Los_Angeles");
		org.joda.time.DateTimeZone romeTimeZone = org.joda.time.DateTimeZone.forID("Europe/Rome");
		org.joda.time.DateTime t1 = new org.joda.time.DateTime( 2015, 11, 1, 1, 15, 0, 0, losAngelesTimeZone ) ;
		org.joda.time.DateTime t5 = new org.joda.time.DateTime( 2015, 11, 1, 2, 15, 0, 0, losAngelesTimeZone ) ;
		org.joda.time.DateTime t6 = new org.joda.time.DateTime( 2015, 11, 1, 3, 15, 0, 0, losAngelesTimeZone ) ;
		org.joda.time.DateTime t2 = new org.joda.time.DateTime( 2015, 10, 25, 1, 20, 0, 0, romeTimeZone ) ; 
		org.joda.time.DateTime t3 = new org.joda.time.DateTime( 2015, 10, 25, 2, 20, 0, 0, romeTimeZone ) ; 
		org.joda.time.DateTime t4 = new org.joda.time.DateTime( 2015, 10, 25, 3, 20, 0, 0, romeTimeZone ) ; 
		
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.println("<br>Datetime 1: " + t1 + " is in standard time: " + losAngelesTimeZone.isStandardOffset(t1.getMillis()));
			out.println("<br>Datetime 5: " + t5 + " is in standard time: " + losAngelesTimeZone.isStandardOffset(t5.getMillis()));
			out.println("<br>Datetime 6: " + t6 + " is in standard time: " + losAngelesTimeZone.isStandardOffset(t6.getMillis()));
			out.println("<br>Datetime 2: " + t2 + " is in standard time: " + romeTimeZone.isStandardOffset(t2.getMillis()));		
			out.println("<br>Datetime 3: " + t3 + " is in standard time: " + romeTimeZone.isStandardOffset(t3.getMillis()));		
			out.println("<br>Datetime 4: " + t4 + " is in standard time: " + romeTimeZone.isStandardOffset(t4.getMillis()));		
			out.println("<br /><br />");
		} catch (IOException e) {
			pageError(req, resp, e);
		}
	}

	/**
	 * PLEASE DON'T CHANGE THIS METHOD'S CODE
	 * It outputs what follows:
	 * 
		Datetime 7: Sun Nov 01 08:15:57 CET 2015 is in DST time: true 
		Datetime 1: Sun Nov 01 10:15:57 CET 2015 is in DST time: false 
		Datetime 2: Sun Nov 01 11:15:57 CET 2015 is in DST time: false 
		Datetime 3: Sun Nov 01 12:15:57 CET 2015 is in DST time: false 
		Datetime 4: Sun Oct 25 01:20:57 CEST 2015 is in DST time: true 
		Datetime 5: Sun Oct 25 02:20:57 CET 2015 is in DST time: false 
		Datetime 6: Sun Oct 25 03:20:57 CET 2015 is in DST time: false 
	 *		
	 * The behavior of java Calendar and Date classes is more consistent, as the repeated time is
	 * taken into account in the same way: in both cases time it's considered to be "already changed" 
	 * 
	 * @param req
	 * @param resp
	 */
	private void javaDate(
			HttpServletRequest req,
			HttpServletResponse resp) {
		TimeZone losAngelesTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone romeTimeZone = TimeZone.getTimeZone("Europe/Rome");
		Calendar c1 = Calendar.getInstance(losAngelesTimeZone);
		Calendar c2 = Calendar.getInstance(romeTimeZone);
		c1.set(2015, 10, 1, 0, 15); Date d7 = c1.getTime();
		c1.set(2015, 10, 1, 1, 15); Date d1 = c1.getTime();
		c1.set(2015, 10, 1, 2, 15); Date d2 = c1.getTime();
		c1.set(2015, 10, 1, 3, 15); Date d3 = c1.getTime();
		c2.set(2015, 9, 25, 1, 20); Date d4 = c2.getTime();
		c2.set(2015, 9, 25, 2, 20); Date d5 = c2.getTime();
		c2.set(2015, 9, 25, 3, 20); Date d6 = c2.getTime();
		
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.println("<br>Datetime 7: " + d7 + " is in DST time: " + losAngelesTimeZone.inDaylightTime(d7));
			out.println("<br>Datetime 1: " + d1 + " is in DST time: " + losAngelesTimeZone.inDaylightTime(d1));
			out.println("<br>Datetime 2: " + d2 + " is in DST time: " + losAngelesTimeZone.inDaylightTime(d2));
			out.println("<br>Datetime 3: " + d3 + " is in DST time: " + losAngelesTimeZone.inDaylightTime(d3));
			out.println("<br>Datetime 4: " + d4 + " is in DST time: " + romeTimeZone.inDaylightTime(d4));		
			out.println("<br>Datetime 5: " + d5 + " is in DST time: " + romeTimeZone.inDaylightTime(d5));			
			out.println("<br>Datetime 6: " + d6 + " is in DST time: " + romeTimeZone.inDaylightTime(d6));			
			out.println("<br /><br />");
		} catch (IOException e) {
			pageError(req, resp, e);
		}
	}
	
	/**
	 * PLEASE DON'T CHANGE THIS METHOD'S CODE
	 * It outputs what follows:
	 * 
		Java: 
		Datetime 7 offset: -25200000 
		Datetime 1 offset: -28800000 


		Joda: 
		Datetime t1 offset: -25200000 
		Datetime t2 offset: -25200000 
	 *		
	 * This confirms what already seen in previous two methods. Setting dates as 0.15am and 1.15am, 
	 * November 1st 2015, Los Angeles time zone, the offset seems to change in java (meaning 1.15 is taken
	 * as after rolling back to winter time), while in Joda remains the same, meaning 1.15 is considered 
	 * before time switch, still in DST
	 * 
	 * @param req
	 * @param resp
	 */
	private void timeZone(
			HttpServletRequest req,
			HttpServletResponse resp) {
		TimeZone losAngelesTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		org.joda.time.DateTimeZone losAngelesJodaTimeZone = org.joda.time.DateTimeZone.forID("America/Los_Angeles");
		Calendar c1 = Calendar.getInstance(losAngelesTimeZone);
		c1.set(2015, 10, 1, 0, 15); Date d7 = c1.getTime();
		c1.set(2015, 10, 1, 1, 15); Date d1 = c1.getTime();
		int o1 = losAngelesTimeZone.getOffset(d7.getTime());
		int o2 = losAngelesTimeZone.getOffset(d1.getTime());
		org.joda.time.DateTime t1 = new org.joda.time.DateTime( 2015, 11, 1, 0, 15, 0, 0, losAngelesJodaTimeZone ) ;
		org.joda.time.DateTime t2 = new org.joda.time.DateTime( 2015, 11, 1, 1, 15, 0, 0, losAngelesJodaTimeZone ) ;
		int o1bis = losAngelesJodaTimeZone.getOffset(t1);
		int o2bis = losAngelesJodaTimeZone.getOffset(t2);
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.println("<br>Java:");
			out.println("<br>Datetime 7 offset: " + o1);
			out.println("<br>Datetime 1 offset: " + o2);
			out.println("<br /><br />");
			out.println("<br>Joda:");
			out.println("<br>Datetime t1 offset: " + o1bis);
			out.println("<br>Datetime t2 offset: " + o2bis);
			out.println("<br /><br />");
		} catch (IOException e) {
			pageError(req, resp, e);
		}
	}

	private void pageError(
		HttpServletRequest req,
		HttpServletResponse resp,
		Exception err) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("Error code: " + err.getMessage() + "<br>");
			out.print("Error description: " + err.getCause().getMessage() + "<br>");
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

	public void pageHeader(HttpServletRequest req, HttpServletResponse resp)
		throws Exception {
		String page = getRequestParameter(req, "page");
		String action = getRequestParameter(req, "action");
		String reqString = "";

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<style type='text/css'>");
		out.println("body {font-size:12px;font-family:Arial, Helvetica, sans-serif;}");
		out.println("table {border-width: 1px; border-spacing: 1px; border-style: outset; border-collapse: separate; border-color: gray; background-color: white; font-size:12px; font-family:Arial, Helvetica, sans-serif;}");
		out.println("table th {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 240, 230); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("table td {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 250, 250); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("METHODS:");
		out.println("<a href='" + req.getContextPath() + "/" + SERVLETNAME + "?page=" + PAGE_METHODLOG + reqString + "'>" + PAGE_METHODLOG + "</a>&nbsp;");
		out.println("<a href='" + req.getContextPath() + "/" + SERVLETNAME + "?page=" + PAGE_METHODNOLOG + reqString + "'>" + PAGE_METHODNOLOG + "</a>&nbsp;");
		out.println("<a href='" + req.getContextPath() + "/" + SERVLETNAME + "?page=" + PAGE_METHODCOMMONLOG + reqString + "'>" + PAGE_METHODCOMMONLOG + "</a>&nbsp;");
		out.println("<hr />");
	}

	public void pageFooter(HttpServletRequest req, HttpServletResponse resp)
		throws Exception
	{
		String srvname = req.getServerName();
		int srvport = req.getServerPort();
		PrintWriter out = resp.getWriter();
		out.print("Server name: " + srvname + " Server port: " + srvport);
		out.println("</body></html>");
	}

	public void addField(HttpServletRequest req, HttpServletResponse resp, String field, String value)
		throws IOException
	{
		PrintWriter out = resp.getWriter();
		out.println(field + ":<input type='text' name='" + field + "' value='" + value + "' /><br />");
	}

	/**
	 * @param req
	 * @param param
	 * @return
	 */
	private String getRequestParameter(HttpServletRequest req,String param){
// To uncomment if you want to use the StringEscapeUtils class		
//		String safeValue = StringEscapeUtils.escapeHtml(req.getParameter(param));
		String safeValue = req.getParameter(param);
		return null == safeValue ? "" : safeValue;
	}		
	
}
