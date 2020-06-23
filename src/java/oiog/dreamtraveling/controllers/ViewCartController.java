/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oiog.dreamtraveling.daos.BookingDAO;
import oiog.dreamtraveling.daos.TourDAO;
import oiog.dreamtraveling.dtos.TourDTO;
import oiog.dreamtraveling.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class ViewCartController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ViewCartController.class);
    private static final String VIEW_CART_P = "viewcart.jsp";
    private static final String CHECKOUT_SUCCESSFUL = "checkout_successful.jsp";

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
        boolean isCheckout = request.getAttribute("checkout") != null;
        String url = VIEW_CART_P;
        try {
            Map<Integer, TourDTO> viewCart = null;
            HttpSession session = request.getSession();
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart != null && !cart.isEmpty()) {
                viewCart = new HashMap();
                StringJoiner joiner = new StringJoiner(",");
                cart.forEach((k, v) -> {
                    joiner.add(k.toString());
                });
                String ids = joiner.toString();
                TourDAO dao = new TourDAO();
                viewCart = dao.getTourInfoForViewCart(ids);
                Map<Integer, Integer> overQuantity = new HashMap();
                viewCart.forEach((k, v) -> {
                    int leftQuantity = v.getQuantity();
                    int cartAmount = cart.get(k);
                    v.setQuantity(cartAmount);
                    if (leftQuantity < cartAmount) {
                        overQuantity.put(k, leftQuantity);
                    }
                });
                request.setAttribute("view_cart", viewCart);
                request.setAttribute("over_quantity", overQuantity);
                if (isCheckout && overQuantity.isEmpty()) {
                    Integer totalPrice = (Integer) session.getAttribute("total_price");
                    String discountCode = (String) session.getAttribute("discount_code");
                    if (totalPrice != null) {
                        /*=== do checkout ===*/
                        UserDTO user = (UserDTO) session.getAttribute("user");
                        /*=== join data ===*/
                        String cartToString;
                        List<String> once = new ArrayList();
                        cart.forEach((k, v) -> {
                            once.add(k + "-" + v);
                        });
                        cartToString = String.join(",", once);
                        BookingDAO bookingDao = new BookingDAO();
                        if (bookingDao.checkout(cart, user.getUsername(), discountCode, totalPrice)) {
                            url = CHECKOUT_SUCCESSFUL;
                            session.removeAttribute("cart");
                            session.removeAttribute("total_price");
                            session.removeAttribute("discount_code");
                            session.removeAttribute("view_cart");
                            session.removeAttribute("over_quantity");
                        } else {
                            request.setAttribute("error_checkout", "Checkout fail");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            request.setAttribute("error", "Server busy please try again");
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
