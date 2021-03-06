<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.RedemptionBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.Format"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.db.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css"/>
<link rel="stylesheet" href="css/bootstrap-theme.min.css"/>
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/list.css">
<style>
    #pickupDateTime{
        display:none;
    }
    input[type=date]::-webkit-inner-spin-button, 
    input[type=date]::-webkit-outer-spin-button { 
        -webkit-appearance: none;
        -moz-appearance: none;
        appearance: none;
        margin: 0; 
    }
</style>
<%
    String dbUser = this.getServletContext().getInitParameter("dbUser");
    String dbPassword = this.getServletContext().getInitParameter("dbPassword");
    String dbUrl = this.getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
%>
<div class="content">
    <div class="container">
        <form method="get" action="editRedeemStatus">
            <input type="hidden" name="action" value="search" />
            <div class="form-group row">
                <label for="inputName" class="col-sm-2 col-form-label">Client ID</label>
                <div class="col-sm-7">
                    <input type="text" class="form-control" name="clientID" placeholder="Search...">
                </div>
                <button type="submit" class="btn btn-info">Search</button>
            </div>
        </form>
    </div>
    <table id="table" class="table table-striped">
        <thead>
            <tr>
                <th>Redeem ID</th>
                <th>Client ID</th>
                <th>Client Name</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Address</th>
                <th>Redeemed Date Time</th>
                <th>Delivery/Pickup Date</th>
                <th>Status</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <%
                ArrayList<RedemptionBean> orders;
                if (request.getAttribute("redeems") == null) {
                    orders = db.queryRedeemAvailable();
                } else {
                    orders = (ArrayList<RedemptionBean>) request.getAttribute("redeems");
                }
                for (RedemptionBean ob : orders) {
                    String pickup;
                    if (ob.getPickupDateTime() == null) {
                        pickup = "";
                    } else {
                        pickup = "" + ob.getPickupDateTime();
                    }
                    String address;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateInString = "" + ob.getRedeemedDateTime();
                    Date date = formatter.parse(dateInString);
                    String redeemDate = formatter.format(date);
                    if (ob.getAddress() == null) {
                        address = "";
                    } else {
                        address = "" + ob.getAddress();
                    }
                    out.print("<tr>");
                    out.print("<th scope='row'>" + ob.getRedemptionID() + "</th>");
                    out.print("<td>" + ob.getClientID() + "</td>");
                    out.print("<td>" + ob.getClient().getName() + "</td>");
                    out.print("<td>" + ob.getType() + "</td>");
                    out.print("<td>" + ob.getBonus() + "</td>");
                    out.print("<td>" + address + "</td>");
                    out.print("<td>" + redeemDate + "</td>");
                    out.print("<td>" + pickup + "</td>");
                    out.print("<td>" + ob.getStatus() + "</td>");
                    out.println("<td><a class='btn btn-default' href=\"editRedeemStatus?action=edit&id=" + ob.getRedemptionID() + "\">edit</a></td>");
                    out.print("</tr>");
                }
            %>
        </tbody>
    </table>
    <%
        RedemptionBean order = new RedemptionBean();
        if (request.getAttribute("redeem") != null) {
            order = (RedemptionBean) request.getAttribute("redeem");
            out.print("<script>$(document).ready(function () {$('#editModal').modal('show');});</script>");
        }
    %>
    <div class="modal fade" id="editModal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Edit Redeem Status</h4>
                </div>
                <div class="modal-body">
                    <form method=“get" action="editRedeemStatus">
                        <input type="hidden" name="action" value="confirm">
                        <input type="hidden" name="redeemID" value="<%=order.getRedemptionID()%>">
                        <div class="form-group row">
                            <label for="inputPassword3" class="col-sm-2 col-form-label">Redeem Status</label>
                            <div class="col-sm-10">
                                <select class="form-control" name="redeemStatus" id="redeemStatus">
                                    <%
                                        String[] status = {"canceled", "delivered", "picked-up", "delay picked-up", "available pick up"};
                                        if (request.getAttribute("redeem") != null) {
                                            for (int i = 0; i < status.length; i++) {
                                                if (order.getType().equals("self-pick up") && status[i].equals("delivered")) {

                                                } else if (order.getType().equals("delivery") && status[i].equals("picked-up")) {

                                                } else if (order.getType().equals("delivery") && status[i].equals("delay picked-up")) {

                                                } else if (order.getType().equals("delivery") && status[i].equals("available pick up")) {

                                                } else {
                                                    if (status.equals(order.getStatus())) {
                                                        out.print("<option value='" + status[i] + "' selected='selected'>" + status[i] + "</option>");
                                                    } else {
                                                        out.print("<option value='" + status[i] + "'>" + status[i] + "</option>");
                                                    }
                                                }
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <%
                            out.print("<script>$(document).ready(function () {"
                                    + "$('#redeemStatus').on('change', function() {"
                                    + "var selected=$('#redeemStatus option:selected').text();"
                                    + "if(selected=='delay picked-up'||selected=='available pick up'||selected=='delivered')"
                                    + "$('#pickupDateTime').css('display','block');"
                                    + "else $('#pickupDateTime').css('display','none');})});"
                                    + "</script>");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String fTime = format.format(date);
                            out.print("<div class=\"form-group row\" id=\"pickupDateTime\">"
                                    + "<label for=\"inputName\" class=\"col-sm-2 col-form-label\">Pickup Date Time</label>"
                                    + "<div class=\"col-sm-10\">"
                                    + "<input type=\"date\" class=\"form-control\" name=\"pickupDateTime\" min=\"" + fTime + "\" value=\"" + fTime + "\" />"
                                    + "</div></div>");
                        %>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-12">
                                <button type="submit" class="btn btn-primary btn-block">Save</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="common/footer.jsp" />
