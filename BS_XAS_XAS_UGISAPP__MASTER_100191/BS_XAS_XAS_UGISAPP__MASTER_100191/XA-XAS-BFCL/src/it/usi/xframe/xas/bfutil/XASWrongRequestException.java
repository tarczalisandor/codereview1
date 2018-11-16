/*
 * Created on Jun 26, 2015
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil;


/**
 * @author us01391
 *
 * This Exception must be used when a SmsRequest has something wrong, so that it
 * must be discarded and cannot be processed again
 */
public class XASWrongRequestException extends XASException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public XASWrongRequestException(String message) {
		super(message);
	}
	
	public XASWrongRequestException(Throwable t) {
		super(t);
	}
	
	public XASWrongRequestException(String message, Throwable t) {
		super(message,t);
	}
	public XASWrongRequestException(String message, Object[] parameters, Throwable cause) {
		super(message, parameters, cause);
	}
	public XASWrongRequestException(
			String message,
			String stackTrace,
			String code,
			Object[] parameters) {
			super(message, stackTrace, code, parameters);
		}
}
