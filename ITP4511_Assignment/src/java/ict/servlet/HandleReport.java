/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.beans.OrdersBean;
import ict.beans.ReportBean;
import ict.db.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sun
 */
@WebServlet(name = "HandleReport", urlPatterns = {"/handleReport"})
public class HandleReport extends HttpServlet {

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
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        Database db = new Database(dbUrl, dbUser, dbPassword);

        ArrayList<OrdersBean> allCanceled = db.queryAllOrders();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        request.setAttribute("test", month);
        ArrayList<ReportBean> reports = new ArrayList();
        ReportBean tempReport;
        ArrayList<OrdersBean> tempOrder;
        int total,incomplete;
        
        for (int i = 0; i < 10; i++) {
            tempReport = new ReportBean();
            tempOrder=new ArrayList();
            total=0;
            incomplete=0;
            if (month == 0) {
                year -= 1;
                month = 12;
            }
            tempReport.setMonth(month);
            tempReport.setYear(year);
            for(int j=0;j<allCanceled.size();j++){
                cal.setTime(allCanceled.get(j).getOrderDateTime());               
                if(cal.get(Calendar.YEAR)==year && cal.get(Calendar.MONTH)+1==month && allCanceled.get(j).getStatus().equals("canceled")){
                    tempOrder.add(allCanceled.get(j));
                    incomplete++;
                    total++;
                }else if(cal.get(Calendar.YEAR)==year && cal.get(Calendar.MONTH)+1==month){
                    total++;
                }
            }
            tempReport.setIncomplete(incomplete);
            tempReport.setTotalOrder(total);
            tempReport.setContent(tempOrder);
            reports.add(tempReport);
            month--;
        }

        request.setAttribute("reports", reports);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/report.jsp");
        rd.forward(request, response);
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
