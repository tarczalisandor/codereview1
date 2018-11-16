/*
 * Created on Feb 17, 2010
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfintf.IXasSecurityServiceFacade;

/**
 * @author EE01995
 */
public class XasSecurityServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasSecurityServiceFacade {

	/**
	 * Retrieve the logged userid.
	 * 
	 * @return the logged userid.
	 */
	public String getLoggedUser() {
		return getSupport().getPrincipal().getName();
	}

}
