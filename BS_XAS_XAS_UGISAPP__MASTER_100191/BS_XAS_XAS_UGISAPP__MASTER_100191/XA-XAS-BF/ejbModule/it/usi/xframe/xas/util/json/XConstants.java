package it.usi.xframe.xas.util.json;

import java.io.Writer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

/**
 * Global constants.
 */
public final class XConstants {
	/** Utility class. */
	private XConstants() {
		super();
	}
	/* Pretty print */
	public static final char[] XSTREAMER_PRETTY_DELIMITER = new char[] {' ',' '};
	public static final char[] XSTREAMER_PRETTY_NEWLINE = new char[] {'\n'};
    /* Compact print */
	public static final char[] XSTREAMER_DELIMITER = new char[0];
	public static final char[] XSTREAMER_NEWLINE = new char[0];
	// Logger JSON XStreamer Formatter
	public static final XStream XSTREAMER = new XStream(new JsonHierarchicalStreamDriver() { 
	       public HierarchicalStreamWriter createWriter(Writer writer) { // Disable pretty print 
 	    	   return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE,  
	                              new JsonWriter.Format(XSTREAMER_DELIMITER, XSTREAMER_NEWLINE,JsonWriter.Format.COMPACT_EMPTY_ELEMENT)); 
	         } 
	     });
	
	/**
	 * The utility class initializer.
	 */
	static {
//		String dateFormat = "yyyy-MM-dd";
//		String timeFormat = "HH:mm:ss";
//		String[] acceptableFormats = {timeFormat};

		XSTREAMER.registerConverter(new UUIDConverter());
//		XSTREAMER.registerConverter(new DateConverter(dateFormat, acceptableFormats));
		XSTREAMER.alias("SmsRequest", it.usi.xframe.xas.bfutil.data.SmsRequest.class);
		XSTREAMER.alias("SmsRequest3", it.usi.xframe.xas.bfutil.data.SmsRequest3.class);
		XSTREAMER.alias("Telecom.Customization", it.usi.xframe.xas.bfimpl.a2psms.providers.telecom.Customization.class);
		XSTREAMER.alias("Vodafonepop.Customization", it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop.Customization.class);
		XSTREAMER.alias("Sendmail.Customization", it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail.Customization.class);
		XSTREAMER.alias("XasUser", it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser.class);
		XSTREAMER.omitField(it.usi.xframe.xas.bfutil.data.SmsRequest.class,"msg"); 
		XSTREAMER.omitField(it.usi.xframe.xas.bfutil.data.SmsRequest.class,"msgBytes"); 
	}
}

