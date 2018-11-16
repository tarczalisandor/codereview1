/*
 * Created on Mar 26, 2010
 */
package it.usi.xframe.xas.pfstruts.servlet;

import it.usi.xframe.system.bfinfo.BaselineInfo;
import it.usi.xframe.system.bfinfo.BaselineInfoFactory;
import it.usi.xframe.xas.pfstruts.util.WebContainer61Wrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.websphere.runtime.ServerName;

/**
 * @author EE01995
 */
public class RichInfo61Servlet extends HttpServlet {

	private static Log log = LogFactory.getLog(RichInfo61Servlet.class);

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
	protected void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		try {
			log.debug("Start");
			HashMap map = new HashMap(20);
			WebContainer61Wrapper wrapper = new WebContainer61Wrapper();
			String[] virualHost = wrapper.getVirtualHostNames();
			for (int i = 0; i < virualHost.length; ++i) {
				map.put("virualHost." + i, virualHost[i]);
			}
			log.debug("after WebContainer wrapper call");

			map.put("serverName.FullName", ServerName.getFullName());
			map.put("serverName.Id", ServerName.getServerId());
			map.put("servletContext.serverInfo", getServletContext().getServerInfo());
			map.put("servletContext.serverName", request.getServerName());
			map.put(
				"servletContext.serverPort",
				String.valueOf(request.getServerPort()));
			Hashtable htable = wrapper.getLocalServerProp();
			map.putAll(htable);
			String serverName = (String) htable.get("name");
			map.put("longLocalHost", wrapper.getLongLocalHostName());

			SortedMap mapWebGroup = new TreeMap();
			String prodName = null;
			String version = null;

			String meth = (String) request.getParameter("meth");
			log.debug("meth: " + meth);
			Vector appNames = null;
			if ("2".equals(meth))
				appNames = wrapper.listApplications2(serverName);
			else if ("3".equals(meth))
				appNames = wrapper.listApplications3(serverName);
			else
				appNames = wrapper.listApplications(serverName);

			for (int i = 0; i < appNames.size(); ++i) {
				String appName = (String) appNames.get(i);
				if (appName.startsWith("XA")) {
					if (appName.lastIndexOf("-") > -1) {
						prodName =
							appName.substring(appName.lastIndexOf("-") + 1).toLowerCase();
						BaselineInfo baselineInfo =
							BaselineInfoFactory.getInstance().getInfo(prodName);
						version = baselineInfo.getVersion();
					}
					if (version == null || "".equals(version))
						version = "unknown";
					mapWebGroup.put(
						appName,
						appName + "</td><td>" + version + "</td><td>UNKNOWN");
					//mapWebGroup.put("test1", "APP</td><td>100</td><td>UNKNOWN");
				}
			}
			request.setAttribute("infoServer", map);
			request.setAttribute("infoWebGroup", mapWebGroup);
			this.getServletContext().getRequestDispatcher(
				"/info/RichInfo.jsp").forward(
				request,
				response);
			return;
		} catch (Exception ex) {
			log.error(ex);
			request.setAttribute("javax.servlet.jsp.jspException", ex);
			this.getServletContext().getRequestDispatcher(
				"/info/RichInfo.jsp").forward(
				request,
				response);
			return;
		}

	}

}
