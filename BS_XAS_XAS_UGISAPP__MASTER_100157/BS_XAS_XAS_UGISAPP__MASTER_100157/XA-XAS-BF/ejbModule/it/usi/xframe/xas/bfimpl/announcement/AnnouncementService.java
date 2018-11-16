/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.announcement;

import java.util.Date;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnnouncementService {
	/**
	 * @param userid
	 * @return
	 */
	public int getUserStatus(String userid) {
		return new UserStatusRequestHandler().getUserStatus(userid);
	}

	/**
	 * @param user
	 * @param sender
	 * @param message
	 * @param expirationDate
	 * @return
	 */
	public int sendAnnouncement(
		String user,
		String sender,
		String message,
		Date expirationDate) {
		return new SendAnnouncementRequestHandler().sendAnnouncement(
			user,
			sender,
			message,
			expirationDate);
	}
}
