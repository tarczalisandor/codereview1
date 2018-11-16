package it.usi.xframe.xas.bfimpl.a2psms.providers;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.usi.xframe.system.jaxrpc.auth.OutboundWSSE2004UsernameToken;
/**
 * NOT USED.
 * @author US00760
 *
 */
public class XasHandler extends OutboundWSSE2004UsernameToken {
	private static Logger logger = LoggerFactory.getLogger(XasHandler.class);
	private static final String WSU_PREFIX = "wsu";
	private static final String WSU_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	private static final String WSSE_PREFIX = "wsse";
	private static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String WSSE_SECUTIRY_TAG = "Security";
	private static final String WSSE_USERNAMETOKEN_TAG = "UsernameToken";
	private static final String TIM_ID_TAG = "Id";
	private static final String TIM_ID_VALUE = "UsernameToken-F981486C4B5E2BAD3515005421702991";

	/* (non-Javadoc)
	 * @see it.usi.xframe.system.jaxrpc.auth.OutboundWSSE2004UsernameToken#addWSSEtoSOAPHeader(javax.xml.soap.SOAPEnvelope)
	 */
	protected void addWSSEtoSOAPHeader(SOAPEnvelope soapEnvelope) throws SOAPException {
		 
	    super.addWSSEtoSOAPHeader(soapEnvelope);	// From OutboundWSSE2004UsernameToken
		/**
		 * Using org.w3c.dom.Document instead of avax.xml.soap.SOAP* due to SAAJ 1.0	    
		 */
	    Node envelopeNode = (Node) soapEnvelope;
	    Document document = envelopeNode.getOwnerDocument();
	    NodeList nl = document.getElementsByTagNameNS(WSSE_NAMESPACE, WSSE_SECUTIRY_TAG);
	    for (int i = 0; i < nl.getLength(); i++) {
	    	Node sh = (Node) nl.item(i);
	    	if ((WSSE_PREFIX + ":" + WSSE_SECUTIRY_TAG).equals(sh.getNodeName())) {
		    	logger.debug("Found: " + sh.getNodeName() + " adding " + WSU_PREFIX + " namespace");
//		    	sh.addNamespaceDeclaration(WSU_PREFIX, WSU_NAMESPACE);
		    	Attr newNameSpace = document.createAttribute("xmlns:" + WSU_PREFIX);
		    	newNameSpace.setValue(WSU_NAMESPACE);
				sh.getAttributes().setNamedItem(newNameSpace);
		    	Node node = sh.getFirstChild();
		    	if ((WSSE_PREFIX + ":" + WSSE_USERNAMETOKEN_TAG).equals(node.getNodeName())) {
			    	logger.debug("Found: " + node.getNodeName() + " adding " + WSU_PREFIX + ":" + TIM_ID_TAG);
			    	// Create wsu:id for TIM <wsse:UsernameToken wsu:Id="UsernameToken-F981486C4B5E2BAD3515005421702991">
			    	Attr newAttrId = document.createAttributeNS(WSU_NAMESPACE, WSU_PREFIX + ":" + TIM_ID_TAG);
			    	newAttrId.setValue(TIM_ID_VALUE);
					node.getAttributes().setNamedItemNS(newAttrId);
		    	}
	    	}
	    }
    }
  
}
