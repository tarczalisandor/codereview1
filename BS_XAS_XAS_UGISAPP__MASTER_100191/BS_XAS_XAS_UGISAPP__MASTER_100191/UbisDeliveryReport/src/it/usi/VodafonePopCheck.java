package it.usi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class VodafonePopCheck extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(VodafonePopCheck.class);

	// public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";
	
	public static final String EXPR_1 = "//SubmitReturn/@href";
	public static final String EXPR_2a = "//*[@id='";
	public static final String EXPR_2b = "']";
	public static final String EXPR_3 = "command_status";
	public static final String HREF = "href";
	public static final String TYPE = "xsi:type";
	public static final String INTEGER = "xsd:int";
	public static final int HTTP_OK = 200;
	//public static final String TARGET_URL = "Testvodafonepop.intranet.unicreditgroup.eu";
	//public static final String TARGET_URL = "Vodafonepop.intranet.unicreditgroup.eu";
	public static final String TARGET_URL = "localhost";
	public static final int TARGET_PORT = 80;
	public static final String REQUEST_URI = "/NEWGWService/services/GWService";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doAll(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doAll(request, response);
	}

	private void doAll(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//long millis = System.currentTimeMillis();
		Integer status = null;
		String postmessage = VodafoneTest.getInstance().getRequestString();
		String result = null;
        HttpClient client = new HttpClient();
        String connectionString = "http://" + TARGET_URL + ":" + TARGET_PORT + REQUEST_URI;
        PostMethod method = new PostMethod(connectionString);
        RequestEntity requestEntity = new StringRequestEntity(postmessage,"application/x-www-form-urlencoded","UTF-8");
        method.setRequestEntity(requestEntity);
        method.getParams().setVersion(HttpVersion.HTTP_1_1);
        method.setRequestHeader("Accept-Encoding", "gzip,deflate");
        method.setRequestHeader("Connection", "Keep-Alive");
        method.setRequestHeader("Accept-language", "en-us");
        method.setRequestHeader("SOAPAction","\"\"");
        method.setRequestHeader("Accept-charset", "iso-8859-1,*,utf-8");
        method.setRequestHeader("Accept", "*/*");
		PrintWriter out = response.getWriter();
        try {
            int retCode = client.executeMethod(method);
            if (retCode != HTTP_OK) throw new VodafonePopCheckException("HTTP code: " + retCode);
            result = method.getResponseBodyAsString();
        	//out.println("RISULTATO DELLA CHIAMATA<br />");
            //out.println(result);
    		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		Document document = builder.parse(new ByteArrayInputStream(result.getBytes("utf-8")));
    		XPath xPath = XPathFactory.newInstance().newXPath();
    		String id = xPath.compile(EXPR_1).evaluate(document);
    		if (id.startsWith("#")) id = id.substring(1);
    		else throw new VodafonePopCheckException("SubmitReturn's href is not an id");
    		String expression = EXPR_2a + id + EXPR_2b;
    		Node submitReturn = (Node)xPath.compile(expression).evaluate(document, XPathConstants.NODE);
    		if (submitReturn == null) throw new VodafonePopCheckException("id " + id + " not found");
    		String csHref = null;
    		Node commandStatus = (Node)xPath.compile(EXPR_3).evaluate(submitReturn, XPathConstants.NODE);
    		if (commandStatus == null) throw new VodafonePopCheckException("Command status not found");
    		NamedNodeMap attrs = commandStatus.getAttributes();
    		for (int i=0; i<attrs.getLength(); i++) {
    			Attr csAttr = (Attr)attrs.item(i);
    			if (HREF.equals(csAttr.getName())) csHref = csAttr.getValue();
    			if (TYPE.equals(csAttr.getName())) {
    				String csType = csAttr.getValue();
    				if (INTEGER.equals(csType)) {
    					status = new Integer(commandStatus.getTextContent());
    				} 
    			}
    		}
    		if (csHref == null && status == null) throw new VodafonePopCheckException("Neither href nor integer type found inside command_status tag");
    		if (status == null) {
    			if (csHref.startsWith("#")) csHref = csHref.substring(1);
    			else throw new VodafonePopCheckException("command_status' href is not an id");
    			expression = EXPR_2a + csHref + EXPR_2b;
    			Node statusNode = (Node)xPath.compile(expression).evaluate(document, XPathConstants.NODE);
    			if (statusNode == null) throw new VodafonePopCheckException("Status node not found");
    			attrs = statusNode.getAttributes();
    			for (int i=0; i<attrs.getLength(); i++) {
    				Attr snAttr = (Attr)attrs.item(i);
    				if (TYPE.equals(snAttr.getName())) {
    					String csType = snAttr.getValue();
    					if (INTEGER.equals(csType)) {
    						status = new Integer(statusNode.getTextContent());
    					} else throw new VodafonePopCheckException("Status Node: integer type expected, found: " + csType);
    				}
    			}
    		}
    		if (status == null) throw new VodafonePopCheckException("Status not found"); 
    		int intStatus = status.intValue();
    		if (intStatus == 0) ok(out);
    		else ko(out,"value = " + intStatus);
    		//long millis1 = System.currentTimeMillis();
    		//out.println("Durata in ms: " + (millis1 - millis));

    		
        } catch (ConnectException e) {
			ko(response.getWriter(),"ConnectException: message = " + e.getMessage());
        } catch (ParserConfigurationException e) {
			ko(response.getWriter(),"ParserConfigurationException: message = " + e.getMessage());
		} catch (SAXException e) {
			ko(response.getWriter(),"SAXException: message = " + e.getMessage());
		} catch (XPathExpressionException e) {
			ko(response.getWriter(),"XPathExpressionException: message = " + e.getMessage());
		} catch (VodafonePopCheckException e) {
			ko(response.getWriter(),e.getMessage());
		}
	}
	
	private void ok(PrintWriter out) {
		out.println("STATUS OK");
	}
	
	private void ko(PrintWriter out, String message) {
		out.println("STATUS KO: " + message);
	}

}