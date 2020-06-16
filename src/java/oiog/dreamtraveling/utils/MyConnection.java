/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author hoang
 */
public class MyConnection implements Serializable {

    public static Connection getConnection() throws SQLException, NamingException {
        Connection conn = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/DBConn");
            conn = ds.getConnection();
        } finally {
        }
        return conn;
    }
}
