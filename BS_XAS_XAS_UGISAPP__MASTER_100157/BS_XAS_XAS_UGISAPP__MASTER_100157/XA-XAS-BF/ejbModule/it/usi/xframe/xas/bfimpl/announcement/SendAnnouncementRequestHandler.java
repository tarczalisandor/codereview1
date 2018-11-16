/*
 * Created on Mar 31, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.announcement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lotus.sametime.community.Channel;
import com.lotus.sametime.community.ChannelEvent;
import com.lotus.sametime.community.ChannelListener;
import com.lotus.sametime.community.ChannelService;
import com.lotus.sametime.community.CommunityService;
import com.lotus.sametime.community.LoginEvent;
import com.lotus.sametime.community.LoginListener;
import com.lotus.sametime.core.comparch.STSession;
import com.lotus.sametime.core.constants.EncLevel;
import com.lotus.sametime.core.types.STObject;
import com.lotus.sametime.core.types.STUser;
import com.lotus.sametime.core.util.NdrOutputStream;
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
public class SendAnnouncementRequestHandler
	implements LoginListener, ResolveListener, ChannelListener {
	private static AnnouncementProperties properties = AnnouncementProperties.getInstance();
	private static final int MAX_WAIT_TIME = 10000;
	private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	private static final int SERVICE_TYPE = 0x80000055;
	private static Log logger =
		LogFactory.getLog(SendAnnouncementRequestHandler.class);
	private SimpleDateFormat dateFormat =
		new SimpleDateFormat(DD_MM_YYYY_HH_MM);
	private boolean loggedIn = false;
	private STUser stUser = null;
	private boolean channelOpened = false;

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
		STSession m_session = null;
		CommunityService commService = null;
		Resolver resolver = null;
		Channel channel = null;
		try {
			m_session = new STSession("" + this);
			m_session.loadComponents(
				new String[] {
					CommunityService.COMP_NAME,
					ChannelService.COMP_NAME,
					LookupService.COMP_NAME });
			m_session.start();

			// Do Sametime login
			commService =
				(CommunityService) m_session.getCompApi(
					CommunityService.COMP_NAME);
			commService.addLoginListener(this);
			commService.loginByPassword(
				properties.getServerName(),
				properties.getUsername(),
				properties.getPassword());

			// Wait until initialization complete
			synchronized (this) {
				wait(MAX_WAIT_TIME);
			}

			if (loggedIn) {
				LookupService lookupService =
					(LookupService) m_session.getCompApi(
						LookupService.COMP_NAME);
				resolver = lookupService.createResolver(true, true, true, true);
				resolver.addResolveListener(this);
				resolver.resolve(user);

				synchronized (this) {
					wait(MAX_WAIT_TIME);
				}

				if (stUser != null) {
					NdrOutputStream outStream = new NdrOutputStream();
					outStream.writeUTF(message);
					outStream.writeUTF(dateFormat.format(expirationDate));
					outStream.writeUTF(sender);
					outStream.writeInt(1);
					stUser.getId().dump(outStream);

					// Get a reference to the needed components.
					ChannelService m_channelService =
						(ChannelService) m_session.getCompApi(
							ChannelService.COMP_NAME);
					channel =
						m_channelService.createChannel(
							SERVICE_TYPE,
							0,
							0,
							EncLevel.ENC_LEVEL_ALL,
							outStream.toByteArray(),
							null);
					channel.addChannelListener(this);
					channel.open();

					synchronized (this) {
						wait(MAX_WAIT_TIME);
					}

					if (channelOpened) {
						return AnnouncementConstants.ANNOUNCEMENT_REQUEST_SENT;
					} else
						return AnnouncementConstants
							.ANNOUNCEMENT_REQUEST_FAILED_SERVICE_UNAVAILABLE;

				}
			}

		} catch (Throwable e) {
			logger.error(
				"Exception in method sendAnnouncement : " + e.getMessage(),
				e);
			return AnnouncementConstants
				.ANNOUNCEMENT_REQUEST_FAILED_SERVICE_UNKNOWN_ERROR;
		} finally {
			if (channel != null) {
				channel.removeChannelListener(this);
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
		return AnnouncementConstants
			.ANNOUNCEMENT_REQUEST_FAILED_SERVICE_UNKNOWN_ERROR;
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

	public void channelClosed(ChannelEvent event) {
		channelOpened = true;
		synchronized (this) {
			notify();
		}
	}

	public void channelMsgReceived(ChannelEvent event) {
	}

	public void channelOpenFailed(ChannelEvent event) {
		channelOpened = false;
		synchronized (this) {
			notify();
		}
	}

	public void channelOpened(ChannelEvent event) {
		channelOpened = true;

		synchronized (this) {
			notify();
		}
	}
}
