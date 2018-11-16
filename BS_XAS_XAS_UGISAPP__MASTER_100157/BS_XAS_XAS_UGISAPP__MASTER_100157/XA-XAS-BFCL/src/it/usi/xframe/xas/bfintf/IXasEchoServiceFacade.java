/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import java.rmi.RemoteException;

import it.usi.xframe.system.eservice.IServiceFacade;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IXasEchoServiceFacade extends IServiceFacade {

	public static String KEY_ID = "XasEcho";

	public String echo(String msg) throws RemoteException;
}
