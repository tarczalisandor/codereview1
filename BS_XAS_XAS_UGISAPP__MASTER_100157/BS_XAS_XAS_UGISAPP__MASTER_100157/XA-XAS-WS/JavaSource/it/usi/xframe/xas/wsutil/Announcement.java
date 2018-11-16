/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.wsutil;

import java.rmi.RemoteException;
import java.util.Date;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasAnnounceServiceFacade;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Announcement {
	public int getUserStatus(String userid)
		throws ServiceFactoryException, RemoteException {
		IXasAnnounceServiceFacade facade =
			XasServiceFactory.getInstance().getXasAnnounceServiceFacade();
		try {
			return facade.getUserStatus(userid);
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
	}

	public int sendAnnouncement(
		String user,
		String sender,
		String message,
		Date expirationDate)
		throws ServiceFactoryException, RemoteException {

		IXasAnnounceServiceFacade facade =
			XasServiceFactory.getInstance().getXasAnnounceServiceFacade();
		try {
			return facade.sendAnnouncement(
				user,
				sender,
				message,
				expirationDate);
		} finally {
			XasServiceFactory.getInstance().dispose(facade);
		}
	}
}
