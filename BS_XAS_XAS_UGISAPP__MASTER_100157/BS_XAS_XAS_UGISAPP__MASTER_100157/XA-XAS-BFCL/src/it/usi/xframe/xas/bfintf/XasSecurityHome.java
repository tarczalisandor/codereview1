package it.usi.xframe.xas.bfintf;
/**
 * Home interface for Enterprise Bean: XasSecurity
 */
public interface XasSecurityHome extends javax.ejb.EJBHome {
	/**
	 * Creates a default instance of Session Bean: XasSecurity
	 */
	public it.usi.xframe.xas.bfintf.XasSecurity create()
		throws javax.ejb.CreateException, java.rmi.RemoteException;
}
