package it.usi.xframe.xas.app;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.DeliveryResponse;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsRequest3;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Check;
import it.usi.xframe.xas.html.fragments.javaobject.Dropdown;
import it.usi.xframe.xas.html.fragments.javaobject.DropdownRow;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.FieldDate;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Radio;
import it.usi.xframe.xas.html.fragments.javaobject.RadioGroup;
import it.usi.xframe.xas.html.fragments.javaobject.TextArea;
import it.usi.xframe.xas.html.fragments.javaobject.Toogle;
import it.usi.xframe.xas.servlet.ServletBase;
import it.usi.xframe.xas.wsutil.DeliveryReport;
import it.usi.xframe.xas.wsutil.ENUM_STATUS;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 1.0
 * @author
 */
public class AppSms extends BaseApplication
{
	private static Logger logger = LoggerFactory.getLogger(AppSms.class);
	private static final long serialVersionUID = 1L;
	
	public static final String APPLICATIONNAME			= "sms";
	public static final String APPLICATIONTITLE			= "Sms";
	public static final String ACTION_VALUE_SENDSMS1	= "sendsms1";
	public static final String ACTION_VALUE_SENDSMS2	= "sendsms2";
	public static final String ACTION_VALUE_SENDSMS3	= "sendsms3";
	public static final String ACTION_VALUE_HASHCODE	= "hashcode";
	public static final String ACTION_VALUE_CONFIG		= "config";
	public static final String ACTION_VALUE_LOGCONFIG	= "logConfig";
	public static final String ACTION_VALUE_DELIVERY	= "delivery";

	public static final String DATE_TIME_FORMAT			= "yyyyMMdd-HHmm";
	public static final String SAMPLE_XASUSER			= "02008XAS, 02008XASUNI, 02008XASNUM - Use a xasUser from the configuration page";
	public static final String SAMPLE_MESSAGE			= "Hello World! or Hello World \"Â\" with UCS2!";
	public static final String SAMPLE_PHONE				= "+39.338.333-4434";
	public static final String SAMPLE_PERIOD			= "in minutes";
	public static final String SAMPLE_DATE				= "Format as: " + DATE_TIME_FORMAT;
	public static final String SAMPLE_CODE				= "XAS00000I";
	public static final String SAMPLE_ERROR_MESSAGE		= "DELIVERED: Message is delivered to destination";
	public static final String SAMPLE_DELIVERY_PHONE	= "393383334434";
	public static final String SAMPLE_SMSIDS			= "NHDFUUIFWRUFHIW,WEORHWERHWEUI,DFWUEUWI";

	public static final String sDir1					= "C:\\Projects\\workspaces\\xas\\";
	public static final String sDir2					= "C:\\Projects\\";

	public static final String VALUE_YES				= "yes";
	public static final String VALUE_NO					= "no";

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
			else if (ACTION_VALUE_SENDSMS1.equals(method))
				method_sendSms1(req, resp, page, method, action);
			else if (ACTION_VALUE_SENDSMS2.equals(method))
				method_sendSms2(req, resp, page, method, action);
			else if (ACTION_VALUE_SENDSMS3.equals(method))
				method_sendSms3(req, resp, page, method, action);
			else if (ACTION_VALUE_HASHCODE.equals(method))
				method_hashcode(req, resp, page, method, action);
 			else if (ACTION_VALUE_CONFIG.equals(method))
				method_config(req, resp, page, method, action);
			else if (ACTION_VALUE_LOGCONFIG.equals(method))
				method_logConfig(req, resp, page, method, action);
			else if (ACTION_VALUE_DELIVERY.equals(method))
				method_delivery(req, resp, page, method, action);
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_SENDSMS1, ACTION_VALUE_SENDSMS1, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_SENDSMS2, ACTION_VALUE_SENDSMS2, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_SENDSMS3, ACTION_VALUE_SENDSMS3, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_HASHCODE, ACTION_VALUE_HASHCODE, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
 		methodList.add(new ApplicationMethod(ACTION_VALUE_CONFIG, ACTION_VALUE_CONFIG, new MultiSecurityProfile(SingleSecurityProfile.ADMIN)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_LOGCONFIG, ACTION_VALUE_LOGCONFIG, new MultiSecurityProfile(SingleSecurityProfile.ADMIN)));
		methodList.add(new ApplicationMethod(ACTION_VALUE_DELIVERY, ACTION_VALUE_DELIVERY, new MultiSecurityProfile(SingleSecurityProfile.ADMIN)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}
	
	/**
	 *
	 */
	private class DataLogConfig extends MethodData
	{
		// servlet params
		public final String SOURCETYPE		= "sourceType";
		public final String SOURCEFORMAT	= "sourceFormat";
		public final String PRETTY			= "pretty";
		public final String FILEDIR			= "fileDir";
		public final String FILE			= "file";

		public final String TYPE_CACHE		= "cache";
		public final String TYPE_FILE		= "file";
		public final String TYPE_TEST		= "test";

		public final String FORMAT_JSON		= "json";
		public final String FORMAT_XML		= "xml";
	}

	/**
	 * @param req
	 * @param resp
	 * @param method
	 * @param action
	 */
	private void method_logConfig(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"it.usi.xframe.xas.servlet.Sms", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG);

		String current_action = ACTION_VALUE_LOGCONFIG;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataLogConfig data = new DataLogConfig();
		
		data.set(data.SOURCETYPE, getRequestParameter(req,data.SOURCETYPE, null), data.TYPE_CACHE);
		data.set(data.SOURCEFORMAT, getRequestParameter(req,data.SOURCEFORMAT, null), data.FORMAT_JSON);
		data.set(data.PRETTY, getRequestParameter(req,data.PRETTY, null), "true");
		data.set(data.FILEDIR, getRequestParameter(req,data.FILEDIR, null), sDir1);
		data.set(data.FILE, getRequestParameter(req,data.FILE, null), "");

		if (logger.isDebugEnabled())
			data.dump();

		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "LogConfig data:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// source type
		Dropdown field_sourceType = new Dropdown(data.SOURCETYPE, "Source Type");
		field_sourceType.add(new DropdownRow(data.TYPE_CACHE, data.TYPE_CACHE, data.TYPE_CACHE.equals(data.getString(data.SOURCETYPE))?true:false));
		field_sourceType.add(new DropdownRow(data.TYPE_FILE, data.TYPE_FILE, data.TYPE_FILE.equals(data.getString(data.SOURCETYPE))?true:false));
		field_sourceType.add(new DropdownRow(data.TYPE_TEST, data.TYPE_TEST, data.TYPE_TEST.equals(data.getString(data.SOURCETYPE))?true:false));
		field_sourceType.setSuggestion("Choose the source type format");
		form.add(field_sourceType);
		// source format
		Dropdown field_sourceFormat = new Dropdown(data.SOURCEFORMAT, "Source format");
		field_sourceFormat.add(new DropdownRow(data.FORMAT_JSON, data.FORMAT_JSON, data.FORMAT_JSON.equals(data.getString(data.SOURCEFORMAT))?true:false));
		field_sourceFormat.add(new DropdownRow(data.FORMAT_XML, data.FORMAT_XML, data.FORMAT_XML.equals(data.getString(data.SOURCEFORMAT))?true:false));
		field_sourceFormat.setSuggestion("Choose the source type format");
		form.add(field_sourceFormat);
		// filedir
		Dropdown field_fielDir = new Dropdown(data.FILEDIR, "File directory");
		field_fielDir.add(new DropdownRow("1", sDir1, "1".equals(data.getString(data.FILEDIR))?true:false));
		field_fielDir.add(new DropdownRow("2", sDir2, "2".equals(data.getString(data.FILEDIR))?true:false));
		field_fielDir.setSuggestion("Choose the directory from which to load files");
		form.add(field_fielDir);
		// fiel list
		Dropdown field_fileList = new Dropdown(data.FILE, "File");
		String dir = null;
		if("1".equals(data.getString(data.FILEDIR)))
			dir = sDir1;
		else if("2".equals(data.getString(data.FILEDIR)))
			dir = sDir2;
		else
			dir = sDir1;
		String[] envList = (String[]) new ArrayList(getTestList(dir)).toArray(new String[0]);
		for (int i = 0; i < envList.length; i++) {
			field_fileList.add(new DropdownRow(Integer.toString(i), envList[i], false));
		}
		form.add(field_fileList);
		// pretty print
		Check field_pretty = new Check(data.PRETTY, "Pretty print", "true".equals(data.getString(data.PRETTY)));
		form.add(field_pretty);

		container.add(form);

		if (action.compareTo(method) == 0)
		{
			try
			{
				StringBuffer params = new StringBuffer();
				params.append(data.getString(data.SOURCETYPE));
				params.append(","+data.getString(data.SOURCEFORMAT));
				
				logger.debug("call service with:" + params.toString());
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
				String configuration = service.getConfiguration(params.toString());
	
				// LOG THE CONFIGURATION 
				sl.logIt("a_configuration", configuration); // LOG THE CONFIGURATION 
				logger.info(sl.getLogRow());				// LOG THE CONFIGURATION
	
				TextArea textArea = new TextArea("result", configuration, "Result", 50);
				container.add(textArea);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (ServiceFactoryException e)	{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param method
	 * @param action
	 */
	private void method_config(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_CONFIG;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Config data:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		container.add(form);

		if (action.compareTo(method) == 0)
		{
			try
			{
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
				String configuration = service.getConfiguration("cache,json,pretty");
				form.add(new Alert("", configuration, AlertType.INFO));
			}
			catch (Exception e)
			{
				this.pageSmsSentError(page, e, null);
			}
		}
	}

	/**
	 *
	 */
	private class DataSendSms1 extends MethodData
	{
		// servlet params
		public final String NUMBER		= "number";
		public final String MESSAGE		= "message";
		public final String ABI			= "abi";
		public final String ALIAS		= "alias";
	}

	/**
	 * @param req
	 * @param resp
	 * @param method
	 * @param action
	 */
	private void method_sendSms1(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_SENDSMS1;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		DataSendSms1 data = new DataSendSms1();

		data.set(data.NUMBER, getRequestParameter(req,data.NUMBER, null), "393351234567");
		data.set(data.MESSAGE, getRequestParameter(req,data.MESSAGE, null), "");
		data.set(data.ABI, getRequestParameter(req,data.ABI, null), "02008XASUNI");
		data.set(data.ALIAS, getRequestParameter(req,data.ALIAS, null), "");

		if (logger.isDebugEnabled())
			data.dump();
		
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Send SMS v.1:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// phone
		Field field_number = new Field(data.NUMBER, data.getString(data.NUMBER), "Phone number", FieldType.TYPE_PHONE);
		field_number.setSuggestion(SAMPLE_PHONE);

		form.add(field_number);
		// message
		// the next row will be uncommented if you use the StringEscpeUtils in the getRequestParameter method ....
//		Field field_message = new Field(data.MESSAGE, StringEscapeUtils.escapeHtml(data.getString(data.MESSAGE)), "Sms message", FieldType.TYPE_TEXT);
		Field field_message = new Field(data.MESSAGE, data.getString(data.MESSAGE), "Sms message", FieldType.TYPE_TEXT);
		field_message.setIcon("fa-comment");
		field_message.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_message);
		// abi
		Field field_abi = new Field(data.ABI, data.getString(data.ABI), "Abi", FieldType.TYPE_TEXT);
		field_abi.setIcon("fa-university");
		field_abi.setSuggestion(SAMPLE_XASUSER);
		form.add(field_abi);
		// alias
		form.add(new Field(data.ALIAS, data.getString(data.ALIAS), "Alias", FieldType.TYPE_TEXT));

		container.add(form);

		if (action.compareTo(method) == 0)
		{
			SmsMessage sms = new SmsMessage();
			sms.setPhoneNumber(data.getString(data.NUMBER));
			sms.setMsg(data.getString(data.MESSAGE));
			SmsSenderInfo sender = new SmsSenderInfo();
			sender.setABICode(data.getString(data.ABI));
			sender.setAlias(data.getString(data.ALIAS).length() == 0 ? null : data.getString(data.ALIAS));

			try
			{
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
				service.sendSms(sms, sender);
				this.pageSmsSentSuccessfully(page, sms, null, null);
			}
			catch (Exception e)
			{
				this.pageSmsSentError(page, e, null);
			}
		}
	}
	
	/**
	 *
	 */
	private class DataSendSms2 extends DataSendSms1
	{
		// servlet params
		public final String VALIDITY			= "validity";
		public final String DELIVERY			= "delivery";
		public final String VALIDITYPERIOD		= "validityPeriod";
		public final String CORRELATIONID		= "correlationID";
		public final String FORCEASCIIENCODING	= "forceAsciiEncoding";
		public final String DELIVERYREPORT		= "deliveryReport";
		public final String SERVICETYPE			= "serviceType";

		public final String SERVICETYPE_EJB		= "EJBType";
		public final String SERVICETYPE_QUEUE	= "Queue";

		public final String CAL_VALIDITY		= VALIDITY;
		public final String CAL_DELIVERY		= DELIVERY;
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param method
	 * @param action
	 */
	private void method_sendSms2(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String current_action = ACTION_VALUE_SENDSMS2;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataSendSms2 data = new DataSendSms2();
		
		data.set(data.NUMBER, getRequestParameter(req,data.NUMBER, null), "393351234567");
		data.set(data.MESSAGE, getRequestParameter(req,data.MESSAGE, null), "");
		data.set(data.ABI, getRequestParameter(req,data.ABI, null), "02008XAS");
		data.set(data.ALIAS, getRequestParameter(req,data.ALIAS, null), "");
		data.set(data.VALIDITY, getRequestParameter(req,data.VALIDITY, null), "");
		data.set(data.DELIVERY, getRequestParameter(req,data.DELIVERY, null), "");
		data.set(data.VALIDITYPERIOD, getRequestParameter(req,data.VALIDITYPERIOD, null), "");
		data.set(data.CORRELATIONID, getRequestParameter(req,data.CORRELATIONID, null), "");
		data.set(data.FORCEASCIIENCODING, getRequestParameter(req,data.FORCEASCIIENCODING, null), VALUE_NO);
		data.set(data.DELIVERYREPORT, getRequestParameter(req,data.DELIVERYREPORT, null), VALUE_NO);
		data.set(data.SERVICETYPE, getRequestParameter(req,data.SERVICETYPE, null), data.SERVICETYPE_EJB);
		
		data.set(data.CAL_VALIDITY, FieldDate.toCalendar(data.getString(data.VALIDITY)));
		data.set(data.CAL_DELIVERY, FieldDate.toCalendar(data.getString(data.DELIVERY)));

		if (logger.isDebugEnabled())
			data.dump();

		// Form
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Send SMS v.2:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// service type
		Dropdown dropdown = new Dropdown(data.SERVICETYPE, "Service type");
		dropdown.add(new DropdownRow("", "", false));
		dropdown.add(new DropdownRow(data.SERVICETYPE_EJB, "Ejb", data.SERVICETYPE_EJB.equals(data.getString(data.SERVICETYPE))? true:false));
		dropdown.add(new DropdownRow(data.SERVICETYPE_QUEUE, "Queue", data.SERVICETYPE_QUEUE.equals(data.getString(data.SERVICETYPE))? true:false));
		form.add(dropdown);
		// phone number
		Field field_number = new Field(data.NUMBER, data.getString(data.NUMBER), "Phone number", FieldType.TYPE_PHONE);
		field_number.setSuggestion(SAMPLE_PHONE);
		form.add(field_number);
		// message
		Field field_message = new Field(data.MESSAGE, data.getString(data.MESSAGE), "Sms message", FieldType.TYPE_TEXT);
		field_message.setIcon("fa-comment");
		field_message.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_message);
		// abi
		Field field_abi = new Field(data.ABI, data.getString(data.ABI), "Abi", FieldType.TYPE_TEXT);
		field_abi.setIcon("fa-university");
		field_abi.setSuggestion(SAMPLE_XASUSER);
		form.add(field_abi);
		// alias
		form.add(new Field(data.ALIAS, data.getString(data.ALIAS), "Alias", FieldType.TYPE_TEXT));
		// date validity
		FieldDate field_validity = new FieldDate(data.VALIDITY, data.getCalendar(data.CAL_VALIDITY), "Validity");
		field_validity.setSuggestion(SAMPLE_DATE);
		form.add(field_validity);
		// validity period
		Field field_validityPeriod = new Field(data.VALIDITYPERIOD, data.getString(data.VALIDITYPERIOD), "Validity period", FieldType.TYPE_TEXT);
		field_validityPeriod.setSuggestion(SAMPLE_PERIOD);
		form.add(field_validityPeriod);
		// date delivery
		FieldDate field_delivery = new FieldDate(data.DELIVERY, data.getCalendar(data.CAL_DELIVERY), "Delivery");
		field_delivery.setSuggestion(SAMPLE_DATE);
		form.add(field_delivery);
		// correlation id
		form.add(new Field(data.CORRELATIONID, data.getString(data.CORRELATIONID), "Correlation ID", FieldType.TYPE_TEXT));
		// forceAsciiEncoding
		RadioGroup radiogroup_forceAsciiEncoding = new RadioGroup("", "Force ASCII encoding"); 
		radiogroup_forceAsciiEncoding.add(new Radio("forceAsciiEncodingYes", data.FORCEASCIIENCODING, VALUE_YES, VALUE_YES, VALUE_YES.equals(data.getString(data.FORCEASCIIENCODING)) ? true : false));
		radiogroup_forceAsciiEncoding.add(new Radio("forceAsciiEncodingNo", data.FORCEASCIIENCODING, VALUE_NO, VALUE_NO, VALUE_NO.equals(data.getString(data.FORCEASCIIENCODING)) ? true : false));
		form.add(radiogroup_forceAsciiEncoding);
		// smsResponse
		RadioGroup radiogroup_deliveryReport = new RadioGroup("", "Delivery report"); 
		radiogroup_deliveryReport.add(new Radio("deliveryReportYes", data.DELIVERYREPORT, VALUE_YES, VALUE_YES, VALUE_YES.equals(data.getString(data.DELIVERYREPORT)) ? true : false));
		radiogroup_deliveryReport.add(new Radio("deliveryReportNo", data.DELIVERYREPORT, VALUE_NO, VALUE_NO, VALUE_NO.equals(data.getString(data.DELIVERYREPORT)) ? true : false));
		form.add(radiogroup_deliveryReport);

		container.add(form);

		SmsResponse smsResponse = new SmsResponse();
		try
		{
			if (action.compareTo(method) == 0)
			{
				SmsRequest request = new SmsRequest();
				request.setPhoneNumber(data.getString(data.NUMBER));
				request.setMsg(data.getString(data.MESSAGE));
				request.setXasUser(data.getString(data.ABI));

				if (!"".equals(data.getString(data.VALIDITY)))
				{
					request.setValidity(FieldDate.toDate(data.getString(data.VALIDITY), DATE_TIME_FORMAT));
				}
				else if (!"".equals(data.getString(data.VALIDITYPERIOD)))
				{
					try
					{
						int validityMinutes = Integer.parseInt(data.getString(data.VALIDITYPERIOD));
						request.setValidityPeriod(new Integer(validityMinutes));
					} catch (NumberFormatException e)
					{
						throw new XASException("Validity period " + data.getString(data.VALIDITYPERIOD) + " is not valid");
					}
				}
				if (!"".equals(data.getString(data.DELIVERY)))
					request.setDeliveryTime(FieldDate.toDate(data.getString(data.DELIVERY), DATE_TIME_FORMAT));

				request.setCorrelationID(data.getString(data.CORRELATIONID));
				request.setForceAsciiEncoding(new Boolean(VALUE_YES.equals(data.getString(data.FORCEASCIIENCODING))));
				request.setSmsResponse(new Boolean(VALUE_YES.equals(data.getString(data.DELIVERYREPORT))));
				/*
				 * if(alias.length()==0) sender.setAlias(null); else
				 * sender.setAlias(alias);
				 */

				if (data.SERVICETYPE_EJB.equals(data.getString(data.SERVICETYPE)))
				{
					IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
					smsResponse = service.sendSms2(request);
				} else if (data.SERVICETYPE_QUEUE.equals(data.getString(data.SERVICETYPE)))
				{
					writeToQueue(request);
					smsResponse.setCode(ConstantsSms.XAS00003I_CODE);
					smsResponse.setMessage(ConstantsSms.XAS00003I_MESSAGE);
				} else
					throw new XASException("Service Type: " + data.getString(data.SERVICETYPE) + " not yet implemented!");
				
				/* SmsMessage only for pageSmsSentSuccessfully */
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(data.getString(data.NUMBER));
				sms.setMsg(data.getString(data.MESSAGE));

				if(smsResponse.getCode().equals(ConstantsSms.XAS00000I_CODE))
					this.pageSmsSentSuccessfully(page, sms, data.getString(data.SERVICETYPE), smsResponse);
				else
					this.pageSmsSentError(page, null, smsResponse);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		} catch (XASException e)
		{
			smsResponse.setCode(ConstantsSms.XAS00002E_CODE);
			Object[] msgArgs = {e.getMessage() + ((e.getCause() != null) ? " - " + e.getCause().getMessage() : "")};
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			smsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00002E_MESSAGE, msgArgs));
			this.pageSmsSentError(page, e, smsResponse);
		} finally
		{
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
			MDC.remove(ConstantsSms.MY_UUID_KEY);
		}
	}

	/**
	 *
	 */
	private class DataSendSms3 extends DataSendSms2
	{
		public final String TOOGLEVALIDITY	= "toogleValidity";
		public final String RADIOVALIDITY	= "radioValidity";
	}

	/**
	 * @param req
	 * @param resp
	 */
	private void method_sendSms3(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String current_action = ACTION_VALUE_SENDSMS3;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataSendSms3 data = new DataSendSms3();

		data.set(data.NUMBER, getRequestParameter(req,data.NUMBER, null), "393351234567");
		data.set(data.MESSAGE, getRequestParameter(req,data.MESSAGE, null), "");
		data.set(data.ABI, getRequestParameter(req,data.ABI, null), "02008XAS");
		data.set(data.ALIAS, getRequestParameter(req,data.ALIAS, null), "");
		data.set(data.TOOGLEVALIDITY, getRequestParameter(req,data.TOOGLEVALIDITY, null), "true");
		data.set(data.RADIOVALIDITY, getRequestParameter(req,data.RADIOVALIDITY, null), "");
		data.set(data.VALIDITY, getRequestParameter(req,data.VALIDITY, null), "");
		data.set(data.DELIVERY, getRequestParameter(req,data.DELIVERY, null), "");
		data.set(data.VALIDITYPERIOD, getRequestParameter(req,data.VALIDITYPERIOD, null), "");
		data.set(data.CORRELATIONID, getRequestParameter(req,data.CORRELATIONID, null), "");
		data.set(data.FORCEASCIIENCODING, getRequestParameter(req,data.FORCEASCIIENCODING, null), VALUE_NO);
		data.set(data.DELIVERYREPORT, getRequestParameter(req,data.DELIVERYREPORT, null), VALUE_NO);
		data.set(data.SERVICETYPE, getRequestParameter(req,data.SERVICETYPE, null), data.SERVICETYPE_EJB);
		
		data.set(data.CAL_VALIDITY, FieldDate.toCalendar(data.getString(data.VALIDITY)));
		data.set(data.CAL_DELIVERY, FieldDate.toCalendar(data.getString(data.DELIVERY)));

		if (logger.isDebugEnabled())
			data.dump();

		// Form
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Send SMS v.3:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// service type
		Dropdown dropdown = new Dropdown(data.SERVICETYPE, "Service type");
		dropdown.add(new DropdownRow("", "", false));
		dropdown.add(new DropdownRow(data.SERVICETYPE_EJB, "Ejb", data.SERVICETYPE_EJB.equals(data.getString(data.SERVICETYPE))? true:false));
		dropdown.add(new DropdownRow(data.SERVICETYPE_QUEUE, "Queue", data.SERVICETYPE_QUEUE.equals(data.getString(data.SERVICETYPE))? true:false));
		form.add(dropdown);
		// phone number
		Field field_number = new Field(data.NUMBER,data.getString(data.NUMBER),  "Phone number", FieldType.TYPE_PHONE);
		field_number.setSuggestion(SAMPLE_PHONE);
		form.add(field_number);
		// message
		Field field_message = new Field(data.MESSAGE, data.getString(data.MESSAGE), "Sms message", FieldType.TYPE_TEXT);
		field_message.setIcon("fa-comment");
		field_message.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_message);
		// abi
		Field field_abi = new Field(data.ABI, data.getString(data.ABI), "Abi", FieldType.TYPE_TEXT);
		field_abi.setIcon("fa-university");
		field_abi.setSuggestion(SAMPLE_XASUSER);
		form.add(field_abi);
		// alias
		form.add(new Field(data.ALIAS, data.getString(data.ALIAS), "Alias", FieldType.TYPE_TEXT));

		// toogle validity
		Toogle field_toogleVallidity = new Toogle(data.TOOGLEVALIDITY, "true".equals(data.getString(data.TOOGLEVALIDITY)) ? true : false, "Enable validity");
		field_toogleVallidity.setDisabledFields(new String[] {data.CAL_VALIDITY, data.VALIDITYPERIOD, "radioValidity1", "radioValidity2"});
		form.add(field_toogleVallidity);
		
		Radio radio_validity = new Radio("radioValidity1", data.RADIOVALIDITY, "1", "Date", "1".equals(data.getString(data.RADIOVALIDITY)) ? true : false);
		radio_validity.setEnabledFields(new String[] {data.VALIDITY});
		radio_validity.setDisabledFields(new String[] {data.VALIDITYPERIOD});

		// date validity
		FieldDate field_validity = new FieldDate(data.VALIDITY, data.getCalendar(data.CAL_VALIDITY), "Validity");
		field_validity.setSuggestion(SAMPLE_DATE);
		field_validity.add(radio_validity, true);
		form.add(field_validity);

		Radio radio_validityperiod = new Radio("radioValidity2", data.RADIOVALIDITY, "2", "Date", "2".equals(data.getString(data.RADIOVALIDITY)) ? true : false);
		radio_validityperiod.setEnabledFields(new String[] {data.VALIDITYPERIOD});
		radio_validityperiod.setDisabledFields(new String[] {data.VALIDITY});

		// validity period
		Field field_validityPeriod = new Field(data.VALIDITYPERIOD, data.getString(data.VALIDITYPERIOD), "Validity period", FieldType.TYPE_TEXT);
		field_validityPeriod.setSuggestion(SAMPLE_PERIOD);
		field_validityPeriod.add(radio_validityperiod, true);
		form.add(field_validityPeriod);

		// date delivery
		FieldDate field_delivery = new FieldDate(data.DELIVERY, data.getCalendar(data.CAL_DELIVERY), "Delivery");
		field_delivery.setSuggestion(SAMPLE_DATE);
		form.add(field_delivery);
		// correlation id
		form.add(new Field(data.CORRELATIONID, data.getString(data.CORRELATIONID), "Correlation ID", FieldType.TYPE_TEXT));
		// forceAsciiEncoding
		RadioGroup radiogroup_forceAsciiEncoding = new RadioGroup("forceAscii", "Force ASCII encoding");
		radiogroup_forceAsciiEncoding.add(new Radio("forceAsciiEncodingYes", data.FORCEASCIIENCODING, VALUE_YES, VALUE_YES, VALUE_YES.equals(data.getString(data.FORCEASCIIENCODING)) ? true : false));
		radiogroup_forceAsciiEncoding.add(new Radio("forceAsciiEncodingNo", data.FORCEASCIIENCODING, VALUE_NO, VALUE_NO, VALUE_NO.equals(data.getString(data.FORCEASCIIENCODING)) ? true : false));
		form.add(radiogroup_forceAsciiEncoding);
		// smsResponse
		RadioGroup radiogroup_deliveryReport = new RadioGroup("getDeliveryReport", "Delivery report"); 
		radiogroup_deliveryReport.add(new Radio("deliveryReportYes", data.DELIVERYREPORT, VALUE_YES, VALUE_YES, VALUE_YES.equals(data.getString(data.DELIVERYREPORT)) ? true : false));
		radiogroup_deliveryReport.add(new Radio("deliveryReportNo", data.DELIVERYREPORT, VALUE_NO, VALUE_NO, VALUE_NO.equals(data.getString(data.DELIVERYREPORT)) ? true : false));
		form.add(radiogroup_deliveryReport);

		container.add(form);

		SmsResponse smsResponse = new SmsResponse();
		try
		{
			if (action.compareTo(method) == 0)
			{
				SmsRequest3 request = new SmsRequest3();
				request.setPhoneNumber(data.getString(data.NUMBER));
				request.setMsg(data.getString(data.MESSAGE));
				request.setXasUser(data.getString(data.ABI));
				if (!"".equals(data.getString(data.VALIDITY)))
				{
					request.setValidity(FieldDate.toDate(data.getString(data.VALIDITY), DATE_TIME_FORMAT));
				}
				else if (!"".equals(data.getString(data.VALIDITYPERIOD)))
				{
					try
					{
						request.setValidityPeriod(new Integer(data.getString(data.VALIDITYPERIOD)));
					} catch (NumberFormatException e)
					{
						throw new XASException("Validity period " + data.getString(data.VALIDITYPERIOD) + " is not valid");
					}
				}

				if (!"".equals(data.getString(data.DELIVERY)))
					request.setDeliveryTime(FieldDate.toDate(data.getString(data.DELIVERY), DATE_TIME_FORMAT));
				
				request.setCorrelationID(data.getString(data.CORRELATIONID));
				request.setForceAsciiEncoding(new Boolean(VALUE_YES.equals(data.getString(data.FORCEASCIIENCODING))));
				request.setSmsResponse(new Boolean(VALUE_YES.equals(data.getString(data.DELIVERYREPORT))));

				if (data.SERVICETYPE_EJB.equals(data.getString(data.SERVICETYPE)))
				{
					IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
					smsResponse = service.sendSms3(request);
				}
				else if (data.SERVICETYPE_QUEUE.equals(data.getString(data.SERVICETYPE)))
				{
					writeToQueue(request);
					smsResponse.setCode(ConstantsSms.XAS00003I_CODE);
					smsResponse.setMessage(ConstantsSms.XAS00003I_MESSAGE);
				}
				else
					throw new XASException("Service Type: " + data.getString(data.SERVICETYPE) + " not yet implemented!");

				/* SmsMessage only for pageSmsSentSuccessfully */
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(data.getString(data.NUMBER));
				sms.setMsg(data.getString(data.MESSAGE));

				this.pageSmsSentSuccessfully(page, sms, data.getString(data.SERVICETYPE), smsResponse);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		} catch (XASException e)
		{
			smsResponse.setCode(ConstantsSms.XAS00002E_CODE);
			Object[] msgArgs =
			{e.getMessage() + ((e.getCause() != null) ? " - " + e.getCause().getMessage() : "")};
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			smsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00002E_MESSAGE, msgArgs));
			this.pageSmsSentError(page, e, smsResponse);
		} finally
		{
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
			MDC.remove(ConstantsSms.MY_UUID_KEY);
		}
	}

	/**
	 * @param req
	 * @param resp
	 */
	private void method_hashcode(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String current_action = ACTION_VALUE_HASHCODE;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataSendSms3 data = new DataSendSms3();

		data.set(data.MESSAGE, req.getParameter(data.MESSAGE), "");

		if (logger.isDebugEnabled())
			data.dump();

		// Form
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Hash code:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// message
		Field field_message = new Field(data.MESSAGE, data.getString(data.MESSAGE), "Message", FieldType.TYPE_TEXT);
		field_message.setIcon("fa-comment");
		field_message.setSuggestion(SAMPLE_MESSAGE);
		form.add(field_message);
		container.add(form);
		String fieldname_hashcode = "hashcode"; 
		Field field_hashcode = new Field(fieldname_hashcode, Integer.toString(data.getString(data.MESSAGE).hashCode()), "HashCode", FieldType.TYPE_TEXT);
		container.add(field_hashcode);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
		MDC.remove(ConstantsSms.MY_UUID_KEY);
	}
	
	/**
	 *
	 */
	private class DataDelivery extends MethodData
	{
		// servlet params
		public final String DELIVERYDATE	= "deliveryDate";
		public final String PROVIDERDATE	= "providerDate";
		public final String PHONENUMBER		= "phoneNumber";
		public final String STATUS			= "status";
		public final String SMSID			= "smsId";
	}

	/**
	 * @param req
	 * @param resp
	 */
	private void method_delivery(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(ConstantsSms.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String current_action = ACTION_VALUE_DELIVERY;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;
		DataDelivery data = new DataDelivery();
		
		data.set(data.DELIVERYDATE, getRequestParameter(req,data.DELIVERYDATE, null), "");
		data.set(data.PROVIDERDATE, getRequestParameter(req,data.PROVIDERDATE, null), "");
		data.set(data.PHONENUMBER, getRequestParameter(req,data.PHONENUMBER, null), "");
		data.set(data.STATUS, getRequestParameter(req,data.STATUS, null), "");
		data.set(data.SMSID, getRequestParameter(req,data.SMSID, null), "");

		if (logger.isDebugEnabled())
			data.dump();

		DeliveryResponse deliveryResponse = new DeliveryResponse();

		// Form
		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Delivery:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		// phone number
		form.add(new FieldDate(data.DELIVERYDATE, data.getCalendar(data.DELIVERYDATE), "Delivery date"));
		form.add(new FieldDate(data.PROVIDERDATE, data.getCalendar(data.PROVIDERDATE), "Provider date"));
		form.add(new Field(data.PHONENUMBER, data.getString(data.PHONENUMBER), "Phone number", FieldType.TYPE_PHONE));
		form.add(new Field(data.STATUS, data.getString(data.STATUS), "Status", FieldType.TYPE_TEXT));
		form.add(new Field(data.SMSID, data.getString(data.SMSID), "Sms id", FieldType.TYPE_TEXT));

		container.add(form);

		try
		{
			if (action.compareTo(method) == 0)
			{
				DeliveryReport deliveryReport = new DeliveryReport();
				Calendar c1 = Calendar.getInstance();
				c1.setTime(FieldDate.toDate(data.getString(data.DELIVERYDATE), DATE_TIME_FORMAT));
				deliveryReport.setDeliveryDate(c1);
				deliveryReport.setPhoneNumber(data.getString(data.PHONENUMBER));
				Calendar c2 = Calendar.getInstance();
				c1.setTime(FieldDate.toDate(data.getString(data.PROVIDERDATE), DATE_TIME_FORMAT));
				deliveryReport.setProviderDate(c2);
				deliveryReport.setStatus(ENUM_STATUS.fromString(data.getString(data.STATUS)));
				deliveryReport.setSmsIds(new String[] {data.getString(data.SMSID)});
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance()
						.getXasSendsmsServiceFacade();
				deliveryResponse = service.receiveTelecomDeliveryReport(deliveryReport);

				/* SmsMessage only for pageSmsSentSuccessfully */
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(data.getString(data.PHONENUMBER));
				sms.setMsg(data.getString(data.SMSID));
				SmsResponse smsResp = new SmsResponse();
				smsResp.setCode(deliveryResponse.getCode());
				smsResp.setMessage(deliveryResponse.getMessage());
				smsResp.setSmsIds(deliveryResponse.getSmsIds());
				this.pageSmsSentSuccessfully(page, sms, "EJB", smsResp);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			deliveryResponse.setCode(ConstantsSms.XAS00002E_CODE);
			Object[] msgArgs =
			{e.getMessage() + ((e.getCause() != null) ? " - " + e.getCause().getMessage() : "")};
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			deliveryResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00002E_MESSAGE, msgArgs));
			SmsResponse smsResp = new SmsResponse();
			smsResp.setCode(deliveryResponse.getCode());
			smsResp.setMessage(deliveryResponse.getMessage());
			smsResp.setSmsIds(deliveryResponse.getSmsIds());
			this.pageSmsSentError(page, e, smsResp);
		}
		finally
		{
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
			MDC.remove(ConstantsSms.MY_UUID_KEY);
		}
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

	/**
	 * @param request
	 * @throws XASException
	 */
	private void writeToQueue(SmsRequest request) throws XASException
	{
		QueueSession session = null;
		QueueConnection connection = null;
		try
		{
			Context ctx = new InitialContext();
			Context localCtx = (Context) ctx.lookup("java:comp/env"); // local
																		// context-sensitive
																		// JNDI
																		// namespace

			// Lookup the queue connection factory
			QueueConnectionFactory jmsFactory = (QueueConnectionFactory) localCtx
					.lookup("jms/TestConnection");

			// Lookup the queue connection factory
			Queue jmsQueue = (Queue) localCtx.lookup("jms/TestQueue");

			// open a JMS connection
			connection = jmsFactory.createQueueConnection();
			// connection = jmsFactory.createQueueConnection("US00659", "xxxx");
			connection.start();

			// create a JMS session
			session = connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE); // Session.AUTO_ACKNOWLEDGE);

			// create JMS sender
			// MessageProducer producer = session.createProducer(jmsQueue);
			QueueSender producer = session.createSender(jmsQueue);

			/*
			 * MapMessage message = session.createMapMessage();
			 * message.setString("text", request.getMsg());
			 * message.setString("destination", request.getPhoneNumber());
			 * message.setString("xasUser",request.getXasUser());
			 * message.setJMSCorrelationID("ALFASLASQRWRO393");
			 * producer.send(message);
			 */

			// ObjectMessage msg = session.createObjectMessage(request);

			String stringMsg = null;

			XStream xstream = new XStream(new JsonHierarchicalStreamDriver()
			{
				public HierarchicalStreamWriter createWriter(Writer writer)
				{ // Disable pretty print
					return new JsonWriter(writer, // JsonWriter.EXPLICIT_MODE,
							new JsonWriter.Format(new char[0], new char[0],
									JsonWriter.Format.COMPACT_EMPTY_ELEMENT));
				}
			});

			// XStream xstream = new XStream();

			xstream.alias("smsRequest", SmsRequest.class);
			stringMsg = xstream.toXML(request);
			logger.debug("Messaggio inviato: " + stringMsg);

			TextMessage msg = session.createTextMessage(stringMsg);
			msg.setJMSCorrelationID("TOIQPTIOWETIGDS");
			// msg.setStringProperty("JMSXUserID", "US01391");
			producer.send(msg);
			producer.close();

		} catch (NamingException e)
		{
			throw new XASException(e);
		} catch (JMSException e)
		{
			throw new XASException(e);
		} finally
		{
			if (session != null)
				try
				{
					session.close();
				} catch (JMSException e)
				{
					e.printStackTrace();
				}
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (JMSException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * @return
	 */
	public static Collection getTestList(String dir)
	{
		ArrayList al = new ArrayList();

		// Filter for test's suffix.
		FilenameFilter testFilter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if (name.matches("(.*)P-XAS-(.*)\\\\_common_\\\\SmsConfiguration.xml$"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		};

		Collection faFiles = listFileTree(new File(dir));
		Iterator iter = faFiles.iterator();
		String fileName;
		while (iter.hasNext())
		{
			File file = (File) iter.next();
			if (testFilter.accept(file, file.getAbsolutePath()))
			{
				fileName = file.getAbsolutePath();
				// Cut prefix & suffix.
				fileName = fileName.substring(sDir1.length(), fileName.length() - 0);
				al.add(sDir1 + fileName);
			}
		}
		al.add("");
		Collections.sort((List) al, (Comparator) String.CASE_INSENSITIVE_ORDER);
		return al;
	}

	/**
	 * @param dir
	 * @return
	 */
	public static Collection listFileTree(File dir)
	{
		Set fileTree = new HashSet();
		File[] files = dir.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++)
			{
				if (!files[i].isFile())
				{
					fileTree.addAll(listFileTree(files[i]));
				}
				else
					fileTree.add(files[i]);
			}
		return fileTree;
	}

	/**
	 * @param page
	 * @param err
	 * @param smsResponse
	 */
	private void pageSmsSentError(Page page, Exception err, SmsResponse smsResponse)
	{
		StringBuffer ret = new StringBuffer(); 
		ret.append("SMS has not been sent:<br>\n");
		if(err!=null) {
			ret.append("---- Error code: " + err.getMessage() + "<br>\n");
			ret.append("---- Error description: " + err.getCause().getMessage() + "<br>\n");
		}
		ret.append("SMS Response:<br>\n");
		if (smsResponse != null)
			ret.append("---- SmsResponse: " + smsResponse.getMessage());
		page.add(new Alert("", ret.toString(), AlertType.DANGER), true);
	}

	/**
	 * @param page
	 * @param sms
	 * @param channel
	 * @param smsResponse
	 */
	private void pageSmsSentSuccessfully(Page page, SmsMessage sms, String channel, SmsResponse smsResponse)
	{
		StringBuffer ret = new StringBuffer(); 
		AlertType alertType = AlertType.INFO; 
		if(smsResponse.isError()) {
			alertType = AlertType.DANGER;
			ret.append("SMS sent with errors:<br>\n");
		}
		else {
			alertType = AlertType.INFO;
			ret.append("SMS sent succesfully:<br>\n");
		}
		ret.append("---- Phone number: " + sms.getPhoneNumber() + "<br>\n");
		ret.append("---- Message: \"" + sms.getMsg() + "\"<br>\n");
		ret.append("SMS Response:<br>\n");
		ret.append("---- Code: " + smsResponse.getCode() + "<br>\n");
		ret.append("---- Message: " + smsResponse.getMessage() + "<br>\n");
		if (channel != null)
			ret.append("----Channel: " + channel + "<br>\n");
		if (smsResponse != null)
			ret.append("----SmsResponse: " + smsResponse.toString() + "<br>\n");
		page.add(new Alert("", ret.toString(), alertType), true);
	}
}
