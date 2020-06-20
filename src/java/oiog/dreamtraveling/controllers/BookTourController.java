/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
public class BookTourController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(BookTourController.class);

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
        try {
            if (id == null || amount == null) {
                response.sendError(400);
            } else {
                try {
                    int idInt = Integer.parseInt(id);
                    int amountInt = Integer.parseInt(amount);
                    String action = null;
                    HttpSession session = request.getSession();
                    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                    if (cart == null) {
                        cart = new HashMap();
                    }
                    if (cart.containsKey(idInt)) {
                        if (amountInt == 0) {
                            cart.remove(idInt);
                            action = "Removed";
                        } else {
                            int tourAmount = cart.get(idInt);
                            cart.put(idInt, tourAmount + amountInt);
                            action = "Updated";
                        }
                    } else if (amountInt > 0) {
                        cart.put(idInt, amountInt);
                        action = "Added";
                    }
                    session.setAttribute("cart", cart);
                    JSONObject resJson = new JSONObject();
                    JSONObject cartJson = new JSONObject(cart);
                    resJson.put("cart", cartJson);
                    resJson.put("action", action);
                    out.print(resJson);
                } catch (NumberFormatException e) {
                    resCode = 400;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (resCode == 200) {
                out.flush();
            } else {
                response.sendError(resCode);
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
