<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/xml" isErrorPage="true" %>
<%
  response.setContentType("text/xml");
  response.setHeader("Cache-Control", "no-Cache");
  response.setHeader("Pragma", "No-cache");
  response.setDateHeader("Expires", 0);

  
%>
<info>
	
	<server name="<% out.print(request.getServerName()); %>"
		port="<% out.print(request.getServerPort()); %>"
		info="<% out.print(request.getSession().getServletContext().getServerInfo()); %>"
	/>
</info>
<% } catch( Exception e ) { %>
	<%@ page import="java.io.PrintWriter" %>
	<result value="KO" message="<%= e.getMessage() %>">
<%
    out.println();
    PrintWriter pw = new PrintWriter(out);
    e.printStackTrace(pw);
    pw.flush();
%>
	</result>

	