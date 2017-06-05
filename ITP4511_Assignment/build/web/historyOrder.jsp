<%@page errorPage="error.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="ict.beans.OrdersBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.db.Database"%>
<%
    String id = (String) session.getAttribute("client");
    if (id == null) {
        response.sendRedirect("login.jsp");
    }

    String dbUser = getServletContext().getInitParameter("dbUser");
    String dbPassword = getServletContext().getInitParameter("dbPassword");
    String dbUrl = getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);

    ArrayList<OrdersBean> orders = db.queryOrders(id);
    for (int i =0; i <orders.size(); i++) {
        OrdersBean order = orders.get(i);
        out.print("<div class=\"order\" onclick=\"location.href='historyDetail?order=" + order.getOrderID() + "';\">");
        out.print(order.getOrderID() + "<br/>");
        Date date = new Date(order.getOrderDateTime().getTime());
        String dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
        out.print(dt + " ");
        String status = order.getStatus();
        if (status.equals(OrdersBean.STATUS_CANCELED)) {
            out.print("<font color=\"red\">" + status + "</font>");
        } else if (status.equals(OrdersBean.STATUS_DELIVERED) || status.equals(OrdersBean.STATUS_PICKED_UP) || status.equals(OrdersBean.STATUS_DELAY_PICKED_UP)) {
            out.print("<font color=\"green\">" + status + "</font>");
        } else if (status.equals(OrdersBean.STATUS_AVAILABLE_PICK_UP)) {
            out.print("<font color=\"orange\">" + status + "</font>");
        } else {
            out.print(status);
        }
        out.print("</div>");
    }
%>

