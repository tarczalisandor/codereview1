package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.app.AppAccount;
import it.usi.xframe.xas.app.AppEMail;
import it.usi.xframe.xas.app.AppMonitor;
import it.usi.xframe.xas.app.AppSms;
import it.usi.xframe.xas.app.AppSmsInternational;
import it.usi.xframe.xas.app.BaseApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servlet implementation class Console
 */
public class ServletConsole extends ServletBase
{
	private static Logger logger = LoggerFactory.getLogger(ServletConsole.class);
	private static final long serialVersionUID = 1L;
	public static final String SERVLETNAME  = "console";

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseServlet#getServletName()
	 */
	protected String getServletNameEx()
	{
		return SERVLETNAME;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.servlet.BaseServlet#getApplications()
	 */
	protected BaseApplication[] getApplications()
	{
		if(this.applications==null)
			this.applications = new BaseApplication[] { 
				new AppSms(),
				new AppSmsInternational(), 
				new AppEMail(),
				new AppMonitor(),
				new AppAccount()
			};
		return this.applications;
	}
}
