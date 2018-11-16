package it.usi.xframe.xas.bfutil.security;

import it.usi.xframe.system.eservice.support.SupportFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtility
{
	private static Logger logger = LoggerFactory.getLogger(SecurityUtility.class);
	
	/**
	 * @param applicationProfile
	 * @return
	 * 
	 * check the match of user role with method roles
	 */
	public static boolean isUserInRole(SupportFacade sf, MultiSecurityProfile msp)
	{
		boolean profileAccepted = false;
		SingleSecurityProfile[] sp = msp.getSecurityProfileList();
		for(int i=0; i<sp.length; i++)
		{
			profileAccepted |= sf.isUserInRole(sp[i].toString());
			logger.debug("Try to match " + sp[i].toString() + " with result " + profileAccepted);
			if(profileAccepted) break;
		}
		return profileAccepted;
	}

	public static boolean isUserInRole(SupportFacade sf, SingleSecurityProfile ssp)
	{
		boolean profileAccepted = sf.isUserInRole(ssp.toString());
		logger.debug("Try to match " + ssp.toString() + " with result " + profileAccepted);
		return profileAccepted;
	}
}
