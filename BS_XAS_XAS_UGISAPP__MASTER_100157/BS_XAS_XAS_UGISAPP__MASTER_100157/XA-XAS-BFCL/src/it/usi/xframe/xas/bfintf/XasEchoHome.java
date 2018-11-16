package it.usi.xframe.xas.bfintf;
/**
 * Home interface for Enterprise Bean: XasEcho
 */
public interface XasEchoHome extends javax.ejb.EJBHome {
	/**
	 * Creates a default instance of Session Bean: XasEcho
	 */
	public it.usi.xframe.xas.bfintf.XasEcho create()
		throws javax.ejb.CreateException, java.rmi.RemoteException;
}
