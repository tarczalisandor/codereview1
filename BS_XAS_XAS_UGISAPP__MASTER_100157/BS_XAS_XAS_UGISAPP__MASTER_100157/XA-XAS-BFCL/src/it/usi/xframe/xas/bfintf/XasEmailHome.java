package it.usi.xframe.xas.bfintf;
/**
 * Home interface for Enterprise Bean: XasEmail
 */
public interface XasEmailHome extends javax.ejb.EJBHome {
	/**
	 * Creates a default instance of Session Bean: XasEmail
	 */
	public it.usi.xframe.xas.bfintf.XasEmail create()
		throws javax.ejb.CreateException, java.rmi.RemoteException;
}
