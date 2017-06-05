<%@page import="java.util.ArrayList"%>
<%@page import="ict.beans.*"%>
<%@page import="ict.db.Database"%>
<%@taglib prefix="ict" uri="../WEB-INF/tlds/icon.tld"%>
<%
    //db
    String dbUser = getServletContext().getInitParameter("dbUser");
    String dbPassword = getServletContext().getInitParameter("dbPassword");
    String dbUrl = getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
    
    char type;
                String id;
                if ((id = (String) session.getAttribute("client")) != null) {
                    type = 'c';
                } else if ((id = (String) session.getAttribute("manager")) != null) {
                    type = 'm';
                } else {
                    type = 'b';
                }
%>
<!DOCTYPE html>
<html lang="zh-Hant-hk">

    <head>
        <meta charset="utf-8">
        <title>C&F Dress</title>
        <meta name="author" content="j1Lib">
    </head>

    <body>
        <div id="header">
            ITP4511 Enterprise Systems Development
        </div>        
        <div id="logo">
            C&F Dresss

            <%                //type and user id
                

                //menu hyperlink and get username
                String[][] link;//{"Title","url"}                
                String username = "";

                if (type == 'c') {
                    link = new String[][]{
                        {"Personal Information", "information.jsp"},
                        {"History", "historyDetail"},
                        {"Bonus", "bonus"},
                        {"Logout", "login?action=logout"}
                    };
                    username = db.queryCustomerByID(id).getName();
                } else if (type == 'm') {
                    link = new String[][]{
                        {"item", "manageItem.jsp"},
                        {"Order status", "manageOrderStatus.jsp"},
                        {"Gift status", "manageRedeemStatus.jsp"},
                        {"confirm account", "handleRequest"},
                        {"approve credit", "handleCredit"},
                        {"report", "handleReport"},
                        {"Logout", "login?action=logout"}
                    };
                    username = db.queryManagerByID(id).getName();
                } else {
                    link = new String[][]{};
                }
            %>


            <div id="icon">
                <% if (type == 'c') {%>
                <a href="<%=request.getContextPath()%>/cart"><ict:icon name="shopping-cart"/>Cart</a>
                <%} else if (type == 'b') {%>
                <a href="<%=request.getContextPath()%>/register.jsp"><ict:icon name="user-plus"/>Register</a>
                <%}%>
            </div>
            <div id="member">
                <% if (type == 'b') {%>
                <a href="<%=request.getContextPath()%>/login"><ict:icon name="user-o"/>Login</a>
                <%} else {%>
                <a><ict:icon name="diamond"/>Account</a>
                <%}%>
            </div>
            <div id="menus">
                <% if (type != 'b') {%>
                <a><%=username%></a>
                <%}%>
                <%
                    for (int i = 0; i < link.length; i++) {
                        out.print("<a href=\"" + link[i][1] + "\">" + link[i][0] + "</a>");
                    }
                %>
            </div>
            <div id="notification">
                <%
                    if(type == 'c'){
                        ArrayList<OrdersBean> ob = db.queryOrders((String)session.getAttribute("client"));
                        for(int i=0;i<ob.size();i++){
                            OrdersBean order = ob.get(i);
                            if(order.getStatus().equals(OrdersBean.STATUS_AVAILABLE_PICK_UP)){
                %>
                <a href="historyDetail?order=<%=order.getOrderID()%>"><ict:icon name="exclamation-circle"/> Your order is available! Click me to check the detail!</a>
                <%                                
                break;
            }
        }    }
                %>
            </div>

        </div>
        <div id="hots">
            <div>Trends:</div>
            <%
                int i = 0;
                for (ItemBean cb : db.queryAllItem()) {
            %>
            <div onclick="location = 'search?q=<%=cb.getName()%>'"><%=cb.getName()%></div>
            <%
                    i++;
                    if (i == 6) {
                        break;
                    }
                }
            %>
        </div>
        <div id="menu">
            <div id="category">
                <%
                    i = 0;
                    for (CategoryBean cb : db.queryAllCategory()) {
                %>
                <div onclick="location = 'search?action=sortByCategory&category=<%=cb.getCategoryID()%>'"><%=cb.getCategory()%></div>
                <%
                        i++;
                        if (i == 8) {
                            break;
                        }
                    }
                %>
                <div><i class="fa fa-search" aria-hidden="true"></i> Search</div>
            </div>
            <div id="search">
                <input type="text" placeholder="Search" />
            </div>
        </div>
        <div id="container">
