/*
 * Created on Nov 07, 2016
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.security;

/**
 * @author C305373
 * @version 1.0
 *
 */
public class LoggedUser {

	private String userName = null;
	private String userPassword = null;
	private boolean authenticated = false;
	 
	public LoggedUser (String UserId, String UserPassword)
	{
		userName = UserId;
	}
		 
	/**
	 * @return
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param b
	 */
	public void setAuthenticated(boolean b) {
		authenticated = b;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string;
	}
}
