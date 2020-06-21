/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author hoang
 */
public class UpdateFromCartController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateFromCartController.class);

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
        String id = request.getParameter("id");
        String amount = request.getParameter("amount");
        PrintWriter out = response.getWriter();
        int resCode = 200;
        String resMsg = null;
        try {
            int idInt = Integer.parseInt(id);
            int amountInt = Integer.parseInt(amount);
            HttpSession session = request.getSession();
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            cart.put(idInt, amountInt);
        } catch (NumberFormatException e) {
            resCode = 400;
            resMsg = "Invalid params";
        } catch (Exception e) {
            resCode = 500;
            resMsg = "Server busy please try again";
            LOGGER.error(e.getMessage());
        } finally {
            if (resCode == 200) {
                out.print(new JSONObject());
            } else {
                response.setStatus(resCode);
                out.print(resMsg);
            }
            out.flush();
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
