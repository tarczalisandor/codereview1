package it.usi.xframe.xas.jaxrpc.auth;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeaderElement;

public class OutboundWSSE2003UsernameToken extends OutboundWSSEUsernameToken {

	private static final String WSSE_NS_URI = "http://schemas.xmlsoap.org/ws/2003/06/secext";
	private static final String WSSE_NS_PREFIX = "wsse";
	private static final String WSSE_PWDTEXT_URI = WSSE_NS_PREFIX + ":PasswordText";

	private static final QName SECURITY_QNAME =
		new QName(WSSE_NS_URI, "Security");
	
	private static final QName TYPE_QNAME =
		new QName(WSSE_NS_URI, "Type");
	
	protected QName getSecurityQName() {
		return SECURITY_QNAME;
	}

	protected String getActor() {
		return this.getClass().getName();
	}

	protected void addWSSEtoSOAPHeader(SOAPEnvelope env) throws SOAPException
	{
		// here we create javax.xml.soap.Name objects to stay back compatible with SAAJ 1.0 API
		Name securityName =	env.createName(
				SECURITY_QNAME.getLocalPart(),
				WSSE_NS_PREFIX,
				SECURITY_QNAME.getNamespaceURI());
		Name typeName = env.createName(
				TYPE_QNAME.getLocalPart());
		
		SOAPHeaderElement security = env.getHeader().addHeaderElement(securityName);
		SOAPElement utElem = security.addChildElement("UsernameToken", WSSE_NS_PREFIX);
		utElem.addChildElement("Username", WSSE_NS_PREFIX).addTextNode(ut.getUsername());
		utElem.addChildElement("Password", WSSE_NS_PREFIX).addAttribute(typeName, WSSE_PWDTEXT_URI).addTextNode(ut.getPassword());
	}

}
