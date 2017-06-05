/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.beans.CategoryBean;
import ict.beans.ItemBean;
import ict.db.Database;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchHandler", urlPatterns = {"/search"})
public class SearchHandler extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        Database db = new Database(dbUrl, dbUser, dbPassword);

        ArrayList<ItemBean> result = new ArrayList();
        ArrayList<CategoryBean> category = db.queryAllCategory();

        String action = request.getParameter("action");
        String q = request.getParameter("q");

        if (q != null) {
            result = getItem(db.queryByItem("", q, 0, 0, "", ""));
        } else if (action == null) {
            result = getItem(db.queryAllItem());
        } else if (action.equals("sortByCategory")) {
            String c = request.getParameter("category");
            if (c != null) {
                result = getItem(db.queryByItem(c, "", 0, 0, "", ""));
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher("/search.jsp");
        request.setAttribute("result", result);
        request.setAttribute("category", category);
        rd.forward(request, response);

    }

    public ArrayList<ItemBean> getItem(ArrayList<ItemBean> ib) {
        ArrayList<ItemBean> result = new ArrayList();
        for (ItemBean i : ib) {
            if (i.getQuantity() > 0) {
                result.add(i);
            }
        }
        return result;
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
