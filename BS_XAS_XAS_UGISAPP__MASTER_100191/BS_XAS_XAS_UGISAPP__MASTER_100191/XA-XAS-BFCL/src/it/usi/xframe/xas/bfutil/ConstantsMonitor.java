package it.usi.xframe.xas.bfutil;

/**
 * Tim global constants.
 */
public class ConstantsMonitor extends Constants
{
	/** Utility class. */
	private ConstantsMonitor() {
		super();
	}

	/** Current version of the log. */
	public static final String MY_LOG_VER			= "1.0.0";
	public static final String MY_ANALYTIC_VER2		= "1.0";

	public final static int MONITOR_WEBSERVER		= 1;
	public final static int MONITOR_APPSERVERJSON	= 2;
	public final static int MONITOR_APPSERVERHTML	= 3;
}
