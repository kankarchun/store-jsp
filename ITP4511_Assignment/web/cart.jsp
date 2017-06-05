<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.CartBean"%>
<%@page import="ict.beans.CartBean"%>
<%@page import="ict.beans.ItemBean"%>
<%@page import="java.util.ArrayList"%>
<jsp:include page="common/header.jsp" />

<%

    CartBean cart = (CartBean) request.getAttribute("cart");
    String msg = (String) request.getAttribute("msg");
    if (cart == null) {
        response.sendRedirect(request.getContextPath() + "/cart");
        return;
    } else if (msg != null) {
%>
<b>Cart</b>
<p><%=msg%></p>
<p><a href="search">Click here</a> to continue shopping</p>
<%
} else if (cart.isEmpty()) {
%>
<b>Cart are empty</b>
<p><a href="search">Click here</a> to continue shopping</p>
<%
} else {
%>
<b>Cart</b>
<table>
    <tbody>
        <tr>
            <th></th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Subtotal</th>
            <th></th>
        </tr>
        <% for (ItemBean ib : cart.getItems()) {
        %>
        <tr>
            <td><img src="<%=ib.getImage()%>" height="100" alt="<%=ib.getItemID()%>"/></td>
            <td><%=ib.getName()%></td>
            <td>HKD$<%=ib.getPrice()%></td>
            <td>
                <input type="number" value="<%=cart.getQuality(ib)%>"/>
                <%--<select>
                    <%
                        for (int i = 1; i <= 10; i++) {
                    %>
                    <option value="<%=i%>" <%=(cart.getQuality(ib) == i) ? "selected='selected'" : ""%>><%=i%></option>
                    <%
                        }
                    %>
                </select>--%>
            </td>
            <td>HKD$<%=ib.getPrice() * cart.getQuality(ib)%></td>
            <td><a href="cart?action=remove&id=<%=ib.getItemID()%>"><i class="fa fa-times" aria-hidden="true"></i></a></td>
        </tr>
        <%
            }%>
    </tbody>
</table>
<div id="coupon">
    <b>Coupon Code</b>
    <input type="text" />
    <button>Redeem</button>
</div>
<div id="checkout">
    <table>
        <tbody>
            <tr>
                <td>Subtotal</td>
                <td>HKD$<%=cart.getSubtotal()%></td>
            </tr>
            <tr>
                <td>Freight (Courier - Fixed)</td>
                <td>HKD$0</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <td>Total</td>
                <td>HKD$<%=cart.getSubtotal()%></td>
            </tr>
        </tfoot>
    </table>
    <button onclick="location = 'search'">Continue Shopping</button>
    <button onclick="location = 'checkout'">Check Out</button>
</div>
<%
    }
%>

<link rel="stylesheet" type="text/css" href="css/cart.css">
<script src="js/cart.js" defer></script>
<jsp:include page="common/footer.jsp" />