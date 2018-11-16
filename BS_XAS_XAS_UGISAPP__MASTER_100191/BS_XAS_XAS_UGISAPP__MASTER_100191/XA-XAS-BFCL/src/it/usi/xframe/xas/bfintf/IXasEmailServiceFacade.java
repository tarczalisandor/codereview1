/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfintf;

import it.usi.xframe.system.eservice.IServiceFacade;
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
public interface IXasEmailServiceFacade extends IServiceFacade {

	public static String KEY_ID= "XasEmail";
	
	/**
	 * 
	 * @param emailMsg
	 * @param attachemnts
	 * @throws RemoteException
	 * @throws JGHException
	 */
	public void sendEmailMessage(
		EmailMessage emailMsg,
		AbstractEmailAttachment[] attachemnts)
		throws RemoteException, XASException;
	
	public byte[] exportEmailMessage(
			EmailMessage emailMsg,
			AbstractEmailAttachment[] attachemnts)
			throws RemoteException, XASException;
	
}
