/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import java.rmi.RemoteException;
import java.util.Date;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfimpl.announcement.AnnouncementService;
import it.usi.xframe.xas.bfintf.IXasAnnounceServiceFacade;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasAnnounceServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasAnnounceServiceFacade {

	public int getUserStatus(String userid)  throws RemoteException{
		AnnouncementService as = new AnnouncementService();
		return as.getUserStatus(userid);
	}

	public int sendAnnouncement  (
		String user,
		String sender,
		String message,
		Date expirationDate) throws RemoteException{
		AnnouncementService as = new AnnouncementService();
		return as.sendAnnouncement(user, sender, message, expirationDate);
	}
}
