<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page isErrorPage="true" %> 
<jsp:include page="common/header.jsp" />



<% if (exception != null) {%>

<p><b>Message:</b> 
You have no permission to take this action.</p>

<p><b>StackTrace:</b><br> 
<%
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    exception.printStackTrace(printWriter);
    out.println(stringWriter);
    printWriter.close();
    stringWriter.close();
%></p>

<%} else {%>

<p><b>Message:</b> 404</p>

<p><b>StackTrace:</b><br> Page not found.</p>


<%}%>
<jsp:include page="common/footer.jsp" />