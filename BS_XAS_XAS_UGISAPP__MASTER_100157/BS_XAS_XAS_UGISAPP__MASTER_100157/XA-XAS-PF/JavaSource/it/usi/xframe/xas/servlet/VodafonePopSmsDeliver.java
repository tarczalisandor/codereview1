package it.usi.xframe.xas.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * Servlet implementation class VodafonePopSmsDeliver
 */
public class VodafonePopSmsDeliver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger log = Logger.getLogger(VodafonePopSmsDeliver.class);


	private static final String LOG_PREFIX = "VODAFONEPOPSOAPSMS";
	
	private static final String LOG_LEVEL_NONE = "dbglevel=none";
	private static final String LOG_LEVEL_DEFAULT = "dbglevel=default";
	private static final String LOG_LEVEL_DEBUG = "dbglevel=full";

	//public static final String PAGE_CHARSET = "iso-8859-1";
	public static final String PAGE_CHARSET = "UTF-8";

	class BufferedServletInputStream extends ServletInputStream {
		 
	    ByteArrayInputStream bais;
	 
	    public BufferedServletInputStream(ByteArrayInputStream bais) {
	      this.bais = bais;
	    }
	 
	    public int available() {
	      return bais.available();
	    }
	 
	    public int read() {
	      return bais.read();
	    }
	 
	    public int read(byte[] buf, int off, int len) {
	      return bais.read(buf, off, len);
	    }
	 
	  }
	 
	class BufferedRequestWrapper extends HttpServletRequestWrapper {
		 
	    ByteArrayInputStream bais;
	 
	    ByteArrayOutputStream baos;
	 
	    BufferedServletInputStream bsis;
	 
	    byte[] buffer;
	 
	    public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
	      super(req);
	      InputStream is = req.getInputStream();
	      baos = new ByteArrayOutputStream();
	      byte buf[] = new byte[1024];
	      int letti;
	      while ((letti = is.read(buf)) > 0) {
	        baos.write(buf, 0, letti);
	      }
	      buffer = baos.toByteArray();
	    }
	 
	    public ServletInputStream getInputStream() {
	      try {
	        bais = new ByteArrayInputStream(buffer);
	        bsis = new BufferedServletInputStream(bais);
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }
	 
	      return bsis;
	    }
	 
	    public byte[] getBuffer() {
	      return buffer;
	    }
	 
	  }

	/**
     * @see HttpServlet#HttpServlet()
     */
    public VodafonePopSmsDeliver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAll(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAll(request, response);
	}

	private void doAll (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        String destinator = null;
        String messageId = null;
        String messageState = null;
        String status = null;
        String errCode = null;
        String errMsg = null;
        
        ParseDeliveryReport p = new ParseDeliveryReport();
        try
        {
    		BufferedRequestWrapper bufferedRequest= new BufferedRequestWrapper(request);
    		String soapMessage = new String(bufferedRequest.getBuffer());
            log.debug("REQUEST -> " + soapMessage);

            p.read(soapMessage);

	        destinator = p.getDestinator();
	        messageId = p.getMessageId();
	        messageState = p.getMessageState();
        }
        catch(Exception e)
        {
        	log.error(e);
        	e.printStackTrace();
	        destinator = null;
	        messageId = null;
	        messageState = null;
        }

        if(messageState==null) {
        	errCode = messageState;
        	errMsg = "Error during delivery processing";
        	status = "ERR";
        }
        else if(messageState.equals("1")) {
        	errCode = messageState;
        	errMsg = "ENROUTE: The message is in enroute state";
        	status = "OK";
        }
        else if(messageState.equals("2")) {
        	errCode = messageState;
        	errMsg = "DELIVERED: Message is delivered to destination";
        	status = "OK";
        }
        else if(messageState.equals("3")) {
        	errCode = messageState;
        	errMsg = "EXPIRED: Message validity period has expired";
        	status = "ERR";
        }
        else if(messageState.equals("4")) {
        	errCode = messageState;
        	errMsg = "DELETED: Message has been deleted";
        	status = "ERR";
        }
        else if(messageState.equals("5")) {
        	errCode = messageState;
        	errMsg = "UNDELIVERABLE: Message is undeliverable";
        	status = "ERR";
        }
        else if(messageState.equals("6")) {
        	errCode = messageState;
        	errMsg = "ACCEPTED: Message is in accepted state (i.e. has been manually read on behalf of the subscriber by customer service)";
        	status = "OK";
        }
        else if(messageState.equals("7")) {
        	errCode = messageState;
        	errMsg = "UNKNOWN: Message is in invalid state";
        	status = "ERR";
        }
        else if(messageState.equals("8")) {
        	errCode = messageState;
        	errMsg = "REJECTED: Message is in a rejected state";
        	status = "ERR";
        }
        else {
        	errCode = messageState;
        	errMsg = "?";
        	status = "ERR";
        }
    
        // log
        securityDeliveryLog(status, errMsg, errCode, destinator, messageId);
        
		response.setContentType("text/html;charset=" + PAGE_CHARSET);

		PrintWriter out = response.getWriter();
		
		out.println("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:SoapSmppGW\">");
		out.println("<soapenv:Header/>");
		out.println("<soapenv:Body>");
		out.println("<urn:DeliverResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
		out.println("<DeliverReturn xsi:type=\"urn:Deliver_resp\">");
		out.println("<command_status xsi:type=\"xsd:int\">0</command_status>");
		out.println("<error_code xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">OK</error_code>");
		out.println("<message_id xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">EMPTY</message_id>");
		out.println("</DeliverReturn>");
		out.println("</urn:DeliverResponse>");
		out.println("</soapenv:Body>");
		out.println("</soapenv:Envelope>");		
	}
	
	//	this log is used by Security Office
	private void securityDeliveryLog(String status, String description, String errCode, String destinatorPhoneNumber, String msgid)
	{
		String userid = null;
		
		// common logging utility
		StringBuffer sb = new StringBuffer();
		sb.append(",smslogprefix=" + LOG_PREFIX);
		sb.append(",smsstatus=" + status);
		sb.append(",errormessage=" + description);
		sb.append("," + LOG_LEVEL_DEFAULT);
		sb.append(",dstphonenumber=" + destinatorPhoneNumber);
		sb.append(",smsgatewayresponse=" + errCode);
		sb.append(",smsid=" + msgid);

		log.info(sb);
	}

}
