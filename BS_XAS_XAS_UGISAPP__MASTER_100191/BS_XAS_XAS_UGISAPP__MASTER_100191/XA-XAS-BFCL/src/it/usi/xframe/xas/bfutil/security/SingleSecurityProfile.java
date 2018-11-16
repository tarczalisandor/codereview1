package it.usi.xframe.xas.bfutil.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author C305373
 *
 * this class is created to manage single profile defined into j2ee app
 */
public class SingleSecurityProfile
{
	private static Logger logger = LoggerFactory.getLogger(SingleSecurityProfile.class);

	// lower bit means less security
	private static final int VALUE_NOTINITIALIZED		= Integer.parseInt("0000000000", 2);
	private static final int VALUE_UNAUTHENTICATED		= Integer.parseInt("0000000001", 2);
	private static final int VALUE_AUTHENTICATED		= Integer.parseInt("0000000010", 2);
	private static final int VALUE_EMAIL				= Integer.parseInt("0000000100", 2);
	private static final int VALUE_SENDSMS				= Integer.parseInt("0000001000", 2);	// DEPRECATED: use VALUE_MT
	private static final int VALUE_MT					= Integer.parseInt("0000010000", 2);
	private static final int VALUE_MODR					= Integer.parseInt("0000100000", 2);
	private static final int VALUE_SEARCH				= Integer.parseInt("0001000000", 2);
	private static final int VALUE_STATS				= Integer.parseInt("0010000000", 2);
	private static final int VALUE_CONFIG				= Integer.parseInt("0100000000", 2);
	private static final int VALUE_ADMIN				= Integer.parseInt("1000000000", 2);
	private static final int VALUE_ALL					= Integer.parseInt("1111111111", 2);

	private static final String VALUE_S_NOTINITIALIZED	= "";
	private static final String VALUE_S_UNAUTHENTICATED	= "Unauthenticated";
	private static final String VALUE_S_AUTHENTICATED	= "Authenticated";
	private static final String VALUE_S_EMAIL			= "SendEmail";
	private static final String VALUE_S_SENDSMS			= "SendSms";
	private static final String VALUE_S_MT				= "SendMT";
	private static final String VALUE_S_MODR			= "SendMODR";
	private static final String VALUE_S_SEARCH			= "SearchSms";
	private static final String VALUE_S_STATS			= "ViewStatistics";
	private static final String VALUE_S_CONFIG			= "ManageConfiguration";
	private static final String VALUE_S_ADMIN			= "Admin";
	private static final String VALUE_S_ALL				= "";

	private static final String VALUE_S_SENDSMS_PGPROFILE	= "XAS002";
	private static final String VALUE_S_MT_PGPROFILE		= "XAS002";
	private static final String VALUE_S_MODR_PGPROFILE		= "XAS002";

	public static final SingleSecurityProfile NOTINITIALIZED	= new SingleSecurityProfile(SingleSecurityProfile.VALUE_NOTINITIALIZED);
	public static final SingleSecurityProfile UNAUTHENTICATED	= new SingleSecurityProfile(SingleSecurityProfile.VALUE_UNAUTHENTICATED);
	public static final SingleSecurityProfile AUTHENTICATED		= new SingleSecurityProfile(SingleSecurityProfile.VALUE_AUTHENTICATED);
	public static final SingleSecurityProfile EMAIL				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_EMAIL);
	public static final SingleSecurityProfile SENDSMS			= new SingleSecurityProfile(SingleSecurityProfile.VALUE_SENDSMS);
	public static final SingleSecurityProfile MT				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_MT);
	public static final SingleSecurityProfile MODR				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_MODR);
	public static final SingleSecurityProfile SEARCH			= new SingleSecurityProfile(SingleSecurityProfile.VALUE_SEARCH);
	public static final SingleSecurityProfile STATS				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_STATS);
	public static final SingleSecurityProfile CONFIG			= new SingleSecurityProfile(SingleSecurityProfile.VALUE_CONFIG);
	public static final SingleSecurityProfile ADMIN				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_ADMIN);
	public static final SingleSecurityProfile ALL				= new SingleSecurityProfile(SingleSecurityProfile.VALUE_ALL);

	private int b = VALUE_UNAUTHENTICATED;
	
	private SingleSecurityProfile (int value) {
		this.b = value;
	}
	
	/**
	 * @param role
	 * @return
	 * 
	 * return the SecurityProfileEx object associated to the role
	 */
	public static SingleSecurityProfile parseString (String role) {
		if(role.equalsIgnoreCase(VALUE_S_NOTINITIALIZED)) return SingleSecurityProfile.NOTINITIALIZED;
		else if(role.equalsIgnoreCase(VALUE_S_UNAUTHENTICATED)) return SingleSecurityProfile.UNAUTHENTICATED;
		else if(role.equalsIgnoreCase(VALUE_S_AUTHENTICATED)) return SingleSecurityProfile.AUTHENTICATED;
		else if(role.equalsIgnoreCase(VALUE_S_EMAIL)) return SingleSecurityProfile.EMAIL;
		else if(role.equalsIgnoreCase(VALUE_S_SENDSMS)) return SingleSecurityProfile.SENDSMS;
		else if(role.equalsIgnoreCase(VALUE_S_MT)) return SingleSecurityProfile.MT;
		else if(role.equalsIgnoreCase(VALUE_S_MODR)) return SingleSecurityProfile.MODR;
		else if(role.equalsIgnoreCase(VALUE_S_SEARCH)) return SingleSecurityProfile.SEARCH;
		else if(role.equalsIgnoreCase(VALUE_S_STATS)) return SingleSecurityProfile.STATS;
		else if(role.equalsIgnoreCase(VALUE_S_CONFIG)) return SingleSecurityProfile.CONFIG;
		else if(role.equalsIgnoreCase(VALUE_S_ADMIN)) return SingleSecurityProfile.ADMIN;
		else if(role.equalsIgnoreCase(VALUE_S_ALL)) return SingleSecurityProfile.ALL;
		return null;
	}

	/**
	 * @param o
	 * @return
	 */
	public boolean isEqual(SingleSecurityProfile o)
	{
		if(this.b == o.b)
			return true;
		return false;
	}

	/**
	 * Return the string value name of the corresponding object profile as defined in java EAR
	 * 
	 * @return
	 */
	public String getJavaProfileDescription()
	{
		if(this.b==VALUE_NOTINITIALIZED) return VALUE_S_NOTINITIALIZED;
		if(this.b==VALUE_UNAUTHENTICATED) return VALUE_S_UNAUTHENTICATED;
		if(this.b==VALUE_AUTHENTICATED) return VALUE_S_AUTHENTICATED;
		if(this.b==VALUE_EMAIL) return VALUE_S_EMAIL;
		if(this.b==VALUE_SENDSMS) return VALUE_S_SENDSMS;
		if(this.b==VALUE_MT) return VALUE_S_MT;
		if(this.b==VALUE_MODR) return VALUE_S_MODR;
		if(this.b==VALUE_SEARCH) return VALUE_S_SEARCH;
		if(this.b==VALUE_STATS) return VALUE_S_STATS;
		if(this.b==VALUE_CONFIG) return VALUE_S_CONFIG;
		if(this.b==VALUE_ADMIN) return VALUE_S_ADMIN;
		if(this.b==VALUE_ALL) return VALUE_S_ALL;
		return null;
	}

	/**
	 * Return the string value name of the corresponding object profile as defined in PG
	 * 
	 * @return
	 */
	public String getPgProfileDescription()
	{
		if(this.b==VALUE_SENDSMS) return VALUE_S_SENDSMS_PGPROFILE;
		if(this.b==VALUE_MT) return VALUE_S_MT_PGPROFILE;
		if(this.b==VALUE_MODR) return VALUE_S_MODR_PGPROFILE;
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 *
	 * the role description must be the same defined into web descriptor
	 */
	public String toString()
	{
		return this.getJavaProfileDescription();
	}

	/**
	 * @return
	 * 
	 * return the binary string representation
	 */
	public String toBinaryString()
	{
		return Integer.toBinaryString(this.b);
	}

	/**
	 * @return
	 * 
	 * return the integer value of current profile
	 */
	public int getValue()
	{
		return this.b;
	}
}
