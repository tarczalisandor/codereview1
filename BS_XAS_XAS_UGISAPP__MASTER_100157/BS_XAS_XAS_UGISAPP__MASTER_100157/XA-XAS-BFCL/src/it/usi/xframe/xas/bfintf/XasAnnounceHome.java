package it.usi.xframe.xas.bfintf;
/**
 * Home interface for Enterprise Bean: XasAnnounce
 */
public interface XasAnnounceHome extends javax.ejb.EJBHome {
	/**
	 * Creates a default instance of Session Bean: XasAnnounce
	 */
	public it.usi.xframe.xas.bfintf.XasAnnounce create()
		throws javax.ejb.CreateException, java.rmi.RemoteException;
}
