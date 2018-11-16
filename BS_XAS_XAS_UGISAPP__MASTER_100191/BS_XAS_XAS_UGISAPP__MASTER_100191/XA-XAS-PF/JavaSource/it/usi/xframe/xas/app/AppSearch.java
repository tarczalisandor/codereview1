/**
 * 
 */
package it.usi.xframe.xas.app;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.TooltipPosition;
import it.usi.xframe.xas.html.fragments.javaobject.Check;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.FieldDate;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Table;
import it.usi.xframe.xas.servlet.ServletBase;
import it.usi.xframe.xas.servlet.ServletSplunkDataRest;
import it.usi.xframe.xas.util.SplunkDataConstant;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @author us03064
 * 
 */
public class AppSearch extends BaseApplication {
	private static Log log = LogFactory.getLog(AppSearch.class);
	private final String MY_UUID = UUID.randomUUID().toString();
	private static final long serialVersionUID = 5L;
	
	public static final String APPLICATIONNAME			= "search";
	public static final String APPLICATIONTITLE			= "Search";
	public static final String ACTION_VALUE_SEARCHSMS	= "searchsms";

	private String HTTP_SERVLET_URL						= "./" + ServletSplunkDataRest.SERVLETNAME;
	private String FORM_DATE_FORMAT						= "dd/MM/yyyy HH:mm";
	
	private String SPLUNK_DATE_FORMAT					= "MM/dd/yyyy:HH:mm:ss";
// Test
	private String[] SPLUNK_TABLE_FIELDS = new String[]{"sourcetype", "total"};
// Production	
//	private String[] SPLUNK_TABLE_FIELDS = new String[]{"Provider", "SMS", "SmsId", "XAS time", "XAS User", "Phone Number", "XAS ret.code", "XAS Status", "Uid"};
	
	
	private String SPLUNK_TABLE_NAME					= "tableSplunk";
	private String SPLUNK_TABLE_LABEL					= "Splunk Table with Grid";
	private boolean TABLE_AJAX = true;
	
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
		try {
			String method = getRequestParameter(req, METHOD_KEY, "");
			String action =getRequestParameter(req,ACTION_KEY, "");

			if (method == null || method.compareTo("") == 0)
				methodNull();
			else if (method.compareTo(ACTION_VALUE_SEARCHSMS) == 0)
				method_searchSms(req, resp, page, method, action);
			else
				methodUknown(page);
		} catch (Exception e) {
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_SEARCHSMS, ACTION_VALUE_SEARCHSMS, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.SEARCH)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}

	/**
	 *
	 */
	private class DataSearchSms extends MethodData
	{
		// servlet params
		public final String STARTDATE			= "startDate";
		public final String ENDDATE				= "endDate";
		public final String PARTIALNUMBER		= "partialNumber";
		public final String PHONENUMBER			= "phoneNumber";

		public final String CAL_STARTDATE		= STARTDATE;
		public final String CAL_ENDDATE			= ENDDATE;
		public final String BOOL_PARTIALNUMBER	= PARTIALNUMBER;
	}

	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException
	 */
	private void method_searchSms(HttpServletRequest req,
			HttpServletResponse resp, Page page, String method, String action)
			throws InterruptedException, UnsupportedEncodingException {

		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID,
				ConstantsSms.MY_LOG_VER, "use_logReferrer", MY_UUID,
				SmartLog.V_SCOPE_DEBUG).logReferrer(0);

		String current_action = ACTION_VALUE_SEARCHSMS;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		DataSearchSms data = new DataSearchSms();
		log.debug(sl.logIt("a_method", current_action).getLogRow());

		data.set(data.STARTDATE, getRequestParameter(req,data.STARTDATE, null), "");
		data.set(data.ENDDATE, getRequestParameter(req,data.ENDDATE, null), "");
		data.set(data.PARTIALNUMBER, getRequestParameter(req,data.PARTIALNUMBER, null), "");
		data.set(data.PHONENUMBER, getRequestParameter(req,data.PHONENUMBER, null), "phoneNumber");

		data.set(data.CAL_STARTDATE, FieldDate.toCalendar(data.getString(data.STARTDATE)));
		data.set(data.CAL_ENDDATE, FieldDate.toCalendar(data.getString(data.ENDDATE)));

		data.set(data.BOOL_PARTIALNUMBER, new Boolean("true".equals(data.getString(data.PARTIALNUMBER)) ? true : false));

		log.debug(sl.logIt("a_startDate", data.getString(data.STARTDATE), "a_endDate", data.getString(data.ENDDATE))
				.getLogRow());

		if (log.isDebugEnabled())
			data.dump();

		String phoneNumber = data.getString(data.PHONENUMBER).replaceAll("\\D","");
		if (data.getBoolean(data.BOOL_PARTIALNUMBER).booleanValue())
			phoneNumber="*".concat(phoneNumber).concat("*");
		
		data.set(data.PHONENUMBER, phoneNumber, "");
		
		// Form
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Search SMS");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY,method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY,current_action, "", FieldType.TYPE_HIDDEN));
		
		FieldDate fStartDate = new FieldDate(data.STARTDATE,
				data.getString(data.STARTDATE).length()>0 ? data.getCalendar(data.CAL_STARTDATE) : new GregorianCalendar(),
				"Start date");
		fStartDate.setSuggestion("Splunk - Search Start Date");
		fStartDate.setTooltip_position(TooltipPosition.LEFT);
		form.add(fStartDate);

		FieldDate fEndDate = new FieldDate(data.ENDDATE,
				data.getString(data.ENDDATE).length()>0 ? data.getCalendar(data.CAL_ENDDATE) : new GregorianCalendar(),
				"End date");
		fEndDate.setSuggestion("Splunk - Search End Date");
		fEndDate.setTooltip_position(TooltipPosition.LEFT);
		form.add(fEndDate);
		
		Field fPhoneNumber = new Field(data.PHONENUMBER, data.getString(data.PHONENUMBER), "Phone number", FieldType.TYPE_PHONE);
		fPhoneNumber.setSuggestion("Insert Mobile Phone Number to Search");
		fPhoneNumber.setTooltip_position(TooltipPosition.LEFT);
		form.add(fPhoneNumber);
		
		Check fPartialNumber = new Check(data.PARTIALNUMBER, "Partial Number", data.getBoolean(data.BOOL_PARTIALNUMBER).booleanValue());
		fPartialNumber.setSuggestion("Select if you know only a part of mobile phone number");
		fPartialNumber.setTooltip_position(TooltipPosition.LEFT);
		form.add(fPartialNumber);
		
		container.add(form);

		if (current_action.compareTo(action) == 0) {
			try {
				String earliest = null;
				String latest = null;
				SimpleDateFormat formDateFormat = new SimpleDateFormat(this.FORM_DATE_FORMAT);
				SimpleDateFormat splunkDateFormat = new SimpleDateFormat(this.SPLUNK_DATE_FORMAT);
				if( null == data.getString(data.STARTDATE) || data.getString(data.STARTDATE).length()<1)
					earliest = SplunkDataConstant.SPLUNK_EARLIEST;
				else{
					try{
						earliest = splunkDateFormat.format(formDateFormat.parse(data.getString(data.STARTDATE)));
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				}			
				if( null == data.getString(data.ENDDATE) || data.getString(data.ENDDATE).length()<1)
					latest = SplunkDataConstant.SPLUNK_LATEST; 
				else {
					try{
						latest = splunkDateFormat.format(formDateFormat.parse(data.getString(data.ENDDATE)));
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				}
				
				// Definisco la chiamata alla Servlet per popolare la Tabella iniziale
				String urlMaster = HTTP_SERVLET_URL + "?" + SplunkDataConstant.SPLUNK_EARLIEST_KEY +"=" + earliest 
				+ "&" + SplunkDataConstant.SPLUNK_LATEST_KEY + "=" + latest	+ "&" + SplunkDataConstant.SPLUNK_PHONENUMBER_KEY +"=" + phoneNumber;

				// Definisco la chiamata alla Servlet per popolare la Tabella di dettaglio
				String urlDetails = HTTP_SERVLET_URL + "?" + SplunkDataConstant.SPLUNK_EARLIEST_KEY +"=" + earliest 
				+ "&" + SplunkDataConstant.SPLUNK_LATEST_KEY + "=" + latest	+ "&" + SplunkDataConstant.SPLUNK_SID_KEY + "=" + SplunkDataConstant.SPLUNK_SID_SMS2; 
				
				Table stable = new Table(SPLUNK_TABLE_NAME ,SPLUNK_TABLE_LABEL ,SPLUNK_TABLE_FIELDS );
				stable.setAjaxCall(this.TABLE_AJAX, urlMaster, urlDetails);
				stable.setSearchKey(SplunkDataConstant.SPLUNK_DETAILS_KEY);
				form.add(stable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
