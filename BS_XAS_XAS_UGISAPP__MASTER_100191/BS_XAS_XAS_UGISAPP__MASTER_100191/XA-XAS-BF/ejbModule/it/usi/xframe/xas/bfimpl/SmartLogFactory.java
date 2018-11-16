package it.usi.xframe.xas.bfimpl;

import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SLF_Exception;
import eu.unicredit.xframe.slf.SmartAnalytic;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class SmartLogFactory {

	private static SmartLogFactory instance;
	
	public static String APP_CODE = "XAS";
	public static String APP_VERSION = "1.0";

	private SmartLogFactory() {}
		
	public static synchronized SmartLogFactory getInstance() {
		if (instance == null) instance = new SmartLogFactory();
		return instance;
	}
	
	public SmartLog createLog(Object requester, String id) {
		return _createLog(requester, id);
	}
	
	public SmartLog createLoggerWithoutId(Object requester) {
		return createLog(requester, null);
	}

	public SmartAnalytic createAnalytic(Object requester, String id) throws SLF_Exception  {
		return _createAnalytic(requester, id, SmartAnalytic.COUPLING_COMMON_I);
	}
	
	public SmartAnalytic createAnalyticWithoutId(Object requester) {
		return _createAnalytic(requester, null, SmartAnalytic.COUPLING_COMMON_I);
	}
	
	public SmartAnalytic createLooseAnalyticWithoutId(Object requester) {
		return _createAnalytic(requester, null, SmartAnalytic.COUPLING_LOOSE_I);
	}
	
	private SmartAnalytic _createAnalytic(Object requester, String id, int coupling) {
		SmartAnalytic log = new SmartAnalytic(coupling);
		if (id == null) {
			id = (String) MDC.get(eu.unicredit.xframe.slf.Constants.MDC_KEY_UUID);
			if (id == null) {
				id = UUID.randomUUID().toString();
				MDC.put(eu.unicredit.xframe.slf.Constants.MDC_KEY_UUID, id);
			}
		}
		log.collectItCompact(APP_CODE,APP_VERSION,requester.getClass().getName(),id);
		return log;
	}
	
	private SmartLog _createLog(Object requester, String id) {
		SmartLog log = new SmartLog();
		if (id == null) {
			UUID uuid = UUID.randomUUID();
			id = uuid.toString();
		}
		log.logItCompact(APP_CODE,APP_VERSION,requester.getClass().getName(),id);
		return log;
	}
	
	
}

