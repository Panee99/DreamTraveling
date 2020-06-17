/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oiog.dreamtraveling.daos.UserDAO;
import oiog.dreamtraveling.dtos.UserDTO;
import oiog.dreamtraveling.utils.Tool;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author hoang
 */
public class RegisterController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterController.class);

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
        JSONObject err = new JSONObject();
        JSONObject resMsg = new JSONObject();
        try {
            /*=== get param ===*/
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");
            String name = request.getParameter("name");
            /*=== validate ===*/
            if (username.isEmpty()) {
                err.put("username", "Username is required");
            } else if (!Tool.check(username, UserDTO.REGEX_USERNAME)) {
                err.put("username", UserDTO.WARNING_VALID_USERNAME);
            }

            if (password.isEmpty()) {
                err.put("password", "Password is required");
            } else if (!Tool.check(password, UserDTO.REGEX_PASSWORD)) {
                err.put("password", UserDTO.WARNING_VALID_PASSWORD);
            }

            if (repassword.isEmpty()) {
                err.put("repassword", "RePassword is required");
            } else if (!repassword.equals(password)) {
                err.put("repassword", "Password and RePassword must be same");
            }

            if (name.isEmpty()) {
                err.put("name", "Name is required");
            } else if (!Tool.check(name, UserDTO.REGEX_FULLNAME)) {
                err.put("name", UserDTO.WARNING_VALID_FULLNAME);
            }

            if (err.length() > 0) {
                resMsg.put("error", err);
            } else {
                UserDAO dao = new UserDAO();
                String encryptedPassword = UserDTO.getValidSHA256Password(password);
                if (dao.register(username, encryptedPassword, name)) {
                    resMsg.put("message", "Created your account. Please login to booking.");
                } else {
                    resMsg.put("error", "Server busy please try again");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                resMsg.put("error", "Username is existed");
            } else {
                LOGGER.error(e.getMessage());
                resMsg.put("error", "Server busy please try again");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resMsg.put("error", "Server busy please try again");
        } finally {
            out.print(resMsg.toString());
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
