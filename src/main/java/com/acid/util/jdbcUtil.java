package com.acid.util;

import java.sql.*;

/**
 * @author R机械223 李明阳
 * java连接数据库的工具类，另有关闭连接并释放资源的方法实现
 */
public class jdbcUtil {
    public static Connection ConnectDB(String DBName,String UserName,String Password) {
        Connection con = null;
        String uri = "jdbc:mysql://172.18.204.134:3306/movielist"
                + "?autoReconnect=true&useUnicode=true&useSSL=false"
                + "&characterEncoding=utf-8&serverTimezone=UTC";
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
