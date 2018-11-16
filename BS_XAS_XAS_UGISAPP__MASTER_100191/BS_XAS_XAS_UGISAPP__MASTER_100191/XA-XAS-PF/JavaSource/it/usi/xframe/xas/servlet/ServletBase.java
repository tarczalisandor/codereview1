package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.app.ApplicationMethod;
import it.usi.xframe.xas.app.BaseApplication;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.BrowserType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Container;
import it.usi.xframe.xas.html.fragments.javaobject.Footer;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Spinner;
import it.usi.xframe.xas.html.fragments.javaobject.TitleBar;
import it.usi.xframe.xas.html.fragments.javaobject.TitleMenu;
import it.usi.xframe.xas.html.fragments.javaobject.TitleVoice;

import java.io.IOException;

import javax.resource.spi.SecurityException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.Chronometer;

public abstract class ServletBase extends HttpServlet implements Servlet
{
	private static Logger logger = LoggerFactory.getLogger(ServletBase.class);
	private static final long serialVersionUID = 1L;

	public static final String APPLICATION_KEY		= "app";
	public static final String ACTION_KEY			= "action";
	public static final String METHOD_KEY			= "method";

	public static final String OBJECT_MAINCOINTAINER = "main";
	public static final String OBJECT_SPINNER		= "spinner";

	public static final String ACTION_VALUE_VOID	= "";

	// public static final String PAGE_CHARSET		= "iso-8859-1";
	public static final String PAGE_CHARSET			= "UTF-8";
	public static final String DEFAULT_CHARSET		= "UTF-8";

	protected Page page = null;
	protected HtmlObject formContainer = null;
	
	private Chronometer chrono = null;
	
	// Application list
	protected BaseApplication[] applications = null;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletBase()
	{
		super();

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

	/**
	 * @param req
	 * @param resp
	 */
	protected void execute(HttpServletRequest req, HttpServletResponse resp)
	{
		page = new Page(getServletNameEx());
		page.setBrowser(BrowserType.getBrowserType(req));
		page.setServlet(req, resp);
		// add spinner for long wait
		page.add(new Spinner(OBJECT_SPINNER));

		try
		{
			String application = req.getParameter(APPLICATION_KEY);
			if (application == null) application = "";
			String method = req.getParameter(METHOD_KEY);
			if (method == null) method = "";

			// add menu
			pageHeader(req, resp);

			this.formContainer = new Container(OBJECT_MAINCOINTAINER);
			page.add(this.formContainer);

			// get application object
			BaseApplication app = getApplication(req, application);
			if(app!=null)
			{
				logger.debug("       User:" + req.getRemoteUser());
				logger.debug("Application:" + application);
				logger.debug("     Method:" + method);
				// call method execution
				this.chrono = new Chronometer();
				executeMethod(req, resp, app, method);
			}
			else
				applicationUknown(application);
		}
		catch (SecurityException e)
		{
			logger.debug(e.getMessage());
			page.add(new Alert("", e.getMessage(), AlertType.DANGER), true);
		}
		catch (UnsupportedOperationException e)
		{
			logger.debug(e.getMessage());
			page.add(new Alert("", e.getMessage(), AlertType.DANGER), true);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage());
			page.add(new Alert("", e.getMessage(), AlertType.DANGER), true);
			e.printStackTrace();
		}
		finally
		{
			if(chrono!=null) this.chrono.stop();
			try {
				pageFooter(req, resp);
				page.serialize(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param req
	 * @param app
	 * @return
	 * 
	 * verify is user has right for application
	 */
	protected boolean isUserInRole(HttpServletRequest req, BaseApplication app)
	{
		boolean b = false;
		SingleSecurityProfile[] sp = app.getApplicationProfile().getSecurityProfileList();
//		if(logger.isDebugEnabled())
//		{
//			StringBuffer sb = new StringBuffer();
//			sb.append("Application [" + app.getName() +  "] require " + sp.length + " profiles: ");
//			for(int i=0; i<sp.length; i++)
//			{
//				sb.append(sp[i].toString());
//				sb.append(i==sp.length-1?"":",");
//			}
//			logger.debug(sb.toString());
//		}
		for(int i=0; i<sp.length; i++)
		{
			b = isUserInRole(req, sp[i]);
			if(b) {
//				logger.debug("user match with profile: " + sp[i].toString());
				break;
			}
		}
		//if(!b) logger.debug("user doesn't match any profile");
		return b;
	}
	
	/**
	 * @param req
	 * @param app
	 * @return
	 * 
	 * verify is user has right for application 
	 */
	protected boolean isUserInRole(HttpServletRequest req, ApplicationMethod method)
	{
		boolean b = false;
		SingleSecurityProfile[] sp = method.getApplicationProfile().getSecurityProfileList();
//		if(logger.isDebugEnabled())
//		{
//			StringBuffer sb = new StringBuffer();
//			sb.append("Method [" + method.getName() +  "] require " + sp.length + " profiles: ");
//			for(int i=0; i<sp.length; i++)
//			{
//				sb.append(sp[i].toString());
//				sb.append(i==sp.length-1?"":",");
//			}
//			logger.debug(sb.toString());
//		}
		for(int i=0; i<sp.length; i++)
		{
			b = isUserInRole(req, sp[i]);
			if(b) {
//				logger.debug("user match with profile: " + sp[i].toString());
				break;
			}
		}
//		if(!b) logger.debug("user doesn't match any profile");
		return b;
	}

	/**
	 * @param req
	 * @param securityProfile
	 * @return
	 */
	protected boolean isUserInRole(HttpServletRequest req, SingleSecurityProfile securityProfile)
	{
		boolean b = req.isUserInRole(securityProfile.getJavaProfileDescription());
		
		// match all profiles contained into master profile
//		SecurityProfile[] sp = securityProfile.getSecurityProfileList(securityProfile);
//		if(sp!=null)
//		{
//			for(int i=0; i<sp.length; i++)
//			{
//				if(req.isUserInRole(sp[i].toString())==true);
//				{
//					logger.debug("Original profile ["+securityProfile.toString()+"] contains ("+(i+1)+"/"+sp.length+")["+sp[i].toString()+"]");
//					return true;
//				}
//			}
//		}
		return b;
	}

	/**
	 * @param name
	 * @return
	 * 
	 * return the application object
	 */
	protected BaseApplication getApplication(HttpServletRequest req, String name) throws SecurityException
	{
		if(this.applications!=null && name!=null && name.length()>0)
		{
			for(int i=0;i<this.applications.length;i++)
			{
				if(this.applications[i].getName().equals(name))
				{
					// Security control
					if(isUserInRole(req, this.applications[i])==false)
						throw new SecurityException("No security role to access application [" + this.applications[i].getTitle() + "]");

					return this.applications[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param app
	 * @param method
	 * 
	 * call the application method execution
	 */
	protected void executeMethod(HttpServletRequest req, HttpServletResponse resp, BaseApplication app, String method) throws SecurityException
	{
		ApplicationMethod appMethod = app.getMethod(method);
		if(appMethod!=null)
		{
			// Security control
			if(isUserInRole(req, appMethod)==false)
				throw new SecurityException("No security role to access method [" + appMethod.getTitle() + "]");

			app.execute(req, resp, page);
		}
		else
			methodUknown(app.getTitle(), method);
	}
	
	/**
	 */
	protected void applicationNull()
	{
		//page.add(new Alert(Alert.LEVEL_DANGER, "application unknown"));
	}

	/**
	 */
	protected void applicationUknown(String name)
	{
		if(name!=null &&  name.length()>0)
			page.add(new Alert("", "application ["  + name + "] unknown", AlertType.DANGER));
	}

	/**
	 */
	protected void methodNull()
	{
		//page.add(new Alert(Alert.LEVEL_DANGER, "method unknown"));
	}

	/**
	 */
	protected void methodUknown(String application, String method)
	{
		page.add(new Alert("", "method [" + method + "] unknown", AlertType.DANGER));
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void pageHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String user = req.getParameter("user");
		if (user == null)
			user = "";
		String password = req.getParameter("password");
		if (password == null)
			password = "";

		String reqString = ServletParameters.USERNAME + "=" + user + "&" + ServletParameters.PASSWORD + "=" + password;
		
		TitleBar titleBar = new TitleBar("titlkeBar", req.getContextPath() + "/" + getServletNameEx(), "HOME");
		page.add(titleBar);

		BaseApplication[] apps = getApplications();
		for(int i=0; i<apps.length; i++)
		{
			// Security control
			//if(isUserInRole(req, app)==true)
				titleBar.add(getTitleMenu(req, apps[i], apps[i].getTitle(), reqString));
		}
	}

	/**
	 * @param req
	 * @param resp
	 */
	protected void pageFooter(HttpServletRequest req, HttpServletResponse resp)
	{
		Footer footer = new Footer("footer1", "");
		footer.setChrono(chrono!=null?Long.toString(this.chrono.getElapsed()):"~ ");
		page.add(footer);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	protected void pageSerialize(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		page.serialize(req, resp);
	}

	/**
	 * @param req
	 * @param bservlet
	 * @param menuTitle
	 * @param reqString
	 * @return
	 */
	private TitleMenu getTitleMenu(HttpServletRequest req, BaseApplication app, String menuTitle, String reqString)
	{
		TitleMenu titleMenu = new TitleMenu(req.getServletPath(), req.getServletPath(), menuTitle);
		ApplicationMethod[] methods = app.getMethods();
		for(int i=0; i<methods.length; i++)
		{
			// Security control
			if(isUserInRole(req, methods[i])==true)
			{
				String href = req.getContextPath() + req.getServletPath()
				+ "?" + ServletBase.APPLICATION_KEY + "=" + app.getName()
				+ "&" + ServletBase.METHOD_KEY + "=" + methods[i].getName() + "&" + reqString;
	
				titleMenu.add(new TitleVoice("titleVoice"+methods[i].getName(), href, methods[i].getName()));
			}
		}
		return titleMenu ;
	}

	/**
	 * @param req
	 * @param resp
	 * @param err
	 */
	protected void pageError(Throwable err)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("ERROR.<br>");
		sb.append("Error code: " + err.getMessage() + "<br>");
		sb.append("Error description: " + err.getCause().getMessage() + "<br>");
		page.add(new Alert("", sb.toString(), AlertType.DANGER));
	}

	/**
	 * @return
	 * 
	 * return the servlet name
	 */
	protected abstract String getServletNameEx();

	/**
	 * @return
	 * 
	 * return the application list
	 */
	protected abstract BaseApplication[] getApplications();
}
