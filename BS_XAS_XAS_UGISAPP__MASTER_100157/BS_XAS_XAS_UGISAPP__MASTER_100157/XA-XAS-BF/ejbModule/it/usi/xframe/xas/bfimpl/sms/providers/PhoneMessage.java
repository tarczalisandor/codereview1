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
//			if(a.equals("�"))
//				normal.append("e'");
//			else if(a.equals("�"))
//				normal.append("e'");
//			else if(a.equals("�"))
//				normal.append("u'");
//			else if(a.equals("�"))
//				normal.append("i'");
//			else if(a.equals("�"))
//				normal.append("o'");
//			else if(a.equals("�"))
//				normal.append("a");
//			else if(a.equals("�"))
//				normal.append("o");
//			else if(a.equals("�"))
//				normal.append("n");
//			else if(a.equals("�"))
//				normal.append("u");
//			else if(a.equals("�"))
//				normal.append("a'");
			if(a.equals("�"))
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
