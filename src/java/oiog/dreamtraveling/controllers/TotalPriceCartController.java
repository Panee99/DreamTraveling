/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oiog.dreamtraveling.daos.DiscountDAO;
import oiog.dreamtraveling.daos.TourDAO;
import oiog.dreamtraveling.dtos.TourDTO;
import oiog.dreamtraveling.dtos.UserDTO;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author hoang
 */
public class TotalPriceCartController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TotalPriceCartController.class);

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
        PrintWriter out = response.getWriter();
        int resCode = 200;
        String resMsg = null;
        String discountCode = request.getParameter("code");
        JSONObject resJson = new JSONObject();
        try {
            /*=== calc total price ===*/
            HttpSession session = request.getSession();
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            StringJoiner joiner = new StringJoiner(",");
            cart.forEach((k, v) -> {
                joiner.add(k.toString());
            });
            String ids = joiner.toString();
            TourDAO dao = new TourDAO();
            Map<Integer, TourDTO> viewCart = dao.getTourInfoForViewCart(ids);

            int totalPrice = 0;
            totalPrice = cart.entrySet().stream().map((entry) -> viewCart.get(entry.getKey()).getPrice() * entry.getValue()).reduce(totalPrice, Integer::sum);

            /*=== calc discount ===*/
            if (discountCode != null) {
                /*=== get userid ===*/
                UserDTO user = (UserDTO) session.getAttribute("user");

                /*=== check code ===*/
                DiscountDAO discountDao = new DiscountDAO();
                int[] info = discountDao.CheckDiscount(user.getUsername(), discountCode);
                if (info != null) {
                    if (info[1] == 1) {
                        /*=== is percent ===*/
                        totalPrice = totalPrice - (int) Math.ceil((float) totalPrice * info[0] / 100);
                    } else {
                        /*=== is price ===*/
                        totalPrice = totalPrice - info[0];
                    }
                    resJson.put("code", discountCode);
                    session.setAttribute("discount_code", discountCode);
                }
            }else{
                session.removeAttribute("discount_code");
            }
            session.setAttribute("total_price", totalPrice);
            resJson.put("price", totalPrice);
            resMsg = resJson.toString();
        } catch (Exception e) {
            resCode = 500;
            resMsg = "Server busy please try again";
            LOGGER.error(e.getMessage());
        } finally {
            response.setStatus(resCode);
            out.print(resMsg);
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
