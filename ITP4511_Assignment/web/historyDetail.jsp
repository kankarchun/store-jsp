<%@page errorPage="error.jsp" %>
<%@page import="ict.util.CalBonus"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="ict.beans.CategoryBean"%>
<%@page import="ict.db.Database"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="ict.beans.OrderItemBean"%>
<%@page import="ict.beans.OrdersBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    OrdersBean order = (OrdersBean) request.getAttribute("order");
    String dbUser = getServletContext().getInitParameter("dbUser");
    String dbPassword = getServletContext().getInitParameter("dbPassword");
    String dbUrl = getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
%>
<jsp:include page="common/header.jsp" />
<script>
    function confirmCancel(id) {
        if (confirm('You need to pay HKD$ 500 for the handling fees.\nAre you sure you want to cancel this order?')) {
            location.href = 'CancelOrder?oid=' + id;
        } else {

        }
    }
</script>
<div class="history_container">
    <div class="orders_container">
        <jsp:include page="historyOrder.jsp"/>
    </div>
    <div class="details_container">
        <center>
            <%
                if (order != null) {
                    out.print("<div class=\"detail\">");
                    if (order.getAmount() > 10000 && (order.getStatus().equals(OrdersBean.STATUS_WAITING)||order.getStatus().equals(OrdersBean.STATUS_AVAILABLE_PICK_UP))) {
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        Date nowdate = new Date(now.getTime());
                        Date orderdate = new Date(order.getOrderDateTime().getTime());
                        long diff = nowdate.getTime() - orderdate.getTime();
                        if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < 1) {
                            out.print("<button style=\"float:right\" onclick=\"confirmCancel('" + order.getOrderID() + "')\">Cancel Order</button>");
                        }
                    }
                    out.print("<h1>" + order.getOrderID() + "</h1>");
                    Date date = new Date(order.getOrderDateTime().getTime());
                    String dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
                    out.print("<table><tr><td>Order Date & Time:</td><td>" + dt + "</td></tr>");
                    out.print("<tr><td>Order Type:</td><td>" + order.getType() + "</td></tr>");
                    if (order.getType().equals(OrdersBean.TYPE_DELIVERY)) {
                        out.print("<tr><td>Delivery Address:</td><td>" + order.getAddress() + "</td></tr>");
                        if (order.getStatus().equals(OrdersBean.STATUS_WAITING)) {
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                        Date ddd = new Date();
                                        String fTime = format2.format(ddd);
                            out.print("<tr><td>Delivery Date:</td><td><form method=\"post\" action=\"EditDeliveryDate\"><input type=\"date\" name=\"ddate\" value=\"" + order.getPickupDateTime() + "\" min=\""+fTime+"\" />");
                            out.print("<input type=\"submit\" value=\"Edit\" /><input type=\"hidden\" name=\"oid\" value=\""+order.getOrderID()+"\"/></form></td></tr>");
                        }else{
                            out.print("<tr><td>Delivery Date:</td><td>" + order.getPickupDateTime() + "</td></tr>");
                        }
                    }
                    out.print("<tr><td>Order Status:</td><td>");
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
                    out.print("</td></tr>");
                    if (status.equals(OrdersBean.STATUS_DELAY_PICKED_UP)) {
                        String d = new SimpleDateFormat("yyyy/MM/dd").format(order.getPickupDateTime());
                        out.print("<tr><td>Delay picked-up date:</td><td>" + d + "</td></tr>");
                    } else if (status.equals(OrdersBean.STATUS_AVAILABLE_PICK_UP)) {
                        String d = new SimpleDateFormat("yyyy/MM/dd").format(order.getPickupDateTime());
                        out.print("<tr><td>Available pick-up date:</td><td>" + d + "</td></tr>");
                    }
                    out.print("<tr><td>Amount:</td><td>$" + order.getAmount() + "</td></tr>");
                    out.print("<tr><td>Bonus gained:</td><td>$" + CalBonus.calBonus(order.getAmount()) + "</td></tr>");
                    out.print("</table>");
                    out.print("<div class=\"items_container\">");
                    for (int i = 0; i < order.getItem().size(); i++) {
                        OrderItemBean item = order.getItem().get(i);
                        out.print("<div class=\"item\">");
                        out.print("<div class=\"item_head\"><span class=\"item_name\">" + item.getName() + "</span>");
                        out.print("<span class=\"item_qty\">Quantity: " + item.getQuantity() + "</span>");
                        out.print("<span class=\"item_price\">Total Price: $" + (item.getPrice() * item.getQuantity()) + "</span>");
                        out.print("</div><div class=\"item_detail\">");
                        out.print("<img src=\"" + item.getImage() + "\" width=\"100\" height=\"100\"/>");
                        out.print("<div class=\"item_detail_content\">");
                        out.print("Designer: " + item.getDesigner() + "<br/>");
                        out.print(item.getDescription());
                        out.print("</div></div>");
                        out.print("</div>");
                    }
                    out.print("</div></div>");
                } else {
                    out.print("<div id=\"msg\">Please Select an Order</div>");
                }
            %>
        </center>
    </div>
</div>
<link rel="stylesheet" type="text/css" href="css/history.css">
<jsp:include page="common/footer.jsp" />