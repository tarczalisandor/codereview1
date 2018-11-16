package it.usi.xframe.xas.bfimpl;

/**
 * Bean implementation class for Enterprise Bean: XasMdbSms
 */
public class XasMdbSmsBean
	extends
	XasSendsmsServiceFacade
	implements
		javax.ejb.MessageDrivenBean,
		javax.jms.MessageListener {

	private javax.ejb.MessageDrivenContext fMessageDrivenCtx;

	/**
	 * getMessageDrivenContext
	 */
	public javax.ejb.MessageDrivenContext getMessageDrivenContext() {
		return fMessageDrivenCtx;
	}

	/**
	 * setMessageDrivenContext
	 */
	public void setMessageDrivenContext(javax.ejb.MessageDrivenContext ctx) {
		fMessageDrivenCtx = ctx;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() {
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
}
