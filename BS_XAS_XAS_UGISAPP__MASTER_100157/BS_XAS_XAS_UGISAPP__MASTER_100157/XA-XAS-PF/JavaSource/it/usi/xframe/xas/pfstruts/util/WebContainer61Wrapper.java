/*
 * Created on Apr 2, 2010
 */
package it.usi.xframe.xas.pfstruts.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wrapper for <code>com.ibm.ws.webcontainer.WebContainer</code> WebSphere6.1 class
 * 
 * @author EE01995
 */
public class WebContainer61Wrapper {

	private static Log log = LogFactory.getLog(WebContainer61Wrapper.class);

	private static final String WEBCONTAINER_CLASS =
		"com.ibm.ws.webcontainer.WebContainer";

	private Class webContainer61Class;

	public WebContainer61Wrapper() {
		try {
			webContainer61Class = Class.forName(WEBCONTAINER_CLASS);
		} catch (ClassNotFoundException ex) {
			log.error(printStackTrace(ex));
		}
	}

	protected Object getWebContainer() throws Exception {
		Method meth =
			webContainer61Class.getMethod("getWebContainer", new Class[] {
		});
		return meth.invoke(webContainer61Class, new Object[] {
		});
	}

	public String[] getVirtualHostNames() {
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		Vector vect = new Vector();
		try {
			Method meth =
				getWebContainer().getClass().getMethod("getVirtualHosts", params);
			Iterator it = (Iterator) meth.invoke(getWebContainer(), paramsObj);
			while (it.hasNext()) {
				Object obj = it.next();
				//Class vitualHost = Class.forName("com.ibm.ws.webcontainer.VirtualHost");
				//Object obj = vitualHost.cast(it.next());
				Method meth2 = obj.getClass().getMethod("getName", params);
				String name = (String) meth2.invoke(obj, paramsObj);
				vect.add(name);
			}
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		return (String[]) vect.toArray(new String[vect.size()]);
	}

	public Hashtable getLocalServerProp() {
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		Hashtable result = new Hashtable();
		try {
			Class adminServiceFactoryClass =
				Class.forName("com.ibm.websphere.management.AdminServiceFactory");
			Method meth =
				adminServiceFactoryClass.getMethod("getAdminService", params);
			Object adminServiceClass =
				(Object) meth.invoke(adminServiceFactoryClass, paramsObj);
			Method meth2 =
				adminServiceClass.getClass().getMethod("getLocalServer", params);
			ObjectName objName =
				(ObjectName) meth2.invoke(adminServiceClass, paramsObj);
			result = objName.getKeyPropertyList();
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		return result;
	}

	public String getLongLocalHostName() {
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		String longLocalHost = "null";
		try {
			Class adminAppClient =
				Class.forName("com.ibm.ws.scripting.AdminAppClient");
			Method meth = adminAppClient.getMethod("getLongLocalHost", params);
			longLocalHost = (String) meth.invoke(adminAppClient, paramsObj);
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		return longLocalHost;
	}

	public Vector listApplications(String serverName) {
		log.info("START listApplications");
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		Vector apps = new Vector(0);
		try {
			Class appManClass = Class.forName("com.ibm.websphere.management.application.AppManagement");
			Class appManProxy =
				Class.forName(
					"com.ibm.websphere.management.application.AppManagementProxy");
			Method meth1 = appManProxy.getMethod("getJMXProxyForServer", params);
			Object appMan = appManClass.getClass();
			log.info("prima di meth1 invoke");
			appMan = meth1.invoke(appManProxy, paramsObj);

			Hashtable prefs = new Hashtable();
			prefs.put("server", serverName);
			log.info("preparazione di meth2 invoke");
			Method meth2 =
				appMan.getClass().getMethod(
					"listApplications",
					new Class[] { Hashtable.class, String.class });
			log.info("prima di meth2 invoke");
			apps = (Vector) meth2.invoke(appMan, new Object[] { prefs, null });
			log.info("ok");
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		log.info("END listApplications");
		return apps;
	}

	public Vector listApplications2(String serverName) {
		log.info("START listApplications2");
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		Vector apps = new Vector(0);
		try {
			Class appManClass = Class.forName("com.ibm.websphere.management.application.AppManagement");
			Class appManProxy =
				Class.forName(
					"com.ibm.websphere.management.application.AppManagementProxy");
			Method meth1 = appManProxy.getMethod("getLocalProxy", params);
			Object appMan = appManClass.getClass();
			log.info("prima di meth1 invoke");
			appMan = meth1.invoke(appManProxy, paramsObj);

			Hashtable prefs = new Hashtable();
			prefs.put("server", serverName);
			log.info("preparazione di meth2 invoke");
			Method meth2 =
				appMan.getClass().getMethod(
					"listApplications",
					new Class[] { Hashtable.class, String.class });
			log.info("prima di meth2 invoke");
			apps = (Vector) meth2.invoke(appMan, new Object[] { prefs, null });
			log.info("ok");
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		log.info("END listApplications2");
		return apps;
	}

	public Vector listApplications3(String serverName) {
		log.info("START listApplications3");
		Class params[] = {
		};
		Object paramsObj[] = {
		};
		Vector apps = new Vector(0);
		try {
			Class appManClass = Class.forName("com.ibm.websphere.management.application.AppManagement");
			Class appManProxy =
				Class.forName(
					"com.ibm.websphere.management.application.AppManagementFactory");
			Method meth1 = appManProxy.getMethod("createLocalAppManagementImpl", params);
			Object appMan = appManClass.getClass();
			log.info("prima di meth1 invoke");
			appMan = meth1.invoke(appManProxy, paramsObj);

			Hashtable prefs = new Hashtable();
			prefs.put("server", serverName);
			log.info("preparazione di meth2 invoke");
			Method meth2 =
				appMan.getClass().getMethod(
					"listApplications",
					new Class[] { Hashtable.class, String.class });
			log.info("prima di meth2 invoke");
			apps = (Vector) meth2.invoke(appMan, new Object[] { prefs, null });
			log.info("ok");
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		log.info("END listApplications3");
		return apps;
	}

	public String getName() {
		String retobj = null;
		try {
			Method meth =
				getWebContainer().getClass().getMethod("getName", new Class[] {
			});
			retobj = (String) meth.invoke(getWebContainer(), new Object[] {
			});
			return retobj;
		} catch (Exception ex) {
			log.error(printStackTrace(ex));
		}
		return retobj;
	}

	private String printStackTrace(Exception ex) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(out);
		ex.printStackTrace(pw);
		pw.flush();
		pw.close();
		return out.toString();
	}

}
