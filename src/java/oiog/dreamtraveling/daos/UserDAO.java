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
import oiog.dreamtraveling.dtos.UserDTO;
import oiog.dreamtraveling.utils.MyConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class);

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

    public UserDTO checkLogin(String username, String password) throws SQLException, NamingException {
        UserDTO user = null;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL CheckLogin (?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setString(1, username);
            caStmt.setString(2, password);
            boolean isRsSet = caStmt.execute();
            if (isRsSet) {
                rs = caStmt.getResultSet();
                if (rs.next()) {
                    user = new UserDTO();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getNString("name"));
                    user.setRole(rs.getString("role"));
                }
            }
        } finally {
            closeConn();
        }
        return user;
    }

    public boolean register(String username, String password, String name) throws SQLException, NamingException {
        boolean created = false;
        try {
            conn = MyConnection.getConnection();
            String sql = "{CALL CreateUser (?, ?, ?)}";
            caStmt = conn.prepareCall(sql);
            caStmt.setString(1, username);
            caStmt.setString(2, password);
            caStmt.setNString(3, name);
            caStmt.execute();
            if (caStmt.getUpdateCount() > 0) {
                created = true;
            }
        } finally {
            closeConn();
        }
        return created;
    }

}
