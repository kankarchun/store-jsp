<%@page errorPage="error.jsp" %>
<%@page import="ict.beans.CategoryBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.beans.ItemBean"%>
<%@page import="ict.db.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="common/header.jsp" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css"/>
<link rel="stylesheet" href="css/bootstrap-theme.min.css"/>
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/list.css">
<%
    String dbUser = this.getServletContext().getInitParameter("dbUser");
    String dbPassword = this.getServletContext().getInitParameter("dbPassword");
    String dbUrl = this.getServletContext().getInitParameter("dbUrl");
    Database db = new Database(dbUrl, dbUser, dbPassword);
%>


<div class="content">
    <div class="container">
        <form method="get" action="item">
            <input type="hidden" name="action" value="search" />
            <div class="form-group row">
                <label for="inputName" class="col-sm-2 col-form-label">Item Name</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" placeholder="Search..." name="name">
                </div>
            </div>
            <div class="form-group row">
                <label for="inputPassword3" class="col-sm-2 col-form-label">Category Name</label>
                <div class="col-sm-10">
                    <select class="form-control" name="category">
                        <option value="" selected disabled>Please select</option>
                        <%
                            ArrayList<CategoryBean> al = db.queryAllCategory();
                            for (CategoryBean cb : al) {
                                out.print("<option value='" + cb.getCategoryID() + "'>" + cb.getCategory() + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="offset-sm-2 col-sm-12">
                    <button type="submit" class="btn btn-info btn-block">Search</button>
                </div>
            </div>
        </form>
    </div>
    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addModal">Add</button>
    <table id="table" class="table table-striped">
        <thead>
            <tr>
                <th>ID</th>
                <th>Category Name</th>
                <th>Image</th>
                <th>Item Name</th>
                <th>Designer</th>
                <th>Description</th>
                <th>Price</th>
                <th>Quantity</th>
                <th></th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <%
                ArrayList<ItemBean> items;
                if (request.getAttribute("items") == null) {
                    items = db.queryAllItem();
                } else {
                    items = (ArrayList<ItemBean>) request.getAttribute("items");
                }
                for (ItemBean ib : items) {
                    out.print("<tr>");
                    out.print("<th scope='row'>" + ib.getItemID() + "</th>");
                    out.print("<td>" + ib.getCategory().getCategory() + "</td>");
                    out.print("<td><img width='100' height='100' src='" + ib.getImage() + "'></td>");
                    out.print("<td>" + ib.getName() + "</td>");
                    out.print("<td>" + ib.getDesigner() + "</td>");
                    out.print("<td>" + ib.getDescription() + "</td>");
                    out.print("<td>" + ib.getPrice() + "</td>");
                    out.print("<td>" + ib.getQuantity() + "</td>");
                    out.println("<td><a class='btn btn-default' href=\"item?action=delete&id=" + ib.getItemID() + "\">delete</a></td>");
                    out.println("<td><a class='btn btn-default' href=\"item?action=edit&id=" + ib.getItemID() + "\">edit</a></td>");
                    out.print("</tr>");
                }
            %>
        </tbody>
    </table>
    <%
        ItemBean item = new ItemBean();
        if (request.getAttribute("edit") != null) {
            item = (ItemBean) request.getAttribute("edit");
            out.print("<script defer>$(document).ready(function () {$('#editModal').modal('show');});</script>");
        } else if (request.getAttribute("delete") != null) {
            item = (ItemBean) request.getAttribute("delete");
            out.print("<script defer>$(document).ready(function () {$('#deleteModal').modal('show');});</script>");
        }
    %>
    <div class="modal fade" id="deleteModal" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Delete Item</h4>
                </div>
                <div class="modal-body">
                    <p>Do you want to delete?</p>
                </div>
                <div class="modal-footer">
                    <a class="btn btn-success" href="item?action=confirm&id=<%=item.getItemID()%>">Yes</a>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>
                </div>
            </div>

        </div>
    </div>

    <div class="modal fade" id="addModal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Add Item</h4>
                </div>
                <div class="modal-body">
                    <form method=“get" action="handleAddItem">
                        <div class="form-group row">
                            <label for="inputPassword3" class="col-sm-2 col-form-label">Category Name</label>
                            <div class="col-sm-10">
                                <select class="form-control" name="category" required>
                                    <%
                                        ArrayList<CategoryBean> al2 = db.queryAllCategory();
                                        for (CategoryBean cb : al2) {
                                            out.print("<option value='" + cb.getCategoryID() + "'>" + cb.getCategory() + "</option>");
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Item Name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="name" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Designer</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="designer" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Description</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="description" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Price</label>
                            <div class="col-sm-10">
                                <input type="number" step="0.01" min="0" class="form-control" name="price" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Quantity</label>
                            <div class="col-sm-10">
                                <input type="number" class="form-control" name="quantity" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Image</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="image" value="" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-12">
                                <button type="submit" class="btn btn-primary btn-block">Save</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editModal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Edit Item</h4>
                </div>
                <div class="modal-body">
                    <form method=“get" action="handleEdit">
                        <input type="hidden" name="itemID" value="<%=item.getItemID()%>">
                        <div class="form-group row">
                            <label for="inputPassword3" class="col-sm-2 col-form-label">Category Name</label>
                            <div class="col-sm-10">
                                <select class="form-control" name="category">
                                    <%
                                        if (request.getAttribute("edit") != null) {
                                            ArrayList<CategoryBean> al1 = db.queryAllCategory();
                                            for (CategoryBean cb : al1) {
                                                if (item.getCategory().getCategory().equals(cb.getCategory())) {
                                                    out.print("<option value='" + cb.getCategoryID() + "' selected>" + cb.getCategory() + "</option>");
                                                } else {
                                                    out.print("<option value='" + cb.getCategoryID() + "'>" + cb.getCategory() + "</option>");
                                                }
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Item Name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="name" value="<%=item.getName()%>">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Designer</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="designer" value="<%=item.getDesigner()%>">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Description</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="description" value="<%=item.getDescription()%>">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Price</label>
                            <div class="col-sm-10">
                                <input type="number" step="0.01" class="form-control" min="0" name="price" value="<%=item.getPrice()%>">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputName" class="col-sm-2 col-form-label">Quantity</label>
                            <div class="col-sm-10">
                                <input type="number" class="form-control" name="quantity" value="<%=item.getQuantity()%>">
                            </div>
                        </div>
                        <input type="hidden" name="image" value="<%=item.getImage()%>">
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-12">
                                <button type="submit" class="btn btn-primary btn-block">Save</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="common/footer.jsp" />