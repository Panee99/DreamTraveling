/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.NamingException;
import oiog.dreamtraveling.utils.MyConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class BookingDAO {

    private static final Logger LOGGER = Logger.getLogger(DiscountDAO.class);

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

    private int newBooking(String userId, String discountCode, int totalPrice) throws SQLException, NamingException {
        int id = -1;
        String sql = "{CALL NewBooking (?, ?, ?, ?)}";
        caStmt = conn.prepareCall(sql);
        caStmt.setString(1, userId);
        caStmt.setString(2, discountCode);
        caStmt.setInt(3, totalPrice);
        caStmt.registerOutParameter(4, java.sql.Types.INTEGER);
        caStmt.execute();
        if (caStmt.getUpdateCount() > 0) {
            id = caStmt.getInt(4);
        }
        return id;
    }

    public boolean checkout(Map<Integer, Integer> cart, String userID, String discountCode, int totalPrice) throws SQLException, NamingException {
        boolean saved = false;
        try {
            conn = MyConnection.getConnection();
            conn.setAutoCommit(false);
            int bookingId = newBooking(userID, discountCode, totalPrice);
            if (bookingId > 0) {
                String sql;
                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                    sql = "{CALL SaveBookingDetails (?, ?, ?, ?)}";
                    caStmt = conn.prepareCall(sql);
                    caStmt.setInt(1, bookingId);
                    caStmt.setInt(2, entry.getKey());
                    caStmt.setInt(3, entry.getValue());
                    caStmt.registerOutParameter(4, java.sql.Types.INTEGER);
                    caStmt.execute();
                    if (caStmt.getUpdateCount() > 0) {
                        int newQuantity = caStmt.getInt(4);
                        if (newQuantity < 0) {
                            conn.rollback();
                        }
                    }
                }
                conn.commit();
                if (conn.getTransactionIsolation() == Connection.TRANSACTION_READ_COMMITTED) {
                    saved = true;
                }
            }
        } finally {
            closeConn();
        }
        return saved;
    }
}
