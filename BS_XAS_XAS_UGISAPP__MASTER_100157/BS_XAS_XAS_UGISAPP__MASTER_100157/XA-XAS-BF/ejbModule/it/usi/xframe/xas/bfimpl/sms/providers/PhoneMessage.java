package it.usi.xframe.xas.bfimpl.sms.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PhoneMessage
{
	private static Log log = LogFactory.getLog(PhoneMessage.class);

	/**
	 * replace some letters with accent, trim spaces
	 * @param message
	 * @return
	 */
	public static String normalizePhoneMessage(String message) {
		// log.debug("Message to normalize: [" + message + "]");
		StringBuffer normal = new StringBuffer("");
		for (int i=0; i<message.length() ;++i)
		{
			String a = message.substring(i,i+1);
//			if(a.equals("è"))
//				normal.append("e'");
//			else if(a.equals("é"))
//				normal.append("e'");
//			else if(a.equals("ù"))
//				normal.append("u'");
//			else if(a.equals("ì"))
//				normal.append("i'");
//			else if(a.equals("ò"))
//				normal.append("o'");
//			else if(a.equals("ä"))
//				normal.append("a");
//			else if(a.equals("ö"))
//				normal.append("o");
//			else if(a.equals("ñ"))
//				normal.append("n");
//			else if(a.equals("ü"))
//				normal.append("u");
//			else if(a.equals("à"))
//				normal.append("a'");
			if(a.equals("€"))
				normal.append("EUR");
			else
				normal.append( a );
		}
		String ret = normal.toString();
		ret = ret.trim();
		// log.debug("Message normalized: [" + ret + "]");
		return ret;
	}

}
