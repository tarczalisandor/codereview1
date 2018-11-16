package it.usi.xframe.xas.bfimpl;

import java.text.MessageFormat;

import it.usi.xframe.xas.bfutil.XASException;

/**
 * Bean implementation class for Enterprise Bean: XasSendsms
 */
public class XasSendsmsBean
	extends it.usi.xframe.xas.bfimpl.XasSendsmsServiceFacade
	implements javax.ejb.SessionBean {
    private static final long serialVersionUID = 1L;
	private javax.ejb.SessionContext mySessionCtx;
	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}
	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
		initializeSupport(getSessionContext());
        initializationCleanup();
	}
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
}
