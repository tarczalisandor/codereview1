/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.announcement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lotus.sametime.awareness.AwarenessService;
import com.lotus.sametime.awareness.STWatchedUser;
import com.lotus.sametime.awareness.StatusEvent;
import com.lotus.sametime.awareness.StatusListener;
import com.lotus.sametime.awareness.WatchList;
import com.lotus.sametime.community.CommunityService;
import com.lotus.sametime.community.LoginEvent;
import com.lotus.sametime.community.LoginListener;
import com.lotus.sametime.core.comparch.STSession;
import com.lotus.sametime.core.types.STId;
import com.lotus.sametime.core.types.STObject;
import com.lotus.sametime.core.types.STUser;
import com.lotus.sametime.lookup.LookupService;
import com.lotus.sametime.lookup.ResolveEvent;
import com.lotus.sametime.lookup.ResolveListener;
import com.lotus.sametime.lookup.Resolver;
/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UserStatusRequestHandler
	implements LoginListener, ResolveListener, StatusListener {
	private static final int MAX_WAIT_TIME = 10000;
	private static Log logger =
		LogFactory.getLog(SendAnnouncementRequestHandler.class);
	private boolean loggedIn = false;
	private STUser stUser = null;
	private int userStatus = AnnouncementConstants.ST_USER_STATUS_UNKNOWN;

	/**
	 * The method return the user status in sametime.
	 * 
	 * @param userid
	 *            .
	 * @return
	 */
	public int getUserStatus(String userid) {
		STSession m_session = null;
		CommunityService commService = null;
		Resolver resolver = null;
		WatchList list = null;
		try {

			// Session creation.
			m_session = new STSession("" + this);
			m_session.loadComponents(
				new String[] {
					CommunityService.COMP_NAME,
					AwarenessService.COMP_NAME,
					LookupService.COMP_NAME });
			m_session.start();

			// Login to Sametime
			commService =
				(CommunityService) m_session.getCompApi(
					CommunityService.COMP_NAME);
			commService.addLoginListener(this);
			commService.loginByPassword("prova-25a58b0dd", "admin", "passw0rd");

			synchronized (this) {
				wait(MAX_WAIT_TIME);
			}

			if (loggedIn) {
				// Resolving userid in Sametime directory
				LookupService lookupService =
					(LookupService) m_session.getCompApi(
						LookupService.COMP_NAME);
				resolver = lookupService.createResolver(true, true, true, true);
				resolver.addResolveListener(this);
				resolver.resolve(userid);

				synchronized (this) {
					wait(MAX_WAIT_TIME);
				}

				if (stUser != null) {
					// lookup current user status
					AwarenessService awarenessService =
						(AwarenessService) m_session.getCompApi(
							AwarenessService.COMP_NAME);

					list = awarenessService.createWatchList();
					list.addStatusListener(this);
					list.addItem(stUser);

					synchronized (this) {
						wait(MAX_WAIT_TIME);
					}

					return userStatus;

				}
			} else
				logger.error("Client not logged in!");
		} catch (Throwable e) {
			logger.error(
				"Exception in method getUserStatus : " + e.getMessage(),
				e);
		} finally {

			if (list != null) {
				list.removeStatusListener(this);
				list.close();
			}

			if (resolver != null) {
				resolver.removeResolveListener(this);
			}

			if (commService != null) {
				commService.removeLoginListener(this);
				if (commService.isLoggedIn())
					commService.logout();
			}

			if (m_session != null && m_session.isActive()) {
				m_session.stop();
				m_session.unloadSession();
			}

		}

		return AnnouncementConstants.ST_USER_STATUS_UNKNOWN;
	}

	public void loggedIn(LoginEvent event) {
		logger.debug("User LoggedIn to sametime");
		loggedIn = true;
		synchronized (this) {
			notify();
		}
	}

	public void loggedOut(LoginEvent event) {
		logger.debug("User LoggeOut to sametime");
		loggedIn = false;
		synchronized (this) {
			notify();
		}
	}

	public void resolveConflict(ResolveEvent event) {
		stUser = null;
		synchronized (this) {
			notify();
		}
	}

	public void resolveFailed(ResolveEvent event) {
		stUser = null;
		synchronized (this) {
			notify();
		}
	}

	public void resolved(ResolveEvent event) {
		STObject object = event.getResolved();
		stUser = new STUser(object.getId(), "", "");
		synchronized (this) {
			notify();
		}
	}

	public void groupCleared(StatusEvent event) {
	}

	public void userStatusChanged(StatusEvent statusEvent) {
		STWatchedUser[] users = statusEvent.getWatchedUsers();
		for (int i = 0; i < users.length; i++) {
			STWatchedUser user = users[i];
			STId stId = user.getId();
			if (stId.getId().equalsIgnoreCase(stUser.getId().getId()))
				userStatus = user.getStatus().getStatusType();
		}

		synchronized (this) {
			notify();
		}
	}
}
