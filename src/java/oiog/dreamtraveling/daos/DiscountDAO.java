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
import javax.naming.NamingException;
import oiog.dreamtraveling.utils.MyConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class DiscountDAO {

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

    public int[] CheckDiscount(String userId, String code) throws SQLException, NamingException {
        int[] info = null;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL CheckDiscount (?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setString(1, userId);
            caStmt.setString(2, code);
            boolean isRsSet = caStmt.execute();
            if (isRsSet) {
                rs = caStmt.getResultSet();
                if (rs.next()) {
                    info = new int[2];
                    info[0] = rs.getInt("discount");
                    info[1] = rs.getBoolean("isPercent") ? 1 : 0;
                }
            }
        } finally {
            closeConn();
        }
        return info;
    }
}
