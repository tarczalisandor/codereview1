/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfintf.IXasEmailServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.AbstractEmailAttachment;
import it.usi.xframe.xas.bfutil.data.EmailMessage;

import java.io.OutputStream;
import java.rmi.RemoteException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasEmailServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasEmailServiceFacade {

	private static final String SENDEMAIL_ROLE = "SendEmail";
	private static final String EXPORTEMAIL_ROLE = "ExportEmail";
	public void sendEmailMessage(
		EmailMessage emailMsg,
		AbstractEmailAttachment[] attachemnts)
		throws RemoteException, XASException {

		boolean isUserInRole = true;
		String userid = getSupport().getPrincipal().getName();
		if ( !getSupport().isUserInRole(SENDEMAIL_ROLE) ) {
			isUserInRole = false;
		}

		SendEmail email = SendEmail.getInstance();
		email.sendMessage(emailMsg, attachemnts, userid, isUserInRole);
	}

	public byte[] exportEmailMessage(
			EmailMessage emailMsg,
			AbstractEmailAttachment[] attachemnts)
			throws RemoteException, XASException {
		boolean isUserInRole = true;
		String userid = getSupport().getPrincipal().getName();
		if ( !getSupport().isUserInRole(EXPORTEMAIL_ROLE) ) {
			isUserInRole = false;
		}
		SendEmail email = SendEmail.getInstance();
		return email.exportMessage(emailMsg, attachemnts, userid, isUserInRole);
		}

}
