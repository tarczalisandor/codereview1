/*
 * Created on Feb 17, 2010
 */
package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSecurityServiceFacade;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author EE01995
 */
public class SecurityWsdlBean implements IXasSecurityServiceFacade {

	private static Log log = LogFactory.getLog(SecurityWsdlBean.class);

	/**
	 * @see it.usi.xframe.xas.bfintf.IXasSecurityServiceFacade#getLoggedUser()
	 */
	public String getLoggedUser() {
		IXasSecurityServiceFacade facade = null;
		String user = null;
		try {
			facade =
				XasServiceFactory.getInstance().getXasSecurityServiceFacade();
			user = facade.getLoggedUser();
			log.info("### getLoggedUser found: [" + user + "]");
		} catch (ServiceFactoryException ex) {
			log.error(
				"ServiceFactoryException in getLoggedUser method: "
					+ ex.getMessage());
		} catch (RemoteException ex) {
			log.error(
				"RemoteException in getLoggedUser method: " + ex.getMessage());
		} finally {
			if (facade != null)
				XasServiceFactory.getInstance().dispose(facade);
		}
		return user;
	}

}
