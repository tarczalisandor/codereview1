package it.usi.xframe.xas.jaxrpc.auth;

import java.lang.reflect.Method;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * In this handler we use an obsolete SAAJ 1.0 API in order to
 * maintain back compatibility with J2EE 1.3 spec.
 * 
 * @author C305393
 */
public abstract class OutboundWSSEUsernameToken extends GenericHandler {
	
	private static Log log = LogFactory.getLog( OutboundWSSEUsernameToken.class );
	
	//private static final String FAULTS_NS_URI = "http://auth.jaxrpc.system.xframe.usi.it/fault";
	private static final String MISSING_UT = "MISSING_UT";
	private static final String SOAPEXCEPTION_CAUGHT = "SOAPEXCEPTION_CAUGHT";
	
	protected abstract QName getSecurityQName();
	protected abstract String getActor();
	protected abstract void addWSSEtoSOAPHeader(SOAPEnvelope env) throws SOAPException;
	
	private final QName[] headers = {getSecurityQName()};
	
	/* We record here the possible reason of init() method fault, if any */
	private String initFaultDetail = null;
	/* The fixed credentials to be inserted in wsse token  */
	protected UsernameToken ut = null;
	
	/**
	 * Returns the array of QNames of header blocks processed by this handler instance.
	 * QName is the qualified name of the outermost element of the Header block
	 */
	public QName[] getHeaders() {
		log.debug("getHeaders() ->");
		return headers;
	}

	public void init(HandlerInfo handlerInfo)
	{
		log.debug("init() <-");
		
		final String H = handlerInfo.getHandlerClass().getName() + "::init(): ";
		Map hc = handlerInfo.getHandlerConfig();
		String profile = (String)hc.get("UTProfile");
		String clazz = (String)hc.get("UTCacheFactory.class");
		try {
			if (clazz == null || profile == null) {
				initFaultDetail = H + "missing at least one of needed parameters for this jax-rpc handler: 'UTProfile' and 'UTCacheFactory.class'";
				throw new IllegalArgumentException(initFaultDetail);
			}
			Class c;
			try {
				c = Class.forName(clazz);
			} catch (ClassNotFoundException cnfe) {
				initFaultDetail = H + "the classname specified wit parameter 'UTCacheFactory.class' was not found: " + clazz;
				throw new IllegalArgumentException(initFaultDetail);
			}
			if (!UTCacheFactory.class.isAssignableFrom(c)) {
				initFaultDetail = H + "the classname specified wit parameter 'UTCacheFactory.class' MUST extend " + UTCacheFactory.class;
				throw new IllegalArgumentException(initFaultDetail);
			}
			Object utcf = c.newInstance();
			Method getUTCache = c.getMethod("getUTCache", null);
			IUsernameTokenCache utc = (IUsernameTokenCache)getUTCache.invoke(utcf, null);
			ut = utc.getUsernameToken(profile);
			if (ut == null) {
				initFaultDetail = H + "No usernameToken for profile: '"
					+ profile + "' from UTCacheFactory: " + clazz; 
				throw new IllegalArgumentException(initFaultDetail);
			}
			log.info(H + "succesfully got usernameToken for profile: '"
				+ profile + "' from UTCacheFactory: " + clazz);
		
		// Better exiting init() method without Exception (if exceptions are not recoverable in future invocations)
		// and just record the failure reason and check it handle* methods. 
		} catch (IllegalArgumentException iae) {
			// Do nothing, initFaultDetail is just set
		} catch (Exception ex) {
			initFaultDetail = H + "caught exception: " + ex.getClass().getName()
				+ " with msg: " + ex.getMessage() + " - Caused by: " + ex.getCause();
		}
		if (initFaultDetail != null) {
			log.error(initFaultDetail);
		}
		log.debug("init() ->");
	}

	public boolean handleRequest(MessageContext msgContext) {
		log.debug("handleRequest() <-");
		SOAPMessageContext smc = (SOAPMessageContext) msgContext;
		
		if (ut == null) {
			StringBuffer e = new StringBuffer("Failed to fetch UsernameToken credentials. "); 
			e.append("This is likely due to a misconfiguration of Handler parameters.");
			if (initFaultDetail != null) {
				e.append(" Tip: init() method failed with: " + initFaultDetail);
			}
			//log.error(e.toString());
			smc.setMessage(createHandlerSoapFault(MISSING_UT, e.toString()));
			return false;
		}
		//log.debug("ut:" + ut.getUsername() + "@" + ut.getPassword());
		
		SOAPPart sp = smc.getMessage().getSOAPPart();
		try {

			addWSSEtoSOAPHeader(sp.getEnvelope());
			
		} catch (SOAPException ex) {
			StringBuffer e = new StringBuffer(ex.getMessage());
			Throwable t = ex.getCause();
			if (t != null) {
				e.append(" - Caused by: ");
				e.append(t.getMessage());
			}
			//log.error(e.toString());
			smc.setMessage(createHandlerSoapFault(SOAPEXCEPTION_CAUGHT, e.toString()));
			return false;
		}
		log.debug("handleRequest() ->");
		return true;
	}
	
	/**
	 * Create a Soap Fault Message that will be propagated back to jax-rpc invoker chain.
	 */
	private SOAPMessage createHandlerSoapFault(String faultCode, String faultString) {
		try {
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage sm = mf.createMessage();
			SOAPEnvelope env = sm.getSOAPPart().getEnvelope();
			SOAPFault fault = env.getBody().addFault();
			//-- Sorry, in the end we must keep default faultCode set in addFault().
			//-- It seems that ibm jarpc engine doesn't accept unqualified faultcodes
			//-- and... we must stay with SAAJ 1.0 so only String typed faultCode.
			//-- and... 'prefix:localname' is the assumed sintax for String faultCode,
			//-- where prefix must be one of the envelope-scoped ns-prefixes.
			//fault.setFaultCode(faultCode);
			fault.setFaultString(faultString);
			fault.setFaultActor(this.getActor());
			return sm;
		} catch (SOAPException se) {
			//se.printStackTrace();
			throw new RuntimeException(se.getMessage());
		}
		
	}	
}
