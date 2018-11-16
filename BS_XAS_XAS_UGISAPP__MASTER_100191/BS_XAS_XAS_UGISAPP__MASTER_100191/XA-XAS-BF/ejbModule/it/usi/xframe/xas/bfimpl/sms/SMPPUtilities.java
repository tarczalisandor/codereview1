package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfutil.XASRuntimeException;
import it.usi.xframe.xas.bfutil.data.SmsRequest;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SMPPUtilities {

	private static SMPPUtilities instance = new SMPPUtilities();
	private Log log = LogFactory.getLog(this.getClass());
	
	public static final String SMPP_DATE_TIME_FORMAT = "yyMMddHHmmss";
	public static final String TIME_ZONE_FORMAT = "Z";
	public static final DateFormat dtdf = new SimpleDateFormat(SMPP_DATE_TIME_FORMAT);
	public static final DateFormat tzdf = new SimpleDateFormat(TIME_ZONE_FORMAT);
	public static final DateTimeFormatter jdtf = DateTimeFormat.forPattern(SMPP_DATE_TIME_FORMAT);
	public static final DateTimeFormatter jtzf = DateTimeFormat.forPattern(TIME_ZONE_FORMAT);
	/*
	 * Methods which use java.text.DateFormat are synchronized to avoid problems with concurrency
	 */
	
	private SMPPUtilities() {
	}
	
	public static SMPPUtilities getInstance() {
		return instance;
	}
	
	public synchronized String convertDate(Date d) {
		StringBuffer sb = new StringBuffer();
		dtdf.format(d, sb, new FieldPosition(DateFormat.YEAR_FIELD));	//field position is useless, but... NullPointerException if not present!
		sb.append("0");	//tenth of seconds always need to be 0
		sb.append(getTimeZone(d));
		return sb.toString();
	}
	
	public String convertDate(DateTime d) {
		StringBuffer sb = new StringBuffer();
		jdtf.printTo(sb, d);
		sb.append("0");	//tenth of seconds always need to be 0
		sb.append(getTimeZone(d));
		return sb.toString();
	}
	
	private synchronized String getTimeZone(Date d) {
		return getTimeZone(tzdf.format(d));
	}
	
	private String getTimeZone(DateTime d) {
		return getTimeZone(jtzf.print(d));
	}
	
	/*
	 * Gets timezone from input string and converts it in SMPP format (nnp, with nn being quarter hours and p being '+' or '-')
	 */
	private String getTimeZone(String tz) {
		String sign = tz.substring(0, 1);
		int hours = Integer.parseInt(tz.substring(1, 3));
		int minutes = Integer.parseInt(tz.substring(3, 5));
		int qh = hours*4;
		switch (minutes) {
		case 0:
			break;
		case 15:
			qh += 1;
			break;
		case 30:
			qh += 2;
			break;
		case 45:
			qh += 3;
			break;
		default: throw new XASRuntimeException("Cannot recognize TimeZone: " + tz);
		}
		return padQuarterHours(qh) + sign;
	}
	
	/*
	 * Takes an integer and puts it on a 2-digit string.
	 * Throws exception is number length is different from 1 and 2
	 */
	private String padQuarterHours(int qh) {
		String out = Integer.toString(qh);
		switch (out.length()) {
		case 1:
			out = "0" + out; break;
		case 2:
			break;
		default: throw new XASRuntimeException("Cannot recognize quarter hours as " + qh);
		}
		return out;
	}
}
