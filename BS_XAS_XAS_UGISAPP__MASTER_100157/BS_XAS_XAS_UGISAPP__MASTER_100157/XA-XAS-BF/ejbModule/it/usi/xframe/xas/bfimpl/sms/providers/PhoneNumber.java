package it.usi.xframe.xas.bfimpl.sms.providers;

import it.usi.xframe.xas.bfutil.XASException;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @deprecated
 * WARNING: THIS CLASS IS NOT TO BE USED IN SMS SERVICE V.2 
 */
public class PhoneNumber

{
	private static Log log = LogFactory.getLog(PhoneNumber.class);

	//static final String FORMAT_RFC3966 = "[0-9]+";
	public static final String FORMAT_UBIQUITY = "[0-9]+";
	public static final String FORMAT_MSISDN   = "^([0-9]|\\+)(\\d*)$";
	public static final int ALIAS_MAX_LENGTH   = 11;			// max string length for alias	

	/**
	 * A valid form for phone number (i.e. remove all spaces)
	 * remove all permitted but unusefull chars
	 * +39 (338) 123-456 --> +39338123456
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static String normalizePhoneNumber(String phoneNumber) throws Exception
	{
		if(phoneNumber==null)
			throw new Exception("Null phoneNumber to normalize");
		StringBuffer normal = new StringBuffer("");
		for (int i=0; i<phoneNumber.length(); ++i)
		{
			String a = phoneNumber.substring(i,i+1);
			if(a.equals(" ") || a.equals("-") || a.equals("(") || a.equals(")"))
			{
			}
			else
				normal.append( a );
		}
		return normal.toString();
		
//		String[] toks = phoneNumber.trim().split("\\s");
//		StringBuffer normal = new StringBuffer("");
//		for (int i=0; i<toks.length ;++i)
//			normal.append( toks[i] );
//		return normal.toString();
	}

	/**
	 * Check for phone number not in black list.
	 * TODO implementare!!!
	 * @param phoneNumber to check for black list.
	 */
	public static void checkBlackList (String phoneNumber) throws XASException {
		String regex = "^(555.*)$"; // Black list mobile starting with 555
		if(phoneNumber == null || regex == null || regex.length() == 0) {
			return;
		}

    	boolean foundMatch = false; 
    	try	{
    		foundMatch = phoneNumber.matches(regex);
    	} catch (PatternSyntaxException ex)	{
			throw new XASException("NumberParseException: Syntax error in the regular expression");
    	}

    	if (foundMatch) {
			throw new XASException("Phone number [" + phoneNumber + "] black listed in [" + regex + "]");
    	}
	}

	/**
	 * Validate the phone number.
	 * @param phoneNumber
	 */
	public static void validatePhoneNumber (String phoneNumber, String regex) throws XASException {
		if (phoneNumber == null || regex == null || regex.length() == 0)
			return;

    	boolean foundMatch = false; 
    	try {
    		foundMatch = phoneNumber.matches(regex);
    	} catch (PatternSyntaxException ex)	{
			throw new XASException("NumberParseException: Syntax error in the regular expression");
    	}

    	if(!foundMatch){
			throw new XASException("Phone number [" + phoneNumber + "] must be formatted as [" + regex + "]");
    	}
	}


	/**
	 * Validate the alias
	 * @param alias
	 */
	public static void validateAlias(String alias, String regex) throws XASException
	{
		if(alias==null || regex==null)
			return;
		if(alias.length()>ALIAS_MAX_LENGTH)
			throw new XASException("The alias [" + alias + "] exceeds the max length of " + ALIAS_MAX_LENGTH + " characters.");
	}
	
	public static void main (String[] args)
	{
		try
		{
			// TEST1
			System.out.println("\nTEST1");
			String p1 = "+39 (338) 123-1456 ";
			System.out.println("original=" + p1);
			String r1 = normalizePhoneNumber(p1);
			System.out.println("result=" + r1);
			try {
				validatePhoneNumber(r1, FORMAT_MSISDN);
				System.out.println("validate ok");
			}
			catch (XASException e) {
				System.out.println("validate KO");
				System.out.println(e.getMessage());
			}
			// TEST2
			System.out.println("\nTEST2");
			String p2 = "+39/(338) A123-1456 ";
			System.out.println("original=" + p2);
			String r2 = normalizePhoneNumber(p2);
			System.out.println("result=" + r2);
			try {
				validatePhoneNumber(r2, FORMAT_MSISDN);
				System.out.println("validate ok");
			}
			catch (XASException e) {
				System.out.println("validate KO");
				System.out.println(e.getMessage());
			}
			// TEST2
			System.out.println("\nTEST3");
			String p3 = "";
			System.out.println("original=" + p3);
			String r3 = normalizePhoneNumber(p3);
			System.out.println("result=" + r3);

			validatePhoneNumber(r3, FORMAT_MSISDN);
			System.out.println("validate ok");
		}
		catch (XASException e) {
			System.out.println("validate KO");
			System.out.println(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
