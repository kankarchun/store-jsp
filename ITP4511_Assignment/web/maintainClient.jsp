<%@page errorPage="error.jsp" %>

<%@page import="ict.beans.ClientBean"%>
<%@page import="ict.db.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String id = (String) session.getAttribute("client");
    if (id == null) {
        response.sendRedirect("login.jsp");
    }
    String dbUser = getServletContext().getInitParameter("dbUser");
    String dbPassword = getServletContext().getInitParameter("dbPassword");
    String dbUrl = getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
    ClientBean client = db.queryCustomerByID(id);
%>
<jsp:include page="common/header.jsp" />
<center>
    <form method="post" action="maintainClient">
        <table>
            <tr>
                <td><label for="name">Name:</label></td>
                <td><input type="text" name="name" id="name" value="<%=client.getName()%>" /></td>
            </tr>
            <tr>
                <td><label for="address">Delivery Address:</label></td>
                <td><input type="text" name="address" id="address" value="<%=client.getAddress()%>" /></td>
            </tr>
            <tr>
                <td><label for="tel">Telephone Number:</label></td>
                <td><input type="text" name="tel" id="tel" value="<%=client.getTelephone()%>" /></td>
            </tr>
            <tr>
                <td><label for="email">Email Address:</label></td>
                <td><input type="text" name="email" id="email" value="<%=client.getEmail()%>" /></td>
            </tr>
        </table>
        <input type="submit" value="Save"/>
        <input type="button" onclick="location.href = 'information.jsp';" value="Cancel"/>
    </form>
</center>
<link rel="stylesheet" type="text/css" href="css/maintainClient.css">
<jsp:include page="common/footer.jsp" />
