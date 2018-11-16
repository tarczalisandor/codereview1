/*
 * Created on Mar 26, 2010
 */
package it.usi.xframe.xas.pfstruts.servlet;

import it.usi.xframe.system.bfinfo.BaselineInfo;
import it.usi.xframe.system.bfinfo.BaselineInfoFactory;

import java.io.IOException;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import com.ibm.ws.runtime.deploy.ApplicationCollaborator;
import com.ibm.websphere.runtime.ServerName;
import com.ibm.ws.runtime.deploy.DeployedModule;
import com.ibm.ws.webservices.engine.server.ServerEngine;
import com.ibm.ws.webcontainer.util.WASSystem;
import com.ibm.ws.webcontainer.webapp.WebApp;
import com.ibm.websphere.servlet.context.IBMServletContext;
import com.ibm.websphere.servlet.event.ServletContextEventSource;
import com.ibm.ws.webcontainer.WebContainer;
import com.ibm.ws.webcontainer.WebAppHost;
import com.ibm.ws.http.Alias;
import com.ibm.ws.http.VirtualHost;*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author EE01995
 */
public class RichInfoServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(RichInfoServlet.class);
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
//		try {
//			HashMap map = new HashMap(20);
//
//			/*
//			System.out.println("## HEADER..");
//			for (java.util.Enumeration en = request.getHeaderNames(); en.hasMoreElements();) {
//				String h = (String) en.nextElement();
//				System.out.println("## HEADER [" + h + "] -> " + request.getHeader(h));
//			}
//			*/
//
//			map.put("serverName.FullName", ServerName.getFullName());
//			map.put("serverName.Id", ServerName.getServerId());
//			
//			map.put("servletContext.serverInfo", getServletContext().getServerInfo());
//			map.put("servletContext.serverName", request.getServerName());
//			map.put("servletContext.serverPort", String.valueOf(request.getServerPort()));
//
//			//VirtualHostMgrImpl v = new VirtualHostMgrImpl();
//			//WebContainerImpl vv = new WebContainerImpl();
//			/*
//			for (java.util.Enumeration en = WASSystem.getPropertyNames(); en.hasMoreElements();) {
//				String s = (String) en.nextElement();
//				System.out.println("## WASSystem [" + s + "] -> " + WASSystem.getProperty(s));
//			}
//			*/
//
//			map.put("nodeName", WebContainer.getWebContainer().getNodeName());
//			map.put("cellName", WebContainer.getWebContainer().getCellName());
//			map.put("serverName", WebContainer.getWebContainer().getServerName());
//
///*
//			System.out.println("## b4: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getVirtualHost().getVirtualHostName());						
//			System.out.println("## b1: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getWebAppName());
//			System.out.println("## b2: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getApplicationName());
//			System.out.println("## b3: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getContextPath());
//			System.out.println("## b4: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getVirtualHost().getVirtualHostName());
//			System.out.println("## b5: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getName());
//			System.out.println("## b6: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getNodeName());
//			
//			System.out.println("## getVirtualHostName: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getVirtualHost().getVirtualHostName());
//			System.out.println("## getVirtualHostName: " + WebContainer.getWebContainer().getWebGroup("default_host/XA-XAS-PF").getServletHost().getVirtualHostName());
//*/
//			try {
//				WebAppHost webappHost = WebContainer.getWebContainer().findServletHostByHostname(request.getServerName(), request.getServerPort());
//
//				map.put("virtualHostName", webappHost.getVirtualHostName());
//				map.put("configurationName", webappHost.getConfiguration().getName());
//
//				if (webappHost.getConfiguration().getAliases() != null)
//					map.put("number of aliases", String.valueOf(webappHost.getConfiguration().getAliases().length));
//				else map.put("number of aliases", "null");
//				Alias alias = webappHost.getConfiguration().getAliases()[0];
//
///*
//				System.out.println("## 1.1: " + webapp.getConfiguration().getAliases()[0].getHostname() + ":" + webapp.getConfiguration().getAliases()[0].getPort());
//				System.out.println("## 1.2: " + webapp.getConfiguration().getAliases()[1].getHostname() + ":" + webapp.getConfiguration().getAliases()[1].getPort());
//				System.out.println("## 1.3: " + webapp.getConfiguration().getAliases()[2].getHostname() + ":" + webapp.getConfiguration().getAliases()[2].getPort());
//*/
//				//System.out.println("## findServletHostByHostname: " + WebContainer.getWebContainer().findServletHostByHostname(request.getServerName(), request.getServerPort()));
//			} catch (Exception e) {
//				log.error(e.getMessage());
//			}
//
//			SortedMap mapWebGroup = new TreeMap();
//			for (java.util.Enumeration en = WebContainer.getWebContainer().getWebGroupNames(); en.hasMoreElements();) {
//				String s = (String) en.nextElement();
//				String appName = WebContainer.getWebContainer().getWebGroup(s).getApplicationName();
//				WebApp webapp = WebContainer.getWebContainer().getWebGroup(s).getWebApp();
//				
//				String prodName = null;
//				String version = null;
//				if (appName.lastIndexOf("-") > -1) {
//					prodName = appName.substring(appName.lastIndexOf("-") + 1).toLowerCase();
//					BaselineInfo baselineInfo = BaselineInfoFactory.getInstance().getInfo(prodName);
//					version = baselineInfo.getVersion();
//				}
//				if (version == null || "".equals(version))
//					version = "unknown";
//				mapWebGroup.put(s, appName + "</td><td>" + version + "</td><td>" + WebContainer.getWebContainer().getWebGroup(s).getConfiguration().getState());
//
///*
//				System.out.println("## a.parent1: " +  WebContainer.getWebContainer().getWebGroup(s).getServletHost().getParent().getCellName());
//				System.out.println("## a.parent2: " + WebContainer.getWebContainer().getWebGroup(s).getServletHost().getParent().getServerName());
//				if ("XA-XAS".equals(appName)) {
//					for (java.util.Enumeration en2 = webapp.getAttributeNames(); en2.hasMoreElements();) {
//						String s2 = (String) en2.nextElement();
//						System.out.println("## a.getAttribute ["+ s2 + "] " + webapp.getAttribute(s2));
//					}
//				}
//*/
//			}
//
//
//			//IBMServletContext a = (IBMServletContext) getServletContext();
//			//System.out.println("## a.getServerInfo: " + a.getServerInfo());
///*
//			try {
//				Class c = Class.forName("com.ibm.db2.jcc.DB2PoolMonitor");
//				Method[] meth = c.getDeclaredMethods();
//				for (int i = 0; i< meth.length;i++)
//					System.out.println("## meth:" + meth[i].getName());
//			} catch (ClassNotFoundException ex) {
//			}
//*/
//			request.setAttribute("infoServer", map);
//			request.setAttribute("infoWebGroup", mapWebGroup);
///*
//			DB2PoolMonitor topm = DB2PoolMonitor.getPoolMonitor (DB2PoolMonitor.TRANSPORT_OBJECT);
//			PoolMonitor poolMonitor = new PoolMonitor();
//			poolMonitor.setMonitorVersion(topm.getMonitorVersion());
//			poolMonitor.setAgedOutObjectCount(topm.agedOutObjectCount());
//			poolMonitor.setCreatedObjectCount(topm.createdObjectCount());
//			poolMonitor.setHeavyWeightReusedObjectCount(topm.heavyWeightReusedObjectCount());
//			poolMonitor.setLightWeightReusedObjectCount(topm.lightWeightReusedObjectCount());
//			//poolMonitor.setLongestBlockedRequestTime(topm.longestBlockedRequestTime());
//			//poolMonitor.setNumberOfConnectionReleaseRefused(topm.numberOfConnectionReleaseRefused());
//			poolMonitor.setNumberOfRequestsBlocked(topm.numberOfRequestsBlocked());
//			//poolMonitor.setShortestBlockedRequestTime(topm.shortestBlockedRequestTime());
//			poolMonitor.setSuccessfullRequestsFromPool(topm.successfullRequestsFromPool());
//			poolMonitor.setTotalPoolObjects(topm.totalPoolObjects());
//			poolMonitor.setTotalRequestsToPool(topm.totalRequestsToPool());
//			poolMonitor.setTotalTimeBlocked(topm.totalTimeBlocked());
//*/
//			this.getServletContext().getRequestDispatcher(
//				"/info/RichInfo.jsp").forward(
//				request,
//				response);
//			return;
//		} catch (Exception ex) {
//			request.setAttribute("javax.servlet.jsp.jspException", ex);
//			this.getServletContext().getRequestDispatcher(
//				"/info/RichInfo.jsp").forward(
//				request,
//				response);
//			return;
//		}

	}

}
