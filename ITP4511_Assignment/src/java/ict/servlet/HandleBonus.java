/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.beans.*;
import ict.db.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Pang
 */
@WebServlet(name = "HandleBonus", urlPatterns = {"/bonus"})
public class HandleBonus extends HttpServlet {

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
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("client");
        if (id == null) {
            response.sendRedirect("login.jsp");
        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "search";
        }

        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        Database db = new Database(dbUrl, dbUser, dbPassword);

        ClientBean client = db.queryCustomerByID(id);
        request.setAttribute("bonus", client.getBonus());

        String url;
        RequestDispatcher view;
        if (action.equalsIgnoreCase("search")) {
            url = "bonusGift.jsp";
            String name = request.getParameter("name");
            Double min = -1.0;
            try {
                min = Double.parseDouble(request.getParameter("min"));
            } catch (Exception e) {
            }
            Double max = -1.0;
            try {
                max = Double.parseDouble(request.getParameter("max"));
            } catch (Exception e) {
            }
            String designer = request.getParameter("designer");
            String sorting = "name asc";
            String s = request.getParameter("sorting");
            if (s != null) {
                if (s.equalsIgnoreCase("name")) {
                    sorting = "name asc";
                } else if (s.equalsIgnoreCase("min")) {
                    sorting = "bonus asc";
                } else if (s.equalsIgnoreCase("max")) {
                    sorting = "bonus desc";
                }
            }
            ArrayList<AccessoryBean> gifts;
            gifts = db.queryByAccessory(name, min, max, designer, sorting);
            request.setAttribute("gifts", gifts);
            request.setAttribute("address", client.getAddress());
        } else if (action.equalsIgnoreCase("history")) {
            url = "bonusHistory.jsp";
            ArrayList<RedemptionBean> records = db.queryRedeem(id);
            request.setAttribute("records", records);
        } else if (action.equalsIgnoreCase("checkout")) {
            String accessoryID = request.getParameter("accessoryID");
            AccessoryBean abean = db.queryByAccessoryID(accessoryID);
            int quantity = 0;
            try {
                quantity = Integer.parseInt(request.getParameter("quantity"));
            } catch (Exception e) {
            }
            String type = request.getParameter("type");
            //check type
            if(type == null || !type.equals(RedemptionBean.TYPE_DELIVERY)&&!type.equals(RedemptionBean.TYPE_SELF_PICK_UP)){
                request.setAttribute("error_msg", "Sorry, you have NOT input type.");
                url = "bonusCheckoutError.jsp";
                view = request.getRequestDispatcher("/" + url);
                view.forward(request, response);
                return;
            }
            String address = request.getParameter("address").trim();
            //check address
            if (type.equals(RedemptionBean.TYPE_DELIVERY) && (address == null || address.equals(""))) {
                request.setAttribute("error_msg", "Sorry, you have NOT input address.");
                url = "bonusCheckoutError.jsp";
                view = request.getRequestDispatcher("/" + url);
                view.forward(request, response);
                return;
            }
            db.editCustomer(client.getClientID(), client.getName(), client.getTelephone(), client.getEmail(), address);
            //check input
            if (quantity < 1) {
                request.setAttribute("error_msg", "Sorry, you have NOT input quantity.");
                url = "bonusCheckoutError.jsp";
                view = request.getRequestDispatcher("/" + url);
                view.forward(request, response);
                return;
            }
            //pay bonus
            double after = client.getBonus() - abean.getBonus() * quantity;
            if (after < 0) {
                request.setAttribute("error_msg", "Sorry, you have NOT enough bonus points.");
                url = "bonusCheckoutError.jsp";
                view = request.getRequestDispatcher("/" + url);
                view.forward(request, response);
                return;
            }
            if (abean.getQuantity() < quantity) {
                request.setAttribute("error_msg", "Sorry, we have not enough " + abean.getName() + ".");
                url = "bonusCheckoutError.jsp";
                view = request.getRequestDispatcher("/" + url);
                view.forward(request, response);
                return;
            }
            db.editCustomerBonus(client.getClientID(), after);
            abean.setQuantity(abean.getQuantity() - quantity);
            db.editAccessory(abean);
            //add redeem
            RedemptionBean redeem = new RedemptionBean();
            redeem.setBonus(abean.getBonus());
            redeem.setClientID(client.getClientID());
            redeem.setDescription(abean.getDescription());
            redeem.setDesigner(abean.getDesigner());
            redeem.setImage(abean.getImage());
            redeem.setName(abean.getName());
            redeem.setQuantity(quantity);
            redeem.setRedeemedDateTime(new Timestamp(System.currentTimeMillis()));
            redeem.setStatus(RedemptionBean.STATUS_WAITING);
            redeem.setType(type);
            ArrayList<RedemptionBean> redeems = new ArrayList();
            redeems.add(redeem);
            db.addRedeem(redeems);

            response.sendRedirect("bonus?action=history");
            return;
        } else {
            response.sendRedirect("index.html");
            return;
        }
        view = request.getRequestDispatcher("/" + url);
        view.forward(request, response);
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
