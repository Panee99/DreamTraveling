/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oiog.dreamtraveling.daos.UserDAO;
import oiog.dreamtraveling.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class LoginController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

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
        String responseMessage = null;
        PrintWriter out = response.getWriter();
        try {
            /*=== get param ===*/
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            /*=== encrypt password (SHA256) ===*/
            String encryptPassword = UserDTO.getValidSHA256Password(password);
            /*=== check login ===*/
            UserDAO dao = new UserDAO();
            UserDTO user = dao.checkLogin(username, encryptPassword);
            if (user == null) {
                responseMessage = "{\"error\":\"Username or Password incorrect\"}";
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                responseMessage = "{\"action\":\"refresh\"}";
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            out.print(responseMessage);
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
