/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.daos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import oiog.dreamtraveling.dtos.TourDTO;
import oiog.dreamtraveling.utils.MyConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class TourDAO {

    private static final Logger LOGGER = Logger.getLogger(TourDAO.class);

    private Connection conn;
    private CallableStatement caStmt;
    private ResultSet rs;

    private void closeConn() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (caStmt != null) {
                caStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    public int getTourInfoForHomePageLength(String name, Date fromDate, Date toDate, Integer fromPrice, Integer toPrice, int minQuantity, boolean fromNowOn) throws SQLException, NamingException {
        int result = 0;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL GetToursInfoForHomeLength (?, ?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setNString(1, name);
            caStmt.setDate(2, fromDate);
            caStmt.setDate(3, toDate);
            caStmt.setObject(4, fromPrice, java.sql.Types.INTEGER);
            caStmt.setObject(5, toPrice, java.sql.Types.INTEGER);
            caStmt.setInt(6, minQuantity);
            caStmt.setBoolean(7, fromNowOn);
            boolean hasResultSet = caStmt.execute();
            if (hasResultSet) {
                rs = caStmt.getResultSet();
                if (rs.next()) {
                    result = rs.getInt("length");
                }
            }
        } finally {
            closeConn();
        }
        return result;
    }

    public Map<Integer, TourDTO> getTourInfoForHomePage(String name, Date fromDate, Date toDate, Integer fromPrice, Integer toPrice, int minQuantity, boolean fromNowOn, int page, int rpp) throws SQLException, NamingException {
        Map<Integer, TourDTO> listTour = new HashMap();
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL GetToursInfoForHome (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setNString(1, name);
            caStmt.setDate(2, fromDate);
            caStmt.setDate(3, toDate);
            caStmt.setObject(4, fromPrice, java.sql.Types.INTEGER);
            caStmt.setObject(5, toPrice, java.sql.Types.INTEGER);
            caStmt.setInt(6, minQuantity);
            caStmt.setBoolean(7, fromNowOn);
            caStmt.setObject(8, page, java.sql.Types.INTEGER);
            caStmt.setObject(9, rpp, java.sql.Types.INTEGER);
            boolean hasResultSet = caStmt.execute();
            if (hasResultSet) {
                rs = caStmt.getResultSet();
                while (rs.next()) {
                    TourDTO tour = new TourDTO();
                    tour.setId(rs.getInt("id"));
                    tour.setName(rs.getNString("Name"));
                    tour.setReview(rs.getNString("review"));
                    tour.setPrice(rs.getInt("price"));
                    tour.setQuantity(rs.getInt("quantity"));
                    tour.setImage(rs.getString("image"));
                    tour.setFromDate(rs.getDate("fromDate"));
                    tour.setToDate(rs.getDate("toDate"));
                    listTour.put(tour.getId(), tour);
                }
            }
        } finally {
            closeConn();
        }
        return listTour;
    }

    public boolean newTour(String name, String review, int price, int quantity, String image, Date fromDate, Date toDate) throws SQLException, NamingException {
        boolean created = false;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL AddTour (?, ?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setString(1, name);
            caStmt.setString(2, review);
            caStmt.setInt(3, price);
            caStmt.setInt(4, quantity);
            caStmt.setString(5, image);
            caStmt.setDate(6, fromDate);
            caStmt.setDate(7, toDate);
            caStmt.execute();
            created = caStmt.getUpdateCount() > 0;
        } finally {
            closeConn();
        }
        return created;
    }

    public TourDTO getTourByID(int id) throws SQLException, NamingException {
        TourDTO tour = null;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL GetTourByID (?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setInt(1, id);
            boolean hasResultSet = caStmt.execute();
            if (hasResultSet) {
                rs = caStmt.getResultSet();
                if (rs.next()) {
                    tour = new TourDTO();
                    tour.setId(rs.getInt("id"));
                    tour.setName(rs.getNString("name"));
                    tour.setReview(rs.getNString("review"));
                    tour.setFromDate(rs.getDate("fromDate"));
                    tour.setToDate(rs.getDate("toDate"));
                    tour.setPrice(rs.getInt("price"));
                    tour.setQuantity(rs.getInt("quantity"));
                    tour.setImage(rs.getString("image"));
                }
            }
        } finally {
            closeConn();
        }
        return tour;
    }

    public boolean deactiveTourByID(int id) throws SQLException, NamingException {
        boolean deactived = false;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL DeactiveTourByID (?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setInt(1, id);
            caStmt.execute();
            deactived = caStmt.getUpdateCount() > 0;
        } finally {
            closeConn();
        }
        return deactived;
    }

    public boolean deleteImageByID(int id, String uploadPath) throws SQLException, NamingException {
        boolean deleted = false;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL getImageOfTour (?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setInt(1, id);
            boolean hasRsSet = caStmt.execute();
            if (hasRsSet) {
                rs = caStmt.getResultSet();
                if (rs.next()) {
                    String imgPath = rs.getString("image");
                    deleted = deleteImage(imgPath, uploadPath);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("One tour had invalid image");
        } finally {
            closeConn();
        }
        return deleted;
    }

    private boolean deleteImage(String filePath,String uploadPath) throws IOException {
        String[] imageNamePart = filePath.split("/");
        String fileName = imageNamePart[imageNamePart.length - 1];
        File file = new File(uploadPath + fileName);
        return Files.deleteIfExists(file.toPath());
    }

    public boolean updateTour(int id, String name, String review, int price, int quantity, String image, Date fromDate, Date toDate) throws SQLException, NamingException {
        boolean updated = false;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL UpdateTour (?, ?, ?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setInt(1, id);
            caStmt.setString(2, name);
            caStmt.setString(3, review);
            caStmt.setInt(4, price);
            caStmt.setInt(5, quantity);
            caStmt.setObject(6, image, java.sql.Types.VARCHAR);
            caStmt.setDate(7, fromDate);
            caStmt.setDate(8, toDate);
            caStmt.execute();
            updated = caStmt.getUpdateCount() > 0;
        } finally {
            closeConn();
        }
        return updated;
    }
    
}
