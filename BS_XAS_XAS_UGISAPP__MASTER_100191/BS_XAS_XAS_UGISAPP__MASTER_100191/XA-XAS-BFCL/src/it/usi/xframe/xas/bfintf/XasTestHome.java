package it.usi.xframe.xas.bfintf;
/**
 * Home interface for Enterprise Bean: Xastest
 */
public interface XasTestHome extends javax.ejb.EJBHome {
	/**
	 * Creates a default instance of Session Bean: Xastest
	 */
	public it.usi.xframe.xas.bfintf.XasTest create()
		throws javax.ejb.CreateException, java.rmi.RemoteException;
}
