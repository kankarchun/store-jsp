<%@page errorPage="error.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.db.Database"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.beans.ClientBean"%>
<%
    ArrayList<ClientBean> customers = (ArrayList<ClientBean>) request.getAttribute("customers");
    String clientID = "";
    String name = "";
    if (request.getAttribute("clientID") != null) {
        clientID = (String) request.getAttribute("clientID");
    }
    if (request.getAttribute("name") != null) {
        name = (String) request.getAttribute("name");
    }

%>
<jsp:include page="common/header.jsp" />
<div class ="credit_box" id="credit"><form method="post" action="handleCredit">
        <table>
            <tr><td><label for="clientID">Client ID:</label></td><td><input type="text" name="clientID" id="clientID" placeholder="clientID" value="<%=clientID%>"/><button type="submit" name="action" value="query">Query</button></td></tr>
            <tr><td><label for="name">Name:</label></td><td><input type="text" name="name" id="name" value="<%=name%>" readonly/></td></tr>
            <tr><td><label for="credit">Credit:</label></td><td><input type="number" name="credit" id="credit" placeholder="credit" step="0.1" value="100"/></td></tr>
            <input type="hidden" name="action" value="issue"/>
            <tr><td colspan="2"><button class="disabled" type="submit" name="action" value="issue" id="issue" disabled>Issue</button></td></tr>
        </table>
    </form>
</div>
<script defer>

    if (document.getElementById("name").value.length){
        issue.className="";
        document.getElementById("issue").disabled=false;
    }
</script>
<link rel="stylesheet" type="text/css" href="css/credit.css"/>
<jsp:include page="common/footer.jsp" />
