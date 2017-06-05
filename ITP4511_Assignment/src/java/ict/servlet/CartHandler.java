package ict.servlet;

import ict.beans.CartBean;
import ict.beans.ItemBean;
import ict.db.Database;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CartHandler", urlPatterns = {"/cart"})
public class CartHandler extends HttpServlet {

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
            RequestDispatcher rd = request.getRequestDispatcher("/cart.jsp");
            request.setAttribute("cart", cart);
            rd.forward(request, response);
        } else if (action.equals("add")) {
            char type;

            if (session.getAttribute("client") != null) {
                String id = request.getParameter("id");
                if (id != null) {
                    Database db = new Database(this.getServletContext().getInitParameter("dbUrl"), this.getServletContext().getInitParameter("dbUser"), this.getServletContext().getInitParameter("dbPassword"));
                    ItemBean ib = db.queryByItemID(id);
                    if (ib != null && cart.contain(id) == null) {
                        cart.addItem(ib);
                        session.setAttribute("cart", cart);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/cart");
            } else if (session.getAttribute("manager") != null) {
                RequestDispatcher rd = request.getRequestDispatcher("/cart.jsp");
                request.setAttribute("cart", cart);
                request.setAttribute("msg", "cart are disabled in this role.");
                rd.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }

        } else if (action.equals("remove")) {
            String id = request.getParameter("id");
            if (id != null) {
                Database db = new Database(this.getServletContext().getInitParameter("dbUrl"), this.getServletContext().getInitParameter("dbUser"), this.getServletContext().getInitParameter("dbPassword"));
                ItemBean ib = cart.contain(id);
                if (ib != null) {
                    cart.removeItem(ib);
                    session.setAttribute("cart", cart);
                }
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        } else if (action.equals("update")) {
            String id = request.getParameter("id");
            String quantity = request.getParameter("quantity");
            if (id != null && quantity != null) {
                Database db = new Database(this.getServletContext().getInitParameter("dbUrl"), this.getServletContext().getInitParameter("dbUser"), this.getServletContext().getInitParameter("dbPassword"));
                ItemBean ib = cart.contain(id);
                if (ib != null) {
                    if (quantity.equals("")){
                        quantity="1";
                    }
                    cart.setQuality(ib, Integer.parseInt(quantity));
                    session.setAttribute("cart", cart);
                }
            }
            response.sendRedirect(request.getContextPath() + "/cart");
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
