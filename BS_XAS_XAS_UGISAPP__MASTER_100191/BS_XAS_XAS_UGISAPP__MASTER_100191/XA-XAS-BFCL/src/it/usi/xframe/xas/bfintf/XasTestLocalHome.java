package it.usi.xframe.xas.bfintf;
/**
 * Local Home interface for Enterprise Bean: Xastest
 */
public interface XasTestLocalHome extends javax.ejb.EJBLocalHome {
	/**
	 * Creates a default instance of Session Bean: Xastest
	 */
	public it.usi.xframe.xas.bfintf.XasTestLocal create()
		throws javax.ejb.CreateException;
}
