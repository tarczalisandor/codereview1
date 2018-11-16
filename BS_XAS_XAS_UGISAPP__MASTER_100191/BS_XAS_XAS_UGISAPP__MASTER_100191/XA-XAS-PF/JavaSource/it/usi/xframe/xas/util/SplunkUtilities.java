package it.usi.xframe.xas.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SplunkUtilities {

	
	public static String buildSplunkQuery(String splunkQuery , HashMap hParams){
		Set set = hParams.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	        Map.Entry mentry = (Map.Entry)iterator.next();
	        splunkQuery = splunkQuery.replaceAll((String) mentry.getKey(), (String) mentry.getValue());
	    }
	    return splunkQuery;
	}

	
}
