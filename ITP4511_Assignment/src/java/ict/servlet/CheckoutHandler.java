package ict.servlet;

import ict.beans.CartBean;
import ict.beans.ItemBean;
import ict.beans.OrderItemBean;
import ict.beans.OrdersBean;
import ict.db.Database;
import ict.util.CalBonus;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CheckoutHandler", urlPatterns = {"/checkout"})
public class CheckoutHandler extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CartBean cart;

        HttpSession session = request.getSession();
        if (session.getAttribute("cart") == null) {
            cart = new CartBean();
        } else {
            cart = (CartBean) session.getAttribute("cart");
        }

        String action = request.getParameter("action");
        if (action == null) {
            Database db = new Database(this.getServletContext().getInitParameter("dbUrl"), this.getServletContext().getInitParameter("dbUser"), this.getServletContext().getInitParameter("dbPassword"));
            RequestDispatcher rd = request.getRequestDispatcher("/checkout.jsp");
            request.setAttribute("amount", db.queryCustomerByID((String) session.getAttribute("client")).getAmount());
            request.setAttribute("cart", cart);
            rd.forward(request, response);
        } else if (action.equals("success")) {
            String method = request.getParameter("method");
            if (method != null) {

                String datetime = request.getParameter("datetime");
                String address = request.getParameter("address");

                Database db = new Database(this.getServletContext().getInitParameter("dbUrl"), this.getServletContext().getInitParameter("dbUser"), this.getServletContext().getInitParameter("dbPassword"));
                double amount = db.queryCustomerByID((String) session.getAttribute("client")).getAmount();

                if (amount - cart.getSubtotal() > 0) {
                    OrdersBean ob = new OrdersBean();
                    ob.setOrderID("O0000" + (db.queryAllOrders().size() + 1));
                    ArrayList<OrderItemBean> items = new ArrayList();
                    for (ItemBean ib : cart.getItems()) {
                        OrderItemBean oib = new OrderItemBean();
                        oib.setCategoryID(ib.getCategoryID());
                        oib.setDescription(ib.getDescription());
                        oib.setDesigner(ib.getDesigner());
                        oib.setImage(ib.getImage());
                        oib.setName(ib.getName());
                        oib.setOrderID(ob.getOrderID());
                        oib.setOrderItemID(ib.getItemID());
                        oib.setPrice(ib.getPrice());
                        oib.setQuantity(cart.getQuality(ib));
                        items.add(oib);
                        ib.setQuantity(ib.getQuantity() - 1);
                        db.editItem(ib);
                    }
                    ob.setClientID((String) session.getAttribute("client"));
                    ob.setClient(db.queryCustomerByID(ob.getClientID()));
                    ob.setOrderDateTime(new Timestamp(System.currentTimeMillis()));
                    ob.setStatus(OrdersBean.STATUS_WAITING);

                    if (method.equals("selfpickup")) {
                        ob.setType(OrdersBean.TYPE_SELF_PICK_UP);
                        ob.setAddress(ob.getClient().getAddress());
                    } else if (method.equals("delivery")) {
                        ob.setType(OrdersBean.TYPE_DELIVERY);
                        ob.setAddress(address);
                        String dateString = datetime.replace("T", " ");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date = sdf.parse(dateString);
                            ob.setPickupDateTime(date);
                        } catch (ParseException ex) {
                        }
                    }

                    ob.setAmount(cart.getSubtotal());
                    ob.setItem(items);
                    db.addOrder(ob);
                    
                    double bonus =  CalBonus.calBonus(cart.getSubtotal());
                    db.editCustomerBonus(ob.getClientID(),ob.getClient().getBonus() + bonus);

                    db.editCustomerAmount(ob.getClientID(), amount - cart.getSubtotal());
                    RequestDispatcher rd = request.getRequestDispatcher("/checkout.jsp");
                    request.setAttribute("cart", cart);
                    cart = new CartBean();
                    session.setAttribute("cart", cart);
                    request.setAttribute("msg", "Pay Successful. You gain "+ bonus+" bonus points.");
                    rd.forward(request, response);
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("/checkout.jsp");
                    request.setAttribute("cart", cart);
                    request.setAttribute("msg", "Pay Fail. Your credit is not enought.");
                    rd.forward(request, response);
                }

            } else {
                response.sendRedirect("cart");
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
