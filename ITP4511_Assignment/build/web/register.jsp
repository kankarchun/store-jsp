<%@page errorPage="error.jsp" %>
<jsp:include page="common/header.jsp" />
<div id="msg">
    <%
        if (request.getAttribute("fail") != null) {
            out.print("Something is wrong! Please input again");
        }
    %>
</div>
<div class="register_box" id="register">
    <b><h2>Register</h2></b>
    <form method="post" action="register">
        <div id="name">
            <label for="name">Name:</label>
            <input type="text" name="name" id="name" placeholder="name"/>
        </div> 
        <div id="telephone">
            <label for="tel">Telephone:</label>
            <input type="text" name="tel" id="tel" placeholder="telephone"/>
        </div> 
        <div id="email">
            <label for="tel">Email:</label>
            <input type="text" name="email" id="email" placeholder="email"/>
        </div> 
        <div id="address">
            <label for="tel">Address:</label>
            <textarea rows="4" cols="50" name="add" id="add" placeholder="address"></textarea>
        </div> 
        <input type="submit" value="Register"/>
        <!--<table>
            <tr><td><label for="name">Name:</label></td><td><input type="text" name="name" id="name" placeholder="name"/></td></tr>
            <tr><td><label for="tel">Telephone:</label></td><td><input type="text" name="tel" id="tel" placeholder="telephone"/></td></tr>
            <tr><td><label for="email">Email:</label></td><td><input type="text" name="email" id="email" placeholder="email"/></td></tr>
            <tr><td><label for="add">Address</label></td></tr><tr><td colspan="2"><textarea rows="4" cols="50" name="add" id="add"></textarea></td></tr>                    
            <tr><td colspan="2"><input type="submit" value="Register"/></td></tr>
        </table>-->
    </form>
</div>

<link rel="stylesheet" type="text/css" href="css/register.css"/>
<jsp:include page="common/footer.jsp" />