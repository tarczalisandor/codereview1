/*
 * Created on Apr 6, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.announcement;

import java.io.IOException;

import it.usi.xframe.system.bfutil.map.XfrEnvironmentPropertiesFactoryCacher;
import it.usi.xframe.system.bfutil.security.Base64Utils;

import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnnouncementProperties {
	private static Log logger = LogFactory.getLog(AnnouncementProperties.class);
	private static AnnouncementProperties istance = null;
	private final String XOR = "{xor}";
	private String serverName, userName, password;

	private AnnouncementProperties() {
		logger.info("Loading JNDI properties...");
		XfrEnvironmentPropertiesFactoryCacher prop =
			XfrEnvironmentPropertiesFactoryCacher.getInstance();
		try {
//			serverName = prop.getStringProperty("rep/sametime", "ServerName");
//			userName = prop.getStringProperty("rep/sametime", "UserName");
//			password = prop.getStringProperty("rep/sametime", "Password");
			serverName = prop.getStringProperty("rep/XFR-XAS-SAMETIME", "ServerName");
			userName = prop.getStringProperty("rep/XFR-XAS-SAMETIME", "UserName");
			password = prop.getStringProperty("rep/XFR-XAS-SAMETIME", "Password");
		} catch (NamingException e) {
			logger.error("JNDI Error: " + e.getMessage());
		}

		if (password.startsWith(XOR)) {
			logger.info("Password must be decoded.");
			Base64Utils crypt = Base64Utils.getInstance();
			try {
				password = new String(crypt.unmaskPwd(password));
				logger.info("Password decoded: " + password);
			} catch (IOException e) {
				logger.error(
					"Unable to decode (xor) Announcement server password");
				logger.error(e.getMessage());
			}
		}

		logger.info("JNDI ServerName: " + serverName);
		logger.info("JNDI UserName: " + userName);
		logger.info("JNDI Password: " + password);
	}

	// singletone
	public static AnnouncementProperties getInstance() {
		if (istance == null)
			istance = new AnnouncementProperties();
		return istance;
	}
	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return userName;
	}
}
