<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.*"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="ict" uri="/WEB-INF/tlds/icon.tld"%>
<jsp:include page="common/header.jsp" />

<%
    ArrayList<ItemBean> result = (ArrayList<ItemBean>) request.getAttribute("result");
    ArrayList<CategoryBean> category = (ArrayList<CategoryBean>) request.getAttribute("category");
    if (result == null || category ==null) {
        response.sendRedirect(request.getContextPath() + "/search");
        return;
    }    
%>


<div id="fliter">
    <div>
        <div>
            Category
            <ict:icon name="minus"/>
        </div>
        <div>
            <%
                for (CategoryBean cb : category){
            %>
            <p onclick="location = 'search?action=sortByCategory&category=<%=cb.getCategoryID() %>'"><ict:icon name="square-o"/> <%=cb.getCategory() %></p>
            <%
                }
            %>
        </div>
    </div> 
    <div>
        <div>
            Fliter
            <ict:icon name="minus"/>
        </div>
        <div>
            <p>ID: <input type="text" oninput='fliterBy(fliters.fliterByID,this.value)'/></p>
            <p>Category Name:  <input type="text" oninput='fliterBy(fliters.fliterByCategory,this.value)'/></p>
            <p>Item Name:  <input type="text" oninput='fliterBy(fliters.fliterByName,this.value)'/></p>
            <p>Designer:  <input type="text" oninput='fliterBy(fliters.fliterByDesigner,this.value)'/></p>
            <p>Description:  <input type="text" oninput='fliterBy(fliters.fliterByDescription,this.value)'/></p>
            <p>Price less than HK$  <input type="number" oninput='priceLess(this.value)'/></p>
            <p>Quantity more than:  <input type="number" oninput='quatityLarge(this.value)' /></p>
        </div>
    </div>

    <div>
        <div>
            Sorting
            <ict:icon name="minus"/>
        </div>
        <div>
            <p onclick="sortByPrice()"><ict:icon name="sort-amount-asc"/> Price (low to high)</p>
            <p onclick="sortByPrice(1)"><ict:icon name="sort-amount-desc"/> Price (high to low)</p>
            <p onclick="sortByName()"><ict:icon name="sort-alpha-asc"/> Name (a to z)</p>
            <p onclick="sortByName(1)"><ict:icon name="sort-alpha-desc"/> Name (z to a)</p>
        </div>
    </div>   
</div>
<div id="result">    

    <%
    if (result.isEmpty()) {
    %>   

    <b>Search results for '<%= request.getParameter("q")%>':</b>
    <p>Sorry, can not find the goods you want.</p>

    <%
    } else {
    %> 

    <% 
        if(request.getParameter("q")!=null){
        
    %>
    <b>Search results for '<%= request.getParameter("q")%>':</b>
    <%
}else{ %>
    <b>Search results for category '<%= request.getParameter("category")%>':</b>
    <%}
    %>


    <%
        for (ItemBean ib : result) {            
    %>
    <div>
        <div>NEW</div>
        <img width="100%" src="<%=ib.getImage()%>" />
        <div class="color"></div>
        <div class="name"><%=ib.getName()%></div>
        <div class="price">HK$<%=ib.getPrice()%></div>
        <button onclick="location = 'cart?action=add&id=<%=ib.getItemID()%>'">Add To Cart</button>
        <div class="tooltips">
            <p>ID:<%=ib.getItemID()%></p>
            <p>Category Name:<%=ib.getCategory().getCategory()%></p>
            <p>Item Name:<%=ib.getName()%></p>
            <p>Designer:<%=ib.getDesigner()%></p>
            <p>Description:<%=ib.getDescription()%></p>
            <p>Price:HK$<%=ib.getPrice()%></p>
            <p>Quantity:<%=ib.getQuantity()%></p>
        </div>
    </div>    
    <%
        }
    %>

    <%
        }
    %>


</div>

<link rel="stylesheet" type="text/css" href="css/search.css">
<script src="js/search.js" defer></script>
<jsp:include page="common/footer.jsp" />