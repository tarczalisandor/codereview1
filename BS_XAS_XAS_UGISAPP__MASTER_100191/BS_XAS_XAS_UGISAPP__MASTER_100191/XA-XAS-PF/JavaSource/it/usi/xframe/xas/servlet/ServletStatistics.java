package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.app.AppAccount;
import it.usi.xframe.xas.app.AppStats;
import it.usi.xframe.xas.app.BaseApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servlet implementation class Console
 */
public class ServletStatistics extends ServletBase
{
	private static Logger logger = LoggerFactory.getLogger(ServletStatistics.class);
	private static final long serialVersionUID = 1L;
	public static final String SERVLETNAME  = "statistics";

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
				new AppStats(),
				new AppAccount()
			};
		return this.applications;
	}
}
