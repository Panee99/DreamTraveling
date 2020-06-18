/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.daos;

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

/**
 *
 * @author hoang
 */
public class TourDAO {

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

    public int getTourInfoForHomePageLength(String name, Date fromDate, Date toDate, Integer fromPrice, Integer toPrice, int minQuantity) throws SQLException, NamingException {
        int result = 0;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL GetToursInfoForHomeLength (?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setNString(1, name);
            caStmt.setDate(2, fromDate);
            caStmt.setDate(3, toDate);
            caStmt.setObject(4, fromPrice, java.sql.Types.INTEGER);
            caStmt.setObject(5, toPrice, java.sql.Types.INTEGER);
            caStmt.setInt(6, minQuantity);
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

    public Map<Integer, TourDTO> getTourInfoForHomePage(String name, Date fromDate, Date toDate, Integer fromPrice, Integer toPrice, int minQuantity, int page, int rpp) throws SQLException, NamingException {
        Map<Integer, TourDTO> listTour = new HashMap();
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL GetToursInfoForHome (?, ?, ?, ?, ?, ?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setNString(1, name);
            caStmt.setDate(2, fromDate);
            caStmt.setDate(3, toDate);
            caStmt.setObject(4, fromPrice, java.sql.Types.INTEGER);
            caStmt.setObject(5, toPrice, java.sql.Types.INTEGER);
            caStmt.setInt(6, minQuantity);
            caStmt.setObject(7, page, java.sql.Types.INTEGER);
            caStmt.setObject(8, rpp, java.sql.Types.INTEGER);
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
}
