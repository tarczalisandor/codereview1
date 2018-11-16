/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import java.rmi.RemoteException;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfintf.IXasEchoServiceFacade;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasEchoServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasEchoServiceFacade {

	public String echo(String msg) throws RemoteException {
		Echo echo = Echo.getInstance();
		return echo.echo(msg);
	}
}
