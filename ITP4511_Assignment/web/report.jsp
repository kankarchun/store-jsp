<%@page errorPage="error.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.db.Database"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.beans.ReportBean"%>
<%
    ArrayList<ReportBean> reports = (ArrayList<ReportBean>) request.getAttribute("reports");
    int test = Integer.parseInt(request.getAttribute("test").toString());
%>
<jsp:include page="common/header.jsp" />
<p class="title">Last 10 months Incomplete Order Report</p>
<div list>
    <%
        for (int i = 0; i < 10; i++) {
            out.print("<div class='item' onclick=\"controlShow(this)\">");
            out.print("<p class='head'>" + reports.get(i).getMonth() + "/" + reports.get(i).getYear() + "</p>");
            out.print("<div class='hide'>");
            out.print("<p>Number of incomplete Order: " + reports.get(i).getIncomplete() + "</p>");
            out.print("<p>Total of Order: " + reports.get(i).getTotalOrder() + "</p>");
            out.print("<p>Percentage of incomplete order: " + ((reports.get(i).getTotalOrder() == 0) ? 0 : ((double) reports.get(i).getIncomplete() / reports.get(i).getTotalOrder()) * 100) + "%</p>");
            out.print("<p>Incomplete Order List: </p>");
            out.print("<table>");
            out.print("<tr><th>OrderID</th><th>ClientID</th><th>Amount</th><th>OrderDate</th><th>Type</th></tr>");
            if (reports.get(i).getIncomplete() != 0) {
                for (int j = 0; j < reports.get(i).getIncomplete(); j++) {
                    out.print("<tr><td>" + reports.get(i).getContent().get(j).getOrderID() + "</td>"
                            + "<td>" + reports.get(i).getContent().get(j).getClientID() + "</td>"
                            + "<td>" + reports.get(i).getContent().get(j).getAmount() + "</td>"
                            + "<td>" + reports.get(i).getContent().get(j).getOrderDateTime() + "</td>"
                            + "<td>" + reports.get(i).getContent().get(j).getType() + "</td></tr>");
                }
                out.print("</table>");
            } else {
                out.print("</table>");
                out.print("<p style='text-align:center;'>No Result</p>");
            }
            out.print("</div>");
            out.print("</div>");
        }
    %>
</div>

<script defer>
    function controlShow(div) {
        if (div.childNodes[1].className === "hide") {
            div.childNodes[1].className = "show";
        } else
            div.childNodes[1].className = "hide";
    }

</script>
<link rel="stylesheet" type="text/css" href="css/report.css"/>
<jsp:include page="common/footer.jsp" />