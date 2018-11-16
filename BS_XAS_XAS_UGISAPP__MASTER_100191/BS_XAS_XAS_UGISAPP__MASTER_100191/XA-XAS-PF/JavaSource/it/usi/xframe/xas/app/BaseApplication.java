package it.usi.xframe.xas.app;

import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.TitleMenu;
import it.usi.xframe.xas.html.fragments.javaobject.TitleVoice;
import it.usi.xframe.xas.servlet.ServletBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.unicredit.xframe.slf.Chronometer;

import org.apache.commons.lang.StringEscapeUtils;

public abstract class BaseApplication
{
	private static final long serialVersionUID = 81L;

	private Chronometer chrono = new Chronometer(false);
	
	public static final String APPLICATION_KEY = "app";
	public static final String ACTION_KEY = "action";
	public static final String METHOD_KEY = "method";

	public static final String ACTION_VALUE_VOID  = "";

	// public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";
	public static final String DEFAULT_CHARSET = "UTF-8";

	// prepare the list of methods
	protected ApplicationMethod[] methods = null;
	 
	/**
	 * @return
	 * 
	 * return the application security profile
	 * the profile contains all the method profiles
	 */
	public MultiSecurityProfile getApplicationProfile()
	{
		MultiSecurityProfile ap = new MultiSecurityProfile(SingleSecurityProfile.UNAUTHENTICATED);  
		ApplicationMethod[] m = prepareMethodList();
		for(int i=0; i<m.length; i++)
			ap.add(m[i].getApplicationProfile());
		return ap;
	}

	/**
	 */
	protected void methodNull()
	{
		//page.add(new Alert(Alert.LEVEL_DANGER, "method unknown"));
	}

	/**
	 */
	protected void methodUknown(Page page)
	{
		page.add(new Alert("", "method unknown", AlertType.DANGER));
	}

	/**
	 * @param page
	 * @param err
	 */
	protected void pageError(Page page, Throwable err)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("ERROR.<br>");
		sb.append("Error code: " + err.getMessage() + "<br>");
		sb.append("Error description: " + err.getCause().getMessage() + "<br>");
		page.add(new Alert("", sb.toString(), AlertType.DANGER));
	}

	/**
	 */
	public abstract void execute(HttpServletRequest req, HttpServletResponse resp, Page page);

	/**
	 * @return
	 * 
	 * return the application name
	 */
	public abstract String getName();

	/**
	 * @return
	 * 
	 * return the application title
	 */
	public abstract String getTitle();

	/**
	 * @return
	 * 
	 * return an array with all methods
	 */
	public ApplicationMethod[] getMethods()
	{
		if(this.methods!=null)
			return this.methods;
		this.methods = prepareMethodList();
		return this.methods;
	}
	/**
	 * @return
	 * 
	 * return an array with all methods
	 */
	protected abstract ApplicationMethod[] prepareMethodList();

	/**
	 * @return
	 * 
	 * return the method requested
	 */
	public ApplicationMethod getMethod(String name)
	{
		ApplicationMethod[] methods = getMethods();
		if(methods!=null)
			for(int i=0; i<methods.length;i++)
				if(methods[i].getName().equals(name))
					return methods[i]; 
		return null;
	}

	/**
	 * @param req
	 * @param resp
	 * @param titleMenu
	 * @param reqString
	 */
	public void writeServletMethods(HttpServletRequest req, HttpServletResponse resp, TitleMenu titleMenu, String reqString)
	{
		ApplicationMethod[] methods = getMethods();
		if(methods!=null)
			for(int i=0; i<methods.length; i++)
			{
				String href = req.getContextPath() + req.getServletPath()
				+ "?" + ServletBase.APPLICATION_KEY + "=" + getName()
				+ "&" + ServletBase.METHOD_KEY + "=" + methods[i].getName() + "&" + reqString;

				titleMenu.add(new TitleVoice("titleVoice"+methods[i].getName(), href, methods[i].getName()));
			}
//		out.println("<a href='http://agora.wiki.intranet.unicredit.it/xwiki/bin/view/APP-XAS/Maintainer_A2PSMS_Configuration#HTestinganddiagnosingconfiguration'>"
//		+ "Documentation" + "</a>&nbsp;");

	}
	
	/**
	 * @param req
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public String getRequestParameter(HttpServletRequest req, String param, String defaultValue)
	{
// To uncomment if you want to use the StringEscapeUtils class		
//		String safeValue = StringEscapeUtils.escapeHtml(req.getParameter(param));
		String safeValue = req.getParameter(param);
		return (safeValue==null) ? defaultValue : safeValue;
	}
}
