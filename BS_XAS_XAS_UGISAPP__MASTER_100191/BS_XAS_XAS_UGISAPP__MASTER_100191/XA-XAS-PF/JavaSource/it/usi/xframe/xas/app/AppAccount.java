package it.usi.xframe.xas.app;

import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Table;
import it.usi.xframe.xas.html.fragments.javaobject.TableRow;
import it.usi.xframe.xas.html.fragments.javaobject.TextArea;
import it.usi.xframe.xas.servlet.ServletBase;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.websphere.security.WSSecurityHelper;

import eu.unicredit.xframe.slf.UUID;

/**
 * @version 1.0
 * @author
 */
public class AppAccount extends BaseApplication
{
	private static Log log = LogFactory.getLog(AppMonitor.class);
	private static final long serialVersionUID		= 16L;
	
	private final String MY_UUID = UUID.randomUUID().toString();

	public static final String APPLICATIONTITLE		= "User";
	public static final String APPLICATIONNAME		= "account";
	public static final String ACTION_VALUE_INFO	= "info";
	public static final String ACTION_VALUE_LOGOUT	= "logout";
	public static final String ACTION_VALUE_LOGIN	= "login";
	public static final String ACTION_VALUE_SITEMAP	= "sitemap";

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

			if (method == null || method.compareTo("")==0)
				methodNull();
			else if (method.compareTo(ACTION_VALUE_INFO) == 0)
				method_info(req, resp, page, method, action);
			else if (method.compareTo(ACTION_VALUE_LOGOUT) == 0)
				method_logout(req, resp, page, method, action);
			else if (method.compareTo(ACTION_VALUE_LOGIN) == 0)
				method_login(req, resp, page, method, action);
			else if (method.compareTo(ACTION_VALUE_SITEMAP) == 0)
				method_siteMap(req, resp, page, method, action);
			else
				methodUknown(page);
		}
		catch (Exception e)
		{
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_INFO, ACTION_VALUE_INFO, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.AUTHENTICATED)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_LOGOUT, ACTION_VALUE_LOGOUT, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.AUTHENTICATED)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_LOGIN, ACTION_VALUE_LOGIN, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.UNAUTHENTICATED)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_SITEMAP, ACTION_VALUE_SITEMAP, new MultiSecurityProfile(SingleSecurityProfile.UNAUTHENTICATED)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_info(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action) 
	{
		String current_action = ACTION_VALUE_INFO;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
			
		try
		{
			Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "User info:");
			form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
			container.add(form);

			if (current_action.compareTo(action) == 0)
			{
				int row=0;
				Table table = new Table("", "", new String[] {"#", "key", "value"});
				table.add(new TableRow(Integer.toString(row++), new String[] {Integer.toString(row), "isSecure()", Boolean.toString(req.isSecure())}));
				table.add(new TableRow(Integer.toString(row++), new String[] {Integer.toString(row), "getServerName()", req.getServerName()}));
				table.add(new TableRow(Integer.toString(row++), new String[] {Integer.toString(row), "getRemoteUser()", req.getRemoteUser()}));
				table.add(new TableRow(Integer.toString(row++), new String[] {Integer.toString(row), "getUserPrincipal().getName()", req.getUserPrincipal().getName()}));
				SingleSecurityProfile[] ss = MultiSecurityProfile.getSecurityProfileList(new MultiSecurityProfile(SingleSecurityProfile.ALL)); 
				for(int i=0; i<ss.length; i++)
				{
					boolean b = req.isUserInRole(ss[i].getJavaProfileDescription());
					table.add(new TableRow(Integer.toString(row++), new String[] {Integer.toString(row), "isUserInRole(" + ss[i].getJavaProfileDescription()+")", Boolean.toString(b)}));
				}
				container.add(table);
			}
		} catch (Exception e)
		{
			this.pageError(page, e);
			e.printStackTrace();
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_logout(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_LOGOUT;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		try
		{
			String location = req.getContextPath() + req.getServletPath()
			+ "?" + ServletBase.APPLICATION_KEY + "=" + getName()
			+ "&" + ServletBase.METHOD_KEY + "=" + method + "&" + ACTION_KEY + "=" + method; 

			Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Logout data:");
			form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
			container.add(form);
			
//			out.println("<script language=\"javascript\">function logout() {document.execCommand(\"ClearAuthenticationCache\",\"false\");window.location = \"" + location + "\"}</script>");
//			out.println("<br />");
//			out.println("<input type='submit' value='logout' onclick='logout();'>");
//			out.println("<br />");

			if (current_action.compareTo(method) == 0)
			{
				HttpSession session = req.getSession(false);
				// Invalidate session
				if (session != null) {
					session.invalidate();
					log.debug("Session invalidated");
				}

				// ?
				try {
					if (null != Class.forName("com.ibm.websphere.security.WSSecurityHelper"))
					{
						WSSecurityHelper.revokeSSOCookies(req, resp);
						log.debug("SSO Cookies revoked");
					}
				} catch (Throwable e) {
					// it does not exist on the classpath
					this.pageError(page, e);
				}
				
			    Cookie[] cooks = req.getCookies();
			    if (cooks != null)
			    	for(int i = 0; i < cooks.length; i++)
			    	{
//			    		if ("LtpaToken".equals(cooks[i].getName()))
//			    		{
//			    			cooks[i].setMaxAge(0); // expire the LtpaToken if present
//			    			resp.addCookie(cooks[i]);
//							log.debug("LtpaToken cookie forced to expire");
//			    		}
//					    if ("LtpaToken2".equals(cooks[i].getName()))
//					    {
//					    	cooks[i].setMaxAge(0); // expire the LtpaToken if present
//					    	resp.addCookie(cooks[i]);
//							log.debug("LtpaToken2 cookie forced to expire");
//					    }
					    if (cooks[i].getName().startsWith("JSESSIONID"))
					    {
					    	cooks[i].setMaxAge(0); // expire the session
					    	resp.addCookie(cooks[i]);
							log.debug("JSESSIONID cookie forced to expire");
					    }
				    }

//			    Cookie[] cc = req.getCookies();
//				if(cc!=null)
//					for(int i=0; i<cc.length; i++)
//					{
//						Cookie cookie = cc[i];
//			            cc[i].setValue(null);
//			            cc[i].setMaxAge(0);
//			            resp.addCookie(cookie);
//					}

			    container.add(new Alert("", "Logged out successfully", AlertType.SUCCESS));
				
				log.debug("set http status to:" + HttpServletResponse.SC_UNAUTHORIZED);
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 	// status=401
				log.debug("send redirect");
				resp.sendRedirect("./console");
			}
		} catch (Exception e)
		{
			this.pageError(page, e);
		}
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_login(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_LOGIN;
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_siteMap(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_SITEMAP;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
			
		try
		{
			Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Site map:");
			form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
			form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
			container.add(form);

			if (current_action.compareTo(action) == 0)
			{
				StringBuffer report = new StringBuffer();
				BaseApplication[] apps = new BaseApplication[] { 
					new AppAccount(),
					new AppEMail(),
					new AppMonitor(),
					new AppSearch(),
					new AppSms(),
					new AppSmsInternational() 
				};

				for(int a=0; a<apps.length; a++)
				{
					report.append("Application: [" + apps[a].getName() + "]\r");
					ApplicationMethod[] methods = apps[a].getMethods();
					for(int m=0; m<methods.length; m++)
					{
						report.append("-- Method: [" + methods[m].getName() + "] security profiles (");
						MultiSecurityProfile appProfile = methods[m].getApplicationProfile();
						SingleSecurityProfile[] secProfiles = appProfile.getSecurityProfileList();
						for(int s=0; s<secProfiles.length; s++)
						{
							report.append(secProfiles[s].getJavaProfileDescription());
							report.append((s < secProfiles.length-1) ? "," : "");
						}
						report.append(")\r");
					}
				}
				TextArea textArea =  new TextArea("testarea1", report.toString(), "SiteMap", 30);
				container.add(textArea);
			}
		} catch (Exception e)
		{
			this.pageError(page, e);
			e.printStackTrace();
		}
	}
}
