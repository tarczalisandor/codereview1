<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<html:html>
<fmt:setLocale value="${sessionScope['org.apache.struts.action.LOCALE']}"/>
<c:set var="cssLang" scope="request" value="en"/>
<c:if test="${sessionScope['org.apache.struts.action.LOCALE'] != null}">
	<c:set var="cssLang" scope="request" value="${sessionScope['org.apache.struts.action.LOCALE'].language}"/>
</c:if>
<head>
	<link rel="stylesheet" href="/XA-XAS-PF/css/style.css" type="text/css"/>
	<title>Info Server home page</title>
</head>

<body>

<table border="1" cellspacing="2" cellpadding="2" style="border: 1px black thin;">
<THEAD>
  <tr>
    <td colspan="2" align="center"><b>Server</b></td>
  </tr>
  <tr>
    <td>Key</td>
    <td>Value</td>
  </tr>
 </THEAD>
<TBODY>
<c:forEach var="current" items="${requestScope.infoServer}">
  <tr>
    <td>
      <c:out value="${current.key}" />
    </td>
    <td>
      <c:out value="${current.value}" />
    </td>
  </tr>
</c:forEach>
</TBODY>
</table>
<p>&nbsp;</p>
<table border="1" cellspacing="2" cellpadding="2" style="border: 1px black thin;">
<THEAD>
  <tr>
    <td colspan="100%" align="center"><b>WebGroup</b></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>WebApp</td>
    <td>Version</td>
    <td>Status</td>
  </tr>
 </THEAD>
<TBODY>
<c:forEach var="current" items="${requestScope.infoWebGroup}">
  <tr>
    <td>
      <c:out value="${current.key}" escapeXml="false" />
    </td>
    <td>
      <c:out value="${current.value}" escapeXml="false" />
    </td>
  </tr>
</c:forEach>
</TBODY>
</table>

</body>

</html:html>
