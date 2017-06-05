<%@page errorPage="error.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<jsp:include page="bonus.jsp" />
<div class="bonus_main_container">
    <center>
        <div id="error_msg">
            <%=request.getAttribute("error_msg")%>
            <br/>
            <input type="button" onclick="location.href = 'bonus?action=search'" value="OK"/>
        </div>
    </center>
</div>
<jsp:include page="common/footer.jsp" />
