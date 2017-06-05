<%@page errorPage="error.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.db.Database"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.beans.ClientBean"%>
<%
    ArrayList<ClientBean> nonConfirm = (ArrayList<ClientBean>) request.getAttribute("nonConfirm");
%>
<jsp:include page="common/header.jsp" />
            <table id="registerCustomer">
                <colgroup>
                    <col style="width:8%">
                    <col style="width:8%">
                    <col style="width:8%">
                    <col style="width:10%">
                    <col style="width:16%">
                    <col style="width:13%">
                    <col style="width:12%">
                    <col style="width:7%">
                    <col style="width:18%">
                </colgroup>  
                <tr><th>Client ID</th><th>Name</th><th>Tel</th><th>Email</th><th>Address</th><th>Login ID</th><th>Password</th><th>Bonus</th><th>Action</th></tr>
                        <%
                            if (nonConfirm != null) {
                                for (int i = 0; i < nonConfirm.size(); i++) {
                                    out.print("<form method='post' action='handleRequest'>");
                                    out.print("<tr><td>"+nonConfirm.get(i).getClientID()+"</td>");
                                    out.print("<td>"+nonConfirm.get(i).getName()+"</td>");
                                    out.print("<td>"+nonConfirm.get(i).getTelephone()+"</td>");
                                    out.print("<td>"+nonConfirm.get(i).getEmail()+"</td>");
                                    out.print("<td>"+nonConfirm.get(i).getAddress()+"</td>");
                                    out.print("<td><input type='text' name='loginID'/></td>");
                                    out.print("<td><input type='text' name='password'/></td>");
                                    out.print("<td><input type='number' name='bonus' step='0.1' value='1000'/></td>");
                                    out.print("<input type='hidden' name='clientID' value='"+nonConfirm.get(i).getClientID()+"'/>");
                                    out.print("<td><button type='submit' name='action' value='confirm'>Confirm</button><button type='submit' name='action' value='deny'>Deny</button></td></tr></form>");
                                }
                            }else{
                                out.print("<tr><td colspan='8'>No Result</td></tr>");
                            }
                        %>
            </table>
<link rel="stylesheet" type="text/css" href="css/request.css"/>
<jsp:include page="common/footer.jsp" />