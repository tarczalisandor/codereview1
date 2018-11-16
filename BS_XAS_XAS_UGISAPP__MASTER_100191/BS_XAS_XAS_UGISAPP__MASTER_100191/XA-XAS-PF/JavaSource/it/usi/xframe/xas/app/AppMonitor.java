package it.usi.xframe.xas.app;

import it.usi.xframe.xas.bfintf.IXasTestServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsMonitor;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Dropdown;
import it.usi.xframe.xas.html.fragments.javaobject.DropdownRow;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.servlet.ServletBase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 1.0
 * @author
 */
public class AppMonitor extends BaseApplication
{
	private static final long serialVersionUID = 15L;

	private static Log logger = LogFactory.getLog(AppMonitor.class);
	private final String MY_UUID = UUID.randomUUID().toString();

	public static final String APPLICATIONNAME		= "monitor";
	public static final String APPLICATIONTITLE		= "Monitor";
	public static final String ACTION_VALUE_PING	= "ping";
	public static final String ACTION_VALUE_TESTTIM	= "testtim";


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

			if (method == null || method.compareTo("") == 0)
				methodNull();
			else if (method.compareTo(ACTION_VALUE_PING) == 0)
				method_ping(req, resp, page, method, action);
			else if (method.compareTo(ACTION_VALUE_TESTTIM) == 0)
				method_testtim(req, resp, page, method, action);
			else
				methodUknown(page);
		}
		catch (Exception e) {
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_PING, ACTION_VALUE_PING, new MultiSecurityProfile(SingleSecurityProfile.ADMIN)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_TESTTIM, ACTION_VALUE_TESTTIM, new MultiSecurityProfile(SingleSecurityProfile.ADMIN)));
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
	private void method_ping(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_PING;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", MY_UUID, SmartLog.V_SCOPE_DEBUG).logReferrer(0);
		
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Ping:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		container.add(form);
		
		String result = "";
		if (action.compareTo(method) == 0)
		{
		result = "Not yet implemented";
		}
		
		form.add(new Alert(null, result, AlertType.INFO));
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_testtim(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_TESTTIM;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String test = getRequestParameter(req, "test", null);
		if (test.equals(""))
			test = Integer.toString(ConstantsMonitor.MONITOR_APPSERVERJSON);

		int testNumber = ConstantsMonitor.MONITOR_APPSERVERJSON;
		try
		{
			testNumber = Integer.getInteger(test).intValue();
		}
		catch (Exception e)
		{
		}
		
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Monitor data:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		Dropdown dropdown = new Dropdown("test", "Test type");
		dropdown.add(new DropdownRow("", "", false));
		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_APPSERVERJSON), "Test application server with json",
				ConstantsMonitor.MONITOR_APPSERVERJSON==testNumber ? true:false));
		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_APPSERVERHTML), "Test application server with html",
				ConstantsMonitor.MONITOR_APPSERVERHTML==testNumber ? true:false));
		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_WEBSERVER), "Test web server",
				ConstantsMonitor.MONITOR_WEBSERVER==testNumber ? true:false));
		form.add(dropdown);
		container.add(form);

//		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_APPSERVERJSON), "=app-srv-json", "1".equals(test)? true:false));
//		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_APPSERVERHTML), "=app-srv-html", "1".equals(test)? true:false));
//		dropdown.add(new DropdownRow(Integer.toString(ConstantsMonitor.MONITOR_WEBSERVER), "=web-serv", "1".equals(test)? true:false));

		String result = "";
		try
		{
			if (action.compareTo(method) == 0)
			{
				IXasTestServiceFacade service = XasServiceFactory.getInstance().getXasTestServiceFacade();
				result = service.eventMonitor(testNumber);
				form.add(new Alert(null, result, AlertType.INFO));
			}
		}
		catch (Exception e)
		{
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			result = errors.toString();
			form.add(new Alert(null, result, AlertType.DANGER));
		}
		finally
		{
		}

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
		MDC.remove(ConstantsSms.MY_UUID_KEY);
	}
}
