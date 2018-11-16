/*
 * Created on Mar 19, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.wsutil;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasEmailServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment;
import it.usi.xframe.xas.bfutil.data.EmailMessage;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

//use this class to generate WSDL
public class SendEmail {
	public void sendEmailMessageWithBinary(
		EmailMessage emailMessage,
		BinaryEmailAttachment[] attachments)
		throws RemoteException, ServiceFactoryException, XASException {

		XasServiceFactory facade = XasServiceFactory.getInstance();
		IXasEmailServiceFacade email = facade.getXasEmailServiceFacade();
		try {
			email.sendEmailMessage(emailMessage, attachments);
		} finally {
			XasServiceFactory.getInstance().dispose(email);
		}
	}
	
	public byte[] exportEmailMessage(
			EmailMessage emailMsg,
			BinaryEmailAttachment[] attachments)
			throws RemoteException, ServiceFactoryException, XASException {

		XasServiceFactory facade = XasServiceFactory.getInstance();
		IXasEmailServiceFacade email = facade.getXasEmailServiceFacade();
		try {
			return email.exportEmailMessage(emailMsg, attachments);
		} finally {
			XasServiceFactory.getInstance().dispose(email);
		}
	}
	
}
