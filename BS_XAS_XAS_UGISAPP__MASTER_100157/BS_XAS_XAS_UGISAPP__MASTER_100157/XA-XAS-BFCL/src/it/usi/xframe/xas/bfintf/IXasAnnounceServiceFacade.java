/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import java.rmi.RemoteException;
import java.util.Date;

import it.usi.xframe.system.eservice.IServiceFacade;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IXasAnnounceServiceFacade extends IServiceFacade {

	public static String KEY_ID= "XasAnnounce";
	
	public int getUserStatus(String userid)  throws RemoteException;
	
	public int sendAnnouncement(
		String user,
		String sender,
		String message,
		Date expirationDate)  throws RemoteException;
}
