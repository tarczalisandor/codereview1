package it.usi.xframe.xas.wsutil.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This servlet Filter analyzes the inbound http request searching for SOAP 1.1 or 1.2 Security tokens of type:
 * WSSE UsernameToken (OASIS Specification 2004/01 or Draft 2003/06).
 * <p>
 * Depending on value of Init Parameters <pre>fromUT</pre> and <pre>newUT</pre> (accepted values: '2003' and '2004')
 * it transforms the matching UsernameToken, fromUT, in order to target the newUT specification format.
 * <p>
 * If no matching inbound UsernameToken is found or no suitable type of http request is detected, the filter just let
 * the HttpServletRequest untouched for next chain processing. 
 * <p>
 * The main purpose of this filter is to maintain backward compatibility for XFrame 1.0 JAX-RPC web-services using IBM
 * JAX-RPC SOAP engine runtime.
 * 
 * @author C305393
 *
 */
public class InboundWSSEFilter implements Filter {

	private static Log log = LogFactory.getLog(InboundWSSEFilter.class);
	
	private static final String P = InboundWSSEFilter.class.getName() + "::";
	
	//private ServletContext ctx;
	private String fromUT = null;
	private String newUT = null;
	
	/**
	 * Filter initializer method. It is called ONCE per instance of this class.
	 * 
	 * @Override
	 */
	public void init(FilterConfig config) throws ServletException
	{
		log.debug(P+"init() <-begin");
		//ctx = config.getServletContext();
		//log.debug(P+"ServletContextName="+this.ctx.getServletContextName());
		//log.debug(P+"ServerInfo="+this.ctx.getServerInfo());
		
		fromUT = config.getInitParameter("fromUT");
		if (fromUT == null) {
			throw new ServletException(P+"init(): missing Init Parameter: fromUT");
		}
		newUT = config.getInitParameter("newUT");
		if (newUT == null) {
			throw new ServletException(P+"init(): missing Init Parameter: newUT");
		}
		if (! (fromUT.equals("2004") || fromUT.equals("2003"))) {
			throw new ServletException(P+"init(): wrong value for Init parameter 'fromUT': " + fromUT);
		}
		if (! (newUT.equals("2004") || newUT.equals("2003"))) {
			throw new ServletException(P+"init(): wrong value for Init parameter 'newUT': " + newUT);
		}
		log.debug(P+"init() ->end");
	}

	/**
	 * @Override
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
		throws IOException, ServletException
	{
		log.debug(P+"doFilter() <-begin");
		HttpServletRequest http_req = (HttpServletRequest)req;
		/*C1*--/
		if (log.isDebugEnabled()) {
			log.debug(P+"CharacterEncoding="+req.getCharacterEncoding());
			log.debug(P+"ContentType="+req.getContentType());
			log.debug(P+"ContentLength="+req.getContentLength());
			log.debug(P+"RemoteAddr="+req.getRemoteAddr());
			log.debug(P+"RemoteHost="+req.getRemoteHost());
			log.debug(P+"RemotePort="+req.getRemotePort());
			log.debug(P+"ServerName="+req.getServerName());
			log.debug(P+"ServerPort="+req.getServerPort());
			log.debug(P+"ContextPath="+http_req.getContextPath());
			log.debug(P+"Method="+http_req.getMethod());
			log.debug(P+"PathInfo="+http_req.getPathInfo());
			log.debug(P+"RemoteUser="+http_req.getRemoteUser());
			log.debug(P+"RequestURI="+http_req.getRequestURI());
			log.debug(P+"SessionId="+http_req.getSession().getId());
			log.debug(P+"UserPrincipal="+http_req.getUserPrincipal());
		}
		/*C1*/
		
		ServletRequest servlet_req = http_req; // by default, do not filter at all
		
		//From SOAP 1.1 spec:
		//-- The presence and content of the SOAPAction header field can be used by servers such as firewalls
		//-- to appropriately filter SOAP request messages in HTTP.
		//-- The header field value of empty string ("") means that the intent of the SOAP message is provided
		//-- by the HTTP Request-URI. No value means that there is no indication of the intent of the message.

		String content_type = http_req.getContentType(); //Content-type: text/html;charset='UTF-8'
		if (content_type != null) //-- this means Content-type header is present on the http_request
		{
			boolean soap11Request = false;
			boolean soap12Request = false;

			String soap_action = http_req.getHeader("SOAPAction");
			String type_subtype = content_type;
			int i;
			if ((i = content_type.indexOf(';')) >= 0) {
				type_subtype = content_type.substring(0, i).trim();
			}
			//-- Mime type/subtype are case insensitive!
			if (type_subtype.equalsIgnoreCase("text/xml") && soap_action != null) {
				log.debug(P+"doFilter(): SOAP 1.1 request detected.");
				soap11Request = true;
			} else if (type_subtype.equalsIgnoreCase("application/soap+xml")) {
				log.debug(P+"doFilter(): SOAP 1.2 request detected.");
				soap12Request = true;
			}
			
			if (soap11Request || soap12Request) {
				InboundWSSERequestWrapper wrappedReq = new InboundWSSERequestWrapper( http_req );
				
				if (this.fromUT.equals("2004")) {
					wrappedReq.setFromWSSE2004();
				} else if (this.fromUT.equals("2003")) {
					wrappedReq.setFromWSSE2003();
				}
				if (this.newUT.equals("2004")) {
					wrappedReq.setToWSSE2004();
				} else if (this.newUT.equals("2003")) {
					wrappedReq.setToWSSE2003();
				}
				servlet_req = wrappedReq;
			}
		} // else may be this is a wsdl GET, or the like...
		
		try {
			chain.doFilter(servlet_req, resp);
		} catch (IOException ioe) {
			// io-exceptions are not clearly propagated to the http client...
			throw new ServletException(P+" caught: "
				+ ioe.getClass().toString() + ": " + ioe.getMessage());
		}
		
		log.debug(P+"doFilter() ->end");
	}

	/**
	 * @Override
	 */
	public void destroy() {
		log.debug(P+"destroy() invoked");
		// Nothing to clean up
	}
}

/**
 * The specialized HttpServletRequestWrapper handling WSSE UsernameToken filtering.
 * 
 */
class InboundWSSERequestWrapper extends HttpServletRequestWrapper {

	private static Log log = LogFactory.getLog(InboundWSSERequestWrapper.class);
	
	private static final String P = InboundWSSERequestWrapper.class.getName() + "::";
	
	private static final String UT = "UsernameToken";
	
	private static final String PW = "Password";
	
	private static final String WSSE2004_URI =
		"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	
	private static final String WSSE2004_PWDTEXT_URI =
		"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";

	private static final String WSSE2003_URI =
		"http://schemas.xmlsoap.org/ws/2003/06/secext";

	private static final String WSSE2003_PWDTEXT_URI = "PasswordText";
	
	public InboundWSSERequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	private boolean fromWSSE2003 = false;
	private boolean fromWSSE2004 = false;
	private boolean toWSSE2003 = false;
	private boolean toWSSE2004 = false;

	void setFromWSSE2004() {
		this.fromWSSE2003 = false;
		this.fromWSSE2004 = true;
	}

	void setFromWSSE2003() {
		this.fromWSSE2004 = false;
		this.fromWSSE2003 = true;
	}
	
	void setToWSSE2004() {
		this.toWSSE2003 = false;
		this.toWSSE2004 = true;
	}

	void setToWSSE2003() {
		this.toWSSE2004 = false;
		this.toWSSE2003 = true;
	}

	private boolean isFromWSSE2003() {
		return this.fromWSSE2003;
	}

	private boolean isFromWSSE2004() {
		return this.fromWSSE2004;
	}
	
	private boolean isToWSSE2003() {
		return this.toWSSE2003;
	}

	private boolean isToWSSE2004() {
		return this.toWSSE2004;
	}
	
	private String getFromNamespaceURI() {
		String uri = null;
		if (isFromWSSE2003()) {
			uri = WSSE2003_URI;
		} else if (isFromWSSE2004()) {
			uri = WSSE2004_URI;
		}
		return uri;
	}
	
	private String getNewNamespaceURI() {
		String uri = null;
		if (isToWSSE2003()) {
			uri = WSSE2003_URI;
		} else if (isToWSSE2004()) {
			uri = WSSE2004_URI;
		}
		return uri;
	}
	
	private String getNewPwdTypeURI(String prefix) {
		String uri = null;
		if (isToWSSE2004()) {
			uri = WSSE2004_PWDTEXT_URI;
		} else if (isToWSSE2003()) {
			uri = prefix + ":" + WSSE2003_PWDTEXT_URI;
		}
		return uri;
	}
	
	private int contentLenght = this.getRequest().getContentLength();
	
	private void setContentLength(int length ) {
		this.contentLenght = length;
	}
	
	/**
	 * @override 
	 */
	public int getContentLength() {
		return contentLenght;
	}
	
	/**
	 * Filter chain will call either this method or getReader(), not both.
	 * 
	 * @Override
	 */
	public ServletInputStream getInputStream() throws IOException {
		log.debug(P+"getInputStream() <-enter");
		long begin = System.currentTimeMillis();
		
		ServletInputStream is = super.getInputStream(); // !invoke the super impl: DO NOT be recursive!
		char[] cbuf = new char[512];
		int readchno = 0;
		// char_enc from http header (Content-type): may be null if not present
		String char_enc = super.getCharacterEncoding();
		if (char_enc == null) {
			readchno = charEncDetection(is, cbuf);
			char_enc = getCharacterEncoding(); // !get the detected charset
		}
		log.debug(P+"getInputStream(): char_enc="+char_enc);
		/* rb will contain the http request body */
		StringBuffer rb = new StringBuffer( this.getContentLength() + 128);
		if (readchno > 0) {
			// let's append the body first chunk (read-ahead for encoding detection)
			rb.append(cbuf, 0, readchno);
		}
		
		BufferedReader reqReader = new BufferedReader(new InputStreamReader(is, char_enc));
		try {					
			while ((readchno = reqReader.read(cbuf)) != -1) {
				rb.append(cbuf, 0, readchno);
			}
		} finally {
			reqReader.close();
		}
		//-- Now the http request body buffer is ready to be analyzed/transformed in place
		if (log.isDebugEnabled()) {
			log.debug("-------- Inbound http_req content begin --------");
			log.debug(rb.toString());
			log.debug("-------- Inbound http_req content end --------");
			log.debug("fromNamespaceURI=" + this.getFromNamespaceURI());
			log.debug("newNamespaceURI=" + this.getNewNamespaceURI());
		}
		
		boolean prefixedUTFound = false;
		String utPrefix = null;
		int i_ut = -1;
		int i_ns = -1;
		int iter_cnt = 0;
		
		/*-- Check if the new nameSpace is already present in the soap header
		  -- If exists no change will be made
		  -- otherwise apply the filter role   
		 */
		if(rb.indexOf(this.getNewNamespaceURI())>0){
			log.debug("New namespaceURI already exists");
			byte[] binary_rb = rb.toString().getBytes(char_enc);
			setContentLength( binary_rb.length );
			
			log.debug(P+"getInputStream() ->exit - elapsed (ms):" + (System.currentTimeMillis() - begin));
			return new ServletInputStreamWrapper(new ByteArrayInputStream(binary_rb));
		}

		/*-- In this loop we scan the request body searching for a prefixed UsernameToken
		  -- belonging to the target namespace.
		  -- Default namaspace qualified UTs are not considered!  */
		while (!prefixedUTFound)
		{
			if (iter_cnt++ > 200) {  // just to be paranoid preventing infinite loops
				log.error("UsernameToken searching loop was interrupted after 200 iterations.");
				break;
			}
			int fromIndex = (iter_cnt == 1 ? 0 : i_ut + UT.length());  
			if ((i_ut = rb.indexOf(UT, fromIndex)) < 0) {
				break; // no more 'UsernameToken' in request body: exit main loop
			}
			utPrefix = null;
			char c = rb.charAt(i_ut-1);
			switch (c) {
				case '<':
					log.warn("Found a <UsernameToken> element not prefixed: skipping it out.");
					continue; // not a fully qualified <UsernameToken>
				case ':':
					int i2 = rb.lastIndexOf("<", i_ut - 1);
					if (rb.charAt(i2 + 1) == '/') { // this is a closure ut tag: (</wsse:UsernameToken>...) skip it
						continue;
					}
					utPrefix = rb.substring(i2+1, i_ut-1);							
					log.debug("utPrefix="+utPrefix);
					break;
				default:
					continue;
			}
			if (utPrefix == null) { //-- redundant safe check
				//-- should never arrive here
				continue;
			}
			//-- once got the UsernameToken prefix, let's search for 
			//-- its namespace declaration
			String decl_attr = "xmlns:" + utPrefix + "=";
			int i_nsdecl = rb.lastIndexOf(decl_attr, i_ut);
			if (i_nsdecl < 0 || !isSpace(rb.charAt(i_nsdecl - 1)))
			{
				log.warn("Expected ns declaration was not found: " + decl_attr);
				continue;
			}
			char attValueDelim = rb.charAt(i_nsdecl + decl_attr.length()); // attribute Value Delimiter (single ['] or double quote ["])
			if (isAttValueDelim(attValueDelim)) {
				int i_delimEnd = rb.indexOf(String.valueOf(attValueDelim), i_nsdecl + decl_attr.length() + 1);
				String nsDeclValue = rb.substring(i_nsdecl + decl_attr.length() + 1, i_delimEnd);
				log.debug("declared UT namespace: " + nsDeclValue);
				if (nsDeclValue.equalsIgnoreCase( getFromNamespaceURI() )) {
					prefixedUTFound = true;
					i_ns = i_nsdecl + decl_attr.length() + 1;
				}				
			}
		} //-- while loop
		log.debug("iter_cnt=" + iter_cnt);
		if (prefixedUTFound)
		{
			log.debug("go for the namespace declaration substitution");
			rb.replace(i_ns, i_ns + getFromNamespaceURI().length(), getNewNamespaceURI());
			
			//-- ok, now try to translate the Type attribute of Password element, if any
			int i_ut_closure = rb.indexOf("</" + utPrefix + ":" + UT, i_ut);
			int i_pw = rb.indexOf("<" + utPrefix + ":" + PW, i_ut);
			int i_pw_closure = rb.indexOf("</" + utPrefix + ":" + PW, i_ut);
			
			if ((i_ut_closure > -1) &&
				(i_pw > i_ut) &&
				(i_pw < i_ut_closure) &&
				(i_pw_closure < i_ut_closure) &&
				(i_pw < i_pw_closure))
			{
				int i_type = rb.indexOf("Type=", i_pw);
				if ((i_type > i_pw) &&
					(i_type < i_pw_closure) &&
					isSpace(rb.charAt(i_type - 1)))
				{
					char attValueDelim = rb.charAt(i_type + 5); // attribute Value Delimiter (single ['] or double quote ["])
					if (isAttValueDelim(attValueDelim)) {
						int i_delimEnd = rb.indexOf(String.valueOf(attValueDelim), i_type + 6);
						String typeAttrValue = rb.substring(i_type+6, i_delimEnd);
						if (log.isDebugEnabled()) {
							log.debug("declared Password Type: " + typeAttrValue);
							log.debug("go for the Password Type attribute substitution");						
						}
						rb.replace(i_type + 6, i_type + 6 + typeAttrValue.length(), getNewPwdTypeURI(utPrefix));						
					}
				}
			}
			
			if (log.isDebugEnabled()) {
				log.debug("-------- Transformed http_req content begin --------");
				log.debug(rb.toString());
				log.debug("-------- Transformed http_req content end --------");
			}
		}
		byte[] binary_rb = rb.toString().getBytes(char_enc);
		setContentLength( binary_rb.length );
		
		log.debug(P+"getInputStream() ->exit - elapsed (ms):" + (System.currentTimeMillis() - begin));
		return new ServletInputStreamWrapper(new ByteArrayInputStream(binary_rb));
	}

	/**
	 * Filter chain will call either this method or getInputStream(), not both.
	 * 
	 * @Override
	 */
	public BufferedReader getReader() throws IOException {
		log.debug(P+"getReader() <-enter");
		//-! Invoke this overridden impl, it applies the filter logic
		ServletInputStream is = this.getInputStream();
		//-! should never return null, because character encoding 
		//-- is also detected internally by this.getInputStream()
		String char_enc = getCharacterEncoding();
		log.debug(P+"getReader() ->exit");
		return new BufferedReader(new InputStreamReader(is, char_enc));
	}
	
	/**
	 * @return true, if given char c is a valid Space according to XML 1.0 specification,
	 *         false otherwise.
	 */
	private boolean isSpace(char c) {
		boolean sp = true;
		switch (c) {
			case '\040': break;	// space
			case '\011': break;	// horizontal tab
			case '\015': break;	// carriage return
			case '\012': break;	// line feed
			default: sp = false;
		}
		return sp;
	}
	
	/**
	 * @return true, if given char c is a valid Attribute Value Delimiter according to XML 1.0 specification,
	 *         false otherwise.
	 */
	private boolean isAttValueDelim(char c) {
		boolean vd = true;
		switch (c) {
			case '\042': break; // double quote
			case '\047': break; // single quote
			default: vd = false;
		}
		return vd;
	}
	
	/* charEncDetection(): NOTE for WAS runtime
	 * ----------------------------------------
	 * The V6.1+ JAX-RPC web services code tolerates only following character encoding:
	 * ISO-8859-1, UTF-8 and UTF-16. Note that the SAAJ 1.2 and 1.3
	 * specifications require only UTF-8 and UTF-16.
	 * 
	 * That said, the below charEncDetection() method is invoked only when the charset
	 * is not specified in http Content-type header (that is the normal way for soap/http),
	 * and this means that the filter could work also for untolerated encodings, but when
	 * the inputstream is propagated onward to filter chain, the untolerated encoding
	 * will cause a:
	 * 
	 * "Error: Exception:java.lang.Exception: WSWS3710E: Internal Error: Attempt to change
	 * encoding on WebServicesInputSource".
	 * 
	 * This behavior depends on WAS JAX-RPC web services. Anyway here we try to detect at
	 * best effort, independently of target jax-rpc runtime limitations.
	 */
	/**
	 * Try to detect the actual charset/encoding  of the inbound HttpServlet
	 * inputStream reading ahead the inputStream first bytes.
	 * 
	 * Once the charset/encoding is detected, copy into cbuffer the byte-decoded
	 * characters just read from the inputStream.
	 * (ServletInputStream cannot be re-read: mark, reset are no-ops).
	 * And, as side effect, it sets the CharacterEncoding for this wrapper. 
	 * 
	 * @see http://www.w3.org/TR/REC-xml/#sec-guessing
	 * @see http://nikitathespider.com/articles/EncodingDivination.html
	 * 
	 * Here we assume that the inbound request is SOAP/XML over HTTP.
	 * This assumption relies on fact that this InboundWSSERequestWrapper is used
	 * only if the parent InboundWSSEFilter detects a soap11 or soap12 request.
	 * 	 
	 * @return the number of read-ahead charactes (copied into cbuffer) or -1.
	 */
	private int charEncDetection(ServletInputStream is, char[] cbuffer)
			throws IOException {
		log.debug(P + "charEncDetection() <-enter");
		if (cbuffer.length < 64) {
			throw new IllegalArgumentException("cbuffer.length must be at least 64");
		}
		final String UTF8 = "UTF-8";
		final String UTF16 = "UTF-16";
		final String UTF32 = "UTF-32";
		final String IBM1047 = "IBM1047";

		final int xFF = 255; // BOM
		final int xFE = 254; // BOM
		//-- At the beginning of the stream we expect an xml declaration: <?xml...		
		final int x4C = 76;  //ebcdic '<'
		final int x6F = 111; //ebcdic '?'
		final int xA7 = 167; //ebcdic 'x'
		final int x94 = 148; //ebcdic 'm'
		final int x93 = 147; //ebcdic 'l'
		final int x3C = 60;  //ascii '<'
		final int x3F = 63;  //ascii '?'
		final int x78 = 120; //ascii 'x'
		final int x6D = 109; //ascii 'm'
		final int x6C = 108; //ascii 'l'
		
		int readChars = -1;
		//-- http rfc states that the default body encoding should be ISO-8859-1
		//-- if not declared in http header, anyway here we expect an application
		//-- generated SOAP/XML body, so let stay with XML default encoding as default.
		String detected = UTF8;
		
		byte[] b;
		int b1 = is.read();
		int b2 = -1, b3 = -1, b4 = -1, b5 = -1;
		
		switch (b1) {
			case -1:
				break;
			case xFF:
				if ((b2 = is.read()) == -1) break;
				if (b2 == xFE) {
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if (b3 == 0 && b4 == 0) {  // UTF-32LE BOM
						detected = UTF32; // Likely WAS JAX-RCP runtime doesn't tolerate UTF-32, and will crash afterwards
					} else { // UTF-16LE BOM + one char
						detected = UTF16;
					}
					b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
					readChars = decodeSeq(detected, b, cbuffer, 0);
				}
				break;
			case xFE:
				if ((b2 = is.read()) == -1) break;
				if (b2 == xFF) {
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if (b3 != 0 || b4 != 0) { // UTF-16BE BOM + one char
						detected = UTF16;
						b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
						readChars = decodeSeq(detected, b, cbuffer, 0);
					}
				}
				break;
			case 0:
				if ((b2 = is.read()) == -1) break;
				if ((b2 == 0) || (b2 == x3C)) {
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if (b2 == 0) {
						if ((b3 == xFE && b4 == xFF) || (b3 == 0 && b4 == x3C)) { // UTF-32BE
							detected = UTF32; // Likely WAS JAX-RCP runtime doesn't tolerate UTF-32, and will crash afterwards
						}
					} else if (b2 == x3C) { // '<'
						if ((b3 != 0) || (b4 != 0)) { // UTF-16BE
							detected = UTF16;
						}
					}
					b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
					readChars = decodeSeq(detected, b, cbuffer, 0);
				}
				break;
			case x3C:  // '<'
				if ((b2 = is.read()) == -1) break;
				if (b2 == 0) {
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if (b3 == 0 && b4 == 0) { // UTF-32LE
						detected = UTF32; // Likely WAS JAX-RCP runtime doesn't tolerate UTF-32, and will crash afterwards
					} else { // UTF-16LE
						detected = UTF16;
					}
					b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
					readChars = decodeSeq(detected, b, cbuffer, 0);
				} else if (b2 == x3F) { // '?'
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if ((b5 = is.read()) == -1) break;
					if ((b3 == x78) && (b4 == x6D) && (b5 == x6C)) { // 'x'+'m'+'l'
						detected = UTF8;
						cbuffer[0]='<';cbuffer[1]='?';cbuffer[2]='x';cbuffer[3]='m';cbuffer[4]='l';
						readChars = 5;
						//-- search actual xml encoding declaration:
						//-- could be: UTF-8, ISO 646, ASCII, some part of ISO 8859, Shift-JIS, EUC,
						//-- or any other 7-bit, 8-bit, or mixed-width encoding which ensures that the
						//-- characters of ASCII have their normal positions, width, and values.
						RetValue r = getActualXMLEncDeclaration(UTF8, is, cbuffer, 5);
						readChars += r.readChars;
						if (r.encodingDecl != null) { // if found an xml encoding declaration
							detected = r.encodingDecl;
							//-- Likely WAS JAX-RCP runtime tolerate only uft-8 and iso-88-59-1,
							//-- with different encoding it will crash afterwards
						}
					} else { // very unlikely
						detected = UTF8;
						readChars = decodeMinimumUTF8Seq(new int[] {b1,b2,b3,b4,b5}, is, cbuffer, 0);
					}
				} else { // '<soapenv:envelope' for example
					detected = UTF8;
					readChars = decodeMinimumUTF8Seq(new int[] {b1,b2}, is, cbuffer, 0);
				}
				break;
			case x4C: // EBCDIC '<'  (ASCII 'L')
				//-- Likely WAS JAX-RCP runtime doesn't tolerate any EBCDIC encoding,
				//-- and so it will crash afterwards
				if ((b2 = is.read()) == -1) break;
				if (b2 == x6F) { // EBCDIC '?'
					if ((b3 = is.read()) == -1) break;
					if ((b4 = is.read()) == -1) break;
					if ((b5 = is.read()) == -1) break;
					if ((b3 == xA7) && (b4 == x94) && (b5 == x93)) { // EBCDIC 'x'+'m'+'l'
						detected = IBM1047; // as default let's choose Latin-1 code-page
						cbuffer[0]='<';cbuffer[1]='?';cbuffer[2]='x';cbuffer[3]='m';cbuffer[4]='l';
						readChars = 5;
						//-- search actual xml encoding declaration:
						//-- could be: IBM500, IBM1047, or any other EBCDIC code page
						RetValue r = getActualXMLEncDeclaration(IBM1047, is, cbuffer, 5);
						readChars += r.readChars;
						if (r.encodingDecl != null) { // if found an xml encoding declaration
							detected = r.encodingDecl;
						}
					} else { // very unlikely: EBCDIC '<?'+somechars or ASCII 'Lo'+somechars
						detected = IBM1047;
						b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4, (byte)b5};
						readChars = decodeSeq(detected, b, cbuffer, 0);
					}
				} else { // EBCDIC '<soapenv:envelope' for example
					detected = IBM1047;
					b = new byte[] {(byte)b1, (byte)b2};
					readChars = decodeSeq(detected, b, cbuffer, 0);
				}
				break;
//			case xEF: // UTF-8 BOM (EF-BB-BF) is managed by case 'default:' being a valid utf-8 sequence
//				break;
			default:
				detected = UTF8;
				// decode a single char to not ugly break utf-8 binary stream
				readChars = decodeMinimumUTF8Seq(new int[] {b1}, is, cbuffer, 0);
		} //-- switch
		log.debug("b1="+b1+",b2="+b2+"b3="+b3+",b4="+b4+",b5="+b5);	
		
		//-- set the detected character encoding for underneath request.
		setCharacterEncoding( detected );
		
		log.debug(P + "charEncDetection() ->exit");
		return readChars;
	}
	
	/** 
	 */
	private int decodeSeq(String charset, byte[] srcBytes, char[] dst, int dstBegin)
		throws IOException
	{
		if (log.isDebugEnabled()) {
			log.debug(P + "decodeSeq() <-enter");
			log.debug("srcBytes-hexdump:"+bytesToHex(srcBytes));
		}
		if (dst.length < srcBytes.length) {
			throw new IllegalArgumentException("dst.length must be at least equal to srcBytes.lenght");
		}
		int readChars = 0;
		
		String s = new String(srcBytes, charset);
		readChars = s.length();
		s.getChars(0, s.length(), dst, dstBegin);
		
		log.debug(P + "decodeSeq() ->exit");
		return readChars;
	}
	
	/**
	 */
	private int decodeMinimumUTF8Seq(int[] srcBytes, ServletInputStream srcStream, char[] dst, int dstBegin)
		throws IOException
	{
		if (log.isDebugEnabled()) {
			log.debug(P + "decodeMinimumUTF8Seq() <-enter");
			log.debug("srcBytes-hexdump:"+bytesToHex(srcBytes));
		}
		if (dst.length < srcBytes.length) {
			throw new IllegalArgumentException("dst.length must be at least equal to srcBytes.lenght");
		}
//		UTF-8 decoding rules:		
//		Binary    Hex          Comments
//		0xxxxxxx  0x00..0x7F   Only byte of a 1-byte character encoding
//		10xxxxxx  0x80..0xBF   Continuation characters (1-3 continuation bytes)
//		110xxxxx  0xC0..0xDF   First byte of a 2-byte character encoding
//		1110xxxx  0xE0..0xEF   First byte of a 3-byte character encoding
//		11110xxx  0xF0..0xF4   First byte of a 4-byte character encoding
		int readChars = 0;
		
		if (srcBytes.length > 0) {
			int ix = 0;
			while (ix < srcBytes.length) {
				int b1 = srcBytes[ix++];
				int b2 = -1, b3 = -1, b4 = -1;
				byte[] b;
				String s;
				// decode a single char to not ugly break utf-8 binary stream
				if (b1 < 128) {
					/* single byte seq */
					b = new byte[] {(byte)b1};
				} else if (b1 < 194) {
					/* error: continuation or overlong 2-byte sequence */
					/* byte b1 will be lost */
					break;
				} else if (b1 < 224) {
					/* two bytes seq */
					if (ix < srcBytes.length) {
						b2 = srcBytes[ix++];
					} else {
						if ((b2 = srcStream.read()) == -1) break; 
					}
					b = new byte[] {(byte)b1, (byte)b2};
				} else if (b1 < 240) {
					/* three bytes seq */
					if (ix < srcBytes.length) {
						b2 = srcBytes[ix++];
					} else {
						if ((b2 = srcStream.read()) == -1) break; 
					}
					if (ix < srcBytes.length) {
						b3 = srcBytes[ix++];
					} else {
						if ((b3 = srcStream.read()) == -1) break; 
					}
					b = new byte[] {(byte)b1, (byte)b2, (byte)b3};
				} else if (b1 < 245) {
					/* four bytes seq */
					if (ix < srcBytes.length) {
						b2 = srcBytes[ix++];
					} else {
						if ((b2 = srcStream.read()) == -1) break; 
					}
					if (ix < srcBytes.length) {
						b3 = srcBytes[ix++];
					} else {
						if ((b3 = srcStream.read()) == -1) break; 
					}
					if (ix < srcBytes.length) {
						b4 = srcBytes[ix++];
					} else {
						if ((b4 = srcStream.read()) == -1) break; 
					}
					b = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
				} else {
					/* error:  byte b1 will be lost */
					break;
				}
				if (log.isDebugEnabled()) {
					log.debug("utf8-char-byte-sequence-hexdump:"+bytesToHex(b));
				}
				s = new String(b,"UTF-8");
				int len;
				if ((len = s.length()) > 1) log.error(P+"readMinimumUTF8Seq(): "
					+ "utf8 binary sequence unexpectedly decoded into more than one char: " + s);
				s.getChars(0, len, dst, dstBegin);
				dstBegin += len;
				readChars += len;
			} //-- while
		}
		log.debug(P + "decodeMinimumUTF8Seq() ->exit");
		return readChars;
	}
	
	private final class RetValue {
		int readChars = 0;
		String encodingDecl = null;
	}
	
	/**
	 * Search actual xml encoding declaration.
	 * Here the assumption is that the binary input stream has encoding: _streamEnc_
	 * and that the first byte follows: '<?xml' char sequence.
	 * 
	 * @return The number of charactes read-ahead from InputStream (copied into dst buffer starting from dstBegin). 
	 */
	private final RetValue getActualXMLEncDeclaration(String streamEnc, ServletInputStream is, char[] dst, int dstBegin)
		throws IOException
	{
		log.debug(P + "getActualXMLEncDeclaration() <-enter");
		if (dst.length < 64) {
			throw new IllegalArgumentException("dst.length must be at least 64");
		}
		RetValue ret = new RetValue();
		byte[] b = new byte[59]; // 64 - 5
		int read = is.read(b, 0, 59);
		if (read > 0) {
			String s = new String(b, streamEnc);
			int i = s.indexOf("encoding=");
			if (i > -1) {
				char c = s.charAt(i+9);
				int j = s.indexOf(c, i+10);
				if (j > -1) {
					String enc = s.substring(i+10, j);
					log.debug(P+"xmldecl encoding=" + enc);
					if (enc.length() > 0) {
						ret.encodingDecl = enc;	
					}
				}
			}
			s.getChars(0, s.length(), dst, dstBegin);
			ret.readChars = s.length();
		}
		log.debug(P + "getActualXMLEncDeclaration() ->exit");
		return ret;
	}
	
	//-- debug utilities:
	
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String bytesToHex(int[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j];
			if (v > 255 || v < 0) throw new IllegalArgumentException("expected array of values in range 0-255");
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}