/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil;

import java.text.MessageFormat;

import it.usi.xframe.system.errors.XFRCodedException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XASException extends XFRCodedException{
	String code = null;
	/**
	 * 
	 */
	public XASException() {
		super();
	}

	/**
	 * @param message
	 */
	public XASException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XASException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public XASException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param parameters
	 */
	public XASException(String message, Object[] parameters) {
		super(message, parameters);
	}

	/**
	 * @param message
	 * @param stackTrace
	 */
	public XASException(String message, String stackTrace) {
		super(message, stackTrace);
	}

	/**
	 * @param message
	 * @param stackTrace
	 * @param code
	 * @param parameters
	 */
	public XASException(
		String message,
		String stackTrace,
		String code,
		Object[] parameters) {
		super(message, stackTrace, code, parameters);
		this.code = code;
	}

	/**
	 * @param message
	 * @param parameters
	 * @param cause
	 */
	public XASException(String message, Object[] parameters, Throwable cause) {
		super(message, parameters, cause);
	}

	public String toString() {
		String errorMessage = "";
		if (this.code != null) {
			errorMessage += code +" ";
		}
		if (this.getParameters() != null) {
			errorMessage += MessageFormat.format(this.getMessage(), this.getParameters());
		} else
			errorMessage += this.getMessage();
		return errorMessage;
	}
}
