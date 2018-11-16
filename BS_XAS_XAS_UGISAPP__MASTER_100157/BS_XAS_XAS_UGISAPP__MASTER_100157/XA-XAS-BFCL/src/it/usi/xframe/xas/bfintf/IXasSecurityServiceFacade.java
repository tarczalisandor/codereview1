/*
 * Created on Feb 17, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import it.usi.xframe.system.eservice.IServiceFacade;

import java.rmi.RemoteException;

/**
 * @author EE01995
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IXasSecurityServiceFacade extends IServiceFacade {

	public static String KEY_ID = "XasSecurity";

	/**
	 * Retrieve the logged userid.
	 * 
	 * @return the logged userid.
	 * @throws RemoteException if there is an error in the remote method call.
	 */
	public abstract String getLoggedUser() throws RemoteException;
}
