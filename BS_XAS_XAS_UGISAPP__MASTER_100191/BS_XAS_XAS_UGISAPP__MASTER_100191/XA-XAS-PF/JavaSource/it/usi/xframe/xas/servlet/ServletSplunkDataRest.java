package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.util.HttpRESTClient;
import it.usi.xframe.xas.util.SplunkDataConstant;
import it.usi.xframe.xas.util.SplunkDataFormat;
import it.usi.xframe.xas.util.SplunkUtilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;

import eu.unicredit.xframe.slf.UUID;

/**
 * Servlet implementation class SplunkDataRest
 */
public class ServletSplunkDataRest extends HttpServlet implements Servlet {

	private static Log log = LogFactory.getLog(ServletSplunkDataRest.class);
	private static final long serialVersionUID		= 1L;

	public static final String SERVLETNAME			= "splunkDataRest";

	private final String ERROR_RETRIVE_SPLUNK_SID	= "Error retreiving the sid from Splunk server";
	private final String ERROR_RETRIVE_SPLUNK_DATA	= "Splunk response is too long please reduce the time range.";
	
	private final String HTTP_RESOURCE_UNAVAILABLE	= "Error Request resource is not available";
	private final String HTTP_SERVICE_UNAVAILABLE	= "Error Service is not available";
	
	private final String SPLUNK_PARAM_SEARCH		= "search";
	private final String SPLUNK_PARAM_ID			= "id";
	private final String SPLUNK_PARAM_AUTOCANCEL	= "auto_cancel";
	private final String SPLUNK_PARAM_MAXTIME		= "max_time";
	private final String SPLUNK_PARAM_OUTPUTMODE	= "output_mode";

	// time on msec to wait on Thread
	private int SLEEP = 1000;
	
	// public static final String PAGE_CHARSET = "iso-8859-1";
	private final String PAGE_CHARSET = "UTF-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletSplunkDataRest() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @param req
	 * @param param
	 * @return
	 */
	private String getRequestParameter(HttpServletRequest req,String param){
// To uncomment if you want to use the StringEscapeUtils class		
//		String safeValue = StringEscapeUtils.escapeHtml(req.getParameter(param));
		String safeValue = req.getParameter(param);
		if(null == safeValue)
			safeValue = "";
		return safeValue;
	}		

	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			log.debug("SplunkDataRest -> Start SplunkDataRest servlet");
			String uuid = UUID.randomUUID().toString();
			String requestURI = null;
			String splunkResponse = null;
			String splunkSid = null;
			String xmlResponse = null;
			String result = null ;
			String querySplunk = null;
			String detailsKey = null;

			HashMap hParams = new HashMap();
			
			String earliest = getRequestParameter(request, SplunkDataConstant.SPLUNK_EARLIEST_KEY);
			if(null != earliest && earliest.length()>0){
				earliest = java.net.URLDecoder.decode(earliest,this.PAGE_CHARSET);
				hParams.put("\\[PARAM_EARLIEST\\]", earliest);
			}
			String latest = getRequestParameter(request, SplunkDataConstant.SPLUNK_LATEST_KEY);
			if(null != latest && latest.length()>0){
				latest = java.net.URLDecoder.decode(latest,this.PAGE_CHARSET);
				hParams.put("\\[PARAM_LATEST\\]", latest);
			}
			String phoneNumber = getRequestParameter(request, SplunkDataConstant.SPLUNK_PHONENUMBER_KEY);
			if(null != phoneNumber && phoneNumber.length()>0){
				phoneNumber = java.net.URLDecoder.decode(phoneNumber,this.PAGE_CHARSET);
				hParams.put("\\[PARAM_PHONENUMBER\\]", phoneNumber);
			}
			String dataFormat = getRequestParameter(request, SplunkDataConstant.SPLUNK_DATATYPE_KEY);
			if(null != dataFormat && dataFormat.length()>0)
				dataFormat=java.net.URLDecoder.decode(dataFormat,this.PAGE_CHARSET).toLowerCase();
			else
				dataFormat=SplunkDataFormat.JSON.toString();
			String sid = getRequestParameter(request, SplunkDataConstant.SPLUNK_SID_KEY);
			if(null != sid && sid.length()>0)
				sid=java.net.URLDecoder.decode(sid,this.PAGE_CHARSET);
			else
				sid=SplunkDataConstant.SPLUNK_SID_SMS1;

			if(sid.equalsIgnoreCase(SplunkDataConstant.SPLUNK_SID_SMS1)){
				querySplunk = SplunkUtilities.buildSplunkQuery(SplunkDataConstant.SPLUNK_QUERY_SEARCH_XAS_REST_SMS1, hParams);
			}else if(sid.equalsIgnoreCase(SplunkDataConstant.SPLUNK_SID_SMS2)){
				detailsKey = getRequestParameter(request, SplunkDataConstant.SPLUNK_DETAILS_KEY);
				if(null != detailsKey && detailsKey.length()>0){
					detailsKey=java.net.URLDecoder.decode(detailsKey,this.PAGE_CHARSET);
					hParams.put("\\[PARAM_DETAILS\\]", detailsKey);
				}
				querySplunk = SplunkUtilities.buildSplunkQuery(SplunkDataConstant.SPLUNK_QUERY_SEARCH_XAS_REST_SMS2, hParams);
			}
			log.debug("SplunkDataRest -> Splunk search : " + querySplunk);
			log.debug("SplunkDataRest parameters : " );
			log.debug("Param - " + SplunkDataConstant.SPLUNK_DATATYPE_KEY + " : " + dataFormat);
			log.debug("Param - " + SplunkDataConstant.SPLUNK_SID_KEY + " : " + sid);
		
			NameValuePair[] params = null;
			NameValuePair param1 = null;
			NameValuePair param2 = null;
			NameValuePair param3 = null;
			NameValuePair param4 = null;

			param1 = new NameValuePair(SPLUNK_PARAM_SEARCH ,querySplunk);
			param2 = new NameValuePair(SPLUNK_PARAM_ID, sid);
			param3 = new NameValuePair(SPLUNK_PARAM_AUTOCANCEL, Integer.toString(SplunkDataConstant.SPLUNK_AUTO_CANCEL_TIME));
			param4 = new NameValuePair(SPLUNK_PARAM_MAXTIME, Integer.toString(SplunkDataConstant.SPLUNK_MAX_TIME));
			params = new NameValuePair[] { param1, param2, param3, param4 };
			xmlResponse = HttpRESTClient.postRESTHttp(SplunkDataConstant.REQUESTURI, uuid, params);
			log.debug("SplunkDataRest -> Rest response  creating Search to retrieve sid : " + xmlResponse);
			splunkSid = loadXmlData(xmlResponse, null, "sid");
			log.debug("SplunkDataRest -> Splunk splunkSid : " + splunkSid);
			if (null != splunkSid && splunkSid.length() > 0) {
				requestURI = SplunkDataConstant.REQUESTURI + SplunkDataConstant.SPLUNK_SLASH + splunkSid;
				waitingFromSplunk(requestURI);
				requestURI = requestURI + SplunkDataConstant.SPLUNK_SLASH + SplunkDataConstant.SPLUNK_RESULT_PATH;
				log.debug("SplunkDataRest -> requestURI : " + requestURI);
				if (!dataFormat.equalsIgnoreCase(SplunkDataFormat.XML.toString()) && 
						!dataFormat.equalsIgnoreCase(SplunkDataFormat.CSV.toString()) &&
						!dataFormat.equalsIgnoreCase(SplunkDataFormat.JSON.toString())) 
					dataFormat = SplunkDataFormat.JSON.toString();
				log.debug("SplunkDataRest -> Splunk dataFormat : " + dataFormat);
				param1 = new NameValuePair(SPLUNK_PARAM_OUTPUTMODE, dataFormat);
				params = new NameValuePair[] { param1 };
				splunkResponse = HttpRESTClient.getRESTHttp(requestURI, uuid,params);
//If the response have data populate the Table with them 
				if(null != splunkResponse && splunkResponse.length()>22){
					if(SplunkDataFormat.JSON.toString().equals(dataFormat)){
						JSONObject jobj = new JSONObject(splunkResponse);
						result = jobj.getString("results");
						result="{\"data\":" + result + "}";
					}else if (SplunkDataFormat.XML.toString().equals(dataFormat)){
						result = splunkResponse;
					}
					response.setContentType("application/" + dataFormat);
//					response.setCharacterEncoding(this.PAGE_CHARSET);
					log.debug("SplunkDataRest -> result : " + result);
					response.getWriter().write(result);
				}else
					response.sendError(HttpServletResponse.SC_NOT_FOUND, HTTP_RESOURCE_UNAVAILABLE);
			}else
				response.sendError(HttpServletResponse.SC_FORBIDDEN,ERROR_RETRIVE_SPLUNK_SID);
		} catch (InterruptedException e) {
			response.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT, ERROR_RETRIVE_SPLUNK_DATA);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, HTTP_SERVICE_UNAVAILABLE);
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private String loadXmlData(String xmlData, String path, String field)
			throws ConfigurationException, IOException {
		String value = "";
		XMLConfiguration config = new XMLConfiguration();
		config.load(new ByteArrayInputStream(xmlData.getBytes(this.PAGE_CHARSET)));
		if (path == null)
			value = config.getString(field);
		else {
			// String keyPath = "content.dict.key";
			String[] keyArray = config.getStringArray(path + "[@name]");
			for (int xi = 0; xi < keyArray.length; xi++) {
				String keyPrefix = path + "(" + xi + ")";
				String path2 = keyPrefix + "[@name]";
				value = config.getString(path2);
				if (field.equals(value)) {
					value = config.getString(keyPrefix);
					break;
				}
			}
		}
		return value;
	}

	private void waitingFromSplunk(String requestURI) throws Exception {
		String uuid = UUID.randomUUID().toString();
		String xmlResponse;
		InterruptedException ie;
		int i = 0;
		for (i = 0; i < SplunkDataConstant.SPLUNK_NUM_LOOP; i++) {
			log.debug("SplunkDataRest -> Dentro nel ciclo step : " + i);
			xmlResponse = HttpRESTClient.getRESTHttp(requestURI, uuid, null);
			if (xmlResponse.indexOf("<s:key name=\"dispatchState\">DONE</s:key>") > 0)
				break;
			Thread.sleep(SLEEP);
		}
		if (i == SplunkDataConstant.SPLUNK_NUM_LOOP){
			ie = new InterruptedException(this.ERROR_RETRIVE_SPLUNK_SID);
			ie.initCause(new Throwable(this.ERROR_RETRIVE_SPLUNK_DATA));
			throw ie;
		}
	}
}
