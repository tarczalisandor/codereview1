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
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SecurityUtility;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;

import java.rmi.RemoteException;

/**
 * @author us01170
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasEmailServiceFacade extends AbstractSupportServiceFacade implements IXasEmailServiceFacade
{

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasEmailServiceFacade#sendEmailMessage(it.usi.xframe.xas.bfutil.data.EmailMessage, it.usi.xframe.xas.bfutil.data.AbstractEmailAttachment[])
	 */
	public void sendEmailMessage(EmailMessage emailMsg, AbstractEmailAttachment[] attachemnts)
			throws RemoteException, XASException
	{
		// Security Validation
		String userid = getSupport().getPrincipal().getName();
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.EMAIL).add(SingleSecurityProfile.ADMIN));

		SendEmail email = SendEmail.getInstance();
		email.sendMessage(emailMsg, attachemnts, userid, isUserInRole);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasEmailServiceFacade#exportEmailMessage(it.usi.xframe.xas.bfutil.data.EmailMessage, it.usi.xframe.xas.bfutil.data.AbstractEmailAttachment[])
	 */
	public byte[] exportEmailMessage(EmailMessage emailMsg, AbstractEmailAttachment[] attachemnts)
			throws RemoteException, XASException
	{
		// Security Validation
		String userid = getSupport().getPrincipal().getName();
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.EMAIL).add(SingleSecurityProfile.ADMIN));

		SendEmail email = SendEmail.getInstance();
		return email.exportMessage(emailMsg, attachemnts, userid, isUserInRole);
	}

}
