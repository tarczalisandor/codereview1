package it.usi.xframe.xas.bfimpl.sms.configuration;

import org.xml.sax.SAXException;

/*
 * Represents an error reading xml configuration file
 */
public class XASConfigurationException extends SAXException {

	/**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

	public XASConfigurationException(String message) {
		super(message);
	}

	public XASConfigurationException(String message, Exception e) {
		super(message, e);
	}

}
