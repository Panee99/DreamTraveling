/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oiog.dreamtraveling.daos.TourDAO;
import oiog.dreamtraveling.dtos.TourDTO;
import oiog.dreamtraveling.utils.Tool;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class LoadHomeController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoadHomeController.class);
    private static final String ERROR_P = "error.jsp";
    private static final String HOME_P = "index.jsp";

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
        String url = ERROR_P;
        try {
            /*=== get param ===*/
            String paramName = request.getParameter("name");
            String paramDate = request.getParameter("dateRange");
            String paramPrice = request.getParameter("priceRange");
            String paramPage = request.getParameter("page");
            String paramRpp = getServletContext().getInitParameter("rpp_tour");  // rows per page

            /*=== convert data ===*/
 /*=== parse date ===*/
            SimpleDateFormat dateFormater = Tool.DATEFORMAT;

            Date fromDate = null;
            Date toDate = null;
            if (paramDate != null) {
                try {
                    String[] datePart = paramDate.split("-");
                    java.util.Date utilFromDate = dateFormater.parse(datePart[0].trim());
                    java.util.Date utilToDate = dateFormater.parse(datePart[1].trim());
                    fromDate = new Date(utilFromDate.getTime());
                    toDate = new Date(utilToDate.getTime());
                } catch (ParseException e) {
                    fromDate = null;
                    toDate = null;
                }
            }


            /*=== parse price ===*/
            Integer fromPrice = null;
            Integer toPrice = null;
            if (paramPrice != null) {
                try {
                    String[] partPrice = paramPrice.split(",");
                    fromPrice = Integer.parseInt(partPrice[0]);
                    toPrice = Integer.parseInt(partPrice[1]);
                } catch (NumberFormatException e) {
                    fromPrice = null;
                    toPrice = null;
                }
            }

            /*=== parse pagination ===*/
            int page = 1;
            int rpp = 5;
            if (paramPage != null && paramRpp != null) {
                try {
                    page = Integer.parseInt(paramPage);
                    rpp = Integer.parseInt(paramRpp);
                } catch (NumberFormatException e) {
                    page = 1;
                    rpp = 5;
                }
            }
            TourDAO dao = new TourDAO();
            int totalResult = dao.getTourInfoForHomePageLength(paramName, fromDate, toDate, fromPrice, toPrice);
            int totalPage = ((Double) Math.ceil((float) totalResult / rpp)).intValue();
            request.setAttribute("total_page", totalPage);
            request.setAttribute("page", page);

            /*=== get data ===*/
            Map< Integer, TourDTO> listTour = dao.getTourInfoForHomePage(paramName, fromDate, toDate, fromPrice, toPrice, page, rpp);
            request.setAttribute("list_tour", listTour);
            url = HOME_P;

        } catch (Exception e) {
            request.setAttribute("error", "Server error");
            LOGGER.error(e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
