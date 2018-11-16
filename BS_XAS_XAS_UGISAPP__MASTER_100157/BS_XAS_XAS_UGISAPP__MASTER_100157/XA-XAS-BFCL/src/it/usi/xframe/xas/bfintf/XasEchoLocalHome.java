package it.usi.xframe.xas.bfintf;
/**
 * Local Home interface for Enterprise Bean: XasEcho
 */
public interface XasEchoLocalHome extends javax.ejb.EJBLocalHome {
	/**
	 * Creates a default instance of Session Bean: XasEcho
	 */
	public it.usi.xframe.xas.bfintf.XasEchoLocal create()
		throws javax.ejb.CreateException;
}
