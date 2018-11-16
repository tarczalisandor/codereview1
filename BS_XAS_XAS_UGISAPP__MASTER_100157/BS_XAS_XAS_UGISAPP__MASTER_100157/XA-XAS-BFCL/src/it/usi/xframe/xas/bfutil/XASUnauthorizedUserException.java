package it.usi.xframe.xas.bfutil;

import it.usi.xframe.xas.bfutil.XASWrongRequestException;

public class XASUnauthorizedUserException extends XASWrongRequestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;

	public XASUnauthorizedUserException(String userId) {
		super("User " + userId + " has not the role to send SMS");
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}
