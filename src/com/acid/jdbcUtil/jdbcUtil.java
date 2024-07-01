package com.acid.jdbcUtil;

import java.sql.*;

/**
 * @author 49454
 * java连接数据库的工具类，另有关闭连接并释放资源的方法实现
 */
public class jdbcUtil {
    public static Connection ConnectDB(String DBName,String UserName,String Password) {
        Connection con = null;
        String uri = "jdbc:mysql://localhost:3306/" + DBName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("无法驱动mySQL");
        }
        try{
            con = DriverManager.getConnection(uri,UserName,Password);
        } catch (SQLException e) {
            System.out.println("无法连接mySQL");
        }
        return con;
    }
    public static void result(Connection con, PreparedStatement pstmt, ResultSet rs) {

        if (con != null) {

            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            con = null;
        }

        if (pstmt != null) {

            try {
                pstmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pstmt = null;
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rs = null;
        }

    }

}
