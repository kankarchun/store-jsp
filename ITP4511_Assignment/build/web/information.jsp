<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.ClientBean"%>
<%@page import="ict.db.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
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
<center>
    <span><a href="maintainClient.jsp"><img id="edit" src="img/layout/setting.png" width="50px" height="50px"/></a></span>

    <table>
        <tr><th colspan="2">Personal Information</th></tr>
        <tr>
            <td>Login ID:</td>
            <td><%=client.getLoginID()%></td>
        </tr>
        <tr>
            <td>Name:</td>
            <td><%=client.getName()%></td>
        </tr>
        <tr>
            <td>Delivery Address:</td>
            <td><%=client.getAddress()%></td>
        </tr>
        <tr>
            <td>Telephone Number:</td>
            <td><%=client.getTelephone()%></td>
        </tr>
        <tr>
            <td>Email Address:</td>
            <td><%=client.getEmail()%></td>
        </tr>
    </table>

    <table>
        <tr><th colspan="2">Account</th></tr>
        <tr>
            <td>Credit Amount:</td>
            <td>$<%=client.getAmount()%></td>
        </tr>
        <tr>
            <td>Bonus Point:</td>
            <td><%=client.getBonus()%></td>
        </tr>
    </table>
</center>
<link rel="stylesheet" type="text/css" href="css/information.css">
<jsp:include page="common/footer.jsp" />
