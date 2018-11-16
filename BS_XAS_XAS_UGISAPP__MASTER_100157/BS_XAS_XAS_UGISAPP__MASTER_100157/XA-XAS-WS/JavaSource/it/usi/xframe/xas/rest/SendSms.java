package it.usi.xframe.xas.rest;

import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.ISO8601DateConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

/**
 * @version 	1.0
 * @author
 */
public class SendSms extends HttpServlet implements Servlet {
	private static Log log = LogFactory.getLog(SendSms.class);

	private static final long serialVersionUID = 1L;
	public static final String SERVLETNAME = "sms";
	public static final String ACTION = "action";
	public static final String ACTION_SENDSMS = "sendsms";
	public static final String ACTION_SENDSMS2 = "sendsms2";
	//public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";
	final String MY_UUID = UUID.randomUUID().toString();
	//public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX";
 
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String MY_METHOD = "doPost";
		SmartLog sl = new SmartLog().logItCompact(Constants.MY_APPL_ID, Constants.MY_LOG_VER, "SendSms", MY_UUID, SmartLog.V_SCOPE_DEBUG);
		String smsVer = req.getParameter("smsVer");
		String jsonSmsRequest = req.getParameter("smsRequest");
		log.debug(sl.logIt(SmartLog.K_METHOD, MY_METHOD, SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER).getLogRow(true));

		SmsResponse smsResponse = new SmsResponse();
		IXasSendsmsServiceFacade facade = null;
        try {
        	if (! "2".equals(smsVer)) throw new Exception("Invalid smsVer:" + smsVer + " not handled");
        	SmsRequest smsRequest = null;
    		
    		if (jsonSmsRequest != null) { // Use json
        		XStream xstream = new XStream(new JettisonMappedXmlDriver()); 
        		xstream.alias("smsRequest", SmsRequest.class);
    			xstream.registerConverter(new ISO8601DateConverter());
        		smsRequest = (SmsRequest) xstream.fromXML(jsonSmsRequest);
    		} else { // Use inline params
    			smsRequest = new SmsRequest();
    			smsRequest.setCorrelationID(req.getParameter("correlationID"));
    			smsRequest.setMsg(req.getParameter("msg"));
    			smsRequest.setPhoneNumber(req.getParameter("phoneNumber"));
    			smsRequest.setXasUser(req.getParameter("xasUser"));
    			smsRequest.setForceAsciiEncoding(new Boolean(req.getParameter("forceAsciiEncoding"))); 
    			smsRequest.setSmsResponse(new Boolean(req.getParameter("smsResponse")));
    			SimpleDateFormat sdf = new SimpleDateFormat(SmsRequest.DATE_FORMAT); // xsd:dateTime format
    			if (req.getParameter("validity") != null) smsRequest.setValidity(sdf.parse(req.getParameter("validity")));
    			if (req.getParameter("validityPeriod") != null) smsRequest.setValidityPeriod(new Integer(Integer.parseInt(req.getParameter("validityPeriod"))));
    			if (req.getParameter("deliveryTime") != null) smsRequest.setDeliveryTime(sdf.parse(req.getParameter("deliveryTime")));
    			/* Use XStream for logging params as JSON */
    			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
    			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
    			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
    			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
    			         } 
    			     });
    			xstream.registerConverter(new ISO8601DateConverter());

        		xstream.alias("smsRequest", SmsRequest.class);
        		jsonSmsRequest = xstream.toXML(smsRequest);
    			
    		}
    		
    		log.debug(sl.logIt(SmartLog.K_METHOD, MY_METHOD, SmartLog.K_PHASE, SmartLog.V_PHASE_CALL, "a_smsRequest", jsonSmsRequest, SmartLog.K_CRID, smsRequest.getCorrelationID()).getLogRow(true));
    		
	        facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			smsResponse = facade.sendSms2(smsRequest);
        } catch (Exception e) {
			smsResponse.setCode(Constants.XAS00002E_CODE);
	          Object[] msgArgs = {e.getMessage() 
						+ ((e.getCause() != null) ? " - " 
								+ e.getCause().getMessage() : "")};
			if (log.isDebugEnabled()) log.debug(sl.logIt("a_cause", (String) msgArgs[0]).getLogRow(true));
			smsResponse.setMessage(MessageFormat.format(Constants.XAS00002E_MESSAGE, msgArgs));
					
        } finally {
			if (facade != null) XasServiceFactory.getInstance().dispose(facade);
		}
    	this.responsePage(req, resp, smsResponse);
	}


	private void responsePage(
		HttpServletRequest req,
		HttpServletResponse resp,
		SmsResponse smsResponse) {
		PrintWriter out;
		try {
			/* Use XStream for logging params as JSON */
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() { 
			       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
			         return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
			                              new JsonWriter.Format(new char[0],new char[0],JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
			         } 
			     });
			out = resp.getWriter();
    		xstream.alias("smsResponse", SmsResponse.class);
			out.print(xstream.toXML(smsResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
