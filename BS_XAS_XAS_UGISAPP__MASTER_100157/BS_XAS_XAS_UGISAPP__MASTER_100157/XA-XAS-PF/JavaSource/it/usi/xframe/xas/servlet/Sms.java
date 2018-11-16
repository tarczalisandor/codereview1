package it.usi.xframe.xas.servlet;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.DeliveryResponse;
import it.usi.xframe.xas.bfutil.data.SmsMessage;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsSenderInfo;
import it.usi.xframe.xas.wsutil.DeliveryReport;
import it.usi.xframe.xas.wsutil.ENUM_STATUS;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public class Sms extends HttpServlet implements Servlet {
	DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
	private static Logger logger = LoggerFactory.getLogger(Sms.class);

	private static final long serialVersionUID = 1L;
	public static final String SERVLETNAME = "sms";
	public static final String ACTION = "action";
	public static final String ACTION_SENDSMS = "sendsms";
	public static final String ACTION_SENDSMS2 = "sendsms2";
	public static final String ACTION_CONFIG = "config";
	public static final String ACTION_LOGCONFIG = "logConfig";
	public static final String ACTION_DELIVERY = "delivery";
	// public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";

	public static final String DATE_TIME_FORMAT = "yyyyMMdd-HHmm";
	public static final String SAMPLE_XASUSER = "02008XAS, 02008XASUNI, 02008XASNUM - Use a xasUser from the configuration page";
	public static final String SAMPLE_MESSAGE = "Hello World! or Hello World \"Â\" with UCS2!";
	public static final String SAMPLE_PHONE = "+39.338.333-4434";
	public static final String SAMPLE_PERIOD = "in minutes";
	public static final String SAMPLE_DATE = "Format as: " + DATE_TIME_FORMAT;
	public static final String SAMPLE_CODE = "XAS00000I";
	public static final String SAMPLE_ERROR_MESSAGE = "DELIVERED: Message is delivered to destination";
	public static final String SAMPLE_DELIVERY_PHONE = "393383334434";
	public static final String SAMPLE_SMSIDS = "NHDFUUIFWRUFHIW,WEORHWERHWEUI,DFWUEUWI";
	public static final String sDir = "C:\\Projects\\";

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *      (javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		this.execute(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#void
	 *      (javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		this.execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp)
	{
		try
		{
//			String charEnc = req.getCharacterEncoding();
			req.setCharacterEncoding(PAGE_CHARSET);
//			charEnc = req.getCharacterEncoding();
			// resp.setCharacterEncoding(PAGE_CHARSET);
			pageHeader(req, resp);
			String page = req.getParameter("page");
			if (page == null)
				pageNull(req, resp);
			else if (ACTION_SENDSMS.equals(page))
				sendSms(req, resp);
			else if (ACTION_SENDSMS2.equals(page))
				sendSms2(req, resp);
			else if (ACTION_CONFIG.equals(page))
				config(req, resp);
			else if (ACTION_LOGCONFIG.equals(page))
				logConfig(req, resp);
			else if (ACTION_DELIVERY.equals(page))
				delivery(req, resp);
			else
				pageNull(req, resp);
			pageFooter(req, resp);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * 
	 *         return an array with all test medhods
	 */
	public static String[] getMethods()
	{
		List actionList = new ArrayList();
		actionList.add(ACTION_SENDSMS);
		actionList.add(ACTION_SENDSMS2);
		actionList.add(ACTION_CONFIG);
		actionList.add(ACTION_LOGCONFIG);
		actionList.add(ACTION_DELIVERY);
		String[] stringArray = (String[]) actionList.toArray(new String[actionList.size()]);
		return stringArray;
	}

	public void init() throws ServletException {
	    super.init();
		MDC.put(Constants.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER,
				"it.usi.xframe.xas.servlet.Sms", (String) MDC.get(Constants.MY_UUID_KEY),
				SmartLog.V_SCOPE_DEBUG);
		IXasSendsmsServiceFacade service;
        try {
	        service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			String configuration = service.getConfiguration("cache,json");
			// LOG THE CONFIGURATION 
			sl.logIt("a_configuration", configuration); // LOG THE CONFIGURATION 
			logger.info(sl.getLogRow());				// LOG THE CONFIGURATION
			// LOG THE CONFIGURATION 
        } catch (ServiceFactoryException e) {
	        e.printStackTrace();
        } catch (RemoteException e) {
	        e.printStackTrace();
        }
    }

	/**
	 * @param req
	 * @param resp
	 */
	private void logConfig(HttpServletRequest req, HttpServletResponse resp) {
		MDC.put(Constants.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER,
				"it.usi.xframe.xas.servlet.Sms", (String) MDC.get(Constants.MY_UUID_KEY),
				SmartLog.V_SCOPE_DEBUG);
		String method = ACTION_LOGCONFIG;
		try
		{
			String params = req.getParameter("params");
			if (params == null)
				params = "";
			String env = req.getParameter("env");
			if (env == null)
				env = "";

			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + method + "' />");
			out.println("<input type='hidden' name='action' value='" + method + "' />");
			String[] envList;
			String eList = req.getParameter("eList");
			if (eList == null || "".equals(eList))
			{
				envList = (String[]) new ArrayList(getTestList()).toArray(new String[0]);
				eList = "";
				for (int i = 0; i < envList.length; i++)
					eList += ((i == 0) ? "" : ",") + envList[i];
			} else
				envList = eList.split(",");
			out.println("<input type='hidden' name='eList' value='" + eList + "' />");
			out.println("<table>");
			addField(req, resp, "params", params, null,
					"[cache,json,pretty|file,xml|test,c:\\Directory\\SmsConfiguration.xml]");

			addField(req, resp, "env", env, envList, "Choose env for test command:" + sDir);
			out.println("</table>");
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			if ("test".equals(params) && !"".equals(env))
			{
				params += "," + env;
			}
			IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			String configuration = service.getConfiguration(params);
			// LOG THE CONFIGURATION 
			sl.logIt("a_configuration", configuration); // LOG THE CONFIGURATION 
			logger.info(sl.getLogRow());				// LOG THE CONFIGURATION
			// LOG THE CONFIGURATION 
			out.println("<textarea style='width:100%;height:100%'>" + configuration + "</textarea>");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceFactoryException e)	{
			e.printStackTrace();
		}
	}

	/**
	 * @param req
	 * @param resp
	 */
	private void config(HttpServletRequest req, HttpServletResponse resp)
	{
		String method = ACTION_CONFIG;
		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("</form>");
			out.println("<hr />");
			IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			String configuration = service.getConfiguration("cache,json,pretty");
			out.println("<pre>" + configuration + "</pre>");
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		}
	}

	private void sendSms(HttpServletRequest req, HttpServletResponse resp)
	{
		String method = ACTION_SENDSMS;
		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String number = req.getParameter("number");
		if (number == null)
			number = "39335******";
		String message = req.getParameter("message");
		if (message == null)
			message = "";
		String abi = req.getParameter("abi");
		if (abi == null)
			abi = "02008XASUNI";
		String alias = req.getParameter("alias");
		if (alias == null)
			alias = "";

		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + method + "' />");
			out.println("<input type='hidden' name='action' value='" + method + "' />");
			out.println("<table>");
			addField(req, resp, "number", number, null, SAMPLE_PHONE);
			addField(req, resp, "message", message, null, SAMPLE_MESSAGE);
			addField(req, resp, "abi", abi, null, SAMPLE_XASUSER);
			addField(req, resp, "alias", alias, null, "");
			out.println("</table>");
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			if (action.compareTo(method) == 0)
			{
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(number);
				sms.setMsg(message);

				SmsSenderInfo sender = new SmsSenderInfo();
				sender.setABICode(abi);
				if (alias.length() == 0)
					sender.setAlias(null);
				else
					sender.setAlias(alias);

				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance()
						.getXasSendsmsServiceFacade();
				service.sendSms(sms, sender);
				this.pageSmsSentSuccessfully(req, resp, sms);
			}
		} catch (Exception e)
		{
			this.pageSmsSentError(req, resp, e, null);
		}
	}

	private void sendSms2(HttpServletRequest req, HttpServletResponse resp)
	{
		MDC.put(Constants.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(Constants.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String method = ACTION_SENDSMS2;
		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String number = req.getParameter("number");
		if (number == null)
			number = "39335******";
		String message = req.getParameter("message");
		if (message == null)
			message = "";
		String abi = req.getParameter("abi");
		if (abi == null)
			abi = "02008XAS";
		String alias = req.getParameter("alias");
		if (alias == null)
			alias = "";
		String validity = req.getParameter("validity");
		if (validity == null)
			validity = "";
		String delivery = req.getParameter("delivery");
		if (delivery == null)
			delivery = "";
		String validityPeriod = req.getParameter("validityPeriod");
		if (validityPeriod == null)
			validityPeriod = "";
		String correlationID = req.getParameter("correlationID");
		if (correlationID == null)
			correlationID = "";
		Boolean bForceAsciiEncoding = new Boolean("true".equals(req.getParameter("forceAsciiEncoding")));

		Boolean bSmsResponse = new Boolean("true".equals(req.getParameter("smsResponse")));

		String serviceType = req.getParameter("serviceType");
		if (serviceType == null)
			serviceType = "EJB";

		SmsResponse smsResponse = new SmsResponse();
		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + method + "' />");
			out.println("<input type='hidden' name='action' value='" + method + "' />");
			out.println("<table>");
			addField(req, resp, "serviceType", serviceType, "EJB,Queue".split(","), "");
			addField(req, resp, "number", number, null, SAMPLE_PHONE);
			addField(req, resp, "message", message, null, SAMPLE_MESSAGE);
			addField(req, resp, "abi", abi, null, SAMPLE_XASUSER);
			addField(req, resp, "alias", alias, null, "");
			addField(req, resp, "validity", validity, null, SAMPLE_DATE);
			addField(req, resp, "validityPeriod", validityPeriod, null, SAMPLE_PERIOD);
			addField(req, resp, "delivery", delivery, null, SAMPLE_DATE);
			addField(req, resp, "correlationID", correlationID, null, "");
			addField(req, resp, "forceAsciiEncoding", bForceAsciiEncoding.toString(),
					"false,true".split(","), "");
			addField(req, resp, "smsResponse", bSmsResponse.toString(), "false,true".split(","), "");
			out.println("</table>");
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			if (action.compareTo(method) == 0)
			{
				SmsRequest request = new SmsRequest();
				request.setPhoneNumber(number);
				request.setMsg(message);
				request.setXasUser(abi);
				// DateTimeFormatter formatter =
				// DateTimeFormat.forPattern(DATE_TIME_FORMAT);
				if (!"".equals(validity))
				{
					try
					{
						Date validityDate = formatter.parse(validity);
						request.setValidity(validityDate);
					} catch (ParseException e)
					{
						throw new XASException("Validity date " + validity + " is not valid");
					}
				} else if (!"".equals(validityPeriod))
				{
					try
					{
						int validityMinutes = Integer.parseInt(validityPeriod);
						request.setValidityPeriod(new Integer(validityMinutes));
					} catch (NumberFormatException e)
					{
						throw new XASException("Validity period " + validityPeriod + " is not valid");
					}
				}
				if (!"".equals(delivery))
				{
					try
					{
						Date deliveryDate = formatter.parse(delivery);
						request.setDeliveryTime(deliveryDate);
					} catch (ParseException e)
					{
						throw new XASException("Delivery date " + delivery + " is not valid");
					}
				}
				request.setCorrelationID(correlationID);
				request.setForceAsciiEncoding(bForceAsciiEncoding);
				request.setSmsResponse(bSmsResponse);
				/*
				 * if(alias.length()==0) sender.setAlias(null); else
				 * sender.setAlias(alias);
				 */

				if ("EJB".equals(serviceType))
				{
					IXasSendsmsServiceFacade service = XasServiceFactory.getInstance()
							.getXasSendsmsServiceFacade();
					smsResponse = service.sendSms2(request);
				} else if ("Queue".equals(serviceType))
				{
					writeToQueue(request);
					smsResponse.setCode(Constants.XAS00003I_CODE);
					smsResponse.setMessage(Constants.XAS00003I_MESSAGE);
				} else
					throw new XASException("Service Type: " + serviceType + " not yet implemented!");

				/* SmsMessage only for pageSmsSentSuccessfully */
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(number);
				sms.setMsg(message);

				this.pageSmsSentSuccessfully(req, resp, sms, serviceType, smsResponse);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		} catch (XASException e)
		{
			smsResponse.setCode(Constants.XAS00002E_CODE);
			Object[] msgArgs =
			{e.getMessage() + ((e.getCause() != null) ? " - " + e.getCause().getMessage() : "")};
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			smsResponse.setMessage(MessageFormat.format(Constants.XAS00002E_MESSAGE, msgArgs));
			this.pageSmsSentError(req, resp, e, smsResponse);
		} finally
		{
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
			MDC.remove(Constants.MY_UUID_KEY);
		}
	}

	private void delivery(HttpServletRequest req, HttpServletResponse resp) {
		MDC.put(Constants.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER,
				"use_logReferrer", (String) MDC.get(Constants.MY_UUID_KEY), SmartLog.V_SCOPE_DEBUG)
				.logReferrer(0);

		if (logger.isDebugEnabled())
			logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		String method = ACTION_DELIVERY;
		String action = req.getParameter("action");
		if (action == null)
			action = "";
		String deliveryDate = req.getParameter("deliveryDate");
		if (deliveryDate == null)
			deliveryDate = "";
		String providerDate = req.getParameter("providerDate");
		if (providerDate == null)
			providerDate = "";
		String phoneNumber = req.getParameter("phoneNumber");
		if (phoneNumber == null)
			phoneNumber = "";
		String status = req.getParameter("status");
		if (status == null)
			status = "";
		String smsId = req.getParameter("smsId");
		if (smsId == null)
			smsId = "";


		DeliveryResponse deliveryResponse = new DeliveryResponse();

		try
		{
			PrintWriter out = resp.getWriter();
			out.println(method + "<br/>");
			out.println("<form action='" + req.getContextPath() + "/" + SERVLETNAME + "' method='GET'>");
			out.println("<input type='hidden' name='page' value='" + method + "' />");
			out.println("<input type='hidden' name='action' value='" + method + "' />");
			out.println("<table>");
			addField(req, resp, "deliveryDate", deliveryDate, null, SAMPLE_DATE);
			addField(req, resp, "providerDate", providerDate, null, SAMPLE_DATE);
			addField(req, resp, "phoneNumber", phoneNumber, null, SAMPLE_DELIVERY_PHONE);
			addField(req, resp, "status", status, null, ENUM_STATUS._DELIVERED_TO_DEV);
			addField(req, resp, "smsId", smsId, null, SAMPLE_SMSIDS);
			out.println("</table>");
			out.println("<input type='submit' value='submit' /><br />");
			out.println("</form>");
			out.println("<hr />");
			if (action.compareTo(method) == 0)
			{
				DeliveryReport deliveryReport = new DeliveryReport();
				Calendar c1 = Calendar.getInstance();
				c1.setTime(formatter.parse(deliveryDate));
				deliveryReport.setDeliveryDate(c1);
				deliveryReport.setPhoneNumber(phoneNumber);
				Calendar c2 = Calendar.getInstance();
				c1.setTime(formatter.parse(providerDate));
				deliveryReport.setProviderDate(c2);
				deliveryReport.setStatus(ENUM_STATUS.fromString(status));
				deliveryReport.setSmsIds(new String[] {smsId});
				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance()
						.getXasSendsmsServiceFacade();
				deliveryResponse = service.receiveTelecomDeliveryReport(deliveryReport);

				/* SmsMessage only for pageSmsSentSuccessfully */
				SmsMessage sms = new SmsMessage();
				sms.setPhoneNumber(phoneNumber);
				sms.setMsg(smsId);
				SmsResponse smsResp = new SmsResponse();
				smsResp.setCode(deliveryResponse.getCode());
				smsResp.setMessage(deliveryResponse.getMessage());
				smsResp.setSmsIds(deliveryResponse.getSmsIds());
				this.pageSmsSentSuccessfully(req, resp, sms, "EJB", smsResp);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			deliveryResponse.setCode(Constants.XAS00002E_CODE);
			Object[] msgArgs =
			{e.getMessage() + ((e.getCause() != null) ? " - " + e.getCause().getMessage() : "")};
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			deliveryResponse.setMessage(MessageFormat.format(Constants.XAS00002E_MESSAGE, msgArgs));
			SmsResponse smsResp = new SmsResponse();
			smsResp.setCode(deliveryResponse.getCode());
			smsResp.setMessage(deliveryResponse.getMessage());
			smsResp.setSmsIds(deliveryResponse.getSmsIds());
			this.pageSmsSentError(req, resp, e, smsResp);
		} finally
		{
			if (logger.isDebugEnabled())
				logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN).getLogRow(true));
			MDC.remove(Constants.MY_UUID_KEY);
		}
	}

	private void pageSmsSentError(HttpServletRequest req, HttpServletResponse resp, Exception err,
			SmsResponse smsResponse)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS has not been sent.<br>");
			out.print("Error code: " + err.getMessage() + "<br>");
			out.print("Error description: " + err.getCause().getMessage() + "<br>");
			if (smsResponse != null)
				out.print("SmsResponse: " + smsResponse.toString() + "<br>");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void pageSmsSentSuccessfully(HttpServletRequest req, HttpServletResponse resp, SmsMessage sms)
	{
		// pageSmsSentSuccessfully(req, resp, sms, null, null);
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS sent succesfully<br>");
			out.print("Phone number: " + sms.getPhoneNumber() + "<br>");
			out.print("Message: " + sms.getMsg() + "<br>");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void pageSmsSentSuccessfully(HttpServletRequest req, HttpServletResponse resp, SmsMessage sms,
			String channel, SmsResponse smsResponse)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("SMS Response<br>");
			out.print("Code: " + smsResponse.getCode() + "<br>");
			out.print("Message: " + smsResponse.getMessage() + "<br>");
			out.print("Phone number: " + sms.getPhoneNumber() + "<br>");
			out.print("Message: " + sms.getMsg() + "<br>");
			if (channel != null)
				out.print("Channel: " + channel + "<br>");
			if (smsResponse != null)
				out.print("SmsResponse: " + smsResponse.toString() + "<br>");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void pageNull(HttpServletRequest req, HttpServletResponse resp)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print("method unknown");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void pageHeader(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		resp.setContentType("text/html;charset=" + PAGE_CHARSET);
		PrintWriter out = resp.getWriter();
		String user = req.getParameter("user");
		if (user == null)
			user = "";
		String password = req.getParameter("password");
		if (password == null)
			password = "";
		String reqString = "&user=" + user + "&password=" + password;
		resp.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-type\" content=\"text/html;charset=" + PAGE_CHARSET + "\">");
		out.println("<style type='text/css'>");
		out.println("body {font-size:12px;font-family:Arial, Helvetica, sans-serif;}");
		out.println("table {border-width: 1px; border-spacing: 0px; border-style: outset; border-collapse: separate; border-color: gray; background-color: white; font-size:12px; font-family:Arial, Helvetica, sans-serif;}");
		out.println("table th {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 240, 230); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("table td {border-width: 1px; padding: 1px; border-style: inset; border-color: gray; background-color: rgb(250, 250, 250); -moz-border-radius: 4px 4px 4px 4px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<a href='" + req.getContextPath() + "/" + Console.SERVLETNAME + "'>Home</a>&nbsp;");
		out.println(SERVLETNAME.toUpperCase() + " v" + ServletParameters.CONSOLEVERSION + " del "
				+ ServletParameters.CONSOLEDATA + "<br/>");
		out.println("<br/>METHODS:");
		String[] methods = getMethods();
		for (int i = 0; i < methods.length; i++)
			out.println("<a href='" + req.getContextPath() + "/" + SERVLETNAME + "?page=" + methods[i]
					+ reqString + "'>" + methods[i] + "</a>&nbsp;");
		out.println("<a href='http://agora.wiki.intranet.unicredit.it/xwiki/bin/view/APP-XAS/Maintainer_A2PSMS_Configuration#HTestinganddiagnosingconfiguration'>"
				+ "Documentation" + "</a>&nbsp;");

		out.println("<hr />");
	}
	public void pageFooter(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		String srvname = req.getServerName();
		int srvport = req.getServerPort();
		PrintWriter out = resp.getWriter();
		out.print("Server name: " + srvname + " Server port: " + srvport);
		out.println("</body></html>");
	}

	public void addField(HttpServletRequest req, HttpServletResponse resp, String field, String value,
			String[] options, String sample) throws IOException
	{

		PrintWriter out = resp.getWriter();
		out.print("<tr><td>" + field + "</td><td>");
		if (options != null)
		{
			out.print("<select name='" + field + "'>");
			for (int i = 0; i < options.length; i++)
			{
				out.print("<option value='" + options[i] + "'" + (options[i].equals(value) ? "SELECTED" : "")
						+ ">" + options[i] + "</option>");
			}
			out.print("</select>");
		} else
		{
			out.print("<input type='text' name='" + field + "' value='" + value + "' />");
		}
		out.println("</td><td>" + sample + "</td></tr>");
	}

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
	public static Collection getTestList() {
		ArrayList al = new ArrayList();

		// Filter for test's suffix.
		FilenameFilter testFilter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if (name.matches("(.*)P-XAS-(.*)\\\\_common_\\\\SmsConfiguration.xml$"))
				{
					return true;
				} else
				{
					return false;
				}
			}
		};

		Collection faFiles = listFileTree(new File(sDir));
		Iterator iter = faFiles.iterator();
		String fileName;
		while (iter.hasNext())
		{
			File file = (File) iter.next();
			if (testFilter.accept(file, file.getAbsolutePath()))
			{
				fileName = file.getAbsolutePath();
				// Cut prefix & suffix.
				fileName = fileName.substring(sDir.length(), fileName.length() - 0);
				al.add(sDir + fileName);
			}
		}
		al.add("");
		Collections.sort((List) al, (Comparator) String.CASE_INSENSITIVE_ORDER);
		return al;
	}

	public static Collection listFileTree(File dir)
	{
		Set fileTree = new HashSet();
		File[] files = dir.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isFile()) {
					fileTree.addAll(listFileTree(files[i]));
				} else
					fileTree.add(files[i]);
			}
		return fileTree;
	}

}
