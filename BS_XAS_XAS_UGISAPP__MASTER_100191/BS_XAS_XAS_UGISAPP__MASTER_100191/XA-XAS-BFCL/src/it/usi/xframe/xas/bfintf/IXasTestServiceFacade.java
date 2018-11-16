/*
 * Created on Jan 26, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import it.usi.xframe.system.eservice.IServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;

import java.rmi.RemoteException;

/**
 * @author ee00681
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IXasTestServiceFacade extends IServiceFacade {

	public static String KEY_ID= "XasTest";
	
	public void testlog() throws RemoteException, XASException;
	public void testnolog() throws RemoteException, XASException;
	public void testcommonlog() throws RemoteException, XASException;
	public String ping(String server) throws RemoteException, XASException;
	public String eventMonitor(int test) throws RemoteException, XASException;
}
