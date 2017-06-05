<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.OrdersBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="ict.beans.RedemptionBean"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<jsp:include page="bonus.jsp" />
<div class="bonus_main_container"><center>
        <%
            ArrayList<RedemptionBean> records = (ArrayList<RedemptionBean>) request.getAttribute("records");
            if (records != null) {
                for (int i = 0; i < records.size(); i++) {
                    RedemptionBean redeem = records.get(i);
                    String status = redeem.getStatus();
                    out.println("<div class=\"redemption_item\"><div class=\"item_head\">");
                    Date date = new Date(redeem.getRedeemedDateTime().getTime());
                    String dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
                    out.println(dt);
                    out.println("<span class=\"name\">" + redeem.getName() + "</span>");
                    out.print("<span class=\"qty\">");
                    if (status.equals(RedemptionBean.STATUS_CANCELED)) {
                        out.print("<font color=\"red\">" + status + "</font>");
                    } else if (status.equals(RedemptionBean.STATUS_DELIVERED) || status.equals(RedemptionBean.STATUS_PICKED_UP) || status.equals(RedemptionBean.STATUS_DELAY_PICKED_UP)) {
                        out.print("<font color=\"green\">" + status + "</font>");
                    } else {
                        out.print(status);
                    }
                    out.println("</span>");
                    out.println("<span class=\"point\">Total Points Used: " + redeem.getBonus() * redeem.getQuantity() + "</span>");
                    out.println("</div><div class=\"item_detail\">");
                    out.println("<img src=\"" + redeem.getImage() + "\"/>");
                    out.println("<div class=\"detail\"><table>");
                    out.println("<tr><td>Type:</td><td>" + redeem.getType() + "</td></tr>");
                    if (redeem.getType().equals(RedemptionBean.TYPE_DELIVERY)) {
                        out.println("<tr><td>Delivery Address:</td><td>" + redeem.getAddress() + "</td></tr>");
                    }
                    if (status.equals(RedemptionBean.STATUS_DELAY_PICKED_UP)) {
                        String d = new SimpleDateFormat("yyyy/MM/dd").format(redeem.getPickupDateTime());
                        out.println("<tr><td>Delay picked-up date:</td><td>" + d + "</td></tr>");
                    }
                    out.println("<tr><td>Quantity:</td><td>" + redeem.getQuantity() + "</td></tr>");
                    out.println("<tr><td>Designer:</td><td>" + redeem.getDesigner() + "</td></tr></table>");
                    out.println(redeem.getDescription());

                    out.println("</div></div></div>");
                }
            }
        %>
    </center></div>
<link rel="stylesheet" type="text/css" href="css/bonusHistory.css">
<jsp:include page="common/footer.jsp" />