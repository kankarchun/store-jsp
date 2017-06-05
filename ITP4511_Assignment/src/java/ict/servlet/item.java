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
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author chinese
 */
@WebServlet(name = "item", urlPatterns = {"/item"})
public class item extends HttpServlet {

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
        /* TODO output your page here. You may use following sample code. */
        String action = request.getParameter("action");
        if ("search".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            ArrayList items;
            if (!name.isEmpty() && category != null) {
                items = db.queryByItem(category, name, 0, 0, null, null);
            } else if (!name.isEmpty() && category == null) {
                items = db.queryByItem(null, name, 0, 0, null, null);
            } else if (name.isEmpty() && category != null) {
                items = db.queryByItem(category, null, 0, 0, null, null);
            } else {
                items = db.queryAllItem();
            }
            request.setAttribute("items", items);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/manageItem.jsp");
            rd.forward(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
//            db.deleteByItemID(id);
//            ArrayList items = db.queryAllItem();
            ItemBean item = db.queryByItemID(id);
            request.setAttribute("delete", item);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/manageItem.jsp");
            rd.forward(request, response);
        } else if ("edit".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
            ItemBean item = db.queryByItemID(id);
            request.setAttribute("edit", item);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/manageItem.jsp");
            rd.forward(request, response);
        }else if ("confirm".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
            db.deleteByItemID(id);
            response.sendRedirect("/ITP4511/manageItem.jsp");
//            ArrayList items = db.queryAllItem();
//            request.setAttribute("items", items);
//            RequestDispatcher rd;
//            rd = getServletContext().getRequestDispatcher("/manageItem.jsp");
//            rd.forward(request, response);
        }  else {
            PrintWriter out = response.getWriter();
            out.println("No such action!!!");
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
