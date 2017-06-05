<%@page errorPage="error.jsp" %>
<jsp:include page="common/header.jsp" />
<center>
    <div id="error_msg">
        <%=request.getAttribute("error_msg")%>
        <br/>
        <input type="button" onclick="location.href = 'information.jsp'" value="OK"/>
    </div>
</center>
<jsp:include page="common/footer.jsp" />