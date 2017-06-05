<%@page errorPage="error.jsp" %>
<link rel="stylesheet" type="text/css" href="css/bonus.css"/>
<div class="bonus_menu_container">
    <a href="bonus?action=search"><div id="gift"></div></a>
    <a href="bonus?action=history"><div id="history"></div></a>
    You have <%=request.getAttribute("bonus")%> bonus points.
</div>
