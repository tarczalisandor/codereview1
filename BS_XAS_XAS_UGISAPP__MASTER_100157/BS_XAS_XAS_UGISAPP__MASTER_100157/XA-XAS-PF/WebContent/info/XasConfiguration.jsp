<?xml version="1.0" encoding="UTF-8" ?><%@ page import="it.usi.xframe.xas.bfutil.XasServiceFactory		" %><%  response.setContentType("text/xml");  response.setHeader("Cache-Control", "no-Cache");  response.setHeader("Pragma", "No-cache");  response.setDateHeader("Expires", 0);  try {    String configString = XasServiceFactory.getInstance().getXasSendsmsServiceFacade().getConfiguration("file,xml").replaceFirst("<\\?.*\\?>","");	 out.print(configString); 	 } catch( Exception e ) {%>	<%@ page import="java.io.PrintWriter" %>	<result value="KO" message="<%= e.getMessage() %>"><%    out.println();    PrintWriter pw = new PrintWriter(out);    e.printStackTrace(pw);    pw.flush();%>	</result><% } %>	