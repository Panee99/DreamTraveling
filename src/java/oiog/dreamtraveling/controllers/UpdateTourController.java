/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author hoang
 */
public class UpdateTourController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateTourController.class);
    private static final String UPDATE_P = "admin/update.jsp";
    private static final String NEW_TOUR = "NewTourController";

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
        request.setAttribute("title", "Update");
        request.setAttribute("form_action", "UpdateTour");

        request.getRequestDispatcher(UPDATE_P).forward(request, response);
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
        JSONObject err = new JSONObject();
        JSONObject resMsg = new JSONObject();
        String url = NEW_TOUR;
//        try {
//            /*=== get param ===*/
//            Part imageFile = request.getPart("fileTourImage");
//            String name = request.getParameter("tourName");
//            String date = request.getParameter("tourDate");
//            String price = request.getParameter("tourPrice");
//            String quantity = request.getParameter("tourQuantity");
//            String review = request.getParameter("tourReview");
//        }
    }

    private String saveImage(Part imageFIle, String root) throws IOException {
        String result;
        try {
            /*=== get type ===*/
            String fileType = imageFIle.getContentType();
            switch (fileType) {
                case "image/png":
                    fileType = "png";
                    break;
                case "image/jpeg":
                    fileType = "jpg";
                    break;
            }

            InputStream is = imageFIle.getInputStream();
            BufferedImage image = ImageIO.read(is);
            if (image != null) {
                String fullPath = getServletContext().getRealPath("/uploads/");
                String name = UUID.randomUUID().toString().replace("-", "");
                File imageDir = new File(fullPath + name + "." + fileType);
                imageDir.getParentFile().mkdirs();
                ImageIO.write(image, fileType, imageDir);
                result = root + "/uploads/" + name + "." + fileType;
            } else {
                throw new IOException("Not " + fileType + " image");
            }
        } catch (IOException e) {
            throw e;
        }
        return result;
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
