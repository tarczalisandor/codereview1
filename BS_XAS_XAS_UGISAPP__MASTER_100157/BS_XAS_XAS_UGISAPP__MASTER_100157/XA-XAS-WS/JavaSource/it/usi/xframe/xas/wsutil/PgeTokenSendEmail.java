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
public class PgeTokenSendEmail {
	public void sendEmailMessageWithBinary(
		EmailMessage emailMessage,
		BinaryEmailAttachment[] attachments)
		throws RemoteException, ServiceFactoryException, XASException {
		
		new SendEmail().sendEmailMessageWithBinary(emailMessage, attachments);

	}
	
	public byte[] exportEmailMessage(
			EmailMessage emailMsg,
			BinaryEmailAttachment[] attachments)
			throws RemoteException, ServiceFactoryException, XASException {
		
		return new SendEmail().exportEmailMessage(emailMsg, attachments);

	}
	
}
