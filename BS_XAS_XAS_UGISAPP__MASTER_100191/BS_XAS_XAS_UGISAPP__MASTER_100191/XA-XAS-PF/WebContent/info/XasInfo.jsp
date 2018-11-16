<?xml version="1.0" encoding="UTF-8" ?><%@ page contentType="text/xml"
	isErrorPage="true"%><%@ page
	import="it.usi.xframe.system.bfinfo.BaselineInfo"%><%@ page
	import="it.usi.xframe.system.bfinfo.BaselineInfoFactory"%>
<%
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
	response.setDateHeader("Expires", 0);
	response.setHeader("Access-Control-Allow-Origin", "*" ); 

	try
	{
		String wsAppHost = (String) session.getServletContext().getAttribute(
				"com.ibm.websphere.servlet.application.host"); //String wsAppClasspath = (String) session.getServletContext().getAttribute("com.ibm.websphere.servlet.application.classpath");    //for (java.util.Enumeration en = session.getServletContext().getAttributeNames(); en.hasMoreElements();)    	//System.out.println("## SPAZZ en -> " + (String) en.nextElement());    BaselineInfo baselineInfo = BaselineInfoFactory.getInstance().getInfo(request.getParameter("applid"));
%><info>
<server name="<%out.print(request.getServerName());%>"
	port="<%out.print(request.getServerPort());%>"
	info="<%out.print(request.getSession().getServletContext().getServerInfo());%>" />
<websphere applicationHost="<%out.print(wsAppHost);%>" /> <xas
<%BaselineInfo baselineInfo = BaselineInfoFactory.getInstance().getInfo("xas");%>
	model="<%out.print(baselineInfo.getModel());%>"
	target="<%out.print(baselineInfo.getTarget());%>"
	version="<%out.print(baselineInfo.getVersion());%>"
	date="<%out.print(baselineInfo.getDate());%>" /></info>
<%
	} catch (Exception e)
	{
%>
<%@ page import="java.io.PrintWriter"%>
<result value="KO" message="<%=e.getMessage()%>">
<%
	out.println();
		PrintWriter pw = new PrintWriter(out);
		e.printStackTrace(pw);
		pw.flush();
%>
</result>
<% } %>
