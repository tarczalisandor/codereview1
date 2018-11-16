/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import java.rmi.RemoteException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Echo {
	private static Echo instance = new Echo();

	protected Echo() {
	}

	public static Echo getInstance() {
		return instance;
	}

	public String echo(String msg) throws RemoteException {
		return msg;
	}
}
