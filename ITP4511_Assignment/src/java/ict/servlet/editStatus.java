/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.beans.*;
import ict.db.Database;
import java.io.IOException;
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

/**
 *
 * @author 1
 */
@WebServlet(name = "editStatus", urlPatterns = {"/editStatus"})
public class editStatus extends HttpServlet {

    private Database db;

    @Override
    public void init()
            throws ServletException {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        db = new Database(dbUrl, dbUser, dbPassword);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if ("search".equalsIgnoreCase(action)) {
            String clientID = request.getParameter("clientID");
            ArrayList<OrdersBean> orders = db.queryOrdersAvailable(clientID);
            request.setAttribute("orders", orders);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/manageOrderStatus.jsp");
            rd.forward(request, response);
        } else if ("edit".equalsIgnoreCase(action)) {
            String orderID = request.getParameter("id");
            OrdersBean order = db.queryOrdersByID(orderID);
            request.setAttribute("order", order);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/manageOrderStatus.jsp");
            rd.forward(request, response);
        } else if ("confirm".equalsIgnoreCase(action)) {
            try {
                String orderID = request.getParameter("orderID");
                String orderStatus = request.getParameter("orderStatus");
                if ((orderStatus.equals("delay picked-up")||orderStatus.equals("available pick up")||orderStatus.equals("delivered")) && request.getParameter("pickupDateTime") != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateInString = request.getParameter("pickupDateTime");
                    Date date = formatter.parse(dateInString);
                    db.editOrderPickupDateTime(orderID, date);
                } else if (orderStatus.equals("picked-up") || orderStatus.equals("canceled")) {
                    db.editOrderPickupDateTime(orderID, null);
                }
                db.editOrderStatus(orderID, orderStatus);
                response.sendRedirect("manageOrderStatus.jsp");
            } catch (ParseException e) {
                e.printStackTrace();
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
