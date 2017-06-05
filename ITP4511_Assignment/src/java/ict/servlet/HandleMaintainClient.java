package ict.servlet;

import ict.beans.*;
import ict.db.Database;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "HandleMaintainClient", urlPatterns = {"/maintainClient"})
public class HandleMaintainClient extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("client");
        if (id == null) {
            response.sendRedirect("login.jsp");
        }

        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        Database db = new Database(dbUrl, dbUser, dbPassword);

        String[] input = new String[4];
        String[] title = {"Name", "Delivery Address", "Telephone Number", "Email Address"};
        input[0] = request.getParameter("name").trim();
        input[1] = request.getParameter("address").trim();
        input[2] = request.getParameter("tel").trim();
        input[3] = request.getParameter("email").trim();
        boolean error = false;
        String error_msg = "Sorry, you have not input your ";
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null || input[i].equals("")) {
                if (error == false) {
                    error = true;
                    error_msg += title[i];
                }else{
                    error_msg += ", "+title[i];
                }
            }
        }
        if (error == true) {
            request.setAttribute("error_msg", error_msg + ".");
            RequestDispatcher view = request.getRequestDispatcher("/maintainClientError.jsp");
            view.forward(request, response);
        } else {
            db.editCustomer(id, input[0], input[2], input[3], input[1]);
            response.sendRedirect("information.jsp");
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
