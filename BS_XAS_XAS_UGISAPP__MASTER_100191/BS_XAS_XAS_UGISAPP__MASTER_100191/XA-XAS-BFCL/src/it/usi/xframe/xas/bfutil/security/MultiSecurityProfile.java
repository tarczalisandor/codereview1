package it.usi.xframe.xas.bfutil.security;


import java.util.ArrayList;

public class MultiSecurityProfile
{
	private int b = SingleSecurityProfile.AUTHENTICATED.getValue();
	
	public MultiSecurityProfile (SingleSecurityProfile o) {
		this.b = o.getValue();
	}

	/**
	 * @return
	 */
	public int getValue() {
		return this.b;
	}
	
	/**
	 * @param o
	 * @return
	 * 
	 * add an application profile to the current one
	 */
	public MultiSecurityProfile add(MultiSecurityProfile o)
	{
		this.b |= o.b;
		return this;
	}

	/**
	 * @param o
	 * @return
	 * 
	 * add a security profile to the current one
	 */
	public MultiSecurityProfile add(SingleSecurityProfile o)
	{
		this.b |= o.getValue();
		return this;
	}

	/**
	 * @param o
	 * @return
	 * 
	 * remove a profile from the current one
	 */
	public MultiSecurityProfile remove(SingleSecurityProfile o)
	{
		// ~ binary inversion
		this.b &= ~o.getValue();
		return this;
	}

	/**
	 * @param o
	 * @return
	 * 
	 * return all SecurityProfiles contained into ApplicationProfile
	 */
	public static SingleSecurityProfile[] getSecurityProfileList (MultiSecurityProfile o)
	{
		ArrayList list = new ArrayList();
		if((o.b & SingleSecurityProfile.UNAUTHENTICATED.getValue())	==SingleSecurityProfile.UNAUTHENTICATED.getValue())	list.add(SingleSecurityProfile.UNAUTHENTICATED);
		if((o.b & SingleSecurityProfile.AUTHENTICATED.getValue())	==SingleSecurityProfile.AUTHENTICATED.getValue()) 	list.add(SingleSecurityProfile.AUTHENTICATED);
		if((o.b & SingleSecurityProfile.EMAIL.getValue())		==SingleSecurityProfile.EMAIL.getValue())				list.add(SingleSecurityProfile.EMAIL);
		if((o.b & SingleSecurityProfile.SENDSMS.getValue())		==SingleSecurityProfile.SENDSMS.getValue())				list.add(SingleSecurityProfile.SENDSMS);
		if((o.b & SingleSecurityProfile.MT.getValue())			==SingleSecurityProfile.MT.getValue())					list.add(SingleSecurityProfile.MT);
		if((o.b & SingleSecurityProfile.MODR.getValue())		==SingleSecurityProfile.MODR.getValue())				list.add(SingleSecurityProfile.MODR);
		if((o.b & SingleSecurityProfile.SEARCH.getValue())		==SingleSecurityProfile.SEARCH.getValue())				list.add(SingleSecurityProfile.SEARCH);
		if((o.b & SingleSecurityProfile.STATS.getValue())		==SingleSecurityProfile.STATS.getValue())				list.add(SingleSecurityProfile.STATS);
		if((o.b & SingleSecurityProfile.CONFIG.getValue())		==SingleSecurityProfile.CONFIG.getValue())				list.add(SingleSecurityProfile.CONFIG);
		if((o.b & SingleSecurityProfile.ADMIN.getValue())		==SingleSecurityProfile.ADMIN.getValue())				list.add(SingleSecurityProfile.ADMIN);
		return (SingleSecurityProfile[]) list.toArray(new SingleSecurityProfile[list.size()]);
	}

	/**
	 * @return
	 * 
	 * return all SecurityProfiles contained into ApplicationProfile
	 */
	public SingleSecurityProfile[] getSecurityProfileList ()
	{
		return getSecurityProfileList(this);
	}

	/**
	 * @param p
	 * @return
	 * search the right profile that has the bit 
	 */
	private static SingleSecurityProfile searchProfileByBitPosition(int p)
	{
		if((p & SingleSecurityProfile.UNAUTHENTICATED.getValue())	==SingleSecurityProfile.UNAUTHENTICATED.getValue())	return SingleSecurityProfile.UNAUTHENTICATED;
		else if((p & SingleSecurityProfile.AUTHENTICATED.getValue())==SingleSecurityProfile.AUTHENTICATED.getValue())	return SingleSecurityProfile.AUTHENTICATED;
		else if((p & SingleSecurityProfile.EMAIL.getValue())		==SingleSecurityProfile.EMAIL.getValue())			return SingleSecurityProfile.EMAIL;
		else if((p & SingleSecurityProfile.SENDSMS.getValue())		==SingleSecurityProfile.SENDSMS.getValue())			return SingleSecurityProfile.SENDSMS;
		else if((p & SingleSecurityProfile.MT.getValue())			==SingleSecurityProfile.MT.getValue())				return SingleSecurityProfile.MT;
		else if((p & SingleSecurityProfile.MODR.getValue())			==SingleSecurityProfile.MODR.getValue())			return SingleSecurityProfile.MODR;
		else if((p & SingleSecurityProfile.SEARCH.getValue())		==SingleSecurityProfile.SEARCH.getValue())			return SingleSecurityProfile.SEARCH;
		else if((p & SingleSecurityProfile.STATS.getValue())		==SingleSecurityProfile.STATS.getValue())			return SingleSecurityProfile.STATS;
		else if((p & SingleSecurityProfile.CONFIG.getValue())		==SingleSecurityProfile.CONFIG.getValue())			return SingleSecurityProfile.CONFIG;
		else if((p & SingleSecurityProfile.ADMIN.getValue())		==SingleSecurityProfile.ADMIN.getValue())			return SingleSecurityProfile.ADMIN;
		return null;
	}

	/**
	 * @return
	 * 
	 * return the lowest profile present in profile list
	 */
	public static SingleSecurityProfile getLowest (SingleSecurityProfile[] value) {
		int v = SingleSecurityProfile.UNAUTHENTICATED.getValue();
		if(value!=null)
			for(int i=0;i<value.length;i++)
				v|=value[i].getValue();
		return searchProfileByBitPosition(v);
	}

	/**
	 * @return
	 * 
	 * return the lowest profile present in the current profile
	 */
//	private SecurityProfileEx getLowest () {
//		return getLowest (this);
//	}

	/**
	 * @return
	 * 
	 * return the lowest profile present in the current profile
	 */
//	public SecurityProfileEx getLowest (SecurityProfileEx o) {
//		if(logger.isDebugEnabled())
//			logger.debug("SecurityProfileEx binary:" + Integer.toBinaryString(o.b));
//		int pos = o.b==0?0:Integer.toBinaryString(o.b).length()-Integer.toBinaryString(o.b).lastIndexOf("1");
//		if((pos & VALUE_UNAUTHENTICATED)==VALUE_UNAUTHENTICATED) return SecurityProfileEx.UNAUTHENTCATED;
//		else if((pos & VALUE_AUTHENTICATED)==VALUE_AUTHENTICATED) return SecurityProfileEx.AUTHENTCATED;
//		else if((pos & VALUE_EMAIL)==VALUE_EMAIL) return SecurityProfileEx.EMAIL;		// will never match
//		else if((pos & VALUE_SMS)==VALUE_SMS) return SecurityProfileEx.SMS;			// will never match
//		else if((pos & VALUE_SEARCH)==VALUE_SEARCH) return SecurityProfileEx.SEARCH;	// will never match
//		else if((pos & VALUE_STATS)==VALUE_STATS) return SecurityProfileEx.STATS;		// will never match
//		else if((pos & VALUE_CONFIG)==VALUE_CONFIG) return SecurityProfileEx.CONFIG;	// will never match
//		else if((pos & VALUE_ADMIN)==VALUE_ADMIN) return SecurityProfileEx.ADMIN;		// will never match
//		return null;
//	}

	public static void main(String[] args)
	{
		int pippo = Integer.parseInt("00000001", 2);
		System.out.println("->" + pippo);
		
		SingleSecurityProfile s = null;
		SingleSecurityProfile sLow  = null;

		int b = Integer.parseInt("00000000", 2);
		if((b & SingleSecurityProfile.UNAUTHENTICATED.getValue())==SingleSecurityProfile.UNAUTHENTICATED.getValue())
			System.out.println("true");

//		s = SecurityProfileEx.SMS;
//		System.out.println("role is:" + s.toBinaryString());
//		s.add(SecurityProfileEx.AUTHENTCATED);
//		s.add(SecurityProfileEx.EMAIL);
//		System.out.println("role is:" + s.toBinaryString());
//		s.remove(SecurityProfileEx.EMAIL);
//		System.out.println("role is:" + s.toBinaryString());
//
//		s = SecurityProfileEx.SMS.add(SecurityProfileEx.EMAIL.add(SecurityProfileEx.AUTHENTCATED));
//		System.out.println("role is:" + s.toBinaryString());

//		s = SecurityProfileEx.SMS;
//		System.out.println("sms role is:" + s.toString());

//		s = SecurityProfileEx.ADMIN;
//		sLow = s.getLowest();
//		System.out.println("1) Lowset:" + sLow.toString());
//
//		s = SecurityProfileEx.SEARCH;
//		sLow = s.getLowest();
//		System.out.println("2) Lowset:" + sLow.toString());
//
//		SecurityProfileEx[] ss = new SecurityProfileEx[]{SecurityProfileEx.UNAUTHENTCATED, SecurityProfileEx.SMS}; 
//		s = SecurityProfileEx.getLowest(ss);
//		System.out.println("3) Lowset:" + sLow.toString());
	}
}
