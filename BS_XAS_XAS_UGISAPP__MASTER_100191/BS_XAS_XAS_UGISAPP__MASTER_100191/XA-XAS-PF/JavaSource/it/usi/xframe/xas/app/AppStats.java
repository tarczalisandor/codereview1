package it.usi.xframe.xas.app;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Dropdown;
import it.usi.xframe.xas.html.fragments.javaobject.DropdownRow;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Table;
import it.usi.xframe.xas.html.fragments.javaobject.TableRow;
import it.usi.xframe.xas.servlet.ServletBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 1.0
 * @author
 */
public class AppStats extends BaseApplication
{
	private static Logger logger = LoggerFactory.getLogger(AppStats.class);
	private static final long serialVersionUID = 1L;
	
	public static final String APPLICATIONNAME			= "stats";
	public static final String APPLICATIONTITLE			= "Stats";
	public static final String ACTION_VALUE_FETCHSTATS	= "fetchStats";

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
			String method = req.getParameter(METHOD_KEY);
			if (method == null)
				method = "";
			String action = req.getParameter(ACTION_KEY);
			if (action == null)
				action = "";

			if (method == null || method.compareTo("") == 0)
				methodNull();
			else if (ACTION_VALUE_FETCHSTATS.equals(method))
				method_fetchStats(req, resp, page, method, action);
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_FETCHSTATS, ACTION_VALUE_FETCHSTATS, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}
	
	private class DataFilters extends MethodData
	{
		// servlet params
		public final String TYPE	 = "Type";
		public final String PROVIDER = "Provider";
		public final String XASUSER  = "XasUser";
		public final String RANGE    = "Range";
		public final String PREFIX   = "Prefix";
	}

	private void method_fetchStats(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				AppStats.class.getName(), (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG);

		logger.info(sl.getLogRow());				// LOG THE CONFIGURATION

		String current_action = ACTION_VALUE_FETCHSTATS;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataFilters data = new DataFilters();
		
		data.set(data.TYPE, req.getParameter(data.TYPE), "");
		data.set(data.PROVIDER, req.getParameter(data.PROVIDER), "");
		data.set(data.XASUSER, req.getParameter(data.XASUSER), "");
		data.set(data.RANGE, req.getParameter(data.RANGE), "");
		data.set(data.PREFIX, req.getParameter(data.PREFIX), "");

		if (logger.isDebugEnabled())
			data.dump();

		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "LogConfig data:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// source type
		Dropdown field_type = new Dropdown(data.TYPE, data.TYPE);
		field_type.add(newDropdownRow("%", "All", data.getString(data.TYPE)));
		field_type.add(newDropdownRow("MT", "MT", data.getString(data.TYPE)));
		field_type.add(newDropdownRow("MO", "MO", data.getString(data.TYPE)));
		field_type.add(newDropdownRow("DR", "DR", data.getString(data.TYPE)));
		field_type.setSuggestion("Choose the type");
		form.add(field_type);

		Dropdown field_provider = new Dropdown(data.PROVIDER, data.PROVIDER);
		field_provider.add(newDropdownRow("%", "All", data.getString(data.PROVIDER)));
		field_provider.add(newDropdownRow("TCprovider@Telecom", "TCprovider@Telecom", data.getString(data.PROVIDER)));
		field_provider.add(newDropdownRow("Telecom", "Telecom", data.getString(data.PROVIDER)));
		field_provider.add(newDropdownRow("TCprovider@VodafonePop", "TCprovider@VodafonePop", data.getString(data.PROVIDER)));
		field_provider.add(newDropdownRow("VodafonePop", "VodafonePop", data.getString(data.PROVIDER)));
		field_provider.add(newDropdownRow("TCprovider@Mail", "TCprovider@Mail", data.getString(data.PROVIDER)));
		field_provider.setSuggestion("Choose the provider");
		form.add(field_provider);

		Field field_xasuser = new Field(data.XASUSER, data.getString(data.XASUSER), "XasUser", FieldType.TYPE_TEXT);
//		field_xasuser.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_xasuser);

		Field field_range = new Field(data.RANGE, data.getString(data.RANGE), "Range", FieldType.TYPE_TEXT);
//		field_range.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_range);

		Field field_prefix = new Field(data.PREFIX, data.getString(data.PREFIX), "Prefix", FieldType.TYPE_TEXT);
//		field_prefix.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_prefix);

		container.add(form);

		if (action.compareTo(method) == 0)
		{
			try
			{
				String[] filters = {data.getString(data.TYPE), data.getString(data.PROVIDER), data.getString(data.XASUSER), data.getString(data.RANGE), data.getString(data.PREFIX)};
				
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
				String[][] stats = service.fetchStats(filters);
	
	
				Table table = new Table("", "", new String[] {"Type", "Provider", "XasUser", "Range", "Prefix" , "Total"});
				for (int row=0; row < stats.length; ++row) {
					table.add(new TableRow(Integer.toString(row), stats[row]));
				}
				container.add(table);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (ServiceFactoryException e)	{
				e.printStackTrace();
			}
		}
	}

	private DropdownRow newDropdownRow(String name, String text, String defaultName) {
	    return new DropdownRow(name, text, name.equals(defaultName));
    }

}
