<%@page errorPage="error.jsp" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="ict.beans.ClientBean"%>
<%@page import="ict.db.Database"%>
<%@page import="ict.beans.ItemBean"%>
<%@page import="ict.beans.CartBean"%>
<%@taglib prefix="ict" uri="/WEB-INF/tlds/icon.tld"%>
<jsp:include page="common/header.jsp" />
<%
    String dbUser = getServletContext().getInitParameter("dbUser");
    String dbPassword = getServletContext().getInitParameter("dbPassword");
    String dbUrl = getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
    ClientBean client = db.queryCustomerByID((String) session.getAttribute("client"));

    CartBean cart = (CartBean) request.getAttribute("cart");
    String msg = (String) request.getAttribute("msg");
    if (cart == null) {
        response.sendRedirect(request.getContextPath() + "/checkout");
        return;
    } else if (cart.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/cart");
        return;
    } else if (msg != null) {
%>
<b>Checkout</b>
<p><%=msg%></p>
<%
} else {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String fTime = format.format(date);
%>

<b>Checkout</b>
<div id="payment">

    <div class="title"><input type="radio" name="method" id="delivery"/> Delivery</div>    
    <div>
        <p>Delivery Date <input id="deliverydateime" type="date" min="<%=fTime%>"/></p>
        <p>Delivery Address <input id="deliveryaddress" type="text" value="<%=client.getAddress()%>"/></p>
    </div>
    <div class="title"><input type="radio" name="method" id="selfpickup" checked/> Self-pick up</div>
    <div>
        <p>
    </div>
    <button>Confrim Order</button>
</div>
<div id="preview">
    <table>
        <tbody>
            <tr>
                <th>Product Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
            </tr>
            <% for (ItemBean ib : cart.getItems()) {
            %>
            <tr>                
                <td><%=ib.getName()%></td>
                <td>HKD$<%=ib.getPrice()%></td>
                <td>                
                    <%=cart.getQuality(ib)%>
                </td>
                <td>HKD$<%=ib.getPrice() * cart.getQuality(ib)%></td>
            </tr>
            <%
                }%>
            <tr>
                <td>Subtotal</td>
                <td></td>
                <td></td>
                <td>HKD$<%=cart.getSubtotal()%></td>
            </tr>
            <tr>
                <td>Freight (Courier - Fixed)</td>
                <td></td>
                <td></td>
                <td>HKD$0</td>
            </tr>
            <tr>
                <td>Total</td>
                <td></td>
                <td></td>
                <td>HKD$<%=cart.getSubtotal()%></td>
            </tr>
        </tbody>
    </table>
</div>
<%}%>
<link rel="stylesheet" type="text/css" href="css/checkout.css">
<script src="js/checkout.js" defer></script>
<jsp:include page="common/footer.jsp" />