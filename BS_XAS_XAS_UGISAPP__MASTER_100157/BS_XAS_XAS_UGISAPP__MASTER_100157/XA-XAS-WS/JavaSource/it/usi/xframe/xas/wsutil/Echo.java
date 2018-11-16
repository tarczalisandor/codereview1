package it.usi.xframe.xas.wsutil;
import java.rmi.RemoteException;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasEchoServiceFacade;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
//use this class to generate WSDL
public class Echo {
	public String echoit(String msg)
		throws ServiceFactoryException, RemoteException {
		XasServiceFactory facade = XasServiceFactory.getInstance();
		IXasEchoServiceFacade echo = facade.getXasEchoServiceFacade();
		try {
			return echo.echo(msg);
		} finally {
			XasServiceFactory.getInstance().dispose(echo);
		}
	}
}
