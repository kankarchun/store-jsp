<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.RedemptionBean"%>
<%@page import="ict.beans.AccessoryBean"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ArrayList<AccessoryBean> gifts = (ArrayList<AccessoryBean>) request.getAttribute("gifts");
%>
<jsp:include page="common/header.jsp" />
<jsp:include page="bonus.jsp" />
<div class="bonus_main_container">
    <div class="searching_container">
        <%
            out.println("<center><table><tr>");
            out.println("<td><label for=\"name\">Name: </label></td>");
            out.print("<td><input type=\"text\" id=\"name\"");
            String name = request.getParameter("name");
            if (name != null) {
                out.print(" value=\"" + name + "\"");
            }
            out.println("/></td>");
            out.println("<td><label for=\"designer\">Designer: </label></td>");
            out.print("<td><input type=\"text\" id=\"designer\"");
            String designer = request.getParameter("designer");
            if (designer != null) {
                out.print(" value=\"" + designer + "\"");
            }
            out.println("/></td>");
            out.println("<td rowspan=\"2\"><input type=\"button\" id=\"btn_search\" onclick=\"search()\"/></td>");
            out.println("</tr><tr>");
            out.println("<td></td><td colspan=\"3\">");
            out.println("<label for=\"min\">MIN Points: </label>");
            out.print("<input type=\"number\" id=\"min\"");
            String min = request.getParameter("min");
            if (min != null) {
                out.print(" value=\"" + min + "\"");
            }
            out.println("/>");
            out.println("<label for=\"max\">MAX Points: </label>");
            out.print("<input type=\"number\" id=\"max\"");
            String max = request.getParameter("max");
            if (max != null) {
                out.print(" value=\"" + max + "\"");
            }
            out.println("/>");
            out.println("</td></tr>");
            out.println("</table></center>");

            //sorting
            String sorting = request.getParameter("sorting");
            String[] selectedOption = {"", "", ""};
            String[] options = {"name", "min", "max"};
            if (sorting != null) {
                for (int i = 0; i < options.length; i++) {
                    if (sorting.equalsIgnoreCase(options[i])) {
                        selectedOption[i] = " selected=\"selected\"";
                        break;
                    }
                }
            }
            out.println("<select id=\"sorting\" onchange=\"search()\">");
            out.println("<option value=\"name\"" + selectedOption[0] + ">Sorting by Name</option>");
            out.println("<option value=\"min\"" + selectedOption[1] + ">Sorting from MIN Points</option>");
            out.println("<option value=\"max\"" + selectedOption[2] + ">Sorting from MAX Points</option>");
            out.println("</select>");
        %>
    </div>
    <div class="bonus_content_container">
        <div id="item_detail_container">
            <div id="detail_img"></div>
            <div id="item_detail">
                <table id="item_detail_table">
                    <tr><td>Name:</td><td><span id="detail_name"/></td></tr>
                    <tr><td>Bonus Point Needed:</td><td><span id="detail_bonus"/></td></tr>
                    <tr><td>Designer:</td><td><span id="detail_designer"/></td></tr>
                    <tr><td>Description:</td><td><span id="detail_description"/></td></tr>
                </table>
                <br/>
                <div id="checkout_detail">
                    <form method="post" action="bonus" oninput="total.value=parseInt(quantity.value)*parseInt(cbonus.value)">
                        <input type="hidden" name="action" value="checkout"/>
                        <input type="hidden" name="cbonus" id="cbonus"/>
                        <input type="hidden" name="accessoryID" id="accessoryID"/>
                        <table>
                            <tr>
                                <td><label for="quantity">Quantity:</label></td>
                                <td><input id="quantity" type="number" name="quantity" value="1" min="1"/></td>
                            </tr>
                            <tr>
                                <td>Total Points:</td>
                                <td><output name="total" id="total" for="quantity cbonus"></output></td>
                            </tr>
                            <tr>
                                <td><label for="type">Type:</label></td>
                                <td>
                                    <select id="type" name="type" onchange="checkType()">
                                        <option value="<%=RedemptionBean.TYPE_SELF_PICK_UP%>">Self-pick up</option>
                                        <option selected="selected" value="<%=RedemptionBean.TYPE_DELIVERY%>">Delivery</option>
                                    </select>
                                </td>
                            </tr>
                            <tr id="addressRow">
                                <td><label for="address">Address:</label></td>
                                <td><input type="text" id="address" name="address" value="<%=request.getAttribute("address")%>"/></td>
                            </tr>
                        </table>
                        <input type="submit" value="Check Out"/>
                    </form>
                </div>
            </div>
        </div>
        <div class="items_container">
            <%
                if (gifts != null) {
                    for (int i = 0; i < gifts.size(); i++) {
                        AccessoryBean curr = gifts.get(i);
                        out.println("<div class=\"accessory_item\" onclick=\"select('" + curr.getAccessoryID() + "')\">");
                        out.println("<b>" + curr.getName() + "</b><br/>");
                        out.println("<div class=\"accessory_item_designer\">Designer:<br/>" + curr.getDesigner() + "</div>");
                        out.print("<div class=\"accessory_item_img\"");
                        out.println(" style=\"background-image:url('" + curr.getImage() + "')\"></div>");
                        out.print("Points: ");
                        double bonus = curr.getBonus();
                        if ((Double) request.getAttribute("bonus") >= bonus) {
                            out.print(bonus);
                        } else {
                            out.print("<font color=\"red\">" + bonus + "</font>");
                        }
                        if (curr.getQuantity() <= 0) {
                            out.print("<font color=\"red\" class=\"soldout\"> Sold Out</font>");
                        }
                        out.println("</div>");
                    }
                }
            %>
        </div>

    </div>
</div>
<link rel="stylesheet" type="text/css" href="css/bonusGift.css">
<script defer>
    function search() {
        var url = "bonus?action=search";
        var name = document.getElementById("name").value;
        if (name) {
            url += "&name=" + name;
        }
        var min = document.getElementById("min").value;
        if (min) {
            url += "&min=" + min;
        }
        var max = document.getElementById("max").value;
        if (max) {
            url += "&max=" + max;
        }
        var designer = document.getElementById("designer").value;
        if (designer) {
            url += "&designer=" + designer;
        }
        var sorting = document.getElementById("sorting").value;
        if (sorting) {
            url += "&sorting=" + sorting;
        }
        location.href = url;
    }
    function Accessory(accessoryID, name, designer, description, image, bonus, quantity, category) {
        this.accessoryID = accessoryID;
        this.name = name;
        this.designer = designer;
        this.description = description;
        this.image = image;
        this.bonus = bonus;
        this.quantity = quantity;
        this.category = category;
    }
    acc = [
    <%
        for (int i = 0; i < gifts.size(); i++) {
            AccessoryBean curr = gifts.get(i);
            out.print("new Accessory(\"");
            out.print(curr.getAccessoryID() + "\",\"");
            out.print(curr.getName() + "\",\"");
            out.print(curr.getDesigner() + "\",\"");
            out.print(curr.getDescription() + "\",\"");
            out.print(curr.getImage() + "\",");
            out.print(curr.getBonus() + ",");
            out.print(curr.getQuantity());
            out.print(")");
            if (i < gifts.size() - 1) {
                out.println(",");
            }
        }
    %>
    ];
    function select(id) {
        var a = null;
        for (var i = 0; i < acc.length; i++) {
            if (acc[i].accessoryID === id) {
                a = acc[i];
                break;
            }
        }
        if (a === null) {
            return;
        } else {
            document.getElementById("item_detail_container").style.maxHeight = "1000px";
        }

        document.getElementById("detail_img").style.backgroundImage = "url('" + a.image + "')";
        document.getElementById("detail_name").innerHTML = a.name;
        document.getElementById("detail_bonus").innerHTML = a.bonus;
        document.getElementById("cbonus").value = a.bonus;
        document.getElementById("quantity").value = 1;
        document.getElementById("total").innerHTML = "" + a.bonus;
        document.getElementById("detail_designer").innerHTML = a.designer;
        document.getElementById("detail_description").innerHTML = a.description;
        if (a.quantity <= 0) {
            document.getElementById("checkout_detail").style.display = "none";
        } else {
            document.getElementById("checkout_detail").style.display = "inherit";
            document.getElementById("accessoryID").value = a.accessoryID;
        }
    }
    function checkType() {
        var row = document.getElementById("addressRow");
        if (document.getElementById("type").value === "<%=RedemptionBean.TYPE_DELIVERY%>") {
            row.style.visibility = "visible";
        } else {
            row.style.visibility = "hidden";
        }
    }
</script>
<jsp:include page="common/footer.jsp" />