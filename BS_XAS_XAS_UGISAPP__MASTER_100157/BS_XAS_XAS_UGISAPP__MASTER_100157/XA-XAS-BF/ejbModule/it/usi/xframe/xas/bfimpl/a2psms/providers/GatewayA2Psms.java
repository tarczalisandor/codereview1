/*
 * Created on Jan 19, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl.a2psms.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ws.webservices.engine.client.Stub;

import it.usi.xframe.xas.bfimpl.a2psms.IGatewayA2Psms;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class GatewayA2Psms implements IGatewayA2Psms {
	private static Logger logger = LoggerFactory.getLogger(GatewayA2Psms.class);

	public InternalSmsResponse sendMessage(InternalSmsRequest request, XasUser xasUser, Provider provider, Originator originator, boolean multipart) throws XASException {
		throw new XASRuntimeException("This method needs to be overridden");
	}
	public boolean isActive() {
		return true;
	}

	protected Byte IntToByte (int value) throws XASException {
    	if(value<0 || value >=128)
    		throw new XASException("Cannot convert from int to byte");
    	return new Byte((byte)value);
    }
	// Not less than 1000 otherwise websphere will multiply by 1000.
	protected void setTimeOut(Stub stub, Integer timeOut) {
		if (timeOut != null) {
			logger.debug("setTimeOut " + timeOut.intValue());
			((Stub) stub).setTimeout(timeOut.intValue()); // Not less than 1000 otherwise websphere will multiply by 1000.
//			((Stub) stub).setWriteTimeout(timeOut.intValue()); // Not less than 1000 otherwise websphere will multiply by 1000.
			
		}
	}

	/*
	public String UCS2ToString(byte[] msg) throws XASException	{
		if((msg.length % 2) != 0)
			throw new XASException("The message has a wrong length");
    
		StringBuffer curr = new StringBuffer();
		for (int i = 0; i < msg.length; i++)
		{
			int unsignedvalue = msg[i] & 0xFF;
			String hexString = Integer.toHexString(unsignedvalue);
			if (hexString.length() < 2)
			    hexString = "0" + hexString;
			else if (hexString.length() > 2)
				throw new XASException("Wrong hex conversion");

			curr.append(hexString);
		}
		String txt = curr.toString();
		return txt;
	}
	*/
	
}
