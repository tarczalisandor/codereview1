package it.usi.xframe.xas.app;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.InternationalSmsMessage;
import it.usi.xframe.xas.bfutil.data.InternationalSmsResponse;
import it.usi.xframe.xas.bfutil.data.SmsBillingInfo;
import it.usi.xframe.xas.bfutil.data.SmsDelivery;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.servlet.ServletBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version 1.0
 * @author
 */
public class AppSmsInternational extends BaseApplication
{
	private static final long serialVersionUID		= 1L;
	private static Log logger = LogFactory.getLog(AppSmsInternational.class);
	public static final String APPLICATIONNAME 		= "smsinternational";
	public static final String APPLICATIONTITLE		= "Sms international";
	public static final String ACTION_VALUE_SENDSMS	= "sendinternationalsms";

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
			else if (method.compareTo(ACTION_VALUE_SENDSMS) == 0)
				method_sendSms(req, resp, page, method, action);
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
		methodList.add(new ApplicationMethod(ACTION_VALUE_SENDSMS, ACTION_VALUE_SENDSMS, new MultiSecurityProfile(SingleSecurityProfile.ADMIN).add(SingleSecurityProfile.MT)));
		ApplicationMethod[] methods = (ApplicationMethod[]) methodList.toArray(new ApplicationMethod[methodList.size()]);
		return methods;
	}

	/**
	 *
	 */
	private class DataSendSms extends MethodData
	{
		// servlet params
		public final String NUMBER		= "number";
		public final String MESSAGE		= "message";
		public final String LEGALENTITY	= "legalentity";
		public final String SERVICENAME	= "servicename";
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param page
	 * @param method
	 * @param action
	 */
	private void method_sendSms(HttpServletRequest req, HttpServletResponse resp, Page page, String method, String action)
	{
		String current_action = ACTION_VALUE_SENDSMS;
		HtmlObject container = page.search(ServletBase.OBJECT_MAINCOINTAINER);
		if(container==null)
			container = page;

		DataSendSms data = new DataSendSms();

		data.set(data.NUMBER, getRequestParameter(req,data.NUMBER, null), "393351234567");
		data.set(data.MESSAGE, getRequestParameter(req,data.MESSAGE, null), "");
		data.set(data.LEGALENTITY, getRequestParameter(req,data.LEGALENTITY, null), "UBIS");
		data.set(data.SERVICENAME, getRequestParameter(req,data.SERVICENAME, null), "PINSAFE");

		if (logger.isDebugEnabled())
			data.dump();

		Form form = new Form("form1", req.getContextPath() + req.getServletPath(), "Send SMS International:");
		form.add(new Field(ServletBase.APPLICATION_KEY, getName(), "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.METHOD_KEY, method, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(ServletBase.ACTION_KEY, current_action, "", FieldType.TYPE_HIDDEN));
		form.add(new Field(data.NUMBER, data.getString(data.NUMBER), "Phone number", FieldType.TYPE_PHONE));
		Field field_message = new Field(data.MESSAGE, data.getString(data.MESSAGE), "Sms message", FieldType.TYPE_TEXT);
		field_message.setIcon("fa-comment");
		field_message.setSuggestion("example:Hello Ë");
		form.add(field_message);
		Field field_legalentity = new Field(data.LEGALENTITY, data.getString(data.LEGALENTITY), "Legal entity", FieldType.TYPE_TEXT);
		field_legalentity.setIcon("fa-university");
		form.add(field_legalentity);
		Field field_servicename = new Field(data.SERVICENAME, data.getString(data.SERVICENAME), "Service name", FieldType.TYPE_TEXT);
		field_servicename.setIcon("fa-industry");
		form.add(field_servicename);
		
		container.add(form);
		
		try
		{
			if (action.compareTo(method) == 0)
			{
				InternationalSmsMessage sms = new InternationalSmsMessage();
				sms.setText(data.getString(data.MESSAGE));

				SmsDelivery delivery = new SmsDelivery();
				delivery.setPhoneNumber(data.getString(data.NUMBER));

				SmsBillingInfo billing = new SmsBillingInfo();
				billing.setLegalEntity(data.getString(data.LEGALENTITY));
				billing.setServiceName(data.getString(data.SERVICENAME));

				IXasSendsmsServiceFacade service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
				InternationalSmsResponse result = service.sendInternationalSms(sms, delivery, billing);
				if (result.getCode() == InternationalSmsResponse.OK)
					this.pageSmsSentSuccesfully(page, sms, delivery);
				else
					this.pageSmsSentError(page, result);
				
				form.add(new Alert(null, "", AlertType.INFO));

			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServiceFactoryException e)
		{
			e.printStackTrace();
		} catch (XASException e)
		{
			form.add(new Alert(null, "", AlertType.DANGER));
			this.pageSmsSentError(page, e);
		}
	}

	/**
	 * @param page
	 * @param err
	 */
	private void pageSmsSentError(Page page, InternationalSmsResponse err)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("SMS has not been sent.\n");
		ret.append("Error code: " + err.getCode() + "\n");
		ret.append("Error description: " + err.getDescr());
		page.add(new Alert(null, ret.toString(), AlertType.DANGER), true);
	}

	/**
	 * @param page
	 * @param err
	 */
	private void pageSmsSentError(Page page, Exception err)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("SMS has not been sent.\n");
		ret.append("Error code: " + err.getMessage() + "\n");
		ret.append("Error description: " + err.getCause().getMessage());
		page.add(new Alert(null, ret.toString(), AlertType.DANGER), true);

		err.printStackTrace();
	}

	/**
	 * @param page
	 * @param sms
	 * @param delivery
	 */
	private void pageSmsSentSuccesfully(Page page, InternationalSmsMessage sms, SmsDelivery delivery)
	{
		StringBuffer ret = new StringBuffer();
		ret.append("SMS sent succesfully.\n");
		ret.append("Phone number: " + delivery.getPhoneNumber() + "\n");
		ret.append("Message: " + sms.getText());
		page.add(new Alert(null, ret.toString(), AlertType.INFO), true);
	}
}
