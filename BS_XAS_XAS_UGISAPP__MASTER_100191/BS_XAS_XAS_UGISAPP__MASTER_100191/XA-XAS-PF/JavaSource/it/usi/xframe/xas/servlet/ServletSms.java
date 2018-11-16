package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.app.AppAccount;
import it.usi.xframe.xas.app.AppSearch;
import it.usi.xframe.xas.app.BaseApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author C305373
 *
 */
public class ServletSms extends ServletBase
{
	private static Logger logger = LoggerFactory.getLogger(ServletSms.class);
	private static final long serialVersionUID = 1L;

	public static final String SERVLETNAME  = "sms";
	
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
				new AppSearch(),
				new AppAccount()
			};
		return this.applications;
	}
}
