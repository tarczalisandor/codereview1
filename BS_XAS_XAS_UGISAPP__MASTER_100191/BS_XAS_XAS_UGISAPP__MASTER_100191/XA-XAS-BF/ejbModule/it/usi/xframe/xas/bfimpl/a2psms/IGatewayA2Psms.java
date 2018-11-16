/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.a2psms;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfutil.XASException;
import eu.unicredit.xframe.slf.Chronometer;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IGatewayA2Psms {
	InternalSmsResponse sendMessage(InternalSmsResponse internalSmsResponse, InternalSmsRequest internalSmsRequest, XasUser xasUser, Provider provider, Originator originator, boolean multiPart, Chronometer chronometer) throws XASException;
}
