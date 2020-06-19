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
import java.nio.file.Files;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import oiog.dreamtraveling.daos.TourDAO;
import oiog.dreamtraveling.dtos.TourDTO;
import oiog.dreamtraveling.utils.Tool;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author hoang
 */
public class UpdateTourController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateTourController.class);
    private static final String UPDATE_P = "admin/update.jsp";
    private static final String UPDATE_TOUR = "UpdateTourController";

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
        loadTourInfo(request, response);
    }

    private void loadTourInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "Update");
        request.setAttribute("form_action", "UpdateTour");
        String url = UPDATE_P;
        try {
            String id = request.getParameter("id");
            int idInt = Integer.parseInt(id);
            TourDAO dao = new TourDAO();
            TourDTO tour = dao.getTourByID(idInt);
            request.setAttribute("tour", tour);
        } catch (Exception e) {
            request.setAttribute("error", "Cannot get this tour");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        try {
            /*=== get param ===*/
            String id = request.getParameter("id");
            Part imageFile = request.getPart("fileTourImage");
            String name = request.getParameter("tourName");
            String date = request.getParameter("tourDate");
            String price = request.getParameter("tourPrice");
            String quantity = request.getParameter("tourQuantity");
            String review = request.getParameter("tourReview");

            /*=== data ===*/
            int priceInt = -1;
            int quantityInt = -1;
            int idInt;
            /*=== Validate ===*/
            idInt = Integer.parseInt(id);

            if (name.isEmpty()) {
                err.put("name", "Name is required");
            } else if (name.length() > 20) {
                err.put("name", "Name too long (0-20)");
            }

            if (date.isEmpty()) {
                err.put("date", "Date is required");
            }

            if (price.isEmpty()) {
                err.put("price", "Price is required");
            } else {
                try {
                    priceInt = Integer.parseInt(price);
                } catch (NumberFormatException e) {
                    priceInt = -1;
                    err.put("price", "Price must be number");
                }
            }

            if (quantity.isEmpty()) {
                err.put("quantity", "Quantity is required");
            } else {
                try {
                    quantityInt = Integer.parseInt(quantity);
                } catch (NumberFormatException e) {
                    quantityInt = -1;
                    err.put("Quantity", "Quantity must be number");
                }
            }

            /*=== case has error ===*/
            if (err.length() > 0) {
                request.setAttribute("error_validate", err);
            } else {
                /*=== case no error ===*/
 /*=== save image ===*/
                String imagePath = null;
                if (imageFile.getSize() > 0) {
                    try {
                        String root = request.getContextPath();
                        imagePath = saveImage(imageFile, root);
                    } catch (IOException e) {
                        err.put("image", "Invalid Image");
                        request.setAttribute("error_validate", err);
                        return;
                    }
                }
                /*=== split date ===*/
                SimpleDateFormat dateFormater = Tool.DATEFORMAT;
                Date fromDate = null;
                Date toDate = null;
                if (date != null) {
                    try {
                        String[] datePart = date.split("-");
                        java.util.Date utilFromDate = dateFormater.parse(datePart[0].trim());
                        java.util.Date utilToDate = dateFormater.parse(datePart[1].trim());
                        fromDate = new Date(utilFromDate.getTime());
                        toDate = new Date(utilToDate.getTime());
                    } catch (ParseException e) {
                        fromDate = null;
                        toDate = null;
                    }
                }
                /*=== new tour ===*/
                TourDAO dao = new TourDAO();
                if (imagePath != null) {
                    dao.deleteImageByID(idInt);
                }
                if (dao.updateTour(idInt, name, review, priceInt, quantityInt, imagePath, fromDate, toDate)) {
                    resMsg.put("success", "Updated tour [" + name + "]");
                    request.setAttribute("message", resMsg);
                } else {
                    resMsg.put("error", "Tour [" + name + "] do not exit");
                    request.setAttribute("message", resMsg);
                }
            }
        } catch (NumberFormatException e) {
            resMsg.put("error", "Failed to update invalid ID");
            request.setAttribute("message", resMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resMsg.put("error", "Server busy please try again");
            request.setAttribute("message", resMsg);
        } finally {
            loadTourInfo(request, response);
        }
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
