package it.usi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class VodafonePopSmsDeliver extends HttpServlet
{
	private Properties configuration;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(VodafonePopSmsDeliver.class);
//	private static final String LOG_PREFIX = "VODAFONEPOPSOAPSMS";
//	private static final String APPL_ID = "XAS";
//	private static final String LOG_VER = "1.0";
//	private static final String SRC_ABI = "VODAFONE_DR";
//
//	private static final String LOG_LEVEL_NONE = "a_dbglevel=\"none\"";
//	private static final String LOG_LEVEL_DEFAULT = "a_dbglevel=\"default\"";
//	private static final String LOG_LEVEL_DEBUG = "a_dbglevel=\"full\"";

	// public static final String PAGE_CHARSET = "iso-8859-1";
//	public static final String PAGE_CHARSET = "UTF-8";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doAll(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doAll(request, response);
	}

	private void doAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
			BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(request);
			String soapMessage = new String(bufferedRequest.getBuffer());
			log.debug("REQUEST -> " + soapMessage);

			// parse the delivery-report
			p.read(soapMessage);

			destinator = p.getDestinator();
			messageId = p.getMessageId();
			messageState = p.getMessageState();
		}
		catch (Exception e)
		{
			log.error(e);
			e.printStackTrace();
			destinator = null;
			messageId = null;
			messageState = null;
		}

//		if (messageState == null) {
//			errCode = messageState;
//			errMsg = "Error during delivery processing";
//			status = Constants.V_STATUS_ERROR;
//		} else if (messageState.equals("1")) {
//			errCode = messageState;
//			errMsg = "ENROUTE: The message is in enroute state";
//			status = Constants.V_STATUS_OK;
//		} else if (messageState.equals("2")) {
//			errCode = messageState;
//			errMsg = "DELIVERED: Message is delivered to destination";
//			status = Constants.V_STATUS_OK;
//		} else if (messageState.equals("3")) {
//			errCode = messageState;
//			errMsg = "EXPIRED: Message validity period has expired";
//			status = Constants.V_STATUS_ERROR;
//		} else if (messageState.equals("4")) {
//			errCode = messageState;
//			errMsg = "DELETED: Message has been deleted";
//			status = Constants.V_STATUS_ERROR;
//		} else if (messageState.equals("5")) {
//			errCode = messageState;
//			errMsg = "UNDELIVERABLE: Message is undeliverable";
//			status = Constants.V_STATUS_ERROR;
//		} else if (messageState.equals("6")) {
//			errCode = messageState;
//			errMsg = "ACCEPTED: Message is in accepted state (i.e. has been manually read on behalf of the subscriber by customer service)";
//			status = Constants.V_STATUS_OK;
//		} else if (messageState.equals("7")) {
//			errCode = messageState;
//			errMsg = "UNKNOWN: Message is in invalid state";
//			status = Constants.V_STATUS_ERROR;
//		} else if (messageState.equals("8")) {
//			errCode = messageState;
//			errMsg = "REJECTED: Message is in a rejected state";
//			status = Constants.V_STATUS_ERROR;
//		} else {
//			errCode = messageState;
//			errMsg = "?";
//			status = Constants.V_STATUS_ERROR;
//		}
		
		if (messageState == null) {
			errCode = messageState;
			errMsg = "Error during delivery processing";
			status = Constants.V_STATUS_ERROR;
		} else {
			if (/*messageState.length() == 1 & */messageState.matches("^\\d$")) {
				switch(Integer.parseInt(messageState)) {
					case Constants.V_MESSAGE_STATE_ONE:
						errCode = messageState;
						errMsg = "ENROUTE: The message is in enroute state";
						status = Constants.V_STATUS_OK;
						break;
					case Constants.V_MESSAGE_STATE_TWO:
						errCode = messageState;
						errMsg = "DELIVERED: Message is delivered to destination";
						status = Constants.V_STATUS_OK;
						break;
					case Constants.V_MESSAGE_STATE_THREE:
						errCode = messageState;
						errMsg = "EXPIRED: Message validity period has expired";
						status = Constants.V_STATUS_ERROR;
						break;
					case Constants.V_MESSAGE_STATE_FOUR:
						errCode = messageState;
						errMsg = "DELETED: Message has been deleted";
						status = Constants.V_STATUS_ERROR;
						break;
					case Constants.V_MESSAGE_STATE_FIVE:
						errCode = messageState;
						errMsg = "UNDELIVERABLE: Message is undeliverable";
						status = Constants.V_STATUS_ERROR;
						break;
					case Constants.V_MESSAGE_STATE_SIX:
						errCode = messageState;
						errMsg = "ACCEPTED: Message is in accepted state (i.e. has been manually read on behalf of the subscriber by customer service)";
						status = Constants.V_STATUS_OK;
						break;
					case Constants.V_MESSAGE_STATE_SEVEN:
						errCode = messageState;
						errMsg = "UNKNOWN: Message is in invalid state";
						status = Constants.V_STATUS_ERROR;
						break;
					case Constants.V_MESSAGE_STATE_EIGHT:
						errCode = messageState;
						errMsg = "REJECTED: Message is in a rejected state";
						status = Constants.V_STATUS_ERROR;
						break;
				}
			} else {
				errCode = messageState;
				errMsg = "?";
				status = Constants.V_STATUS_ERROR;
			}
		}

		// log
		securityDeliveryLog(status, errMsg, errCode, destinator, messageId);

		response.setContentType("text/html;charset=" + Constants.PAGE_CHARSET);

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

	private void securityDeliveryLog(String status, String description, String errCode, String destinatorPhoneNumber, String msgid)
	{
		try {
			String filter = getConfiguration().getProperty(VodafonePopCheck.DESTINATION_NUMBER);
			if (filter.equals(destinatorPhoneNumber)) return;
		} catch (IOException e) {}
		
		String userid = null;

		StringBuffer sb = new StringBuffer();
		sb.append(Constants.K_SMSLOGPREFIX   + "=\"" + Constants.V_LOG_PREFIX    + "\",");
		sb.append(Constants.K_APPLID         + "=\"" + Constants.V_APPL_ID       + "\",");
		sb.append(Constants.K_LOGVER         + "=\"" + Constants.V_LOG_VER       + "\",");
		sb.append(Constants.K_SCOPE          + "=\"" + Constants.V_SCOPE         + "\",");
		sb.append(Constants.K_SRCABI         + "=\"" + Constants.V_SRC_ABI       + "\",");
		sb.append(Constants.K_SMSSTATUS      + "=\"" + status                    + "\",");
		sb.append(Constants.K_ERRMESSAGE     + "=\"" + description               + "\",");
		sb.append(Constants.K_DSTPHONENUM    + "=\"" + destinatorPhoneNumber     + "\",");
		sb.append(Constants.K_SMSGATEWAYRESP + "=\"" + errCode                   + "\",");
		sb.append(Constants.K_SMSID          + "=\"" + msgid                     + "\",");
		sb.append(Constants.K_DBGLEVEL       + "=\"" + Constants.V_LEVEL_DEFAULT + "\"");

		log.info(sb);
	}

	private Properties getConfiguration() throws IOException {
		if (configuration == null) {
			Properties props = new Properties();
			props.load(getServletContext().getResourceAsStream(VodafonePopCheck.PROPERTIES_FILE));
			configuration = props;
		}
		return configuration;
	}

	class BufferedRequestWrapper extends HttpServletRequestWrapper
	{
		ByteArrayInputStream bais;
		ByteArrayOutputStream baos;
		VodafonePopSmsDeliver.BufferedServletInputStream bsis;
		byte[] buffer;

		public BufferedRequestWrapper(HttpServletRequest req) throws IOException
		{
			super(req);
			InputStream is = req.getInputStream();
			this.baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0)
			{
				this.baos.write(buf, 0, letti);
			}
			this.buffer = this.baos.toByteArray();
		}

		public ServletInputStream getInputStream()
		{
			try
			{
				this.bais = new ByteArrayInputStream(this.buffer);
				this.bsis = new VodafonePopSmsDeliver.BufferedServletInputStream(bais);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}

			return this.bsis;
		}

		public byte[] getBuffer() {
			return this.buffer;
		}
	}

	class BufferedServletInputStream extends ServletInputStream
	{
		ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		public int available() {
			return this.bais.available();
		}

		public int read() {
			return this.bais.read();
		}

		public int read(byte[] buf, int off, int len) {
			return this.bais.read(buf, off, len);
		}
	}
}