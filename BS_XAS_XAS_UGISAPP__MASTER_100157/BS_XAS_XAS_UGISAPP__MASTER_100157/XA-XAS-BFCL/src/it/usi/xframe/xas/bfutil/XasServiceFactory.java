package it.usi.xframe.xas.bfutil;

import it.usi.xframe.xas.bfintf.IXasEchoServiceFacade;
import it.usi.xframe.xas.bfintf.IXasEmailServiceFacade;
import it.usi.xframe.system.eservice.AbstractServiceFactory;
import it.usi.xframe.system.eservice.ServiceFactoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import it.usi.xframe.xas.bfintf.IXasAnnounceServiceFacade;
import it.usi.xframe.xas.bfintf.IXasSecurityServiceFacade;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfintf.IXasTestServiceFacade;
/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasServiceFactory extends AbstractServiceFactory {

	private static XasServiceFactory me;
	private static Log logger = LogFactory.getLog(XasServiceFactory.class);

	/**
	 * 
	 */
	private XasServiceFactory() {
		super();
	}

	public static XasServiceFactory getInstance() {
		if (me == null) {
			try {
				me = new XasServiceFactory();
			} catch (Exception e) {
				logger.error(
					"Failed to load XasServiceFactory: " + e.getMessage(),
					e);
			}
		}
		return me;
	}

	/**
	 * 
	 * @return <code>IXasEmailServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasEmailServiceFacade getXasEmailServiceFacade()
		throws ServiceFactoryException {
		return (IXasEmailServiceFacade) this.getXfrServiceFacade(
			IXasEmailServiceFacade.KEY_ID);
	}

	/**
	 * 
	 * @return <code>IXasEchoServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasEchoServiceFacade getXasEchoServiceFacade()
		throws ServiceFactoryException {
		return (IXasEchoServiceFacade) this.getXfrServiceFacade(
			IXasEchoServiceFacade.KEY_ID);
	}

	/**
	 * 
	 * @return <code>IXasAnnounceServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasAnnounceServiceFacade getXasAnnounceServiceFacade()
		throws ServiceFactoryException {
		return (IXasAnnounceServiceFacade) this.getXfrServiceFacade(
			IXasAnnounceServiceFacade.KEY_ID);
	}
	/**
	 * 
	 * @return <code>IXasSecurityServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasSecurityServiceFacade getXasSecurityServiceFacade()
		throws ServiceFactoryException {
		return (IXasSecurityServiceFacade) this.getXfrServiceFacade(
			IXasSecurityServiceFacade.KEY_ID);
	}
	/**
	 * 
	 * @return <code>IXasSendsmsServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasSendsmsServiceFacade getXasSendsmsServiceFacade()
		throws ServiceFactoryException {
		return (IXasSendsmsServiceFacade) this.getXfrServiceFacade(
			IXasSendsmsServiceFacade.KEY_ID);
	}
	/**
	 * 
	 * @return <code>IXasTestServiceFacade</code>
	 * @throws ServiceFactoryException
	 */
	public IXasTestServiceFacade getXasTestServiceFacade()
		throws ServiceFactoryException {
		return (IXasTestServiceFacade) this.getXfrServiceFacade(
			IXasTestServiceFacade.KEY_ID);
	}
}