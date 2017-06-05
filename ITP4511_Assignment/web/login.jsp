<%@page errorPage="error.jsp" %>
<jsp:include page="common/header.jsp" />
<div id="msg">
    <%
        if (request.getAttribute("fail") != null) {
            out.print("Incorrect username or password! Please input again");
        }
    %>
</div>
<div class="login_box">
    <b><h2>Login</h2></b>
    <form method="post" action="login">
        <div id="username">
            <label for="un">Username:</label>
            <input type="text" name="username" id="un" placeholder="username"/>
        </div> 
        <div id="password">
            <label for="pw">Password:</label>
            <input type="password" name="password" id="pw" placeholder="password"/>
        </div> 
        <input type="hidden" id="action" name="action" value="authenticate"/>
        <input type="submit" value="Login"/>       
        <!--<table>
            <tr><td><label for="un">Username:</label></td><td><input type="text" name="username" id="un" placeholder="username"/></td></tr>
            <tr><td><label for="pw">Password:</label></td><td><input type="password" name="password" id="pw" placeholder="password"/></td></tr>
            <label for="action"><input type="hidden" id="action" name="action" value="authenticate"/>
                <tr><td colspan="2"><input type="submit" value="Login"/></td></tr>
        </table>-->
    </form>
    <b><h2><a href="register.jsp">Register</a> | Forgot Password?</h2></b>
</div>
<link rel="stylesheet" type="text/css" href="css/login.css"/>
<jsp:include page="common/footer.jsp" />
